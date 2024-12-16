package wdmsystem.tax;

import java.util.List;

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

@RestController
public class TaxController {

    private TaxService _taxService;

    public TaxController(TaxService taxService) {
        this._taxService = taxService;
    } 

    @PostMapping("/taxes")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    ResponseEntity<TaxDTO> createTax(@RequestBody TaxDTO request) {
        TaxDTO newTax = _taxService.createTax(request);
        return new ResponseEntity<>(newTax, HttpStatus.CREATED);
    }

    @GetMapping("/taxes")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER') or hasRole('REGULAR')")
    ResponseEntity<List<TaxDTO>> getTaxes(@RequestParam(required=false) Integer limit) {
        List<TaxDTO> taxList = _taxService.getTaxes(limit);
        return new ResponseEntity<>(taxList, HttpStatus.OK);
    }

    @GetMapping("/taxes/{taxId}")
    @PreAuthorize("hasRole('ADMIN') or ((hasRole('OWNER') or hasRole('REGULAR')) and @taxService.isOwnedByCurrentUser(#taxId))")
    ResponseEntity<TaxDTO> getTax(@PathVariable int taxId) {
        TaxDTO newTax = _taxService.getTax(taxId);
        return new ResponseEntity<>(newTax, HttpStatus.OK);
    }

    //changed to NO_CONTENT
    @PutMapping("/taxes/{taxId}")
    @PreAuthorize("hasRole('ADMIN') or ((hasRole('OWNER') or hasRole('REGULAR')) and @taxService.isOwnedByCurrentUser(#taxId))")
    ResponseEntity<TaxDTO> updateTax(@PathVariable int taxId, @RequestBody TaxDTO request) {
        _taxService.updateTax(taxId, request);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    //changed to NO_CONTENT
    @DeleteMapping("/taxes/{taxId}")
    @PreAuthorize("hasRole('ADMIN') or ((hasRole('OWNER') or hasRole('REGULAR')) and @taxService.isOwnedByCurrentUser(#taxId))")
    ResponseEntity<TaxDTO> deleteTax(@PathVariable int taxId) {
        _taxService.deleteTax(taxId);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }



}
