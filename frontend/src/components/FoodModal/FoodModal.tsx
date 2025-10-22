import { useMemo } from "react";
import Button from "../Button/Button";
import { MEAL_TITLES, UNIDADE_MEDIDA_LABELS, type MealKey, type MeasureKey } from "../../utils/Constants";
import "./FoodModal.scss";

const MOCK_MEASURES: { key: MeasureKey; label: string }[] = [
  { key: "GRAMA", label: "gramas" },
  { key: "MILILITRO", label: "ml" },
  { key: "UNIDADE", label: "unidade" },
];

const MACRO_BASE_DIVISOR = 100;

interface AlimentoResponseDTO {
  id: string;
  nome: string;
  calorias: number;
  proteinasG: number;
  carboidratosG: number;
  gordurasG: number;
}

interface FoodModalProps {
  isOpen: boolean;
  onClose: () => void;
  activeFood: AlimentoResponseDTO | null;
  selectedMealType: MealKey | null;
  onMealTypeChange: (mealType: MealKey) => void;
  unit: MeasureKey;
  onUnitChange: (unit: MeasureKey) => void;
  amount: number;
  onAmountChange: (amount: number) => void;
  onAddSelection: () => void;
  loading?: boolean;
}

export default function FoodModal({
  isOpen,
  onClose,
  activeFood,
  selectedMealType,
  onMealTypeChange,
  unit,
  onUnitChange,
  amount,
  onAmountChange,
  onAddSelection,
  loading = false
}: FoodModalProps) {
  const totalKcal = useMemo(() =>
    activeFood ? Math.round((activeFood.calorias / MACRO_BASE_DIVISOR) * amount) : 0,
    [activeFood, amount]
  );

  const totalCarbs = useMemo(() =>
    activeFood ? +((activeFood.carboidratosG / MACRO_BASE_DIVISOR) * amount).toFixed(1) : 0,
    [activeFood, amount]
  );

  const totalFat = useMemo(() =>
    activeFood ? +((activeFood.gordurasG / MACRO_BASE_DIVISOR) * amount).toFixed(1) : 0,
    [activeFood, amount]
  );

  const totalProtein = useMemo(() =>
    activeFood ? +((activeFood.proteinasG / MACRO_BASE_DIVISOR) * amount).toFixed(1) : 0,
    [activeFood, amount]
  );

  if (!isOpen || !activeFood) return null;

  return (
    <div className="modal-backdrop" onClick={onClose}>
      <div className="modal" onClick={(e) => e.stopPropagation()}>
        <div className="modal-head">
          <h3>{activeFood.nome}</h3>
          <button className="close-x" onClick={onClose} aria-label="Fechar">
            ×
          </button>
        </div>

        <div className="modal-body">
          <div className="grid">
            <div className="row">
              <label>Unidade</label>
              <select
                value={unit}
                onChange={(e) => onUnitChange(e.target.value as MeasureKey)}
              >
                {MOCK_MEASURES.map((m) => (
                  <option key={m.key} value={m.key}>
                    {UNIDADE_MEDIDA_LABELS[m.key]}
                  </option>
                ))}
              </select>
            </div>

            <div className="row">
              <label>Quantidade ({UNIDADE_MEDIDA_LABELS[unit]})</label>
              <div className="qty">
                <button
                  type="button"
                  onClick={() => onAmountChange(Math.max(1, amount - 10))}
                >
                  −
                </button>
                <input
                  type="number"
                  min={1}
                  value={amount}
                  onChange={(e) => onAmountChange(Math.max(1, Number(e.target.value)))}
                />
                <button
                  type="button"
                  onClick={() => onAmountChange(amount + 10)}
                >
                  +
                </button>
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
            <div className="meal-pills">
              {(Object.keys(MEAL_TITLES) as MealKey[]).map((m) => (
                <button
                  key={m}
                  type="button"
                  className={`pill ${selectedMealType === m ? "on" : ""}`}
                  onClick={() => onMealTypeChange(m)}
                >
                  {MEAL_TITLES[m]}
                </button>
              ))}
            </div>
          </div>
        </div>

        <div className="modal-actions">
          <Button
            title="Cancelar"
            onClick={onClose}
            red
          />
          <Button
            title="Adicionar"
            onClick={onAddSelection}
            disabled={!selectedMealType || loading}
          />
        </div>
      </div>
    </div>
  );
}

function Macro({ label, value }: { label: string; value: string }) {
  return (
    <div className="macro">
      <span className="m-label">{label}</span>
      <span className="m-value">{value}</span>
    </div>
  );
}