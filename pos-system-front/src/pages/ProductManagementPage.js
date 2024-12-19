import React, { useState, useEffect } from 'react';
import {
    PageContainer,
    SectionTitle,
    ContentLayout,
    SectionContainer,
    SectionHeader,
    ListContainer,
    ItemCard,
    CardContent,
    CardInfo,
    CardTitle,
    CardDetails,
    DetailText,
    ButtonGroup,
    Button,
    Modal,
    ModalContent,
    FormGroup,
    Label,
    Input,
    Select
} from '../styles/ProductManagementStyle';
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
        <PageContainer>
            <SectionTitle>Product Management</SectionTitle>

            {/* Product List Section */}
            <ContentLayout>
                <SectionContainer>
                    <SectionHeader>
                        <h3>Products</h3>
                        <Button primary onClick={() => setIsAddProductModalOpen(true)}>Add Product</Button>
                    </SectionHeader>
                    <ListContainer>
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
                                <CardContent>
                                    <CardInfo>
                                        <CardTitle>{product.title}</CardTitle>
                                        <CardDetails>
                                            <DetailText>Price: €{product.price.toFixed(2)}</DetailText>
                                            {product.categoryId && categories.find(c => c.id === product.categoryId) && (
                                                <DetailText>Category: {categories.find(c => c.id === product.categoryId).title}</DetailText>
                                            )}
                                            {product.taxId && taxes.find(t => t.id === product.taxId) && (
                                                <DetailText>Tax: {taxes.find(t => t.id === product.taxId).title} 
                                                ({(taxes.find(t => t.id === product.taxId).percentage * 100).toFixed(1)}%)</DetailText>
                                            )}
                                            {product.discountId && discounts.find(d => d.id === product.discountId) && (
                                                <DetailText>Discount: {discounts.find(d => d.id === product.discountId).title} 
                                                ({discounts.find(d => d.id === product.discountId).percentage}%)</DetailText>
                                            )}
                                            {product.weight && (
                                                <DetailText>Weight: {product.weight} {product.weightUnit}</DetailText>
                                            )}
                                        </CardDetails>
                                    </CardInfo>
                                    <ButtonGroup>
                                        <Button onClick={() => {
                                            setEditingProduct(product);
                                            setIsEditProductModalOpen(true);
                                        }}>Edit</Button>
                                        <Button 
                                            onClick={(e) => {
                                                e.stopPropagation();
                                                handleDeleteProduct(product.id);
                                            }}>Delete</Button>
                                    </ButtonGroup>
                                </CardContent>
                            </div>
                        ))}
                    </ListContainer>
                </SectionContainer>

                {/* Variants Section */}
                <SectionContainer>
                    <SectionHeader>
                        <h3>Variants</h3>
                        {selectedProduct && (
                            <Button primary onClick={() => setIsAddVariantModalOpen(true)}>Add Variant</Button>
                        )}
                    </SectionHeader>
                    <ListContainer>
                        {selectedProduct ? (
                            variants.map(variant => (
                                <ItemCard
                                    key={variant.id}
                                >
                                    <CardContent>
                                        <span>{variant.title}</span>
                                        <ButtonGroup>
                                            <Button onClick={() => {
                                                setEditingVariant(variant);
                                                setIsEditVariantModalOpen(true);
                                            }}>Edit</Button>
                                            <Button onClick={() => handleDeleteVariant(variant.id)}>Delete</Button>
                                        </ButtonGroup>
                                    </CardContent>
                                    <div>Additional Price: €{variant.additionalPrice}</div>
                                </ItemCard>
                            ))
                        ) : (
                            <p>Select a product to view variants</p>
                        )}
                    </ListContainer>
                </SectionContainer>
            </ContentLayout>

            {/* Add Product Modal */}
            {isAddProductModalOpen && (
                <Modal>
                    <ModalContent>
                        <h3>Add New Product</h3>
                        <FormGroup>
                            <Label>Title:</Label>
                            <Input
                                type="text"
                                placeholder="Enter product title"
                                value={newProduct.title}
                                onChange={e => setNewProduct({...newProduct, title: e.target.value})}
                            />
                        </FormGroup>
                        <FormGroup>
                            <Label>Price:</Label>
                            <Input
                                type="number"
                                placeholder="Enter price"
                                value={newProduct.price}
                                onChange={e => setNewProduct({...newProduct, price: parseFloat(e.target.value)})}
                            />
                        </FormGroup>
                        <FormGroup>
                            <Label>Category:</Label>
                            <Select
                                value={newProduct.categoryId || ''}
                                onChange={e => setNewProduct({...newProduct, categoryId: parseInt(e.target.value)})}
                            >
                                <option value="">Select Category</option>
                                {categories.map(category => (
                                    <option key={category.id} value={category.id}>
                                        {category.title}
                                    </option>
                                ))}
                            </Select>
                        </FormGroup>
                        <FormGroup>
                            <Label>Tax:</Label>
                            <Select
                                value={newProduct.taxId || ''}
                                onChange={e => setNewProduct({...newProduct, taxId: parseInt(e.target.value)})}
                            >
                                <option value="">Select Tax</option>
                                {taxes.map(tax => (
                                    <option key={tax.id} value={tax.id}>
                                        {tax.title} ({tax.percentage*100}%)
                                    </option>
                                ))}
                            </Select>
                        </FormGroup>
                        <FormGroup>
                            <Label>Discount:</Label>
                            <Select
                                value={newProduct.discountId || ''}
                                onChange={e => setNewProduct({...newProduct, discountId: parseInt(e.target.value)})}
                            >
                                <option value="">Select Discount</option>
                                {discounts.map(discount => (
                                    <option key={discount.id} value={discount.id}>
                                        {discount.title} ({discount.percentage}%)
                                    </option>
                                ))}
                            </Select>
                        </FormGroup>
                        <FormGroup>
                            <Label>Weight:</Label>
                            <Input
                                type="number"
                                placeholder="Enter weight"
                                value={newProduct.weight}
                                onChange={e => setNewProduct({...newProduct, weight: parseFloat(e.target.value)})}
                            />
                        </FormGroup>
                        <FormGroup>
                            <Label>Weight Unit:</Label>
                            <Input
                                type="text"
                                placeholder="Enter weight unit (e.g., kg)"
                                value={newProduct.weightUnit}
                                onChange={e => setNewProduct({...newProduct, weightUnit: e.target.value})}
                            />
                        </FormGroup>
                        <div>
                            <Button onClick={handleAddProduct}>Add</Button>
                            <Button onClick={() => setIsAddProductModalOpen(false)}>Cancel</Button>
                        </div>
                    </ModalContent>
                </Modal>
            )}

            {/* Add Variant Modal */}
            {isAddVariantModalOpen && (
                <Modal>
                    <ModalContent>
                        <h3>Add New Variant</h3>
                        <FormGroup>
                            <Label>Title:</Label>
                            <Input
                                type="text"
                                placeholder="Enter variant title"
                                value={newVariant.title}
                                onChange={e => setNewVariant({...newVariant, title: e.target.value})}
                            />
                        </FormGroup>
                        <FormGroup>
                            <Label>Additional Price:</Label>
                            <Input
                                type="number"
                                placeholder="Enter additional price"
                                value={newVariant.additionalPrice}
                                onChange={e => setNewVariant({...newVariant, additionalPrice: parseFloat(e.target.value)})}
                            />
                        </FormGroup>
                        <div>
                            <Button onClick={handleAddVariant}>Add</Button>
                            <Button onClick={() => setIsAddVariantModalOpen(false)}>Cancel</Button>
                        </div>
                    </ModalContent>
                </Modal>
            )}

            {/* Edit Product Modal */}
            {isEditProductModalOpen && (
                <Modal>
                    <ModalContent>
                        <h3>Edit Product</h3>
                        <FormGroup>
                            <Label>Title:</Label>
                            <Input
                                type="text"
                                value={editingProduct?.title || ''}
                                onChange={e => setEditingProduct({
                                    ...editingProduct,
                                    title: e.target.value
                                })}
                            />
                        </FormGroup>
                        <FormGroup>
                            <Label>Price:</Label>
                            <Input
                                type="number"
                                value={editingProduct?.price || 0}
                                onChange={e => setEditingProduct({
                                    ...editingProduct,
                                    price: parseFloat(e.target.value)
                                })}
                            />
                        </FormGroup>
                        <FormGroup>
                            <Label>Category:</Label>
                            <Select
                                value={editingProduct?.categoryId || ''}
                                onChange={e => setEditingProduct({...editingProduct, categoryId: parseInt(e.target.value)})}
                            >
                                <option value="">Select Category</option>
                                {categories.map(category => (
                                    <option key={category.id} value={category.id}>
                                        {category.title}
                                    </option>
                                ))}
                            </Select>
                        </FormGroup>
                        <FormGroup>
                            <Label>Tax:</Label>
                            <Select
                                value={editingProduct?.taxId || ''}
                                onChange={e => setEditingProduct({...editingProduct, taxId: parseInt(e.target.value)})}
                            >
                                <option value="">Select Tax</option>
                                {taxes.map(tax => (
                                    <option key={tax.id} value={tax.id}>
                                        {tax.title} ({tax.percentage*100}%)
                                    </option>
                                ))}
                            </Select>
                        </FormGroup>
                        <FormGroup>
                            <Label>Discount:</Label>
                            <Select
                                value={editingProduct?.discountId || ''}
                                onChange={e => setEditingProduct({...editingProduct, discountId: parseInt(e.target.value)})}
                            >
                                <option value="">Select Discount</option>
                                {discounts.map(discount => (
                                    <option key={discount.id} value={discount.id}>
                                        {discount.title} ({discount.percentage}%)
                                    </option>
                                ))}
                            </Select>
                        </FormGroup>
                        <FormGroup>
                            <Label>Weight:</Label>
                            <Input
                                type="number"
                                value={editingProduct?.weight || 0}
                                onChange={e => setEditingProduct({
                                    ...editingProduct,
                                    weight: parseFloat(e.target.value)
                                })}
                            />
                        </FormGroup>
                        <FormGroup>
                            <Label>Weight Unit:</Label>
                            <Input
                                type="text"
                                value={editingProduct?.weightUnit || ''}
                                onChange={e => setEditingProduct({
                                    ...editingProduct,
                                    weightUnit: e.target.value
                                })}
                            />
                        </FormGroup>
                        <div>
                            <Button onClick={handleEditProduct}>Save</Button>
                            <Button onClick={() => {
                                setIsEditProductModalOpen(false);
                                setEditingProduct(null);
                            }}>Cancel</Button>
                        </div>
                    </ModalContent>
                </Modal>
            )}

            {/* Edit Variant Modal */}
            {isEditVariantModalOpen && (
                <Modal>
                    <ModalContent>
                        <h3>Edit Variant</h3>
                        <FormGroup>
                            <Label>Title:</Label>
                            <Input
                                type="text"
                                value={editingVariant?.title || ''}
                                onChange={e => setEditingVariant({
                                    ...editingVariant,
                                    title: e.target.value
                                })}
                            />
                        </FormGroup>
                        <FormGroup>
                            <Label>Additional Price:</Label>
                            <Input
                                type="number"
                                value={editingVariant?.additionalPrice || 0}
                                onChange={e => setEditingVariant({
                                    ...editingVariant,
                                    additionalPrice: parseFloat(e.target.value)
                                })}
                            />
                        </FormGroup>
                        <div>
                            <Button onClick={handleEditVariant}>Save</Button>
                            <Button onClick={() => {
                                setIsEditVariantModalOpen(false);
                                setEditingVariant(null);
                            }}>Cancel</Button>
                        </div>
                    </ModalContent>
                </Modal>
            )}
        </PageContainer>
    );
};

export default ProductManagementPage;
