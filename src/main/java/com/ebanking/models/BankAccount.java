package com.ebanking.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table
@Entity(name = "bank_accounts")
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean isDebit;

    private Double balance;

    private String accountNum;

    @CreationTimestamp
    private LocalDate dateCreatedOn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id")
    private CurrencyType currencyType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public BankAccount(Long id, Boolean isDebit, Double balance, String accountNum, LocalDate dateCreatedOn,
                       CurrencyType currencyType) {

        this.id = id;
        this.isDebit = isDebit;
        this.balance = balance;
        this.accountNum = accountNum;
        this.dateCreatedOn = dateCreatedOn;
        this.currencyType = currencyType;
    }

    public boolean canSubstractAmount(Double amount) {
        return this.balance - amount > 0;
    }

    public void substractAmount(Double amount) {
        this.balance = this.balance - amount;
    }

    public void addAmount(Double amount) {
        this.balance = this.balance + amount;
    }
}

