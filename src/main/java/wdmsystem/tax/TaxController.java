package wdmsystem.tax;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TaxController {

    private TaxService _taxService;

    public TaxController(TaxService taxService) {
        this._taxService = taxService;
    } 

    @PostMapping("/taxes")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    ResponseEntity<TaxDTO> createTax(@RequestBody TaxDTO request) {
        log.info("Received request to create tax: {}", request);
        TaxDTO newTax = _taxService.createTax(request);
        log.info("Tax created successfully: {}", newTax);
        return new ResponseEntity<>(newTax, HttpStatus.CREATED);
    }

    @GetMapping("/taxes")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER') or hasRole('REGULAR')")
    ResponseEntity<List<TaxDTO>> getTaxes(@RequestParam(required=false) Integer limit) {
        log.info("Fetching all taxes with limit: {}", limit);
        List<TaxDTO> taxList = _taxService.getTaxes(limit);
        log.info("Retrieved {} taxes.", taxList.size());
        return new ResponseEntity<>(taxList, HttpStatus.OK);
    }

    @GetMapping("/taxes/{taxId}")
    @PreAuthorize("hasRole('ADMIN') or ((hasRole('OWNER') or hasRole('REGULAR')) and @taxService.isOwnedByCurrentUser(#taxId))")
    ResponseEntity<TaxDTO> getTax(@PathVariable int taxId) {
        log.info("Fetching tax with ID: {}", taxId);
        TaxDTO newTax = _taxService.getTax(taxId);
        log.info("Retrieved tax with ID: {}", taxId);
        return new ResponseEntity<>(newTax, HttpStatus.OK);
    }

    //changed to NO_CONTENT
    @PutMapping("/taxes/{taxId}")
    @PreAuthorize("hasRole('ADMIN') or ((hasRole('OWNER') or hasRole('REGULAR')) and @taxService.isOwnedByCurrentUser(#taxId))")
    ResponseEntity<TaxDTO> updateTax(@PathVariable int taxId, @RequestBody TaxDTO request) {
        log.info("Received request to update tax with ID: {}", taxId);
        _taxService.updateTax(taxId, request);
        log.info("Tax with ID: {} updated successfully.", taxId);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    //changed to NO_CONTENT
    @DeleteMapping("/taxes/{taxId}")
    @PreAuthorize("hasRole('ADMIN') or ((hasRole('OWNER') or hasRole('REGULAR')) and @taxService.isOwnedByCurrentUser(#taxId))")
    ResponseEntity<TaxDTO> deleteTax(@PathVariable int taxId) {
        log.info("Received request to delete tax with ID: {}", taxId);
        _taxService.deleteTax(taxId);
        log.info("Tax with ID: {} deleted successfully.", taxId);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }



}
