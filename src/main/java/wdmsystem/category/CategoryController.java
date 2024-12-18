package wdmsystem.category;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class CategoryController {

    private final CategoryService _categoryService;

    public CategoryController(CategoryService categoryService) {
        this._categoryService = categoryService;
    }

    @PostMapping("/categories")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO request) {
        log.info("Received request to create category: {}", request);
        CategoryDTO newCategory = _categoryService.createCategory(request);
        log.info("Category created successfully: {}", newCategory);
        return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
    }

    @GetMapping("/categories")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER') or hasRole('REGULAR')")
    ResponseEntity<List<CategoryDTO>> getCategories() {
        log.info("Fetching all categories.");
        List<CategoryDTO> categoryList = _categoryService.getCategories();
        log.info("Retrieved {} categories.", categoryList.size());
        return new ResponseEntity<>(categoryList, HttpStatus.OK);
    }

    @PutMapping("/categories/{categoryId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('OWNER') and @categoryService.isOwnedByCurrentUser(#categoryId))")
    ResponseEntity<CategoryDTO> updateCategory(@PathVariable int categoryId, @RequestBody CategoryDTO request) {
        log.info("Received request to update category with ID: {}", categoryId);
        CategoryDTO updatedCategory = _categoryService.updateCategory(categoryId, request);
        log.info("Category with ID: {} updated successfully", categoryId);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }

    @DeleteMapping("/categories/{categoryId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('OWNER') and @categoryService.isOwnedByCurrentUser(#categoryId))")
    ResponseEntity<Category> deleteCategory(@PathVariable int categoryId) {
        log.info("Received request to delete category with ID: {}", categoryId);
        _categoryService.deleteCategory(categoryId);
        log.info("Category with ID: {} deleted successfully", categoryId);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

}


