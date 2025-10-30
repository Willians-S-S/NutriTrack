import "./Header.scss";
import logo from "../../assets/logo.png";
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
          to="/foods" 
          className={({ isActive }) => (isActive ? "active" : "")}
        >
          Alimentos
        </NavLink>
        <NavLink 
          to="/progress" 
          className={({ isActive }) => (isActive ? "active" : "")}
        >
          Progresso
        </NavLink>
      </nav>

      <div className="actions">
        <NavLink to="/profile" className="profile-link" aria-label="Profile">
          <i><CgProfile /></i>
        </NavLink>
      </div>
    </header>
  );
}

export default Header;
