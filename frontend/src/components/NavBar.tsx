import "../App.css";

export function NavBar() {
    return (
        <>
        <div className = "nav-bar">
            <nav>
                <ul className = "nav-bar-links">
                <li><a href = "/">Home</a></li>
                <li><a href = "/satellite-map">Satellite Map</a></li>
                <li><a href = "/how-it-works">How It Works</a></li>
                <li><a href = "/about">About</a></li>
                </ul>
            </nav>
        </div> 
        </>
    );
}