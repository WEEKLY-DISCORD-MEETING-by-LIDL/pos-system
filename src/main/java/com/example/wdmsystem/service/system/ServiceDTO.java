package com.example.wdmsystem.service.system;

public record ServiceDTO(Integer merchantId, String title, Integer categoryId, Double price, Integer discountId, Integer taxId, Integer durationMins) {
}
