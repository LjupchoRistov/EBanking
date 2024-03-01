package com.ebanking.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.Table;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table
@Entity(name = "transaction")
public class Transaction {
    // Constructor with arguments
    public Transaction(Long id, String description, BankAccount sender, BankAccount receiver, CurrencyType currencyTypeSender, LocalDateTime transactionDate) {
        this.id = id;
        this.Description = description;
        this.sender = sender;
        this.receiver = receiver;
        this.currencyTypeSender = currencyTypeSender;
        this.transactionDate = transactionDate;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String Description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private BankAccount sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private BankAccount receiver;

    private Double amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id")
    private CurrencyType currencyTypeSender;

    @CreationTimestamp
    private LocalDateTime transactionDate;
}
