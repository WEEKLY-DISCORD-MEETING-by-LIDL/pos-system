package wdmsystem.product;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final ProductService _productService;

    public ProductController(ProductService productService) {
        this._productService = productService;
    }

    @PostMapping("/products")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO request) {
        logger.info("Received request to create a product: {}", request);
        ProductDTO newProduct = _productService.createProduct(request);
        logger.info("Product created successfully.");
        return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
    }

    @PostMapping("/products/{productId}/variants")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('OWNER') and @productService.productIsOwnedByCurrentUser(#productId))")
    ResponseEntity<ProductVariantDTO> createVariant(@PathVariable int productId, @RequestBody ProductVariantDTO request) {
        logger.info("Received request to create a variant for product ID: {}", productId);
        ProductVariantDTO newVariant = _productService.createVariant(productId, request);
        logger.info("Variant created successfully.");
        return new ResponseEntity<>(newVariant, HttpStatus.CREATED);
    }

    @GetMapping("/products")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER') or hasRole('REGULAR')")
    ResponseEntity<List<ProductDTO>> getProducts(@RequestParam(required = false) Integer categoryId,
                                                 @RequestParam(required = false) String createdAtMin,
                                                 @RequestParam(required = false) String createdAtMax,
                                                 @RequestParam(required = false) Integer limit) {
        logger.info("Fetching products with filters - categoryId: {}, createdAtMin: {}, createdAtMax: {}, limit: {}",
                categoryId, createdAtMin, createdAtMax, limit);
        List<ProductDTO> productList = _productService.getProducts(categoryId, createdAtMin, createdAtMax, limit);
        logger.info("Fetched {} products", productList.size());
        return new ResponseEntity<>(productList, HttpStatus.OK);
    }

    @GetMapping("/products/{productId}/variants")
    @PreAuthorize("hasRole('ADMIN') or ((hasRole('OWNER') or hasRole('REGULAR')) and @productService.productIsOwnedByCurrentUser(#productId))")
    ResponseEntity<List<ProductVariantDTO>> getVariants(@PathVariable int productId) {
        logger.info("Fetching variants for product ID: {}", productId);
        List<ProductVariantDTO> productList = _productService.getVariants(productId);
        logger.info("Fetched {} variants for product ID: {}", productList.size(), productId);
        return new ResponseEntity<>(productList, HttpStatus.OK);
    }

    @GetMapping("/products/{productId}")
    @PreAuthorize("hasRole('ADMIN') or ((hasRole('OWNER') or hasRole('REGULAR')) and @productService.productIsOwnedByCurrentUser(#productId))")
    ResponseEntity<ProductDTO> getProduct(@PathVariable int productId) {
        logger.info("Fetching product with ID: {}", productId);
        ProductDTO product = _productService.getProduct(productId);
        logger.info("Fetched product: {}", product);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping("/variants/{variantId}")
    @PreAuthorize("hasRole('ADMIN') or ((hasRole('OWNER') or hasRole('REGULAR')) and @productService.variantIsOwnedByCurrentUser(#variantId))")
    ResponseEntity<ProductVariantDTO> getVariant(@PathVariable int variantId) {
        logger.info("Fetching variant with ID: {}", variantId);
        ProductVariantDTO productVariant = _productService.getVariant(variantId);
        logger.info("Fetched variant: {}", productVariant);
        return new ResponseEntity<>(productVariant, HttpStatus.OK);
    }

    @PutMapping("/products/{productId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('OWNER') and @productService.productIsOwnedByCurrentUser(#productId))")
    ResponseEntity<ProductDTO> updateProduct(@PathVariable int productId, @RequestBody ProductDTO request) {
        logger.info("Updating product with ID: {}", productId);
        _productService.updateProduct(productId, request);
        logger.info("Product with ID: {} updated successfully", productId);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @PutMapping("/variants/{variantId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('OWNER') and @productService.variantIsOwnedByCurrentUser(#variantId))")
    ResponseEntity<ProductVariantDTO> updateVariant(@PathVariable int variantId, @RequestBody ProductVariantDTO request) {
        logger.info("Updating variant with ID: {}", variantId);
        _productService.updateVariant(variantId, request);
        logger.info("Variant with ID: {} updated successfully", variantId);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/products/{productId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('OWNER') and @productService.productIsOwnedByCurrentUser(#productId))")
    ResponseEntity<ProductDTO> deleteProduct(@PathVariable int productId) {
        logger.info("Deleting product with ID: {}", productId);
        _productService.deleteProduct(productId);
        logger.info("Product with ID: {} deleted successfully", productId);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/variants/{variantId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('OWNER') and @productService.variantIsOwnedByCurrentUser(#variantId))")
    ResponseEntity<ProductVariantDTO> deleteVariant(@PathVariable int variantId) {
        logger.info("Deleting variant with ID: {}", variantId);
        _productService.deleteVariant(variantId);
        logger.info("Variant with ID: {} deleted successfully", variantId);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/products/{productId}/tax/{taxId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('OWNER') and @productService.productIsOwnedByCurrentUser(#productId))")
    ResponseEntity<ProductDTO> updateTax(@PathVariable int productId, @PathVariable int taxId) {
        logger.info("Applying tax ID: {} to product ID: {}", taxId, productId);
        _productService.applyTax(productId, taxId);
        logger.info("Tax ID: {} applied to product ID: {}", taxId, productId);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }
}