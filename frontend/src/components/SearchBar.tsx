export function SearchBar() {
    return (
        <div className="search-bar">
            <input type = "text" style = {{width:"400px"}} placeholder = "Enter a beach name..."/>
            <button>Search</button>
        </div>
    );
}