import axios from "axios";

export const createPayment = async (payment) => {
    try {
        let response = await axios.post(`http://localhost:8080/pay`, payment, {
            headers: {
                'Content-Type': 'application/json',
            },
        });

        console.log('Payment created successfully.');
    } catch (error) {
        console.error('Error creating payment:', error.message);
    }
}