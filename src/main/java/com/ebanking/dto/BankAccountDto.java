package com.ebanking.dto;

import com.ebanking.models.CurrencyType;
import com.ebanking.models.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountDto {
    private Long id;

    private String accountNum;
    private Boolean isDebit;
    private Double balance;
    private LocalDateTime dateCreatedOn;
    private CurrencyTypeDto currencyTypeDto;
    private UserEntity user;
}
