import "./Button.scss";
import type { MouseEventHandler, ReactNode } from "react";

/**
 * Um componente de botão customizado.
 * @param {object} props As propriedades para o componente.
 * @param {ReactNode} props.title O título do botão.
 * @param {MouseEventHandler<HTMLButtonElement>} props.onClick O manipulador de clique para o botão.
 * @param {boolean} [props.white=false] Se o botão deve ser branco.
 * @param {boolean} [props.red=false] Se o botão deve ser vermelho.
 * @returns {JSX.Element} O componente de botão renderizado.
 */
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