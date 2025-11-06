import { useEffect, useMemo, useState, useCallback } from "react";
import "./Profile.scss";
import Button from "../../components/Button/Button";
import { jwtDecode } from "jwt-decode";

type Goal = "perder_peso" | "manter_peso" | "ganhar_peso" | "performance" | "saude";
type Activity = "sedentario" | "leve" | "moderado" | "alto" | "atleta";

type ProfileData = {
  name: string;
  weight: number | null;   // kg
  height: number | null;   // cm
  goal: Goal;
  activity: Activity;
};

interface JwtPayload {
  userId: string;
}

/**
 * A página de perfil do usuário.
 * Exibe as informações do perfil do usuário e permite que ele as edite.
 * @returns {JSX.Element} A página de perfil renderizada.
 */
export default function Profile() {
  const [data, setData] = useState<ProfileData | null>(null);

  const fetchProfile = useCallback(async () => {
    setLoading(true);
    try {
      const token = localStorage.getItem("token");
      if (!token) return;

      const decodedToken = jwtDecode<JwtPayload>(token);
      const userId = decodedToken.userId;

      const response = await fetch(`/api/v1/usuarios/${userId}`, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });
      if (response.ok) {
        const userData = await response.json();
        setData({
          name: userData.nome,
          weight: userData.peso, // Assuming the backend sends these fields
          height: userData.alturaM * 100, // Convert meters to cm
          goal: userData.objetivoUsuario.toLowerCase(),
          activity: userData.nivelAtividade.toLowerCase(),
        });
      } else {
        console.error("Erro ao buscar perfil");
      }
    } catch (error) {
      console.error("Erro ao buscar perfil:", error);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchProfile();
  }, [fetchProfile]);

  const fetchNutritionalGoals = useCallback(async () => {
    try {
      const token = localStorage.getItem("token");
      if (!token) return;

      const decodedToken = jwtDecode<JwtPayload>(token);
      const userId = decodedToken.userId;

      const response = await fetch(`/api/v1/usuarios/${userId}/metas?tipo=DIARIA`, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });
      if (response.ok) {
        const goalsData = await response.json();
        if (goalsData && goalsData.length > 0) {
          setNutritionalGoals(goalsData[0]);
        }
      } else {
        console.error("Erro ao buscar metas nutricionais");
      }
    } catch (error) {
      console.error("Erro ao buscar metas nutricionais:", error);
    }
  }, []);

  useEffect(() => {
    fetchProfile();
    fetchNutritionalGoals();
  }, [fetchProfile, fetchNutritionalGoals]);

  const [isEditing, setIsEditing] = useState(false);
  const [loading, setLoading] = useState(false);

  // Form (modo edição)
  const [name, setName] = useState("");
  const [weight, setWeight] = useState("");
  const [height, setHeight] = useState("");
  const [goal, setGoal] = useState<Goal>("saude");
  const [activity, setActivity] = useState<Activity>("sedentario");
  const [errors, setErrors] = useState<{ name?: string; weight?: string; height?: string; calorias?: string; proteinas?: string; carboidratos?: string; gorduras?: string; }>({});

  // Nutritional Goals state
  const [nutritionalGoals, setNutritionalGoals] = useState<any>(null);
  const [calorias, setCalorias] = useState("");
  const [proteinas, setProteinas] = useState("");
  const [carboidratos, setCarboidratos] = useState("");
  const [gorduras, setGorduras] = useState("");

  // IMC (modo visualização)
  const bmiView = useMemo(() => {
    if (!data || !data.weight || !data.height) return null;
    const h = data.height / 100;
    const v = data.weight / (h * h);
    return isFinite(v) ? v.toFixed(1) : null;
  }, [data]);

  const startEdit = useCallback(() => {
    if (!data) return;
    setName(data.name);
    setWeight(data.weight?.toString() ?? "");
    setHeight(data.height?.toString() ?? "");
    setGoal(data.goal);
    setActivity(data.activity);
    if (nutritionalGoals) {
      setCalorias(nutritionalGoals.caloriasObjetivo.toString());
      setProteinas(nutritionalGoals.proteinasObjetivo.toString());
      setCarboidratos(nutritionalGoals.carboidratosObjetivo.toString());
      setGorduras(nutritionalGoals.gordurasObjetivo.toString());
    }
    setErrors({});
    setIsEditing(true);
  }, [data, nutritionalGoals]);

  const cancelEdit = useCallback(() => {
    setErrors({});
    setIsEditing(false);
  }, []);

  const validateForm = useCallback(() => {
    const e: { name?: string; weight?: string; height?: string; calorias?: string; proteinas?: string; carboidratos?: string; gorduras?: string; } = {};
    const w = Number(weight.toString().replace(",", "."));
    const h = Number(height.toString().replace(",", "."));
    const cal = Number(calorias.toString().replace(",", "."));
    const p = Number(proteinas.toString().replace(",", "."));
    const carb = Number(carboidratos.toString().replace(",", "."));
    const gord = Number(gorduras.toString().replace(",", "."));

    if (!name.trim()) e.name = "Obrigatório";
    if (weight === "") e.weight = "Obrigatório";
    else if (isNaN(w) || w <= 0) e.weight = "Valor inválido";
    if (height === "") e.height = "Obrigatório";
    else if (isNaN(h) || h <= 0) e.height = "Valor inválido";

    if (calorias === "") e.calorias = "Obrigatório";
    else if (isNaN(cal) || cal <= 0) e.calorias = "Valor inválido";
    if (proteinas === "") e.proteinas = "Obrigatório";
    else if (isNaN(p) || p <= 0) e.proteinas = "Valor inválido";
    if (carboidratos === "") e.carboidratos = "Obrigatório";
    else if (isNaN(carb) || carb <= 0) e.carboidratos = "Valor inválido";
    if (gorduras === "") e.gorduras = "Obrigatório";
    else if (isNaN(gord) || gord <= 0) e.gorduras = "Valor inválido";

    setErrors(e);
    return Object.keys(e).length === 0;
  }, [height, name, weight, calorias, proteinas, carboidratos, gorduras]);

  const submitGoals = useCallback(async () => {
    const token = localStorage.getItem("token");
    if (!token) return;

    const decodedToken = jwtDecode<JwtPayload>(token);
    const userId = decodedToken.userId;

    const payload = {
      tipo: "DIARIA",
      caloriasObjetivo: Number(calorias.toString().replace(",", ".")),
      proteinasObjetivo: Number(proteinas.toString().replace(",", ".")),
      carboidratosObjetivo: Number(carboidratos.toString().replace(",", ".")),
      gordurasObjetivo: Number(gorduras.toString().replace(",", ".")),
      dataInicio: new Date().toISOString().split('T')[0],
      dataFim: new Date().toISOString().split('T')[0], // Not ideal, but the backend requires it.
    };

<<<<<<< HEAD
    const response = await fetch(`/api/v1/usuarios/${userId}/metas`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify(payload),
    });

    if (!response.ok) {
      throw new Error("Erro ao atualizar metas nutricionais");
    }
  }, [calorias, proteinas, carboidratos, gorduras]);

  const submitUpdate = useCallback(async (e?: React.FormEvent) => {
    e?.preventDefault();
    if (!validateForm()) return;

    setLoading(true);
    try {
      const token = localStorage.getItem("token");
      if (!token) return;

      const decodedToken = jwtDecode<JwtPayload>(token);
      const userId = decodedToken.userId;

      const profilePayload = {
        nome: name,
        alturaM: Number(height.toString().replace(",", ".")) / 100,
        peso: Number(weight.toString().replace(",", ".")),
        nivelAtividade: activity.toUpperCase(),
        objetivoUsuario: goal.toUpperCase(),
      };
      
      const profileResponse = await fetch(`/api/v1/usuarios/${userId}`, {
>>>>>>> 471a69f11f6bc087b8d60c07020869a727f9ea22
        method: 'PATCH',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify(profilePayload),
      });    }

<<<<<<< HEAD

      if (!profileResponse.ok) {
        throw new Error("Erro ao atualizar perfil");
      }

      await submitGoals();

      const updatedUserData = await profileResponse.json();
      setData({
        name: updatedUserData.nome,
        weight: updatedUserData.peso,
        height: updatedUserData.alturaM * 100,
        goal: updatedUserData.objetivoUsuario.toLowerCase(),
        activity: updatedUserData.nivelAtividade.toLowerCase(),
      });
      fetchNutritionalGoals(); // Refetch goals to display the updated values
      setIsEditing(false);
    } catch (error) {
      console.error("Erro ao atualizar perfil:", error);
    } finally {
      setLoading(false);
    }
  }, [activity, goal, height, name, validateForm, weight, submitGoals, fetchNutritionalGoals]);

  if (!data) {
    return <div className="loading-profile">Carregando perfil...</div>;
  }

  const goalLabel = (g: Goal) =>
    g === "perder_peso" ? "Perder peso" : g === "manter_peso" ? "Manter peso" : g === "ganhar_peso" ? "Ganhar peso" : g === "performance" ? "Performance" : "Saúde";
  const activityLabel = (a: Activity) => {
    switch (a) {
      case "sedentario": return "Sedentário (pouco ou nenhum exercício)";
      case "leve":     return "Leve (1–3 dias/semana)";
      case "moderado":  return "Moderado (3–5 dias/semana)";
      case "alto":   return "Intenso (6–7 dias/semana)";
      case "atleta": return "Atleta (treino intenso diário)";
    }
  };

  return (
    <main className="profile-page">
      <div className="profile-card">
        <h2>Perfil do Usuário</h2>

        {!isEditing ? (
          <>
            {/* ======= VISUALIZAÇÃO ======= */}
            <section className="info-list">
              <div className="row name-row">
                <span className="label">Nome</span>
                <span className="value user-name">{data.name}</span>
              </div>

              <div className="row">
                <span className="label">Altura (cm)</span>
                <span className="value">{data.height ?? "—"}</span>
              </div>

              <div className="row">
                <span className="label">Peso (Kg)</span>
                <span className="value">{data.weight ?? "—"}</span>
              </div>

              <div className="row">
                <span className="label">Meta</span>
                <span className="value">{goalLabel(data.goal)}</span>
              </div>

              <div className="row">
                <span className="label">Nível de atividade</span>
                <span className="value">{activityLabel(data.activity)}</span>
              </div>

              <div className="row">
                <span className="label">IMC</span>
                <span className="value">{bmiView ?? "—"}</span>
              </div>

              <div className="row">
                <span className="label">Metas Nutricionais</span>
              </div>
              <div className="row">
                <span className="label sub-label">Calorias</span>
                <span className="value">{nutritionalGoals ? `${nutritionalGoals.caloriasObjetivo} kcal` : "—"}</span>
              </div>
              <div className="row">
                <span className="label sub-label">Proteínas</span>
                <span className="value">{nutritionalGoals ? `${nutritionalGoals.proteinasObjetivo} g` : "—"}</span>
              </div>
              <div className="row">
                <span className="label sub-label">Carboidratos</span>
                <span className="value">{nutritionalGoals ? `${nutritionalGoals.carboidratosObjetivo} g` : "—"}</span>
              </div>
              <div className="row">
                <span className="label sub-label">Gorduras</span>
                <span className="value">{nutritionalGoals ? `${nutritionalGoals.gordurasObjetivo} g` : "—"}</span>
              </div>
            </section>

            <div className="footer-actions">
              <Button title="Editar" onClick={startEdit} />
            </div>
          </>
        ) : (
          <>
            {/* ======= EDIÇÃO ======= */}
            <form className="form" onSubmit={submitUpdate}>
              {/* Nome */}
              <div className="grid one">
                <div className="field">
                  <label htmlFor="name">Nome</label>
                  <input
                    id="name"
                    type="text"
                    placeholder="Ex: Ana Souza"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    disabled={loading}
                    aria-invalid={!!errors.name}
                  />
                  {errors.name && <small className="error">{errors.name}</small>}
                </div>
              </div>

              {/* Peso / Altura */}
              <div className="grid two">
                <div className="field">
                  <label htmlFor="weight">Peso (Kg)</label>
                  <input
                    id="weight"
                    type="text"
                    inputMode="decimal"
                    placeholder="Ex: 75"
                    value={weight}
                    onChange={(e) => setWeight(e.target.value)}
                    disabled={loading}
                    aria-invalid={!!errors.weight}
                  />
                  {errors.weight && <small className="error">{errors.weight}</small>}
                </div>

                <div className="field">
                  <label htmlFor="height">Altura (cm)</label>
                  <input
                    id="height"
                    type="text"
                    inputMode="decimal"
                    placeholder="Ex: 180"
                    value={height}
                    onChange={(e) => setHeight(e.target.value)}
                    disabled={loading}
                    aria-invalid={!!errors.height}
                  />
                  {errors.height && <small className="error">{errors.height}</small>}
                </div>
              </div>

              {/* Meta / Nível de atividade LADO A LADO */}
              <div className="grid two">
                <div className="field">
                  <label htmlFor="goal">Meta</label>
                  <select
                    id="goal"
                    value={goal}
                    onChange={(e) => setGoal(e.target.value as Goal)}
                    disabled={loading}
                  >
                    <option value="perder_peso">Perder peso</option>
                    <option value="manter_peso">Manter peso</option>
                    <option value="ganhar_peso">Ganhar peso</option>
                    <option value="performance">Performance</option>
                    <option value="saude">Saúde</option>
                  </select>
                </div>

                <div className="field">
                  <label htmlFor="activity">Nível de atividade</label>
                  <select
                    id="activity"
                    value={activity}
                    onChange={(e) => setActivity(e.target.value as Activity)}
                    disabled={loading}
                  >
                    <option value="sedentario">Sedentário (pouco ou nenhum exercício)</option>
                    <option value="leve">Leve (1–3 dias/semana)</option>
                    <option value="moderado">Moderado (3–5 dias/semana)</option>
                    <option value="alto">Intenso (6–7 dias/semana)</option>
                    <option value="atleta">Atleta (treino intenso diário)</option>
                  </select>
                </div>
              </div>

              {/* Nutritional Goals */}
              <div className="grid four">
                <div className="field">
                  <label htmlFor="calorias">Calorias (kcal)</label>
                  <input
                    id="calorias"
                    type="text"
                    inputMode="decimal"
                    placeholder="Ex: 2000"
                    value={calorias}
                    onChange={(e) => setCalorias(e.target.value)}
                    disabled={loading}
                    aria-invalid={!!errors.calorias}
                  />
                  {errors.calorias && <small className="error">{errors.calorias}</small>}
                </div>
                <div className="field">
                  <label htmlFor="proteinas">Proteínas (g)</label>
                  <input
                    id="proteinas"
                    type="text"
                    inputMode="decimal"
                    placeholder="Ex: 150"
                    value={proteinas}
                    onChange={(e) => setProteinas(e.target.value)}
                    disabled={loading}
                    aria-invalid={!!errors.proteinas}
                  />
                  {errors.proteinas && <small className="error">{errors.proteinas}</small>}
                </div>
                <div className="field">
                  <label htmlFor="carboidratos">Carboidratos (g)</label>
                  <input
                    id="carboidratos"
                    type="text"
                    inputMode="decimal"
                    placeholder="Ex: 250"
                    value={carboidratos}
                    onChange={(e) => setCarboidratos(e.target.value)}
                    disabled={loading}
                    aria-invalid={!!errors.carboidratos}
                  />
                  {errors.carboidratos && <small className="error">{errors.carboidratos}</small>}
                </div>
                <div className="field">
                  <label htmlFor="gorduras">Gorduras (g)</label>
                  <input
                    id="gorduras"
                    type="text"
                    inputMode="decimal"
                    placeholder="Ex: 80"
                    value={gorduras}
                    onChange={(e) => setGorduras(e.target.value)}
                    disabled={loading}
                    aria-invalid={!!errors.gorduras}
                  />
                  {errors.gorduras && <small className="error">{errors.gorduras}</small>}
                </div>
              </div>
            </form>

            <div className="footer-actions edit">
              <button
                type="button"
                className="ghost-btn"
                onClick={cancelEdit}
                disabled={loading}
              >
                Cancelar
              </button>
              <Button title={loading ? "Salvando..." : "Atualizar Dados"} onClick={submitUpdate} />
            </div>
          </>
        )}
      </div>
    </main>
  );
}
