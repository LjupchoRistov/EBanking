package com.ebanking.repository;

import com.ebanking.models.BankAccount;
import com.ebanking.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, String> {

    List<BankAccount> findAllByUserEquals(UserEntity user);

    List<BankAccount> findAllByUserUsername(String username);

    Optional<BankAccount> findByAccountNumEquals(String accountNum);
}
