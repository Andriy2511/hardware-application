package com.example.hardware.service;

import com.example.hardware.model.Role;
import com.example.hardware.model.User;

import java.util.List;

public interface IUserService {

    void registerUser(User user);

    boolean isUserExistCheckByLogin(String username);

    User findUserByName(String username);

    User findUserById(Long id);

    void saveUser(User user);

    List<User> findAllUsersByRoles(List<Role> roleList);
    List<User> findAllUsersByRole(Role role);

    List<User> findAllUsersByRolesWithPagination(List<Role> roleList, int numberOfPage, int recordsPerPage);

    Long getCountUsersByRole(Role role);
}

