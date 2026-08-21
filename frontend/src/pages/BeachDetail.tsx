import "../App.css";
import {useParams} from "react-router-dom";
import { BeachHeader } from "../components/BeachHeader";

const beachData: [string, number][] = [
  ["Unknown beach", 0],
  ["Bonzai Pipeline", 8.6],
  ["Bells Beach", 6.1],
  ["Jeffreys Bay", 9.4],
  ["Teahupo'o", 7.8],
  ["Supertubos", 3.5],
  ["Nazaré", 9.1],
  ["Uluwatu", 5.3],
  ["Gold Coast", 8.0],
  ["Mavericks", 2.7],
  ["Hossegor", 6.9]
];

const getBeachName = (nameToFind: string): string => {
  const nameToFindCleaned = nameToFind.toLowerCase().replace(/-/g, " ");
  for (let i = 0; i <beachData.length; i++) {
    if (beachData[i][0].toLowerCase() === nameToFindCleaned) {
      return beachData[i][0];
    }
  }
  return "Unknown beach";
};

const getBeachRating = (nameToFind: string): number => {
  const nameToFindCleaned = nameToFind.toLowerCase().replace(/-/g, " ");
  for (let i = 0; i <beachData.length; i++) {
    if (beachData[i][0].toLowerCase() === nameToFindCleaned) {
      return beachData[i][1];
    }
  }
  return 0;
};

type BeachRouteParams = {
    beachName: string;
};

export default function BeachDetail() {
    const {beachName} = useParams<BeachRouteParams>();
    const currentBeach = beachName ? beachName : "Unknown beach";

  return (
    <div className = "beach-page-grid">
      <div className = "beach-header">
        <BeachHeader beachName = {getBeachName(currentBeach)} beachRating = {getBeachRating(currentBeach)}/>
      </div>
    </div>
  );
}