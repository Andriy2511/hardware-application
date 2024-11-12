package com.example.hardware.repository;

import com.example.hardware.model.Component;
import com.example.hardware.model.Developer;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface ComponentRepository extends JpaRepository<Component, Long> {
    @Query("SELECT c FROM Component c WHERE c.developer = :developer")
    Page<Component> findComponentsByDeveloperWithPaginationAndSorting(Developer developer, Pageable pageable);

    Component findComponentByName(String name);
    List<Component> findComponentByDeveloper(Developer developer);
}
