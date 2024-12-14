package com.example.wdmsystem.order.system;

import com.example.wdmsystem.exception.*;
import com.example.wdmsystem.utility.DTOMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class OrderService {
    private final IOrderRepository orderRepository;
    private final IOrderItemRepository orderItemRepository;
    private final IOrderDiscountRepository orderDiscountRepository;
    private final DTOMapper dtoMapper;

    public OrderService(IOrderRepository orderRepository, IOrderItemRepository orderItemRepository, IOrderDiscountRepository orderDiscountRepository, DTOMapper dtoMapper) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.orderDiscountRepository = orderDiscountRepository;
        this.dtoMapper = dtoMapper;
    }

    public OrderDTO createOrder(Integer orderDiscountId, List<OrderItemDTO> orderItemDTOs) {

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
        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemDTO item : orderItemDTOs) {

            if (item.quantity() <= 0) {
                throw new InvalidInputException("Order item quantity must be greater than 0. Current is: " + item.quantity());
            }

            orderItems.add(dtoMapper.OrderItem_DTOToModel(item, savedOrder));
        }

        orderItemRepository.saveAll(orderItems);

        return dtoMapper.Order_ModelToDTO(savedOrder);
    }

    public OrderDTO getOrder(int orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new NotFoundException("Order with id " + orderId + " not found"));

        return dtoMapper.Order_ModelToDTO(order);
    }

    public OrderDTO updateOrderStatus(int orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new NotFoundException("Order with id " + orderId + " not found"));

        order.status = status;
        order.updatedAt = LocalDateTime.now();
        Order savedOrder = orderRepository.save(order);

        return dtoMapper.Order_ModelToDTO(savedOrder);
    }

    public void deleteOrder(int orderId) {
        if(orderRepository.existsById(orderId)) {
            orderRepository.deleteById(orderId);
        }
        else {
            throw new NotFoundException("Order with id " + orderId + " not found");
        }
    }

    public OrderDTO cancelOrder(int orderId) {

        Order orderToUpdate = orderRepository.findById(orderId).orElseThrow(() ->
                new NotFoundException("Order with id " + orderId + " not found"));

        if (orderToUpdate.status != OrderStatus.PAID && orderToUpdate.status != OrderStatus.PARTIALLY_PAID) {
            orderToUpdate.status = OrderStatus.CANCELED;
            orderToUpdate.updatedAt = LocalDateTime.now();
            Order savedOrder = orderRepository.save(orderToUpdate);
            return dtoMapper.Order_ModelToDTO(savedOrder);
        }
        else {
            throw new InsufficientPrivilegesException("Order with id " + orderId + " has already been paid for.");
        }
    }

    public OrderDTO applyDiscountToOrder(int orderId, int discountId) {

        Order orderToUpdate = orderRepository.findById(orderId).orElseThrow(() ->
                new NotFoundException("Order with id " + orderId + " not found"));

        orderToUpdate.orderDiscount = orderDiscountRepository.findById(discountId).orElseThrow(() ->
                new NotFoundException("Order discount with id " + discountId + " not found"));

        orderToUpdate.updatedAt = LocalDateTime.now();
        Order savedOrder = orderRepository.save(orderToUpdate);
        return dtoMapper.Order_ModelToDTO(savedOrder);
    }
}
