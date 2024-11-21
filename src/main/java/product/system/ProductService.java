package product.system;

import java.util.List;

public class ProductService {

    public ProductService() {

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

    public Product getVariant(int variantId) {
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
