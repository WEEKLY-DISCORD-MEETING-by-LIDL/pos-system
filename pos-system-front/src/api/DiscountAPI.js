import axios from "axios";

export const fetchDiscounts = async (setDiscounts) => {
    try {
        const token = localStorage.getItem("token");
        const response = await axios.get('http://localhost:8080/discounts',{
            headers:{
                Authorization: `Bearer ${token}`
            }
        });
        const formattedDiscounts = response.data.map((discount) => ({
            ...discount,
            percentage: discount.percentage * 100, // Convert to percentage
        }));

        console.log("Formatted Discounts:", formattedDiscounts);
        setDiscounts(formattedDiscounts);
    } catch (err) {
        console.error("Error fetching product discounts:", err.message);
        throw err;
    }
};

export const createDiscount = async (discountData) => {
    try {
        const token = localStorage.getItem("token");
        const response = await axios.post('http://localhost:8080/discounts', discountData,{
            headers:{
                Authorization: `Bearer ${token}`
            }
        });
        console.log("Product discount created:", response.data);
        return response.data;
    } catch (err) {
        console.error("Error creating product discount:", err.message);
        throw err;
    }
};

export const fetchDiscount = async (discountId, setDiscount) => {

    const token = localStorage.getItem("token");
    try {
        const response = await axios.get(`http://localhost:8080/discounts/${discountId}`, {
            headers:{
                Authorization: `Bearer ${token}`
            }
        });

        setDiscount(response.data);
    } catch (err) {
        console.log(err.message);
    }
}

export const updateDiscount = async (discountId, discountData) => {
    try {
        const token = localStorage.getItem("token");
        const response = await axios.put(`http://localhost:8080/discounts/${discountId}`, discountData,{
            headers:{
                Authorization: `Bearer ${token}`
            }
        });
        console.log("Product discount updated:", response.data);
        return response.data;
    } catch (err) {
        console.error("Error updating product discount:", err.message);
        throw err;
    }
};

export const deleteDiscount = async (discountId) => {
    try {
        const token = localStorage.getItem("token");
        await axios.delete(`http://localhost:8080/discounts/${discountId}`,{
            headers:{
                Authorization: `Bearer ${token}`
            }
        });
        console.log(`Product discount with ID ${discountId} deleted`);
    } catch (err) {
        console.error("Error deleting product discount:", err.message);
        throw err;
    }
};

export const fetchOrderDiscounts = async (setOrderDiscounts) => {
    try {
        const token = localStorage.getItem("token");
        const response = await axios.get('http://localhost:8080/orderDiscounts',{
            headers:{
                Authorization: `Bearer ${token}`
            }
        });
        const formattedDiscounts = response.data.map((discount) => ({
            ...discount,
            percentage: discount.percentage * 100, // Convert to percentage
        }));

        console.log("Formatted Discounts:", formattedDiscounts);
        setOrderDiscounts(formattedDiscounts);
    } catch (err) {
        console.error("Error fetching order discounts:", err.message);
        throw err;
    }
};

export const createOrderDiscount = async (orderDiscountData) => {
    try {
        const token = localStorage.getItem("token");
        const response = await axios.post('http://localhost:8080/orderDiscounts', orderDiscountData,{
            headers:{
                Authorization: `Bearer ${token}`
            }
        });
        console.log("Order discount created:", response.data);
        return response.data;
    } catch (err) {
        console.error("Error creating order discount:", err.message);
        throw err;
    }
};

export const updateOrderDicount = async (orderDiscountId, orderDiscountData) => {
    try {
        const token = localStorage.getItem("token");
        const response = await axios.put(`http://localhost:8080/orderDiscounts/${orderDiscountId}`, orderDiscountData,{
            headers:{
                Authorization: `Bearer ${token}`
            }
        });
        console.log("Order discount updated:", response.data);
        return response.data;
    } catch (err) {
        console.error("Error updating order discount:", err.message);
        throw err;
    }
};

export const deleteOrderDiscount = async (orderDiscountId) => {
    try {
        const token = localStorage.getItem("token");
        await axios.delete(`http://localhost:8080/orderDiscounts/${orderDiscountId}`,{
            headers:{
                Authorization: `Bearer ${token}`
            }
        });
        console.log(`Order discount with ID ${orderDiscountId} deleted`);
    } catch (err) {
        console.error("Error deleting order discount:", err.message);
        throw err;
    }
};
