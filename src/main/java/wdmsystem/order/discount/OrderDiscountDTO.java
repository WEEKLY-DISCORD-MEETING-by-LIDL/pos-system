package wdmsystem.order.discount;

import java.time.LocalDateTime;

public record OrderDiscountDTO (int id, String title, Double percentage, LocalDateTime expiresOn){}
