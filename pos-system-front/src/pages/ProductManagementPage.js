import React, { useState, useEffect } from 'react';
import { 
    fetchProducts,
    createProduct, 
    updateProduct, 
    deleteProduct,
    fetchVariants,
    createVariant,
    deleteVariant 
} from '../api/ProductAPI';

const ProductManagementPage = () => {
    const [products, setProducts] = useState([]);
    const [selectedProduct, setSelectedProduct] = useState(null);
    const [variants, setVariants] = useState([]);
    const [isAddProductModalOpen, setIsAddProductModalOpen] = useState(false);
    const [isAddVariantModalOpen, setIsAddVariantModalOpen] = useState(false);
    const [newProduct, setNewProduct] = useState({
        title: '',
        price: 0,
        weight: 0,
        weightUnit: 'kg',
        categoryId: 1
    });
    const [newVariant, setNewVariant] = useState({
        title: '',
        additionalPrice: 0
    });

    useEffect(() => {
        loadProducts();
    }, []);

    const loadProducts = async () => {
        await fetchProducts(null, null, null, null, setProducts);
    };

    const loadVariants = async (productId) => {
        await fetchVariants(productId, setVariants);
    };

    const handleProductSelect = async (product) => {
        setSelectedProduct(product);
        await loadVariants(product.id);
    };

    const handleAddProduct = async () => {
        try {
            await createProduct(newProduct);
            setIsAddProductModalOpen(false);
            setNewProduct({ title: '', price: 0, weight: 0, weightUnit: 'kg', categoryId: 1 });
            await loadProducts();
        } catch (error) {
            console.error('Error adding product:', error);
        }
    };

    const handleAddVariant = async () => {
        if (!selectedProduct) return;
        try {
            await createVariant(selectedProduct.id, newVariant);
            setIsAddVariantModalOpen(false);
            setNewVariant({ title: '', additionalPrice: 0 });
            await loadVariants(selectedProduct.id);
        } catch (error) {
            console.error('Error adding variant:', error);
        }
    };

    const handleDeleteProduct = async (productId) => {
        try {
            await deleteProduct(productId);
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
            await deleteVariant(variantId);
            if (selectedProduct) {
                await loadVariants(selectedProduct.id);
            }
        } catch (error) {
            console.error('Error deleting variant:', error);
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
                                    <button onClick={(e) => {
                                        e.stopPropagation();
                                        handleDeleteProduct(product.id);
                                    }}>Delete</button>
                                </div>
                                <div>Price: ${product.price}</div>
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
                                        <button onClick={() => handleDeleteVariant(variant.id)}>Delete</button>
                                    </div>
                                    <div>Additional Price: ${variant.additionalPrice}</div>
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
                        <div>
                            <input
                                type="text"
                                placeholder="Product Title"
                                value={newProduct.title}
                                onChange={e => setNewProduct({...newProduct, title: e.target.value})}
                            />
                        </div>
                        <div>
                            <input
                                type="number"
                                placeholder="Price"
                                value={newProduct.price}
                                onChange={e => setNewProduct({...newProduct, price: parseFloat(e.target.value)})}
                            />
                        </div>
                        <div>
                            <input
                                type="number"
                                placeholder="Weight"
                                value={newProduct.weight}
                                onChange={e => setNewProduct({...newProduct, weight: parseFloat(e.target.value)})}
                            />
                        </div>
                        <div>
                            <input
                                type="text"
                                placeholder="Weight Unit"
                                value={newProduct.weightUnit}
                                onChange={e => setNewProduct({...newProduct, weightUnit: e.target.value})}
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
                        <div>
                            <input
                                type="text"
                                placeholder="Variant Title"
                                value={newVariant.title}
                                onChange={e => setNewVariant({...newVariant, title: e.target.value})}
                            />
                        </div>
                        <div>
                            <input
                                type="number"
                                placeholder="Additional Price"
                                value={newVariant.additionalPrice}
                                onChange={e => setNewVariant({...newVariant, additionalPrice: parseFloat(e.target.value)})}
                            />
                        </div>
                        <div style={{ marginTop: '10px' }}>
                            <button onClick={handleAddVariant}>Add</button>
                            <button onClick={() => setIsAddVariantModalOpen(false)}>Cancel</button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};

export default ProductManagementPage;
