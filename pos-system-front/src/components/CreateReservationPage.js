import React, { useEffect, useState } from "react";
import {
  Container,
  Row,
  Col,
  Form,
  FormGroup,
  Label,
  Input,
  CustomInput,
  Button,
} from "reactstrap";
import { useLocation, useNavigate } from "react-router-dom";
import { FetchCustomers, CreateCustomer } from "../api/CustomerAPI";
import { createReservation } from "../api/ReservationAPI";
import { jwtDecode } from "jwt-decode";

export const CreateReservationPage = () => {
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [phoneNumber, setPhoneNumber] = useState("");
    const [sendConfirmation, setSendConfirmation] = useState(false);
    const [customers, setCustomers] = useState([]);
    const [selectedCustomer, setSelectedCustomer] = useState("");
    const [reservationData, setReservationData] = useState(null);
    const location = useLocation();
    const navigate = useNavigate();

    const { selectedDate, selectedTime, selectedService } = location.state || {};
    const date = selectedDate ? new Date(selectedDate).toISOString().split('T')[0] : "N/A";

    function getEmployeeId() {
        const token = localStorage.getItem("token");
        try {
          // Decode the token
          const decodedToken = jwtDecode(token);
          
          // Access employeeId from the payload
          const employeeId = decodedToken.employeeId;
      
          return employeeId;
        } catch (error) {
          console.error("Failed to decode token:", error);
          return null;
        }
      }

    useEffect(() => {
        const fetchData = async () => {
          const customerList = await FetchCustomers(); 
          setCustomers(customerList); 
        };
    
        fetchData(); 
      },[]);

    const handleCustomerChange = (event) => {
        const selectedId = event.target.value;
        const customer = customers.find((customer) => customer.id === (parseInt(selectedId)));
        setSelectedCustomer(customer);
        console.log(customer)

        if (customer) {
            setFirstName(customer.firstName); // Set first name input field
            setLastName(customer.lastName); // Set last name input field
            setPhoneNumber(customer.phone); // Set phone number input field
          }
      };

    const handleReservationCreation = async () => {
        if (!firstName || !lastName || !phoneNumber) {
            alert("Please fill in all customer details or select an existing customer.");
            return;
        }

        let customerId;

        console.log(customers);
        const matchingCustomer = customers.find(
                (customer) =>
                    customer.firstName.trim().toLowerCase() === firstName.trim().toLowerCase() &&
                    customer.lastName.trim().toLowerCase() === lastName.trim().toLowerCase() &&
                    customer.phone.trim() === phoneNumber.trim()
        );

        if (matchingCustomer) {
            customerId = matchingCustomer.id;
        } else {
            // Create new customer if details don't match
            try {
              const newCustomer = await CreateCustomer(firstName, lastName, phoneNumber);
              customerId = newCustomer.id; 
              setSelectedCustomer(newCustomer); 
            } catch (error) {
              console.error("Failed to create new customer:", error.message);
              alert("Error creating customer. Please try again.");
              return;
            }
        }
        
        const startTime = new Date(`${date}T${selectedTime}:00Z`);
        const endTime = new Date(startTime);
        endTime.setMinutes(startTime.getMinutes() + selectedService.durationMins);
        // console.log(startTime);

        const reservation = {
            customerId: customerId,
            serviceId: selectedService.id,
            employeeId: getEmployeeId(), 
            startTime: startTime,
            endTime: endTime,
            reservationStatus: null,
            sendConfirmation: sendConfirmation,
        }

        // console.log(reservation)
        createReservation(reservation)
        navigate("/services")
    }

    return (
        <Container className="d-flex justify-content-center align-items-center vh-100">
          <Row>
            <Col md="12">
              <div className="border p-4 rounded shadow">
                {/* Date and Service Display */}
                <h4 className="mb-4 text-center">Reservation Details</h4>
                <p><strong>Date:</strong> {date || "N/A"}</p>
                <p><strong>Time:</strong> {selectedTime || "N/A"}</p>
                <p><strong>Service:</strong> {selectedService?.title || "N/A"}</p>
    
                {/* Input Fields */}
                <Form>
                  <FormGroup>
                    <Label for="customer">Existing customers:</Label>
                    <Input
                        type="select"
                        name="customer"
                        id="customer"
                        value={selectedCustomer}
                        onChange={handleCustomerChange}
                    >
                    <option value="">--Select a customer--</option>
                    {customers.map((customer) => (
                        <option key={customer.id} value={customer.id}>
                            {customer.firstName} {customer.lastName} {customer.phone}
                        </option>
                    ))}
                    </Input>
                </FormGroup>
                <FormGroup>
                    <Label for="firstName">First Name</Label>
                    <Input
                      type="text"
                      id="firstName"
                      placeholder="Enter first name"
                      value={firstName}
                      onChange={(e) => setFirstName(e.target.value)}
                    />
                  </FormGroup>
    
                  <FormGroup>
                    <Label for="lastName">Last Name</Label>
                    <Input
                      type="text"
                      id="lastName"
                      placeholder="Enter last name"
                      value={lastName}
                      onChange={(e) => setLastName(e.target.value)}
                    />
                  </FormGroup>
    
                  <FormGroup>
                    <Label for="phoneNumber">Phone Number</Label>
                    <Input
                      type="text"
                      id="phoneNumber"
                      placeholder="Enter phone number"
                      value={phoneNumber}
                      onChange={(e) => setPhoneNumber(e.target.value)}
                    />
                  </FormGroup>
    
                  {/* Toggle Switch */}
                  <FormGroup check className="mt-3">
                    <Label check>
                      <Input
                        type="switch"
                        checked={sendConfirmation}
                        onChange={() => setSendConfirmation(!sendConfirmation)}
                      />
                      {" Send Confirmation"}
                    </Label>
                  </FormGroup>
    
                  {/* Submit Button */}
                  <Button color="primary" className="mt-4 w-100" 
                    onClick={handleReservationCreation}>
                    Submit
                  </Button>
                </Form>
              </div>
            </Col>
          </Row>
        </Container>
      );
}