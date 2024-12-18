import { useEffect, useState } from 'react';
import { fetchMerchant, updateMerchant } from '../api/MerchantAPI';
import { 
    fetchEmployees,
    createEmployee,
    updateEmployee,
    deleteEmployee 
} from '../api/EmployeeAPI';
import '../styles/MerchantStyle.css';
import { useNavigate } from "react-router-dom";

export const MerchantManagementPage = () => {
    const navigate = useNavigate();

    const [merchant, setMerchant] = useState({});
    const [employees, setEmployees] = useState([]);
    const [editableMerchant, setEditableMerchant] = useState({
        id:'',
        name: '',
        email: '',
        phone: '',
        address: {
            address1: '',
            address2: '',
            city: '',
            country: '',
            countryCode: '',
            zipCode: '',
        },
    });
    const [isEditing, setIsEditing] = useState(false);

    useEffect(() => {
        loadMerchant();
    }, []);

    const loadMerchant = async () => {
        await fetchMerchant((data) => {
            setMerchant(data);
            setEditableMerchant(data);
        });
        fetchEmployees("OWNER", null, setEmployees);
    };

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setEditableMerchant((prev) => ({
            ...prev,
            [name]: value,
        }));
    };

    const handleAddressChange = (e) => {
        const { name, value } = e.target;
        setEditableMerchant((prev) => ({
            ...prev,
            address: {
                ...prev.address,
                [name]: value,
            },
        }));
    };

    const handleUpdateMerchant = async () => {
        try {
            editableMerchant.id = merchant.id;
            await updateMerchant(editableMerchant);
            setMerchant(editableMerchant);
            setIsEditing(false);
            loadMerchant();
        } catch (error) {
            console.error("Error updating merchant:", error);
        }
    };

    const returnHome = () => {
        navigate("/home");
    };

    return (
        <div className="business-details">
            <h1>Business Details</h1>

            {employees.map((employee) => (
                <div className="detail-item" key={employee.username}>
                    <h2><strong>Owner:</strong> {employee.username}</h2>
                </div>
            ))}

            {isEditing ? (
                <>
                    <div className="detail-item">
                        <h2>Edit Business Details</h2>
                        <label>Business Name:</label>
                        <input
                            type="text"
                            name="name"
                            value={editableMerchant.name || ''}
                            onChange={handleInputChange}
                        />
                        <label>Email:</label>
                        <input
                            type="email"
                            name="email"
                            value={editableMerchant.email || ''}
                            onChange={handleInputChange}
                        />
                        <label>Phone:</label>
                        <input
                            type="text"
                            name="phone"
                            value={editableMerchant.phone || ''}
                            onChange={handleInputChange}
                        />
                        <h3>Address</h3>
                        <label>Address 1:</label>
                        <input
                            type="text"
                            name="address1"
                            value={editableMerchant.address?.address1 || ''}
                            onChange={handleAddressChange}
                        />
                        <label>Address 2:</label>
                        <input
                            type="text"
                            name="address2"
                            value={editableMerchant.address?.address2 || ''}
                            onChange={handleAddressChange}
                        />
                        <label>City:</label>
                        <input
                            type="text"
                            name="city"
                            value={editableMerchant.address?.city || ''}
                            onChange={handleAddressChange}
                        />
                        <label>Country:</label>
                        <input
                            type="text"
                            name="country"
                            value={editableMerchant.address?.country || ''}
                            onChange={handleAddressChange}
                        />
                        <label>Country Code:</label>
                        <input
                            type="text"
                            name="countryCode"
                            value={editableMerchant.address?.countryCode || ''}
                            onChange={handleAddressChange}
                        />
                        <label>Zip Code:</label>
                        <input
                            type="text"
                            name="zipCode"
                            value={editableMerchant.address?.zipCode || ''}
                            onChange={handleAddressChange}
                        />
                        <button onClick={() => handleUpdateMerchant()}>Save Changes</button>
                        <button onClick={() => setIsEditing(false)}>Cancel</button>
                    </div>
                </>
            ) : (
                <>
                    <div className="detail-item">
                        <h2><strong>Business Name:</strong> {merchant.name}</h2>
                    </div>
                    <div className="detail-item">
                        <h2><strong>Contact Info:</strong></h2>
                        <h3>Email: {merchant.email}</h3>
                        <h3>Phone: +{merchant.phone}</h3>
                    </div>
                    {merchant.address && (
                        <div className="detail-item">
                            <h2><strong>Address:</strong></h2>
                            <h3>Address 1: {merchant.address.address1}</h3>
                            <h3>Address 2: {merchant.address.address2}</h3>
                            <h3>City: {merchant.address.city}</h3>
                            <h3>Country: {merchant.address.country}</h3>
                            <h3>Country Code: {merchant.address.countryCode}</h3>
                            <h3>Zip Code: {merchant.address.zipCode}</h3>
                        </div>
                    )}
                    <button onClick={() => setIsEditing(true)}>Edit Merchant Info</button>
                </>
            )}
            <div>
                <button onClick={returnHome}>Back To Home</button>
            </div>
        </div>
    );
};
