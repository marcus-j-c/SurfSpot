import {Outlet} from "react-router-dom";
import {NavBar} from "./components/NavBar";
import {AccountButtons} from "./components/AccountButtons";

export default function Layout() {
    return (
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
                        <use href="#gentle-wave" x="48" y="0" fill="rgba(2, 132, 199, 0.7)" />
                        <use href="#gentle-wave" x="48" y="3" fill="rgba(2, 132, 199, 0.5)" />
                        <use href="#gentle-wave" x="48" y="5" fill="rgba(2, 132, 199, 0.3)" />
                        <use href="#gentle-wave" x="48" y="7" fill="#0284c7" />
                    </g>
                </svg>
            </div>
            <div className="title-and-logo"><span className="logo-emoji">🌊</span>SurfSpot</div>
            <AccountButtons/>
            <NavBar/>
            <main className = "main-content">
                <Outlet/>
            </main>
        </div>
    );
}