package com.ebanking.controller;

import com.ebanking.dto.RegistrationDto;
import com.ebanking.models.UserEntity;
import com.ebanking.service.UserService;
import com.ebanking.service.impl.RecaptchaResponseServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Optional;

import static com.ebanking.validator.EmailValidator.isValidEmail;
import static com.ebanking.validator.PasswordValidator.isValidPassword;


@Controller
public class AuthController {

    private final RecaptchaResponseServiceImpl validator;

    private final UserService userService;

    public AuthController(RecaptchaResponseServiceImpl validator, UserService userService) {
        this.validator = validator;
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String getRegisterForm(Model model) {

        RegistrationDto user = new RegistrationDto();
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/register/save")
    public String register(@Valid @ModelAttribute("user") RegistrationDto user,
                           BindingResult result,
                           Model model,
                           @RequestParam(name = "g-recaptcha-response") String captcha) {

        Optional<UserEntity> existingUserEmail = userService.findByEmailOptional(user.getEmail());

        if (existingUserEmail.isPresent() && existingUserEmail.get().getEmail() != null &&
                !existingUserEmail.get().getEmail().isEmpty()) {
            return "redirect:/register?fail=username-email";
        }

        Optional<UserEntity> existingUserUsername = userService.findByUsernameOptional(user.getUsername());

        if (existingUserUsername.isPresent() && existingUserUsername.get().getUsername() != null &&
                !existingUserUsername.get().getUsername().isEmpty()) {
            return "redirect:/register?fail=username-email";
        }

        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "register";
        }

        if (!isValidPassword(user.getPassword())) {
            result.rejectValue("password", "error.user", "Password does not meet strength requirements");
        }

        if (!isValidEmail(user.getEmail())) {
            result.rejectValue("email", "error.user", "Email is not valid");
        }

        String password = user.getPassword();
        String repeatPassword = user.getRP();
        if (!password.equals(repeatPassword)) {
            result.rejectValue("repeatPassword", "error.user", "Passwords don't match");
        }

        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "register";
        }

        if (!validator.validateCaptcha(captcha)) {
            model.addAttribute("message", "Please Verify Captcha");
            return "register";
        }

        userService.saveUser(user);
        return "redirect:/login";
    }
}
