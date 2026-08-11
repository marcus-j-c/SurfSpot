import { useState } from "react";
import "./App.css";
import {ForecastCard} from './components/ForecastCard';
import {SearchBar} from './components/SearchBar';
import {AccountButtons} from './components/AccountButtons'
import {NavBar} from "./components/NavBar";
import { SideBar } from "./components/SideBar";
import { CentreHeader } from "./components/CentreHeader";

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
      <div className = "sun-moon-top-header">
        <div className = "sun-moon-orb"></div>
      </div>
      <div className="title-and-logo"><img src ="../favicon.svg"></img>SurfSpot</div> {/*temporary classes for grid*/}
      <div className="box-6">Box 6</div>
      <CentreHeader/>
      <SearchBar onSearch = {handleSearch} onType = {handleType} /> {/*the search bar component created here the propert on search is replaced by the handleSearch function from App.tsx*/}
      <AccountButtons/>
      <NavBar/>
      <SideBar/>
      <div className = "typed-text-display">
        <p>Current state of typedText: {typedText}</p>
        <p>Currently showing forecast for: {selectedBeach}</p>
      </div>
    </div>
    </>
  );
}

export default App
