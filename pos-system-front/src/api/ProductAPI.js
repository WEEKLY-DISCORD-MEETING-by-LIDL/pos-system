import axios from "axios";

export const fetchProducts = async (categoryId, createdAtMin, createdAtMax, limit, setProducts, token) => {

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

export const fetchVariants = async (productId, setVariants, token) => {
    try {
        const response = await axios.get(`http://localhost:8080/products/${productId}/variants`, {
            headers:{
                Authorization: `Bearer ${token}`
            }
        });
        console.log(response.data);
        setVariants(response.data);
    } catch (err) {
        console.log(err.message);
    }
};

export const createProduct = async (productData, token) => {
    try {
        const response = await axios.post('http://localhost:8080/products', productData, {
            headers:{
                Authorization: `Bearer ${token}`
            }
        });
        return response.data;
    } catch (err) {
        console.error(err.message);
        throw err;
    }
};

export const updateProduct = async (productId, productData, token) => {
    try {
        const response = await axios.put(`http://localhost:8080/products/${productId}`, productData, {
            headers:{
                Authorization: `Bearer ${token}`
            }
        });
        return response.data;
    } catch (err) {
        console.error(err.message);
        throw err;
    }
};

export const deleteProduct = async (productId, token) => {
    try {
        await axios.delete(`http://localhost:8080/products/${productId}`, {
            headers:{
                Authorization: `Bearer ${token}`
            }
        });
    } catch (err) {
        console.error(err.message);
        throw err;
    }
};

export const createVariant = async (productId, variantData, token) => {
    try {
        const response = await axios.post(`http://localhost:8080/products/${productId}/variants`, variantData, {
            headers:{
                Authorization: `Bearer ${token}`
            }
        });
        return response.data;
    } catch (err) {
        console.error(err.message);
        throw err;
    }
};

export const deleteVariant = async (variantId, token) => {
    try {
        await axios.delete(`http://localhost:8080/variants/${variantId}`, {
            headers:{
                Authorization: `Bearer ${token}`
            }
        });
    } catch (err) {
        console.error(err.message);
        throw err;
    }
};

export const updateVariant = async (variantId, variantData, token) => {
    try {
        const response = await axios.put(`http://localhost:8080/variants/${variantId}`, variantData, {
            headers:{
                Authorization: `Bearer ${token}`
            }
        });
        return response.data;
    } catch (err) {
        console.error(err.message);
        throw err;
    }
};
