import "../App.css";

export function SideBar() {
    return (
        <>
        <div className = "side-bar">
            <h1>Trending Beaches</h1>
            <nav>
                <ul className = "side-bar-links">
                <li><a href = "#">Beach1</a></li>
                <li><a href = "#">Beach2</a></li>
                <li><a href = "#">Beach3</a></li>
                <li><a href = "#">Beach4</a></li>
                <li><a href = "#">Beach5</a></li>
                </ul>
            </nav>
        </div>
        </>
    );
}