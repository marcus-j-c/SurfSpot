import "../App.css";
import {useState} from 'react';

interface SearchBarProps { //interface holds list of things (props (properties)) required to build a SearchBar component, in this case, onSearch
  onSearch: (beachName: string) => void; //onSearch is a tube that leads out of a box (the textbox after search button is pressed), the stuff allowed down the tube is strings hence :string, and beachName is just something added to make it clear that a beachName is the expected string input, but has no effect on what is actually passed down the tube, aka for my (the programmer's) eyes only
  onType: (typedText: string) => void; //onType is a tube that leads out of a box (the textbox as the user types), the stuff allowed down the tube is strings hence :string, and typedText is just something added to make it clear that a typedText is the expected string input, but has no effect on what is actually passed down the tube, aka for my (the programmer's) eyes only
}

export function SearchBar(props: SearchBarProps) {
    const [typedText, setTypedText] = useState(""); //this  variable holds what the user types
    const handleSearch = () => {props.onSearch(typedText);}; //creates a func that grabs the onSearch tube and shoves down it the searchTerm, the user typed. This func is then saved in the variable handleSearch. This code is triggered when the user hits the search button
    //the onSearch above has actually been replaced by the handleSearch function from App.tsx, therefore when doing props.onSearch(typedText) it is actually passing typedText into the handleSearch function from App.tsx, which then sets the selectedBeach state!!
    const handleType = (e: React.ChangeEvent<HTMLInputElement>) => { //looks for a change event in the input box
        const value = e.target.value; //saves said new value of input box to a variable
        setTypedText(value); //sets state of typed text to said value
        props.onType(value); //passes this to the onType tube, which has been replaced by the handleType function from App.tsx, which then sets the typedText state!!
    };
    return (
        <>
        <div className="search-bar">
            <input type = "text" placeholder = "Enter a beach name..." onChange = {handleType}/>
            <button onClick = {handleSearch}>Search</button>
        </div>
        </>
    );
}