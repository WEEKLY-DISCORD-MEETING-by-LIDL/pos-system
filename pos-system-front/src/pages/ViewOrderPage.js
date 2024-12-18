import {useLocation, useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {createPayment} from "../api/PaymentAPI";
import '../styles/ViewOrderPageStyle.css';
import {
    archiveOrder,
    cancelOrder,
    fetchOrder,
    fetchOrderItems,
    fetchOrderPrice,
    fetchOrderSummary,
    updateOrderStatus
} from "../api/OrderAPI";

export const ViewOrderPage = (props) => {

    const token = localStorage.getItem("token");
    const [order, setOrder] = useState([]);
    const [price, setPrice] = useState(0);

    const { id } = useParams();

    const fetchDetails = () => {
        fetchOrderSummary(id, setOrder, token);
        fetchOrderPrice(id, setPrice, token);
    }

    useEffect(() => {
        fetchDetails();
    }, []);

    const onPay = (event) => {
        event.preventDefault();
        const payment = {
            tipAmount: Number(event.target.tipInput.value),
            totalAmount: price,
            method: "CASH",
            orderId: id
        }

        createPayment(payment, token).then(r => {
            updateOrderStatus(id, "PAID", token).then(r => {
                archiveOrder(id, token);
                fetchDetails();
            });
        });
    }

    const onCancel = () => {
        cancelOrder(id, token);
    }

    return (
        <div>
            <h2>Order #{id}</h2>
            <p>Status: {order.status}</p>
            <table>
                <thead>
                <tr>
                    <th>Price</th>
                    <th>Quantity</th>
                    <th>Product</th>
                    <th>Product price</th>
                    <th>Variant</th>
                    <th>Variant price</th>
                    <th>Tax</th>
                    <th>Tax percent</th>
                    <th>Discount</th>
                    <th>Discount percent</th>
                    <th id={"weightHead"}>Weight</th>
                </tr>
                </thead>
                <tbody>
                {order.items && order.items.map((item) => (
                    <tr>
                        <td>{item.price}</td>
                        <td>x{item.quantity}</td>
                        <td>{item.product.title}</td>
                        <td>{item.product.price}</td>
                        <td>{item.variant.title}</td>
                        <td>{item.variant.additionalPrice}</td>
                        <td>{item.product.tax == null ? "none" : item.product.tax.title}</td>
                        <td>{item.product.tax == null ? "none" : item.product.tax.percentage}</td>
                        <td>{item.product.discount == null ? "none" : item.product.discount.title}</td>
                        <td>{item.product.discount == null ? "none" : item.product.discount.percentage}</td>
                        <td id={"weightCell"}>{item.product.weight} {item.product.weightUnit}</td>
                    </tr>
                ))}
                </tbody>
            </table>
            <p id={"totalPrice"}>Total price: {price}</p>

            {(order.status === "OPENED" || order.status === "PARTIALLY_PAID") && (
                <>
                    <form onSubmit={onPay}>
                        <label>Tip: </label>
                        <input type={"number"} name={"tipInput"} step={"any"} min={0}></input>
                        <input type={"submit"} value={"Pay"}></input>
                    </form>
                    <button>Split payment</button>
                </>
            )}

            {order.status === "OPENED" && (
                <button onClick={onCancel}>Cancel order</button>
            )}

        </div>
    );
}