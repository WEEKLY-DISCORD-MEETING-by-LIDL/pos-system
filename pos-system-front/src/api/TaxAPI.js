import axios from "axios";

export const fetchTax = async (taxId, setTax) => {
    try {
        const response = await axios.get(`http://localhost:8080/taxes/${taxId}`);

        setTax(response.data);
    } catch (err) {
        console.log(err.message);
    }
}

export const fetchTaxes = async(limit, setTaxes) => {
    try {
        const token = localStorage.getItem("token");
        const params = new URLSearchParams();
        if(limit != null) params.append('limit', limit);
        const requestString = `http://localhost:8080/taxes?${params.toString()}`;
               
        const response = await axios.get(requestString, {
            headers: {
                Authorization: `Bearer ${token}`, 
            },
        });
        console.log(response.data);
        setTaxes(response.data);
    } catch (error) {
        console.log(error.message);
    }
}

export const deleteTax = async (taxId) => {
    try {
        const token = localStorage.getItem("token");
        const response = await axios.delete(`http://localhost:8080/taxes/${taxId}`, {
            headers: {
                Authorization: `Bearer ${token}`, 
            },
        });
    } catch (error) {
        console.error(error.message);
        throw error;
    }
};

export const updateTax = async (taxId, taxData) => {
    try {
        const token = localStorage.getItem("token");
        const response = await axios.put(`http://localhost:8080/taxes/${taxId}`, taxData, {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });
        return response.data;
    } catch (error) {
        console.error(error.message);
        throw error;
    }
};

export const createTax = async (taxData) => {
    try {
        const token = localStorage.getItem("token");
        const response = await axios.post(`http://localhost:8080/taxes`, taxData, {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });
        return response.data;
    } catch (error) {
        console.error(error.message);
        throw error;
    }
};