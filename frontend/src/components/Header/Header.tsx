import "./Header.scss";
import logo from "../../assets/logo.png";
import { BsBell } from "react-icons/bs";
import { CgProfile } from "react-icons/cg";
import { NavLink } from "react-router-dom";

function Header() {
  return (
    <header className="header">
      <div className="logo">
        <img src={logo} alt="Logo" />
      </div>

      <nav className="nav">
        <NavLink 
          to="/" 
          className={({ isActive }) => (isActive ? "active" : "")}
        >
          Home
        </NavLink>
        <NavLink 
          to="/alimentos" 
          className={({ isActive }) => (isActive ? "active" : "")}
        >
          Alimentos
        </NavLink>
        <NavLink 
          to="/progresso" 
          className={({ isActive }) => (isActive ? "active" : "")}
        >
          Progresso
        </NavLink>
      </nav>

      <div className="actions">
        <div className="notification">
          <i><BsBell /></i>
          <span className="badge"></span>
        </div>
        <i><CgProfile /></i>
      </div>
    </header>
  );
}

export default Header;
