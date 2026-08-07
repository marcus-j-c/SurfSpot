import "./App.css";
import {ForecastCard} from './components/ForecastCard';
import {SearchBar} from './components/SearchBar';

function App() {
  return (
    <>
    <div className = "welcome">
      <h1>Hello Surfspot!</h1>
      <ForecastCard 
        beachName = "Bonzai Pipeline"
        waveHeight = {3.8}
        windDirection ="ENE"
        airTemp = {28.0}
      />
      <ForecastCard
        beachName = "Bells Beach"
        waveHeight = {2.3}
        windDirection = "W"
        airTemp = {15.0}
        />
        <ForecastCard
        beachName = "Jeffreys Bay"
        waveHeight = {1.6}
        windDirection = "SW"
        airTemp = {19.0}
        />
      <SearchBar/>
    </div>
    </>
  );
}

export default App
