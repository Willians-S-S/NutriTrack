import { BrowserRouter, Routes, Route, useLocation } from "react-router-dom";
import "./styles/global.scss";
import Header from "./components/Header/Header";
import Register from "./pages/Register/Register";
import Login from "./pages/Login/Login";
import Home from "./pages/Home/Home";
import Profile from "./pages/Profile/Profile";
import Progress from "./pages/Progress/Progress";
import Foods from "./pages/Foods/Foods";

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
        <Route path="/profile" element={<Profile />} />
        <Route path="/progress" element={<Progress />} />
        <Route path="/foods" element={<Foods />} />
      </Routes>
    </>
  );
}

export default AppWrapper;
