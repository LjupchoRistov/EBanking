package com.ebanking.repository;

import com.ebanking.dto.TransactionDto;
import com.ebanking.models.BankAccount;
import com.ebanking.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findAllBySenderEquals(BankAccount sender);

    List<Transaction> findAllByReceiverEquals(BankAccount receiver);

    List<Transaction> findAllBySender_AccountNumOrReceiver_AccountNum(String senderAccount,
                                                                      String receiverAccount);
    void deleteAllBySender_AccountNum(String senderAccount);

    void deleteAllByReceiver_AccountNum(String receiverAccount);
}
