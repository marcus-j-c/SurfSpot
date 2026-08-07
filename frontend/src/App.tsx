import "./App.css";
import {ForecastCard} from './components/ForecastCard';
import {SearchBar} from './components/SearchBar';

function App() {
  return (
    <>
    <div className = "welcome">
      <h1>Hello Surfspot!</h1>
      <ForecastCard/>
      <SearchBar/>
    </div>
    </>
  );
}

export default App
