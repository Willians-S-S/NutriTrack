import "./Button.scss";
import type { MouseEventHandler, ReactNode } from "react";

const Button = ({
  title,
  onClick,
  white = false,
  red = false,
  disabled = false,
}: {
  title: ReactNode;
  onClick: MouseEventHandler<HTMLButtonElement>;
  white?: boolean;
  red?: boolean;
  disabled?: boolean;
}) => {
  return (
    <button
      className={`custom-button ${white ? "custom-button--white" : ""} ${
        red ? "custom-button--red" : ""
      }`}
      onClick={onClick}
      disabled={disabled}
    >
      {title}
    </button>
  );
};

export default Button;