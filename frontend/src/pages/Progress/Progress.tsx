import { useEffect, useMemo, useState, useCallback } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import "./Progress.scss";
import Button from "../../components/Button/Button";

type TabKey = "diaria" | "semanal" | "mensal";

type Metric = {
  title: string;
  unit: "kcal" | "g";
  total: number;
  changePct: number;
  breakdown: { label: string; value: number }[];
};

type PeriodData = {
  calories: Metric;
  proteins: Metric;
  carbs: Metric;
  goals: {
    calories: { current: number; target: number; suffix: "kcal" };
    proteins: { current: number; target: number; suffix: "g" };
    carbs: { current: number; target: number; suffix: "g" };
  };
};

const TABS: { key: TabKey; label: string }[] = [
  { key: "diaria", label: "Diário" },
  { key: "semanal", label: "Semanal" },
  { key: "mensal", label: "Mensal" },
];

const initialData: Record<TabKey, PeriodData> = {
  diaria: { calories: { title: "Calorias", unit: "kcal", total: 0, changePct: 0, breakdown: [] }, proteins: { title: "Proteínas", unit: "g", total: 0, changePct: 0, breakdown: [] }, carbs: { title: "Carboidratos", unit: "g", total: 0, changePct: 0, breakdown: [] }, goals: { calories: { current: 0, target: 0, suffix: "kcal" }, proteins: { current: 0, target: 0, suffix: "g" }, carbs: { current: 0, target: 0, suffix: "g" } } },
  semanal: { calories: { title: "Calorias", unit: "kcal", total: 0, changePct: 0, breakdown: [] }, proteins: { title: "Proteínas", unit: "g", total: 0, changePct: 0, breakdown: [] }, carbs: { title: "Carboidratos", unit: "g", total: 0, changePct: 0, breakdown: [] }, goals: { calories: { current: 0, target: 0, suffix: "kcal" }, proteins: { current: 0, target: 0, suffix: "g" }, carbs: { current: 0, target: 0, suffix: "g" } } },
  mensal: { calories: { title: "Calorias", unit: "kcal", total: 0, changePct: 0, breakdown: [] }, proteins: { title: "Proteínas", unit: "g", total: 0, changePct: 0, breakdown: [] }, carbs: { title: "Carboidratos", unit: "g", total: 0, changePct: 0, breakdown: [] }, goals: { calories: { current: 0, target: 0, suffix: "kcal" }, proteins: { current: 0, target: 0, suffix: "g" }, carbs: { current: 0, target: 0, suffix: "g" } } },
};

export default function Progress() {
  const [tab, setTab] = useState<TabKey>("diaria");
  const [data, setData] = useState<Record<TabKey, PeriodData>>(initialData);
  const [loading, setLoading] = useState(true);
  const [weightHistory, setWeightHistory] = useState<any[]>([]);
  const location = useLocation();
  const navigate = useNavigate();

  const fetchWeightHistory = useCallback(async () => {
    const userId = localStorage.getItem("userId");
    if (!userId) return;
    const token = localStorage.getItem("token");
    if (!token) return;

    const today = new Date();
    const lastMonth = new Date(today);
    lastMonth.setMonth(today.getMonth() - 1);

    const start = lastMonth.toISOString().split('T')[0];
    const end = today.toISOString().split('T')[0];

    try {
      const response = await fetch(`${import.meta.env.VITE_API_URL}/api/v1/registros-peso/usuario/${userId}?start=${start}&end=${end}&_=${new Date().getTime()}`, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });
      if (response.ok) {
        const data = await response.json();
        setWeightHistory(data.sort((a: any, b: any) => new Date(a.dataMedicao).getTime() - new Date(b.dataMedicao).getTime()));
      } else {
        console.error("Erro ao buscar histórico de peso");
      }
    } catch (error) {
      console.error("Erro ao buscar histórico de peso:", error);
    }
  }, []);

  useEffect(() => {
    const userId = localStorage.getItem("userId");
    if (!userId) return;
    const token = localStorage.getItem("token");
    if (!token) return;

    async function fetchData() {
      setLoading(true);
      try {
        const headers = { 'Authorization': `Bearer ${token}` };
        const promises = TABS.map(t => fetch(`${import.meta.env.VITE_API_URL}/api/v1/usuarios/${userId}/metas/progresso?tipo=${t.key.toUpperCase()}&_=${new Date().getTime()}`, { headers }));
        const responses = await Promise.all(promises);
        const results = await Promise.all(responses.map(res => res.ok ? res.json() : null));

        const newData: Record<TabKey, PeriodData> = { ...initialData };

        results.forEach((result, index) => {
          if (result) {
            const key = TABS[index].key;
            newData[key] = {
              calories: { title: "Calorias Consumidas", unit: "kcal", total: result.calorias.consumido, changePct: 0, breakdown: [] },
              proteins: { title: "Proteínas Consumidas", unit: "g", total: result.proteinas.consumido, changePct: 0, breakdown: [] },
              carbs: { title: "Carboidratos Consumidos", unit: "g", total: result.carboidratos.consumido, changePct: 0, breakdown: [] },
              goals: {
                calories: { current: result.calorias.consumido, target: result.calorias.objetivo, suffix: "kcal" },
                proteins: { current: result.proteinas.consumido, target: result.proteinas.objetivo, suffix: "g" },
                carbs: { current: result.carboidratos.consumido, target: result.carboidratos.objetivo, suffix: "g" },
              },
            };
          }
        });

        setData(newData);
      } catch (error) {
        console.error("Erro ao buscar dados de progresso:", error);
      } finally {
        setLoading(false);
      }
    }

    fetchData();
    fetchWeightHistory();
  }, [location, fetchWeightHistory]);

  const currentData = data[tab];

  const goalsPerc = useMemo(() => {
    const pct = (curr: number, target: number) =>
      target > 0 ? Math.min(100, Math.round((curr / target) * 100)) : 0;
    return {
      calories: pct(currentData.goals.calories.current, currentData.goals.calories.target),
      proteins: pct(currentData.goals.proteins.current, currentData.goals.proteins.target),
      carbs: pct(currentData.goals.carbs.current, currentData.goals.carbs.target),
    };
  }, [currentData]);

  if (loading) {
    return <div className="progress-page loading">Carregando...</div>;
  }

  return (
    <main className="progress-page">
      <h2>Visão Geral Nutricional</h2>

      {/* Tabs */}
      <div className="tabs">
        {TABS.map(({ key, label }) => (
          <button
            key={key}
            className={`tab ${tab === key ? "active" : ""}`}
            onClick={() => setTab(key)}
            type="button"
          >
            {label}
          </button>
        ))}
      </div>

      {/* Metric cards */}
      <section className="metrics">
        <MetricCard metric={currentData.calories} />
        <MetricCard metric={currentData.proteins} />
        <MetricCard metric={currentData.carbs} />
      </section>

      {/* Goals */}
      <h3 className="section-title">Metas Nutricionais</h3>
      <section className="goals">
        <GoalCard
          title="Calorias"
          current={currentData.goals.calories.current}
          target={currentData.goals.calories.target}
          suffix={currentData.goals.calories.suffix}
          percent={goalsPerc.calories}
        />
        <GoalCard
          title="Proteínas"
          current={currentData.goals.proteins.current}
          target={currentData.goals.proteins.target}
          suffix={currentData.goals.proteins.suffix}
          percent={goalsPerc.proteins}
        />
        <GoalCard
          title="Carboidratos"
          current={currentData.goals.carbs.current}
          target={currentData.goals.carbs.target}
          suffix={currentData.goals.carbs.suffix}
          percent={goalsPerc.carbs}
        />
        <Button 
            title="Editar Metas" 
            onClick={() => navigate('/profile')} 
            white={true}
          />
      </section>

      <h3 className="section-title">Acompanhamento de Peso</h3>
      <section className="weight-tracking">
        <div className="card">
          <h4>Histórico de Peso</h4>
          <ul>
            {weightHistory.map((record: any) => (
              <li key={record.id}>{record.dataMedicao}: {record.pesoKg}kg</li>
            ))}
          </ul>
        </div>
      </section>
    </main>
  );
}

function MetricCard({ metric }: { metric: Metric }) {
  const valueText =
    metric.unit === "kcal"
      ? `${metric.total.toFixed(0)} kcal`
      : `${metric.total.toFixed(1)} g`;

  return (
    <div className="card metric-card">
      <div className="card-head">
        <span className="title">{metric.title}</span>
      </div>

      <div className="big-value">{valueText}</div>
    </div>
  );
}

function GoalCard({
  title,
  current,
  target,
  suffix,
  percent,
}: {
  title: string;
  current: number;
  target: number;
  suffix: "kcal" | "g";
  percent: number;
}) {
  return (
    <div className="card goal-card">
      <div className="goal-head">
        <span className="title">{title}</span>
        <span className="percent">{percent}%</span>
      </div>

      <div className="goal-values">
        {current.toFixed(0)} / {target.toFixed(0)} {suffix}
      </div>

      <div className="track">
        <div className="bar" style={{ width: `${percent}%` }} />
      </div>
    </div>
  );
}