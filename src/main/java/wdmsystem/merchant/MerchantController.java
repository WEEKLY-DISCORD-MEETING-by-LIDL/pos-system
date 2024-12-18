package wdmsystem.merchant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class MerchantController {

    private final MerchantService _merchantService;

    public MerchantController(MerchantService merchantService) {
        this._merchantService = merchantService;
    }

    @PostMapping("/merchants")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<MerchantDTO> createMerchant(@RequestBody MerchantDTO request) {
        MerchantDTO newMerchant = _merchantService.createMerchant(request);
        return new ResponseEntity<>(newMerchant, HttpStatus.CREATED); 
    }

    @GetMapping("/merchants")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER') or hasRole('REGULAR')")
    ResponseEntity<MerchantDTO> getMerchant() {
        MerchantDTO merchant = _merchantService.getMerchant();
        return new ResponseEntity<>(merchant, HttpStatus.OK);
    }

    @PutMapping("/merchants/{merchantId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('OWNER') and @merchantService.isOwnedByCurrentUser(#merchantId))")
    ResponseEntity<MerchantDTO> updateMerchant(@PathVariable int merchantId, @RequestBody MerchantDTO response) {
        _merchantService.updateMerchant(merchantId, response);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
    
    @DeleteMapping("/merchants/{merchantId}") // There is no delete method defined in the api but someone on the team requested it
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<MerchantDTO> deleteMerchant(@PathVariable int merchantId) {
        _merchantService.deleteMerchant(merchantId);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

}
