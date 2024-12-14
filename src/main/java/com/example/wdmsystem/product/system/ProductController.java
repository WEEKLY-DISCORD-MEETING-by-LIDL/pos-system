package com.example.wdmsystem.product.system;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ProductController {

    private ProductService _productService;

    public ProductController(ProductService productService) {
        this._productService = productService;
    }

    @PostMapping("/products")
    ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO request) {
        ProductDTO newProduct = _productService.createProduct(request);
        return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
    }

    @PostMapping("/products/{productId}/variants")
    ResponseEntity<ProductVariantDTO> createVariant(@PathVariable int productId, @RequestBody ProductVariantDTO request) {
        ProductVariantDTO newVariant = _productService.createVariant(productId, request);
        return new ResponseEntity<>(newVariant, HttpStatus.CREATED);
    }

    @GetMapping("/products")
    ResponseEntity<List<ProductDTO>> getProducts(@RequestParam(required = false) Integer categoryId,
                                              @RequestParam(required = false) String createdAtMin,
                                              @RequestParam(required = false) String createdAtMax,
                                              @RequestParam(required = false) Integer limit) {
        List<ProductDTO> productList = _productService.getProducts(categoryId, createdAtMin, createdAtMax, limit);
        return new ResponseEntity<>(productList, HttpStatus.OK);
    }

    @GetMapping("/products/{productId}/variants")
    ResponseEntity<List<ProductVariantDTO>> getVariants(@PathVariable int productId) {
        List<ProductVariantDTO> productList = _productService.getVariants(productId);
        return new ResponseEntity<>(productList, HttpStatus.OK);
    }

    @GetMapping("/products/{productId}")
    ResponseEntity<ProductDTO> getProduct(@PathVariable int productId) {
        ProductDTO product = _productService.getProduct(productId);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping("/variants/{variantId}")
    ResponseEntity<ProductVariantDTO> getVariant(@PathVariable int variantId) {
        ProductVariantDTO productVariant = _productService.getVariant(variantId);
        return new ResponseEntity<>(productVariant, HttpStatus.OK);
    }

    @PutMapping("/products/{productId}")
    ResponseEntity<ProductDTO> updateProduct(@PathVariable int productId, @RequestBody ProductDTO request) {
        _productService.updateProduct(productId, request);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @PutMapping("/variants/{variantId}")
    ResponseEntity<ProductVariantDTO> updateVariant(@PathVariable int variantId, @RequestBody ProductVariantDTO request) {
        _productService.updateVariant(variantId, request);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/products/{productId}")
    ResponseEntity<ProductDTO> deleteProduct(@PathVariable int productId) {
        _productService.deleteProduct(productId);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/variants/{variantId}")
    ResponseEntity<ProductVariantDTO> deleteVariant(@PathVariable int variantId) {
        _productService.deleteVariant(variantId);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }
}