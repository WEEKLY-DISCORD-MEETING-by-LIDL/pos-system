package customer.system;

import customer.system.DTOS.CreateCustomer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

//TODO: Request validation
//TODO: Error response codes
//TODO: Custom response messages
@RestController
public final class CustomerController {

    private CustomerService _customerService;

    public CustomerController(CustomerService customerService) {
        this._customerService = customerService;
    }

    @PostMapping("/customers")
    ResponseEntity<Customer> createCustomer(@RequestBody CreateCustomer request) {
        Customer newCustomer = _customerService.createCustomer(request);
        return new ResponseEntity<>(newCustomer, HttpStatus.CREATED);
    }

    ///In the API contract Limit is an entity, but in the doc it isn't mentioned.
    //TODO: Decide whick package Limit should be in. (It is used in several modules)
    @GetMapping("/customers")
    ResponseEntity<List<Customer>> getCustomers(@RequestParam(value = "createdAtMin", required = false) Date createdAtMin,
                                                @RequestParam(value = "createdAtMax", required = false) Date createdAtMax,
                                                @RequestParam(value = "limit", required = false) int limit) {
        List<Customer> customers = _customerService.getCustomers(createdAtMin, createdAtMax, limit);
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }
    //get
    //put
    //delete
    //get
}
