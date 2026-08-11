import "../App.css";
import { useState, useEffect } from "react";

export function CentreHeader() {
  const dynamicText = ["surfing", "paddling out", "waxing up", "catching waves", "calling in sick"];
  const [i, setI] = useState(0);
  const [displayText, setDisplayText] = useState("");
  const [isDeleting, setIsDeleting] = useState(false);
  const isPaused = displayText === dynamicText[i]; //determine if text should be paused or not.
  const textClass = isPaused === true ? "dynamic-text blinking" : "dynamic-text";

  useEffect(() => { //use effect only runs the code inside it the first time the component is drawn.
    const currentFullWord = dynamicText[i];
    let typingSpeed;
    if (isDeleting === true) { //makes deleting faster than typing.
        typingSpeed = 75;
    } 
    else {
        typingSpeed = 100;
    }
    if (isDeleting === false && displayText === currentFullWord) { //pause if word is fully writen.
      typingSpeed = 5000;
    }
    if (isDeleting === true && displayText === "") { //pause if word is fully deleted.
      typingSpeed = 400;
    }
    const timer = setTimeout(() => {
      if (isDeleting === false) {
        if (displayText !== currentFullWord) { //if word isnt fully typed, add one new char to displayText.
          setDisplayText(currentFullWord.substring(0, displayText.length + 1));
        } 
        else { //if word is typed, start deleting it.
          setIsDeleting(true);
        }
      } 
      else {
        if (displayText !== "") { //the inverse of the writing logic.
          setDisplayText(currentFullWord.substring(0, displayText.length - 1));
        } 
        else {
          setIsDeleting(false);
          setI((prevI) => (prevI + 1) % dynamicText.length);
        }
      }
    }, typingSpeed);
    return () => clearTimeout(timer); //deletes timer if component no longer exists.
  }, [displayText, isDeleting, i, dynamicText]); //rerun code if any of these change.
  return (
  <div className="centre-header">
    Is it worth <span className={textClass}>{displayText}</span> today?
  </div>
);
}