package com.ebanking.controller;

import com.ebanking.dto.BankAccountDto;
import com.ebanking.dto.CurrencyTypeDto;
import com.ebanking.dto.RegistrationDto;
import com.ebanking.dto.TransactionDto;
import com.ebanking.service.BankAccountService;
import com.ebanking.service.TransactionService;
import com.ebanking.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class TransactionController {
    private final BankAccountService bankAccountService;
    private final TransactionService transactionService;
    private final UserService userService;

    public TransactionController(BankAccountService bankAccountService, TransactionService transactionService, UserService userService) {
        this.bankAccountService = bankAccountService;
        this.transactionService = transactionService;
        this.userService = userService;
    }

    @PostMapping("/transaction/new")
    public String createTransaction(@Valid @ModelAttribute("transaction") TransactionDto transactionDto) {
        String transactionValidation = this.transactionService.createTransaction(transactionDto);

        BankAccountDto bankAccountDto = this.bankAccountService.findBankAccountByNumber(transactionDto.getSender());

        if (!transactionValidation.equalsIgnoreCase("Success")) {
            return "redirect:/user/" + bankAccountDto.getId() + "/account?transactionValidation=failed";
        }

        return "redirect:/user/" + bankAccountDto.getId() + "/account";
    }
}
