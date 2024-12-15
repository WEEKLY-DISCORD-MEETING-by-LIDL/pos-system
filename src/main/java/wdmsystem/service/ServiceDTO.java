package wdmsystem.service;

public record ServiceDTO(String title, Integer categoryId, Double price, Integer discountId, Integer taxId, Integer durationMins) {
}
