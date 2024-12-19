import {useEffect, useState} from "react";
import { fetchService } from "../api/ServiceAPI";
import { useLocation } from "react-router-dom";
import {fetchReservationUnpaidPrice, updateReservation} from "../api/ReservationAPI";
import { createPayment } from "../api/PaymentAPI";
import { Button } from "reactstrap";

export const ReservationPaymentPage = () => {
    const [splitPayments, setSplitPayments] = useState([]);
    const [price, setPrice] = useState(null);
    const [unpaidPrice, setUnpaidPrice] = useState(0);
    const location = useLocation();
    const [selectedService, setSelectedService] = useState(null);

    const { reservation } = location.state || {};

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
    //     if (!selectedService || !reservation) {
    //         return 
    //     }
    //     setPrice(selectedService.price)
    //     fetchReservationUnpaidPrice(reservation, setUnpaidPrice);
    // }

    // useEffect(() => {
    //     fetchDetails();
    // }, [selectedService, reservation]);

    useEffect(() => {
        fetchService(reservation.serviceId, setSelectedService);
        console.log(selectedService)
        console.log(reservation)
        fetchReservationUnpaidPrice(reservation, setUnpaidPrice);
    }, [reservation,]);


    const onPay = (event) => {
        event.preventDefault();
        const payment = {
            tipAmount: Number(event.target.tipInput.value),
            totalAmount: unpaidPrice,
            method: "CASH",
            reservationId: reservation.id,
            orderId: 0
        }

        console.log(payment)
        createPayment(payment, token).then(r => {
            const updatedReservation = {
                ...reservation,
                reservationStatus: "CONFIRMED"
            }

            updateReservation(updatedReservation, updatedReservation);
        });
        // createPayment(payment).then(r => {
        //     validatePaymentsAndUpdateOrderStatus(reservation.id).then(r => {
        //         archiveOrder(reservation.id);
        //         fetchDetails();
        //     });
        // });
    }



    return (
        <div>
            {reservation && reservation.id ? (
                <>
                    <h2>Reservation #{reservation.id}</h2>
                </>
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