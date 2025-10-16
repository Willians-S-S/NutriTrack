import Button from "../../components/Button/Button";
import { useEffect, useState, useCallback } from "react";
import { IoMdAddCircleOutline } from "react-icons/io";
import { LuWeight } from "react-icons/lu";
import { MdOutlineWaterDrop } from "react-icons/md";
import { useNavigate } from "react-router-dom";
import WaterModal from "../../components/WaterModal/WaterModal";
import "./Home.scss";

type MacroData = {
  calorias: number;
  proteinasG: number;
  carboidratosG: number;
  gordurasG: number;
};

type HomeData = {
  currentWeight: number | null;
  dailyMacros: MacroData;
  dailyWaterMl: number;
  targetCalories: number;
};

function Home() {
  const [data, setData] = useState<HomeData | null>(null);
  const [loading, setLoading] = useState(true);
  const [isWaterModalOpen, setIsWaterModalOpen] = useState(false);
  const navigate = useNavigate();

  const getTodayRange = useCallback(() => {
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    const start = today.toISOString();
    today.setHours(23, 59, 59, 999);
    const end = today.toISOString();
    const dateOnly = new Date().toISOString().split('T')[0];
    return { start, end, dateOnly };
  }, []);

  const fetchHomeData = useCallback(async () => {
    const userId = localStorage.getItem('userId');
    const token = localStorage.getItem('token');
    if (!userId || !token) {
      setLoading(false);
      navigate('/login');
      return;
    }

    setLoading(true);
    const { start, end, dateOnly } = getTodayRange();
    const headers = { 'Authorization': `Bearer ${token}` };

    try {
      const userResponse = await fetch(`/api/v1/usuarios/${userId}`, { headers });
      const userData = userResponse.ok ? await userResponse.json() : { peso: null };
      const currentWeight = userData.peso ?? null;

      const mealsResponse = await fetch(`/api/v1/refeicoes/usuario/${userId}?start=${start}&end=${end}`, { headers });
      const mealsData = mealsResponse.ok ? await mealsResponse.json() : [];

      let dailyMacros: MacroData = { calorias: 0, proteinasG: 0, carboidratosG: 0, gordurasG: 0 };
      mealsData.forEach((meal: any) => {
        dailyMacros.calorias += meal.totalCalorias ? parseFloat(meal.totalCalorias) : 0;
        dailyMacros.proteinasG += meal.totalProteinasG ? parseFloat(meal.totalProteinasG) : 0;
        dailyMacros.carboidratosG += meal.totalCarboidratosG ? parseFloat(meal.totalCarboidratosG) : 0;
        dailyMacros.gordurasG += meal.totalGordurasG ? parseFloat(meal.totalGordurasG) : 0;
      });

      const waterResponse = await fetch(`/api/v1/registros-agua/summary/${userId}?start=${dateOnly}&end=${dateOnly}`, { headers });
      const waterData = waterResponse.ok ? await waterResponse.json() : [];
      const dailyWaterMl = waterData.length > 0 ? waterData[0].totalQuantidadeMl : 0;

      const targetCalories = 2500;

      setData({
        currentWeight,
        dailyMacros,
        dailyWaterMl,
        targetCalories,
      });

    } catch (error) {
      console.error("Erro ao buscar dados da Home:", error);
    } finally {
      setLoading(false);
    }
  }, [getTodayRange, navigate]);

  useEffect(() => {
    fetchHomeData();
  }, [fetchHomeData]);

  const handleCloseWaterModal = (didUpdate: boolean) => {
    setIsWaterModalOpen(false);
    if (didUpdate) {
      fetchHomeData();
    }
  };


  if (loading || !data) {
    return <div className="dashboard loading">Carregando resumo do dia...</div>;
  }

  const caloriesConsumed = Math.round(data.dailyMacros.calorias);
  const caloriesRemaining = Math.max(0, data.targetCalories - caloriesConsumed);

  const totalMacros = data.dailyMacros.proteinasG + data.dailyMacros.carboidratosG + data.dailyMacros.gordurasG;

  const proteinPct = totalMacros > 0 ? (data.dailyMacros.proteinasG / totalMacros) * 100 : 0;
  const carbsPct = totalMacros > 0 ? (data.dailyMacros.carboidratosG / totalMacros) * 100 : 0;
  const fatPct = totalMacros > 0 ? (data.dailyMacros.gordurasG / totalMacros) * 100 : 0;

  const conicGradient = `conic-gradient(
    #2f80ed 0% ${carbsPct}%,
    #eb5757 ${carbsPct}% ${carbsPct + proteinPct}%,
    #f2c94c ${carbsPct + proteinPct}% 100%
  )`;

  const formatNum = (num: number) => num.toFixed(1).replace('.', ',');
  const formatPct = (pct: number) => pct.toFixed(0);

  return (
    <>
      <div className="dashboard">
        <h2 className="dashboard-title">Resumo do Dia</h2>

        <div className="dashboard-cards">
          <div className="card">
            <span className="card-label">Calorias Consumidas</span>
            <span className="card-value highlight">{caloriesConsumed.toLocaleString('pt-BR')} <span className="card-unit">kcal</span></span>
          </div>
          <div className="card">
            <span className="card-label">Calorias Restantes (Meta: {data.targetCalories} kcal)</span>
            <span className="card-value success">{caloriesRemaining.toLocaleString('pt-BR')} <span className="card-unit">kcal</span></span>
          </div>
          <div className="card">
            <span className="card-label">Peso Atual</span>
            <span className="card-value">{data.currentWeight ? formatNum(data.currentWeight) : '—'} <span className="card-unit">kg</span></span>
          </div>
          <div className="card">
            <span className="card-label">Água Consumida</span>
            <span className="card-value water">{data.dailyWaterMl.toLocaleString('pt-BR')} <span className="card-unit">ml</span></span>
          </div>
        </div>

        <div className="dashboard-macro">
          <h3>Macronutrientes</h3>
          <div className="macro-content">
            <div className="macro-circle" style={{ background: conicGradient }}>
              <div className="macro-total">
                <span className="macro-amount">{Math.round(totalMacros)}g</span>
                <span className="macro-label">Total Consumido</span>
              </div>
            </div>
            <div className="macro-legend">
              <p>
                <span className="dot blue"></span> Carboidratos
                <span className="value">{formatNum(data.dailyMacros.carboidratosG)}g ({formatPct(carbsPct)}%)</span>
              </p>
              <p>
                <span className="dot red"></span> Proteínas
                <span className="value">{formatNum(data.dailyMacros.proteinasG)}g ({formatPct(proteinPct)}%)</span>
              </p>
              <p>
                <span className="dot yellow"></span> Gorduras
                <span className="value">{formatNum(data.dailyMacros.gordurasG)}g ({formatPct(fatPct)}%)</span>
              </p>
            </div>
          </div>
        </div>

        <div className="dashboard-actions">
          <Button title={<><IoMdAddCircleOutline size={24}/> Registrar Refeição</>} onClick={() => navigate('/foods')}></Button>
          <Button title={<><LuWeight size={24} /> Adicionar Peso</>} onClick={() => navigate('/progress')} white={true}></Button>
          <Button title={<><MdOutlineWaterDrop size={24} /> Consumo de Água</>} onClick={() => setIsWaterModalOpen(true)} white={true}></Button>
        </div>
      </div>

      <WaterModal
        isOpen={isWaterModalOpen}
        onClose={handleCloseWaterModal}
      />
    </>
  );
}

export default Home;