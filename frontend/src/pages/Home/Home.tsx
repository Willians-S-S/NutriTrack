import Button from "../../components/Button/Button";
import type { MouseEvent } from "react";
import { IoMdAddCircleOutline } from "react-icons/io";
import { LuWeight } from "react-icons/lu";
import { MdOutlineWaterDrop } from "react-icons/md";
import { MdFitnessCenter } from "react-icons/md";
import "./Home.scss";

function Home() {

  function teste(e: MouseEvent<HTMLButtonElement>) {
    e.preventDefault();
    console.log("botao funcionando");
  }

  return (
    <div className="dashboard">
      <h2 className="dashboard-title">Resumo do Dia</h2>

      <div className="dashboard-cards">
        <div className="card">
          <span className="card-label">Calorias Consumidas</span>
          <span className="card-value highlight">1,850 <span className="card-unit">kcal</span></span>
          
        </div>
        <div className="card">
          <span className="card-label">Calorias Restantes</span>
          <span className="card-value success">650 <span className="card-unit">kcal</span></span>
          
        </div>
        <div className="card">
          <span className="card-label">Peso Atual</span>
          <span className="card-value">68 <span className="card-unit">kg</span></span>
          
        </div>
      </div>

      <div className="dashboard-macro">
        <h3>Macronutrientes</h3>
        <div className="macro-content">
          <div className="macro-circle">
            <div className="macro-total">
              <span className="macro-amount">120g</span>
              <span className="macro-label">Total</span>
            </div>
          </div>
          <div className="macro-legend">
            <p>
              <span className="dot blue"></span> Carboidratos 
              <span className="value">50g (40%)</span>
            </p>
            <p>
              <span className="dot red"></span> Proteínas 
              <span className="value">45g (35%)</span>
            </p>
            <p>
              <span className="dot yellow"></span> Gorduras 
              <span className="value">25g (25%)</span>
            </p>
          </div>
        </div>
      </div>

      <div className="dashboard-actions">
        <Button title={<><IoMdAddCircleOutline size={24}/> Registrar Refeição</>} onClick={teste}></Button>
        <Button title={<><LuWeight size={24} /> Adicionar Peso</>} onClick={teste} white={true}></Button>
        <Button title={<><MdOutlineWaterDrop size={24} /> Consumo de Água</>} onClick={teste} white={true}></Button>
        <Button title={<><MdFitnessCenter size={24} /> Adicionar Exercício</>} onClick={teste} white={true}></Button>
      </div>
    </div>
  );
}

export default Home;
