package com.ebanking.dto;

import com.ebanking.models.BankAccount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
    private Long id;
    @NotEmpty(message = "Description cannot be empty!")
    private String description;
    private String sender;
    @NotEmpty(message = "Receiver cannot be empty!")
    private String receiver;
    @NotEmpty(message = "Amount cannot be empty!")
    private String amount;
    private String currencyTypeSender;
    private LocalDateTime transactionDate;
}