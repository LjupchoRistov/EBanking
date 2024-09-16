package com.ebanking.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Loader;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table
@Entity(name = "bank_account")
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // Number must contain 15 digits
    // 300 for country code

    private Boolean isDebit;
    private Double balance;
    private String accountNum;
    @CreationTimestamp
    private LocalDateTime dateCreatedOn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id")
    private CurrencyType currencyType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public boolean canSubstractAmount(Double amount){
        return this.balance - amount > 0;
    }

    public void substractAmount(Double amount) {
        this.balance = this.balance - amount;
    }

    public void addAmount(Double amount) {
        this.balance = this.balance + amount;
    }
}

