import "../App.css";

interface SearchBarProps { //interface holds list of things (props (properties)) required to build a SearchBar component, in this case, onSearch
  onSearch: (beachName: string) => void; //onSearch is a tube that leads out of a box (the textbox after search button is pressed), the stuff allowed down the tube is strings hence :string, and beachName is just something added to make it clear that a beachName is the expected string input, but has no effect on what is actually passed down the tube, aka for my (the programmer's) eyes only
  }

export function SearchBar(props: SearchBarProps) {
    const handleSearch = (e: React.SubmitEvent<HTMLFormElement>) => {
        e.preventDefault(); //stops page reloading when enter is hit
        const input = e.currentTarget.elements.namedItem("beachName") as HTMLInputElement | null; //grabs the input from the textbox and saves it as a variable named input
        if (input === null || input.value.trim() === "") { //if the input is null or the input is empty, return nothing
            return;
        }
        props.onSearch(input.value);
    };
    return (
        <>
        <form className = "search-bar" onSubmit = {handleSearch}>
            <span className = "search-bar-icon material-symbols-outlined">search</span>
            <input className = "search-bar-input" name = "beachName" type = "text" placeholder = "Enter a beach name..." autoComplete = "off"/>
        </form>
        </>
    );
}