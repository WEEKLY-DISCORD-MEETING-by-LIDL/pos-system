package wdmsystem.discount;

import java.time.LocalDateTime;

public record DiscountDTO (String title, Double percentage, LocalDateTime expiresOn){
}
