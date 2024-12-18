package wdmsystem.order.discount;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import wdmsystem.auth.CustomUserDetails;
import wdmsystem.discount.Discount;
import wdmsystem.discount.DiscountDTO;
import wdmsystem.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import wdmsystem.merchant.IMerchantRepository;
import wdmsystem.merchant.Merchant;
import wdmsystem.utility.DTOMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderDiscountService {
    private final IOrderDiscountRepository orderDiscountRepository;
    private final IMerchantRepository merchantRepository;
    private final DTOMapper dtoMapper;

    public OrderDiscountDTO createOrderDiscount(OrderDiscountDTO discountDTO) {
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        Merchant merchant = merchantRepository.findById(currentUser.getMerchantId()).orElseThrow(
                () -> new NotFoundException("Merchant with ID " + currentUser.getMerchantId() + " not found")
        );

        OrderDiscount discount = dtoMapper.OrderDiscount_DTOToModel(discountDTO);
        discount.setMerchant(merchant);
        discount.setCreatedAt(LocalDateTime.now());
        discount.setUpdatedAt(LocalDateTime.now());
        OrderDiscount savedDiscount = orderDiscountRepository.save(discount);
        return dtoMapper.OrderDiscount_ModelToDTO(savedDiscount);
    }
    public List<OrderDiscountDTO> getOrderDiscounts(Integer limit){
        if (limit == null || limit <= 0) {
            limit = 50;
        }

        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        boolean isAdmin = currentUser.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        List<OrderDiscount> discountList;
        if(isAdmin){
            discountList = orderDiscountRepository.findAll();
        } else {
            discountList = orderDiscountRepository.findByMerchantId(currentUser.getMerchantId(), PageRequest.of(0, limit));
        }

        List<OrderDiscountDTO> discountDTOList = new ArrayList<>();
        for(OrderDiscount discount : discountList){
            discountDTOList.add(dtoMapper.OrderDiscount_ModelToDTO(discount));
        }
        return discountDTOList;
    }
    public OrderDiscountDTO getOrderDiscount(int orderDiscountId) {
        OrderDiscount discount = orderDiscountRepository.findById(orderDiscountId).orElseThrow(
                () -> new NotFoundException("Order discount with id " + orderDiscountId + " not found"));
        return dtoMapper.OrderDiscount_ModelToDTO(discount);
    }

    public OrderDiscountDTO updateOrderDiscount(int orderDiscountId, OrderDiscountDTO discountDTO) {
        OrderDiscount discount = orderDiscountRepository.findById(orderDiscountId).orElseThrow(
                () -> new NotFoundException("Order discount with id " + orderDiscountId + " not found"));

        if (discountDTO.title() != null) {
            discount.setTitle(discountDTO.title());
        }
        if (discountDTO.percentage() != null) {
            discount.setPercentage(discountDTO.percentage());
        }
        if (discountDTO.expiresOn() != null) {
            discount.setExpiresOn(discountDTO.expiresOn());
        }

        discount.setUpdatedAt(LocalDateTime.now());
        OrderDiscount savedDiscount = orderDiscountRepository.save(discount);
        return dtoMapper.OrderDiscount_ModelToDTO(savedDiscount);
    }

    public void deleteOrderDiscount(int discountId) {
        if (orderDiscountRepository.existsById(discountId)) {
            orderDiscountRepository.deleteById(discountId);
        }
        else {
            throw new NotFoundException("Order discount with id " + discountId + " not found");
        }
    }

    public boolean isOwnedByCurrentUser(int discountId) {
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        OrderDiscount orderDiscount = orderDiscountRepository.findById(discountId).orElseThrow(() ->
                new NotFoundException("Discount with id " + discountId + " not found"));

        return orderDiscount.getMerchant().getId() == currentUser.getMerchantId();
    }
}
