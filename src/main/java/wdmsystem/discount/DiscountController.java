package wdmsystem.discount;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
public class DiscountController {
    private final DiscountService _discountService;

    @PostMapping("/discounts")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    ResponseEntity<DiscountDTO> createDiscount(@RequestBody DiscountDTO request) {
        log.info("Received request to create discount: {}", request);
        DiscountDTO newDiscount = _discountService.createDiscount(request);
        log.info("Created discount: {}", newDiscount);
        return new ResponseEntity<>(newDiscount, HttpStatus.CREATED);
    }

    @GetMapping("/discounts")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER') or hasRole('REGULAR')")
    ResponseEntity<List<DiscountDTO>> getDiscounts() {
        log.info("Fetching all discounts");
        List<DiscountDTO> discountList = _discountService.getDiscounts();
        log.info("Fetched the following discounts: {}", discountList);
        return new ResponseEntity<>(discountList, HttpStatus.OK);
    }

    @GetMapping("/discounts/{discountId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('OWNER') and @discountService.isOwnedByCurrentUser(#discountId))")
    ResponseEntity<DiscountDTO> getDiscount(@PathVariable Integer discountId) {
        DiscountDTO discount = _discountService.getDiscount(discountId);
        return new ResponseEntity<>(discount, HttpStatus.OK);
    }

    @PutMapping("/discounts/{discountId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('OWNER') and @discountService.isOwnedByCurrentUser(#discountId))")
    ResponseEntity<DiscountDTO> updateDiscount(@PathVariable int discountId, @RequestBody DiscountDTO request) {
        log.info("Updating discount with id {} with the following content: {}", discountId, request);
        DiscountDTO updatedDiscount = _discountService.updateDiscount(discountId, request);
        log.info("Updated discount: {}", updatedDiscount);
        return new ResponseEntity<>(updatedDiscount, HttpStatus.OK);
    }

    @DeleteMapping("/discounts/{discountId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('OWNER') and @discountService.isOwnedByCurrentUser(#discountId))")
    ResponseEntity<String> deleteDiscount(@PathVariable int discountId) {
        log.info("Received reuqest to delete discount with ID: {}", discountId);
        _discountService.deleteDiscount(discountId);
        log.info("Deleted discount with ID: {}", discountId);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

}
