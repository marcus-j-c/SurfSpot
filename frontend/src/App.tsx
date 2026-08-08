import { useState } from "react";
import "./App.css";
import {ForecastCard} from './components/ForecastCard';
import {SearchBar} from './components/SearchBar';

function App() {
  const [selectedBeach, setSelectedBeach] = useState("");
  const [typedText, setTypedText] = useState("");
  const handleSearch = (beachName: string) => {
    setSelectedBeach(beachName);};
  const handleType = (typedText: string) => {
    setTypedText(typedText);
  };
  return (
    <>
    <div className = "Overall-Grid">
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
      </div>
      <SearchBar onSearch = {handleSearch} onType = {handleType} /> {/*the search bar component created here the propert on search is replaced by the handleSearch function from App.tsx*/}
      <div className = "typed-text-display">
        <p>Current state of typedText: {typedText}</p>
        <p>Currently showing forecast for: {selectedBeach}</p>
      </div>
    </div>
    </>
  );
}

export default App
