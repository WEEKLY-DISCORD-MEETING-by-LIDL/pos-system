import {useEffect, useState} from "react";
import {OrderManagementStyle} from "../styles/OrderManagementStyle";
import {OrderCard} from "./OrderCard";
import {fetchOrders} from "../api/OrderAPI";

export const OrderManagementPage = () => {

    const token = localStorage.getItem("token");
    const [orders, setOrders] = useState([]);

    useEffect(() => {
        fetchOrders(null, null, null, null, setOrders, token);
    }, []);

    return (
        <div>
            <ul style={OrderManagementStyle.list}>
                {orders.map((order) => ( order.status !== "PAID" &&
                    <li key={order.id} style={OrderManagementStyle.listItem}>
                        <OrderCard order={order}/>
                    </li>
                ))}
            </ul>
        </div>
    );
}