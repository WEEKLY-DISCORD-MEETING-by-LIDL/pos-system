package com.example.wdmsystem.utility;

import com.example.wdmsystem.exception.InvalidInputException;
import com.example.wdmsystem.exception.NotFoundException;
import com.example.wdmsystem.order.system.*;
import com.example.wdmsystem.payment.system.Payment;
import com.example.wdmsystem.payment.system.PaymentDTO;
import com.example.wdmsystem.product.system.*;
import com.example.wdmsystem.reservation.system.Reservation;
import com.example.wdmsystem.tax.system.ITaxRepository;
import com.example.wdmsystem.tax.system.Tax;
import com.example.wdmsystem.tax.system.TaxDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DTOMapper {
    IOrderRepository orderRepository;
    IOrderItemRepository orderItemRepository;
    IProductRepository productRepository;
    IProductVariantRepository productVariantRepository;
    ITaxRepository taxRepository;


    /// ORDER
    public OrderDTO Order_ModelToDTO(Order order) {
        return new OrderDTO(order.id, order.merchantId, (order.orderDiscount == null ? null : order.orderDiscount.id), order.status);
    }

    /// PRODUCT
    public ProductDTO Product_ModelToDTO(Product product) {
        return new ProductDTO(product.id, product.merchantId, product.title, (product.category == null ? null : product.category.id), product.price, product.discountId, (product.tax == null ? null : product.tax.id), product.weight, product.weightUnit);
    }

    public Product Product_DTOToModel(ProductDTO productDTO) {
        Tax tax;

        if(productDTO.taxId() == null) {
            tax = null;
        }
        else {
            tax = taxRepository.findById(productDTO.taxId()).orElse(null);
        }

        //category pulling

        return new Product(productDTO.id(), productDTO.merchantId(), productDTO.title(), null, productDTO.price(), productDTO.discountId(), tax, productDTO.weight(), productDTO.weightUnit());
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

    public OrderItemDTO OrderItem_ModelToDTO(OrderItem orderItem) {
        return new OrderItemDTO(orderItem.id, orderItem.order.id, orderItem.productVariant.id, orderItem.quantity);
    }

    /// TAX

    public Tax Tax_DTOToModel(TaxDTO taxDTO) {
        return new Tax(taxDTO.id(), taxDTO.merchantId(), taxDTO.title(), taxDTO.percentage());
    }

    public TaxDTO Tax_ModelToDTO(Tax tax) {
        return new TaxDTO(tax.id, tax.merchantId, tax.title, tax.percentage);
    }

    /// PAYMENT

    public Payment Payment_DTOToModel(PaymentDTO paymentDTO) {
        Order order = orderRepository.findById(paymentDTO.orderId()).orElse(null);
        Reservation reservation = null; //will change once reservations work

        if(order == null && reservation == null) {
            throw new NotFoundException("Payment is not applied to any order or reservation");
        }

        return new Payment(paymentDTO.id(), paymentDTO.tipAmount(), paymentDTO.totalAmount(), paymentDTO.method(), order);
    }

    public PaymentDTO Payment_ModelToDTO(Payment payment) {
        return new PaymentDTO(payment.id, payment.tipAmount, payment.totalAmount, payment.method, (payment.order == null ? null : payment.order.id));
    }
}
