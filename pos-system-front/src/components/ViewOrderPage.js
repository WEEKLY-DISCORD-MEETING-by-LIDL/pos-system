import {useLocation, useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {createPayment} from "../api/PaymentAPI";
import {cancelOrder, fetchOrder, fetchOrderItems, fetchOrderPrice} from "../api/OrderAPI";
import {fetchProductsByOrder, fetchVariantsByOrder} from "../api/ProductAPI";

export const ViewOrderPage = (props) => {

    const token = localStorage.getItem("token");
    const location = useLocation();
    const [order, setOrder] = useState([]);
    const [orderItems, setOrderItems] = useState([]);
    const [variants, setVariants] = useState([]);
    const [products, setProducts] = useState([]);
    const [price, setPrice] = useState([]);

    const { id } = useParams();

    useEffect(() => {
        fetchOrder(id, setOrder, token);
        fetchOrderItems(id, setOrderItems, token);
        fetchVariantsByOrder(id, setVariants, token);
        fetchProductsByOrder(id, setProducts, token);
        fetchOrderPrice(id, setPrice, token);
    }, []);

    const onPay = () => {
        const payment = {
            tipAmount: 0,
            totalAmount: price,
            method: "CASH",
            orderId: order.id
        }

        createPayment(payment, token);
    }

    const onCancel = () => {
        cancelOrder(order.id, token);
    }

    return (
        <div>
            <h2>Order #{order.id}</h2>
            <p>Status: {order.status}</p>
            <ul>
                {orderItems.map((item) => (
                    <li key={item.id}>
                        <p>x{item.quantity} </p>
                    </li>
                ))}
            </ul>
            <p>Total price: {price}</p>

            <button onClick={onPay}>Pay</button>
            <button>Split payment</button>
            {order.status === "OPENED" && (
                <button onClick={onCancel}>Cancel order</button>
            )}

        </div>
    );
}