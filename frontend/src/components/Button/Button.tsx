import "./Button.scss";

const Button = ({ title, onClick }: { title: string; onClick: () => void }) => {
  return (
    <button className="custom-button" onClick={onClick}>
      {title}
    </button>
  );
};

export default Button;
