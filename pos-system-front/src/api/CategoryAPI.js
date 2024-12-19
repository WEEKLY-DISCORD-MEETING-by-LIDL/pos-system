import axios from "axios";

export const fetchCategories = async (setCategories, token) => {
    try {
        const response = await axios.get('http://localhost:8080/categories', {
            headers: {
                Authorization: `Bearer ${token}`
            }
        });

        console.log('Fetched categories:', response.data);
        setCategories(response.data);
    } catch (err) {
        console.error("Error fetching categories:", err.message);
        throw err;
    }
};
