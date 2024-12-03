package com.example.wdmsystem.merchant.system;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

public final class MerchantController {

    private final MerchantService _merchantService;

    public MerchantController(MerchantService merchantService) {
        this._merchantService = merchantService;
    }

    @PostMapping("/merchants")
    ResponseEntity<Merchant> createMerchant(@RequestBody MerchantDTO request) {
        Merchant newMerchant = _merchantService.createMerchant(request);
        return new ResponseEntity<>(newMerchant, HttpStatus.CREATED); 
    }

    @GetMapping("/merchants/{merchantId}")
    ResponseEntity<Merchant> getMerchant(@PathVariable int id) {
        Merchant merchant = _merchantService.getMerchant(id);
        return new ResponseEntity<>(merchant, HttpStatus.OK);
    }

    @PutMapping("/merchants/{merchantId}")
    ResponseEntity<Merchant> updateMerchant(@PathVariable int id, @RequestBody MerchantDTO response) {
        _merchantService.updateMerchant(id, response);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
    
    @DeleteMapping("/merchants/{merchantId}") // There is no delete method defined in the api but someone on the team requested it
    ResponseEntity<Merchant> deleteMerchant(@PathVariable int id) {
        _merchantService.deleteMerchant(id);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

}
