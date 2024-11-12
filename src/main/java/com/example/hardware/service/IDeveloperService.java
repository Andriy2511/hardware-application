package com.example.hardware.service;

import com.example.hardware.model.Developer;

import java.util.List;

public interface IDeveloperService {

    Developer addNewDeveloper(Developer developer);
    List<Developer> findAllDevelopers();
    List<Developer> findAllDevelopersById(List<Long> authorId);
    Developer findDeveloperById(Long id);
    Developer findDeveloperByName(String name);
    Developer changeDeveloper(Developer developer);
    Long getDevelopersCount();
    List<Developer> findAllDevelopersWithPagination(int page, int pageSize);
    boolean isDeveloperExist(Developer developer);
}