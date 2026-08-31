import "../App.css";
import {useParams} from "react-router-dom";
import {BeachHeader} from "../components/BeachHeader";
import BeachStats from "../components/BeachStats";
import {useState, useEffect} from "react";
import WhyRating from "../components/WhyRating";
import GoodStuff from "../components/GoodStuff";
import BadStuff from "../components/BadStuff";

interface BeachData {
  id: number;
  name: string;
  rating: number;
  waveHeight: number;
  wavePeriod: number;
  windSpeed: number;
  windDirection: string;
  tide: number;
  waterTemp: number;
  weather: string;
  reasoning: string;
  goodStuff: string;
  badStuff: string;
}

export default function BeachDetail() {
  const {beachName} = useParams<{beachName: string}>(); //save the recieved parameter as a variable named beachName
  const [currentBeach, setCurrentBeach] = useState<BeachData | null>(null); //state variable to hold the current beach data, can either hold a valid beach data object or be null.
  const [isLoading, setIsLoading] = useState<boolean>(true); //waits for the data to be fetched before rendering the page, initially set to true, while true can show a placeholder like Loading...
  //const cleanBeachName = (beachName?.split("-").map(word => word.charAt(0).toUpperCase() + word.slice(1)).join(" ")) ?? "Unknown Beach"; //split the beach name by hyphens, capitalize the first letter of each word, and join them back with spaces, and falls back to "Unknown Beach". NOW REDUNDANT BC I DO THE CLEANING ON THE BACKEND.
  //const url = `http://localhost:5000/beaches?name=${encodeURIComponent(cleanBeachName)}`; //construct the url so go to /beaches which accesses the json packet named beaches the search for the entry where the name matches cleanBeachName.
  const url = `http://localhost:8080/beaches?name=${encodeURIComponent(beachName ?? "")}`; //points to my real backend, and uses raw name cause i do the cleaning on the backend!!!
  /*useEffect(() => { //FOR THE OLD FAKE BACKEND.
    fetch(url) //fetch the data from the url
    .then((response) => response.json()) //parse the response as json
    .then ((data) => {
      if (data.length > 0) { //check if the data array has any entries
        setCurrentBeach(data[0]); //if it does, set the currentBeach state to the first entry in the data array
      }
      else {
        setCurrentBeach(null); //if it doesn't, set the currentBeach state to null
      }
    })
    .catch((error) => {
      console.error("Error fetching beach data:", error); //log any errors that occur during the fetch
    })
    .finally(() => {
      setIsLoading(false); //set isLoading to false once the fetch is complete, regardless of success or failure
    });
  }, [url]);*/

  useEffect(() => { //FOR THE REAL BACKEND.
    fetch(url)
    .then((response) => response.json()) //parse the response as json
    .then((data: BeachData) => { //the data returned from the backend will be of type BeachData, aka it will match my interface BeachData
      setCurrentBeach(data);
    })
    .catch((error) => {
      console.error("Error fetching beach data:", error); //log any errors that occur during the fetch
    })
    .finally(() => {
      setIsLoading(false); //set isLoading to false once the fetch is complete, regardless of success or failure
    });
  }, [url]);

  return (
    <div className = "beach-page-grid">
      <div className = "beach-header">
        {isLoading === true ? <p>Loading...</p> : <BeachHeader beachName = {currentBeach?.name ?? "Unknown Beach"} beachRating = {currentBeach?.rating ?? 0}/>}
      </div>
      <BeachStats waveHeight={currentBeach?.waveHeight ?? 0} wavePeriod={currentBeach?.wavePeriod ?? 0} windSpeed={currentBeach?.windSpeed ?? 0} windDirection={currentBeach?.windDirection ?? "Unknown"} tide={currentBeach?.tide ?? 0} waterTemp={currentBeach?.waterTemp ?? 0} weather={currentBeach?.weather ?? "Unknown"}/>
      <WhyRating rating={currentBeach?.rating ?? 0} reasoning={currentBeach?.reasoning ?? "No Reasoning Available"}/>
      <GoodStuff goodStuff={currentBeach?.goodStuff ?? "N/A"}/>
      <BadStuff badStuff={currentBeach?.badStuff ?? "N/A"}/>
    </div>
  );
}