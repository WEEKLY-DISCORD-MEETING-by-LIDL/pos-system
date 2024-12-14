package com.example.wdmsystem.merchant.system;

import org.springframework.stereotype.Service;


@Service
public class MerchantService {

    private final IMerchantRepository merchantRepository;
    public MerchantService(IMerchantRepository merchantRepository) {
        this.merchantRepository = merchantRepository;
    }

    public Merchant createMerchant(MerchantDTO request) {
        return null;
    }

    public Merchant getMerchant(int id) {
        return null;
    }

    public void updateMerchant(int id, MerchantDTO request) {
        
    }

    public void deleteMerchant(int id) { // There is no delete method defined in the api but someone on the team requested it

    }

}
