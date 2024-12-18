import axios from "axios";

export const createOrder = async (orderDiscountId, orderItems, token) => {
    try {
        let response;

        console.log(orderItems)

        if(orderDiscountId == null) {
            response = await axios.post(`http://localhost:8080/orders`, orderItems, {
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${token}`
                },
            });
        }
        else {
            console.log("WITH DISCOUNT");
            response = await axios.post(`http://localhost:8080/orders?orderDiscountId=${orderDiscountId}`, orderItems, {
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${token}`
                },
            });
        }

        console.log('Order created successfully.', response.data);
    } catch (error) {
        console.error('Error creating order:', error.message);
    }
};

export const fetchOrders = async (status, createdAtMin, createdAtMax, limit, setOrders, token) => {
    try {
        const params = new URLSearchParams();

        if (status != null) params.append('status', status);
        if (createdAtMin != null) params.append('createdAtMin', createdAtMin);
        if (createdAtMax != null) params.append('createdAtMax', createdAtMax);
        if (limit != null) params.append('limit', limit);

        const requestString = `http://localhost:8080/orders?${params.toString()}`;
        const response = await axios.get(requestString, {
            headers:{
                Authorization: `Bearer ${token}`
            }
        });
        console.log(response.data);
        setOrders(response.data);
    } catch (err) {
        console.error(err.message);
    }
}

export const fetchOrder = async (orderId, setOrder, token) => {
    try {
        const response = await axios.get(`http://localhost:8080/orders/${orderId}`, {
            headers:{
                Authorization: `Bearer ${token}`
            }
        });

        setOrder(response.data);
    } catch (err) {
        console.log(err.message);
    }
}

export const archiveOrder = async (orderId, token) => {
    try {
        const response = await axios.post(`http://localhost:8080/orders/${orderId}/archive`, null, {
            headers:{
                Authorization: `Bearer ${token}`
            }
        });
    } catch (err) {
        console.log(err.message);
    }
}

export const fetchOrderItems = async (orderId, setOrderItems, token) => {
    try {
        const response = await axios.get(`http://localhost:8080/orders/${orderId}/items`, {
            headers:{
                Authorization: `Bearer ${token}`
            }
        });
        console.log(response.data);
        setOrderItems(response.data);
    } catch (err) {
        console.log(err.message);
    }
}

export const fetchOrderPrice = async (orderId, setPrice, token) => {
    try {
        const response = await axios.get(`http://localhost:8080/orders/${orderId}/price`, {
            headers:{
                Authorization: `Bearer ${token}`
            }
        });
        console.log(response.data);
        setPrice(response.data);
    } catch (err) {
        console.log(err.message);
    }
}

export const fetchOrderUnpaidPrice = async (orderId, setUnpaidPrice, token) => {
    try {
        const response = await axios.get(`http://localhost:8080/orders/${orderId}/unpaid-price`, {
            headers:{
                Authorization: `Bearer ${token}`
            }
        });
        console.log(response.data);
        setUnpaidPrice(response.data);
    } catch (err) {
        console.log(err.message);
    }
}

export const cancelOrder = async (orderId, token) => {
    try {
        const response = await axios.patch(`http://localhost:8080/orders/${orderId}/cancel`, null,{
            headers:{
                Authorization: `Bearer ${token}`
            }
        });
        console.log(response.data);
    } catch (err) {
        console.log(err.message);
    }
}

export const updateOrderStatus = async (orderId, status, token) => {
    try {
        const response = await axios.put(`http://localhost:8080/orders/${orderId}?status=${status}`, null,{
            headers:{
                Authorization: `Bearer ${token}`
            }
        });
        console.log(response.data);
    } catch (err) {
        console.log(err.message);
    }
}

export const validatePaymentsAndUpdateOrderStatus = async (orderId, token) => {
    try {
        const response = await axios.patch(`http://localhost:8080/orders/${orderId}/validate-payments`, null, {
            headers:{
                Authorization: `Bearer ${token}`
            }
        });
        console.log(response.data);
    } catch (err) {
        console.log(err.message);
    }
}

export const fetchOrderSummary = async (orderId, setOrder, token) => {
    try {
        const response = await axios.get(`http://localhost:8080/orders/${orderId}/summary`, {
            headers:{
                Authorization: `Bearer ${token}`
            }
        });

        setOrder(response.data);
    } catch (err) {
        console.log(err.message);
    }
}