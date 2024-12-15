import {useLocation} from "react-router-dom";
import {useState} from "react";
import {createPayment} from "../api/PaymentAPI";

export const ViewOrderPage = (props) => {

    const location = useLocation();
    const { order, orderItems, price } = location.state || {};
    const {leftAmount, setLeftAmount} = useState(price)

    const onPay = () => {
        const payment = {
            tipAmount: 0,
            totalAmount: price,
            method: "CASH",
            orderId: order.id
        }

        createPayment(payment);
    }

    return (
        <div>
            <h2>Order #{order.id}</h2>
            <p>Status: {order.status}</p>
            <ul>
                {orderItems.map((item) => (
                    <li key={item.id}>
                        <p>Product variant #{item.productVariantId} x{item.quantity}</p>
                    </li>
                ))}
            </ul>
            <p>Total price: {leftAmount}</p>

            <button onClick={onPay}>Pay</button>
            <button>Split payment</button>
        </div>
    );
}