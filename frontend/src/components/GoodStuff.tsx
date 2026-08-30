import "../App.css";

interface GoodStuffProps {
  goodStuff: string;
}

export default function GoodStuff({goodStuff}: GoodStuffProps) {
  return (
    <div className="good-stuff">
      <h2 className="good-stuff-title">
        <span className="icon">✅</span>
        <span className="title-text">What's Good?</span>
      </h2>
      <p>{goodStuff}</p>
    </div>
  );
}