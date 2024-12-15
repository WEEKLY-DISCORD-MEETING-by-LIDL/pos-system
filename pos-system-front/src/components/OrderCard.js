import {useEffect, useState} from "react";
import {fetchOrderItems, fetchOrderPrice} from "../api/OrderAPI";
import '../styles/OrderCardStyle.css';
import {useNavigate} from "react-router-dom";

export const OrderCard = (props) => {

    const navigate = useNavigate();
    const [orderItems, setOrderItems] = useState([]);
    const [price, setPrice] = useState(0);

    useEffect(() => {
        fetchOrderItems(props.order.id, setOrderItems)
        fetchOrderPrice(props.order.id, setPrice);
    }, []);

    const openOrder = () => {
        navigate("/view-order", {
            state: {order: props.order, orderItems: orderItems, price: price}
        });
    }

    return (
        <div className="order-card" onClick={openOrder}>
            <h4>Order: #{props.order.id}</h4>
            <p>Items: {orderItems.length}</p>
            <p>Price: {price}</p>
        </div>
    );
}