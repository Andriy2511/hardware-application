package com.example.hardware.service;

import com.example.hardware.model.Category;

public interface ICategoryService {
    Category addCategory(Category category);
    Category findCategoryByName(String name);
}
