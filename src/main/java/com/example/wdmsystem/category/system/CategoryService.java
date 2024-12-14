package com.example.wdmsystem.category.system;

import com.example.wdmsystem.auth.CustomUserDetails;
import com.example.wdmsystem.employee.system.Employee;
import com.example.wdmsystem.exception.NotFoundException;
import org.springframework.data.domain.Limit;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final ICategoryRepository categoryRepository;
    public CategoryService(ICategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category createCategory(CategoryDTO request) {
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        Category category = new Category(
                0,
                currentUser.getMerchantId(),
                request.title()
        );
        return categoryRepository.save(category);
    }

    public List<Category> getCategories() {
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        boolean isAdmin = currentUser.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        if(isAdmin) {
            return categoryRepository.findAll();
        } else {
            return categoryRepository.findByMerchantId(currentUser.getMerchantId());
        }
    }

    public Category updateCategory(int categoryId, CategoryDTO request) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new NotFoundException("Category with id " + categoryId + " not found"));

        category.setTitle(request.title());

        return categoryRepository.save(category);
    }

    public void deleteCategory(int categoryId) {
        if (categoryRepository.existsById(categoryId)) {
            categoryRepository.deleteById(categoryId);
        }
        else {
            throw new NotFoundException("Category with id " + categoryId + " not found");
        }
    }

    public boolean isOwnedByCurrentUser(int categoryId) {
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new NotFoundException("Category with id " + categoryId + " not found"));

        return category.getMerchantId() == currentUser.getMerchantId();
    }
}
