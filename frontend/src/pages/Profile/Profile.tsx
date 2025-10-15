import { useEffect, useMemo, useState, useCallback } from "react";
import "./Profile.scss";
import Button from "../../components/Button/Button";

type Goal = "perder_peso" | "manter_peso" | "ganhar_peso" | "performance" | "saude";
type Activity = "sedentario" | "leve" | "moderado" | "alto" | "atleta";

type ProfileData = {
  name: string;
  weight: number | null;   // kg
  height: number | null;   // cm
  goal: Goal;
  activity: Activity;
};

export default function Profile() {
  const [data, setData] = useState<ProfileData | null>(null);

  const fetchProfile = useCallback(async () => {
    setLoading(true);
    try {
      const userId = localStorage.getItem("userId");
      if (!userId) return;

      const token = localStorage.getItem("token");
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

  const [isEditing, setIsEditing] = useState(false);
  const [loading, setLoading] = useState(false);

  // Form (modo edição)
  const [name, setName] = useState("");
  const [weight, setWeight] = useState("");
  const [height, setHeight] = useState("");
  const [goal, setGoal] = useState<Goal>("saude");
  const [activity, setActivity] = useState<Activity>("sedentario");
  const [errors, setErrors] = useState<{ name?: string; weight?: string; height?: string }>({});

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
    setErrors({});
    setIsEditing(true);
  }, [data]);

  const cancelEdit = useCallback(() => {
    setErrors({});
    setIsEditing(false);
  }, []);

  const validateForm = useCallback(() => {
    const e: { name?: string; weight?: string; height?: string } = {};
    const w = Number(weight.toString().replace(",", "."));
    const h = Number(height.toString().replace(",", "."));
    if (!name.trim()) e.name = "Obrigatório";
    if (weight === "") e.weight = "Obrigatório";
    else if (isNaN(w) || w <= 0) e.weight = "Valor inválido";
    if (height === "") e.height = "Obrigatório";
    else if (isNaN(h) || h <= 0) e.height = "Valor inválido";
    setErrors(e);
    return Object.keys(e).length === 0;
  }, [height, name, weight]);

  const submitUpdate = useCallback(async (e?: React.FormEvent) => {
    e?.preventDefault();
    if (!validateForm()) return;

    const userId = localStorage.getItem("userId");
    if (!userId) return;

    setLoading(true);
    try {
      const payload = {
        nome: name,
        alturaM: Number(height.toString().replace(",", ".")) / 100, // Convert cm to meters
        peso: Number(weight.toString().replace(",", ".")),
        nivelAtividade: activity.toUpperCase(),
        objetivoUsuario: goal.toUpperCase(),
      };
      const token = localStorage.getItem("token");
      const response = await fetch(`/api/v1/usuarios/${userId}`, {
        method: 'PATCH',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify(payload),
      });

      if (response.ok) {
        const updatedUserData = await response.json();
        setData({
          name: updatedUserData.nome,
          weight: updatedUserData.peso, // Assuming the backend sends these fields
          height: updatedUserData.alturaM * 100, // Convert meters to cm
          goal: updatedUserData.objetivoUsuario.toLowerCase(),
          activity: updatedUserData.nivelAtividade.toLowerCase(),
        });
        setIsEditing(false);
      } else {
        console.error("Erro ao atualizar perfil");
      }
    } catch (error) {
      console.error("Erro ao atualizar perfil:", error);
    } finally {
      setLoading(false);
    }
  }, [activity, goal, height, name, validateForm, weight]);

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
