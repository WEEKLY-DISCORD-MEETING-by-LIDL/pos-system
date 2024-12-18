import { useEffect, useState } from 'react';
import { fetchMerchant } from '../api/MerchantAPI';
import { fetchEmployees } from '../api/EmployeeAPI';
import '../styles/MerchantStyle.css';
import { useNavigate } from "react-router-dom";

export const MerchantManagementPage = () => {

    const navigate = useNavigate();
    const [merchants, setMerchant] = useState(0);
    const [employees, setEmployees] = useState([]);

    useEffect(() => {
        fetchMerchant(setMerchant);
        fetchEmployees("OWNER", null, setEmployees);
    }, [])

    console.log(employees);


    const returnHome = () => {
        navigate("/home")
    }


    return (
        <div className="business-details">
            <h1>Business Details</h1>
            {employees.map((employee) => (
                <div className="detail-item" key={employee.username}>
                    <h2><strong>Owner:</strong> {employee.username}</h2>
                </div>
            ))}

            <div className="detail-item">
                <h2><strong>Business Name:</strong> {merchants.name}</h2>
            </div>
            <div className="detail-item">
                <h2><strong>Location:</strong></h2>
            </div>
            {merchants.address && (
                <>
                    <div className="detail-item">
                    <h2><strong>Address 1:</strong> {merchants.address.address1}</h2>
                    <h2><strong>Address 2:</strong> {merchants.address.address2}</h2>
                    <h2><strong>City:</strong> {merchants.address.city}</h2>
                    <h2><strong>Country:</strong> {merchants.address.country}</h2>
                    <h2><strong>Country Code:</strong> {merchants.address.countryCode}</h2>
                    <h2><strong>Zip Code:</strong> {merchants.address.zipCode}</h2>
                    </div>
                </>
            )}
            <div className="detail-item">
                <h2><strong>Contact Info:</strong></h2>
            </div>
            <div className="detail-item">
                <h2><strong>Email:</strong> {merchants.email}</h2>
                <h2><strong>Phone:</strong> +{merchants.phone}</h2>
            </div>
            <div>
                <button onClick={returnHome}>Back To Home</button>
            </div>
        </div>
    );

}