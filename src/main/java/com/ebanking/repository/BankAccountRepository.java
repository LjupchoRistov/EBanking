package com.ebanking.repository;

import com.ebanking.models.BankAccount;
import com.ebanking.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    List<BankAccount> findAllByUserEquals(UserEntity user);
    BankAccount findByAccountNumEquals(String accountNum);
}
