import React, {useEffect, useState} from "react";
import {ProductSelection} from "./ProductSelection";
import {fetchProducts} from "../api/ProductAPI";
import {createOrderStyle as CreateOrderStyle} from "../styles/CreateOrderStyle";

export const CreateOrderPage = () => {

    const [items, setItems] = useState([]);
    const [products, setProducts] = useState([]);

    useEffect(() => {
        fetchProducts(null, null, null, null, setProducts);
    }, []);

    const onPlus = (item) => {
        item.quantity++;

        // let index = items.indexOf(item);
        // if (index > -1) {
        //     items[index].quantity++;
        // }

        setItems([...items]);
    }

    const onMinus = (item) => {
        if(item.quantity > 1) {
            item.quantity--;

            setItems([...items]);
        }
        else {
            onRemove(item);
        }
    }

    const onRemove = (item) => {
        let index = items.indexOf(item);
        if (index > -1) {
            items.splice(index, 1);
        }

        setItems([...items]);
    }

    const onCreate = () => {

    }


    return (
        <div style={CreateOrderStyle.page}>
            <div style={CreateOrderStyle.itemListSidebar}>
                <ul style={CreateOrderStyle.itemList}>
                    {items.map((item) => (
                        <li>
                            <div>
                                <p>{item.product.title} {item.productVariant.title} x{item.quantity}</p>
                                <p>Price: {item.quantity * (item.product.price + item.productVariant.additionalPrice)}</p>
                                <button onClick={() => {
                                    onMinus(item)
                                }}>-
                                </button>
                                <button onClick={() => {
                                    onPlus(item)
                                }}>+
                                </button>
                                <button onClick={() => {
                                    onRemove(item)
                                }}>Remove
                                </button>
                            </div>
                        </li>
                    ))}
                </ul>

                <button style={CreateOrderStyle.createOrderButton}>Create</button>
            </div>

            <div style={CreateOrderStyle.selectionBox}>
                <ProductSelection products={products} items={items} setItems={setItems}/>
            </div>


        </div>
    );
}