import "../App.css";

interface BadStuffProps {
  badStuff: string;
}

export default function BadStuff({badStuff}: BadStuffProps) {
  return (
    <div className="bad-stuff">
      <h2 className="bad-stuff-title">
        <span className="icon">⚠️</span>
        <span className="title-text">Watch out for:</span>
      </h2>
      <p>{badStuff}</p>
    </div>
  );
}