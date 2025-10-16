import { useEffect, useMemo, useRef, useState } from "react";
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
  mealId: string;
  foodId: string;
  name: string;
  measure: MeasureInfo;
  qty: number;
};

export default function Foods() {
  const [catalog, setCatalog] = useState<CatalogFood[]>([]);

  useEffect(() => {
    async function fetchCatalog() {
      setLoading(true);
      try {
        const response = await fetch(`/api/v1/alimentos?nome=${query}`);
        if (response.ok) {
          const data = await response.json();
          setCatalog(data.content); 
        } else {
          setError('Erro ao buscar o catálogo de alimentos');
        }
      } catch (error) {
        setError('Erro ao buscar o catálogo de alimentos');
      } finally {
        setLoading(false);
      }
    }

    if (query) {
      fetchCatalog();
    }
  }, [query]);
useEffect(() => {
    async function fetchMeals() {
      const userId = localStorage.getItem('userId');
      if (!userId) return;

      setLoading(true);
      const today = new Date();
      const start = new Date(today.setHours(0, 0, 0, 0)).toISOString();
      const end = new Date(today.setHours(23, 59, 59, 999)).toISOString();

      try {
        const response = await fetch(`/api/v1/refeicoes/${userId}?start=${start}&end=${end}`);
        if (response.ok) {
          const data = await response.json();
          // Group meals by type
          const mealsByType = data.reduce((acc, meal) => {
            const mealType = meal.tipoRefeicao.toLowerCase();
            if (!acc[mealType]) {
              acc[mealType] = [];
            }
            const measure = meal.alimento.measures.find(m => m.key === meal.medida);
            if (measure) {
              acc[mealType].push({
                mealId: meal.id,
                foodId: meal.alimento.id,
                name: meal.alimento.nome,
                measure,
                qty: meal.quantidade,
              });
            }
            return acc;
          }, {});
          setMeals(mealsByType);
        } else {
          setError('Erro ao buscar as refeições');
        }
      } catch (error) {
        setError('Erro ao buscar as refeições');
      } finally {
        setLoading(false);
      }
    }

    fetchMeals();
  }, []);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const searchRef = useRef<HTMLInputElement>(null);

  const [meals, setMeals] = useState<Record<MealKey, AddedItem[]>>({
    breakfast: [],
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
    return catalog.filter((f) => f.name.toLowerCase().includes(q));
  }, [query, catalog]);

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

  async function addSelection() {
    if (!activeFood || !measure || selectedMeals.size === 0) return;

    const userId = localStorage.getItem('userId');
    if (!userId) {
      setError("Usuário não autenticado.");
      return;
    }

    const items = Array.from(selectedMeals).map((meal) => ({
      alimentoId: activeFood.id,
      quantidade: amount,
      medida: measure.key,
      tipoRefeicao: meal.toUpperCase(),
    }));

    setLoading(true);
    try {
      const token = localStorage.getItem("token");
      const response = await fetch(`/api/v1/refeicoes/${userId}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify({ items }),
      });

      if (response.ok) {
        const newMeal = await response.json();
        // Update local state after successful save
        setMeals((prev) => {
          const next = { ...prev };
          selectedMeals.forEach((meal) => {
            next[meal] = [
              ...next[meal],
              {
                mealId: newMeal.id, // Get mealId from response
                foodId: activeFood.id,
                name: activeFood.name,
                measure,
                qty: amount
              },
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
      } else {
        setError('Erro ao salvar a refeição');
      }
    } catch (error) {
      setError('Erro ao salvar a refeição');
    } finally {
      setLoading(false);
    }
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
  async function saveMeal(meal: MealKey) {
    const userId = localStorage.getItem('userId');
    if (!userId) {
      setError("Usuário não autenticado.");
      return;
    }
    const mealId = meals[meal][0].mealId; // Assuming all items in a meal have the same mealId

    const items = meals[meal].map(item => ({
      alimentoId: item.foodId,
      quantidade: item.qty,
      medida: item.measure.key,
      tipoRefeicao: meal.toUpperCase(),
    }));

    setLoading(true);
    try {
      const token = localStorage.getItem("token");
      const response = await fetch(`/api/v1/refeicoes/${userId}/${mealId}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify({ items }),
      });

      if (response.ok) {
        setSnapshots((s) => ({ ...s, [meal]: [...meals[meal]] }));
        setMealSaved((s) => ({ ...s, [meal]: true }));
      } else {
        setError('Erro ao salvar a refeição');
      }
    } catch (error) {
      setError('Erro ao salvar a refeição');
    } finally {
      setLoading(false);
    }
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
  async function removeItem(meal: MealKey, idx: number) {
    const userId = localStorage.getItem('userId');
    if (!userId) {
      setError("Usuário não autenticado.");
      return;
    }
    const itemToRemove = meals[meal][idx];
    const mealId = itemToRemove.mealId;

    setLoading(true);
    try {
      const token = localStorage.getItem("token");
      const response = await fetch(`/api/v1/refeicoes/${userId}/${mealId}`, {
        method: 'DELETE',
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });

      if (response.ok) {
        setMeals((prev) => {
          const arr = [...prev[meal]];
          arr.splice(idx, 1);
          return { ...prev, [meal]: arr };
        });
      } else {
        setError('Erro ao remover o item da refeição');
      }
    } catch (error) {
      setError('Erro ao remover o item da refeição');
    } finally {
      setLoading(false);
    }
  }

  return (
    <main className="foods-page">
      {loading && <div className="loading">Carregando...</div>}
      {error && <div className="error">{error}</div>}
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
