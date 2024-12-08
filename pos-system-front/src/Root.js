import React from "react";
import { BrowserRouter as Router, Route, Routes} from "react-router-dom";
import App from "./App";
import Home from "./components/Home"

function Root() {
    return (
        <Router>
            <Routes>
                <Route exact path="/" element={<App />} />
                <Route path="/home" element={<Home />} />
            </Routes>
        </Router>
    );
}

export default Root;