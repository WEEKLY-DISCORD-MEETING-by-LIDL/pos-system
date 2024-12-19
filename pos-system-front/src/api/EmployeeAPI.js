import axios from "axios";

export const fetchEmployees = async(employeeType, limit, setEmployees) => {
    try {
        const token = localStorage.getItem("token");
        const params = new URLSearchParams();
        if(employeeType != null) params.append('type', employeeType);
        if(limit != null) params.append('limit', limit);
        const requestString = `http://localhost:8080/employees?${params.toString()}`;
               
        const response = await axios.get(requestString, {
            headers: {
                Authorization: `Bearer ${token}`, 
            },
        });
        console.log(response.data);
        setEmployees(response.data);
    } catch (error) {
        console.log(error.message);
    }
}

export const createEmployee = async (employeeData) => {
    try {
        const token = localStorage.getItem("token");
        const requestString = `http://localhost:8080/employees`;
        
        const response = await axios.post(requestString, employeeData, {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });

        console.log("Employee created:", response.data);
    } catch (error) {
        console.log("Error creating employee:", error.message);
    }
};

export const updateEmployee = async (employeeId, updatedData) => {
    try {
        const token = localStorage.getItem("token");
        const requestString = `http://localhost:8080/employees/${employeeId}`;
        
        const response = await axios.put(requestString, updatedData, {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });

        console.log("Employee updated:", response.data);
    } catch (error) {
        console.log("Error updating employee:", error.message);
    }
};

export const deleteEmployee = async (employeeId) => {
    try {
        const token = localStorage.getItem("token");
        const requestString = `http://localhost:8080/employees/${employeeId}`;
        
        const response = await axios.delete(requestString, {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });

        console.log("Employee deleted:", response.data);
    } catch (error) {
        console.log("Error deleting employee:", error.message);
    }
};

