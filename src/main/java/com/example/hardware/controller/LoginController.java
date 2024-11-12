package com.example.hardware.controller;

import com.example.hardware.DTO.UserDTO;
import com.example.hardware.model.User;
import com.example.hardware.service.IRoleService;
import com.example.hardware.service.implementation.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Collection;

@Controller
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final IRoleService roleService;

    @Autowired
    public LoginController(AuthenticationManager authenticationManager, RoleService roleService) {
        this.authenticationManager = authenticationManager;
        this.roleService = roleService;
    }

    @GetMapping("/login")
    public String showLoginForm(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("userDTO", new UserDTO());
        model.addAttribute("isAdmin" , roleService.isUserContainRole(user, "ADMIN"));
        return "login";
    }

    @PostMapping("/authorize")
    public String loginUser(@ModelAttribute("userDTO") @Valid UserDTO userDTO, BindingResult result) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDTO.getLogin(), userDTO.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return getPageByRole(authentication);
        } catch (AuthenticationException e) {
            e.printStackTrace();
            result.rejectValue("login", "error.invalidCredentials", "Invalid login or password");
            return "login";
        }
    }

    private String getPageByRole(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ADMIN"))) {
            return "redirect:/admin/showComponentsList";
        } else if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals("USER"))) {
            return "redirect:/catalog/showComponentsCatalog";
        } else {
            return "redirect:/registration";
        }
    }
}