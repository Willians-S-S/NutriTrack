import logo from "../../assets/logo-2.png"
import Button from "../../components/Button/Button";
import { Link } from "react-router-dom";
import type { MouseEvent } from "react";
import "./Login.scss";

function Login() {

  function teste(e: MouseEvent<HTMLButtonElement>) {
    e.preventDefault();
    console.log("botao funcionando");
  }
  
  return (
    <div className="login-container">
      <div className="login-card">
        <img src={logo} alt="Logo" className="logo"/>
        <p className="subtitle">Seu diário alimentar inteligente</p>

        <h3 className="form-title">Acesse sua conta</h3>

        <form className="login-form">
          <label htmlFor="email">E-mail</label>
          <input
            type="email"
            id="email"
            placeholder="seuemail@exemplo.com"
          />

          <label htmlFor="password">Senha</label>
          <input type="password" id="password" placeholder="Sua senha" />

          <Button title='Login' onClick={teste}/>
        </form>

        <p className="register">
          Não possui uma conta? <Link to="/registrar">Crie agora!</Link>
        </p>
      </div>
    </div>
  );
}

export default Login;
