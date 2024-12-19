package wdmsystem.merchant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class MerchantController {

    private final MerchantService _merchantService;

    public MerchantController(MerchantService merchantService) {
        this._merchantService = merchantService;
    }

    @PostMapping("/merchants")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<MerchantDTO> createMerchant(@RequestBody MerchantDTO request) {
        log.info("Received request to create merchant: {}", request);
        MerchantDTO newMerchant = _merchantService.createMerchant(request);
        log.info("Merchant created successfully: {}", newMerchant);
        return new ResponseEntity<>(newMerchant, HttpStatus.CREATED); 
    }

    @GetMapping("/merchants/{merchantId}")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<MerchantDTO> getMerchantById(@PathVariable int merchantId) {
        log.info("Fetching merchant with ID: {}", merchantId);
        MerchantDTO merchant = _merchantService.getMerchantById(merchantId);
        log.info("Fetched merchant details: {}", merchant);
        return new ResponseEntity<>(merchant, HttpStatus.OK);
    }
  
    @GetMapping("/merchants")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER') or hasRole('REGULAR')")
    ResponseEntity<MerchantDTO> getMerchant() {
        log.info("Fetching current merchant from token!"); // not sure if this should include something more
        MerchantDTO merchant = _merchantService.getMerchant();
        log.info("Fetched merchant details: {}", merchant);
        return new ResponseEntity<>(merchant, HttpStatus.OK);
    }

    @PutMapping("/merchants/{merchantId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('OWNER') and @merchantService.isOwnedByCurrentUser(#merchantId))")
    ResponseEntity<MerchantDTO> updateMerchant(@PathVariable int merchantId, @RequestBody MerchantDTO response) {
        log.info("Updating merchant with ID: {}", merchantId);
        MerchantDTO merchant = _merchantService.updateMerchant(merchantId, response);
        log.info("Merchant with ID: {} updated successfully", merchantId);
        return new ResponseEntity<>(merchant, HttpStatus.OK);
    }
    
    @DeleteMapping("/merchants/{merchantId}") // There is no delete method defined in the api but someone on the team requested it
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<MerchantDTO> deleteMerchant(@PathVariable int merchantId) {
        log.info("Deleting merchant with ID: {}", merchantId);
        _merchantService.deleteMerchant(merchantId);
        log.info("Merchant with ID: {} deleted successfully", merchantId);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

}
