import axios from "axios";

export const createOrder = async (orderDiscountId, orderItems) => {
    try {
        let response;

        console.log(orderItems)

        if(orderDiscountId == null) {
            response = await axios.post(`http://localhost:8080/orders`, orderItems, {
                headers: {
                    'Content-Type': 'application/json',
                },
            });
        }
        else {
            console.log("WITH DISCOUNT");
            response = await axios.post(`http://localhost:8080/orders?orderDiscountId=${orderDiscountId}`, orderItems, {
                headers: {
                    'Content-Type': 'application/json',
                },
            });
        }

        console.log('Order created successfully.', response.data);
    } catch (error) {
        console.error('Error creating order:', error.message);
    }
};

export const fetchOrders = async (status, createdAtMin, createdAtMax, limit, setOrders) => {
    try {
        const params = new URLSearchParams();

        if (status != null) params.append('status', status);
        if (createdAtMin != null) params.append('createdAtMin', createdAtMin);
        if (createdAtMax != null) params.append('createdAtMax', createdAtMax);
        if (limit != null) params.append('limit', limit);

        const requestString = `http://localhost:8080/orders?${params.toString()}`;
        const response = await axios.get(requestString);
        console.log(response.data);
        setOrders(response.data);
    } catch (err) {
        console.error(err.message);
    }
}

export const fetchOrderItems = async (orderId, setOrderItems) => {
    try {
        const response = await axios.get(`http://localhost:8080/orders/${orderId}/items`);
        console.log(response.data);
        setOrderItems(response.data);
    } catch (err) {
        console.log(err.message);
    }
}

export const fetchOrderPrice = async (orderId, setPrice) => {
    try {
        const response = await axios.get(`http://localhost:8080/orders/${orderId}/price`);
        console.log(response.data);
        setPrice(response.data);
    } catch (err) {
        console.log(err.message);
    }
}