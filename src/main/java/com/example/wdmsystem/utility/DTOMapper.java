package com.example.wdmsystem.utility;

import com.example.wdmsystem.employee.system.CreateEmployeeDTO;
import com.example.wdmsystem.employee.system.Employee;
import com.example.wdmsystem.employee.system.IEmployeeRepository;
import com.example.wdmsystem.employee.system.UpdateEmployeeDTO;
import com.example.wdmsystem.exception.NotFoundException;
import com.example.wdmsystem.order.system.*;
import com.example.wdmsystem.product.system.*;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DTOMapper {
    private PasswordEncoder passwordEncoder;
    IOrderRepository orderRepository;
    IOrderItemRepository orderItemRepository;
    IProductRepository productRepository;
    IProductVariantRepository productVariantRepository;
    IEmployeeRepository employeeRepository;

    /// ORDER
    public OrderDTO Order_ModelToDTO(Order order) {
        return new OrderDTO(order.id, order.merchant.id, (order.orderDiscount == null ? null : order.orderDiscount.id), order.status); // making note for merchantId
    }

    /// PRODUCT
    public ProductDTO Product_ModelToDTO(Product product) {
        return new ProductDTO(product.id, product.merchant, product.title, product.categoryId, product.price, product.discountId, product.taxId, product.weight, product.weightUnit);
    }

    public Product Product_DTOToModel(ProductDTO productDTO) {
        return new Product(productDTO.id(), productDTO.merchant(), productDTO.title(), productDTO.categoryId(), productDTO.price(), productDTO.discountId(), productDTO.taxId(), productDTO.weight(), productDTO.weightUnit());
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
    public Employee CreateEmployee_DTOToModel(CreateEmployeeDTO createEmployeeDTO) {
        return new Employee(0, null , createEmployeeDTO.firstName(), createEmployeeDTO.lastName(),
                createEmployeeDTO.employeeType(), createEmployeeDTO.username(),
                passwordEncoder.encode(createEmployeeDTO.password()),null);
    }

    public Employee UpdateEmployee_DTOToModel(UpdateEmployeeDTO updateEmployeeDTO, Employee employee){
        employee.setMerchant(null);
        employee.setFirstName(updateEmployeeDTO.firstName());
        employee.setLastName(updateEmployeeDTO.lastName());
        employee.setEmployeeType(updateEmployeeDTO.employeeType());
        employee.setUsername(updateEmployeeDTO.username());
        employee.setPassword(passwordEncoder.encode(updateEmployeeDTO.password()));
        return employee;
    }
}
