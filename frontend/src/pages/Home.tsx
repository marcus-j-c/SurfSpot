import {useEffect} from "react";
import "../App.css";
import {SearchBar} from '../components/SearchBar';
import {SideBar} from "../components/SideBar";
import {CentreHeader} from "../components/CentreHeader";
import {useNavigate} from "react-router-dom";

export default function Home() {
  const navigate = useNavigate();
  const handleSearch = (beachName: string) => {
    const cleanedBeachName = beachName.toLowerCase().replace(/\s+/g, "-");
    navigate(`/spot/${cleanedBeachName}`);
  };
  useEffect(() => {
    document.body.style.overflow = "hidden"; // Lock scroll on Home
    return () => {
      document.body.style.overflow = "auto"; // Re-enable scroll when leaving Home
    };
  }, []);
  return (
    <div className="home-grid">
      <CentreHeader/>
      <SearchBar onSearch = {handleSearch}/> {/*the search bar component created here the propert on search is replaced by the handleSearch function from App.tsx*/}
      <SideBar/>
    </div>
  );
}
