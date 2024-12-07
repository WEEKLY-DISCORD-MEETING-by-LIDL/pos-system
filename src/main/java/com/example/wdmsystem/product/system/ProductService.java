package com.example.wdmsystem.product.system;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final IProductRepository productRepository;
    private final IProductVariantRepository productVariantRepository;

    public ProductService(IProductRepository productRepository, IProductVariantRepository productVariantRepository) {
        this.productRepository = productRepository;
        this.productVariantRepository = productVariantRepository;
    }

    public Product createProduct(ProductDTO request) {
        return null;
    }

    public ProductVariant createVariant(int productId, VariantDTO request) {
        return null;
    }

    public List<Product> getProducts(int categoryId, String createdAtMin, String createdAtMax, int limit) {
        return null;
    }

    public List<ProductVariant> getVariants(int productId) {
        return null;
    }

    public Product getProduct(int productId) {
        return null;
    }

    public ProductVariant getVariant(int variantId) {
        return null;
    }

    public void updateProduct(int productId, ProductDTO request) {

    }

    public void updateVariant(int productId, VariantDTO request) {

    }

    public void deleteProduct(int productId) {

    }

    public void deleteVariant(int productId, int variantId) {

    }

}
