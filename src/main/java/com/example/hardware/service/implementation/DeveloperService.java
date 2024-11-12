package com.example.hardware.service.implementation;

import com.example.hardware.model.Developer;
import com.example.hardware.repository.DeveloperRepository;
import com.example.hardware.service.IDeveloperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeveloperService implements IDeveloperService {

    private final DeveloperRepository developerRepository;

    @Autowired
    public DeveloperService(DeveloperRepository developerRepository) {
        this.developerRepository = developerRepository;
    }

    @Override
    public Developer addNewDeveloper(Developer developer){
        return developerRepository.save(developer);
    }

    @Override
    public List<Developer> findAllDevelopers(){
        return developerRepository.findAll();
    }

    @Override
    public List<Developer> findAllDevelopersById(List<Long> developerId){
        return developerRepository.findAllById(developerId);
    }

    @Override
    public Developer findDeveloperById(Long id){
        return developerRepository.findById(id).get();
    }

    @Override
    public Developer findDeveloperByName(String name) {
        List<Developer> developers = developerRepository.findDevelopersByNameIgnoreCase(name);
        return developers.size() > 0 ? developers.get(0) : null;
    }

    @Override
    public Developer changeDeveloper(Developer developer) {
        return developerRepository.save(developer);
    }

    @Override
    public Long getDevelopersCount() {
        return developerRepository.count();
    }

    @Override
    public List<Developer> findAllDevelopersWithPagination(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return developerRepository.findAll(pageable).getContent();
    }

    @Override
    public boolean isDeveloperExist(Developer developer) {
        return (developerRepository.existsByNameIgnoreCase(developer.getName()));
    }
}
