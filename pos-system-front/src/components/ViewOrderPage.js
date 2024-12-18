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
    fetchOrderSummary, fetchOrderUnpaidPrice,
    updateOrderStatus, validatePaymentsAndUpdateOrderStatus
} from "../api/OrderAPI";

export const ViewOrderPage = (props) => {

    const token = localStorage.getItem("token");
    const [order, setOrder] = useState([]);
    const [price, setPrice] = useState(0);
    const [unpaidPrice, setUnpaidPrice] = useState(0);
    const [splitPayments, setSplitPayments] = useState([]);

    const { id } = useParams();

    const setupSplitPayments = (event) => {
        event.preventDefault();
        let numberOfSplitPayments = Number(event.target.splitInput.value);

        let payments = []
        let sum = 0;

        for(let i = 0 ; i<numberOfSplitPayments-1;++i) {
            const amount = Number((unpaidPrice/numberOfSplitPayments).toFixed(2));
            sum += amount;
            payments.push(amount);
        }

        console.log(sum);
        payments.push(unpaidPrice - sum);

        setSplitPayments(payments);
    }

    const fetchDetails = () => {
        fetchOrderSummary(id, setOrder, token);
        fetchOrderPrice(id, setPrice, token);
        fetchOrderUnpaidPrice(id, setUnpaidPrice, token);
    }

    useEffect(() => {
        fetchDetails();
    }, []);

    const onPay = (event) => {
        event.preventDefault();
        const payment = {
            tipAmount: Number(event.target.tipInput.value),
            totalAmount: unpaidPrice,
            method: "CASH",
            orderId: id
        }

        createPayment(payment, token).then(r => {
            validatePaymentsAndUpdateOrderStatus(id, token).then(r => {
                archiveOrder(id, token);
                fetchDetails();
            });
        });
    }

    const onCancel = () => {
        cancelOrder(id, token).then(r => {
            fetchDetails();
        });
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
            {unpaidPrice < price && (
                <p>Unpaid: {unpaidPrice}</p>
            )}

            {(order.status === "OPENED")  && (
                <button onClick={onCancel}>Cancel order</button>
            )}

            {(order.status === "OPENED" || order.status === "PARTIALLY_PAID") && (
                <>
                    <form onSubmit={onPay}>
                        <label>Tip: </label>
                        <input type={"number"} name={"tipInput"} step={"any"} min={0}></input>
                        <input type={"submit"} value={"Pay"}></input>
                    </form>


                    <form onSubmit={setupSplitPayments}>
                        <label>Number of splits: </label>
                        <input type={"number"} name={"splitInput"} min={2} max={5}></input>
                        <input type={"submit"} value={"Split"}></input>
                    </form>
                    <ul>
                        {splitPayments.map((amount) => (
                            <li>
                                {amount}
                            </li>
                        ))}
                    </ul>
                </>
            )}



        </div>
    );
}