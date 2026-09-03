import "../App.css";

export function AccountButtons() {
    const handleClick = () => {
        alert("Coming soon!");
    };

    return (
        <>
        <div className = "account-buttons"> 
            <div className = "get-started-button">
                <button onClick = {handleClick}>Get Started</button>
            </div>
            <div className = "log-in-button">
                <button onClick = {handleClick}>Log In</button>
            </div>
        </div> 
        </>
    );
}