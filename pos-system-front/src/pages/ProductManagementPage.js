import React, { useState, useEffect } from 'react';
import {
    fetchProducts,
    createProduct,
    updateProduct,
    deleteProduct,
    fetchVariants,
    createVariant,
    deleteVariant,
    updateVariant
} from '../api/ProductAPI';
import { fetchCategories } from '../api/CategoryAPI';
import { fetchTaxes } from '../api/TaxAPI';
import { fetchDiscounts } from '../api/DiscountAPI';

const ProductManagementPage = () => {

    const token = localStorage.getItem("token");
    const [products, setProducts] = useState([]);
    const [categories, setCategories] = useState([]);
    const [taxes, setTaxes] = useState([]);
    const [discounts, setDiscounts] = useState([]);
    const [selectedProduct, setSelectedProduct] = useState(null);
    const [variants, setVariants] = useState([]);
    const [isAddProductModalOpen, setIsAddProductModalOpen] = useState(false);
    const [isAddVariantModalOpen, setIsAddVariantModalOpen] = useState(false);
    const [isEditProductModalOpen, setIsEditProductModalOpen] = useState(false);
    const [isEditVariantModalOpen, setIsEditVariantModalOpen] = useState(false);
    const [newProduct, setNewProduct] = useState({
        title: '',
        price: 0,
        weight: 0,
        weightUnit: 'kg',
        categoryId: 1,
        taxId: null,
        discountId: null
    });
    const [newVariant, setNewVariant] = useState({
        title: '',
        additionalPrice: 0
    });
    const [editingProduct, setEditingProduct] = useState(null);
    const [editingVariant, setEditingVariant] = useState(null);

    useEffect(() => {
        loadProducts();
        loadCategories();
        loadTaxes();
        loadDiscounts();
    }, []);

    const loadProducts = async () => {
        await fetchProducts(null, null, null, null, setProducts, token);
    };

    const loadCategories = async () => {
        await fetchCategories(setCategories, token);
    };

    const loadTaxes = async () => {
        await fetchTaxes(null, setTaxes, token);
    };

    const loadDiscounts = async () => {
        await fetchDiscounts(setDiscounts);
    };

    const loadVariants = async (productId) => {
        await fetchVariants(productId, setVariants, token);
    };

    const handleProductSelect = async (product) => {
        setSelectedProduct(product);
        await loadVariants(product.id);
    };

    const handleAddProduct = async () => {
        try {
            await createProduct(newProduct, token);
            setIsAddProductModalOpen(false);
            setNewProduct({ title: '', price: 0, weight: 0, weightUnit: 'kg', categoryId: 1, taxId: null, discountId: null });
            await loadProducts();
        } catch (error) {
            console.error('Error adding product:', error);
        }
    };

    const handleAddVariant = async () => {
        if (!selectedProduct) return;
        try {
            await createVariant(selectedProduct.id, newVariant, token);
            setIsAddVariantModalOpen(false);
            setNewVariant({ title: '', additionalPrice: 0 });
            await loadVariants(selectedProduct.id);
        } catch (error) {
            console.error('Error adding variant:', error);
        }
    };

    const handleDeleteProduct = async (productId) => {
        try {
            await deleteProduct(productId, token);
            await loadProducts();
            if (selectedProduct?.id === productId) {
                setSelectedProduct(null);
                setVariants([]);
            }
        } catch (error) {
            console.error('Error deleting product:', error);
        }
    };

    const handleDeleteVariant = async (variantId) => {
        try {
            await deleteVariant(variantId, token);
            if (selectedProduct) {
                await loadVariants(selectedProduct.id);
            }
        } catch (error) {
            console.error('Error deleting variant:', error);
        }
    };

    const handleEditProduct = async () => {
        if (!editingProduct) return;
        try {
            await updateProduct(editingProduct.id, editingProduct, token);
            setIsEditProductModalOpen(false);
            setEditingProduct(null);
            await loadProducts();
        } catch (error) {
            console.error('Error updating product:', error);
        }
    };

    const handleEditVariant = async () => {
        if (!editingVariant) return;
        try {
            await updateVariant(editingVariant.id, editingVariant, token);
            setIsEditVariantModalOpen(false);
            setEditingVariant(null);
            if (selectedProduct) {
                await loadVariants(selectedProduct.id);
            }
        } catch (error) {
            console.error('Error updating variant:', error);
        }
    };

    return (
        <div style={{ padding: '20px' }}>
            <h2>Product Management</h2>

            {/* Product List Section */}
            <div style={{ display: 'flex', gap: '20px' }}>
                <div style={{ flex: 1 }}>
                    <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '10px' }}>
                        <h3>Products</h3>
                        <button onClick={() => setIsAddProductModalOpen(true)}>Add Product</button>
                    </div>
                    <div style={{ border: '1px solid #ccc', borderRadius: '4px', padding: '10px' }}>
                        {products.map(product => (
                            <div
                                key={product.id}
                                style={{
                                    padding: '10px',
                                    border: '1px solid #eee',
                                    marginBottom: '5px',
                                    cursor: 'pointer',
                                    backgroundColor: selectedProduct?.id === product.id ? '#f0f0f0' : 'white'
                                }}
                                onClick={() => handleProductSelect(product)}
                            >
                                <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                                    <span>{product.title}</span>
                                    <div>
                                        <button onClick={() => {
                                            setEditingProduct(product);
                                            setIsEditProductModalOpen(true);
                                        }}>Edit</button>
                                        <button onClick={(e) => {
                                            e.stopPropagation();
                                            handleDeleteProduct(product.id);
                                        }}>Delete</button>
                                    </div>
                                </div>
                                <div>Price: €{product.price}</div>
                            </div>
                        ))}
                    </div>
                </div>

                {/* Variants Section */}
                <div style={{ flex: 1 }}>
                    <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '10px' }}>
                        <h3>Variants</h3>
                        {selectedProduct && (
                            <button onClick={() => setIsAddVariantModalOpen(true)}>Add Variant</button>
                        )}
                    </div>
                    <div style={{ border: '1px solid #ccc', borderRadius: '4px', padding: '10px' }}>
                        {selectedProduct ? (
                            variants.map(variant => (
                                <div
                                    key={variant.id}
                                    style={{
                                        padding: '10px',
                                        border: '1px solid #eee',
                                        marginBottom: '5px'
                                    }}
                                >
                                    <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                                        <span>{variant.title}</span>
                                        <div>
                                            <button onClick={() => {
                                                setEditingVariant(variant);
                                                setIsEditVariantModalOpen(true);
                                            }}>Edit</button>
                                            <button onClick={() => handleDeleteVariant(variant.id)}>Delete</button>
                                        </div>
                                    </div>
                                    <div>Additional Price: €{variant.additionalPrice}</div>
                                </div>
                            ))
                        ) : (
                            <p>Select a product to view variants</p>
                        )}
                    </div>
                </div>
            </div>

            {/* Add Product Modal */}
            {isAddProductModalOpen && (
                <div style={{
                    position: 'fixed',
                    top: 0,
                    left: 0,
                    right: 0,
                    bottom: 0,
                    backgroundColor: 'rgba(0,0,0,0.5)',
                    display: 'flex',
                    justifyContent: 'center',
                    alignItems: 'center'
                }}>
                    <div style={{ backgroundColor: 'white', padding: '20px', borderRadius: '4px' }}>
                        <h3>Add New Product</h3>
                        <div style={{ marginBottom: '10px' }}>
                            <label style={{ display: 'block', marginBottom: '5px' }}>Title:</label>
                            <input
                                type="text"
                                placeholder="Enter product title"
                                value={newProduct.title}
                                onChange={e => setNewProduct({...newProduct, title: e.target.value})}
                                style={{ width: '100%', padding: '5px' }}
                            />
                        </div>
                        <div style={{ marginBottom: '10px' }}>
                            <label style={{ display: 'block', marginBottom: '5px' }}>Price:</label>
                            <input
                                type="number"
                                placeholder="Enter price"
                                value={newProduct.price}
                                onChange={e => setNewProduct({...newProduct, price: parseFloat(e.target.value)})}
                                style={{ width: '100%', padding: '5px' }}
                            />
                        </div>
                        <div style={{ marginBottom: '10px' }}>
                            <label style={{ display: 'block', marginBottom: '5px' }}>Category:</label>
                            <select
                                value={newProduct.categoryId || ''}
                                onChange={e => setNewProduct({...newProduct, categoryId: parseInt(e.target.value)})}
                                style={{ width: '100%', padding: '5px' }}
                            >
                                <option value="">Select Category</option>
                                {categories.map(category => (
                                    <option key={category.id} value={category.id}>
                                        {category.title}
                                    </option>
                                ))}
                            </select>
                        </div>
                        <div style={{ marginBottom: '10px' }}>
                            <label style={{ display: 'block', marginBottom: '5px' }}>Tax:</label>
                            <select
                                value={newProduct.taxId || ''}
                                onChange={e => setNewProduct({...newProduct, taxId: parseInt(e.target.value)})}
                                style={{ width: '100%', padding: '5px' }}
                            >
                                <option value="">Select Tax</option>
                                {taxes.map(tax => (
                                    <option key={tax.id} value={tax.id}>
                                        {tax.title} ({tax.percentage*100}%)
                                    </option>
                                ))}
                            </select>
                        </div>
                        <div style={{ marginBottom: '10px' }}>
                            <label style={{ display: 'block', marginBottom: '5px' }}>Discount:</label>
                            <select
                                value={newProduct.discountId || ''}
                                onChange={e => setNewProduct({...newProduct, discountId: parseInt(e.target.value)})}
                                style={{ width: '100%', padding: '5px' }}
                            >
                                <option value="">Select Discount</option>
                                {discounts.map(discount => (
                                    <option key={discount.id} value={discount.id}>
                                        {discount.title} ({discount.percentage}%)
                                    </option>
                                ))}
                            </select>
                        </div>
                        <div style={{ marginBottom: '10px' }}>
                            <label style={{ display: 'block', marginBottom: '5px' }}>Weight:</label>
                            <input
                                type="number"
                                placeholder="Enter weight"
                                value={newProduct.weight}
                                onChange={e => setNewProduct({...newProduct, weight: parseFloat(e.target.value)})}
                                style={{ width: '100%', padding: '5px' }}
                            />
                        </div>
                        <div style={{ marginBottom: '10px' }}>
                            <label style={{ display: 'block', marginBottom: '5px' }}>Weight Unit:</label>
                            <input
                                type="text"
                                placeholder="Enter weight unit (e.g., kg)"
                                value={newProduct.weightUnit}
                                onChange={e => setNewProduct({...newProduct, weightUnit: e.target.value})}
                                style={{ width: '100%', padding: '5px' }}
                            />
                        </div>
                        <div style={{ marginTop: '10px' }}>
                            <button onClick={handleAddProduct}>Add</button>
                            <button onClick={() => setIsAddProductModalOpen(false)}>Cancel</button>
                        </div>
                    </div>
                </div>
            )}

            {/* Add Variant Modal */}
            {isAddVariantModalOpen && (
                <div style={{
                    position: 'fixed',
                    top: 0,
                    left: 0,
                    right: 0,
                    bottom: 0,
                    backgroundColor: 'rgba(0,0,0,0.5)',
                    display: 'flex',
                    justifyContent: 'center',
                    alignItems: 'center'
                }}>
                    <div style={{ backgroundColor: 'white', padding: '20px', borderRadius: '4px' }}>
                        <h3>Add New Variant</h3>
                        <div style={{ marginBottom: '10px' }}>
                            <label style={{ display: 'block', marginBottom: '5px' }}>Title:</label>
                            <input
                                type="text"
                                placeholder="Enter variant title"
                                value={newVariant.title}
                                onChange={e => setNewVariant({...newVariant, title: e.target.value})}
                                style={{ width: '100%', padding: '5px' }}
                            />
                        </div>
                        <div style={{ marginBottom: '10px' }}>
                            <label style={{ display: 'block', marginBottom: '5px' }}>Additional Price:</label>
                            <input
                                type="number"
                                placeholder="Enter additional price"
                                value={newVariant.additionalPrice}
                                onChange={e => setNewVariant({...newVariant, additionalPrice: parseFloat(e.target.value)})}
                                style={{ width: '100%', padding: '5px' }}
                            />
                        </div>
                        <div style={{ marginTop: '10px' }}>
                            <button onClick={handleAddVariant}>Add</button>
                            <button onClick={() => setIsAddVariantModalOpen(false)}>Cancel</button>
                        </div>
                    </div>
                </div>
            )}

            {/* Edit Product Modal */}
            {isEditProductModalOpen && (
                <div style={{
                    position: 'fixed',
                    top: 0,
                    left: 0,
                    right: 0,
                    bottom: 0,
                    backgroundColor: 'rgba(0,0,0,0.5)',
                    display: 'flex',
                    justifyContent: 'center',
                    alignItems: 'center'
                }}>
                    <div style={{ backgroundColor: 'white', padding: '20px', borderRadius: '4px' }}>
                        <h3>Edit Product</h3>
                        <div style={{ marginBottom: '10px' }}>
                            <label>Title:</label>
                            <input
                                type="text"
                                value={editingProduct?.title || ''}
                                onChange={e => setEditingProduct({
                                    ...editingProduct,
                                    title: e.target.value
                                })}
                            />
                        </div>
                        <div style={{ marginBottom: '10px' }}>
                            <label>Price:</label>
                            <input
                                type="number"
                                value={editingProduct?.price || 0}
                                onChange={e => setEditingProduct({
                                    ...editingProduct,
                                    price: parseFloat(e.target.value)
                                })}
                            />
                        </div>
                        <div style={{ marginBottom: '10px' }}>
                            <label>Category:</label>
                            <select
                                value={editingProduct?.categoryId || ''}
                                onChange={e => setEditingProduct({...editingProduct, categoryId: parseInt(e.target.value)})}
                                style={{ width: '100%', padding: '5px' }}
                            >
                                <option value="">Select Category</option>
                                {categories.map(category => (
                                    <option key={category.id} value={category.id}>
                                        {category.title}
                                    </option>
                                ))}
                            </select>
                        </div>
                        <div style={{ marginBottom: '10px' }}>
                            <label>Tax:</label>
                            <select
                                value={editingProduct?.taxId || ''}
                                onChange={e => setEditingProduct({...editingProduct, taxId: parseInt(e.target.value)})}
                                style={{ width: '100%', padding: '5px' }}
                            >
                                <option value="">Select Tax</option>
                                {taxes.map(tax => (
                                    <option key={tax.id} value={tax.id}>
                                        {tax.title} ({tax.percentage*100}%)
                                    </option>
                                ))}
                            </select>
                        </div>
                        <div style={{ marginBottom: '10px' }}>
                            <label>Discount:</label>
                            <select
                                value={editingProduct?.discountId || ''}
                                onChange={e => setEditingProduct({...editingProduct, discountId: parseInt(e.target.value)})}
                                style={{ width: '100%', padding: '5px' }}
                            >
                                <option value="">Select Discount</option>
                                {discounts.map(discount => (
                                    <option key={discount.id} value={discount.id}>
                                        {discount.title} ({discount.percentage}%)
                                    </option>
                                ))}
                            </select>
                        </div>
                        <div style={{ marginBottom: '10px' }}>
                            <label>Weight:</label>
                            <input
                                type="number"
                                value={editingProduct?.weight || 0}
                                onChange={e => setEditingProduct({
                                    ...editingProduct,
                                    weight: parseFloat(e.target.value)
                                })}
                            />
                        </div>
                        <div style={{ marginBottom: '10px' }}>
                            <label>Weight Unit:</label>
                            <input
                                type="text"
                                value={editingProduct?.weightUnit || ''}
                                onChange={e => setEditingProduct({
                                    ...editingProduct,
                                    weightUnit: e.target.value
                                })}
                            />
                        </div>
                        <div>
                            <button onClick={handleEditProduct}>Save</button>
                            <button onClick={() => {
                                setIsEditProductModalOpen(false);
                                setEditingProduct(null);
                            }}>Cancel</button>
                        </div>
                    </div>
                </div>
            )}

            {/* Edit Variant Modal */}
            {isEditVariantModalOpen && (
                <div style={{
                    position: 'fixed',
                    top: 0,
                    left: 0,
                    right: 0,
                    bottom: 0,
                    backgroundColor: 'rgba(0,0,0,0.5)',
                    display: 'flex',
                    justifyContent: 'center',
                    alignItems: 'center'
                }}>
                    <div style={{ backgroundColor: 'white', padding: '20px', borderRadius: '4px' }}>
                        <h3>Edit Variant</h3>
                        <div style={{ marginBottom: '10px' }}>
                            <label>Title:</label>
                            <input
                                type="text"
                                value={editingVariant?.title || ''}
                                onChange={e => setEditingVariant({
                                    ...editingVariant,
                                    title: e.target.value
                                })}
                            />
                        </div>
                        <div style={{ marginBottom: '10px' }}>
                            <label>Additional Price:</label>
                            <input
                                type="number"
                                value={editingVariant?.additionalPrice || 0}
                                onChange={e => setEditingVariant({
                                    ...editingVariant,
                                    additionalPrice: parseFloat(e.target.value)
                                })}
                            />
                        </div>
                        <div>
                            <button onClick={handleEditVariant}>Save</button>
                            <button onClick={() => {
                                setIsEditVariantModalOpen(false);
                                setEditingVariant(null);
                            }}>Cancel</button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};

export default ProductManagementPage;
