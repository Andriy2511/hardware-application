package com.example.hardware.controller;

import com.example.hardware.DTO.UserDTO;
import com.example.hardware.model.User;
import com.example.hardware.service.IRoleService;
import com.example.hardware.service.IUserService;
import com.example.hardware.service.implementation.RoleService;
import com.example.hardware.service.implementation.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Slf4j
@Controller
public class RegistrationController {
    private final IUserService userService;
    private final IRoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationController(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder){
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/registration")
    public String showRegistrationForm(Model model){
        model.addAttribute("userDTO", new UserDTO());
        return "registration";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("userDTO") @Valid UserDTO userDTO, BindingResult result){
        if(result.hasErrors()){
            return "registration";
        }

        try {
            if (userService.isUserExistCheckByLogin(userDTO.getLogin())) {
                log.info("A user with this login has already exists, username: {}", userDTO.getLogin());
                result.rejectValue("login", "error.duplicateUsername", "A user with this name has already exist");
                return "registration";
            }

            userService.registerUser(getUserFromDTO(userDTO));
            System.out.println(getUserFromDTO(userDTO));
            return "redirect:login";
        } catch (Exception e){
            log.error("Error while adding to the database: \n{}", e.getMessage());
            result.rejectValue( "database.addingReaderError", "Error while adding to the database: " + e.getMessage());
            return "registration";
        }
    }

    private User getUserFromDTO(UserDTO userDTO){
        User user = new User();
        user.setName(userDTO.getName());
        user.setSurname(userDTO.getSurname());
        user.setLogin(userDTO.getLogin());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRole(roleService.findRoleByName("USER").get(0));

        return user;
    }
}
