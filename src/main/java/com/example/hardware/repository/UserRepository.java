package com.example.hardware.repository;

import com.example.hardware.model.Role;
import com.example.hardware.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findAllByName(String username);
    Boolean existsByLogin(String login);
    List<User> findAllByRoleIn(List<Role> roles);
    List<User> findAllByRole(Role role);
    List<User> findAllByRoleIn(List<Role> roles, Pageable pageable);
    Long countUsersByRole(Role role);
}
