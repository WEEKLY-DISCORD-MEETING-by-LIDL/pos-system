import { useState } from "react";
import {Container, Table, Col, Row, FormGroup, Label, Button, Modal, ModalBody, ModalFooter, ModalHeader, Input} from "reactstrap";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
// import TimeSlotCard from "./TimeSlotCard";
import { fetchServices, test } from "../api/ServiceAPI";
import { useEffect } from "react";
import { fetchAvailableTimes } from "../api/ReservationAPI";
import { Navigate, useNavigate } from "react-router-dom";
import { createService, deleteService, updateService} from "../api/ServiceAPI";
import { fetchTaxes } from "../api/TaxAPI";
import { fetchDiscounts } from "../api/DiscountAPI";
import { fetchCategories } from "../api/CategoryAPI";

export const ServicePage = () => {
    const [selectedService, setSelectedService] = useState(null);
    const [selectedDate, setSelectedDate] = useState(new Date());
    const [selectedTime, setSelectedTime] = useState("");
    const [services, setServices] = useState([]);
    const [availableTimes, setAvailableTimes] = useState([]);
    const [timePairs, setTimePairs] = useState([])
    const [isValid, setIsValid] = useState(false)
    const [taxes, setTaxes] = useState([]);
    const [discounts, setDiscounts] = useState([]);
    const [categories, setCategories] = useState([])
    const [selectedDiscount, setSelectedDiscount] = useState(null)
    const [selectedTax, setSelectedTax] = useState(null)
    const [selectedCategory, setSelectedCategory] = useState(null)
    const navigate = useNavigate();

    const [createModalOpen, setCreateModalOpen] = useState(false); // State to control modal visibility
    const [createServiceData, setCreateServiceData] = useState({
        id: null,
        title: null,
        categoryId: null,
        price: null,
        discountId: null,
        taxId: null,
        durationMins: null
    }); // State for form input fields

    const [updateModalOpen, setUpdateModalOpen] = useState(false); // State to control modal visibility


    const handleRowClick = (service) => {
        setSelectedService(service);
    };

    useEffect(() => {
        fetchServices(null, null, setServices);
      }, []);

    const handleDateChange = (date) => {
        setSelectedDate(date);
        fetchAvailableTimes(selectedService.id, date, setAvailableTimes);       
    }

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

    const handleDeleteService = () => {
        deleteService(selectedService);
        fetchServices(null, null, setServices)
    }

    const handleUpdateService = () => {
        setUpdateModalOpen(true);
        fetchDiscounts(setDiscounts);
        fetchTaxes(null, setTaxes);
        fetchCategories(setCategories);
    }

    const handleCloseUpdateModal = () => {
        setUpdateModalOpen(false); // Close the modal
        setCreateServiceData({
            id: null,
            title: null,
            categoryId: null,
            price: null,
            discountId: null,
            taxId: null,
            durationMins: null
        }); // Reset fields
        setSelectedCategory(null);
        setSelectedDiscount(null);
        setSelectedTax(null);
    };

    const handlePutService = () => {
        console.log("Updating Service:", createServiceData); // Placeholder for API call
        updateService(selectedService, createServiceData); // Call your create function
        handleCloseUpdateModal(); // Close modal after submission
        fetchServices(null,null,setServices)
    };

    const handleCreateService = () => {
        setCreateModalOpen(true);
        fetchDiscounts(setDiscounts);
        fetchTaxes(null, setTaxes);
        fetchCategories(setCategories);
    }

    const handleCloseCreateModal = () => {
        setCreateModalOpen(false); // Close the modal
        setCreateServiceData({
            id: null,
            title: null,
            categoryId: null,
            price: null,
            discountId: null,
            taxId: null,
            durationMins: null
        }); // Reset fields
        setSelectedCategory(null);
        setSelectedDiscount(null);
        setSelectedTax(null);
    };

    const handleCreateInputChange = (e) => {
        const { name, value } = e.target;
        setCreateServiceData((prevState) => ({
            ...prevState,
            [name]: value,
        }));
    };

    const handleSubmitService = () => {
        console.log("Creating Service:", createServiceData); // Placeholder for API call
        createService(createServiceData); // Call your create function
        handleCloseCreateModal(); // Close modal after submission
        fetchServices(null,null,setServices)
    };

    const handleDiscountChange = (e) => {
        const { value } = e.target;
        setSelectedDiscount(value);

        setCreateServiceData((prevState) => ({
            ...prevState,
            discountId: value,
        }));
    }
    const handleTaxChange = (e) => {
        const { value } = e.target;
        setSelectedTax(value);

        setCreateServiceData((prevState) => ({
            ...prevState,
            taxId: value,
        }));
    }
    const handleCategoryChange = (e) => {
        const { value } = e.target;
        setSelectedCategory(value);

        setCreateServiceData((prevState) => ({
            ...prevState,
            categoryId: value,
        }));
    }

    return (
        <Container fluid>
            <Row>
                {/* Left Column */}
                <Col md="4" className="border-right p-3">
                    {selectedService ? (
                        <h3>{selectedService.title}</h3>
                    ) : (
                        <h3>Select a Service</h3>
                    )}
                    {selectedService ? (
                        <div>
                            <p><strong>Price:</strong> ${selectedService.price.toFixed(2)}</p>
                            <p><strong>Duration:</strong> {selectedService.durationMins} mins</p>
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
                            <p></p>
                            <Button
                                color="success"
                                onClick={() => navigate("/createReservation", {state: {selectedDate, selectedTime, selectedService}})}
                                // onClick={() => console.log(selectedService) }
                                disabled={!isValid}
                            >
                                Create Reservation
                            </Button>
                            <p></p>
                            <p></p>
                            <Button
                                color="danger"
                                onClick={handleDeleteService}
                            >
                                Delete Service
                            </Button>
                            <Button
                                color="warning"
                                onClick={handleUpdateService}
                            >
                                Edit Service
                            </Button>
                        </div>
                    ) : (
                        <p>No service selected. Click a row to see details here.</p>
                    )}
                </Col>
                {/* Right Column */}
                <Col md="8">
                    <h3>Available Services</h3>
                    <Button
                        color="success"
                        onClick={handleCreateService}
                    >
                        Create Service
                    </Button>
                    <Table>
                        <thead>
                            <tr>
                                {/* <th>#</th> */}
                                <th>Service Title</th>
                                <th>Price</th>
                                <th>Duration</th>
                            </tr>
                        </thead>
                        <tbody>
                            {services.map((service) => (
                                <tr
                                    key={service.id}
                                    onClick={() => handleRowClick(service)} // Pass the full row data
                                    className={selectedService && selectedService.id === service.id ? 'table-active' : ''}
                                    style={{ cursor: 'pointer' }}
                                >
                                    {/* <td>{service.id}</td> */}
                                    <td>{service.title}</td>
                                    <td>${service.price.toFixed(2)}</td>
                                    <td>{service.durationMins} mins</td>
                                </tr>
                            ))}
                        </tbody>
                    </Table>
                </Col>
            </Row>




            <Modal isOpen={createModalOpen} toggle={handleCloseCreateModal}>
                <ModalHeader toggle={handleCloseCreateModal}>Create Service</ModalHeader>
                <ModalBody>
                    <FormGroup>
                        <Label for="serviceTitle">Title</Label>
                        <Input
                            type="text"
                            id="serviceTitle"
                            name="title"
                            placeholder="Enter service title"
                            value={createServiceData.title}
                            onChange={handleCreateInputChange}
                        />
                    </FormGroup>
                    <FormGroup>
                        <Label for="servicePrice">Price</Label>
                        <Input
                            type="number"
                            id="servicePrice"
                            name="price"
                            placeholder="Enter service price"
                            value={createServiceData.price}
                            onChange={handleCreateInputChange}
                            step="0.01"
                        />
                    </FormGroup>
                    <FormGroup>
                        <Label for="serviceDuration">Duration in minutes</Label>
                        <Input
                            type="number"
                            id="serviceDuration"
                            name="durationMins"
                            placeholder="Enter service duration"
                            value={createServiceData.durationMins}
                            onChange={handleCreateInputChange}
                            step="1"
                        />
                    </FormGroup>
                    <FormGroup>
                        <Label for="discountSelect">Select Discount</Label>
                        <Input
                            type="select"
                            id="discountSelect"
                            value={selectedDiscount || ""}
                            onChange={handleDiscountChange}
                        >
                            <option value="" disabled>
                            -- Choose a Discount --
                            </option>
                            {discounts.map((discount) => (
                            <option key={discount.id} value={discount.id}>
                                {discount.title} - {discount.percentage}%
                            </option>
                            ))}
                        </Input>
                    </FormGroup>
                    <FormGroup>
                        <Label for="taxSelect">Select Tax</Label>
                        <Input
                            type="select"
                            id="taxSelect"
                            value={selectedTax || ""}
                            onChange={handleTaxChange}
                        >
                            <option value="" disabled>
                            -- Choose a Tax --
                            </option>
                            {taxes.map((tax) => (
                            <option key={tax.id} value={tax.id}>
                                {tax.title} - {tax.percentage * 100}%
                            </option>
                            ))}
                        </Input>
                    </FormGroup>
                    <FormGroup>
                        <Label for="categorySelect">Select Category</Label>
                        <Input
                            type="select"
                            id="categorySelect"
                            value={selectedCategory || ""}
                            onChange={handleCategoryChange}
                        >
                            <option value="" disabled>
                            -- Choose a Category --
                            </option>
                            {categories.map((category) => (
                            <option key={category.id} value={category.id}>
                                {category.title}
                            </option>
                            ))}
                        </Input>
                    </FormGroup>
                </ModalBody>
                <ModalFooter>
                    <Button color="success" onClick={handleSubmitService}>
                        Submit
                    </Button>
                    <Button color="secondary" onClick={handleCloseCreateModal}>
                        Cancel
                    </Button>
                </ModalFooter>
            </Modal>



            <Modal isOpen={updateModalOpen} toggle={handleCloseUpdateModal}>
                <ModalHeader toggle={handleCloseUpdateModal}>Update Service</ModalHeader>
                <ModalBody>
                    <FormGroup>
                        <Label for="serviceTitle">Title</Label>
                        <Input
                            type="text"
                            id="serviceTitle"
                            name="title"
                            placeholder="Enter service title"
                            value={createServiceData.title}
                            onChange={handleCreateInputChange}
                        />
                    </FormGroup>
                    <FormGroup>
                        <Label for="servicePrice">Price</Label>
                        <Input
                            type="number"
                            id="servicePrice"
                            name="price"
                            placeholder="Enter service price"
                            value={createServiceData.price}
                            onChange={handleCreateInputChange}
                            step="0.01"
                        />
                    </FormGroup>
                    {/* <FormGroup>
                        <Label for="serviceDuration">Duration in minutes</Label>
                        <Input
                            type="number"
                            id="serviceDuration"
                            name="durationMins"
                            placeholder="Enter service duration"
                            value={createServiceData.durationMins}
                            onChange={handleCreateInputChange}
                            step="1"
                        />
                    </FormGroup> */}
                    <FormGroup>
                        <Label for="discountSelect">Select Discount</Label>
                        <Input
                            type="select"
                            id="discountSelect"
                            value={selectedDiscount || ""}
                            onChange={handleDiscountChange}
                        >
                            <option value="" disabled>
                            -- Choose a Discount --
                            </option>
                            {discounts.map((discount) => (
                            <option key={discount.id} value={discount.id}>
                                {discount.title} - {discount.percentage}%
                            </option>
                            ))}
                        </Input>
                    </FormGroup>
                    <FormGroup>
                        <Label for="taxSelect">Select Tax</Label>
                        <Input
                            type="select"
                            id="taxSelect"
                            value={selectedTax || ""}
                            onChange={handleTaxChange}
                        >
                            <option value="" disabled>
                            -- Choose a Tax --
                            </option>
                            {taxes.map((tax) => (
                            <option key={tax.id} value={tax.id}>
                                {tax.title} - {tax.percentage * 100}%
                            </option>
                            ))}
                        </Input>
                    </FormGroup>
                    <FormGroup>
                        <Label for="categorySelect">Select Category</Label>
                        <Input
                            type="select"
                            id="categorySelect"
                            value={selectedCategory || ""}
                            onChange={handleCategoryChange}
                        >
                            <option value="" disabled>
                            -- Choose a Category --
                            </option>
                            {categories.map((category) => (
                            <option key={category.id} value={category.id}>
                                {category.title}
                            </option>
                            ))}
                        </Input>
                    </FormGroup>
                </ModalBody>
                <ModalFooter>
                    <Button color="success" onClick={handlePutService}>
                        Submit
                    </Button>
                    <Button color="secondary" onClick={handleCloseUpdateModal}>
                        Cancel
                    </Button>
                </ModalFooter>
            </Modal>
                   

        </Container>
    );
}
