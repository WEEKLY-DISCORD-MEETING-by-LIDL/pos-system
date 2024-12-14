import React from 'react';
import { useNavigate } from "react-router-dom";
import "../styles/HomeStyle.css";

const Home = () => {
    const navigate = useNavigate();

    const returnBack = () => {
        navigate("/")
    }

    const goToCreateOrder = () => {
        navigate("/order")
    }

    return (
        <div className="home-container">
            <h1>Home/Navigation Page</h1>

            <div className="button-container">
                <button onClick={goToCreateOrder}>Orders</button>

                <button onClick={returnBack}>Back to Login</button>
            </div>
        </div>
    )
}

export default Home;