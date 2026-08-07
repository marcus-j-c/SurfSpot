import "../App.css";

interface ForecastCardProps {
    beachName: string;
    waveHeight: number;
    windDirection: string;
    airTemp: number;
}

export function ForecastCard(props: ForecastCardProps) {
    return (
        <div className="forecast-card">
            <h2>{props.beachName}</h2>
            <p>Wave Height: {props.waveHeight}m</p>
            <p>Wind Direction: {props.windDirection}</p>
            <p>Air Temperature: {props.airTemp}°C</p>
        </div>
    );
}