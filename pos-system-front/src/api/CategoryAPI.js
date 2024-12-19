import axios from "axios";

export const fetchCategories = async(setCategories) => {
    try {
        const token = localStorage.getItem("token");
        const requestString = `http://localhost:8080/categories`;
               
        const response = await axios.get(requestString, {
            headers: {
                Authorization: `Bearer ${token}`, 
            },
        });
        console.log(response.data);
        setCategories(response.data);
    } catch (error) {
        console.log(error.message);
    }
}