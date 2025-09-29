import "./Button.scss";
import type { MouseEventHandler } from "react";

const Button = ({ title, onClick }: { title: string; onClick: MouseEventHandler<HTMLButtonElement> }) => {
  return (
    <button className="custom-button" onClick={onClick}>
      {title}
    </button>
  );
};

export default Button;
