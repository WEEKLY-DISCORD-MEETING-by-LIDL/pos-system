package wdmsystem.service;

public record ServiceDTO(Integer id, String title, Integer categoryId, Double price, Integer discountId, Integer taxId, Integer durationMins) {
}
