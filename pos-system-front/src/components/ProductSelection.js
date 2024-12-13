import {ProductCard} from "./ProductCard";
import {ProductSelectionStyle} from "../styles/ProductSelectionStyle";

export const ProductSelection = (props) => {





    return (
        <ul style={ProductSelectionStyle.list}>
            {props.products.map((product) => (
                <li style={ProductSelectionStyle.cardListItem}>
                    <ProductCard product={product} items={props.items} setItems={props.setItems}/>
                </li>
            ))}
        </ul>
    );
}