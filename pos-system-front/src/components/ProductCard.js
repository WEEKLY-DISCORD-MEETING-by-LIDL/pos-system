import {useEffect, useState} from "react";
import {fetchVariants} from "../api/ProductAPI";
import {ProductCardStyle} from "../styles/ProductCardStyle";

export const ProductCard = (props) => {

    const [selectedVariant, setSelectedVariants] = useState("0");

    // useEffect(() => {
    //     fetchVariants(props.product.id, setVariants)
    // }, []);

    const onAdd = () =>{

        for(let i = 0; i<props.items.length; ++i) {
            if (props.items[i].productVariant === props.product.variants[selectedVariant]) {
                props.items[i].quantity++;
                props.setItems([...props.items]);
                return;
            }
        }

        const newItem = {
            product: props.product,
            productVariant: props.product.variants[selectedVariant],
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
            <select value={selectedVariant} onChange={e => setSelectedVariants(e.target.value)}>
                {props.product.variants.map((variant) => (
                    <option value={index++}>{variant.title}</option>
                ))}
            </select>
            <p>Variant: {props.product.variants[selectedVariant].title}</p>
            <p>Additional price: {props.product.variants[selectedVariant].additionalPrice}</p>
        </div>
    );
}