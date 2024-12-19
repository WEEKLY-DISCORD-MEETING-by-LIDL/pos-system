import axios from "axios";

const token = localStorage.getItem("token");

export const fetchAvailableTimes = async (serviceId, date, setAvailableTime) => {
    try {
        const params = new URLSearchParams();
        // const formattedDate = date.toISOString().split('T')[0]
        const formattedDate = date.toLocaleDateString("en-CA");
        
        if (formattedDate != null) params.append('date', formattedDate);

        const requestString = `http://localhost:8080/services/${serviceId}/available-times?${params.toString()}`;
        // console.log("serviceId:", serviceId);
        // console.log("date:", formattedDate);
        // console.log("Request URL:", requestString);
        const response = await axios.get(requestString, {
            headers:{
                Authorization: `Bearer ${token}`,
            },
        });
        console.log(response.data);
        setAvailableTime(response.data);
        // return response.data;
    } catch (err) {
        console.error(err.message);
    }
};

export const createReservation = async (reservationData) => {
    try {
        const requestString = `http://localhost:8080/reservations`;

        const response = await axios.post(requestString, reservationData, {
            headers:{
                Authorization: `Bearer ${token}`,
            },
        });
        console.log(response.data)
        return response.data
    } catch (error) {
        console.error(error.message);
    }
}

export const fetchReservations = async (setReservations) => {
    try {
        const requestString = `http://localhost:8080/reservations`;
        // console.log("serviceId:", serviceId);
        // console.log("date:", formattedDate);
        // console.log("Request URL:", requestString);
        const response = await axios.get(requestString, {
            headers:{
                Authorization: `Bearer ${token}`,
            },
        });
        console.log(response.data);
        setReservations(response.data);
        // return response.data;
    } catch (err) {
        console.error(err.message);
    }
};

export const updateReservation = async (reservation, reservationData) => {
    try {
        const requestString = `http://localhost:8080/reservations/${reservation.id}`;

        const response = await axios.put(requestString, reservationData, {
            headers:{
                Authorization: `Bearer ${token}`,
            },
        });
        console.log(response.data)
        // Update reservation list
      } catch (error) {
        console.error('Error saving reservation:', error);
      }
}

export const fetchReservationUnpaidPrice = async (reservation, setUnpaidPrice) => {
    try {
        const requestString = `http://localhost:8080/reservations/${reservation.id}/unpaid-price`;

        const response = await axios.get(requestString, {
            headers:{
                Authorization: `Bearer ${token}`,
            },
        });

        console.log(response.data)
        setUnpaidPrice(response.data)
    } catch (error) {
        console.error(error.message);
    }
}