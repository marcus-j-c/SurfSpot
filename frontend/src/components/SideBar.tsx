import "../App.css";

export function SideBar() {
    return (
        <>
        <div className = "side-bar">
            <h1>Trending Beaches</h1>
            <nav>
                <ul className = "side-bar-links">
                <li>
                    <a href = "/spot/banzai-pipeline" className = "trending-item">
                        <div className = "beach-info">
                            <span className = "beach-name">1. Banzai Pipeline</span>
                            <span className = "beach-location">Hawaii, USA</span>
                        </div>
                        <i className = "material-symbols-outlined icon positive">trending_up</i>
                        <span className = "beach-change positive">+28%</span>
                    </a>
                </li>
                <li>
                    <a href = "/spot/bells-beach" className = "trending-item">
                        <div className = "beach-info">
                            <span className = "beach-name">2. Bells Beach</span>
                            <span className = "beach-location">Victoria, Australia</span>
                        </div>
                        <i className = "material-symbols-outlined icon negative">trending_down</i>
                        <span className = "beach-change negative">-15%</span>
                    </a>
                </li>
                <li>
                    <a href = "/spot/jeffreys-bay" className = "trending-item">
                        <div className = "beach-info">
                            <span className = "beach-name">3. Jeffreys Bay</span>
                            <span className = "beach-location">Eastern Cape, South Africa</span>
                        </div>
                        <i className = "material-symbols-outlined icon positive">trending_up</i>
                        <span className = "beach-change positive">+43%</span>
                    </a>
                </li>
                <li>
                    <a href = "/spot/teahupo'o" className = "trending-item">
                        <div className = "beach-info">
                            <span className = "beach-name">4. Teahupo'o</span>
                            <span className = "beach-location">Tahiti, French Polynesia</span>
                        </div>
                        <i className = "material-symbols-outlined icon positive">trending_up</i>
                        <span className = "beach-change positive">+5%</span>
                    </a>
                </li>
                <li>
                    <a href = "/spot/supertubos" className = "trending-item">
                        <div className = "beach-info">
                            <span className = "beach-name">5. Supertubos</span>
                            <span className = "beach-location">Peniche, Portugal</span>
                        </div>
                        <i className = "material-symbols-outlined icon negative">trending_down</i>
                        <span className = "beach-change negative">-2%</span>
                    </a>
                </li>
                <li>
                    <a href = "/spot/nazaré" className = "trending-item">
                        <div className="beach-info">
                            <span className="beach-name">6. Nazaré</span>
                            <span className="beach-location">Leiria, Portugal</span>
                        </div>
                        <i className="material-symbols-outlined icon positive">trending_up</i>
                        <span className="beach-change positive">+51%</span>
                    </a>
                </li>
                <li>
                    <a href = "/spot/uluwatu" className = "trending-item">
                        <div className="beach-info">
                            <span className="beach-name">7. Uluwatu</span>
                            <span className="beach-location">Bali, Indonesia</span>
                        </div>
                        <i className="material-symbols-outlined icon positive">trending_up</i>
                        <span className="beach-change positive">+19%</span>
                    </a>
                </li>
                <li>
                    <a href = "/spot/gold-coast" className = "trending-item">
                        <div className="beach-info">
                            <span className="beach-name">8. Gold Coast</span>
                            <span className="beach-location">Queensland, Australia</span>
                        </div>
                        <i className="material-symbols-outlined icon negative">trending_down</i>
                        <span className="beach-change negative">-8%</span>
                    </a>
                </li>
                <li>
                    <a href = "/spot/mavericks" className = "trending-item">
                        <div className="beach-info">
                            <span className="beach-name">9. Mavericks</span>
                            <span className="beach-location">California, USA</span>
                        </div>
                        <i className="material-symbols-outlined icon positive">trending_up</i>
                        <span className="beach-change positive">+34%</span>
                    </a>
                </li>
                <li>
                    <a href = "/spot/hossegor" className = "trending-item">
                        <div className="beach-info">
                            <span className="beach-name">10. Hossegor</span>
                            <span className="beach-location">Landes, France</span>
                        </div>
                        <i className="material-symbols-outlined icon negative">trending_down</i>
                        <span className="beach-change negative">-4%</span>
                    </a>
                </li>
                </ul>
            </nav>
        </div>
        </>
    );
}