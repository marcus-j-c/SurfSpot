import { useState } from "react";
import "./App.css";
import {ForecastCard} from './components/ForecastCard';
import {SearchBar} from './components/SearchBar';
import {AccountButtons} from './components/AccountButtons'

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
    <div className = "overall-grid">
      <div className="box-1">Box 1</div> {/*temporary classes for grid*/}
      <div className="box-2">Box 2</div>
      <div className="box-4">Box 4</div>
      <div className="centre-header">Is it worth surfing today?</div>
      <div className="box-6">Box 6</div>
      <div className="box-7">Box 7</div>
    {/*<div className = "welcome">
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
      </div>*/}
      <SearchBar onSearch = {handleSearch} onType = {handleType} /> {/*the search bar component created here the propert on search is replaced by the handleSearch function from App.tsx*/}
      <AccountButtons/>
      <div className = "typed-text-display">
        <p>Current state of typedText: {typedText}</p>
        <p>Currently showing forecast for: {selectedBeach}</p>
      </div>
    </div>
    </>
  );
}

export default App
