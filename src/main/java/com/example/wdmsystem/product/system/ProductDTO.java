package com.example.wdmsystem.product.system;

import com.example.wdmsystem.merchant.system.Merchant;

public record ProductDTO(Integer id, Merchant merchant, String title, int categoryId, double price, int discountId, int taxId, float weight, String weightUnit) {
}
