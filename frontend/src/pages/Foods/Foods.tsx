import { useEffect, useMemo, useRef, useState, useCallback } from "react";
import "./Foods.scss";
import Button from "../../components/Button/Button";
import FoodModal from "../../components/FoodModal/FoodModal";
import { MEAL_TITLES, UNIDADE_MEDIDA_LABELS, type MealKey, type MeasureKey } from "../../utils/Constants";
import { toast } from "react-toastify";

type AlimentoResumidoDTO = {
  id: string;
  nome: string;
};

type AlimentoResponseDTO = AlimentoResumidoDTO & {
  calorias: number;
  proteinasG: number;
  carboidratosG: number;
  gordurasG: number;
};

type ItemRefeicaoResponseDTO = {
  id: string;
  alimento: AlimentoResumidoDTO;
  quantidade: number;
  unidade: MeasureKey;
  observacoes: string | null;
};

type RefeicaoResponseDTO = {
  id: string;
  usuarioId: string;
  tipo: MealKey;
  dataHora: string;
  observacoes: string | null;
  itens: ItemRefeicaoResponseDTO[];
  totalCalorias: number;
  totalProteinasG: number;
  totalCarboidratosG: number;
  totalGordurasG: number;
};

type ItemRefeicaoRequestDTO = {
    alimentoId: string;
    quantidade: number;
    unidade: MeasureKey;
    observacoes: string | null;
};
type RefeicaoRequestDTO = {
    tipo: MealKey;
    dataHora: string;
    observacoes: string | null;
    itens: ItemRefeicaoRequestDTO[];
};

type CatalogFood = AlimentoResponseDTO & {
  measures: { key: MeasureKey; label: string }[];
};

type MealsState = Record<MealKey, RefeicaoResponseDTO | undefined>;

const MOCK_MEASURES: { key: MeasureKey; label: string }[] = [
    { key: "GRAMA", label: "gramas" },
    { key: "MILILITRO", label: "ml" },
    { key: "UNIDADE", label: "unidade" },
];

const MACRO_BASE_DIVISOR = 100;

export default function Foods() {
  const [query, setQuery] = useState("");
  const [catalog, setCatalog] = useState<CatalogFood[]>([]);
  const [meals, setMeals] = useState<MealsState>({} as MealsState);

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const searchRef = useRef<HTMLInputElement>(null);

  const [presetMeal, setPresetMeal] = useState<MealKey | null>(null);
  const [open, setOpen] = useState(false);
  const [activeFood, setActiveFood] = useState<CatalogFood | null>(null);
  const [unit, setUnit] = useState<MeasureKey>("GRAMA");
  const [amount, setAmount] = useState<number>(100);
  const [selectedMealType, setSelectedMealType] = useState<MealKey | null>(presetMeal);

  const getTodayRange = useCallback(() => {
    const today = new Date();

    const year = today.getFullYear();
    const month = (today.getMonth() + 1).toString().padStart(2, '0');
    const day = today.getDate().toString().padStart(2, '0');
    const dateOnly = `${year}-${month}-${day}`;

    const start = dateOnly;
    const end = dateOnly;
    const dataHora = today.toISOString();

    return { start, end, dataHora };
  }, []);

  useEffect(() => {
    async function fetchCatalog() {
      if (query.trim().length < 3) {
        setCatalog([]);
        return;
      }
      setLoading(true);
      setError(null);
      try {
        const token = localStorage.getItem("token");
        const response = await fetch(`/api/v1/alimentos?nome=${encodeURIComponent(query)}&size=10`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        if (response.ok) {
          const data = await response.json();
          const formattedCatalog = data.content.map((food: AlimentoResponseDTO) => ({
              ...food,
              measures: MOCK_MEASURES,
          }));
          setCatalog(formattedCatalog);
        } else {
          setError('Erro ao buscar o catálogo de alimentos');
        }
      } catch (error) {
        setError('Erro ao buscar o catálogo de alimentos');
      } finally {
        setLoading(false);
      }
    }

    const delayDebounceFn = setTimeout(() => {
        fetchCatalog();
    }, 300);

    return () => clearTimeout(delayDebounceFn);
  }, [query]);

  const fetchMeals = useCallback(async () => {
    const userId = localStorage.getItem('userId');
    const token = localStorage.getItem('token');
    if (!userId || !token) return;

    setLoading(true);
    const { start, end } = getTodayRange();
    setError(null);

    try {
      const response = await fetch(`/api/v1/refeicoes/usuario/${userId}?start=${start}&end=${end}`, {
          headers: { 'Authorization': `Bearer ${token}` }
      });

      if (response.ok) {
        const data: RefeicaoResponseDTO[] = await response.json();
        const mealsByType = data.reduce((acc, meal) => {
          acc[meal.tipo] = meal;
          return acc;
        }, {} as MealsState);

        setMeals(mealsByType);
      } else {
        setError('Erro ao buscar as refeições');
      }
    } catch (error) {
      setError('Erro ao buscar as refeições');
    } finally {
      setLoading(false);
    }
  }, [getTodayRange]);

  useEffect(() => {
    fetchMeals();
  }, [fetchMeals]);

  const activeFoodData = activeFood as AlimentoResponseDTO;

  const totalKcal = useMemo(() => activeFoodData ? Math.round((activeFoodData.calorias / MACRO_BASE_DIVISOR) * amount) : 0, [activeFoodData, amount]);
  const totalCarbs = useMemo(() => activeFoodData ? +((activeFoodData.carboidratosG / MACRO_BASE_DIVISOR) * amount).toFixed(1) : 0, [activeFoodData, amount]);
  const totalFat = useMemo(() => activeFoodData ? +((activeFoodData.gordurasG / MACRO_BASE_DIVISOR) * amount).toFixed(1) : 0, [activeFoodData, amount]);
  const totalProtein = useMemo(() => activeFoodData ? +((activeFoodData.proteinasG / MACRO_BASE_DIVISOR) * amount).toFixed(1) : 0, [activeFoodData, amount]);

  function openModal(food: CatalogFood, preset: MealKey | null) {
    setActiveFood(food);
    setUnit("GRAMA");
    setAmount(100);
    setSelectedMealType(preset ?? presetMeal ?? "CAFE_MANHA");
    setOpen(true);
  }

  function closeModal() {
    setOpen(false);
    setActiveFood(null);
    setUnit("GRAMA");
    setAmount(100);
    setQuery("");
  }

  async function addSelection() {
    if (!activeFood || !selectedMealType) return;

    const userId = localStorage.getItem('userId');
    const token = localStorage.getItem('token');
    if (!userId || !token) {
      setError("Usuário não autenticado.");
      return;
    }

    setLoading(true);
    setError(null);

    const newItem: Omit<ItemRefeicaoResponseDTO, 'id' | 'alimento' | 'observacoes'> & { alimento: AlimentoResumidoDTO } = {
        alimento: { id: activeFood.id, nome: activeFood.nome },
        quantidade: amount,
        unidade: unit,
    };

    const existingMeal = meals[selectedMealType];
    const { dataHora } = getTodayRange();

    const isUpdate = existingMeal !== undefined;
    const itemsPayload: ItemRefeicaoRequestDTO[] = (existingMeal?.itens || []).map(item => ({
        alimentoId: item.alimento.id,
        quantidade: item.quantidade,
        unidade: item.unidade,
        observacoes: item.observacoes,
    }));

    itemsPayload.push({
        alimentoId: activeFood.id,
        quantidade: amount,
        unidade: unit,
        observacoes: ""
    });

    const refeicaoPayload: RefeicaoRequestDTO = {
        tipo: selectedMealType,
        dataHora: existingMeal?.dataHora ?? dataHora,
        observacoes: existingMeal?.observacoes ?? "",
        itens: itemsPayload,
    }

    try {
        const url = isUpdate
            ? `/api/v1/refeicoes/${userId}/${existingMeal.id}`
            : `/api/v1/refeicoes/${userId}`;
        const method = isUpdate ? 'PUT' : 'POST';

        const response = await fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(refeicaoPayload),
        });

        if (response.ok) {
            const updatedMeal: RefeicaoResponseDTO = await response.json();
            setMeals((prev) => ({ ...prev, [selectedMealType]: updatedMeal }));
            closeModal();
        } else {
            const errorData = await response.json().catch(() => ({}));
            setError(errorData.message || 'Erro ao salvar a refeição');
        }
    } catch (error) {
        setError('Erro de rede ao salvar a refeição.');
    } finally {
        setLoading(false);
    }
  }

  async function saveMeal(mealKey: MealKey) {
    const mealToSave = meals[mealKey];
    if (!mealToSave) return;

    const userId = localStorage.getItem('userId');
    const token = localStorage.getItem('token');
    if (!userId || !token) return;

    setLoading(true);
    setError(null);

    const itemsPayload: ItemRefeicaoRequestDTO[] = mealToSave.itens.map(item => ({
        alimentoId: item.alimento.id,
        quantidade: item.quantidade,
        unidade: item.unidade,
        observacoes: item.observacoes,
    }));

    const refeicaoPayload: RefeicaoRequestDTO = {
        tipo: mealToSave.tipo,
        dataHora: mealToSave.dataHora,
        observacoes: mealToSave.observacoes,
        itens: itemsPayload,
    }

    try {
        const response = await fetch(`/api/v1/refeicoes/${userId}/${mealToSave.id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(refeicaoPayload),
        });

        if (response.ok) {
            const updatedMeal: RefeicaoResponseDTO = await response.json();
            setMeals((prev) => ({ ...prev, [mealKey]: updatedMeal }));
            toast.success(`${MEAL_TITLES[mealKey]} salva com sucesso!`);
        } else {
            const errorData = await response.json().catch(() => ({}));
            setError(errorData.message || 'Erro ao salvar a refeição');
        }
    } catch (error) {
        setError('Erro de rede ao salvar a refeição.');
    } finally {
        setLoading(false);
    }
  }

  async function removeMeal(mealKey: MealKey) {
    const mealToDelete = meals[mealKey];
    if (!mealToDelete || !confirm(`Tem certeza que deseja remover TODA a refeição de ${MEAL_TITLES[mealKey]}?`)) return;

    const userId = localStorage.getItem('userId');
    const token = localStorage.getItem('token');
    if (!userId || !token) return;

    setLoading(true);
    setError(null);

    try {
        const response = await fetch(`/api/v1/refeicoes/${userId}/${mealToDelete.id}`, {
            method: 'DELETE',
            headers: { 'Authorization': `Bearer ${token}` }
        });

        if (response.status === 204) {
            setMeals((prev) => {
                const next = { ...prev };
                delete next[mealKey];
                return next;
            });
            toast.success(`Refeição de ${MEAL_TITLES[mealKey]} removida.`);
        } else {
             const errorData = await response.json().catch(() => ({}));
             setError(errorData.message || 'Erro ao remover a refeição');
        }
    } catch (error) {
        setError('Erro de rede ao remover a refeição.');
    } finally {
        setLoading(false);
    }
  }

  function updateItemQty(mealKey: MealKey, itemIndex: number, newQty: number) {
    const currentMeal = meals[mealKey];
    if (!currentMeal) return;

    setMeals(prev => {
        const updatedItems = currentMeal.itens.map((item, idx) =>
            idx === itemIndex ? { ...item, quantidade: Math.max(1, newQty) } : item
        );
        return {
            ...prev,
            [mealKey]: { ...currentMeal, itens: updatedItems }
        };
    });
  }

  function removeItemFromList(mealKey: MealKey, itemIndex: number) {
    const currentMeal = meals[mealKey];
    if (!currentMeal) return;

    if (currentMeal.itens.length === 1) {
      if (confirm(`Remover o último item de ${MEAL_TITLES[mealKey]} removerá a refeição inteira. Deseja continuar?`)) {
        removeMeal(mealKey);
      }
      return;
    }

    setMeals(prev => {
        const updatedItems = currentMeal.itens.filter((_, idx) => idx !== itemIndex);
        return {
            ...prev,
            [mealKey]: { ...currentMeal, itens: updatedItems }
        };
    });
  }

  function goToSearchFor(meal: MealKey) {
    setPresetMeal(meal);
    setTimeout(() => searchRef.current?.focus(), 0);
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
        {query.trim().length >= 3 && (
          <div className="results">
            {catalog.length === 0 ? (
              <div className="empty">Nenhum alimento encontrado</div>
            ) : (
              catalog.map((f) => (
                <button
                  key={f.id}
                  className="result-row"
                  onClick={() => openModal(f, presetMeal)}
                  type="button"
                >
                  {f.nome}
                </button>
              ))
            )}
          </div>
        )}
      </div>

      <section className="meals">
        {(Object.keys(MEAL_TITLES) as MealKey[]).map((mealKey) => {
          const meal = meals[mealKey];
          const hasItems = meal && meal.itens.length > 0;

          return (
            <div className="meal-card" key={mealKey}>
              <div className="meal-title">
                <span>{MEAL_TITLES[mealKey]}</span>
                {hasItems && <span className="subkcal">{Math.round(meal.totalCalorias)} kcal</span>}
              </div>

              {!hasItems ? (
                <button className="empty-cta" onClick={() => goToSearchFor(mealKey)} type="button">
                  <span className="plus">+</span> Adicione seus alimentos
                </button>
              ) : (
                <ul className="food-list editing">
                  {meal.itens.map((it, idx) => (
                    <li className="food-row" key={`${it.alimento.id}-${idx}`}>
                      <div className="left">
                        <div className="name">{it.alimento.nome}</div>
                        <div className="meta">
                          {it.quantidade} {UNIDADE_MEDIDA_LABELS[it.unidade] || it.unidade}
                        </div>
                      </div>

                      <div className="right editing">
                        <div className="counter">
                          <button
                            type="button"
                            onClick={() => updateItemQty(mealKey, idx, it.quantidade - (it.unidade === 'GRAMA' ? 10 : 1))}
                            aria-label="Diminuir"
                            disabled={it.quantidade <= 1}
                          >
                            −
                          </button>
                          <span className="qty" aria-live="polite">{it.quantidade}</span>
                          <button
                            type="button"
                            onClick={() => updateItemQty(mealKey, idx, it.quantidade + (it.unidade === 'GRAMA' ? 10 : 1))}
                            aria-label="Aumentar"
                          >
                            +
                          </button>
                        </div>
                        <button
                          type="button"
                          className="remove-btn"
                          onClick={() => removeItemFromList(mealKey, idx)}
                          aria-label="Remover alimento"
                        >
                          ×
                        </button>
                      </div>
                    </li>
                  ))}
                </ul>
              )}

              {hasItems && (
                <div className="meal-actions">
                    <Button
                       title="Remover tudo"
                       onClick={() => removeMeal(mealKey)}
                       red
                     />
                    <Button
                      title="Atualizar Refeição"
                      onClick={() => saveMeal(mealKey)}
                    />
                </div>
              )}
            </div>
          );
        })}
      </section>

      <FoodModal
        isOpen={open}
        onClose={closeModal}
        activeFood={activeFood}
        selectedMealType={selectedMealType}
        onMealTypeChange={setSelectedMealType}
        unit={unit}
        onUnitChange={setUnit}
        amount={amount}
        onAmountChange={setAmount}
        onAddSelection={addSelection}
        loading={loading}
      />
    </main>
  );
}