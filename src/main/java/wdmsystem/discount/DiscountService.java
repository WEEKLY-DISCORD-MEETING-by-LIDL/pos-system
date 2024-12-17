package wdmsystem.discount;

import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import wdmsystem.auth.CustomUserDetails;
import wdmsystem.exception.NotFoundException;
import wdmsystem.merchant.IMerchantRepository;
import wdmsystem.merchant.Merchant;
import wdmsystem.utility.DTOMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class DiscountService {
    private final IDiscountRepository discountRepository;
    private final IMerchantRepository merchantRepository;
    private final DTOMapper dtoMapper;

    public DiscountDTO createDiscount(DiscountDTO discountDTO) {
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        Merchant merchant = merchantRepository.findById(currentUser.getMerchantId()).orElseThrow(
                () -> new NotFoundException("Merchant with ID " + currentUser.getMerchantId() + " not found")
        );

        Discount discount = new Discount(
                0,
                merchant,
                discountDTO.title(),
                discountDTO.expiresOn(),
                discountDTO.percentage()
        );
        discount.setCreatedAt(LocalDateTime.now());
        discount.setUpdatedAt(LocalDateTime.now());
        discountRepository.save(discount);
        return dtoMapper.Discount_ModelToDTO(discount);
    }

    public List<DiscountDTO> getDiscounts(){
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        boolean isAdmin = currentUser.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        List<Discount> discountList;
        if(isAdmin){
            discountList = discountRepository.findAll();
        } else {
            discountList = discountRepository.findByMerchantId(currentUser.getMerchantId());
        }

        List<DiscountDTO> discountDTOList = new ArrayList<>();
        for(Discount discount : discountList){
            discountDTOList.add(dtoMapper.Discount_ModelToDTO(discount));
        }
        return discountDTOList;
    }

    public DiscountDTO updateDiscount(int discountId, DiscountDTO discountDTO) {
        Discount discount = discountRepository.findById(discountId).orElseThrow(
                () -> new NotFoundException("Discount with id " + discountId + " not found"));

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
        discountRepository.save(discount);
        return dtoMapper.Discount_ModelToDTO(discount);
    }

    public void deleteDiscount(int discountId) {
        if (discountRepository.existsById(discountId)) {
            discountRepository.deleteById(discountId);
        }
        else {
            throw new NotFoundException("Discount with id " + discountId + " not found");
        }
    }

    public boolean isOwnedByCurrentUser(int discountId) {
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        Discount discount = discountRepository.findById(discountId).orElseThrow(
                () -> new NotFoundException("Discount with id " + discountId + " not found"));

        return discount.getMerchant().getId() == currentUser.getMerchantId();
    }
}
