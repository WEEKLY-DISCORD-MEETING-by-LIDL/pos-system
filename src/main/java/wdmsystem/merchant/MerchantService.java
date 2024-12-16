package wdmsystem.merchant;

import java.time.LocalDateTime;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import wdmsystem.auth.CustomUserDetails;
import wdmsystem.exception.InvalidInputException;
import wdmsystem.exception.NotFoundException;
import wdmsystem.utility.DTOMapper;


@Service
public class MerchantService {

    private final IMerchantRepository merchantRepository;
    private final DTOMapper dtoMapper;

    public MerchantService(IMerchantRepository merchantRepository, DTOMapper dtoMapper) {
        this.merchantRepository = merchantRepository;
        this.dtoMapper = dtoMapper;
    }

    public MerchantDTO createMerchant(MerchantDTO request) {

        if(request.address().countryCode.length() != 3) {
            throw new InvalidInputException("Country code must be exactly 3 symbols long.");
        }

        if(request.address().address1.length() > 30 || request.address().address2.length() > 30) {
            throw new InvalidInputException("Neither address can be longer than 30 characters.");
        }

        if(request.address().city.length() > 30) {
            throw new InvalidInputException("City of merchant cannot be longer than 30 characters.");
        }

        if(request.address().country.length() > 30) {
            throw new InvalidInputException("Country of merchant cannot be longer than 30 characters.");
        }

        if(request.address().zipCode.length() > 30) {
            throw new InvalidInputException("Zip code of merchant cannot be longer than 30 characters.");
        }

        if(request.name().length() > 30) {
            throw new InvalidInputException("Merchant name cannot be longer than 30 characters");
        }

        if(request.phone().length() > 30) {
            throw new InvalidInputException("Merchant phone number cannot be longer than 30 characters");
        }

        if(request.email().length() > 30) {
            throw new InvalidInputException("Merchant email cannot be longer than 30 characters");
        }

        Merchant merchant = new Merchant(
            0,
            request.name(),
            request.vat(),
            request.address(),
            request.email(),
            request.phone(),
            LocalDateTime.now()
        );
        merchant.setUpdatedAt(LocalDateTime.now());
        merchantRepository.save(merchant);
        return dtoMapper.Merchant_ModelToDTO(merchant);
    }

    public MerchantDTO getMerchant(int id) {
        Merchant merchant =  merchantRepository.findById(id).orElseThrow(
            () -> new NotFoundException("Merchant with ID " + id + " not found")
        );
        return dtoMapper.Merchant_ModelToDTO(merchant);
    }

    public void updateMerchant(int id, MerchantDTO request) {
        Merchant merchantToUpdate = merchantRepository.findById(id).orElseThrow(
            () -> new NotFoundException("Merchant with ID " + id + " not found")
        );

        merchantToUpdate.setName(request.name());
        merchantToUpdate.setVat(request.vat());
        merchantToUpdate.setAddress(request.address());
        merchantToUpdate.setEmail(request.email());
        merchantToUpdate.setPhone(request.phone());
        merchantToUpdate.setUpdatedAt(LocalDateTime.now());

        merchantRepository.save(merchantToUpdate);

    }

    public void deleteMerchant(int merchantId) { // There is no delete method defined in the api but someone on the team requested it
        if(merchantRepository.existsById(merchantId)) {
            merchantRepository.deleteById(merchantId); // for now just deletes the merchant
        }
        else {
            throw new NotFoundException("Merchant with id " + merchantId + " not found");
        }
    }

    public boolean isOwnedByCurrentUser(int merchantId) {
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        Merchant merchant = merchantRepository.findById(merchantId).orElseThrow(
                () -> new NotFoundException("Merchant with id " + merchantId + " not found"));
        return merchant.id == currentUser.getMerchantId();
    }
}
