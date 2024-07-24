package com.ebanking.service;

import com.ebanking.models.CurrencyType;

import java.util.List;
import java.util.Optional;

public interface CurrencyTypeService {

    List<CurrencyType> findAll();

    CurrencyType findByName(String name);
}
