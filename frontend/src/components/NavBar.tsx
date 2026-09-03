import "../App.css";

export function NavBar() {
    const handleClick = (e: React.MouseEvent<HTMLAnchorElement>) => { //temporary coming soon, sattelite map not in v1
        e.preventDefault();
        alert("Coming soon!");
    };
    return (
        <>
        <div className = "nav-bar">
            <nav>
                <ul className = "nav-bar-links">
                <li><a href = "/">Home</a></li>
                <li><a href = "/satellite-map" onClick = {handleClick}>Satellite Map</a></li>
                <li><a href = "/how-it-works">How It Works</a></li>
                <li><a href = "/about">About</a></li>
                </ul>
            </nav>
        </div> 
        </>
    );
}