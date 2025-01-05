package com.ebanking.service.impl;

import com.ebanking.models.CurrencyType;
import com.ebanking.repository.CurrencyTypeRepository;
import com.ebanking.service.CurrencyTypeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurrencyTypeServiceImpl implements CurrencyTypeService {

    private final CurrencyTypeRepository currencyTypeRepository;

    public CurrencyTypeServiceImpl(CurrencyTypeRepository currencyTypeRepository) {
        this.currencyTypeRepository = currencyTypeRepository;
    }

    @Override
    public List<CurrencyType> findAll() {
        return this.currencyTypeRepository.findAll();
    }
}
