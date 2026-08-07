import {useState} from 'react';

export function SearchBar() {
    const [typedText, setTypedText] = useState(""); //this  variable holds what the user types
    const handleSearch = () => {alert("You searched for: " + typedText);}; //triggers when user hits button

    return (
        <div className="search-bar">
            <input type = "text" style = {{width:"400px"}} placeholder = "Enter a beach name..." onChange = {(e) => setTypedText(e.target.value) /*updates typedText instantly*/}/>
            <button onClick = {handleSearch}>Search</button>
            {/*show live updates to typedText */}
            <p>Current state of typedText: {typedText}</p>
        </div>
    );
}