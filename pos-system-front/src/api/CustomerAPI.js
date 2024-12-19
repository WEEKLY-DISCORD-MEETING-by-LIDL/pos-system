import axios from "axios";

const token = localStorage.getItem("token");

export const CreateCustomer = async (firstName, lastName, phoneNumber) => {
    const customerData = {
        id: null,
        firstName: firstName,
        lastName: lastName,
        phone: phoneNumber
    }

    try {
        const requestString = `http://localhost:8080/customers`;
    
        const response = await axios.post(requestString, customerData, {
            headers:{
                Authorization: `Bearer ${token}`,
            },
        });
        console.log(response.data);
        // setSelectedCustomer(response.data);
        return response.data
    } catch (error) {
        console.error(error.message)
    }
}

// export const FetchCustomersByName = async (firstName) => {
//     try {
//         const requestString = `http://localhost:8080/customers/${firstName}`;
    
//         const response = await axios.post(requestString,{
//             headers:{
//                 Authorization: `Bearer ${token}`,
//             },
//         });
//         console.log(response.data);
//     } catch (error) {
//         console.error(error.message)
//     }
// }

export const FetchCustomers = async () => {
    try {
        const requestString = 'http://localhost:8080/customers';

        const respone = await axios.get(requestString, {
            headers:{
                Authorization: `Bearer ${token}`,
            }
        })

        return respone.data;
    } catch (error) {
        console.error(error.message);
    }
}