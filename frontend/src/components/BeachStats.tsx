import "../App.css";

interface BeachStatsProps {
    waveHeight: number;
    wavePeriod: number;
    windSpeed: number;
    windDirection: string;
    tide: number;
    waterTemp: number;
    weather: string;
}

export default function BeachStats({ waveHeight, wavePeriod, windSpeed, windDirection, tide, waterTemp, weather }: BeachStatsProps) {
    return (
        <div className = "beach-stats">
            <div className = "beach-stats-item">
                <h2>Wave Height</h2>
                <p>{waveHeight} m</p>
            </div>
            <div className = "beach-stats-item">
                <h2>Wave Period</h2>
                <p>{wavePeriod} s</p>
            </div>
            <div className = "beach-stats-item">
                <h2>Wind Speed & Direction</h2>
                <p>{windSpeed} m/s, {windDirection}</p>
            </div>
            <div className = "beach-stats-item">
                <h2>Tide</h2>
                <p>{tide} m</p>
            </div>
            <div className = "beach-stats-item">
                <h2>Water Temperature</h2>
                <p>{waterTemp} °C</p>
            </div>
            <div className = "beach-stats-item">
                <h2>Weather Conditions</h2>
                <p className="beach-stats-weather">{weather}</p>
            </div>
        </div>
    );
}