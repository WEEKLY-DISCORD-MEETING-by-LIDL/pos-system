package wdmsystem.merchant.system;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import wdmsystem.exception.InvalidInputException;
import wdmsystem.exception.NotFoundException;


@Service
public class MerchantService {

    private final IMerchantRepository merchantRepository;

    public MerchantService(IMerchantRepository merchantRepository) {
        this.merchantRepository = merchantRepository;
    }

    public Merchant createMerchant(MerchantDTO request) {

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
        return merchantRepository.save(merchant);
    }

    public Merchant getMerchant(int id) {
        return merchantRepository.findById(id).orElseThrow(
            () -> new NotFoundException("Merchant with ID " + id + " not found")
        );
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

}
