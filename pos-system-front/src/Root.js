import React from "react";
import { BrowserRouter as Router, Route, Routes} from "react-router-dom";
import App from "./App";
import Home from "./pages/Home"
import {CreateOrderPage} from "./pages/CreateOrderPage";
import {OrderManagementPage} from "./pages/OrderManagementPage";
import {ViewOrderPage} from "./pages/ViewOrderPage";
import ProductManagementPage from "./pages/ProductManagementPage";

function Root() {
    return (
        <Router>
            <Routes>
                <Route exact path="/" element={<App />} />
                <Route path="/create-order" element={<CreateOrderPage />} />
                <Route path="/all-orders" element={<OrderManagementPage />} />
                <Route path="/view-order/:id" element={<ViewOrderPage />} />
                <Route path="/home" element={<Home />} />
                <Route path="/product-management" element={<ProductManagementPage />} />
            </Routes>
        </Router>
    );
}

export default Root;