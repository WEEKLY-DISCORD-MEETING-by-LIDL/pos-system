package wdmsystem.category;

import java.util.List;

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

@RestController
public class CategoryController {

    private final CategoryService _categoryService;

    public CategoryController(CategoryService categoryService) {
        this._categoryService = categoryService;
    }

    @PostMapping("/categories")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO request) {
        CategoryDTO newCategory = _categoryService.createCategory(request);
        return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
    }

    @GetMapping("/categories")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER') or hasRole('REGULAR')")
    ResponseEntity<List<CategoryDTO>> getCategories() {
        List<CategoryDTO> categoryList = _categoryService.getCategories();
        return new ResponseEntity<>(categoryList, HttpStatus.OK);
    }

    @PutMapping("/categories/{categoryId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('OWNER') and @categoryService.isOwnedByCurrentUser(#categoryId))")
    ResponseEntity<CategoryDTO> updateCategory(@PathVariable int categoryId, @RequestBody CategoryDTO request) {
        CategoryDTO updatedCategory = _categoryService.updateCategory(categoryId, request);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }

    @DeleteMapping("/categories/{categoryId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('OWNER') and @categoryService.isOwnedByCurrentUser(#categoryId))")
    ResponseEntity<Category> deleteCategory(@PathVariable int categoryId) {
        _categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

}


