import React from 'react';
import { useNavigate } from "react-router-dom";
import {homeStyle as HomeStyle} from "../styles/HomeStyle";

const Home = () => {
    const navigate = useNavigate();

    const returnBack = () => {
        navigate("/")
    }

    const goToCreateOrder = () => {
        navigate("/create-order")
    }

    const goToProductManagement = () => {
        navigate("/product-management");
    }

    const goToOrderManagement = () => {
        navigate("/all-orders");
    }

    const goToMerchantManagement = () => {
        navigate("/merchant-management");
    }

    const goToTaxManagement = () => {
        navigate("/tax-management");
    }
    const goToDiscounts = () => {
        navigate("/discount");
    }

    return (
        <div style={HomeStyle.homeContainer}>
            <h1 style={HomeStyle.homeContainer}> Home/Navigation Page</h1>

            <div style={HomeStyle.buttonContainer}>
                <button style={HomeStyle.button} onClick={goToCreateOrder}>Create order</button>
                <button style={HomeStyle.button} onClick={goToOrderManagement}>Order Management</button>
                <button style={HomeStyle.button} onClick={returnBack}>Back to Login</button>
                <button style={HomeStyle.button} onClick={goToProductManagement}>Product Management</button>
                <button style={HomeStyle.button} onClick={goToMerchantManagement}>Merchant Management</button>
                <button style={HomeStyle.button} onClick={goToTaxManagement}>Tax Management</button>
                <button style={HomeStyle.button} onClick={goToDiscounts}>Discount Management</button>
            </div>
        </div>
    )
}

export default Home;