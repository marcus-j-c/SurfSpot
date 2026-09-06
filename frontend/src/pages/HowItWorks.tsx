import "../App.css";

export default function HowItWorks() {
    return (
        <div className="how-it-works-grid">
            <div className="how-it-works">
                <h1>How It Works</h1>
                <h2>Search:</h2>
                <p>Use the search bar to find any surfing spot in the world.</p>
                <h2>Locate & Fetch Conditions:</h2>
                <p>SurfSpot finds the spot's exact coordinates, then gathers the current conditions of that spot using external APIs.</p>
                <h2>Analyse:</h2>
                <p>Finally, SurfSpot calculates a 1-10 rating for the spot via our algorithm that evaluates wave height, period, water temperature, wind speed, and weather conditions. Then SurfSpot provides reasoning for that score, including a breakdown of what is good and what to watch out for.</p>
            </div>
        </div>
    );
}