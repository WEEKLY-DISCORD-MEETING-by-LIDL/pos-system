package wdmsystem.category;

import wdmsystem.auth.CustomUserDetails;
import wdmsystem.exception.NotFoundException;
import wdmsystem.merchant.IMerchantRepository;
import wdmsystem.merchant.Merchant;
import wdmsystem.utility.DTOMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CategoryService {

    private final ICategoryRepository categoryRepository;
    private final IMerchantRepository merchantRepository;
    private final DTOMapper dtoMapper;

    public CategoryDTO createCategory(CategoryDTO request) {
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
        categoryRepository.save(category);
        return dtoMapper.Category_ModelToDTO(category);
    }

    public List<CategoryDTO> getCategories() {
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        boolean isAdmin = currentUser.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        List<Category> categoryList;
        if(isAdmin) {
            categoryList = categoryRepository.findAll();
        } else {
            categoryList = categoryRepository.findByMerchantId(currentUser.getMerchantId());
        }

        List<CategoryDTO> categoryDTOList = new ArrayList<>();
        for(Category category : categoryList) {
            categoryDTOList.add(dtoMapper.Category_ModelToDTO(category));
        }
        return categoryDTOList;
    }

    public CategoryDTO updateCategory(int categoryId, CategoryDTO request) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new NotFoundException("Category with id " + categoryId + " not found"));

        category.setTitle(request.title());
        categoryRepository.save(category);
        return dtoMapper.Category_ModelToDTO(category);
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
