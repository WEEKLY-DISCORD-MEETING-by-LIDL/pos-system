package wdmsystem.tax;

import org.springframework.security.core.context.SecurityContextHolder;
import wdmsystem.auth.CustomUserDetails;
import wdmsystem.exception.InvalidInputException;
import wdmsystem.exception.NotFoundException;
import wdmsystem.merchant.IMerchantRepository;
import wdmsystem.merchant.Merchant;
import wdmsystem.utility.DTOMapper;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TaxService {
    private final ITaxRepository taxRepository;
    private final IMerchantRepository merchantRepository;
    private final DTOMapper dtoMapper;

    public TaxService(ITaxRepository taxRepository, DTOMapper dtoMapper,IMerchantRepository merchantRepository) {
        this.taxRepository = taxRepository;
        this.merchantRepository = merchantRepository;
        this.dtoMapper = dtoMapper;
    }

    public TaxDTO createTax(TaxDTO request) {

        if(request.title().length() > 30) {
            throw new InvalidInputException("Tax title is longer than 30 characters");
        }
        if(request.percentage() < 0) {
            throw new InvalidInputException("Tax percentage is negative");
        }

        Tax tax = dtoMapper.Tax_DTOToModel(request);

        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        Merchant merchant = merchantRepository.findById(currentUser.getMerchantId()).orElseThrow(
                () -> new NotFoundException("Merchant with ID " + currentUser.getMerchantId() + " not found")
        );

        tax.setMerchant(merchant);
        tax.createdAt = LocalDateTime.now();
        tax.updatedAt = LocalDateTime.now();

        return dtoMapper.Tax_ModelToDTO(taxRepository.save(tax));
    }

    public List<TaxDTO> getTaxes(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 50;
        }

        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        boolean isAdmin = currentUser.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        List<Tax> taxes;
        if(isAdmin) {
            taxes = taxRepository.findAll();
        } else {
            taxes = taxRepository.getTaxesByMerchantId(currentUser.getMerchantId(), Limit.of(limit));
        }
        List<TaxDTO> taxDTOs = new ArrayList<>();

        for (Tax tax : taxes) {
            taxDTOs.add(dtoMapper.Tax_ModelToDTO(tax));
        }

        return taxDTOs;
    }

    public TaxDTO getTax(int taxId) {

        Tax tax = taxRepository.findById(taxId).orElseThrow(() ->
                new NotFoundException("Tax with id " + taxId + " not found"));

        return dtoMapper.Tax_ModelToDTO(tax);
    }

    public void updateTax(int taxId, TaxDTO request) {

        Tax tax = taxRepository.findById(taxId).orElseThrow(() ->
                new NotFoundException("Tax with id " + taxId + " not found"));

        tax.title = request.title();
        tax.percentage = request.percentage();
        tax.updatedAt = LocalDateTime.now();

        taxRepository.save(tax);
    }

    public void deleteTax(int taxId) {
        if(taxRepository.existsById(taxId)) {
            taxRepository.deleteById(taxId);
        }
        else {
            throw new NotFoundException("Tax with id " + taxId + " not found");
        }
    }

    public boolean isOwnedByCurrentUser(int taxId) {
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        Tax tax = taxRepository.findById(taxId).orElseThrow(() ->
                new NotFoundException("Tax with id " + taxId + " not found"));

        return tax.getMerchant().id == currentUser.getMerchantId();
    }

}
