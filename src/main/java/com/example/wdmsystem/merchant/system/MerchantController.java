package com.example.wdmsystem.merchant.system;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
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
    ResponseEntity<Merchant> getMerchant(@PathVariable int merchantId) {
        Merchant merchant = _merchantService.getMerchant(merchantId);
        return new ResponseEntity<>(merchant, HttpStatus.OK);
    }

    @PutMapping("/merchants/{merchantId}")
    ResponseEntity<Merchant> updateMerchant(@PathVariable int merchantId, @RequestBody MerchantDTO response) {
        _merchantService.updateMerchant(merchantId, response);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
    
    @DeleteMapping("/merchants/{merchantId}") // There is no delete method defined in the api but someone on the team requested it
    ResponseEntity<Merchant> deleteMerchant(@PathVariable int merchantId) {
        _merchantService.deleteMerchant(merchantId);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

}
