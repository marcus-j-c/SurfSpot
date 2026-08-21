import "../App.css";

interface BeachHeaderProps {
    beachName: string;
    beachRating: number;
}

export function BeachHeader(props: BeachHeaderProps) {
    return (
        <h1 className = "beach-header">
            {props.beachName}
            {": "}
            <span className = "beach-rating">
                {props.beachRating}/10
            </span>
        </h1>
    );
}