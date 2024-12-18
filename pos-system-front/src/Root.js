import React from "react";
import { BrowserRouter as Router, Route, Routes} from "react-router-dom";
import App from "./App";
import Home from "./components/Home"
import Discount from "./components/Discount"
import {CreateOrderPage} from "./components/CreateOrderPage";
import {OrderManagementPage} from "./components/OrderManagementPage";
import {ViewOrderPage} from "./components/ViewOrderPage";
import ProductManagementPage from "./pages/ProductManagementPage";
import {MerchantManagementPage} from "./components/MerchantManagementPage";
import {TaxManagementPage} from "./components/TaxManagementPage";

function Root() {
    return (
        <Router>
            <Routes>
                <Route exact path="/" element={<App />} />
                <Route path="/create-order" element={<CreateOrderPage />} />
                <Route path="/all-orders" element={<OrderManagementPage />} />
                <Route path="/view-order" element={<ViewOrderPage />} />
                <Route path="/home" element={<Home />} />
                <Route path="/product-management" element={<ProductManagementPage />} />
                <Route path="/merchant-management" element={<MerchantManagementPage />} />
                <Route path="/tax-management" element={<TaxManagementPage />} />
                <Route path="/discount" element={<Discount />} />
            </Routes>
        </Router>
    );
}

export default Root;