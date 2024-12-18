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

export const fetchVariantsByOrder = async (orderId, setVariants, token) => {
    try {
        const response = await axios.get(`http://localhost:8080/variants/order/${orderId}`, {
            headers:{
                Authorization: `Bearer ${token}`
            }
        });

        setVariants(response.data);
    } catch (err) {
        console.log(err.message);
    }
};

export const fetchProductsByOrder = async (orderId, setProducts, token) => {
    try {
        const response = await axios.get(`http://localhost:8080/products/order/${orderId}`, {
            headers:{
                Authorization: `Bearer ${token}`
            }
        });

        setProducts(response.data);
    } catch (err) {
        console.log(err.message);
    }
};

export const createProduct = async (productData) => {
    try {
        const response = await axios.post('http://localhost:8080/products', productData);
        return response.data;
    } catch (err) {
        console.error(err.message);
        throw err;
    }
};

export const updateProduct = async (productId, productData) => {
    try {
        const response = await axios.put(`http://localhost:8080/products/${productId}`, productData);
        return response.data;
    } catch (err) {
        console.error(err.message);
        throw err;
    }
};

export const deleteProduct = async (productId) => {
    try {
        await axios.delete(`http://localhost:8080/products/${productId}`);
    } catch (err) {
        console.error(err.message);
        throw err;
    }
};

export const createVariant = async (productId, variantData) => {
    try {
        const response = await axios.post(`http://localhost:8080/products/${productId}/variants`, variantData);
        return response.data;
    } catch (err) {
        console.error(err.message);
        throw err;
    }
};

export const deleteVariant = async (variantId) => {
    try {
        await axios.delete(`http://localhost:8080/variants/${variantId}`);
    } catch (err) {
        console.error(err.message);
        throw err;
    }
};

export const updateVariant = async (variantId, variantData) => {
    try {
        const response = await axios.put(`http://localhost:8080/variants/${variantId}`, variantData);
        return response.data;
    } catch (err) {
        console.error(err.message);
        throw err;
    }
};
