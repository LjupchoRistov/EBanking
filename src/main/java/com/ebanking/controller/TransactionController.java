package com.ebanking.controller;

import com.ebanking.dto.BankAccountDto;
import com.ebanking.dto.TransactionDto;
import com.ebanking.service.BankAccountService;
import com.ebanking.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@AllArgsConstructor
@Controller
public class TransactionController {

    private final BankAccountService bankAccountService;

    private final TransactionService transactionService;

    @PostMapping("/transaction/new")
    public String createTransaction(@Valid @ModelAttribute("transaction") TransactionDto transactionDto) {

        String transactionValidation = this.transactionService.createTransaction(transactionDto);

        BankAccountDto bankAccountDto = this.bankAccountService.findBankAccountByNumber(transactionDto.getSender());

        if (!transactionValidation.equalsIgnoreCase("Success")) {
            return "redirect:/user/" + bankAccountDto.getAccountNum() + "/account?transactionValidation=failed";
        }

        return "redirect:/user/" + bankAccountDto.getAccountNum() + "/account";
    }
}
