import "../App.css";
import {useParams} from "react-router-dom";
import { BeachHeader } from "../components/BeachHeader";

type BeachRouteParams = {
    beachName: string;
    beachRating: string;
};

export default function BeachDetail() {
    const {beachName, beachRating} = useParams<BeachRouteParams>();

  return (
    <div className = "beach-page-grid">
      <div className = "beach-header">
        <BeachHeader beachName = {beachName ?? "Unknown Beach"} beachRating = {beachRating ? Number(beachRating) : 0}/>
      </div>
    </div>
  );
}