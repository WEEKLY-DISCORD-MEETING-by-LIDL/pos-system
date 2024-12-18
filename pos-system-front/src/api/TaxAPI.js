import axios from "axios";

export const fetchTax = async (taxId, setTax, token) => {
    try {
        const response = await axios.get(`http://localhost:8080/taxes/${taxId}`, {
            headers:{
                Authorization: `Bearer ${token}`
            }
        });

        setTax(response.data);
    } catch (err) {
        console.log(err.message);
    }
}