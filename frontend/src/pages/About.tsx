import "../App.css";

export default function About() {
    return (
        <div className="about-grid">
            <div className="about">
                <h1>About SurfSpot</h1>
                <h2>What is SurfSpot?</h2>
                <p>SurfSpot is a surf forecasting web app that provides real-time data, simplifying multiple stats into a single rating and simple insights so surfers know if it's worth calling in sick.</p>
                <h2>Why I built SurfSpot</h2>
                <p>I built SurfSpot as I was getting into surfing and needed a tool that could give me instant forecasts, in a format I actually wanted, no matter where in the world I am. I wanted to learn full-stack development by building something real from scratch rather than just copying tutorials, so I decided to build SurfSpot end-to-end as a 16-day sprint.</p>
                <h2>Tech Stack & Architecture</h2>
                <ul className="tech-list">
                    <li>
                    <strong>Frontend:</strong> I built the frontend with React, TypeScript, and I went for a glassmorphism style via App.css. I used React Router for persistent layouts and routing, and I built a custom animation for the center header of the homepage.
                    </li>
                    <li>
                    <strong>Backend:</strong> I built the backend with Java Spring Boot REST endpoints, using Java Records (DTOs) and custom string normalisation to handle user inputs. I used a multi-fallback strategy: Open-Meteo first (sorted by population), falling back to Nominatim coastal search (sorted by importance), and recursively stripping suffixes like "beach" or "spot" if the initial searches fail.  
                    </li>
                    <li>
                    <strong>Scoring Algorithm & Insights:</strong> I built a custom algorithm that converts the raw marine and weather data from Open-Meteo into a single rating out of 10. Then I take that rating, and using a formula I created, I weight the ratings, and connect them (some are dependant on others) to create the final spot rating. I also wrote custom logic that creates reasoning, and  understandable insights based off the raw data.
                    </li>
                </ul>
            </div>
        </div>
    );
}