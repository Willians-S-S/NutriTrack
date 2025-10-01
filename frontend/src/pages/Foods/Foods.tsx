import { useMemo, useRef, useState } from "react";
import "./Foods.scss";
import Button from "../../components/Button/Button";

/** --- Types --- */
type MealKey = "breakfast" | "lunch" | "dinner" | "snacks";
type MeasureKey = "unit" | "slice" | "cup" | "g100";

type MeasureInfo = {
  key: MeasureKey;
  label: string;   // "unidade", "fatia", "100 g"
  kcal: number;
  carbs: number;   // g
  fat: number;     // g
  protein: number; // g
};

type CatalogFood = {
  id: string;
  name: string;        // PT
  measures: MeasureInfo[];
};

type AddedItem = {
  foodId: string;
  name: string;
  measure: MeasureInfo;
  qty: number;
};

/** --- Mock catalog com valores de referência aproximados --- */
const CATALOG: CatalogFood[] = [
  {
    id: "eggs",
    name: "Ovos Mexidos",
    measures: [
      { key: "unit", label: "unidade", kcal: 78, carbs: 0.6, fat: 5.3, protein: 6.3 },
      { key: "g100", label: "100 g",   kcal: 148, carbs: 1.1, fat: 10.0, protein: 12.5 },
    ],
  },
  {
    id: "toast",
    name: "Torrada Integral",
    measures: [
      { key: "slice", label: "fatia", kcal: 69, carbs: 11.6, fat: 1.1, protein: 2.9 },
      { key: "g100",  label: "100 g", kcal: 340, carbs: 67,  fat: 4.2, protein: 12 },
    ],
  },
  {
    id: "avocado",
    name: "Abacate",
    measures: [
      { key: "unit", label: "1/2 unidade", kcal: 160, carbs: 8.5, fat: 14.7, protein: 2 },
      { key: "g100", label: "100 g",       kcal: 160, carbs: 9,   fat: 15,   protein: 2 },
    ],
  },
  // Outros alimentos...
];

const MEAL_TITLES: Record<MealKey, string> = {
  breakfast: "Café da Manhã",
  lunch: "Almoço",
  dinner: "Jantar",
  snacks: "Lanches",
};

export default function Foods() {
  const [query, setQuery] = useState("");
  const searchRef = useRef<HTMLInputElement>(null);

  const [meals, setMeals] = useState<Record<MealKey, AddedItem[]>>({
    breakfast: [
      { foodId: "eggs", name: "Ovos Mexidos", measure: CATALOG[0].measures[0], qty: 2 },
      { foodId: "toast", name: "Torrada Integral", measure: CATALOG[1].measures[0], qty: 2 },
      { foodId: "avocado", name: "Abacate", measure: CATALOG[2].measures[0], qty: 1 },
    ],
    lunch: [],
    dinner: [],
    snacks: [],
  });

  const [mealSaved, setMealSaved] = useState<Record<MealKey, boolean>>({
    breakfast: true,
    lunch: false,
    dinner: false,
    snacks: false,
  });

  const [snapshots, setSnapshots] = useState<Record<MealKey, AddedItem[]>>({
    breakfast: [...(meals.breakfast || [])],
    lunch: [],
    dinner: [],
    snacks: [],
  });

  const [presetMeal, setPresetMeal] = useState<MealKey | null>(null);
  const [open, setOpen] = useState(false);
  const [activeFood, setActiveFood] = useState<CatalogFood | null>(null);
  const [measure, setMeasure] = useState<MeasureInfo | null>(null);
  const [amount, setAmount] = useState<number>(1);
  const [selectedMeals, setSelectedMeals] = useState<Set<MealKey>>(new Set());

  const results = useMemo(() => {
    const q = query.trim().toLowerCase();
    if (!q) return [];
    return CATALOG.filter((f) => f.name.toLowerCase().includes(q));
  }, [query]);

  const totalKcal = useMemo(() => (measure ? measure.kcal * amount : 0), [measure, amount]);
  const totalCarbs = useMemo(() => (measure ? +(measure.carbs * amount).toFixed(1) : 0), [measure, amount]);
  const totalFat = useMemo(() => (measure ? +(measure.fat * amount).toFixed(1) : 0), [measure, amount]);
  const totalProtein = useMemo(() => (measure ? +(measure.protein * amount).toFixed(1) : 0), [measure, amount]);

  function openModal(food: CatalogFood, preset?: MealKey | null) {
    setActiveFood(food);
    setMeasure(food.measures[0]);
    setAmount(1);
    const start = new Set<MealKey>();
    if (preset) start.add(preset);
    else if (presetMeal) start.add(presetMeal);
    setSelectedMeals(start);
    setOpen(true);
  }

  function closeModal() {
    setOpen(false);
    setActiveFood(null);
    setMeasure(null);
    setAmount(1);
    setSelectedMeals(new Set());
  }

  function addSelection() {
    if (!activeFood || !measure || selectedMeals.size === 0) return;
    setMeals((prev) => {
      const next = { ...prev };
      selectedMeals.forEach((meal) => {
        next[meal] = [
          ...next[meal],
          { foodId: activeFood.id, name: activeFood.name, measure, qty: amount },
        ];
        if (mealSaved[meal]) {
          setMealSaved((s) => ({ ...s, [meal]: false }));
        }
      });
      return next;
    });
    setMealSaved((s) => ({ ...s, [presetMeal || "breakfast"]: true }));
    closeModal();
    setQuery("");
  }

  function goToSearchFor(meal: MealKey) {
    setPresetMeal(meal);
    setTimeout(() => searchRef.current?.focus(), 0);
  }

  function subtotalKcal(meal: MealKey) {
    return meals[meal].reduce((acc, it) => acc + it.measure.kcal * it.qty, 0);
  }

  function startEditMeal(meal: MealKey) {
    setSnapshots((s) => ({ ...s, [meal]: [...meals[meal]] }));
    setMealSaved((s) => ({ ...s, [meal]: false }));
  }
  function cancelEditMeal(meal: MealKey) {
    setMeals((prev) => ({ ...prev, [meal]: [...(snapshots[meal] || [])] }));
    setMealSaved((s) => ({ ...s, [meal]: true }));
  }
  function saveMeal(meal: MealKey) {
    setSnapshots((s) => ({ ...s, [meal]: [...meals[meal]] }));
    setMealSaved((s) => ({ ...s, [meal]: true }));
  }

  function incItem(meal: MealKey, idx: number) {
    setMeals((prev) => {
      const arr = [...prev[meal]];
      arr[idx] = { ...arr[idx], qty: arr[idx].qty + 1 };
      return { ...prev, [meal]: arr };
    });
  }
  function decItem(meal: MealKey, idx: number) {
    setMeals((prev) => {
      const arr = [...prev[meal]];
      arr[idx] = { ...arr[idx], qty: Math.max(1, arr[idx].qty - 1) };
      return { ...prev, [meal]: arr };
    });
  }
  function removeItem(meal: MealKey, idx: number) {
    setMeals((prev) => {
      const arr = [...prev[meal]];
      arr.splice(idx, 1);
      return { ...prev, [meal]: arr };
    });
  }

  return (
    <main className="foods-page">
      <h2>Adicionar Alimentos</h2>

      <div className="search">
        <input
          ref={searchRef}
          type="text"
          placeholder="Buscar alimentos..."
          value={query}
          onChange={(e) => setQuery(e.target.value)}
        />
        {query && (
          <div className="results">
            {results.length === 0 ? (
              <div className="empty">Nenhum alimento encontrado</div>
            ) : (
              results.map((f) => (
                <button
                  key={f.id}
                  className="result-row"
                  onClick={() => openModal(f, presetMeal)}
                  type="button"
                >
                  {f.name}
                </button>
              ))
            )}
          </div>
        )}
      </div>

      <section className="meals">
        {(Object.keys(MEAL_TITLES) as MealKey[]).map((meal) => {
          const items = meals[meal];
          const hasItems = items.length > 0;
          const isSaved = mealSaved[meal];

          return (
            <div className="meal-card" key={meal}>
              <div className="meal-title">
                <span>{MEAL_TITLES[meal]}</span>
                {hasItems && <span className="subkcal">{subtotalKcal(meal)} kcal</span>}
              </div>

              {!hasItems ? (
                <button className="empty-cta" onClick={() => goToSearchFor(meal)} type="button">
                  <span className="plus">+</span> Adicione seus alimentos
                </button>
              ) : (
                <ul className={`food-list ${isSaved ? "readonly" : "editing"}`}>
                  {items.map((it, idx) => (
                    <li className="food-row" key={`${it.foodId}-${idx}`}>
                      <div className="left">
                        <div className="name">{it.name}</div>
                        <div className="meta">
                          {isSaved
                            ? `${it.qty} × ${it.measure.label} • ${it.measure.kcal * it.qty} kcal`
                            : `${it.measure.label} • ${it.measure.kcal} kcal por unidade`}
                        </div>
                      </div>

                      {isSaved ? (
                        <div className="right read-only">
                          <span className="qty-tag">{it.qty}</span>
                        </div>
                      ) : (
                        <div className="right editing">
                          <div className="counter">
                            <button type="button" onClick={() => decItem(meal, idx)} aria-label="Diminuir">−</button>
                            <span className="qty" aria-live="polite">{it.qty}</span>
                            <button type="button" onClick={() => incItem(meal, idx)} aria-label="Aumentar">+</button>
                          </div>
                          <button
                            type="button"
                            className="remove-btn"
                            onClick={() => removeItem(meal, idx)}
                            aria-label="Remover alimento"
                          >
                            ×
                          </button>
                        </div>
                      )}
                    </li>
                  ))}
                </ul>
              )}

              {hasItems && (
                <div className="meal-actions">
                  {isSaved ? (
                    <button className="save-btn saved" onClick={() => startEditMeal(meal)} type="button">
                      Editar refeições
                    </button>
                  ) : (
                    <>
                      <button className="ghost-btn" onClick={() => cancelEditMeal(meal)} type="button">
                        Cancelar
                      </button>
                      <button className="save-btn" onClick={() => saveMeal(meal)} type="button">
                        Salvar refeição
                      </button>
                    </>
                  )}
                </div>
              )}
            </div>
          );
        })}
      </section>

      {open && activeFood && measure && (
        <div className="modal-backdrop" onClick={closeModal}>
          <div className="modal" onClick={(e) => e.stopPropagation()}>
            <div className="modal-head">
              <h3>{activeFood.name}</h3>
              <button className="close-x" onClick={closeModal} aria-label="Fechar">×</button>
            </div>

            <div className="modal-body">
              <div className="grid">
                <div className="row">
                  <label>Medida</label>
                  <select
                    value={measure.key}
                    onChange={(e) => {
                      const m = activeFood.measures.find((x) => x.key === (e.target.value as MeasureKey));
                      if (m) setMeasure(m);
                    }}
                  >
                    {activeFood.measures.map((m) => (
                      <option key={m.key} value={m.key}>{m.label}</option>
                    ))}
                  </select>
                </div>

                <div className="row">
                  <label>Quantidade</label>
                  <div className="qty">
                    <button type="button" onClick={() => setAmount((v) => Math.max(1, v - 1))}>−</button>
                    <input
                      type="number"
                      min={1}
                      value={amount}
                      onChange={(e) => setAmount(Math.max(1, Number(e.target.value)))}
                    />
                    <button type="button" onClick={() => setAmount((v) => v + 1)}>+</button>
                  </div>
                </div>
              </div>

              <div className="macros">
                <Macro label="Calorias" value={`${totalKcal} kcal`} />
                <Macro label="Carboidratos" value={`${totalCarbs} g`} />
                <Macro label="Gorduras" value={`${totalFat} g`} />
                <Macro label="Proteínas" value={`${totalProtein} g`} />
              </div>

              <div className="meal-select">
                <span>Adicionar em:</span>
                {(["breakfast", "lunch", "dinner", "snacks"] as MealKey[]).map((m) => (
                  <button
                    key={m}
                    type="button"
                    className={`pill ${selectedMeals.has(m) ? "on" : ""}`}
                    onClick={() => {
                      const next = new Set(selectedMeals);
                      next.has(m) ? next.delete(m) : next.add(m);
                      setSelectedMeals(next);
                    }}
                  >
                    {MEAL_TITLES[m]}
                  </button>
                ))}
              </div>
            </div>

            <div className="modal-actions">
              <button className="ghost-btn" onClick={closeModal} type="button">Cancelar</button>
              <Button title="Adicionar" onClick={addSelection} />
            </div>
          </div>
        </div>
      )}
    </main>
  );
}

/** Badge de macro no modal */
function Macro({ label, value }: { label: string; value: string }) {
  return (
    <div className="macro">
      <span className="m-label">{label}</span>
      <span className="m-value">{value}</span>
    </div>
  );
}
