package wdmsystem.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.PageRequest;
import wdmsystem.auth.CustomUserDetails;
import wdmsystem.exception.InsufficientPrivilegesException;
import wdmsystem.exception.InvalidInputException;
import wdmsystem.exception.NotFoundException;
import wdmsystem.merchant.IMerchantRepository;
import wdmsystem.merchant.Merchant;
import wdmsystem.order.discount.IOrderDiscountRepository;
import wdmsystem.order.discount.OrderDiscount;
import wdmsystem.order.summary.*;
import wdmsystem.payment.Payment;
import wdmsystem.utility.DTOMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final IArchivedOrderRepository archivedOrderRepository;
    private final IMerchantRepository merchantRepository;
    private final DTOMapper dtoMapper;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public OrderService(IOrderRepository orderRepository, IMerchantRepository merchantRepository, IOrderItemRepository orderItemRepository, IOrderDiscountRepository orderDiscountRepository, IArchivedOrderRepository archivedOrderRepository, DTOMapper dtoMapper) {
        this.orderRepository = orderRepository;
        this.merchantRepository = merchantRepository;
        this.orderItemRepository = orderItemRepository;
        this.orderDiscountRepository = orderDiscountRepository;
        this.archivedOrderRepository = archivedOrderRepository;
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

        List<Order> filteredOrders = filterOrders(orderStatus, createdAtMin, createdAtMax, limit);
        List<OrderDTO> orderDTOs = new ArrayList<>();

        for (Order order : filteredOrders) {
            orderDTOs.add(dtoMapper.Order_ModelToDTO(order));
        }

        return orderDTOs;
    }

    private List<Order> filterOrders(OrderStatus orderStatus, String createdAtMin, String createdAtMax, Integer limit) {
        List<Order> filteredOrders;
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        boolean isAdmin = currentUser.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        if (isAdmin) {
            if(orderStatus == null) {
                filteredOrders = orderRepository.getOrdersByCreatedAtGreaterThanEqualAndCreatedAtLessThanEqual(
                        LocalDateTime.parse(createdAtMin),
                        LocalDateTime.parse(createdAtMax),
                        PageRequest.of(0, limit)
                );
            }
            else {
                filteredOrders = orderRepository.getOrdersByStatusAndCreatedAtGreaterThanEqualAndCreatedAtLessThanEqual(
                        orderStatus,
                        LocalDateTime.parse(createdAtMin),
                        LocalDateTime.parse(createdAtMax),
                        PageRequest.of(0, limit)
                );
            }
        }else {
            if(orderStatus == null) {
                filteredOrders = orderRepository.getOrdersByCreatedAtGreaterThanEqualAndCreatedAtLessThanEqualAndMerchantId(
                        LocalDateTime.parse(createdAtMin),
                        LocalDateTime.parse(createdAtMax),
                        currentUser.getMerchantId(),
                        PageRequest.of(0, limit)
                );
            }
            else {
                filteredOrders = orderRepository.getOrdersByStatusAndCreatedAtGreaterThanEqualAndCreatedAtLessThanEqualAndMerchantId(
                        orderStatus,
                        LocalDateTime.parse(createdAtMin),
                        LocalDateTime.parse(createdAtMax),
                        currentUser.getMerchantId(),
                        PageRequest.of(0, limit)
                );
            }
        }
        return filteredOrders;
    }

    public OrderDTO getOrder(int orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new NotFoundException("Order with id " + orderId + " not found"));

        return dtoMapper.Order_ModelToDTO(order);
    }

    public OrderDTO updateOrderStatus(int orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new NotFoundException("Order with id " + orderId + " not found"));

        if(status != null) order.status = status;
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

        if(orderToUpdate.orderDiscount.expiresOn.isAfter(LocalDateTime.now())){
            orderToUpdate.orderDiscount = orderDiscountRepository.findById(discountId).orElseThrow(() ->
                    new NotFoundException("Order discount with id " + discountId + " not found"));

            orderToUpdate.updatedAt = LocalDateTime.now();
            Order savedOrder = orderRepository.save(orderToUpdate);
            return dtoMapper.Order_ModelToDTO(savedOrder);
        } else {
            throw new InvalidInputException("Order discount " + discountId + " has expired.");
        }
    }

    public double getPrice(int orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order with id " + orderId + " not found"));

        return order.getOrderItems()
                .stream()
                .mapToDouble(OrderItem::getTotalPrice)
                .sum();
    }

    public double getUnpaidPrice(int orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order with id " + orderId + " not found"));

        double price = order.getOrderItems()
                .stream()
                .mapToDouble(OrderItem::getTotalPrice)
                .sum();

        double totalAmountPaid = 0;

        for(Payment payment : order.getPayments()) {
            totalAmountPaid += payment.totalAmount;
        }

        return price - totalAmountPaid;
    }

    //new
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

    //new
    public OrderSummary getOrderSummary(int orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new NotFoundException("Order with id " + orderId + " not found."));

        return fillOutOrderSummary(order);
    }

    //new
    public void archiveOrder(int orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new NotFoundException("Order with id " + orderId + " not found."));

        OrderSummary summary = fillOutOrderSummary(order);
        String json;
        try {
            json = objectMapper.writeValueAsString(summary);
        } catch (JsonProcessingException e) {
            throw new InvalidInputException("Failed to archive order: " + e.getMessage());
        }

        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        Merchant merchant = merchantRepository.findById(currentUser.getMerchantId()).orElseThrow(() ->
                new NotFoundException("Merchant with id " + currentUser.getMerchantId() + " not found"));

        archivedOrderRepository.save(new ArchivedOrder(0, merchant, json, LocalDateTime.now()));
    }

    //new
    public OrderSummary getArchivedOrder(int archivedOrderId) {
        ArchivedOrder archivedOrder = archivedOrderRepository.findById(archivedOrderId).orElseThrow(() ->
                new NotFoundException("Archived order with id " + archivedOrderId + " not found"));

        OrderSummary summary;
        try {
            summary = objectMapper.readValue(archivedOrder.orderSummaryJson, OrderSummary.class);
        } catch (JsonProcessingException e) {
            throw new InvalidInputException("Failed to get archived order: " + e.getMessage());
        }

        return summary;
    }

    //new
    public OrderDTO validatePaymentsAndUpdateOrderStatus(int orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new NotFoundException("Order with id " + orderId + " not found"));

        double price = order.getOrderItems()
                .stream()
                .mapToDouble(OrderItem::getTotalPrice)
                .sum();

        double totalAmountPaid = 0;

        for(Payment payment : order.getPayments()) {
            totalAmountPaid += payment.totalAmount;
        }

        if(totalAmountPaid < price && totalAmountPaid > 0) {
            order.status = OrderStatus.PARTIALLY_PAID;
            return dtoMapper.Order_ModelToDTO(orderRepository.save(order));
        }
        else if(totalAmountPaid >= price) {
            order.status = OrderStatus.PAID;
            return dtoMapper.Order_ModelToDTO(orderRepository.save(order));
        }

        return null;
    }

    private OrderSummary fillOutOrderSummary(Order order) {
        OrderDiscountSummary orderDiscountSummary = (order.orderDiscount == null ? null : new OrderDiscountSummary(order.orderDiscount.title, order.orderDiscount.percentage));

        List<OrderItemSummary> itemSummaries = new ArrayList<>();

        for(OrderItem item : order.getOrderItems()) {

            TaxSummary taxSummary = (item.productVariant.product.tax == null ? null : new TaxSummary(item.productVariant.product.tax.title, item.productVariant.product.tax.percentage));
            ProductDiscountSummary productDiscountSummary = (item.productVariant.product.discount == null ? null : new ProductDiscountSummary(item.productVariant.product.discount.title, item.productVariant.product.discount.percentage));

            ProductSummary productSummary = new ProductSummary(item.productVariant.product.title, item.productVariant.product.price, taxSummary, item.productVariant.product.weight, item.productVariant.product.weightUnit, productDiscountSummary);
            ProductVariantSummary variantSummary = new ProductVariantSummary(item.productVariant.title, item.productVariant.additionalPrice);

            OrderItemSummary itemSummary = new OrderItemSummary(item.quantity, item.getTotalPrice(), productSummary, variantSummary);

            itemSummaries.add(itemSummary);
        }

        return new OrderSummary(orderDiscountSummary, order.status, itemSummaries);
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
