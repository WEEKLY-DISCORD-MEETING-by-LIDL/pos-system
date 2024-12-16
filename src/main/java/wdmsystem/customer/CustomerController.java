package wdmsystem.customer;

import org.springframework.security.access.prepost.PreAuthorize;
import wdmsystem.reservation.ReservationDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class CustomerController {

    private final CustomerService _customerService;

    public CustomerController(CustomerService customerService) {
        this._customerService = customerService;
    }

    @PostMapping("/customers")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER') or hasRole('REGULAR')")
    ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerDTO request) {
        CustomerDTO newCustomer = _customerService.createCustomer(request);
        return new ResponseEntity<>(newCustomer, HttpStatus.CREATED);
    }

    /// In the API contract Limit is an entity, but in the doc it isn't mentioned.
    @GetMapping("/customers")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER') or hasRole('REGULAR')")
    ResponseEntity<List<CustomerDTO>> getCustomers(@RequestParam(required = false) LocalDateTime createdAtMin,
                                                @RequestParam(required = false) LocalDateTime createdAtMax,
                                                @RequestParam(required = false) Integer limit) {
        List<CustomerDTO> customers = _customerService.getCustomers(createdAtMin, createdAtMax, limit);
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @GetMapping("/customers/{customerId}")
    @PreAuthorize("hasRole('ADMIN') or ((hasRole('OWNER') or hasRole('REGULAR')) and @customerService.isOwnedByCurrentUser(#customerId))")
    ResponseEntity<CustomerDTO> getCustomer(@PathVariable int customerId) {
        CustomerDTO customer = _customerService.getCustomer(customerId);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @PutMapping("/customers/{customerId}")
    @PreAuthorize("hasRole('ADMIN') or ((hasRole('OWNER') or hasRole('REGULAR')) and @customerService.isOwnedByCurrentUser(#customerId))")
    ResponseEntity<String> updateCustomer(@PathVariable int customerId, @RequestBody CustomerDTO request) {
        _customerService.updateCustomer(customerId, request);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    /// API contract has operation id DeleteCustomer, changed to deleteCustomer for naming consistency
    @DeleteMapping("/customers/{customerId}")
    @PreAuthorize("hasRole('ADMIN') or ((hasRole('OWNER') or hasRole('REGULAR')) and @customerService.isOwnedByCurrentUser(#customerId))")
    ResponseEntity<String> deleteCustomer(@PathVariable int customerId) {
        _customerService.deleteCustomer(customerId);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @GetMapping("/customers/{customerId}/reservations")
    @PreAuthorize("hasRole('ADMIN') or ((hasRole('OWNER') or hasRole('REGULAR')) and @customerService.isOwnedByCurrentUser(#customerId))")
    ResponseEntity<List<ReservationDTO>> getCustomerReservations(@PathVariable int customerId,
                                                              @RequestParam(required = false) boolean upcoming,
                                                              @RequestParam(required = false) Integer limit) {
        List<ReservationDTO> reservations = _customerService.getCustomerReservations(customerId, upcoming, limit);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }
}
