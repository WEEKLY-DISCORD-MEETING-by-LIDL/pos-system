package com.example.wdmsystem.category.system;

import com.example.wdmsystem.auth.*;
import com.example.wdmsystem.exception.NotFoundException;
import com.example.wdmsystem.merchant.system.IMerchantRepository;
import com.example.wdmsystem.merchant.system.Merchant;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final ICategoryRepository categoryRepository;
    private final IMerchantRepository merchantRepository;

    public CategoryService(ICategoryRepository categoryRepository, IMerchantRepository merchantRepository) {
        this.categoryRepository = categoryRepository;
        this.merchantRepository = merchantRepository;
    }

    public Category createCategory(CategoryDTO request) {
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        Merchant merchant = merchantRepository.findById(currentUser.getMerchantId()).orElseThrow(
                () -> new NotFoundException("Merchant with ID " + currentUser.getMerchantId() + " not found")
        );
        Category category = new Category(
                0,
                merchant,
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

        return category.getMerchant().getId() == currentUser.getMerchantId();
    }
}
