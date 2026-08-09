import "../App.css";

export function NavBar() {
    return (
        <>
        <div className = "nav-bar">
            <nav>
                <ul className = "nav-bar-links">
                <li><a href = "#">Home</a></li>
                <li><a href = "#">Satellite Map</a></li>
                <li><a href = "#">How It Works</a></li>
                <li><a href = "#">About</a></li>
                </ul>
            </nav>
        </div> 
        </>
    );
}