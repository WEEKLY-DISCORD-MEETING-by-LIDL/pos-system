import React from "react";
import { BrowserRouter as Router, Route, Routes} from "react-router-dom";
import App from "./App";
import Home from "./components/Home"
import {CreateOrderPage} from "./components/CreateOrderPage";
import {OrderManagementPage} from "./components/OrderManagementPage";
import {ViewOrderPage} from "./components/ViewOrderPage";

function Root() {
    return (
        <Router>
            <Routes>
                <Route exact path="/" element={<App />} />
                <Route path="/create-order" element={<CreateOrderPage />} />
                <Route path="/all-orders" element={<OrderManagementPage />} />
                <Route path="/view-order" element={<ViewOrderPage />} />
            </Routes>
        </Router>
    );
}

export default Root;