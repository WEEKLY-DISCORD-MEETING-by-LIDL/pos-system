import {useEffect, useState} from "react";
import {fetchVariants} from "../api/ProductAPI";
import {ProductCardStyle} from "../styles/ProductCardStyle";
import {fetchTax} from "../api/TaxAPI";

export const ProductCard = (props) => {

    const token = localStorage.getItem("token");
    const [selectedVariant, setSelectedVariants] = useState(0);
    const [variants, setVariants] = useState([]);
    const [tax, setTax] = useState(null)
    
    useEffect(() => {
        fetchVariants(props.product.id, setVariants, token);
        fetchTax(props.product.taxId, setTax, token);
    }, []);

    const currentVariant = variants[selectedVariant];

    const onAdd = () => {

        for(let i = 0; i<props.items.length; ++i) {
            if (props.items[i].productVariant === variants[selectedVariant]) {
                props.items[i].quantity++;
                props.setItems([...props.items]);
                return;
            }
        }

        const newItem = {
            product: props.product,
            productVariant: variants[selectedVariant],
            tax: tax,
            quantity: 1
        }

        const updatedItems = [...props.items, newItem];

        props.setItems(updatedItems);
    }

    let index = 0;

    return (
        <div style={ProductCardStyle.card}>
            <h4>{props.product.title}</h4>
            <p>Price: {props.product.price}</p>
            <button onClick={onAdd}>Add</button>
            <select value={selectedVariant} onChange={e => setSelectedVariants(Number(e.target.value))}>
                {variants.map((variant) => (
                    <option value={index++} key={variant.id}>{variant.title}</option>
                ))}
            </select>
            {currentVariant && (
                <>
                    <p>Variant: {currentVariant.title}</p>
                    <p>Additional price: {currentVariant.additionalPrice}</p>
                </>
            )}
        </div>
    );
}