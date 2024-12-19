import React from "react";
import { BrowserRouter as Router, Route, Routes} from "react-router-dom";
import App from "./components/App";
import Home from "./components/Home"
import {CreateOrderPage} from "./components/CreateOrderPage";
import {OrderManagementPage} from "./components/OrderManagementPage";
import {ViewOrderPage} from "./components/ViewOrderPage";
import ProductManagementPage from "./pages/ProductManagementPage";
import Layout from "./components/Layout";
import { ServicePage } from "./components/ServicePage";
import { CreateReservationPage } from "./components/CreateReservationPage";
import { ReservationsPage } from "./components/ReservationsPage"
import { ReservationPaymentPage } from "./components/ReservationPaymentPage";

function Root() {
    return (
        <Router>
            {/* <Layout> */} {/* adds navbar*/}
                <Routes>
                    <Route exact path="/" element={<App />} />
                    <Route path="/create-order" element={<CreateOrderPage />} />
                    <Route path="/all-orders" element={<OrderManagementPage />} />
                    <Route path="/view-order" element={<ViewOrderPage />} />
                    <Route path="/home" element={<Home />} />
                    <Route path="/product-management" element={<ProductManagementPage />} />
                    <Route path="/services" element={<ServicePage />} />
                    <Route path="/createReservation" element={<CreateReservationPage />} />
                    <Route path="/reservations" element={<ReservationsPage />} />
                    <Route path="/pay-reservation" element={<ReservationPaymentPage />} />
                </Routes>
            {/* </Layout> */}
        </Router>
    );
}

export default Root;