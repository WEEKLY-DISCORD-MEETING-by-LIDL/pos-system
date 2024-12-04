package com.example.wdmsystem.category.system;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final ICategoryRepository categoryRepository;
    public CategoryService(ICategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category createCategory(CategoryDTO request) {
        return null;
    }

    public List<Category> getCategories() {
        return null;
    }

    public void updateCategory(int categoryId, CategoryDTO request) {

    }

    public void deleteCategory(int categoryId) {

    }

}
