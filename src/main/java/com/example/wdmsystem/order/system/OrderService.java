package com.example.wdmsystem.order.system;

import com.example.wdmsystem.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class OrderService {
    private final IOrderRepository orderRepository;
    private final IOrderItemRepository orderItemRepository;
    private final IOrderDiscountRepository orderDiscountRepository;

    public OrderService(IOrderRepository orderRepository, IOrderItemRepository orderItemRepository, IOrderDiscountRepository orderDiscountRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.orderDiscountRepository = orderDiscountRepository;
    }

    public Order createOrder(Integer orderDiscountId, List<OrderItem> orderItems) {

        OrderDiscount orderDiscount;

        if (orderDiscountId != null) {
            orderDiscount = orderDiscountRepository.findById(orderDiscountId).orElseThrow(() ->
                    new NotFoundException("Order discount with id " + orderDiscountId + " not found"));
        }
        else {
            orderDiscount = null;
        }

        Order order = new Order(
                0,
                10, //placeholder
                orderDiscount,
                OrderStatus.OPENED,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Order savedOrder = orderRepository.save(order);

        for (OrderItem item : orderItems) {

//            if (item.productVariantId < 0) {
//                throw new InvalidInputException("Order item product variant id must be greater than 0. Current is: " + item.productVariantId);
//            }
            if (item.quantity <= 0) {
                throw new InvalidInputException("Order item quantity must be greater than 0. Current is: " + item.quantity);
            }

            item.order = savedOrder;
        }

        orderItemRepository.saveAll(orderItems);

        return savedOrder;
    }

    public Order getOrder(int orderId) {
        return orderRepository.findById(orderId).orElseThrow(() ->
                new NotFoundException("Order with id " + orderId + " not found"));
    }

    public Order updateOrderStatus(int orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new NotFoundException("Order with id " + orderId + " not found"));

        order.status = status;
        order.updatedAt = LocalDateTime.now();
        return orderRepository.save(order);
    }

    public void deleteOrder(int orderId) {
        if(orderRepository.existsById(orderId)) {
            orderRepository.deleteById(orderId);
        }
        else {
            throw new NotFoundException("Order with id " + orderId + " not found");
        }
    }

    public Order cancelOrder(int orderId) {

        Order orderToUpdate = orderRepository.findById(orderId).orElseThrow(() ->
                new NotFoundException("Order with id " + orderId + " not found"));

        if (orderToUpdate.status != OrderStatus.PAID && orderToUpdate.status != OrderStatus.PARTIALLY_PAID) {
            orderToUpdate.status = OrderStatus.CANCELED;
            orderToUpdate.updatedAt = LocalDateTime.now();
            return orderRepository.save(orderToUpdate);
        }
        else {
            throw new InsufficientPrivilegesException("Order with id " + orderId + " has already been paid for.");
        }
    }

    public Order applyDiscountToOrder(int orderId, int discountId) {

        Order orderToUpdate = orderRepository.findById(orderId).orElseThrow(() ->
                new NotFoundException("Order with id " + orderId + " not found"));

        orderToUpdate.orderDiscount = orderDiscountRepository.findById(discountId).orElseThrow(() ->
                new NotFoundException("Order discount with id " + discountId + " not found"));

        orderToUpdate.updatedAt = LocalDateTime.now();
        return orderRepository.save(orderToUpdate);

    }
}
