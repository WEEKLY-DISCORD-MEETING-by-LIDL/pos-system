package com.example.wdmsystem.product.system;

import com.example.wdmsystem.exception.InvalidInputException;
import com.example.wdmsystem.exception.NotFoundException;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final IProductRepository productRepository;
    private final IProductVariantRepository productVariantRepository;

    public ProductService(IProductRepository productRepository, IProductVariantRepository productVariantRepository) {
        this.productRepository = productRepository;
        this.productVariantRepository = productVariantRepository;
    }

    public Product createProduct(Product request) {

        if (request.title.length() > 30) {
            throw new InvalidInputException("Product title is longer than 30 characters");
        }
        if (request.weightUnit.length() > 10) {
            throw new InvalidInputException("Product weight unit is longer than 10 characters");
        }
        if (request.weight <= 0) {
            throw new InvalidInputException("Product weight must be greater than 0");
        }
        if (request.price < 0) {
            throw new InvalidInputException("Product price must be greater than or equal to 0");
        }

        // placeholder
        request.merchantId = 10;

        request.createdAt = LocalDateTime.now();
        request.updatedAt = LocalDateTime.now();

        return productRepository.save(request);
    }

    public ProductVariant createVariant(int productId, ProductVariant request) {

        if (request.title.length() > 30) {
            throw new InvalidInputException("Product variant title is longer than 30 characters");
        }

        if (productRepository.existsById(productId)) {
            request.productId = productId;
            request.createdAt = LocalDateTime.now();
            request.updatedAt = LocalDateTime.now();
            return productVariantRepository.save(request);
        }
        else {
            throw new NotFoundException("Product with id " + productId + " not found");
        }
    }

    public List<Product> getProducts(int categoryId, String createdAtMin, String createdAtMax, int limit) {

        if (limit <= 0) {
            limit = 50;
        }

        List<Product> filteredProducts = productRepository.getProductsByCategoryIdAndCreatedAtGreaterThanEqualAndCreatedAtLessThanEqual(
                categoryId,
                LocalDateTime.parse(createdAtMin),
                LocalDateTime.parse(createdAtMax),
                Limit.of(limit)
        );

        return filteredProducts;
    }

    public List<ProductVariant> getVariants(int productId) {

        if (productRepository.existsById(productId)) {
            return productVariantRepository.getProductVariantsByProductId(productId);
        }
        else {
            throw new NotFoundException("Product with id " + productId + " not found");
        }
    }

    public Product getProduct(int productId) {

        Optional<Product> product = productRepository.findById(productId);

        if (product.isPresent()) {
            return product.get();
        }
        else {
            throw new NotFoundException("Product with id " + productId + " not found");
        }
    }

    public ProductVariant getVariant(int variantId) {

        Optional<ProductVariant> variant = productVariantRepository.findById(variantId);

        if (variant.isPresent()) {
            return variant.get();
        }
        else {
            throw new NotFoundException("Product with id " + variantId + " not found");
        }
    }

    public void updateProduct(int productId, Product request) {

        Optional<Product> product = productRepository.findById(productId);

        if(request.title.length() > 30) {
            throw new InvalidInputException("Product title is longer than 30 characters");
        }
        if(request.weightUnit.length() > 10) {
            throw new InvalidInputException("Product weight unit is longer than 10 characters");
        }
        if(request.weight <= 0) {
            throw new InvalidInputException("Product weight must be greater than 0");
        }
        if(request.price < 0) {
            throw new InvalidInputException("Product price must be greater than or equal to 0");
        }


        if (product.isPresent()) {
            Product updatedProduct = product.get();

            updatedProduct.title = request.title;
            updatedProduct.weight = request.weight;
            updatedProduct.weightUnit = request.weightUnit;
            updatedProduct.price = request.price;
            updatedProduct.updatedAt = LocalDateTime.now();

            productRepository.save(updatedProduct);
        }
        else {
            throw new NotFoundException("Product with id " + productId + " not found");
        }
    }

    public void updateVariant(int variantId, ProductVariant request) {

        Optional<ProductVariant> variant = productVariantRepository.findById(variantId);

        if(request.title.length() > 30) {
            throw new InvalidInputException("Product variant title is longer than 30 characters");
        }

        if (variant.isPresent()) {
            ProductVariant updatedVariant = variant.get();

            updatedVariant.title = request.title;
            updatedVariant.additionalPrice = request.additionalPrice;
            updatedVariant.updatedAt = LocalDateTime.now();

            productVariantRepository.save(updatedVariant);
        }
        else {
            throw new NotFoundException("Product with id " + variantId + " not found");
        }
    }

    public void deleteProduct(int productId) {

        Optional<Product> product = productRepository.findById(productId);

        if (product.isPresent()) {

            List<ProductVariant> variants = productVariantRepository.getProductVariantsByProductId(productId);
            productVariantRepository.deleteAll(variants);

            productRepository.deleteById(productId);
        }
        else {
            throw new NotFoundException("Product with id " + productId + " not found");
        }
    }

    public void deleteVariant(int variantId) {

        Optional<ProductVariant> variant = productVariantRepository.findById(variantId);

        if (variant.isPresent()) {
            productVariantRepository.deleteById(variantId);
        }
        else {
            throw new NotFoundException("Product variant with id " + variantId + " not found");
        }
    }

}
