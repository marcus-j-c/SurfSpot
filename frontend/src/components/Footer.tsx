export default function Footer() {
  return (
    <footer className="app-footer">
      <p>
        Weather data provided by{' '}
        <a href="https://openweathermap.org/" target="_blank" rel="noopener noreferrer">
          OpenWeather
        </a>
        . Marine data provided by{' '}
        <a href="https://open-meteo.com/" target="_blank" rel="noopener noreferrer">
          Open-Meteo
        </a>{' '}
        (licensed under{' '}
        <a href="https://creativecommons.org/licenses/by/4.0/" target="_blank" rel="noopener noreferrer">
          CC-BY 4.0
        </a>
        ).
      </p>
      <p>
        Location data provided by{' '}
        <a href="https://locationiq.com/" target="_blank" rel="noopener noreferrer">
          LocationIQ
        </a>{' '}
        and{' '}
        <a href="https://www.openstreetmap.org/copyright" target="_blank" rel="noopener noreferrer">
          OpenStreetMap
        </a>{' '}
        (Nominatim).
      </p>
    </footer>
  );
}