import axios from "axios";

const token = localStorage.getItem("token");

export const fetchServices = async (categoryId, limit, setServices) => {
    try {
        console.log(token);

        const params = new URLSearchParams();
        
        if (categoryId != null) params.append('category', categoryId);
        if (limit != null) params.append('limit', limit);

        const requestString = `http://localhost:8080/services?${params.toString()}`;
        console.log("Request URL:", requestString);
        const response = await axios.get(requestString, {
            headers:{
                Authorization: `Bearer ${token}`,
            },
        });
        console.log(response.data);
        setServices(response.data);
    } catch (err) {
        console.error(err.message);
    }
};

export const test = async (categoryId, createdAtMin, createdAtMax, limit, setProducts) => {
    try {
        const params = new URLSearchParams();
        
        if (categoryId != null) params.append('categoryId', categoryId);
        if (createdAtMin != null) params.append('createdAtMin', createdAtMin);
        if (createdAtMax != null) params.append('createdAtMax', createdAtMax);
        if (limit != null) params.append('limit', limit);

        const requestString = `http://localhost:8080/products?${params.toString()}`;
        const response = await axios.get(requestString, {
            headers:{
                Authorization: `Bearer ${token}`
            }
        });
        console.log(response.data);
        setProducts(response.data);
    } catch (err) {
        console.error(err.message);
    }
};

export const deleteService = async (service) => {
    try {
        const serviceId = service.id
        const requestString = `http://localhost:8080/services/${serviceId}`;
        const response = await axios.delete(requestString, {
            headers:{
                Authorization: `Bearer ${token}`
            }
        });
        console.log(response.data);
    } catch (err) {
        console.error(err.message);
    }
}

export const createService = async (serviceData) => {
    try {
        const requestString = `http://localhost:8080/services`;
        const response = await axios.post(requestString, serviceData, {
            headers:{
                Authorization: `Bearer ${token}`
            }
        });
        console.log(response.data)
    } catch (error) {
        console.error(error.message);
    }
}

export const updateService = async (service, serviceData) => {
    try {
        const requestString = `http://localhost:8080/services/${service.id}`;
        const response = await axios.put(requestString, serviceData, {
            headers:{
                Authorization: `Bearer ${token}`
            }
        });
        console.log(response.data)
    } catch (error) {
        console.error(error.message);
    }
}

export const fetchService = async (serviceId, setSelectedService) => {
    try {
        const requestString = `http://localhost:8080/services/${serviceId}`;
        const response = await axios.get(requestString, {
            headers:{
                Authorization: `Bearer ${token}`
            }
        });
        console.log(response.data)
        setSelectedService(response.data)
    } catch (error) {
        console.error(error.message);
    }
}