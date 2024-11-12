package com.example.hardware.service;

import com.example.hardware.model.Role;
import com.example.hardware.model.User;

import java.util.List;

public interface IRoleService {

    List<Role> findRoleByName(String name);

    List<Role> findAllRoleByName(String name);

    boolean isUserContainRole(User user, String roleName);
    Role getRoleByName(String name);

    Role createRole(Role role);
}
