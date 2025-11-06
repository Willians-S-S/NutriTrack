import { BrowserRouter, Routes, Route, useLocation } from "react-router-dom";
import "./styles/global.scss";
import Header from "./components/Header/Header";
import Register from "./pages/Register/Register";
import Login from "./pages/Login/Login";
import Home from "./pages/Home/Home";
import Profile from "./pages/Profile/Profile";
import Progress from "./pages/Progress/Progress";
import Foods from "./pages/Foods/Foods";

/**
 * Envolve o componente principal da aplicação com o BrowserRouter para habilitar o roteamento.
 */
function AppWrapper() {
  return (
    <BrowserRouter>
      <App />
    </BrowserRouter>
  );
}

import ProtectedRoute from "./components/ProtectedRoute/ProtectedRoute";

/**
 * O componente principal da aplicação.
 * Ele configura as rotas e renderiza os componentes apropriados com base na URL atual.
 * Ele também renderiza condicionalmente o componente Header com base na rota.
 */
function App() {
  const location = useLocation();
  const noHeaderRoutes = ["/login", "/registrar"];
  const showHeader = !noHeaderRoutes.includes(location.pathname);

  return (
    <>
      {showHeader && <Header />}
      <Routes>
        <Route path="/registrar" element={<Register />} />
        <Route path="/login" element={<Login />} />
        <Route path="/" element={<ProtectedRoute><Home /></ProtectedRoute>} />
        <Route path="/profile" element={<ProtectedRoute><Profile /></ProtectedRoute>} />
        <Route path="/progress" element={<ProtectedRoute><Progress /></ProtectedRoute>} />
        <Route path="/foods" element={<ProtectedRoute><Foods /></ProtectedRoute>} />
      </Routes>
    </>
  );
}

export default AppWrapper;
