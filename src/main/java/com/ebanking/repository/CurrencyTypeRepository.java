package com.ebanking.repository;

import com.ebanking.models.CurrencyType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CurrencyTypeRepository extends JpaRepository<CurrencyType, Long> {

    Optional<CurrencyType> findByNameEquals(String name);
}
