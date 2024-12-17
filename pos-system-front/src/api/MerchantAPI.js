import axios from "axios";

export const fetchMerchant = async(merchantId, setMerchant) => {
    try {
        const token = localStorage.getItem("token");
        const response = await axios.get(`http://localhost:8080/merchants/${merchantId}`, {
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

