
import { useState } from "react";
import styles from "./styles.module.scss";
import WorkoutModal from "../../components/WorkoutModal/WorkoutModal";
import { api } from "../../services/api";

/**
 * Renderiza a página de Treino.
 * Exibe uma lista de treinos e permite ao usuário adicionar um novo treino.
 */
export const Workout = () => {
  const [isModalOpen, setIsModalOpen] = useState(false);

  const handleOpenModal = () => {
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
  };

  return (
    <div className={styles.container}>
      <header className={styles.header}>
        <h1>Treinos</h1>
        <button onClick={handleOpenModal}>Adicionar Treino</button>
      </header>
      <WorkoutModal
        isOpen={isModalOpen}
        onClose={handleCloseModal}
        onWorkoutAdded={() => {}}
      />
    </div>
  );
};
