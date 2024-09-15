package com.ebanking.service.impl;

import com.ebanking.exceptions.CurrencyTypeNotFoundException;
import com.ebanking.models.CurrencyType;
import com.ebanking.repository.CurrencyTypeRepository;
import com.ebanking.service.CurrencyTypeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CurrencyTypeServiceImpl implements CurrencyTypeService {

    private final CurrencyTypeRepository currencyTypeRepository;

    @Override
    public List<CurrencyType> findAll() {
        return this.currencyTypeRepository.findAll();
    }

    @Override
    public CurrencyType findByName(String name) {

        Optional<CurrencyType> currencyTypeOptional = currencyTypeRepository.findByNameEquals(name);

        if (currencyTypeOptional.isEmpty()){
            throw new CurrencyTypeNotFoundException(name);
        }

        return currencyTypeOptional.get();
    }
}
