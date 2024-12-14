import React from "react";
import { BrowserRouter as Router, Route, Routes} from "react-router-dom";
import App from "./App";
import Home from "./components/Home"
import {CreateOrderPage} from "./components/CreateOrderPage";

function Root() {
    return (
        <Router>
            <Routes>
                <Route exact path="/" element={<App />} />
                <Route path="/home" element={<Home />} />
                <Route path="/order" element={<CreateOrderPage />} />
            </Routes>
        </Router>
    );
}

export default Root;