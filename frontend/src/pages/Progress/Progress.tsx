import { useEffect, useMemo, useState } from "react";
import "./Progress.scss";

type TabKey = "daily" | "weekly" | "monthly";

type Metric = {
  title: string; // PT label
  unit: "kcal" | "g";
  total: number;
  changePct: number; // positive or negative
  breakdown: { label: string; value: number }[]; // Café/Almoço/Jantar
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

// --- MOCK DATA (ajuste livre depois) ---
const DATA: Record<TabKey, PeriodData> = {
  daily: {
    calories: {
      title: "Calorias Consumidas",
      unit: "kcal",
      total: 1850,
      changePct: +10,
      breakdown: [
        { label: "Café", value: 450 },
        { label: "Almoço", value: 900 },
        { label: "Jantar", value: 500 },
      ],
    },
    proteins: {
      title: "Proteínas Consumidas",
      unit: "g",
      total: 120,
      changePct: -5,
      breakdown: [
        { label: "Café", value: 30 },
        { label: "Almoço", value: 60 },
        { label: "Jantar", value: 30 },
      ],
    },
    carbs: {
      title: "Carboidratos Consumidos",
      unit: "g",
      total: 250,
      changePct: +2,
      breakdown: [
        { label: "Café", value: 70 },
        { label: "Almoço", value: 120 },
        { label: "Jantar", value: 60 },
      ],
    },
    goals: {
      calories: { current: 1850, target: 2200, suffix: "kcal" },
      proteins: { current: 120, target: 160, suffix: "g" },
      carbs: { current: 250, target: 300, suffix: "g" },
    },
  },
  weekly: {
    calories: {
      title: "Calorias Consumidas",
      unit: "kcal",
      total: 12600,
      changePct: +4,
      breakdown: [
        { label: "Café", value: 3800 },
        { label: "Almoço", value: 6000 },
        { label: "Jantar", value: 2800 },
      ],
    },
    proteins: {
      title: "Proteínas Consumidas",
      unit: "g",
      total: 840,
      changePct: +3,
      breakdown: [
        { label: "Café", value: 210 },
        { label: "Almoço", value: 420 },
        { label: "Jantar", value: 210 },
      ],
    },
    carbs: {
      title: "Carboidratos Consumidos",
      unit: "g",
      total: 1700,
      changePct: -2,
      breakdown: [
        { label: "Café", value: 450 },
        { label: "Almoço", value: 800 },
        { label: "Jantar", value: 450 },
      ],
    },
    goals: {
      calories: { current: 12600, target: 15400, suffix: "kcal" }, // 2200*7
      proteins: { current: 840, target: 1120, suffix: "g" },       // 160*7
      carbs: { current: 1700, target: 2100, suffix: "g" },         // 300*7
    },
  },
  monthly: {
    calories: {
      title: "Calorias Consumidas",
      unit: "kcal",
      total: 54000,
      changePct: +1,
      breakdown: [
        { label: "Café", value: 16000 },
        { label: "Almoço", value: 26000 },
        { label: "Jantar", value: 12000 },
      ],
    },
    proteins: {
      title: "Proteínas Consumidas",
      unit: "g",
      total: 3600,
      changePct: 0,
      breakdown: [
        { label: "Café", value: 900 },
        { label: "Almoço", value: 1800 },
        { label: "Jantar", value: 900 },
      ],
    },
    carbs: {
      title: "Carboidratos Consumidos",
      unit: "g",
      total: 7200,
      changePct: +3,
      breakdown: [
        { label: "Café", value: 1900 },
        { label: "Almoço", value: 3600 },
        { label: "Jantar", value: 1700 },
      ],
    },
    goals: {
      calories: { current: 54000, target: 66000, suffix: "kcal" }, // 2200*30
      proteins: { current: 3600, target: 4800, suffix: "g" },      // 160*30
      carbs: { current: 7200, target: 9000, suffix: "g" },         // 300*30
    },
  },
};

const TABS: { key: TabKey; label: string }[] = [
  { key: "daily", label: "Diário" },
  { key: "weekly", label: "Semanal" },
  { key: "monthly", label: "Mensal" },
];

export default function Progress() {
  const [tab, setTab] = useState<TabKey>("daily");
  const data = DATA[tab];
  const [weightHistory, setWeightHistory] = useState<any[]>([]);

  useEffect(() => {
    const userId = localStorage.getItem("userId");
    if (!userId) return;

    async function fetchWeightHistory() {
      const token = localStorage.getItem("token");
      const today = new Date();
      const lastMonth = new Date(today);
      lastMonth.setMonth(today.getMonth() - 1);

      const start = lastMonth.toISOString().split('T')[0];
      const end = today.toISOString().split('T')[0];

      try {
        const response = await fetch(`/api/v1/registros-peso/usuario/${userId}?start=${start}&end=${end}`, {
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
    }

    fetchWeightHistory();
  }, []);

  const goalsPerc = useMemo(() => {
    const pct = (curr: number, target: number) =>
      Math.min(100, Math.round((curr / target) * 100));
    return {
      calories: pct(data.goals.calories.current, data.goals.calories.target),
      proteins: pct(data.goals.proteins.current, data.goals.proteins.target),
      carbs: pct(data.goals.carbs.current, data.goals.carbs.target),
    };
  }, [data]);

  async function handleAddWeight(e: React.FormEvent<HTMLFormElement>) {
    e.preventDefault();
    const userId = localStorage.getItem("userId");
    if (!userId) return;

    const form = e.currentTarget;
    const dateInput = form.elements.namedItem("weightDate") as HTMLInputElement;
    const weightInput = form.elements.namedItem("weightInput") as HTMLInputElement;

    if (!dateInput.value || !weightInput.value) {
      alert("Por favor, preencha a data e o peso.");
      return;
    }

    const payload = {
      dataMedicao: dateInput.value,
      pesoKg: parseFloat(weightInput.value),
    };

    const token = localStorage.getItem("token");
    try {
      const response = await fetch(`/api/v1/registros-peso/${userId}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        },
        body: JSON.stringify(payload),
      });

      if (response.ok) {
        const newRecord = await response.json();
        setWeightHistory(prev => [...prev, newRecord].sort((a, b) => new Date(a.dataMedicao).getTime() - new Date(b.dataMedicao).getTime()));
        form.reset();
      } else {
        console.error("Erro ao adicionar registro de peso");
        alert("Erro ao adicionar registro de peso. Verifique se já existe um registro para esta data.");
      }
    } catch (error) {
      console.error("Erro ao adicionar registro de peso:", error);
    }
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
        <MetricCard metric={data.calories} />
        <MetricCard metric={data.proteins} />
        <MetricCard metric={data.carbs} />
      </section>

      {/* Goals */}
      <h3 className="section-title">Metas Nutricionais</h3>
      <section className="goals">
        <GoalCard
          title="Calorias"
          current={data.goals.calories.current}
          target={data.goals.calories.target}
          suffix={data.goals.calories.suffix}
          percent={goalsPerc.calories}
        />
        <GoalCard
          title="Proteínas"
          current={data.goals.proteins.current}
          target={data.goals.proteins.target}
          suffix={data.goals.proteins.suffix}
          percent={goalsPerc.proteins}
        />
        <GoalCard
          title="Carboidratos"
          current={data.goals.carbs.current}
          target={data.goals.carbs.target}
          suffix={data.goals.carbs.suffix}
          percent={goalsPerc.carbs}
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
        <div className="card">
          <h4>Adicionar Novo Registro</h4>
          <form onSubmit={handleAddWeight} className="weight-form">
            <div className="form-group">
              <label htmlFor="weightDate">Data</label>
              <input type="date" id="weightDate" name="weightDate" defaultValue={new Date().toISOString().split('T')[0]} />
            </div>
            <div className="form-group">
              <label htmlFor="weightInput">Peso (kg)</label>
              <input type="number" id="weightInput" name="weightInput" step="0.1" required />
            </div>
            <button type="submit">Adicionar</button>
          </form>
        </div>
      </section>
    </main>
  );
}

function MetricCard({ metric }: { metric: Metric }) {
  const isUp = metric.changePct >= 0;
  const badgeClass = `trend-badge ${isUp ? "up" : "down"}`;
  const valueText =
    metric.unit === "kcal"
      ? `${metric.total} kcal`
      : `${metric.total} g`;

  return (
    <div className="card metric-card">
      <div className="card-head">
        <span className="title">{metric.title}</span>
        <span className={badgeClass}>
          {isUp ? "↗︎" : "↘︎"} {metric.changePct > 0 ? `+${metric.changePct}%` : `${metric.changePct}%`}
        </span>
      </div>

      <div className="big-value">{valueText}</div>

      <div className="labels-row">
        {metric.breakdown.map((b) => (
          <span key={b.label} className="label-item">
            {b.label}
          </span>
        ))}
      </div>
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
        {current} / {target} {suffix}
      </div>

      <div className="track">
        <div className="bar" style={{ width: `${percent}%` }} />
      </div>
    </div>
  );
}