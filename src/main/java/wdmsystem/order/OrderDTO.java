package wdmsystem.order;

public record OrderDTO(Integer id, Integer merchantId, Integer orderDiscountId, OrderStatus status) {

}
