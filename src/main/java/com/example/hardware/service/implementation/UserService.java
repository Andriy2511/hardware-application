package com.example.hardware.service.implementation;

import com.example.hardware.model.Role;
import com.example.hardware.model.User;
import com.example.hardware.repository.UserRepository;
import com.example.hardware.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserService implements IUserService {
    UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public void registerUser(User user){
        userRepository.save(user);
    }

    @Override
    public boolean isUserExistCheckByLogin(String login){
        return userRepository.existsByLogin(login);
    }

    @Override
    public User findUserByName(String username){
        return userRepository.findAllByName(username).get();
    }

    @Override
    public User findUserById(Long id){
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public void saveUser(User user){
        userRepository.save(user);
    }

    @Override
    public List<User> findAllUsersByRoles(List<Role> roleList) {return userRepository.findAllByRoleIn(roleList);}

    @Override
    public List<User> findAllUsersByRole(Role role) {
        return userRepository.findAllByRole(role);
    }

    @Override
    public List<User> findAllUsersByRolesWithPagination(List<Role> roleList, int numberOfPage, int recordPerPage){
        Pageable pageable = PageRequest.of(numberOfPage, recordPerPage);
        return userRepository.findAllByRoleIn(roleList, pageable);
    }

    @Override
    public Long getCountUsersByRole(Role role) {
        return userRepository.countUsersByRole(role);
    }
}
