import { fetchReservations, updateReservation } from "../api/ReservationAPI";
import React, { useState, useEffect } from 'react';
import {
  Table,
  Button,
  Modal,
  ModalHeader,
  ModalBody,
  ModalFooter,
  Form,
  FormGroup,
  Label,
  Input,
  Container,
  Row,
  Col,
} from 'reactstrap';
import DatePicker from "react-datepicker";
import { fetchService, fetchServices } from "../api/ServiceAPI";
import { FetchCustomers } from "../api/CustomerAPI";
import { fetchAvailableTimes } from "../api/ReservationAPI";
import { useNavigate } from "react-router-dom";

export const ReservationsPage = () => {
    const [reservations, setReservations] = useState([]); // State for list of reservations
    const [selectedReservation, setSelectedReservation] = useState(null); // State for currently selected reservation
    const [modalOpen, setModalOpen] = useState(false); // Modal visibility state
    const [loading, setLoading] = useState(false); // Loading state
    const [selectedDate, setSelectedDate] = useState(new Date());
    const [availableTimes, setAvailableTimes] = useState([]);
    const [timePairs, setTimePairs] = useState([])
    const [selectedService, setSelectedService] = useState(null)
    const [selectedTime, setSelectedTime] = useState("")
    const [isValid, setIsValid] = useState(false);
    const [services, setServices] = useState([]);
    const [customers, setCustomers] = useState([]);
    const navigate = useNavigate();

    
      useEffect(() => {
        const fetchData = async () => {
            fetchReservations(setReservations);
            fetchServices(null, null, setServices)
            const customerList = await FetchCustomers();
            console.log(customerList)
            setCustomers(customerList)
        };
    
        fetchData(); 
      },[]);

      const handleEdit = (reservation) => {
        setSelectedReservation(reservation);
        fetchService(reservation.serviceId, setSelectedService)
        setModalOpen(true);
      };

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setSelectedReservation({
          ...selectedReservation,
          [name]: value,
        });
      };

      const handleSave = async () => {
        const date = selectedDate ? new Date(selectedDate).toISOString().split('T')[0] : "N/A";
        const startTime = new Date(`${date}T${selectedTime}:00`);
        const endTime = new Date(startTime);
        endTime.setMinutes(startTime.getMinutes() + selectedService.durationMins);

        const reservation = {
            customerId: null,
            serviceId: selectedService.id,
            employeeId: null, 
            startTime: startTime,
            endTime: endTime,
            reservationStatus: null,
            sendConfirmation: null,
        }

        updateReservation(selectedReservation, reservation)
        fetchReservations(setReservations);
        setModalOpen(false);
      };

      useEffect(() => {
        if (selectedService == null || selectedDate == null) {
            return
        }
        // setSelectedDate(date);
        fetchAvailableTimes(selectedService.id, selectedDate, setAvailableTimes); 
    },[selectedDate,selectedService])

    useEffect(() => {
        console.log(availableTimes)
        if (!availableTimes || availableTimes.length === 0) {
            return;
          }
        const times = availableTimes.map((dateTime) => dateTime.split("T")[1].slice(0, 5)); // Extract HH:MM
        
        const pairs = [];
        for (let i = 0; i < times.length; i += 2) {
            if (i + 1 < times.length) {
                pairs.push(`${times[i]}-${times[i + 1]}`);
            }
        }

        console.log("Processed Time Pairs:", pairs);
        setTimePairs(pairs); // Update the time pairs state
    }, [availableTimes])


    const isTimeValid = () => {
        const [inputHours, inputMinutes] = selectedTime.split(":").map(Number);
        const inputTotalMinutes = inputHours * 60 + inputMinutes;

        for (const range of timePairs) {
            const [start, end] = range.split("-");
            const [startHours, startMinutes] = start.split(":").map(Number);
            const [endHours, endMinutes] = end.split(":").map(Number);

            const startTotalMinutes = startHours * 60 + startMinutes;
            const endTotalMinutes = endHours * 60 + endMinutes;

            if (inputTotalMinutes >= startTotalMinutes && inputTotalMinutes <= endTotalMinutes) {
                return true; // The input time is valid
            }
        }
        return false; // If no range matches
    };

    useEffect(() => {
        setIsValid(isTimeValid()); // Update the validity whenever selectedTime changes
    }, [selectedTime, timePairs]);

    
    const handleCancel = (reservation) => {
        setSelectedReservation(reservation)
        const newReservation = {
            customerId: null,
            serviceId: null,
            employeeId: null, 
            startTime: null,
            endTime: null,
            reservationStatus: "CANCELED",
            sendConfirmation: null,
        }
        updateReservation(selectedReservation, newReservation)
        fetchReservations(setReservations);
    }

    const handlePayment = async (reservation) => {
        setSelectedReservation(reservation);

        navigate("/pay-reservation", {state: {reservation}});

        // navigate("/pay-reservation", {state: {selectedService, selectedReservation}})
    }

      return (
        <Container className="mt-5">
          <Row>
            <Col>
              <h2>Reservations</h2>
              {loading ? (
                <p>Loading...</p>
              ) : (
                <Table striped>
                  <thead>
                    <tr>
                      <th>Service</th>
                      <th>Start Time</th>
                      <th>Customer</th>
                      <th>Employee</th>
                      <th>Status</th>
                      <th>Actions</th>
                    </tr>
                  </thead>
                  <tbody>
                    {reservations.map((reservation) => {
                    // Find the matching service
                    const service = services?.find((s) => s.id === reservation.serviceId);
                    const customer = customers?.find((c) => c.id === reservation.customerId);

                    return (
                    <tr key={reservation.id}>
                        {/* Service Title */}
                        <td>{service ? service.title : "Unknown Service"}</td>

                        {/* Start Time */}
                        <td>{reservation.startTime}</td>

                        {/* Customer Full Name */}
                        <td>
                        {customer
                            ? `${customer.firstName} ${customer.lastName}`
                            : "Unknown Customer"}
                        </td>
                        <td>{reservation.employeeId || "N/A"}</td>
                        <td>{reservation.reservationStatus}</td>
                        <td>
                            <Button color="primary" size="sm" onClick={() => handlePayment(reservation)}>
                                Pay
                            </Button>
                          <Button color="primary" size="sm" onClick={() => handleEdit(reservation)}>
                            Edit
                          </Button>
                          <Button color="primary" size="sm" onClick={() => handleCancel(reservation)}>
                            Cancel
                          </Button>
                        </td>
                      </tr>
                    );
                  })}
                  </tbody>
                </Table>
              )}
            </Col>
          </Row>
    
          {/* Modal for Editing Reservation */}
          {selectedReservation && (
            <Modal isOpen={modalOpen} toggle={() => setModalOpen(false)}>
              <ModalHeader toggle={() => setModalOpen(false)}>Edit Reservation</ModalHeader>
              <ModalBody>
                <Form>
                  {/* <FormGroup>
                    <Label for="startTime">Start Date</Label>
                    <Input
                      type="text"
                      name="customerName"
                      id="customerName"
                      value={selectedReservation.customerName}
                      onChange={handleInputChange}
                    />
                  </FormGroup> */}
                  <FormGroup>
                    <Label for="datePicker">Select Date: </Label>
                    <DatePicker
                        dateFormat="yyyy/MM/dd"
                        selected={selectedDate}
                        onChange={(date) => setSelectedDate(date)} // Update state when date is selected
                        className="form-control" 
                        id="datePicker"
                    />
                </FormGroup>
                <FormGroup>
                <p><strong>Available times:</strong></p>
                {/* Display Time Pairs */}
                {timePairs.length > 0 ? (
                    <ul>
                    {timePairs.map((pair, index) => (
                        <li key={index}>{pair}</li>
                    ))}
                    </ul>
                ) : (
                    <p>No available times.</p>
                )}
                <label>
                    Select Time:
                    <input
                        type="time"
                        value={selectedTime}
                        onChange={(e) => setSelectedTime(e.target.value)}
                    />
                </label>
                {!isValid && (
                    <p style={{ color: "red" }}>Invalid time! Please select a time within the allowed ranges.</p>
                )}
                {isValid && selectedTime && (
                    <p style={{ color: "green" }}>Valid time selected: {selectedTime}</p>
                )}
                </FormGroup>
                  {/* <FormGroup>
                    <Label for="date">Date</Label>
                    <Input
                      type="date"
                      name="date"
                      id="date"
                      value={selectedReservation.date}
                      onChange={handleInputChange}
                    />
                  </FormGroup> */}
                  {/* <FormGroup>
                    <Label for="time">Time</Label>
                    <Input
                      type="time"
                      name="time"
                      id="time"
                      value={selectedReservation.time}
                      onChange={handleInputChange}
                    />
                  </FormGroup> */}
                </Form>
              </ModalBody>
              <ModalFooter>
                <Button color="primary" onClick={handleSave} disabled={!isValid}>
                  Save Changes
                </Button>{' '}
                <Button color="secondary" onClick={() => setModalOpen(false)}>
                  Cancel
                </Button>
              </ModalFooter>
            </Modal>
          )}
        </Container>
      );
    };
    
    // export default ReservationsPage;