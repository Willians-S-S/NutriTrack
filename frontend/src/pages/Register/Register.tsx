import React from "react";
import logo from '../../assets/logo-white.png'
import Button from '../../components/Button/Button'
import { Link } from "react-router-dom";
import "./Register.scss";

function teste() {
  console.log('botão funcionando')
}

const Register: React.FC = () => {
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
                <input type="text" id="name" placeholder="Seu nome completo" />
              </div>

              <div className="form-group">
                <label htmlFor="email">E-mail</label>
                <input type="email" id="email" placeholder="seuemail@exemplo.com" />
              </div>

              <div className="form-group">
                <label htmlFor="password">Senha</label>
                <input type="password" id="password" placeholder="Crie uma senha forte" />
              </div>

              <div className="form-group">
                <label htmlFor="confirmPassword">Confirmar senha</label>
                <input type="password" id="confirmPassword" placeholder="Confirme sua senha" />
              </div>

              <div className="form-inline">
                <div className="form-group">
                  <label htmlFor="age">Idade</label>
                  <input type="number" id="age" placeholder="Sua idade" />
                </div>
                <div className="form-group">
                  <label htmlFor="gender">Sexo</label>
                  <select id="gender">
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
                  <input type="number" id="weight" placeholder="Ex: 70.5" />
                </div>
                <div className="form-group">
                  <label htmlFor="height">Altura (cm)</label>
                  <input type="number" id="height" placeholder="Ex: 175" />
                </div>
              </div>

              <div className="form-group">
                <label>Nível de atividade física</label>
                <select>
                  <option>Selecione</option>
                  <option>Sedentário</option>
                  <option>Leve</option>
                  <option>Moderado</option>
                  <option>Intenso</option>
                </select>
              </div>

              <Button title='Cadastrar-se' onClick={teste}/>
            </form>
          </div>
        </div>
      </div>
    </>
  );
};

export default Register;
