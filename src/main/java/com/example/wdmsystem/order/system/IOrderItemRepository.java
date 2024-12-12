package com.example.wdmsystem.order.system;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IOrderItemRepository extends JpaRepository<OrderItem, Integer> {
    List<OrderItem> getOrderItemsByOrderId(int orderId);
}
