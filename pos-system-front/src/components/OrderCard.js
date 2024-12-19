import {useEffect, useState} from "react";
import {fetchOrderItems, fetchOrderPrice} from "../api/OrderAPI";
import '../styles/OrderCardStyle.css';
import {useNavigate} from "react-router-dom";

export const OrderCard = (props) => {

    const token = localStorage.getItem("token");
    const navigate = useNavigate();
    const [orderItems, setOrderItems] = useState([]);
    const [price, setPrice] = useState(0);

    useEffect(() => {
        fetchOrderItems(props.order.id, setOrderItems, token)
        fetchOrderPrice(props.order.id, setPrice, token);
    }, []);

    const openOrder = () => {
        navigate(`/view-order/${props.order.id}`);
    }

    return (
        <div className="order-card" onClick={openOrder}>
            <h4>Order: #{props.order.id}</h4>
            <p>{props.order.status}</p>
            <p>Items: {orderItems.length}</p>
            <p>Price: {price}</p>
        </div>
    );
}