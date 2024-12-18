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
