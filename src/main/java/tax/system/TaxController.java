package tax.system;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    ResponseEntity<Tax> createTax(@RequestBody TaxDTO request) {
        Tax newTax = _taxService.createTax(request);
        return new ResponseEntity<>(newTax, HttpStatus.CREATED);
    }

    @GetMapping("/taxes")
    ResponseEntity<List<Tax>> getTaxes(@RequestParam(required=false) int limit) {
        List<Tax> taxList = _taxService.getTaxes(limit);
        return new ResponseEntity<>(taxList, HttpStatus.OK);
    }

    @GetMapping("/taxes/{taxId}")
    ResponseEntity<Tax> getTax(@PathVariable int taxId) {
        Tax newTax = _taxService.getTax(taxId);
        return new ResponseEntity<>(newTax, HttpStatus.OK);
    }

    @PutMapping("/taxes/{taxId}")
    ResponseEntity<Tax> updateTax(@PathVariable int taxId) {
        _taxService.updateTax(taxId);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @DeleteMapping("/taxes/{taxId}")
    ResponseEntity<Tax> deleteTax(@PathVariable int taxId) {
        _taxService.deleteTax(taxId);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }



}
