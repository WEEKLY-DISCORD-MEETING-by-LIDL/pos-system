package order.system;

import org.springframework.http.HttpStatus;

import java.util.List;

public class OrderDiscountService {

    public OrderDiscountService() {

    }

    public OrderDiscount createOrderDiscount(OrderDiscount discount) {
        return null;
    }
    public List<OrderDiscount> getFilteredDiscounts(boolean expired, int limit){
        return null;
    }
    public OrderDiscount getOrderDiscount(int orderDiscountId) {
        return null;
    }

    public OrderDiscount updateOrderDiscount(int orderDiscountId, OrderDiscount discount) {
        return null;
    }

    public HttpStatus deleteOrderDiscount(int orderDiscountId) {
        return null;
    }
}
