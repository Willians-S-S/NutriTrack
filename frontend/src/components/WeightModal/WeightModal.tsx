import { useEffect } from "react";
import "./WeightModal.scss";

type WeightModalProps = {
  isOpen: boolean;
  onClose: (didUpdate?: boolean) => void;
};

export default function WeightModal({ isOpen, onClose }: WeightModalProps) {
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
      const response = await fetch(`${import.meta.env.VITE_API_URL}/api/v1/registros-peso/${userId}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        },
        body: JSON.stringify(payload),
      });

      if (response.ok) {
        onClose(true); // Signal that an update occurred
      } else {
        const errorData = await response.json().catch(() => ({}));
        console.error("Erro ao adicionar registro de peso:", errorData);
        alert("Ocorreu um erro ao adicionar o registro de peso. Verifique o console para mais detalhes.");
      }
    } catch (error) {
      console.error("Erro ao adicionar registro de peso:", error);
    }
  }

  useEffect(() => {
    const handleEsc = (e: KeyboardEvent) => {
      if (e.key === "Escape") onClose();
    };
    window.addEventListener("keydown", handleEsc);
    return () => window.removeEventListener("keydown", handleEsc);
  }, [onClose]);

  if (!isOpen) return null;

  return (
    <div className="modal-overlay" onClick={() => onClose()}>
      <div className="modal-content" onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <h2>Adicionar Novo Registro de Peso</h2>
          <button onClick={() => onClose()} className="close-button">&times;</button>
        </div>
        <div className="modal-body">
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
      </div>
    </div>
  );
}