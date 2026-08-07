import "../App.css";

export function ForecastCard() {
    const beachName: string = "Bonzai Pipeline";
    const waveHeight: number = 3.8;
    const windDirection: string = "ENE";
    const airTemp: number = 28.0;

    return (
        <div className="forecast-card">
            <h2>{beachName}</h2>
            <p>Wave Height: {waveHeight}m</p>
            <p>Wind Direction: {windDirection}</p>
            <p>Air Temperature: {airTemp}°C</p>
        </div>
    );
}