import {ProductCard} from "./ProductCard";

export const ProductSelection = (props) => {





    return (
        <div>
            <ul>
                {props.products.map((product) => (
                    <li>
                        <ProductCard product={product}/>
                    </li>
                ))}
            </ul>
        </div>
    );
}