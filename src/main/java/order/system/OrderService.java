package order.system;

import org.springframework.http.HttpStatus;

public class OrderService {

    public OrderService() {}

    public Order createOrder(OrderDTO order){
        return null;
    }

    public Order getOrder(int orderId) {
        return null;
    }

    public Order updateOrderStatus(int orderId, OrderStatus status) {
        return null;
    }

    public HttpStatus deleteOrder(int orderId) {
        return HttpStatus.NO_CONTENT;
    }
    public HttpStatus cancelOrder(int orderId) {
        return HttpStatus.OK;
    }

    public Order applyDiscountToOrder(int orderId) {
        return null;
    }
}
