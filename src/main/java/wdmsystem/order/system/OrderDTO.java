package wdmsystem.order.system;

public record OrderDTO(Integer id, Integer merchantId, Integer orderDiscountId, OrderStatus status) {

}
