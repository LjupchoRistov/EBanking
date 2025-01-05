package com.ebanking.mapper;

import com.ebanking.dto.CurrencyTypeDto;
import com.ebanking.dto.TransactionDto;
import com.ebanking.models.CurrencyType;
import com.ebanking.models.Transaction;

public class CurrencyTypeMapper {
    public static CurrencyType mapToCurrencyType(CurrencyTypeDto currencyTypeDto){
        return  CurrencyType.builder()
                .id(currencyTypeDto.getId())
                .name(currencyTypeDto.getName())
                .value(currencyTypeDto.getValue())
                .build();
    }

    public static CurrencyTypeDto mapToCurrencyTypeDto(CurrencyType currencyType){
        return  CurrencyTypeDto.builder()
                .id(currencyType.getId())
                .name(currencyType.getName())
                .value(currencyType.getValue())
                .build();
    }
}
