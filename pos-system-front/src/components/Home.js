import React from 'react';
import { useNavigate } from "react-router-dom";
import {homeStyle as HomeStyle} from "../styles/HomeStyle";

const Home = () => {
    const navigate = useNavigate();

    const returnBack = () => {
        navigate("/")
    }

    const goToCreateOrder = () => {
        navigate("/order")
    }

    return (
        <div style={HomeStyle.homeContainer}>
            <h1 style={HomeStyle.homeContainer}> Home/Navigation Page</h1>

            <div style={HomeStyle.buttonContainer}>
                <button style = {HomeStyle.button} onClick={goToCreateOrder}>Orders</button>

                <button style = {HomeStyle.button} onClick={returnBack}>Back to Login</button>
            </div>
        </div>
    )
}

export default Home;