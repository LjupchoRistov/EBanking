package com.ebanking.controller;

import com.example.racepulse.dto.RegistrationDto;
import com.example.racepulse.models.UserEntity;
import com.example.racepulse.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.example.racepulse.validator.EmailValidator.*;
import static com.example.racepulse.validator.PasswordValidator.*;

@Controller
public class AuthController {
    private UserService userService;

    public AuthController(UserService userService) {
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
                           HttpServletRequest request) {

        //todo: check if username or email is present
        UserEntity existingUserEmail = userService.findByEmail(user.getEmail());
        if (existingUserEmail != null && existingUserEmail.getEmail() != null && !existingUserEmail.getEmail().isEmpty()) {
            return "redirect:/register?fail=username-email";
        }
        UserEntity existingUserUsername = userService.findByUsername(user.getUsername());
        if (existingUserUsername != null && existingUserUsername.getUsername() != null && !existingUserUsername.getUsername().isEmpty()) {
            return "redirect:/register?fail=username-email";
        }

        //todo: check if everything is present
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "register";
        }

        //todo: check if password is strong
        if (!isValidPassword(user.getPassword())) {
            result.rejectValue("password", "error.user", "Password does not meet strength requirements");
        }

        //todo: check if email is valid
        if (!isValidEmail(user.getEmail())) {
            result.rejectValue("email", "error.user", "Email is not valid");
        }

        //todo: check if passwords match
        String password = user.getPassword();
        String repeatPassword = user.getRP();
        if (!password.equals(repeatPassword)) {
            result.rejectValue("repeatPassword", "error.user", "Passwords don't match");
        }

        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "register";
        }

        userService.saveUser(user);
        return "redirect:/news-articles";
    }
}
