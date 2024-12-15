package com.example.wdmsystem.utility;

import com.example.wdmsystem.auth.CustomUserDetails;
import com.example.wdmsystem.category.system.Category;
import com.example.wdmsystem.category.system.CategoryDTO;
import com.example.wdmsystem.category.system.ICategoryRepository;
import com.example.wdmsystem.customer.system.Customer;
import com.example.wdmsystem.customer.system.CustomerDTO;
import com.example.wdmsystem.employee.system.EmployeeDTO;
import com.example.wdmsystem.employee.system.Employee;
import com.example.wdmsystem.employee.system.IEmployeeRepository;
import com.example.wdmsystem.employee.system.UpdateEmployeeDTO;
import com.example.wdmsystem.exception.NotFoundException;
import com.example.wdmsystem.merchant.system.IMerchantRepository;
import com.example.wdmsystem.merchant.system.Merchant;
import com.example.wdmsystem.order.system.*;
import com.example.wdmsystem.payment.system.Payment;
import com.example.wdmsystem.payment.system.PaymentDTO;
import com.example.wdmsystem.product.system.*;
import com.example.wdmsystem.reservation.system.Reservation;
import com.example.wdmsystem.reservation.system.ReservationDTO;
import com.example.wdmsystem.service.system.ServiceDTO;
import com.example.wdmsystem.tax.system.ITaxRepository;
import com.example.wdmsystem.tax.system.Tax;
import com.example.wdmsystem.tax.system.TaxDTO;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import com.example.wdmsystem.service.system.Service;


@org.springframework.stereotype.Service
@AllArgsConstructor
public class DTOMapper {
    IOrderRepository orderRepository;
    IOrderItemRepository orderItemRepository;
    IProductRepository productRepository;
    IProductVariantRepository productVariantRepository;
    IEmployeeRepository employeeRepository;
    ITaxRepository taxRepository;
    ICategoryRepository categoryRepository;
    IMerchantRepository merchantRepository;

    /// ORDER
    public OrderDTO Order_ModelToDTO(Order order) {
        return new OrderDTO(order.id, order.merchant.id, (order.orderDiscount == null ? null : order.orderDiscount.id), order.status); // making note for merchantId
    }

    /// PRODUCT
    public ProductDTO Product_ModelToDTO(Product product) {
        return new ProductDTO(product.id, product.merchant.id, product.title, (product.category == null ? null : product.category.id), product.price, product.discountId, (product.tax == null ? null : product.tax.id), product.weight, product.weightUnit);
    }

    public Product Product_DTOToModel(ProductDTO productDTO) {
        Tax tax;
        Category category;

        if (productDTO.taxId() == null) {
            tax = null;
        } else {
            tax = taxRepository.findById(productDTO.taxId()).orElse(null);
        }

        if (productDTO.categoryId() == null) {
            category = null;
        } else {
            category = categoryRepository.findById(productDTO.categoryId()).orElse(null);
        }

        return new Product(productDTO.id(), null, productDTO.title(), category, productDTO.price(), productDTO.discountId(), tax, productDTO.weight(), productDTO.weightUnit());
    }

    /// PRODUCT VARIANT
    public ProductVariantDTO ProductVariant_ModelToDTO(ProductVariant productVariant) {
        return new ProductVariantDTO(productVariant.id, productVariant.product.id, productVariant.title, productVariant.additionalPrice);
    }

    public ProductVariant ProductVariant_DTOToModel(ProductVariantDTO productVariantDTO, Product product) {
        return new ProductVariant(productVariantDTO.id(), product, productVariantDTO.title(), productVariantDTO.additionalPrice());
    }

    /// ORDER ITEMS
    public OrderItem OrderItem_DTOToModel(OrderItemDTO orderItemDTO) {
        Order order = orderRepository.findById(orderItemDTO.orderId()).orElseThrow(() ->
                new NotFoundException("Order not found"));
        ProductVariant productVariant = productVariantRepository.findById(orderItemDTO.productVariantId()).orElseThrow(() ->
                new NotFoundException("Product variant not found"));

        return new OrderItem(orderItemDTO.id(), order, productVariant, orderItemDTO.quantity());
    }

    public OrderItem OrderItem_DTOToModel(OrderItemDTO orderItemDTO, Order order) {
        ProductVariant productVariant = productVariantRepository.findById(orderItemDTO.productVariantId()).orElseThrow(() ->
                new NotFoundException("Product variant not found"));

        return new OrderItem(orderItemDTO.id(), order, productVariant, orderItemDTO.quantity());
    }

    /// EMPLOYEE

    public EmployeeDTO Employee_ModelToDTO(Employee employee) {
        return new EmployeeDTO(employee.firstName, employee.lastName, employee.employeeType, employee.username, "<hidden>");
    }

    public OrderItemDTO OrderItem_ModelToDTO(OrderItem orderItem) {
        return new OrderItemDTO(orderItem.id, orderItem.order.id, orderItem.productVariant.id, orderItem.quantity);
    }

    /// TAX

    public Tax Tax_DTOToModel(TaxDTO taxDTO) {
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        Merchant merchant = merchantRepository.findById(currentUser.getMerchantId()).orElseThrow(() ->
                new NotFoundException("Merchant with id " + currentUser.getMerchantId() + " not found"));
        return new Tax(taxDTO.id(), merchant, taxDTO.title(), taxDTO.percentage());
    }

    public TaxDTO Tax_ModelToDTO(Tax tax) {
        return new TaxDTO(tax.id, tax.merchant.id, tax.title, tax.percentage);
    }

    /// PAYMENT

    public Payment Payment_DTOToModel(PaymentDTO paymentDTO) {
        Order order = orderRepository.findById(paymentDTO.orderId()).orElse(null);
        Reservation reservation = null; //will change once reservations work

        if (order == null && reservation == null) {
            throw new NotFoundException("Payment is not applied to any order or reservation");
        }

        return new Payment(paymentDTO.id(), paymentDTO.tipAmount(), paymentDTO.totalAmount(), paymentDTO.method(), order);
    }

    public PaymentDTO Payment_ModelToDTO(Payment payment) {
        return new PaymentDTO(payment.id, payment.tipAmount, payment.totalAmount, payment.method, (payment.order == null ? null : payment.order.id));
    }

    /// Customer
    public CustomerDTO Customer_ModelToDTO(Customer customer) {
        return new CustomerDTO(customer.firstName, customer.lastName, customer.phone);
    }

    /// Category
    public CategoryDTO Category_ModelToDTO(Category category) {
        return new CategoryDTO(category.title);
    }

    /// Reservation
    public ReservationDTO Reservation_ModelToDTO(Reservation reservation) {
        return new ReservationDTO(reservation.id, reservation.getService().id, reservation.employeeId, reservation.startTime, reservation.sendConfirmation, reservation.endTime, reservation.reservationStatus);
    }

    /// Service
    public ServiceDTO Service_ModelToDTO(Service service) {
        return new ServiceDTO(service.title, service.category.id, service.price, service.discount.id,service.tax.id, service.durationMins);
    }

}
