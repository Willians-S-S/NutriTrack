import "./Button.scss";
import type { MouseEventHandler, ReactNode } from "react";

const Button = ({
  title,
  onClick,
  white = false,
}: {
  title: ReactNode;
  onClick: MouseEventHandler<HTMLButtonElement>;
  white?: boolean;
}) => {
  return (
    <button
      className={`custom-button ${white ? "custom-button--white" : ""}`}
      onClick={onClick}
    >
      {title}
    </button>
  );
};

export default Button;
