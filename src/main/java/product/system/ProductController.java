package product.system;

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
    ResponseEntity<Product> createProduct(@RequestBody ProductDTO request) {
        Product newProduct = _productService.createProduct(request);
        return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
    }

    @PostMapping("/products/{productId}/variants")
    ResponseEntity<ProductVariant> createVariant(@PathVariable int productId, @RequestBody VariantDTO request) {
        ProductVariant newVariant = _productService.createVariant(productId, request);
        return new ResponseEntity<>(newVariant, HttpStatus.CREATED);
    }

    @GetMapping("/products")
    ResponseEntity<List<Product>> getProducts(@RequestParam int categoryId, @RequestParam String createdAtMin, @RequestParam String createdAtMax, @RequestParam int limit) {
        List<Product> productList = _productService.getProducts(categoryId, createdAtMin, createdAtMax, limit);
        return new ResponseEntity<>(productList, HttpStatus.OK);
    }

    @GetMapping("/products/{productId}/variants")
    ResponseEntity<List<ProductVariant>> getVariants(@PathVariable int productId) {
        List<ProductVariant> productList = _productService.getVariants(productId);
        return new ResponseEntity<>(productList, HttpStatus.OK);
    }

    @GetMapping("/products/{productId}")
    ResponseEntity<Product> getProduct(@PathVariable int productId) {
        Product product = _productService.getProduct(productId);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping("/variants/{variantId}")
    ResponseEntity<ProductVariant> getVariant(@PathVariable int variantId) {
        ProductVariant productVariant = _productService.getVariant(variantId);
        return new ResponseEntity<>(productVariant, HttpStatus.OK);
    }

    @PutMapping("/products/{productId}")
    ResponseEntity<Product> updateProduct(@PathVariable int productId, @RequestBody ProductDTO request) {
        _productService.updateProduct(productId, request);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PutMapping("/variants/{variantId}")
    ResponseEntity<ProductVariant> updateVariant(@PathVariable int variantId, @RequestBody VariantDTO request) {
        _productService.updateVariant(variantId, request);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @DeleteMapping("/products/{productId}")
    ResponseEntity<Product> deleteProduct(@PathVariable int productId) {
        _productService.deleteProduct(productId);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @DeleteMapping("/products/{productId}/variants/{variantId}")
    ResponseEntity<ProductVariant> deleteVariant(@PathVariable int productId, @PathVariable int variantId) {
        _productService.deleteVariant(productId, variantId);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}