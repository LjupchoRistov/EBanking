package com.ebanking.repository;

import com.ebanking.models.CurrencyType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyTypeRepository extends JpaRepository<CurrencyType, Long> {
    CurrencyType findByNameEquals(String name);
}
