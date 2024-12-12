import axios from 'axios';
import React, { useEffect, useState } from 'react';
import { useNavigate } from "react-router-dom";

const Home = () => {
    const [text, setText] = useState([]);
    const [axiosText, setAxiosText] = useState([])
    const navigate = useNavigate();

    // useEffect(() => {
    //     fetch('http://localhost:8080/test', {
    //         method: 'GET',
    //         credentials: 'include',
    //     })
    //         .then(response => response.text())
    //         .then(data => setText(data))
    //         .catch(error => console.error("Error doing something", error));
    //
    //     axios.get(`http://localhost:8080/test2`)
    //         .then(res => {
    //           const axiosText = res.data;
    //           setAxiosText(axiosText);
    //         })
    // }, []);

    const returnBack = () => {
        navigate("/")
      }

    return (
        <div>
            <h1>Users</h1>
            <div>fetch: {text}</div>
            <div>axios: {axiosText}</div>
            <button onClick={returnBack}>Return</button>
        </div>
    )
}

export default Home;