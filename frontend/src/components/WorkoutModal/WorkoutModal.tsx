import Button from "../Button/Button";
import "./WorkoutModal.scss";

interface WorkoutModalProps {
  isOpen: boolean;
  onClose: () => void;
  onWorkoutAdded: () => void;
}

/**
 * Um componente de modal para adicionar um novo treino.
 * @param {WorkoutModalProps} props As propriedades para o componente.
 * @returns {JSX.Element | null} O componente renderizado.
 */
export default function WorkoutModal({
  isOpen,
  onClose,
  onWorkoutAdded,
}: WorkoutModalProps) {
  if (!isOpen) return null;

  return (
    <div className="modal-backdrop" onClick={onClose}>
      <div className="modal" onClick={(e) => e.stopPropagation()}>
        <div className="modal-head">
          <h3>Adicionar Treino</h3>
          <button className="close-x" onClick={onClose} aria-label="Fechar">
            ×
          </button>
        </div>

        <div className="modal-body">
          <p>Formulário de treino aqui</p>
        </div>

        <div className="modal-actions">
          <Button
            title="Cancelar"
            onClick={onClose}
            red
          />
          <Button
            title="Adicionar"
            onClick={onWorkoutAdded}
          />
        </div>
      </div>
    </div>
  );
}