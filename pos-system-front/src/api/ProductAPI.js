import axios from "axios";

export const fetchProducts = async (categoryId, createdAtMin, createdAtMax, limit, setProducts) => {
    try {

        let requestString = 'http://localhost:8080/products';

        //this part is sus
        if(categoryId != null) {
            requestString += `?categoryId=${categoryId}`;
        }
        if(createdAtMin != null) {
            requestString += `?createdAtMin=${createdAtMin}`;
        }
        if(createdAtMax != null) {
            requestString += `?createdAtMax=${createdAtMax}`;
        }
        if(limit != null) {
            requestString += `?limit=${limit}`;
        }

        const response = await axios.get(requestString);

        setProducts(response.data);
    } catch (err) {
        console.log(err.message);
    }
}


export const fetchVariants = async (productId, setVariants) => {
    try {
        const response = await axios.get(`http://localhost:8080/products/${productId}/variants`)

        setVariants(response.data);
    } catch (err) {
        console.log(err.message);
    }
}