package com.example.wdmsystem.utility;

import com.example.wdmsystem.exception.NotFoundException;
import com.example.wdmsystem.order.system.*;
import com.example.wdmsystem.product.system.*;
import com.example.wdmsystem.tax.system.Tax;
import com.example.wdmsystem.tax.system.TaxDTO;
import org.springframework.stereotype.Service;

@Service
public class DTOMapper {
    IOrderRepository orderRepository;
    IOrderItemRepository orderItemRepository;
    IProductRepository productRepository;
    IProductVariantRepository productVariantRepository;

    public DTOMapper(IOrderItemRepository orderItemRepository, IProductRepository productRepository, IOrderRepository orderRepository, IProductVariantRepository productVariantRepository) {
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.productVariantRepository = productVariantRepository;
    }

    /// ORDER
    public OrderDTO Order_ModelToDTO(Order order) {
        return new OrderDTO(order.id, order.merchantId, (order.orderDiscount == null ? null : order.orderDiscount.id), order.status);
    }

    /// PRODUCT
    public ProductDTO Product_ModelToDTO(Product product) {
        return new ProductDTO(product.id, product.merchantId, product.title, product.categoryId, product.price, product.discountId, product.tax.id, product.weight, product.weightUnit);
    }

    public Product Product_DTOToModel(ProductDTO productDTO) {
        return new Product(productDTO.id(), productDTO.merchantId(), productDTO.title(), productDTO.categoryId(), productDTO.price(), productDTO.discountId(), productDTO.taxId(), productDTO.weight(), productDTO.weightUnit());
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

    /// TAX

    public Tax Tax_DTOToModel(TaxDTO taxDTO) {
        return new Tax(taxDTO.id(), taxDTO.merchantId(), taxDTO.title(), taxDTO.percentage());
    }

    public TaxDTO Tax_ModelToDTO(Tax tax) {
        return new TaxDTO(tax.id, tax.merchantId, tax.title, tax.percentage);
    }
}
