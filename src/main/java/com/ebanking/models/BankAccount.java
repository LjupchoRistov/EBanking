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
    private String accountNum;

    private Boolean isDebit;

    private Double balance;

    @CreationTimestamp
    private LocalDate dateCreatedOn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id")
    private CurrencyType currencyType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private String hashedPin;

    private String salt;

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

