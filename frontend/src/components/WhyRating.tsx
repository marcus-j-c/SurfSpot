import "../App.css";

interface WhyRatingProps {
    reasoning: string;
    rating: number;
}

export default function WhyRating({ reasoning, rating }: WhyRatingProps) {
  return (
    <div className="why-rating">
      <h2>
        Why <span className="score">{rating}/10</span>?
      </h2>
      <p>{reasoning}</p>
    </div>
  );
}