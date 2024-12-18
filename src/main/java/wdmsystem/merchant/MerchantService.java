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
        Merchant savedMerchant = merchantRepository.save(merchant);
        return dtoMapper.Merchant_ModelToDTO(savedMerchant);
    }

    public MerchantDTO getMerchantById(int merchantId) {
        Merchant merchant = merchantRepository.findById(merchantId).orElseThrow(() ->
                new NotFoundException("Merchant with id " + merchantId + " not found."));

        return dtoMapper.Merchant_ModelToDTO(merchant);
    }

    public MerchantDTO getMerchant() {
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext()
        .getAuthentication()
        .getPrincipal();
        Merchant merchant =  merchantRepository.findById(currentUser.getMerchantId()).orElseThrow(
            () -> new NotFoundException("Merchant with ID " + currentUser.getMerchantId() + " not found")
        );
        return dtoMapper.Merchant_ModelToDTO(merchant);
    }

    public MerchantDTO updateMerchant(Integer id, MerchantDTO request) {
        Merchant merchantToUpdate = merchantRepository.findById(id).orElseThrow(
            () -> new NotFoundException("Merchant with ID " + id + " not found")
        );
        if (request.name() != null && request.name().length() <= 30) {
            merchantToUpdate.setName(request.name());
        }
        if (request.vat() != null) {
            merchantToUpdate.setVat(request.vat());
        }
        if (request.address() != null) {
            merchantToUpdate.setAddress(request.address());
        }
        if (request.email() != null) {
            merchantToUpdate.setEmail(request.email());
        }
        if (request.phone() != null) {
            merchantToUpdate.setPhone(request.phone());
        }
        merchantToUpdate.setUpdatedAt(LocalDateTime.now());
        Merchant savedMerchant = merchantRepository.save(merchantToUpdate);
        return dtoMapper.Merchant_ModelToDTO(savedMerchant);
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
