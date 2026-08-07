import {useState} from 'react';

interface SearchBarProps { //interface holds list of things (props (properties)) required to build a SearchBar component, in this case, onSearch
  onSearch: (beachName: string) => void; //onSearch is a tube that leads out of a box (the textbox after search button is pressed), the stuff allowed down the stube is strings hence :string, and beachName is just something added to make it clear that a beachName is the expected string input, but has no effect on what is actually passed down the tube, aka for my (the programmer's) eyes only
}

export function SearchBar(props: SearchBarProps) {
    const [typedText, setTypedText] = useState(""); //this  variable holds what the user types
    const handleSearch = () => {props.onSearch(typedText);}; //creates a func that grabs the onSearch tube and shoves down it the searchTerm, the user typed. This func is then saved in the variable handleSearch. This code is triggered when the user hits the search button
    //the onSearch above has actually been replaced by the handleSearch function from App.tsx, therefore when doing props.onSearch(typedText) it is actually passing typedText into the handleSearch function from App.tsx, which then sets the selectedBeach state!!

    return (
        <div className="search-bar">
            <input type = "text" style = {{width:"400px"}} placeholder = "Enter a beach name..." onChange = {(e) => setTypedText(e.target.value) /*updates typedText instantly*/}/>
            <button onClick = {handleSearch}>Search</button>
            {/*show live updates to typedText */}
            <p>Current state of typedText: {typedText}</p>
        </div>
    );
}