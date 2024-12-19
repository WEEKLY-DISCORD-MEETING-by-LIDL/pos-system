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

    //Merchant states
    const [merchant, setMerchant] = useState({});
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

    //Employee states
    const [employees, setEmployees] = useState([]);
    const [newEmployee, setNewEmployee] = useState({
        id:'',
        merchantId: '',
        firstName: '',
        lastName: '',
        employeeType: 'REGULAR', // Default type
        username: '',
        password: '',
    });
    const [editingEmployee, setEditingEmployee] = useState(null);

    useEffect(() => {
        loadMerchant();
        loadEmployees();
    }, []);

    //Merchant methods
    const loadMerchant = async () => {
        await fetchMerchant((data) => {
            setMerchant(data);
            setEditableMerchant(data);
            console.log("Merchant data:", data);
        });
    };

    const handleMerchantInputChange = (e) => {
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

    //Employee methods
    const loadEmployees = async () => {
        try {
            const allEmployees = [];
            await fetchEmployees("OWNER", null, (data) => {
                allEmployees.push(...data);
            });
            await fetchEmployees("REGULAR", null, (data) => {
                allEmployees.push(...data);
            });
            console.log("All Employees:", allEmployees); 
            setEmployees(allEmployees);
        } catch (error) {
            console.error("Error fetching employees:", error);
        }
    };

    const handleAddEmployee = async () => {
        try {
            console.log("New employee: "+ newEmployee);
            await createEmployee(newEmployee);
            setNewEmployee({
                id:'',
                merchantId: '',
                firstName: '',
                lastName: '',
                employeeType: 'REGULAR',
                username: '',
                password: '',
            });
            loadEmployees();
        } catch (error) {
            console.error('Error adding employee:', error);
        }
    };

    const handleUpdateEmployee = async (employeeId) => {
        try {
            await updateEmployee(employeeId, editingEmployee);
            setEditingEmployee(null);
            loadEmployees();
        } catch (error) {
            console.error('Error updating employee:', error);
        }
    };
    
    const handleDeleteEmployee = async (employeeId) => {
        try {
            await deleteEmployee(employeeId);
            loadEmployees();
        } catch (error) {
            console.error('Error deleting employee:', error);
        }
    };

    const returnHome = () => {
        navigate("/home");
    };

    return (
        <div className="business-details">
            <h1>Business and Employee management</h1>

            {isEditing ? (
                <div className="detail-item">
                    <h2>Edit Business Details</h2>
                    <label>Business Name:</label>
                    <input
                        type="text"
                        name="name"
                        value={editableMerchant.name || ''}
                        onChange={handleMerchantInputChange}
                    />
                    <label>Email:</label>
                    <input
                        type="email"
                        name="email"
                        value={editableMerchant.email || ''}
                        onChange={handleMerchantInputChange}
                    />
                    <label>Phone:</label>
                    <input
                        type="text"
                        name="phone"
                        value={editableMerchant.phone || ''}
                        onChange={handleMerchantInputChange}
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
                    <label>Country:</label>
                    <input
                        type="text"
                        name="country"
                        value={editableMerchant.address?.country || ''}
                        onChange={handleAddressChange}
                    />
                    <label>City:</label>
                    <input
                        type="text"
                        name="city"
                        value={editableMerchant.address?.city || ''}
                        onChange={handleAddressChange}
                    />
                    <label>Country code:</label>
                    <input
                        type="text"
                        name="countryCode"
                        value={editableMerchant.address?.countryCode || ''}
                        onChange={handleAddressChange}
                    />
                    <label>Zip code:</label>
                    <input
                        type="text"
                        name="zipCode"
                        value={editableMerchant.address?.zipCode || ''}
                        onChange={handleAddressChange}
                    />
                    <button onClick={handleUpdateMerchant}>Save Changes</button>
                    <button onClick={() => setIsEditing(false)}>Cancel</button>
                </div>
            ) : (
                <>
                    <div className="detail-item">
                        <h2><strong>Business Name:</strong> {merchant?.name || "Not available"}</h2>
                        <h3><strong>Email:</strong> {merchant?.email || "Not available"}</h3>
                        <h3><strong>Phone:</strong> {merchant?.phone || "Not available"}</h3>
                        <h3><strong>Address:</strong></h3>
                        <p><strong>Address 1:</strong> {merchant?.address?.address1 || "Not available"}</p>
                        <p><strong>Address 2:</strong> {merchant?.address?.address2 || "Not available"}</p>
                        <p><strong>City:</strong> {merchant?.address?.city || "Not available"}</p>
                        <p><strong>Country:</strong> {merchant?.address?.country || "Not available"}</p>
                        <p><strong>Country code:</strong> {merchant?.address?.countryCode || "Not available"}</p>
                        <p><strong>Zip code:</strong> {merchant?.address?.zipCode || "Not available"}</p>
                    </div>
                    <button onClick={() => setIsEditing(true)}>Edit Merchant Info</button>
                </>
            )}

            <h2>Employee Management</h2>
            <div className="employee-list">
                {employees.map((employee) => (
                    <div key={employee.username} className="employee-card">
                        {editingEmployee?.id === employee.id ? (
                            <>
                                <input
                                    type="text"
                                    value={editingEmployee.firstName}
                                    onChange={(e) =>
                                        setEditingEmployee({ ...editingEmployee, firstName: e.target.value })
                                    }
                                />
                                <input
                                    type="text"
                                    value={editingEmployee.lastName}
                                    onChange={(e) =>
                                        setEditingEmployee({ ...editingEmployee, lastName: e.target.value })
                                    }
                                />
                                <input
                                    type="text"
                                    value={editingEmployee.username}
                                    onChange={(e) =>
                                        setEditingEmployee({ ...editingEmployee, username: e.target.value })
                                    }
                                />
                                <select
                                    value={editingEmployee.employeeType}
                                    onChange={(e) =>
                                        setEditingEmployee({ ...editingEmployee, employeeType: e.target.value })
                                    }
                                >
                                    <option value="OWNER">Owner</option>
                                    <option value="REGULAR">Regular</option>
                                </select>
                                <button onClick={() => handleUpdateEmployee(employee.id)}>Save</button>
                                <button onClick={() => setEditingEmployee(null)}>Cancel</button>
                            </>
                        ) : (
                            <>
                                <h3>
                                    {employee.firstName} {employee.lastName} ({employee.employeeType})
                                </h3>
                                <p>Username: {employee.username}</p>
                                <button onClick={() => setEditingEmployee(employee)}>Edit</button>
                                <button onClick={() => handleDeleteEmployee(employee.id)}>Delete</button>
                            </>
                        )}
                    </div>
                ))}
            </div>

            <h3>Add New Employee</h3>
            <input
                type="text"
                placeholder="First Name"
                value={newEmployee.firstName}
                onChange={(e) => setNewEmployee({ ...newEmployee, firstName: e.target.value })}
            />
            <input
                type="text"
                placeholder="Last Name"
                value={newEmployee.lastName}
                onChange={(e) => setNewEmployee({ ...newEmployee, lastName: e.target.value })}
            />
            <input
                type="text"
                placeholder="Username"
                value={newEmployee.username}
                onChange={(e) => setNewEmployee({ ...newEmployee, username: e.target.value })}
            />
            <input
                type="password"
                placeholder="Password"
                value={newEmployee.password}
                onChange={(e) => setNewEmployee({ ...newEmployee, password: e.target.value })}
            />
            <select
                value={newEmployee.employeeType}
                onChange={(e) => setNewEmployee({ ...newEmployee, employeeType: e.target.value })}
            >
                <option value="OWNER">Owner</option>
                <option value="REGULAR">Regular</option>
            </select>
            <button onClick={handleAddEmployee}>Add Employee</button>

            <div>
                <button onClick={returnHome}>Back To Home</button>
            </div>
        </div>
    );
};
