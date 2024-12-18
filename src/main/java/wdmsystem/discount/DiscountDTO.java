package wdmsystem.discount;

import java.time.LocalDateTime;

public record DiscountDTO (int id, String title, Double percentage, LocalDateTime expiresOn){
}
