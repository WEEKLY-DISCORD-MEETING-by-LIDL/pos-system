package com.example.wdmsystem.product.system;

import com.example.wdmsystem.exception.InvalidInputException;
import com.example.wdmsystem.exception.NotFoundException;
import com.example.wdmsystem.utility.DTOMapper;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final IProductRepository productRepository;
    private final IProductVariantRepository productVariantRepository;

    private final DTOMapper dtoMapper;

    public ProductService(IProductRepository productRepository, IProductVariantRepository productVariantRepository, DTOMapper dtoMapper) {
        this.productRepository = productRepository;
        this.productVariantRepository = productVariantRepository;
        this.dtoMapper = dtoMapper;
    }

    public ProductDTO createProduct(ProductDTO request) {

        if (request.title().length() > 30) {
            throw new InvalidInputException("Product title is longer than 30 characters");
        }
        if (request.weightUnit().length() > 10) {
            throw new InvalidInputException("Product weight unit is longer than 10 characters");
        }
        if (request.weight() <= 0) {
            throw new InvalidInputException("Product weight must be greater than 0");
        }
        if (request.price() < 0) {
            throw new InvalidInputException("Product price must be greater than or equal to 0");
        }

        Product product = dtoMapper.Product_DTOToModel(request);

        // placeholder
        //product.merchant.id = 10; idk what to do with this

        product.createdAt = LocalDateTime.now();
        product.updatedAt = LocalDateTime.now();

        Product savedProduct = productRepository.save(product);

        ProductVariantDTO defaultVariantRequest = new ProductVariantDTO(null, savedProduct.getId(), "Default", 0);
        createVariant(savedProduct.getId(), defaultVariantRequest);

        return dtoMapper.Product_ModelToDTO(savedProduct);
    }

    public ProductVariantDTO createVariant(int productId, ProductVariantDTO request) {

        if (request.title().length() > 30) {
            throw new InvalidInputException("Product variant title is longer than 30 characters");
        }
        if (request.additionalPrice() < 0) {
            throw new InvalidInputException("Product variant additional price must be greater than or equal 0");
        }

        Product product = productRepository.findById(productId).orElseThrow(() ->
                new NotFoundException("Product with id " + productId + " not found"));

        ProductVariant variant = dtoMapper.ProductVariant_DTOToModel(request, product);

        variant.createdAt = LocalDateTime.now();
        variant.updatedAt = LocalDateTime.now();

        return dtoMapper.ProductVariant_ModelToDTO(productVariantRepository.save(variant));
    }

    public List<ProductDTO> getProducts(Integer categoryId, String createdAtMin, String createdAtMax, Integer limit) {

        if(createdAtMin == null) {
            createdAtMin = "0000-01-01T00:00:00.000";
        }
        if(createdAtMax == null) {
            createdAtMax = "9999-01-01T00:00:00.000";
        }
        if (limit == null || limit <= 0) {
            limit = 50;
        }
        if(categoryId != null && categoryId <= 0) {
            categoryId = null;
        }

        List<Product> filteredProducts;

        if(categoryId == null) {
            filteredProducts = productRepository.getProductsByCreatedAtGreaterThanEqualAndCreatedAtLessThanEqual(
                    LocalDateTime.parse(createdAtMin),
                    LocalDateTime.parse(createdAtMax),
                    Limit.of(limit)
            );
        }
        else {
            filteredProducts = productRepository.getProductsByCategoryIdAndCreatedAtGreaterThanEqualAndCreatedAtLessThanEqual(
                    categoryId,
                    LocalDateTime.parse(createdAtMin),
                    LocalDateTime.parse(createdAtMax),
                    Limit.of(limit)
            );
        }

        List<ProductDTO> productDTOs = new ArrayList<>();

        for(Product product : filteredProducts) {
            productDTOs.add(dtoMapper.Product_ModelToDTO(product));
        }

        return productDTOs;
    }

    public List<ProductVariantDTO> getVariants(int productId) {

        if (productRepository.existsById(productId)) {
            List<ProductVariant> variants = productVariantRepository.getProductVariantsByProductId(productId);

            List<ProductVariantDTO> variantDTOs = new ArrayList<>();

            for(ProductVariant variant : variants) {
                variantDTOs.add(dtoMapper.ProductVariant_ModelToDTO(variant));
            }

            return variantDTOs;
        }
        else {
            throw new NotFoundException("Product with id " + productId + " not found");
        }
    }

    public ProductDTO getProduct(int productId) {

        Product product = productRepository.findById(productId).orElseThrow(() ->
                new NotFoundException("Product with id " + productId + " not found"));

        return dtoMapper.Product_ModelToDTO(product);
    }

    public ProductVariantDTO getVariant(int variantId) {

        ProductVariant variant = productVariantRepository.findById(variantId).orElseThrow(() ->
                new NotFoundException("Variant with id " + variantId + " not found"));

        return dtoMapper.ProductVariant_ModelToDTO(variant);
    }

    public void updateProduct(int productId, ProductDTO request) {

        Product product = productRepository.findById(productId).orElseThrow(() ->
                new NotFoundException("Product with id " + productId + " not found"));

        if(request.title().length() > 30) {
            throw new InvalidInputException("Product title is longer than 30 characters");
        }
        if(request.weightUnit().length() > 10) {
            throw new InvalidInputException("Product weight unit is longer than 10 characters");
        }
        if(request.weight() <= 0) {
            throw new InvalidInputException("Product weight must be greater than 0");
        }
        if(request.price() < 0) {
            throw new InvalidInputException("Product price must be greater than or equal to 0");
        }

        product.title = request.title();
        product.weight = request.weight();
        product.weightUnit = request.weightUnit();
        product.price = request.price();
        product.updatedAt = LocalDateTime.now();

        productRepository.save(product);
    }

    public void updateVariant(int variantId, ProductVariantDTO request) {

        ProductVariant variant = productVariantRepository.findById(variantId).orElseThrow(() ->
                new NotFoundException("Variant with id " + variantId + " not found"));

        if(request.title().length() > 30) {
            throw new InvalidInputException("Product variant title is longer than 30 characters");
        }
        if (request.additionalPrice() < 0) {
            throw new InvalidInputException("Product variant additional price must be greater than or equal 0");
        }

        variant.title = request.title();
        variant.additionalPrice = request.additionalPrice();
        variant.updatedAt = LocalDateTime.now();

        productVariantRepository.save(variant);
    }

    public void deleteProduct(int productId) {

        if(productRepository.existsById(productId)) {
            productRepository.deleteById(productId);
        }
        else {
            throw new NotFoundException("Product with id " + productId + " not found");
        }
    }

    public void deleteVariant(int variantId) {

        if(productVariantRepository.existsById(variantId)) {
            productVariantRepository.deleteById(variantId);
        }
        else {
            throw new NotFoundException("Variant with id " + variantId + " not found");
        }
    }

}
