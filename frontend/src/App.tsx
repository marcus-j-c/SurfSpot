import {BrowserRouter, Routes, Route} from "react-router-dom";
import Home from "./pages/Home";
import BeachDetail from "./pages/BeachDetail";
import Layout from "./Layout";
import "./App.css";
import NotFound from "./pages/NotFound";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route element={<Layout />}>
          <Route path = "/" element = {<Home/>}/>
          <Route path = "spot/:beachName" element = {<BeachDetail/>}/> //anything that comes after spot is saved as a parameter named beachName and the element bit means make this parameter available to the beach component.
          <Route path="*" element={<NotFound/>} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App
