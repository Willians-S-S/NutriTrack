import { Navigate } from "react-router-dom";

/**
 * Um componente que protege uma rota de acesso não autenticado.
 * Se o usuário não estiver autenticado, ele redireciona para a página de login.
 * @param {object} props As propriedades para o componente.
 * @param {JSX.Element} props.children Os filhos a serem renderizados se o usuário estiver autenticado.
 * @returns {JSX.Element} O componente renderizado.
 */
const ProtectedRoute = ({ children }: { children: JSX.Element }) => {
  const token = localStorage.getItem("token");

  if (!token) {
    return <Navigate to="/login" replace />;
  }

  return children;
};

export default ProtectedRoute;