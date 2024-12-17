package wdmsystem.discount;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class DiscountController {
    private final DiscountService _discountService;

    @PostMapping("/discounts")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    ResponseEntity<DiscountDTO> createDiscount(@RequestBody DiscountDTO request) {
        DiscountDTO newDiscount = _discountService.createDiscount(request);
        return new ResponseEntity<>(newDiscount, HttpStatus.CREATED);
    }

    @GetMapping("/discounts")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER') or hasRole('REGULAR')")
    ResponseEntity<List<DiscountDTO>> getDiscounts() {
        List<DiscountDTO> discountList = _discountService.getDiscounts();
        return new ResponseEntity<>(discountList, HttpStatus.OK);
    }

    @PutMapping("/discounts/{discountId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('OWNER') and @discountService.isOwnedByCurrentUser(#discountId))")
    ResponseEntity<DiscountDTO> updateDiscount(@PathVariable int discountId, @RequestBody DiscountDTO request) {
        DiscountDTO updatedDiscount = _discountService.updateDiscount(discountId, request);
        return new ResponseEntity<>(updatedDiscount, HttpStatus.OK);
    }

    @DeleteMapping("/discounts/{discountId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('OWNER') and @discountService.isOwnedByCurrentUser(#discountId))")
    ResponseEntity<String> deleteDiscount(@PathVariable int discountId) {
        _discountService.deleteDiscount(discountId);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

}
