package com.example.hardware.service.implementation;

import com.example.hardware.model.Category;
import com.example.hardware.repository.CategoryRepository;
import com.example.hardware.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService implements ICategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category addCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category findCategoryByName(String name) {
        List<Category> categories = categoryRepository.findCategoriesByNameIgnoreCase(name);
        return categories.size() > 0 ? categories.get(0) : null;
    }
}