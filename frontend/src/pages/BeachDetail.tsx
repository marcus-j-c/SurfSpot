import "../App.css";
import {useParams} from "react-router-dom";

type BeachRouteParams = {
    beachName: string;
};

export default function BeachDetail() {
    const {beachName} = useParams<BeachRouteParams>();

  return (
    <div className="beach-page">
      <h1>{beachName}</h1>
      <p>Details about {beachName} will be displayed here.</p>
    </div>
  );
}