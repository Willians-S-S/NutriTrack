import { BrowserRouter, Routes, Route, useLocation } from "react-router-dom";
import "./styles/global.scss";
import Header from "./components/Header/Header";
import Register from "./pages/Register/Register";
import Login from "./pages/Login/Login";
import Home from "./pages/Home/Home";
// import Meals from "./pages/Meals/Meals";
// import Progress from "./pages/Progress/Progress";
// import Profile from "./pages/Profile/Profile";

function AppWrapper() {
  return (
    <BrowserRouter>
      <App />
    </BrowserRouter>
  );
}

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
        <Route path="/" element={<Home />} />
        {/* <Route path="/alimentos" element={<Meals />} />
        <Route path="/progresso" element={<Progress />} />
        <Route path="/perfil" element={<Profile />} /> */}
      </Routes>
    </>
  );
}

export default AppWrapper;
