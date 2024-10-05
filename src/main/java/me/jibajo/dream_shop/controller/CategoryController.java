package me.jibajo.dream_shop.controller;

import me.jibajo.dream_shop.exception.AlreadyExistsException;
import me.jibajo.dream_shop.model.Category;
import me.jibajo.dream_shop.response.APIResponse;
import me.jibajo.dream_shop.service.category.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("${api-prefix}/categories")
public class CategoryController {

    @Autowired
    private ICategoryService iCategoryService;

    @GetMapping("/all-category")
    public ResponseEntity<APIResponse> getAllCategories() {
        try {
            List<Category> categories = iCategoryService.getAllCategories();
            return ResponseEntity.ok(new APIResponse("Found", categories));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new APIResponse("Error:", INTERNAL_SERVER_ERROR));
        }
    }

    @PostMapping("/add-category")
    public ResponseEntity<APIResponse> addCategory(@RequestBody Category name) {
        try {
            Category category = iCategoryService.addCategory(name);
            return ResponseEntity.ok(new APIResponse("Added Successfully", category));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new APIResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/{id}/category")
    public ResponseEntity<APIResponse> getCategoryById(@PathVariable Long id) {
        try {
            Category category = iCategoryService.getCategoryById(id);
            return ResponseEntity.ok(new APIResponse("Found", category));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(NOT_FOUND).body(new APIResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/category/{name}")
    public ResponseEntity<APIResponse> getCategoryByName(@PathVariable String name) {
        try {
            Category category = iCategoryService.getCategoryByName(name);
            return ResponseEntity.ok(new APIResponse("Found", category));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(NOT_FOUND).body(new APIResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<APIResponse> deleteCategoryById(@PathVariable Long id) {
        try {
            iCategoryService.deleteCategoryById(id);
            return ResponseEntity.ok(new APIResponse("Deleted Successfully", null));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(NOT_FOUND).body(new APIResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<APIResponse> updateCategoryById(@PathVariable Long id, @RequestBody Category category) {
        try {
            Category updateCategory = iCategoryService.updateCategory(category, id);
            return ResponseEntity.ok(new APIResponse("Update Successful", updateCategory));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(NOT_FOUND).body(new APIResponse(e.getMessage(), null));
        }
    }


}
