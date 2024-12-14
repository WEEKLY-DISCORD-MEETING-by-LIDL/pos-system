package com.example.wdmsystem.merchant.system;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;


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
        return null;
    }

    public void updateMerchant(int id, MerchantDTO request) {
        
    }

    public void deleteMerchant(int id) { // There is no delete method defined in the api but someone on the team requested it

    }

}
