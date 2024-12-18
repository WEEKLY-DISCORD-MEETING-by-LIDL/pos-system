import React, {useEffect, useState} from "react";
import {ProductSelection} from "./ProductSelection";
import {fetchProducts} from "../api/ProductAPI";
import {createOrderStyle as CreateOrderStyle} from "../styles/CreateOrderStyle";
import {createOrder} from "../api/OrderAPI";

export const CreateOrderPage = () => {

    const token = localStorage.getItem("token");
    const [items, setItems] = useState([]);
    const [products, setProducts] = useState([]);
    const [discountId, setDiscountId] = useState(null);

    useEffect(() => {
        fetchProducts(null, null, null, null, setProducts, token);
    }, []);

    const onPlus = (item) => {
        item.quantity++;

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
        let orderItems = [];

        for(let i = 0;i<items.length;++i) {
            let orderItem = {
                id: 0,
                productVariantId: items[i].productVariant.id,
                quantity: items[i].quantity
            }

            orderItems.push(orderItem);
        }

        createOrder(discountId, orderItems, token);
    }

    const getTotalAmount = () => {
        let sum = 0;
        items.forEach((item) => {
            sum += item.quantity * (item.product.price + item.productVariant.additionalPrice) * (item.tax != null ? item.tax.percentage + 1 : 1);
        });

        return sum;
    }



    return (
        <div style={CreateOrderStyle.page}>
            <div style={CreateOrderStyle.itemListSidebar}>
                <ul style={CreateOrderStyle.itemList}>
                    {items.map((item) => (
                        <li>
                            <div>
                                <p>{item.product.title} {item.productVariant.title} x{item.quantity}</p>
                                <p>Price: {item.quantity * (item.product.price + item.productVariant.additionalPrice) * (item.tax != null ? item.tax.percentage + 1 : 1)}</p>
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

                <h2>Total amount: {getTotalAmount()}</h2>
                <button style={CreateOrderStyle.createOrderButton} onClick={onCreate}>Create</button>
            </div>

            <div style={CreateOrderStyle.selectionBox}>
                <ProductSelection products={products} items={items} setItems={setItems}/>
            </div>


        </div>
    );
}