package category.system;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class CategoryController {

    private CategoryService _categoryService = new CategoryService();

    public CategoryController(CategoryService categoryService) {
        this._categoryService = categoryService;
    }

    @PostMapping("/categories")
    ResponseEntity<Category> createCategory(@RequestBody CategoryDTO request) {
        Category newCategory = _categoryService.createCategory(request);
        return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
    }

    @GetMapping("/categories")
    ResponseEntity<List<Category>> getCategories() { // this has no required parameters so idk
        List<Category> categoryList = _categoryService.getCategories();
        return new ResponseEntity<>(categoryList, HttpStatus.OK);
    }

    @PutMapping("/categories/{categoryId}")
    ResponseEntity<Category> updateCategory(@PathVariable int categoryId, @RequestBody CategoryDTO request) {
        _categoryService.updateCategory(categoryId, request);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @DeleteMapping("/categories/{categoryId}")
    ResponseEntity<Category> deleteCategory(@PathVariable int categoryId) {
        _categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

}


