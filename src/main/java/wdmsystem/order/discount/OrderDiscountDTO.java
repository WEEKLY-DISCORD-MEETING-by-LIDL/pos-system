package wdmsystem.order.discount;

import java.time.LocalDateTime;

public record OrderDiscountDTO(
        Integer id,
        String title,
        Double percentage,
        LocalDateTime expiresOn
) {
}
