import "../App.css";
import {useParams} from "react-router-dom";
import {BeachHeader} from "../components/BeachHeader";
import BeachStats from "../components/BeachStats";
import {usestate, useEffect} from "react";

interface BeachData {
  id: number;
  name: string;
  rating: number;
}

export default function BeachDetail() {
  const {beachName} = useParams<{beachName: string}>(); //save the recieved parameter as a variable named beachName
  const [currentBeach, setCurrentBeach] = useState<BeachData | null>(null); //state variable to hold the current beach data, can either hold a valid beach data object or be null.
  const [isLoading, setIsLoading] = useState<boolean>(true); //waits for the data to be fetched before rendering the page, initially set to true, while true can show a placeholder like Loading...
  const cleanBeachName = (beachName?.replace(/-/g, " ").replace(/\b\w/g, char => char.toUpperCase()) ?? "Unknown Beach"); //replace hyphens with spaces, and capitalise the first letter of each word to match the beach names in the data, and falls back to "Unknown Beach".
  const url = `http://localhost:5000/beaches?name=${encodeURIComponent(cleanBeachName)}`; //construct the url so go to /beaches which accesses the json packet named beaches the search for the entry where the name matches cleanBeachName.
  
  return (
    <div className = "beach-page-grid">
      <div className = "beach-header">
        <BeachHeader beachName = {getBeachName(currentBeach)} beachRating = {getBeachRating(currentBeach)}/>
      </div><BeachStats/>
    </div>
  );
}