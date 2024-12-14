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