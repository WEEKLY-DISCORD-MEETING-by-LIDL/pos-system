package com.example.wdmsystem.order.system;

import com.example.wdmsystem.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class OrderService {
    private final IOrderRepository orderRepository;
    private final IOrderItemRepository orderItemRepository;

    public OrderService(IOrderRepository orderRepository, IOrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public Order createOrder(int orderDiscountId, List<OrderItem> orderItems) {

        if (orderDiscountId < 0) {
            throw new InvalidInputException("Order discount id must be greater than or equal to 0. Current is: " + orderDiscountId);
        }

        Order order = new Order(
                0,
                10, //placeholder
                orderDiscountId,
                OrderStatus.OPENED,
                0, //placeholder
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Order savedOrder = orderRepository.save(order);

        for (OrderItem item : orderItems) {

            if (item.productVariantId < 0) {
                throw new InvalidInputException("Order item product variant id must be greater than 0. Current is: " + item.productVariantId);
            }
            if (item.quantity <= 0) {
                throw new InvalidInputException("Order item quantity must be greater than 0. Current is: " + item.quantity);
            }

            item.orderId = savedOrder.id;
        }

        orderItemRepository.saveAll(orderItems);

        return savedOrder;
    }

    public Order getOrder(int orderId) {

        Optional<Order> order = orderRepository.findById(orderId);

        if (order.isPresent()) {
            return order.get();
        }
        else {
            throw new NotFoundException("Order with id " + orderId + " not found");
        }
    }

    public Order updateOrderStatus(int orderId, String statusString) {

        Optional<Order> order = orderRepository.findById(orderId);

        OrderStatus status;

        try {
            status = OrderStatus.valueOf(statusString);
        } catch (IllegalArgumentException e) {
            throw new InvalidInputException("Order status must be one of " + Arrays.asList(OrderStatus.values()));
        }

        if (order.isPresent()) {
            Order orderToUpdate = order.get();
            orderToUpdate.status = status;
            orderToUpdate.updatedAt = LocalDateTime.now();
            return orderRepository.save(orderToUpdate);
        }
        else {
            throw new NotFoundException("Order with id " + orderId + " not found");
        }
    }

    public void deleteOrder(int orderId) {

        Optional<Order> order = orderRepository.findById(orderId);

        if (order.isPresent()) {
            Order orderToDelete = order.get();

            List<OrderItem> orderItems = orderItemRepository.getOrderItemsByOrderId(orderToDelete.id);
            orderItemRepository.deleteAll(orderItems);

            orderRepository.delete(orderToDelete);
        }
        else {
            throw new NotFoundException("Order with id " + orderId + " not found");
        }

    }

    public Order cancelOrder(int orderId) {

        Optional<Order> order = orderRepository.findById(orderId);

        if (order.isPresent()) {
            Order orderToUpdate = order.get();
            if (orderToUpdate.status != OrderStatus.PAID && orderToUpdate.status != OrderStatus.PARTIALLY_PAID) {
                orderToUpdate.status = OrderStatus.CANCELLED;
                orderToUpdate.updatedAt = LocalDateTime.now();
                orderRepository.save(orderToUpdate);

                return orderToUpdate;
            }
            else {
                throw new InsufficientPrivilegesException("Order with id " + orderId + " has already been paid for.");
            }
        }
        else {
            throw new NotFoundException("Order with id " + orderId + " not found");
        }
    }

    public Order applyDiscountToOrder(int orderId, int discountId) {

        Optional<Order> order = orderRepository.findById(orderId);

        // maybe change this to check if order discount with this id even exists
        if (discountId < 0) {
            throw new InvalidInputException("Order discount id must be greater than 0. Current is: " + discountId);
        }

        if (order.isPresent()) {
            Order orderToUpdate = order.get();
            orderToUpdate.orderDiscountId = discountId;
            orderToUpdate.updatedAt = LocalDateTime.now();
            orderRepository.save(orderToUpdate);
            return orderToUpdate;
        }
        else {
            throw new NotFoundException("Order with id " + orderId + " not found");
        }

    }
}
