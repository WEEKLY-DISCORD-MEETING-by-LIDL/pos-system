package com.example.wdmsystem.tax.system;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaxService {
    private final ITaxRepository taxRepository;

    public TaxService(ITaxRepository taxRepository) {
        this.taxRepository = taxRepository;
    }

    public Tax createTax(TaxDTO request) {
        return null;
    }

    public List<Tax> getTaxes(int limit) {
        return null;
    }

    public Tax getTax(int taxId) {
        return null;
    }

    public void updateTax(int taxId) {
        
    }

    public void deleteTax(int taxId) {

    }

}
