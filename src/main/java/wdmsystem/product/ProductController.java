package wdmsystem.product;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class ProductController {

    private final ProductService _productService;

    public ProductController(ProductService productService) {
        this._productService = productService;
    }

    @PostMapping("/products")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO request) {
        log.info("Received request to create a product: {}", request);
        ProductDTO newProduct = _productService.createProduct(request);
        log.info("Product created successfully.");
        return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
    }

    @PostMapping("/products/{productId}/variants")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('OWNER') and @productService.productIsOwnedByCurrentUser(#productId))")
    ResponseEntity<ProductVariantDTO> createVariant(@PathVariable int productId, @RequestBody ProductVariantDTO request) {
        log.info("Received request to create a variant for product ID: {}", productId);
        ProductVariantDTO newVariant = _productService.createVariant(productId, request);
        log.info("Variant created successfully.");
        return new ResponseEntity<>(newVariant, HttpStatus.CREATED);
    }

    @GetMapping("/products")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER') or hasRole('REGULAR')")
    ResponseEntity<List<ProductDTO>> getProducts(@RequestParam(required = false) Integer categoryId,
                                                 @RequestParam(required = false) String createdAtMin,
                                                 @RequestParam(required = false) String createdAtMax,
                                                 @RequestParam(required = false) Integer limit) {
        log.info("Fetching products with filters - categoryId: {}, createdAtMin: {}, createdAtMax: {}, limit: {}",
                categoryId, createdAtMin, createdAtMax, limit);
        List<ProductDTO> productList = _productService.getProducts(categoryId, createdAtMin, createdAtMax, limit);
        log.info("Fetched {} products", productList.size());
        return new ResponseEntity<>(productList, HttpStatus.OK);
    }

    // new method
    @GetMapping("/products/order/{orderId}")
    ResponseEntity<List<ProductDTO>> getProductsByOrder(@PathVariable int orderId) {
        List<ProductDTO> products = _productService.getProductsByOrder(orderId);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/products/{productId}/variants")
    @PreAuthorize("hasRole('ADMIN') or ((hasRole('OWNER') or hasRole('REGULAR')) and @productService.productIsOwnedByCurrentUser(#productId))")
    ResponseEntity<List<ProductVariantDTO>> getVariants(@PathVariable int productId) {
        log.info("Fetching variants for product ID: {}", productId);
        List<ProductVariantDTO> productList = _productService.getVariants(productId);
        log.info("Fetched {} variants for product ID: {}", productList.size(), productId);
        return new ResponseEntity<>(productList, HttpStatus.OK);
    }



    @GetMapping("/products/{productId}")
    @PreAuthorize("hasRole('ADMIN') or ((hasRole('OWNER') or hasRole('REGULAR')) and @productService.productIsOwnedByCurrentUser(#productId))")
    ResponseEntity<ProductDTO> getProduct(@PathVariable int productId) {
        log.info("Fetching product with ID: {}", productId);
        ProductDTO product = _productService.getProduct(productId);
        log.info("Fetched product: {}", product);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    // new method
    @GetMapping("/variants/order/{orderId}")
    @PreAuthorize("hasRole('ADMIN') or ((hasRole('OWNER') or hasRole('REGULAR')) and @productService.variantIsOwnedByCurrentUser(#variantId))")
    ResponseEntity<List<ProductVariantDTO>> getVariantsByOrder(@PathVariable int orderId) {
        List<ProductVariantDTO> variants = _productService.getVariantsByOrder(orderId);
        return new ResponseEntity<>(variants, HttpStatus.OK);
    }

    @GetMapping("/variants/{variantId}")
    @PreAuthorize("hasRole('ADMIN') or ((hasRole('OWNER') or hasRole('REGULAR')) and @productService.variantIsOwnedByCurrentUser(#variantId))")
    ResponseEntity<ProductVariantDTO> getVariant(@PathVariable int variantId) {
        log.info("Fetching variant with ID: {}", variantId);
        ProductVariantDTO productVariant = _productService.getVariant(variantId);
        log.info("Fetched variant: {}", productVariant);
        return new ResponseEntity<>(productVariant, HttpStatus.OK);
    }

    @PutMapping("/products/{productId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('OWNER') and @productService.productIsOwnedByCurrentUser(#productId))")
    ResponseEntity<ProductDTO> updateProduct(@PathVariable int productId, @RequestBody ProductDTO request) {
        log.info("Updating product with ID: {}", productId);
        _productService.updateProduct(productId, request);
        log.info("Product with ID: {} updated successfully", productId);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @PutMapping("/variants/{variantId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('OWNER') and @productService.variantIsOwnedByCurrentUser(#variantId))")
    ResponseEntity<ProductVariantDTO> updateVariant(@PathVariable int variantId, @RequestBody ProductVariantDTO request) {
        log.info("Updating variant with ID: {}", variantId);
        _productService.updateVariant(variantId, request);
        log.info("Variant with ID: {} updated successfully", variantId);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/products/{productId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('OWNER') and @productService.productIsOwnedByCurrentUser(#productId))")
    ResponseEntity<ProductDTO> deleteProduct(@PathVariable int productId) {
        log.info("Deleting product with ID: {}", productId);
        _productService.deleteProduct(productId);
        log.info("Product with ID: {} deleted successfully", productId);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/variants/{variantId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('OWNER') and @productService.variantIsOwnedByCurrentUser(#variantId))")
    ResponseEntity<ProductVariantDTO> deleteVariant(@PathVariable int variantId) {
        log.info("Deleting variant with ID: {}", variantId);
        _productService.deleteVariant(variantId);
        log.info("Variant with ID: {} deleted successfully", variantId);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/products/{productId}/tax/{taxId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('OWNER') and @productService.productIsOwnedByCurrentUser(#productId))")
    ResponseEntity<ProductDTO> updateTax(@PathVariable int productId, @PathVariable int taxId) {
        log.info("Applying tax ID: {} to product ID: {}", taxId, productId);
        _productService.applyTax(productId, taxId);
        log.info("Tax ID: {} applied to product ID: {}", taxId, productId);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }
}