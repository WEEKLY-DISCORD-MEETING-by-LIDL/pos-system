package com.example.wdmsystem.order.system;

import com.example.wdmsystem.auth.CustomUserDetails;
import com.example.wdmsystem.exception.*;
import com.example.wdmsystem.merchant.system.IMerchantRepository;
import com.example.wdmsystem.merchant.system.Merchant;
import com.example.wdmsystem.utility.DTOMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.data.domain.Limit;
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
    private final IMerchantRepository merchantRepository;
    private final DTOMapper dtoMapper;

    public OrderService(IOrderRepository orderRepository, IMerchantRepository merchantRepository, IOrderItemRepository orderItemRepository, IOrderDiscountRepository orderDiscountRepository, DTOMapper dtoMapper) {
        this.orderRepository = orderRepository;
        this.merchantRepository = merchantRepository;
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
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        Merchant merchant = merchantRepository.findById(currentUser.getMerchantId()).orElseThrow(() ->
                    new NotFoundException("Merchant with id " + currentUser.getMerchantId() + " not found"));

        Order order = new Order(
                0,
                merchant,
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

    public List<OrderDTO> getOrders(OrderStatus orderStatus, String createdAtMin, String createdAtMax, Integer limit) {
        if(createdAtMin == null) {
            createdAtMin = "0000-01-01T00:00:00.000";
        }
        if(createdAtMax == null) {
            createdAtMax = "9999-01-01T00:00:00.000";
        }
        if (limit == null || limit <= 0) {
            limit = 50;
        }

        List<Order> filteredOrders;

        if(orderStatus == null) {
            filteredOrders = orderRepository.getOrdersByCreatedAtGreaterThanEqualAndCreatedAtLessThanEqual(
                    LocalDateTime.parse(createdAtMin),
                    LocalDateTime.parse(createdAtMax),
                    Limit.of(limit)
            );
        }
        else {
            filteredOrders = orderRepository.getOrdersByStatusAndCreatedAtGreaterThanEqualAndCreatedAtLessThanEqual(
                    orderStatus,
                    LocalDateTime.parse(createdAtMin),
                    LocalDateTime.parse(createdAtMax),
                    Limit.of(limit)
            );
        }

        List<OrderDTO> orderDTOs = new ArrayList<>();

        for (Order order : filteredOrders) {
            orderDTOs.add(dtoMapper.Order_ModelToDTO(order));
        }

        return orderDTOs;
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

    public double getPrice(int orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order with id " + orderId + " not found"));

        return order.getOrderItems()
                .stream()
                .mapToDouble(OrderItem::getTotalPrice)
                .sum();
    }
	
    public List<OrderItemDTO> getOrderItems(int orderId) {

        if(orderRepository.existsById(orderId)) {
            List<OrderItem> orderItems = orderItemRepository.getOrderItemsByOrderId(orderId);
            List<OrderItemDTO> orderItemDTOs = new ArrayList<>();

            for(OrderItem orderItem : orderItems) {
                orderItemDTOs.add(dtoMapper.OrderItem_ModelToDTO(orderItem));
            }

            return orderItemDTOs;
        }
        else {
            throw new NotFoundException("Order with id " + orderId + " not found");
        }
    }
	
	public boolean isOwnedByCurrentUser(int orderId) {
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new NotFoundException("Order with id " + orderId + " not found"));

        return order.getMerchant().getId() == currentUser.getMerchantId();
	}
}
