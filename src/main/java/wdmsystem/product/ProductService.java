package wdmsystem.product;

import wdmsystem.auth.CustomUserDetails;
import wdmsystem.exception.InvalidInputException;
import wdmsystem.exception.NotFoundException;
import wdmsystem.merchant.IMerchantRepository;
import wdmsystem.tax.ITaxRepository;
import wdmsystem.tax.Tax;
import wdmsystem.utility.DTOMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Limit;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {
    private final IProductRepository productRepository;
    private final IProductVariantRepository productVariantRepository;
    private final ITaxRepository taxRepository;
    private final DTOMapper dtoMapper;
    private final IMerchantRepository merchantRepository;

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

        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        product.merchant = merchantRepository.findById(currentUser.getMerchantId()).orElseThrow(() ->
                new NotFoundException("Merchant with id " + currentUser.getMerchantId() + " not found"));

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

        List<Product> filteredProducts = filterProducts(categoryId, createdAtMin, createdAtMax, limit);

        List<ProductDTO> productDTOs = new ArrayList<>();

        for(Product product : filteredProducts) {
            productDTOs.add(dtoMapper.Product_ModelToDTO(product));
        }

        return productDTOs;
    }

    private List<Product> filterProducts(Integer categoryId, String createdAtMin, String createdAtMax, Integer limit){
        List<Product> filteredProducts;
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        boolean isAdmin = currentUser.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        if(isAdmin) {
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
        } else {
            if(categoryId == null) {
                filteredProducts = productRepository.getProductsByCreatedAtGreaterThanEqualAndCreatedAtLessThanEqualAndMerchantId(
                        LocalDateTime.parse(createdAtMin),
                        LocalDateTime.parse(createdAtMax),
                        Limit.of(limit),
                        currentUser.getMerchantId()
                );
            }
            else {
                filteredProducts = productRepository.getProductsByCategoryIdAndCreatedAtGreaterThanEqualAndCreatedAtLessThanEqualAndMerchantId(
                        categoryId,
                        LocalDateTime.parse(createdAtMin),
                        LocalDateTime.parse(createdAtMax),
                        Limit.of(limit),
                        currentUser.getMerchantId()
                );
            }
        }
        return filteredProducts;
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

    public void applyTax(int productId, int taxId) {
        Product product = productRepository.findById(productId).orElseThrow(() ->
                new NotFoundException("Product with id " + productId + " not found"));

        Tax tax = taxRepository.findById(taxId).orElseThrow(() ->
                new NotFoundException("Tax with id " + taxId + " not found"));

        product.setTax(tax);
        productRepository.save(product);
    }

    public boolean productIsOwnedByCurrentUser(int productId) {
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        Product product = productRepository.findById(productId).orElseThrow(() ->
                new NotFoundException("Product with id " + productId + " not found"));
        return product.getMerchant().getId() == currentUser.getMerchantId();
    }

    public boolean variantIsOwnedByCurrentUser(int variantId) {
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        ProductVariant variant = productVariantRepository.findById(variantId).orElseThrow(() ->
                new NotFoundException("Variant with id " + variantId + " not found"));
        return variant.getProduct().getMerchant().getId() == currentUser.getMerchantId();
	}
}
