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
  return (
    <div className="home-grid">
      <CentreHeader/>
      <SearchBar onSearch = {handleSearch}/> {/*the search bar component created here the propert on search is replaced by the handleSearch function from App.tsx*/}
      <SideBar/>
    </div>
  );
}
