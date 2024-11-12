package com.example.hardware.repository;

import com.example.hardware.model.Developer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeveloperRepository extends JpaRepository<Developer, Long> {
    Boolean existsByNameIgnoreCase(String name);
    List<Developer> findDevelopersByNameIgnoreCase(String name);
}
