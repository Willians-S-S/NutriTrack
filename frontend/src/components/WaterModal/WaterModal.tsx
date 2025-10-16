import React, { useState } from 'react';
import './WaterModal.scss';

type WaterLogModalProps = {
  isOpen: boolean;
  onClose: (didUpdate: boolean) => void;
};

const WaterLogModal: React.FC<WaterLogModalProps> = ({ isOpen, onClose }) => {
  const [quantidadeMl, setQuantidadeMl] = useState(250);
  const [dataMedicao, setDataMedicao] = useState(new Date().toISOString().split('T')[0]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  if (!isOpen) return null;

  const handleSave = async () => {
    const userId = localStorage.getItem('userId');
    const token = localStorage.getItem('token');
    if (!userId || !token) {
      setError('Usuário não autenticado.');
      return;
    }

    if (quantidadeMl <= 0 || !dataMedicao) {
      setError('Por favor, insira uma quantidade válida e a data.');
      return;
    }

    setLoading(true);
    setError(null);

    const payload = {
      quantidadeMl,
      dataMedicao,
    };

    try {
      const response = await fetch(`/api/v1/registros-agua/${userId}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        },
        body: JSON.stringify(payload),
      });

      if (response.ok) {
        onClose(true);
      } else {
        const errorData = await response.json().catch(() => ({}));
        const errorMessage = errorData.message || 'Erro ao registrar consumo de água.';
        setError(errorMessage);
        onClose(false);
      }
    } catch (err) {
      console.error('Erro de rede:', err);
      setError('Ocorreu um erro de rede. Tente novamente.');
      onClose(false);
    } finally {
      setLoading(false);
    }
  };

  const today = new Date().toISOString().split('T')[0];

  return (
    <div className="modal-backdrop" onClick={() => onClose(false)}>
      <div className="water-modal" onClick={(e) => e.stopPropagation()}>
        <div className="modal-head">
          <h3>Registrar Água</h3>
          <button className="close-x" onClick={() => onClose(false)} aria-label="Fechar">×</button>
        </div>

        <div className="modal-body">
          {error && <p className="error-message">{error}</p>}

          <div className="field-group">
            <label htmlFor="quantity">Quantidade (ml)</label>
            <div className="quantity-controls">
              <button
                type="button"
                onClick={() => setQuantidadeMl(v => Math.max(10, v - 50))}
                disabled={quantidadeMl <= 10 || loading}
              >
                −
              </button>
              <input
                id="quantity"
                type="number"
                min={1}
                step={50}
                value={quantidadeMl}
                onChange={(e) => setQuantidadeMl(Number(e.target.value))}
                disabled={loading}
              />
              <button
                type="button"
                onClick={() => setQuantidadeMl(v => v + 50)}
                disabled={loading}
              >
                +
              </button>
            </div>
          </div>

          <div className="field-group">
            <label htmlFor="date">Data</label>
            <input
              id="date"
              type="date"
              value={dataMedicao}
              max={today}
              onChange={(e) => setDataMedicao(e.target.value)}
              disabled={loading}
            />
          </div>

        </div>

        <div className="modal-actions">
          <button
            className="ghost-btn"
            onClick={() => onClose(false)}
            disabled={loading}
            type="button"
          >
            Cancelar
          </button>
          <button
            className="save-btn"
            onClick={handleSave}
            disabled={loading}
            type="button"
          >
            {loading ? 'Salvando...' : 'Adicionar Registro'}
          </button>
        </div>
      </div>
    </div>
  );
};

export default WaterLogModal;