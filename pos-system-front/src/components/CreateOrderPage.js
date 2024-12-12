import React, {useEffect, useState} from "react";
import {ProductSelection} from "./ProductSelection";
import {fetchProducts} from "../api/ProductAPI";

export const CreateOrderPage = () => {

    const [items, setItems] = useState([]);
    const [products, setProducts] = useState([]);

    useEffect(() => {
        fetchProducts(null, null, null, null, setProducts);
    }, []);




    return (
        <div>
            <header>
                <h1>
                    Create order
                </h1>
            </header>

            <ul>
                {items.map((item) => (
                    <li>
                        woo
                    </li>
                ))}
            </ul>

            <ProductSelection products={products} />
        </div>
    );
}