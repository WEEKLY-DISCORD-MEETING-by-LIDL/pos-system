import axios from "axios";

export const fetchMerchant = async(setMerchant) => {
    try {
        const token = localStorage.getItem("token");
        const response = await axios.get(`http://localhost:8080/merchants`, {
            headers: {
                Authorization: `Bearer ${token}`, 
            },
        });
        //console.log(response.data);
        setMerchant(response.data);
    } catch (error) {
        console.log(error.message);
    }

}

export const updateMerchant = async(editableMerchant) => {
    try {
        const token = localStorage.getItem("token");
        const response = await axios.put(`http://localhost:8080/merchants/${editableMerchant.id}`, editableMerchant, {
            headers: {
                Authorization: `Bearer ${token}`, 
            },
        });
        console.log(response.data);
    } catch (error) {
        console.log(error.message);
    }
}


