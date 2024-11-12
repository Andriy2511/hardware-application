package com.example.hardware.listener;

import com.example.hardware.model.Role;
import com.example.hardware.model.User;
import com.example.hardware.service.IRoleService;
import com.example.hardware.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class RoleInitializationListener implements ApplicationListener<ContextRefreshedEvent> {

    private final IRoleService roleService;
    private final IUserService userService;
    private final PasswordEncoder passwordEncoder;
    private final String userRole = "USER";
    private final String adminRole = "ADMIN";

    @Autowired
    public RoleInitializationListener(IRoleService roleService, IUserService userService, PasswordEncoder passwordEncoder) {
        this.roleService = roleService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        initializeRoles();
        registerAdminIfAbsent();
    }

    private void initializeRoles() {
        if (roleService.getRoleByName(adminRole) == null) {
            roleService.createRole(new Role(adminRole));
        }
        if (roleService.getRoleByName(userRole)== null) {
            roleService.createRole(new Role(userRole));
        }
    }

    private void registerAdminIfAbsent(){
        Role role = roleService.getRoleByName(adminRole);
        if(userService.findAllUsersByRole(role).isEmpty()) {
            User user = new User();
            user.setName("Admin");
            user.setSurname("Admin");
            user.setLogin("Admin");
            String encodedPassword = passwordEncoder.encode("Admin");
            user.setPassword(encodedPassword);
            user.setRole(roleService.getRoleByName(adminRole));
            userService.registerUser(user);
        }
    }
}
