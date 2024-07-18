package com.ebanking.controller;

import com.ebanking.dto.BankAccountDto;
import com.ebanking.dto.UserDisplayDto;
import com.ebanking.security.SecurityUtil;
import com.ebanking.service.BankAccountService;
import com.ebanking.service.CurrencyTypeService;
import com.ebanking.service.TransactionService;
import com.ebanking.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class BankAccountController {
    private final BankAccountService bankAccountService;
    private final TransactionService transactionService;
    private final UserService userService;
    private final CurrencyTypeService currencyTypeService;

    public BankAccountController(BankAccountService bankAccountService, TransactionService transactionService,
                                 UserService userService, CurrencyTypeService currencyTypeService) {
        this.bankAccountService = bankAccountService;
        this.transactionService = transactionService;
        this.userService = userService;
        this.currencyTypeService = currencyTypeService;
    }

    @GetMapping("/user/accounts")
    public String getUserBankAccount(Model model) {

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

//    @GetMapping("/user/account-new")
//    public String createBankAccount(Model model) {
//        //todo: Implement dynamic real life user
//        String username = SecurityUtil.getSessionUser();
//        Optional<UserEntity> user = userService.findByUsernameOptional(username);
//        if (user.isEmpty() || username == null) {
//            return "redirect:/login"; // Redirect to login if the user is not authenticated
//        }
//
//        // Add user
//        model.addAttribute("user", user);
//
//        // Add bank account num
//        model.addAttribute("bankAccountNum", this.bankAccountService.activeBankAccounts(username));
//
//        // Add max bank account num
//        model.addAttribute("maxBankAccountNum", BankAccountService.MAX_BANK_ACCOUNTS);
//
//        // Current date
//        model.addAttribute("date", LocalDate.now());
//
//        // BankAccount template
//        model.addAttribute("bankAccount", new BankAccountDto());
//
//        // Currency type
//        model.addAttribute("currencies", this.currencyTypeService.findAll());
//
//        return "accounts-new";
//    }
//
//    @PostMapping("/user/account-new")
//    public String createBankAccount(@RequestParam String currency) {
//
//        String username = SecurityUtil.getSessionUser();
//        Optional<UserEntity> user = userService.findByUsernameOptional(username);
//
//        if (user.isEmpty()) {
//            return "redirect:/login"; // Redirect to login if the user is not authenticated
//        }
//
//        this.bankAccountService.createBankAccount(currency, user.orElse(null));
//        return "redirect:/user/accounts";
//
//    }
//
//    @GetMapping("/user/{id}/account")
//    public String previewAccount(@PathVariable Long id,
//                                 @RequestParam(required = false) String transactionValidation,
//                                 Model model) {
//
//        // Add the Bank Account to the model
//        BankAccountDto bankAccountDto = this.bankAccountService.findBankAccountById(id);
//        model.addAttribute("account", bankAccountDto);
//
//        // Add the user of the acc
//        UserDisplayDto user = userService.getUserDisplayDto()
//        model.addAttribute("user", this.userService.findByUsernameOptional(user.getUsername()));
//
//        // Add the Treansactions of the Bank Account
//        List<TransactionDto> transactionDtoList = this.transactionService.findAllByBankAccount(bankAccountDto);
//        model.addAttribute("transactions", transactionDtoList);
//
//        // Add TransactionDto template
//        TransactionDto transactionDto = new TransactionDto();
//        transactionDto.setSender(String.valueOf(bankAccountDto.getAccountNum()));
//        transactionDto.setCurrencyTypeSender(bankAccountDto.getCurrencyTypeDto().getName());
//        model.addAttribute("transaction", transactionDto);
//
//        if (transactionValidation == null || transactionValidation.isBlank())
//            model.addAttribute("transactionValidation", "OK");
//        else model.addAttribute("transactionValidation", transactionValidation);
//
//        return "accounts-details";
//    }
//
//    @PostMapping("/user/{id}/delete")
//    public String deleteBankAccount(@PathVariable Long id) {
//
//        this.bankAccountService.deleteBankAccount(id);
//        return "redirect:/user/accounts";
//
//    }

//    @GetMapping("/user/verify")
//    public String getVerifyPin(Model model,@RequestParam (required = false) String error ){
//        String username= SecurityUtil.getSessionUser();
//        UserEntity user=userService.findByUsername(username);
//        if (user == null || username==null) {
//            return "redirect:/login"; // Redirect to login if the user is not authenticated
//        }
//        if(error!=null){
//            model.addAttribute("pinError","Incorrect PIN try again");
//        }
//
//      return "verify-pin";
//    }

//    @PostMapping("/verify-user")
//    public String verifyPin(@RequestParam String pin,RedirectAttributes redirectAttributes){
//        String username=SecurityUtil.getSessionUser();
//       UserEntity  user=userService.findByUsername(username);
//      if(userService.checkPin(pin,user)){
//          return "redirect:/user/accounts";
//      }
//      else{
//          redirectAttributes.addAttribute("error","pinError");
//       return "redirect:/user/verify";
//      }
//
//    }
}
