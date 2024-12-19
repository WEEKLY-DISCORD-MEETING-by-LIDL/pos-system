import React, {useEffect, useState} from "react";
import {ProductSelection} from "./ProductSelection";
import {fetchProducts} from "../api/ProductAPI";
import {createOrderStyle as CreateOrderStyle} from "../styles/CreateOrderStyle";
import {createOrder} from "../api/OrderAPI";
import {fetchOrderDiscounts} from "../api/DiscountAPI";

export const CreateOrderPage = () => {

    const token = localStorage.getItem("token");
    const [items, setItems] = useState([]);
    const [products, setProducts] = useState([]);
    const [discounts, setDiscounts] = useState([]);
    const [discountId, setDiscountId] = useState(0);

    useEffect(() => {
        fetchProducts(null, null, null, null, setProducts, token);
        fetchOrderDiscounts(setDiscounts);
    }, []);

    const getItemPrice = (item) => {
        return (item.quantity * (((item.product.price + item.productVariant.additionalPrice) * (item.discount != null ? 1 - item.discount.percentage : 1)) * (item.tax != null ? item.tax.percentage + 1 : 1))).toFixed(2)
    }

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

        createOrder((discountId === 0 ? null : discountId), orderItems, token);
    }

    const getTotalAmount = () => {
        let sum = 0;
        items.forEach((item) => {
            sum += Number(getItemPrice(item));
        });

        if(discountId != null) {
            for(let i = 0 ;i<discounts.length;++i) {
                if(discounts[i].id === discountId) {
                    sum *= (1 - discounts[i].percentage/100);
                    break;
                }
            }
        }

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
                                <p>Price: {getItemPrice(item)}</p>
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

                <h3>Discount:</h3>
                <select onChange={e => setDiscountId(Number(e.target.value))}>
                    <option value={0}>None</option>
                    {discounts && discounts.map((discount) => (
                        <option value={discount.id}>{discount.title}</option>
                    ))}
                </select>

                <h2>Total amount: {getTotalAmount()} €</h2>
                <button style={CreateOrderStyle.createOrderButton} onClick={onCreate}>Create</button>
            </div>

            <div style={CreateOrderStyle.selectionBox}>
                <ProductSelection products={products} items={items} setItems={setItems}/>
            </div>


        </div>
    );
}