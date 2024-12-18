import React, { useState, useEffect } from 'react';
import { discountStyle as DiscountStyle } from "../styles/DiscountStyle";
import {
    fetchDiscounts,
    createDiscount,
    updateDiscount,
    deleteDiscount,
    fetchOrderDiscounts,
    createOrderDiscount,
    deleteOrderDiscount,
    updateOrderDicount
} from '../api/DiscountAPI';

const Discount = () => {
    const [discounts, setDiscounts] = useState([]);
    const [orderDiscounts, setOrderDiscounts] = useState([]);
    const [newDiscount, setNewDiscount] = useState({ title: '', percentage: '', expiresOn: '' });
    const [newOrderDiscount, setNewOrderDiscount] = useState({ title: '', percentage: '', expiresOn: '' });

    const [editingDiscountId, setEditingDiscountId] = useState(null);
    const [editingDiscount, setEditingDiscount] = useState({ title: '', percentage: '', expiresOn: '' });
    const [editingOrderId, setEditingOrderId] = useState(null);
    const [editingOrderDiscount, setEditingOrderDiscount] = useState({ title: '', percentage: '', expiresOn: '' });

    useEffect(() => {
        loadDiscounts();
    }, []);

    const loadDiscounts = async () => {
        try {
            await fetchDiscounts(setDiscounts);
            await fetchOrderDiscounts(setOrderDiscounts);
        } catch (error) {
            console.error('Error fetching discounts:', error.message);
        }
    };

    // Function to format date without the T
    const formatDate = (date) => {
        if (!date) return '';
        const formattedDate = new Date(date).toLocaleString([], { 
            weekday: 'short', year: 'numeric', month: 'short', day: 'numeric', hour: 'numeric', minute: 'numeric', hour12: true 
        });
        return formattedDate;
    };

    // Handlers for product/service discount
    const handleCreateDiscount = async () => {
        try {
            const formattedDiscount = { 
                ...newDiscount, 
                percentage: newDiscount.percentage / 100 // Convert to decimal
            };
            await createDiscount(formattedDiscount);
            setNewDiscount({ title: '', percentage: '', expiresOn: '' });
            loadDiscounts();
        } catch (error) {
            console.error('Error creating discount:', error.message);
        }
    };
    
    const handleUpdateDiscount = async (id) => {
        try {
            const formattedDiscount = { 
                ...editingDiscount, 
                percentage: editingDiscount.percentage / 100 // Convert to decimal
            };
            await updateDiscount(id, formattedDiscount);
            setEditingDiscountId(null); // Exit editing mode
            loadDiscounts();
        } catch (error) {
            console.error('Error updating discount:', error.message);
        }
    };

    const handleDeleteDiscount = async (id) => {
        try {
            await deleteDiscount(id);
            loadDiscounts();
        } catch (error) {
            console.error('Error deleting discount:', error.message);
        }
    };

    // Handlers for order discounts
    const handleCreateOrderDiscount = async () => {
        try {
            const formattedOrderDiscount = { 
                ...newOrderDiscount, 
                percentage: newOrderDiscount.percentage / 100 // Convert to decimal
            };
            await createOrderDiscount(formattedOrderDiscount);
            setNewOrderDiscount({ title: '', percentage: '', expiresOn: '' });
            loadDiscounts();
        } catch (error) {
            console.error('Error creating order discount:', error.message);
        }
    };
    
    const handleUpdateOrderDiscount = async (id) => {
        try {
            const formattedOrderDiscount = { 
                ...editingOrderDiscount, 
                percentage: editingOrderDiscount.percentage / 100 // Convert to decimal
            };
            await updateOrderDicount(id, formattedOrderDiscount);
            setEditingOrderId(null); // Exit editing mode
            loadDiscounts();
        } catch (error) {
            console.error('Error updating order discount:', error.message);
        }
    };

    const handleDeleteOrderDiscount = async (id) => {
        try {
            await deleteOrderDiscount(id);
            loadDiscounts();
        } catch (error) {
            console.error('Error deleting order discount:', error.message);
        }
    };

    return (
        <div style={DiscountStyle.container}>
            <h1 style={DiscountStyle.header}>Discount Management</h1>
            <div style={DiscountStyle.splitContainer}>
                {/* Product/Service Discounts */}
                <div style={DiscountStyle.section}>
                    <h2>Discounts</h2>
                    <ul>
                        {discounts.map((discount) => (
                            <li key={discount.id}>
                                {editingDiscountId === discount.id ? (
                                    <>
                                        <input
                                            type="text"
                                            value={editingDiscount.title}
                                            onChange={(e) =>
                                                setEditingDiscount({ ...editingDiscount, title: e.target.value })
                                            }
                                            placeholder="Title"
                                            style={DiscountStyle.input}
                                        />
                                        <input
                                            type="number"
                                            value={editingDiscount.percentage}
                                            onChange={(e) =>
                                                setEditingDiscount({ ...editingDiscount, percentage: e.target.value })
                                            }
                                            placeholder="Percentage"
                                            style={DiscountStyle.input}
                                        />
                                        <input
                                            type="datetime-local"
                                            value={editingDiscount.expiresOn}
                                            onChange={(e) =>
                                                setEditingDiscount({ ...editingDiscount, expiresOn: e.target.value })
                                            }
                                            placeholder="Expires On"
                                            style={DiscountStyle.input}
                                        />
                                        <button onClick={() => handleUpdateDiscount(discount.id)} style={DiscountStyle.button}>
                                            Save
                                        </button>
                                        <button onClick={() => setEditingDiscountId(null)} style={DiscountStyle.button}>
                                            Cancel
                                        </button>
                                    </>
                                ) : (
                                    <>
                                        <strong>Title:</strong> {discount.title} <br />
                                        <strong>Percentage:</strong> {discount.percentage}% <br />
                                        <strong>Expires On:</strong> {formatDate(discount.expiresOn)} <br />
                                        <button
                                            onClick={() => {
                                                setEditingDiscountId(discount.id);
                                                setEditingDiscount(discount);
                                            }}
                                            style={DiscountStyle.button}
                                        >
                                            Update
                                        </button>
                                        <button onClick={() => handleDeleteDiscount(discount.id)} style={DiscountStyle.button}>
                                            Delete
                                        </button>
                                    </>
                                )}
                            </li>
                        ))}
                    </ul>
                    <div>
                        <h3>Add Discount</h3>
                        <input
                            type="text"
                            placeholder="Title"
                            value={newDiscount.title}
                            onChange={(e) => setNewDiscount({ ...newDiscount, title: e.target.value })}
                            style={DiscountStyle.input}
                        />
                        <input
                            type="number"
                            placeholder="Percentage"
                            value={newDiscount.percentage}
                            onChange={(e) => setNewDiscount({ ...newDiscount, percentage: e.target.value })}
                            style={DiscountStyle.input}
                        />
                        <input
                            type="datetime-local"
                            placeholder="Expires On"
                            value={newDiscount.expiresOn}
                            onChange={(e) => setNewDiscount({ ...newDiscount, expiresOn: e.target.value })}
                            style={DiscountStyle.input}
                        />
                        <button onClick={handleCreateDiscount} style={DiscountStyle.button}>
                            Add Discount
                        </button>
                    </div>
                </div>

                {/* Order Discounts */}
                <div style={DiscountStyle.section}>
                    <h2>Order Discounts</h2>
                    <ul>
                        {orderDiscounts.map((discount) => (
                            <li key={discount.id}>
                                {editingOrderId === discount.id ? (
                                    <>
                                        <input
                                            type="text"
                                            value={editingOrderDiscount.title}
                                            onChange={(e) =>
                                                setEditingOrderDiscount({ ...editingOrderDiscount, title: e.target.value })
                                            }
                                            placeholder="Title"
                                            style={DiscountStyle.input}
                                        />
                                        <input
                                            type="number"
                                            value={editingOrderDiscount.percentage}
                                            onChange={(e) =>
                                                setEditingOrderDiscount({ ...editingOrderDiscount, percentage: e.target.value })
                                            }
                                            placeholder="Percentage"
                                            style={DiscountStyle.input}
                                        />
                                        <input
                                            type="datetime-local"
                                            value={editingOrderDiscount.expiresOn}
                                            onChange={(e) =>
                                                setEditingOrderDiscount({ ...editingOrderDiscount, expiresOn: e.target.value })
                                            }
                                            placeholder="Expires On"
                                            style={DiscountStyle.input}
                                        />
                                        <button onClick={() => handleUpdateOrderDiscount(discount.id)} style={DiscountStyle.button}>
                                            Save
                                        </button>
                                        <button onClick={() => setEditingOrderId(null)} style={DiscountStyle.button}>
                                            Cancel
                                        </button>
                                    </>
                                ) : (
                                    <>
                                        <strong>Title:</strong> {discount.title} <br />
                                        <strong>Percentage:</strong> {discount.percentage}% <br />
                                        <strong>Expires On:</strong> {formatDate(discount.expiresOn)} <br />
                                        <button
                                            onClick={() => {
                                                setEditingOrderId(discount.id);
                                                setEditingOrderDiscount(discount);
                                            }}
                                            style={DiscountStyle.button}
                                        >
                                            Update
                                        </button>
                                        <button onClick={() => handleDeleteOrderDiscount(discount.id)} style={DiscountStyle.button}>
                                            Delete
                                        </button>
                                    </>
                                )}
                            </li>
                        ))}
                    </ul>
                    <div>
                        <h3>Add Order Discount</h3>
                        <input
                            type="text"
                            placeholder="Title"
                            value={newOrderDiscount.title}
                            onChange={(e) => setNewOrderDiscount({ ...newOrderDiscount, title: e.target.value })}
                            style={DiscountStyle.input}
                        />
                        <input
                            type="number"
                            placeholder="Percentage"
                            value={newOrderDiscount.percentage}
                            onChange={(e) => setNewOrderDiscount({ ...newOrderDiscount, percentage: e.target.value })}
                            style={DiscountStyle.input}
                        />
                        <input
                            type="datetime-local"
                            value={newOrderDiscount.expiresOn}
                            onChange={(e) =>
                                setNewOrderDiscount({ ...newOrderDiscount, expiresOn: e.target.value })
                            }
                            placeholder="Expires On"
                            style={DiscountStyle.input}
                        />
                        <button onClick={handleCreateOrderDiscount} style={DiscountStyle.button}>
                            Add Order Discount
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Discount;
