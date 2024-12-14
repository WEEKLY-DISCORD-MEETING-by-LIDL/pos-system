package com.example.wdmsystem.product.system;

public record ProductDTO(Integer id, Integer merchantId, String title, int categoryId, double price, int discountId, int taxId, float weight, String weightUnit) {
}
