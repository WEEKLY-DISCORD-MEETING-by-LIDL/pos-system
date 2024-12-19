import {useEffect, useState} from "react";
import { fetchService } from "../api/ServiceAPI";
import { useLocation } from "react-router-dom";
import { fetchReservationUnpaidPrice } from "../api/ReservationAPI";
import { createPayment } from "../api/PaymentAPI";
import { Button } from "reactstrap";

export const ReservationPaymentPage = () => {
    const [splitPayments, setSplitPayments] = useState([]);
    const [price, setPrice] = useState(null);
    const [unpaidPrice, setUnpaidPrice] = useState(0);
    const location = useLocation();

    const { selectedService, selectedReservation } = location.state || {};

    const token = localStorage.getItem("token");

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

    // const fetchDetails = () => {
    //     if (!selectedService || !selectedReservation) {
    //         return 
    //     }
    //     setPrice(selectedService.price)
    //     fetchReservationUnpaidPrice(selectedReservation, setUnpaidPrice);
    // }

    // useEffect(() => {
    //     fetchDetails();
    // }, [selectedService, selectedReservation]);

    useEffect(() => {
        console.log(selectedService)
        console.log(selectedReservation)
        if (selectedService && selectedReservation) {
            setPrice(selectedService.price);
            fetchReservationUnpaidPrice(selectedReservation, setUnpaidPrice);
        }
    }, [selectedService, selectedReservation,]);


    const onPay = (event) => {
        event.preventDefault();
        const payment = {
            tipAmount: Number(event.target.tipInput.value),
            totalAmount: unpaidPrice,
            method: "CASH",
            reservationId: selectedReservation.id
        }

        console.log(payment)
        createPayment(payment, token);
        // createPayment(payment).then(r => {
        //     validatePaymentsAndUpdateOrderStatus(selectedReservation.id).then(r => {
        //         archiveOrder(selectedReservation.id);
        //         fetchDetails();
        //     });
        // });
    }



    return (
        <div>
            {selectedReservation && selectedReservation.id ? (
                <h2>Reservation #{selectedReservation.id}</h2>
            ) : (
                <h2>Loading reservation...</h2>
            )}
            {selectedService && selectedService.title ? (
                <p>Title: {selectedService.title}</p>
            ) : (
                <p>Loading Service...</p>
            )}
            {selectedService && selectedService.price ? (
                <p>Total price: {selectedService.price}</p>
            ) : (
                <p>Loading price...</p>
            )}
            <form onSubmit={onPay}>
                <label>Tip: </label>
                <input type={"number"} name={"tipInput"} step={"any"} min={0}></input>
                <input type={"submit"} value={"Pay"}></input>
            </form>
            

            {/* {unpaidPrice < price && (
                <p>Unpaid: {unpaidPrice}</p>
            )} */}
        </div>
    );



}