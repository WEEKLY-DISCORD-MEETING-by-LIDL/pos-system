package com.example.wdmsystem.customer.system;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.wdmsystem.reservation.system.Reservation;

import java.util.Date;
import java.util.List;

//TODO: Request validation
//TODO: Error response codes
//TODO: Custom response messages
@RestController
public final class CustomerController {

    private final CustomerService _customerService;

    public CustomerController(CustomerService customerService) {
        this._customerService = customerService;
    }

    @PostMapping("/customers")
    ResponseEntity<Customer> createCustomer(@RequestBody CustomerDTO request) {
        Customer newCustomer = _customerService.createCustomer(request);
        return new ResponseEntity<>(newCustomer, HttpStatus.CREATED);
    }

    /// In the API contract Limit is an entity, but in the doc it isn't mentioned.
    //TODO: Decide which package Limit should be in. (It is used in several modules)
    @GetMapping("/customers")
    ResponseEntity<List<Customer>> getCustomers(@RequestParam(required = false) Date createdAtMin,
                                                @RequestParam(required = false) Date createdAtMax,
                                                @RequestParam(required = false) int limit) {
        List<Customer> customers = _customerService.getCustomers(createdAtMin, createdAtMax, limit);
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @GetMapping("/customers/{customerId}")
    ResponseEntity<Customer> getCustomer(@PathVariable int customerId) {
        Customer customer = _customerService.getCustomer(customerId);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @PutMapping("/customers/{customerId}")
    ResponseEntity<String> updateCustomer(@PathVariable int customerId, @RequestBody CustomerDTO request) {
        _customerService.updateCustomer(customerId, request);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    /// API contract has operation id DeleteCustomer, changed to deleteCustomer for naming consistency
    @DeleteMapping("/customers/{customerId}")
    ResponseEntity<String> deleteCustomer(@PathVariable int customerId) {
        _customerService.deleteCustomer(customerId);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @GetMapping("/customers/{customerId}/reservations")
    ResponseEntity<List<Reservation>> getCustomerReservations(@PathVariable int customerId,
                                                              @RequestParam(required = false) boolean upcoming,
                                                              @RequestParam(required = false) int limit) {
        List<Reservation> reservations = _customerService.getCustomerReservations(customerId, upcoming, limit);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }
}
