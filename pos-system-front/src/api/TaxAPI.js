import axios from "axios";

export const fetchTax = async (taxId, setTax) => {
    try {
        const response = await axios.get(`http://localhost:8080/taxes/${taxId}`);

        setTax(response.data);
    } catch (err) {
        console.log(err.message);
    }
}