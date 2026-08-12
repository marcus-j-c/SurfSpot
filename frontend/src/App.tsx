import {BrowserRouter, Routes, Route} from "react-router-dom";
import Home from "./pages/Home";
import BeachDetail from "./pages/BeachDetail";
import Layout from "./Layout";
import "./App.css";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route element={<Layout />}>
          <Route path = "/" element = {<Home/>}/>
          <Route path = "spot/:beachName" element = {<BeachDetail/>}/>
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App
