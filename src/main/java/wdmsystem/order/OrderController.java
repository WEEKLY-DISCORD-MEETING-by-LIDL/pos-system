package wdmsystem.order;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import wdmsystem.order.summary.OrderSummary;

import java.util.List;

@RestController
public class OrderController {
    private final OrderService _orderService;

    public OrderController(OrderService orderService) {
        this._orderService = orderService;
    }

    @PostMapping("/orders")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER') or hasRole('REGULAR')")
    ResponseEntity<OrderDTO> createOrder(@RequestParam(required = false) Integer orderDiscountId, @RequestBody List<OrderItemDTO> orderItems) {
        OrderDTO newOrder = _orderService.createOrder(orderDiscountId, orderItems);
        return new ResponseEntity<>(newOrder, HttpStatus.CREATED);
    }

    //new method
    @GetMapping("/orders")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER') or hasRole('REGULAR')")
    ResponseEntity<List<OrderDTO>> getOrders(@RequestParam(required = false) OrderStatus status,
                                             @RequestParam(required = false) String createdAtMin,
                                             @RequestParam(required = false) String createdAtMax,
                                             @RequestParam(required = false) Integer limit) {
        List<OrderDTO> orders = _orderService.getOrders(status, createdAtMin, createdAtMax, limit);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/orders/{orderId}")
    @PreAuthorize("hasRole('ADMIN') or ((hasRole('OWNER') or hasRole('REGULAR')) and @orderService.isOwnedByCurrentUser(#orderId))")
    ResponseEntity<OrderDTO> getOrder(@PathVariable int orderId) {
        OrderDTO order = _orderService.getOrder(orderId);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PutMapping("/orders/{orderId}")
    @PreAuthorize("hasRole('ADMIN') or ((hasRole('OWNER') or hasRole('REGULAR')) and @orderService.isOwnedByCurrentUser(#orderId))")
    ResponseEntity<OrderDTO> updateOrderStatus(@PathVariable int orderId,
                                            @RequestParam OrderStatus status) {
        OrderDTO order = _orderService.updateOrderStatus(orderId, status);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @DeleteMapping("/orders/{orderId}")
    @PreAuthorize("hasRole('ADMIN') or ((hasRole('OWNER') or hasRole('REGULAR')) and @orderService.isOwnedByCurrentUser(#orderId))")
    ResponseEntity<Order> deleteOrder(@PathVariable int orderId) {
        _orderService.deleteOrder(orderId);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/orders/{orderId}/cancel")
    @PreAuthorize("hasRole('ADMIN') or ((hasRole('OWNER') or hasRole('REGULAR')) and @orderService.isOwnedByCurrentUser(#orderId))")
    ResponseEntity<OrderDTO> cancelOrder(@PathVariable int orderId) {
        OrderDTO order = _orderService.cancelOrder(orderId);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PatchMapping("/orders/{orderId}/discount")
    @PreAuthorize("hasRole('ADMIN') or ((hasRole('OWNER') or hasRole('REGULAR')) " +
            "and (@orderService.isOwnedByCurrentUser(#orderId) and @orderDiscountService.isOwnedByCurrentUser(#discountId)))")
    ResponseEntity<OrderDTO> applyDiscountToOrder(@PathVariable int orderId, @RequestParam int discountId) {
        OrderDTO order = _orderService.applyDiscountToOrder(orderId, discountId);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    //new
    @GetMapping("/orders/{orderId}/price")
    @PreAuthorize("hasRole('ADMIN') or ((hasRole('OWNER') or hasRole('REGULAR')) and @orderService.isOwnedByCurrentUser(#orderId))")
    public ResponseEntity<Double> getPrice(@PathVariable int orderId) {
        double price = _orderService.getPrice(orderId);
        return new ResponseEntity<>(price, HttpStatus.OK);
    }

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
        List<OrderItemDTO> orderItems = _orderService.getOrderItems(orderId);
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
