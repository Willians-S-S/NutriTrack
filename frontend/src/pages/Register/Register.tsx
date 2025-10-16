import React, { useState } from "react";
import logo from '../../assets/logo-white.png'
import Button from '../../components/Button/Button'
import { Link, useNavigate } from "react-router-dom";
import type { MouseEvent } from "react";
import "./Register.scss";

const Register: React.FC = () => {
  const [nome, setNome] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [age, setAge] = useState('');
  const [gender, setGender] = useState('');
  const [weight, setWeight] = useState('');
  const [height, setHeight] = useState('');
  const [activityLevel, setActivityLevel] = useState('');
  const [objective, setObjective] = useState('');
  const navigate = useNavigate();

  async function handleRegister(e: MouseEvent<HTMLButtonElement>) {
    e.preventDefault();

    if (!nome || !email || !password || !confirmPassword || !age || !gender || !weight || !height || !activityLevel || !objective) {
      alert("Por favor, preencha todos os campos.");
      return;
    }

    if (password !== confirmPassword) {
      alert("As senhas não conferem.");
      return;
    }

    const birthDate = new Date();
    birthDate.setFullYear(birthDate.getFullYear() - parseInt(age));

    const heightInMeters = parseFloat(height) / 100;

    const activityLevelMap: { [key: string]: string } = {
      'Sedentário': 'SEDENTARIO',
      'Leve': 'LEVE',
      'Moderado': 'MODERADO',
      'Intenso': 'INTENSO',
    };

    const objectiveMap: { [key: string]: string } = {
      'Perder peso': 'PERDER_PESO',
      'Manter peso': 'MANTER_PESO',
      'Ganhar peso': 'GANHAR_PESO',
    };

    const userRequestDTO = {
      nome,
      email,
      senha: password,
      alturaM: heightInMeters,
      dataNascimento: birthDate.toISOString().split('T')[0],
      nivelAtividade: activityLevelMap[activityLevel],
      objetivoUsuario: objectiveMap[objective],
      peso: parseFloat(weight),
    };

    try {
      const response = await fetch('/api/v1/auth/register', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(userRequestDTO),
      });

      if (response.ok) {
        alert("Cadastro realizado com sucesso!");
        navigate('/login');
      } else {
        const errorData = await response.json();
        alert(`Erro ao cadastrar: ${errorData.message}`);
      }
    } catch (error) {
      console.error('Erro ao cadastrar:', error);
      alert('Ocorreu um erro ao tentar cadastrar. Tente novamente.');
    }
  }

  return (
    <>
      <div className="container">
        <div className="register-container">
          <div className="register-left">
            <img src={logo} alt="Logo" className="logo"/>
            <p>Seu diário alimentar inteligente.</p>
          </div>
          <div className="register-right">
            <h2>Crie sua conta</h2>
            <p>
              Ou <Link to="/login">faça login</Link> se já tiver uma conta.
            </p>

            <form className="form">
              <div className="form-group">
                <label htmlFor="name">Nome</label>
                <input type="text" id="name" placeholder="Seu nome completo" value={nome} onChange={(e) => setNome(e.target.value)} />
              </div>

              <div className="form-group">
                <label htmlFor="email">E-mail</label>
                <input type="email" id="email" placeholder="seuemail@exemplo.com" value={email} onChange={(e) => setEmail(e.target.value)} />
              </div>

              <div className="form-group">
                <label htmlFor="password">Senha</label>
                <input type="password" id="password" placeholder="Crie uma senha forte" value={password} onChange={(e) => setPassword(e.target.value)} />
              </div>

              <div className="form-group">
                <label htmlFor="confirmPassword">Confirmar senha</label>
                <input type="password" id="confirmPassword" placeholder="Confirme sua senha" value={confirmPassword} onChange={(e) => setConfirmPassword(e.target.value)} />
              </div>

              <div className="form-inline">
                <div className="form-group">
                  <label htmlFor="age">Idade</label>
                  <input type="number" id="age" placeholder="Sua idade" value={age} onChange={(e) => setAge(e.target.value)} />
                </div>
                <div className="form-group">
                  <label htmlFor="gender">Sexo</label>
                  <select id="gender" value={gender} onChange={(e) => setGender(e.target.value)}>
                    <option>Selecione</option>
                    <option>Masculino</option>
                    <option>Feminino</option>
                    <option>Outro</option>
                  </select>
                </div>
              </div>

              <div className="form-inline">
                <div className="form-group">
                  <label htmlFor="weight">Peso (kg)</label>
                  <input type="number" id="weight" placeholder="Ex: 70.5" value={weight} onChange={(e) => setWeight(e.target.value)} />
                </div>
                <div className="form-group">
                  <label htmlFor="height">Altura (cm)</label>
                  <input type="number" id="height" placeholder="Ex: 175" value={height} onChange={(e) => setHeight(e.target.value)} />
                </div>
              </div>

              <div className="form-group">
                <label>Nível de atividade física</label>
                <select value={activityLevel} onChange={(e) => setActivityLevel(e.target.value)}>
                  <option>Selecione</option>
                  <option>Sedentário</option>
                  <option>Leve</option>
                  <option>Moderado</option>
                  <option>Intenso</option>
                </select>
              </div>

              <div className="form-group">
                <label>Objetivo</label>
                <select value={objective} onChange={(e) => setObjective(e.target.value)}>
                  <option>Selecione</option>
                  <option>Perder peso</option>
                  <option>Manter peso</option>
                  <option>Ganhar peso</option>
                </select>
              </div>

              <Button title='Cadastrar-se' onClick={handleRegister}/>
            </form>
          </div>
        </div>
      </div>
    </>
  );
};

export default Register;
