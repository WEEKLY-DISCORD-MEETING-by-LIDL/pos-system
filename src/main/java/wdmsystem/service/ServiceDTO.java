package wdmsystem.service;

import java.math.BigDecimal;

public record ServiceDTO(Integer id, String title, Integer categoryId, BigDecimal price, Integer discountId, Integer taxId, Integer durationMins) {
}
