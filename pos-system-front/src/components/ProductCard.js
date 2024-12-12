import {useEffect, useState} from "react";
import {fetchVariants} from "../api/ProductAPI";

export const ProductCard = (props) => {

    // const [variants, setVariants] = useState([]);
    //
    // useEffect(() => {
    //     fetchVariants(props.product.id, setVariants)
    // }, []);

    return (
        <div>
            <h4>{props.product.title}</h4>
            <p>Price: {props.product.price}</p>
            <button>Add</button>
            <select>
                {props.product.variants.map((variant) => (
                    <option>{variant.title}</option>
                ))}
            </select>
        </div>
    );
}