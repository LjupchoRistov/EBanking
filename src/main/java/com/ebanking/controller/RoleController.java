package com.ebanking.controller;

import com.ebanking.models.Role;
import com.ebanking.models.UserEntity;
import com.ebanking.security.SecurityUtil;
import com.ebanking.service.RoleService;
import com.ebanking.service.UserService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class RoleController {
    private final RoleService roleService;
    private final UserService userService;

    public RoleController(RoleService roleService, UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }

    @GetMapping("/users")
    public String listUsers(Model model){
        String username = SecurityUtil.getSessionUser();
        UserEntity user = this.userService.findByUsername(username);

        if (username == null || user == null)
            return "redirect:/login";

        List<Role> roles = user.getRoles();
        roles.removeIf(role -> !role.getName().equals("ADMIN"));

        if (roles.isEmpty())
            return "redirect:/user/accounts";


        List<UserEntity> users = this.userService.findAll();
        model.addAttribute("users", users);

        return "users-list";
    }

    @GetMapping("/users/{userId}/roles")
    public String userRoles(Model model,
                            @PathVariable("userId") long userId){
        String sessionUsername = SecurityUtil.getSessionUser();
        UserEntity sessionUser = this.userService.findByUsername(sessionUsername);

        if (sessionUsername == null || sessionUser == null)
            return "redirect:/login";

        List<Role> roles = sessionUser.getRoles();
        roles.removeIf(role -> !role.getName().equals("ADMIN"));

        if (roles.isEmpty())
            return "redirect:/user/accounts";

        UserEntity user = this.userService.findById(userId);

        List<Role> userRoles = user.getRoles();
        List<Role> allRoles = this.roleService.findAll();
        List<Role> missingRoles = new ArrayList<>();

        boolean isPresent;
        for (Role allRole : allRoles) {
            isPresent = false;
            for (Role userRole : userRoles) {
                if (allRole.equals(userRole)) {
                    isPresent = true;
                    break;
                }
            }

            if (!isPresent)
                missingRoles.add(allRole);
        }

        model.addAttribute("user", user);
        model.addAttribute("missingRoles", missingRoles);
        boolean hasMissingRoles = !missingRoles.isEmpty();
        model.addAttribute("hasMissingRoles", hasMissingRoles);
        return "users-roles";
    }

    @PostMapping("/role/add")
    public String addRole(Model model,
                          @RequestParam("userId") long userId,
                          @RequestParam("roleName") String roleName){

        try {
            Role role = this.roleService.findByName(roleName);
            UserEntity user = this.userService.findById(userId);

            if (role != null && user != null) {
                List<Role> roles = user.getRoles();
                roles.add(role);
                user.setRoles(roles);
            } else {
                // Handle the case where role or user is not found
                // You may want to redirect to an error page or log the issue
            }

            this.userService.saveUser(user);

            return "redirect:/users/" +
                    String.format("%d", userId) +
                    "/roles";
        } catch (Exception e) {
            // Handle any exceptions that might occur during the process
            e.printStackTrace(); // Log or print the stack trace for debugging
            // You may want to redirect to an error page or log the issue
            return "redirect:/error";
        }
    }

    @PostMapping("/role/remove")
    public String removeRole(Model model,
                             @RequestParam("userId") long userId,
                             @RequestParam("roleId") long roleId) {

        try {
            UserEntity user = this.userService.findById(userId);
            List<Role> roles = user.getRoles();
            roles.removeIf(role -> role.getId().equals(roleId));
            user.setRoles(roles);
            this.userService.saveUser(user);

            return "redirect:/users/" +
                    String.format("%d", userId) +
                    "/roles";
        } catch (Exception e) {
            // Handle any exceptions that might occur during the process
            e.printStackTrace(); // Log or print the stack trace for debugging
            // You may want to redirect to an error page or log the issue
            return "redirect:/error";
        }
    }
}
