package wdmsystem.product;

public record ProductDTO(Integer id, Integer merchantId, String title, Integer categoryId, double price, Integer discountId, Integer taxId, float weight, String weightUnit) {
}