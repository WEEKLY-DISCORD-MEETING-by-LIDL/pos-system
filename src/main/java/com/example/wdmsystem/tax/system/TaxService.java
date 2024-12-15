package com.example.wdmsystem.tax.system;

import com.example.wdmsystem.exception.InvalidInputException;
import com.example.wdmsystem.exception.NotFoundException;
import com.example.wdmsystem.utility.DTOMapper;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TaxService {
    private final ITaxRepository taxRepository;
    private final DTOMapper dtoMapper;

    public TaxService(ITaxRepository taxRepository, DTOMapper dtoMapper) {
        this.taxRepository = taxRepository;
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

        tax.createdAt = LocalDateTime.now();
        tax.updatedAt = LocalDateTime.now();

        return dtoMapper.Tax_ModelToDTO(taxRepository.save(tax));
    }

    public List<TaxDTO> getTaxes(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 50;
        }

        // change this to pulling merchant id from user
        int merchantId = 10;

        List<Tax> taxes = taxRepository.getTaxesByMerchantId(merchantId, Limit.of(limit));
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

}
