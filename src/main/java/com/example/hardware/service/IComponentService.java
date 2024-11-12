package com.example.hardware.service;

import com.example.hardware.model.Component;
import com.example.hardware.model.Developer;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IComponentService {

    void addComponent(Component component);
    Component findComponentById(Long id);
    Page<Component> findComponentsWithPaginationAndSorting(int page, int pageSize, String sortedField);
    Page<Component> findComponentsByDeveloperWithPaginationAndSorting(int page, int pageSize, Developer developer);
    Long selectCountOfComponents();
    Long getComponentsCount();
    List<Component> getComponentsCountByDeveloper(Developer developer);
    List<Component> findAllComponentsWithPagination(int page, int pageSize);

    Component findComponentByName(String name);

}