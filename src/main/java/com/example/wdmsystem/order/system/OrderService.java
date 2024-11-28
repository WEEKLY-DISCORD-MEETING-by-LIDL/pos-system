package com.example.wdmsystem.order.system;

import com.example.wdmsystem.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    public OrderService() {}

    public Order createOrder(OrderDTO order){
        return null;
    }

    public Order getOrder(int orderId) {

        switch (orderId) {
            case 1:
                throw new InvalidInputException("INVALID INPUT");
            case 2:
                throw new NotFoundException("NOT FOUND");
            case 3:
                throw new ConflictException("Conflict");
            case 4:
                throw new UnauthorizedException("UNAUTHROIZED");
            case 5:
                throw new InsufficientPrivilegesException("insufficient privileges");
            default:
                break;
        }



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
