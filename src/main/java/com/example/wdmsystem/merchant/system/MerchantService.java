package com.example.wdmsystem.merchant.system;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.example.wdmsystem.exception.NotFoundException;


@Service
public class MerchantService {

    private final IMerchantRepository merchantRepository;

    public MerchantService(IMerchantRepository merchantRepository) {
        this.merchantRepository = merchantRepository;
    }

    public Merchant createMerchant(MerchantDTO request) {

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
