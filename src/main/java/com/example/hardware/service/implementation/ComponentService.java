package com.example.hardware.service.implementation;

import com.example.hardware.model.Component;
import com.example.hardware.model.Developer;
import com.example.hardware.repository.ComponentRepository;
import com.example.hardware.service.IComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComponentService implements IComponentService {

    private final ComponentRepository componentRepository;

    @Autowired
    public ComponentService(ComponentRepository componentRepository) {
        this.componentRepository = componentRepository;
    }

    @Override
    public void addComponent(Component component){
        componentRepository.save(component);
    }

    @Override
    public Component findComponentById(Long id){
        return componentRepository.findById(id).get();
    }

    @Override
    public Page<Component> findComponentsWithPaginationAndSorting(int page, int pageSize, String sortedField){
        return componentRepository.findAll(PageRequest.of(page, pageSize).withSort(Sort.by(sortedField)));
    }

    @Override
    public Page<Component> findComponentsByDeveloperWithPaginationAndSorting(int page, int pageSize, Developer developer) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return componentRepository.findComponentsByDeveloperWithPaginationAndSorting(developer, pageable);
    }

    @Override
    public Long selectCountOfComponents(){
        return componentRepository.count();
    }

    @Override
    public Long getComponentsCount() {
        return componentRepository.count();
    }

    @Override
    public List<Component> getComponentsCountByDeveloper(Developer developer) {
        return componentRepository.findComponentByDeveloper(developer);
    }

    @Override
    public List<Component> findAllComponentsWithPagination(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return componentRepository.findAll(pageable).getContent();
    }

    @Override
    public Component findComponentByName(String name) {
        return componentRepository.findComponentByName(name);
    }

}
