import { useState } from "react";
import "../App.css";
import {ForecastCard} from '../components/ForecastCard';
import {SearchBar} from '../components/SearchBar';
import {AccountButtons} from '../components/AccountButtons'
import {NavBar} from "../components/NavBar";
import { SideBar } from "../components/SideBar";
import { CentreHeader } from "../components/CentreHeader";

export default function Home() {
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
      <div className="wave-top-header">
        <svg 
          className="waves" 
          xmlns="http://www.w3.org/2000/svg" 
          viewBox="0 24 150 28" 
          preserveAspectRatio="none" 
          shapeRendering="auto"
        >
          <defs>
            <path id="gentle-wave" d="M-160 44c30 0 58-18 88-18s 58 18 88 18 58-18 88-18 58 18 88 18 v44h-352z" />
          </defs>
          <g className="parallax">
            <use href="#gentle-wave" x="48" y="0" fill="rgba(255,255,255,0.7)" />
            <use href="#gentle-wave" x="48" y="3" fill="rgba(255,255,255,0.5)" />
            <use href="#gentle-wave" x="48" y="5" fill="rgba(255,255,255,0.3)" />
            <use href="#gentle-wave" x="48" y="7" fill="#fff" />
          </g>
        </svg>
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
