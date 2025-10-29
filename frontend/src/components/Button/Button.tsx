import "./Button.scss";
import type { MouseEventHandler, ReactNode } from "react";

const Button = ({
  title,
  onClick,
  white = false,
  red = false,
}: {
  title: ReactNode;
  onClick: MouseEventHandler<HTMLButtonElement>;
  white?: boolean;
  red?: boolean;
}) => {
  return (
    <button
      className={`custom-button ${white ? "custom-button--white" : ""} ${
        red ? "custom-button--red" : ""
      }`}
      onClick={onClick}
    >
      {title}
    </button>
  );
};

export default Button;