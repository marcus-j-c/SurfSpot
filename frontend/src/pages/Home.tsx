import { useState } from "react";
import "../App.css";
import {ForecastCard} from '../components/ForecastCard';
import {SearchBar} from '../components/SearchBar';
import {SideBar} from "../components/SideBar";
import {CentreHeader} from "../components/CentreHeader";

export default function Home() {
  const [selectedBeach, setSelectedBeach] = useState("");
  const [typedText, setTypedText] = useState("");
  const handleSearch = (beachName: string) => {
    setSelectedBeach(beachName);};
  const handleType = (typedText: string) => {
    setTypedText(typedText);
  };
  return (
    <div className="home-grid">
      <CentreHeader/>
      <SearchBar onSearch = {handleSearch} onType = {handleType} /> {/*the search bar component created here the propert on search is replaced by the handleSearch function from App.tsx*/}
      <SideBar/>
      <div className = "typed-text-display">
        <p>Current state of typedText: {typedText}</p>
        <p>Currently showing forecast for: {selectedBeach}</p>
      </div>
    </div>
  );
}
