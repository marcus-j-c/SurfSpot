import "../App.css";

export function SideBar() {
    return (
        <>
        <div className = "side-bar">
            <h1>Trending Beaches</h1>
            <nav>
                <ul className = "side-bar-links">
                <li>
                    <a href = "#" className = "trending-item">
                        <span className = "beach-name">1. Bonzai Pipeline</span>
                        <i className = "material-symbols-outlined icon positive">trending_up</i>
                        <span className = "beach-change positive">+28%</span>
                    </a>
                </li>
                <li>
                    <a href = "#" className = "trending-item">
                        <span className = "beach-name">2. Bells Beach</span>
                        <i className = "material-symbols-outlined icon negative">trending_down</i>
                        <span className = "beach-change negative">-15%</span>
                    </a>
                </li>
                <li>
                    <a href = "#" className = "trending-item">
                        <span className = "beach-name">3. Jeffreys Bay</span>
                        <i className = "material-symbols-outlined icon positive">trending_up</i>
                        <span className = "beach-change positive">+43%</span>
                    </a>
                </li>
                <li>
                    <a href = "#" className = "trending-item">
                        <span className = "beach-name">4. Teahupo'o</span>
                        <i className = "material-symbols-outlined icon positive">trending_up</i>
                        <span className = "beach-change positive">+5%</span>
                    </a>
                </li>
                <li>
                    <a href = "#" className = "trending-item">
                        <span className = "beach-name">5. Supertubos</span>
                        <i className = "material-symbols-outlined icon negative">trending_down</i>
                        <span className = "beach-change negative">-2%</span>
                    </a>
                </li>
                </ul>
            </nav>
        </div>
        </>
    );
}