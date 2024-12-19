package wdmsystem.utility;

import org.springframework.security.crypto.password.PasswordEncoder;
import wdmsystem.auth.CustomUserDetails;
import wdmsystem.category.Category;
import wdmsystem.category.CategoryDTO;
import wdmsystem.category.ICategoryRepository;
import wdmsystem.customer.Customer;
import wdmsystem.customer.CustomerDTO;
import wdmsystem.discount.Discount;
import wdmsystem.discount.DiscountDTO;
import wdmsystem.discount.IDiscountRepository;
import wdmsystem.employee.EmployeeDTO;
import wdmsystem.employee.Employee;
import wdmsystem.employee.IEmployeeRepository;
import wdmsystem.employee.UpdateEmployeeDTO;
import wdmsystem.exception.NotFoundException;
import wdmsystem.merchant.IMerchantRepository;
import wdmsystem.merchant.Merchant;
import wdmsystem.merchant.MerchantDTO;
import wdmsystem.order.*;
import wdmsystem.order.discount.IOrderDiscountRepository;
import wdmsystem.order.discount.OrderDiscount;
import wdmsystem.order.discount.OrderDiscountDTO;
import wdmsystem.payment.Payment;
import wdmsystem.payment.PaymentDTO;
import wdmsystem.product.*;
import wdmsystem.reservation.Reservation;
import wdmsystem.reservation.ReservationDTO;
import wdmsystem.service.ServiceDTO;
import wdmsystem.tax.ITaxRepository;
import wdmsystem.tax.Tax;
import wdmsystem.tax.TaxDTO;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import wdmsystem.service.Service;


@org.springframework.stereotype.Service
@AllArgsConstructor
public class DTOMapper {
    private final PasswordEncoder passwordEncoder;
    IOrderRepository orderRepository;
    IOrderItemRepository orderItemRepository;
    IProductRepository productRepository;
    IProductVariantRepository productVariantRepository;
    IEmployeeRepository employeeRepository;
    ITaxRepository taxRepository;
    ICategoryRepository categoryRepository;
    IMerchantRepository merchantRepository;
    IDiscountRepository discountRepository;
    IOrderDiscountRepository orderDiscountRepository;

    /// ORDER
    public OrderDTO Order_ModelToDTO(Order order) {
        return new OrderDTO(order.id, order.merchant.id, (order.orderDiscount == null ? null : order.orderDiscount.id), order.status); // making note for merchantId
    }

    /// PRODUCT
    public ProductDTO Product_ModelToDTO(Product product) {
        return new ProductDTO(product.id, product.merchant.id, product.title, (product.category == null ? null : product.category.id), product.price, (product.discount == null ? null : product.discount.id), (product.tax == null ? null : product.tax.id), product.weight, product.weightUnit);
    }

    public Product Product_DTOToModel(ProductDTO productDTO) {
        Tax tax;
        Category category;
        Discount discount;

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

        if (productDTO.discountId() == null) {
            discount = null;
        } else {
            discount = discountRepository.findById(productDTO.discountId()).orElse(null);
        }

        return new Product(productDTO.id(), null, productDTO.title(), category, productDTO.price(), discount, tax, productDTO.weight(), productDTO.weightUnit());
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

    /// EMPLOYEE

    public EmployeeDTO Employee_ModelToDTO(Employee employee) {
        return new EmployeeDTO(employee.firstName, employee.lastName, employee.employeeType, employee.username, "<hidden>");
    }

    public void UpdateEmployee_DTOToModel(Employee employee, UpdateEmployeeDTO dto) {
        if (dto.firstName() != null && dto.firstName().length() >= 30) {
            employee.setFirstName(dto.firstName());
        }
        if (dto.lastName() != null && dto.lastName().length() >= 30) {
            employee.setLastName(dto.lastName());
        }
        if (dto.employeeType() != null) {
            employee.setEmployeeType(dto.employeeType());
        }
        if (dto.username() != null && dto.username().length() >= 30) {
            employee.setUsername(dto.username());
        }
        if (dto.password() != null) {
            employee.setPassword(passwordEncoder.encode(dto.password()));
        }
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
        return new CategoryDTO(category.id, category.title);
    }

    /// Reservation
    public ReservationDTO Reservation_ModelToDTO(Reservation reservation) {
        return new ReservationDTO(reservation.id, reservation.getService().id, reservation.employeeId, reservation.startTime, reservation.sendConfirmation, reservation.endTime, reservation.reservationStatus);
    }

    /// Service
    public ServiceDTO Service_ModelToDTO(Service service) {
        return new ServiceDTO(service.title, service.category.id, service.price, service.discount.id,service.tax.id, service.durationMins);
    }

    /// Merchant

    public MerchantDTO Merchant_ModelToDTO(Merchant merchant){
        return new MerchantDTO(merchant.name, merchant.vat, merchant.address, merchant.email, merchant.phone);
    }

    /// Discount

    public DiscountDTO Discount_ModelToDTO(Discount discount){
        return new DiscountDTO(discount.id, discount.title, discount.percentage, discount.expiresOn);
    }

    public Discount Discount_DTOToModel(DiscountDTO dto){
        return new Discount(0, null, dto.title(), dto.expiresOn(), dto.percentage());
    }

    /// Order discount

    public OrderDiscountDTO OrderDiscount_ModelToDTO(OrderDiscount discount){
        return new OrderDiscountDTO(discount.id, discount.title, discount.percentage, discount.expiresOn);
    }

    public OrderDiscount OrderDiscount_DTOToModel(OrderDiscountDTO discountDTO) {
        return new OrderDiscount(0, null, discountDTO.title(), discountDTO.percentage(), discountDTO.expiresOn());
    }
}
