import { jwtDecode } from "jwt-decode";
import logo from "../../assets/logo-2.png"
import Button from "../../components/Button/Button";
import { Link, useNavigate } from "react-router-dom";
import { MouseEvent, useState } from "react";
import "./Login.scss";

/**
 * A página de login da aplicação.
 * @returns {JSX.Element} A página de login renderizada.
 */
function Login() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();

  async function handleLogin(e: MouseEvent<HTMLButtonElement>) {
    e.preventDefault();
    
    try {
      const response = await fetch('/api/v1/auth/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ email, password }),
      });

      if (response.ok) {
        const data = await response.json();
        localStorage.setItem('token', data.token);

        const decodedToken: { sub: string, name: string, userId: string } = jwtDecode(data.token);
        const userId = decodedToken.userId;
        const userName = decodedToken.name;

        localStorage.setItem('userId', userId);
        localStorage.setItem('userName', userName);

        navigate('/');
      } else {
        alert('E-mail ou senha inválidos');
      }
    } catch (error) {
      console.error('Erro ao fazer login:', error);
      alert('Ocorreu um erro ao tentar fazer login. Tente novamente.');
    }
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
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />

          <label htmlFor="password">Senha</label>
          <input 
            type="password" 
            id="password" 
            placeholder="Sua senha" 
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />

          <Button title='Login' onClick={handleLogin}/>
        </form>

        <p className="register">
          Não possui uma conta? <Link to="/registrar">Crie agora!</Link>
        </p>
      </div>
    </div>
  );
}

export default Login;
