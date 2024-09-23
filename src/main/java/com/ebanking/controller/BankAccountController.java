package com.ebanking.controller;

import com.ebanking.dto.*;
import com.ebanking.models.CurrencyType;
import com.ebanking.models.UserEntity;
import com.ebanking.security.SecurityUtil;
import com.ebanking.service.*;
import com.ebanking.validator.PinValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class BankAccountController {

    private final BankAccountService bankAccountService;

    private final TransactionService transactionService;

    private final UserService userService;

    private final CurrencyTypeService currencyTypeService;

    private final BankAccountPinService bankAccountPinService;

    private final PinValidator pinValidator;

    @GetMapping("/user/accounts")
    public String getUserBankAccounts(Model model) {

        String username = SecurityUtil.getSessionUser();

        boolean isUserLoggedIn = userService.existsByUsername(username);

        if (!isUserLoggedIn) {
            return "redirect:/login";
        }

        UserDisplayDto user = userService.getUserDisplayDto(username);

        List<BankAccountDto> accounts = bankAccountService.findBankAccountsByUsername(username);

        model.addAttribute("user", user);

        model.addAttribute("accounts", accounts);

        model.addAttribute("activeBankAccounts", this.bankAccountService.activeBankAccounts(username));

        model.addAttribute("maxBankAccountNum", BankAccountService.MAX_BANK_ACCOUNTS);

        return "accounts-list";
    }

    @GetMapping("/user/account-new")
    public String createBankAccount(Model model) {

        String username = SecurityUtil.getSessionUser();

        Optional<UserEntity> userOptional = userService.findByUsernameOptional(username);

        if (userOptional.isEmpty() || username == null) {
            return "redirect:/login";
        }

        UserEntity user = userOptional.get();

        BankAccountRegistrationForm form = new BankAccountRegistrationForm();

        model.addAttribute("form", form);

        List<CurrencyType> currencies = currencyTypeService.findAll();

        model.addAttribute("currencies", currencies);

        model.addAttribute("user", user);

        return "accounts-new";
    }

    @PostMapping("/user/account-new")
    public String createBankAccount(@ModelAttribute("form") BankAccountRegistrationForm form,
                                    BindingResult bindingResult,
                                    Model model) {

        String username = SecurityUtil.getSessionUser();

        Optional<UserEntity> userOptional = userService.findByUsernameOptional(username);

        if (userOptional.isEmpty() || username == null) {
            return "redirect:/login";
        }

        UserEntity user = userOptional.get();

        String pin = form.getPin();

        String repeatPin = form.getRepeatPin();

        CurrencyType currency = currencyTypeService.findByName(form.getCurrencyType());

        model.addAttribute("form", form);

        List<CurrencyType> currencies = currencyTypeService.findAll();

        model.addAttribute("currencies", currencies);

        model.addAttribute("user", user);

        if (!pinValidator.validatePin(pin)) {

            bindingResult.rejectValue("pin", "error.pin.invalid.format", "Provided pin doesn't meet format " +
                    "requirements!");

            return "accounts-new";
        }

        if (!bankAccountPinService.doesPinMatch(pin, repeatPin)) {

            bindingResult.rejectValue("repeatPin", "error.pin.no.match", "Provided pin's doesn't match!");

            return "accounts-new";
        }

        this.bankAccountService.createBankAccount(currency, Integer.valueOf(pin), username);

        return "redirect:/user/accounts";
    }

    @GetMapping("/user/{bankAccountNum}/account")
    public String previewAccount(@PathVariable String bankAccountNum,
                                 @RequestParam(required = false) String transactionValidation,
                                 Model model) {

        String username = SecurityUtil.getSessionUser();

        boolean isUserLoggedIn = userService.existsByUsername(username);

        if (!isUserLoggedIn) {
            return "redirect:/login";
        }

        BankAccountDto bankAccountDto = this.bankAccountService.findBankAccountByNumber(bankAccountNum);

        model.addAttribute("account", bankAccountDto);

        UserDisplayDto user = userService.getUserDisplayDto(username);
        model.addAttribute("user", user);

        List<TransactionDto> transactionDtoList = this.transactionService.findAllByBankAccountNum(bankAccountNum);
        model.addAttribute("transactions", transactionDtoList);

        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setSender(String.valueOf(bankAccountDto.getAccountNum()));

        CurrencyType currencyType = bankAccountDto.getCurrencyType();
        transactionDto.setCurrencyTypeSender(currencyType.getName());

        model.addAttribute("transaction", transactionDto);

        if (transactionValidation == null || transactionValidation.isBlank())
            model.addAttribute("transactionValidation", "OK");
        else model.addAttribute("transactionValidation", transactionValidation);

        return "accounts-details";
    }

    @PostMapping("/user/{bankAccountNumber}/delete")
    public String deleteBankAccount(@PathVariable String bankAccountNumber) {

        this.bankAccountService.deleteBankAccount(bankAccountNumber);
        return "redirect:/user/accounts";
    }

    @GetMapping("/user/{bankAccountNum}/account-verify")
    public String getVerifyPinView(@PathVariable String bankAccountNum, Model model) {

        model.addAttribute("bankAccountNum", bankAccountNum);

        model.addAttribute("pinVerificationDto", new PinVerificationDto());

        return "account-pin-verification";
    }

    @PostMapping("/user/{bankAccountNum}/account-verify")
    public String verifyPin(@PathVariable String bankAccountNum,
                            @ModelAttribute("pinVerificationDto") @Valid PinVerificationDto pinVerificationDto,
                            BindingResult bindingResult,
                            Model model) {

        String username = SecurityUtil.getSessionUser();

        boolean isUserLoggedIn = userService.existsByUsername(username);

        if (!isUserLoggedIn) {
            return "redirect:/login";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("bankAccountId", bankAccountNum);
            return "account-pin-verification";
        }

        boolean isPinValid =
                bankAccountPinService.isPinValid(pinVerificationDto.getPin(), bankAccountNum);

        if (!isPinValid) {

            bindingResult.rejectValue("pin", "pin.invalid", "Invalid pin");

            model.addAttribute("bankAccountId", bankAccountNum);

            return "account-pin-verification";
        }

        return "redirect:/user/" + bankAccountNum + "/account";
    }
}
