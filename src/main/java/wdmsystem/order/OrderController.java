package wdmsystem.order;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import wdmsystem.order.summary.OrderSummary;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RestController
public class OrderController {
    private final OrderService _orderService;

    public OrderController(OrderService orderService) {
        this._orderService = orderService;
    }

    @PostMapping("/orders")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER') or hasRole('REGULAR')")
    ResponseEntity<OrderDTO> createOrder(@RequestParam(required = false) Integer orderDiscountId, @RequestBody List<OrderItemDTO> orderItems) {
        log.info("Creating new order with {} items and discount ID: {}", orderItems.size(), orderDiscountId);
        OrderDTO newOrder = _orderService.createOrder(orderDiscountId, orderItems);
        log.info("Order created successfully with ID: {}", newOrder.id());
        return new ResponseEntity<>(newOrder, HttpStatus.CREATED);
    }

    //new method
    @GetMapping("/orders")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER') or hasRole('REGULAR')")
    ResponseEntity<List<OrderDTO>> getOrders(@RequestParam(required = false) OrderStatus status,
                                             @RequestParam(required = false) String createdAtMin,
                                             @RequestParam(required = false) String createdAtMax,
                                             @RequestParam(required = false) Integer limit) {
        log.info("Fetching orders with status: {}, createdAtMin: {}, createdAtMax: {}, limit: {}", status, createdAtMin, createdAtMax, limit);
        List<OrderDTO> orders = _orderService.getOrders(status, createdAtMin, createdAtMax, limit);
        log.info("Fetched {} orders", orders.size());
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/orders/{orderId}")
    @PreAuthorize("hasRole('ADMIN') or ((hasRole('OWNER') or hasRole('REGULAR')) and @orderService.isOwnedByCurrentUser(#orderId))")
    ResponseEntity<OrderDTO> getOrder(@PathVariable int orderId) {
        log.info("Fetching order with ID: {}", orderId);
        OrderDTO order = _orderService.getOrder(orderId);
        log.info("Fetched order: {}", order);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PutMapping("/orders/{orderId}")
    @PreAuthorize("hasRole('ADMIN') or ((hasRole('OWNER') or hasRole('REGULAR')) and @orderService.isOwnedByCurrentUser(#orderId))")
    ResponseEntity<OrderDTO> updateOrderStatus(@PathVariable int orderId,
                                               @RequestParam OrderStatus status) {
        log.info("Updating status of order with ID: {} to {}", orderId, status);
        OrderDTO order = _orderService.updateOrderStatus(orderId, status);
        log.info("Updated order status successfully: {}", order);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @DeleteMapping("/orders/{orderId}")
    @PreAuthorize("hasRole('ADMIN') or ((hasRole('OWNER') or hasRole('REGULAR')) and @orderService.isOwnedByCurrentUser(#orderId))")
    ResponseEntity<Order> deleteOrder(@PathVariable int orderId) {
        log.info("Deleting order with ID: {}", orderId);
        _orderService.deleteOrder(orderId);
        log.info("Order with ID: {} deleted successfully", orderId);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/orders/{orderId}/cancel")
    @PreAuthorize("hasRole('ADMIN') or ((hasRole('OWNER') or hasRole('REGULAR')) and @orderService.isOwnedByCurrentUser(#orderId))")
    ResponseEntity<OrderDTO> cancelOrder(@PathVariable int orderId) {
        log.info("Cancelling order with ID: {}", orderId);
        OrderDTO order = _orderService.cancelOrder(orderId);
        log.info("Order with ID: {} cancelled successfully", orderId);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PatchMapping("/orders/{orderId}/discount")
    @PreAuthorize("hasRole('ADMIN') or ((hasRole('OWNER') or hasRole('REGULAR')) " +
            "and (@orderService.isOwnedByCurrentUser(#orderId) and @orderDiscountService.isOwnedByCurrentUser(#discountId)))")
    ResponseEntity<OrderDTO> applyDiscountToOrder(@PathVariable int orderId, @RequestParam int discountId) {
        log.info("Applying discount with ID: {} to order with ID: {}", discountId, orderId);
        OrderDTO order = _orderService.applyDiscountToOrder(orderId, discountId);
        log.info("Applied discount successfully to order: {}", order);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    //new
    @GetMapping("/orders/{orderId}/price")
    @PreAuthorize("hasRole('ADMIN') or ((hasRole('OWNER') or hasRole('REGULAR')) and @orderService.isOwnedByCurrentUser(#orderId))")
    public ResponseEntity<Double> getPrice(@PathVariable int orderId) {
        log.info("Fetching price for order with ID: {}", orderId);
        double price = _orderService.getPrice(orderId);
        log.info("Fetched price for order with ID {}: {}", orderId, price);
        return new ResponseEntity<>(price, HttpStatus.OK);
    }

    //new
    @GetMapping("/orders/{orderId}/unpaid-price")
    @PreAuthorize("hasRole('ADMIN') or ((hasRole('OWNER') or hasRole('REGULAR')) and @orderService.isOwnedByCurrentUser(#orderId))")
    public ResponseEntity<Double> getUnpaidPrice(@PathVariable int orderId) {
        double price = _orderService.getUnpaidPrice(orderId);
        return new ResponseEntity<>(price, HttpStatus.OK);
    }

    //new method
    @GetMapping("/orders/{orderId}/items")
    @PreAuthorize("hasRole('ADMIN') or ((hasRole('OWNER') or hasRole('REGULAR')) and @orderService.isOwnedByCurrentUser(#orderId))")
    ResponseEntity<List<OrderItemDTO>> getOrderItems(@PathVariable int orderId) {
        log.info("Fetching items for order with ID: {}", orderId);
        List<OrderItemDTO> orderItems = _orderService.getOrderItems(orderId);
        log.info("Fetched {} items for order with ID: {}", orderItems.size(), orderId);
        return new ResponseEntity<>(orderItems, HttpStatus.OK);
    }

    //new method
    @GetMapping("/orders/{orderId}/summary")
    @PreAuthorize("hasRole('ADMIN') or ((hasRole('OWNER') or hasRole('REGULAR')) and @orderService.isOwnedByCurrentUser(#orderId))")
    ResponseEntity<OrderSummary> getOrderSummary(@PathVariable int orderId) {
        OrderSummary summary = _orderService.getOrderSummary(orderId);
        return new ResponseEntity<>(summary, HttpStatus.OK);
    }

    //new method
    @PostMapping("/orders/{orderId}/archive")
    @PreAuthorize("hasRole('ADMIN') or ((hasRole('OWNER') or hasRole('REGULAR')) and @orderService.isOwnedByCurrentUser(#orderId))")
    ResponseEntity<OrderDTO> archiveOrder(@PathVariable int orderId) {
        _orderService.archiveOrder(orderId);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    //new method
    @GetMapping("/orders/archive/{archivedOrderId}")
    //pre authorize archivedOrderId
    ResponseEntity<OrderSummary> archiveOrders(@PathVariable int archivedOrderId) {
        OrderSummary summary = _orderService.getArchivedOrder(archivedOrderId);
        return new ResponseEntity<>(summary, HttpStatus.OK);
    }

    //new
    @PatchMapping("/orders/{orderId}/validate-payments")
    @PreAuthorize("hasRole('ADMIN') or ((hasRole('OWNER') or hasRole('REGULAR')) and @orderService.isOwnedByCurrentUser(#orderId))")
    ResponseEntity<OrderDTO> validatePaymentsAndUpdateOrderStatus(@PathVariable int orderId) {
        OrderDTO order = _orderService.validatePaymentsAndUpdateOrderStatus(orderId);
        if(order != null) {
            return new ResponseEntity<>(order, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
    }
}
