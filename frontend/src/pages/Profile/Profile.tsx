import { useMemo, useState } from "react";
import "./Profile.scss";
import Button from "../../components/Button/Button";

type Goal = "lose" | "maintain" | "gain";
type Activity = "sedentary" | "light" | "moderate" | "intense";

type ProfileData = {
  name: string;
  weight: number | null;   // kg
  height: number | null;   // cm
  goal: Goal;
  activity: Activity;
};

export default function Profile() {
  // Mock inicial (trocar por API depois)
  const [data, setData] = useState<ProfileData>({
    name: "Maria",
    weight: 75,
    height: 180,
    goal: "lose",
    activity: "sedentary",
  });

  const [isEditing, setIsEditing] = useState(false);
  const [loading, setLoading] = useState(false);

  // Form (modo edição)
  const [name, setName] = useState(data.name);
  const [weight, setWeight] = useState(data.weight?.toString() ?? "");
  const [height, setHeight] = useState(data.height?.toString() ?? "");
  const [goal, setGoal] = useState<Goal>(data.goal);
  const [activity, setActivity] = useState<Activity>(data.activity);
  const [errors, setErrors] = useState<{ name?: string; weight?: string; height?: string }>({});

  // IMC (modo visualização)
  const bmiView = useMemo(() => {
    if (!data.weight || !data.height) return null;
    const h = data.height / 100;
    const v = data.weight / (h * h);
    return isFinite(v) ? v.toFixed(1) : null;
  }, [data]);

  function startEdit() {
    setName(data.name);
    setWeight(data.weight?.toString() ?? "");
    setHeight(data.height?.toString() ?? "");
    setGoal(data.goal);
    setActivity(data.activity);
    setErrors({});
    setIsEditing(true);
  }

  function cancelEdit() {
    setErrors({});
    setIsEditing(false);
  }

  function validateForm() {
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
  }

  async function submitUpdate(e?: React.FormEvent) {
    e?.preventDefault();
    if (!validateForm()) return;
    setLoading(true);
    try {
      const payload: ProfileData = {
        name: name.trim(),
        weight: Number(weight.toString().replace(",", ".")),
        height: Number(height.toString().replace(",", ".")),
        goal,
        activity,
      };
      console.log("[PROFILE] update payload:", payload);
      await new Promise((r) => setTimeout(r, 600)); // mock
      setData(payload);
      setIsEditing(false);
    } finally {
      setLoading(false);
    }
  }

  const goalLabel = (g: Goal) =>
    g === "lose" ? "Perder peso" : g === "maintain" ? "Manter peso" : "Ganhar peso";
  const activityLabel = (a: Activity) => {
    switch (a) {
      case "sedentary": return "Sedentário (pouco ou nenhum exercício)";
      case "light":     return "Leve (1–3 dias/semana)";
      case "moderate":  return "Moderado (3–5 dias/semana)";
      case "intense":   return "Intenso (6–7 dias/semana)";
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
                    <option value="lose">Perder peso</option>
                    <option value="maintain">Manter peso</option>
                    <option value="gain">Ganhar peso</option>
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
                    <option value="sedentary">Sedentário (pouco ou nenhum exercício)</option>
                    <option value="light">Leve (1–3 dias/semana)</option>
                    <option value="moderate">Moderado (3–5 dias/semana)</option>
                    <option value="intense">Intenso (6–7 dias/semana)</option>
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
