package wdmsystem.product;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
public class ProductController {

    private final ProductService _productService;

    public ProductController(ProductService productService) {
        this._productService = productService;
    }

    @PostMapping("/products")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO request) {
        ProductDTO newProduct = _productService.createProduct(request);
        return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
    }

    @PostMapping("/products/{productId}/variants")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('OWNER') and @productService.productIsOwnedByCurrentUser(#productId))")
    ResponseEntity<ProductVariantDTO> createVariant(@PathVariable int productId, @RequestBody ProductVariantDTO request) {
        ProductVariantDTO newVariant = _productService.createVariant(productId, request);
        return new ResponseEntity<>(newVariant, HttpStatus.CREATED);
    }

    @GetMapping("/products")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER') or hasRole('REGULAR')")
    ResponseEntity<List<ProductDTO>> getProducts(@RequestParam(required = false) Integer categoryId,
                                              @RequestParam(required = false) String createdAtMin,
                                              @RequestParam(required = false) String createdAtMax,
                                              @RequestParam(required = false) Integer limit) {
        List<ProductDTO> productList = _productService.getProducts(categoryId, createdAtMin, createdAtMax, limit);
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
        List<ProductVariantDTO> productList = _productService.getVariants(productId);
        return new ResponseEntity<>(productList, HttpStatus.OK);
    }



    @GetMapping("/products/{productId}")
    @PreAuthorize("hasRole('ADMIN') or ((hasRole('OWNER') or hasRole('REGULAR')) and @productService.productIsOwnedByCurrentUser(#productId))")
    ResponseEntity<ProductDTO> getProduct(@PathVariable int productId) {
        ProductDTO product = _productService.getProduct(productId);
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
        ProductVariantDTO productVariant = _productService.getVariant(variantId);
        return new ResponseEntity<>(productVariant, HttpStatus.OK);
    }

    @PutMapping("/products/{productId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('OWNER') and @productService.productIsOwnedByCurrentUser(#productId))")
    ResponseEntity<ProductDTO> updateProduct(@PathVariable int productId, @RequestBody ProductDTO request) {
        _productService.updateProduct(productId, request);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @PutMapping("/variants/{variantId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('OWNER') and @productService.variantIsOwnedByCurrentUser(#variantId))")
    ResponseEntity<ProductVariantDTO> updateVariant(@PathVariable int variantId, @RequestBody ProductVariantDTO request) {
        _productService.updateVariant(variantId, request);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/products/{productId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('OWNER') and @productService.productIsOwnedByCurrentUser(#productId))")
    ResponseEntity<ProductDTO> deleteProduct(@PathVariable int productId) {
        _productService.deleteProduct(productId);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/variants/{variantId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('OWNER') and @productService.variantIsOwnedByCurrentUser(#variantId))")
    ResponseEntity<ProductVariantDTO> deleteVariant(@PathVariable int variantId) {
        _productService.deleteVariant(variantId);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/products/{productId}/tax/{taxId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('OWNER') and @productService.productIsOwnedByCurrentUser(#productId))")
    ResponseEntity<ProductDTO> updateTax(@PathVariable int productId, @PathVariable int taxId) {
        _productService.applyTax(productId, taxId);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }
}