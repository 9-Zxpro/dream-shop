package me.jibajo.dream_shop.controller;

import me.jibajo.dream_shop.dto.ProductDTO;
import me.jibajo.dream_shop.exception.AlreadyExistsException;
import me.jibajo.dream_shop.exception.ProductNotFoundException;
import me.jibajo.dream_shop.exception.ResourceNotFoundException;
import me.jibajo.dream_shop.model.Category;
import me.jibajo.dream_shop.model.Product;
import me.jibajo.dream_shop.requests.AddProductRequest;
import me.jibajo.dream_shop.requests.ProductUpdateRequest;
import me.jibajo.dream_shop.response.APIResponse;
import me.jibajo.dream_shop.service.product.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("${api-prefix}/products")
public class ProductController {

    @Autowired
    private IProductService iProductService;

    @GetMapping("/all")
    public ResponseEntity<APIResponse> getAllProducts() {
        List<Product> productList = iProductService.getAllProducts();
        List<ProductDTO> productDTOList = iProductService.getConvertedProducts(productList);
        return ResponseEntity.ok(new APIResponse("Success ", productDTOList));
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<APIResponse> getProductById(@PathVariable Long id) {
        try {
            Product product = iProductService.getProductById(id);
            ProductDTO productDTO = iProductService.convertToDTO(product);
            return ResponseEntity.ok(new APIResponse("Success ", productDTO));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new APIResponse(e.getMessage(), null));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<APIResponse> addProduct(@RequestBody AddProductRequest product) {
        try {
            Product theProduct = iProductService.addProduct(product);
            ProductDTO productDTO = iProductService.convertToDTO(theProduct);
            return ResponseEntity.ok(new APIResponse("Added Successfully ", productDTO));
        } catch (ResourceNotFoundException | AlreadyExistsException e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new APIResponse("Error:", e.getMessage()));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<APIResponse> updateProduct(@RequestBody ProductUpdateRequest product, @PathVariable Long id) {
        try {
            Product theProduct = iProductService.updateProduct(product, id);
            return ResponseEntity.ok(new APIResponse("Updated Successfully ", theProduct.getName()));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new APIResponse(e.getMessage(), null));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<APIResponse> deleteProductById(@PathVariable Long id) {
        try {
            iProductService.deleteProductById(id);
            return ResponseEntity.ok(new APIResponse("Deleted Successfully ", null));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new APIResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/by/category-and-brand")
    public ResponseEntity<APIResponse> getProductsByCategoryAndBrand(@RequestParam String category, @RequestParam String brand) {
        try {
            List<Product> productList = iProductService.getProductsByCategoryAndBrand(category, brand);
            if(productList.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new APIResponse("There is no product by this "+category+ " and "+brand, null));
            }
            List<ProductDTO> productDTOList = iProductService.getConvertedProducts(productList);
            return ResponseEntity.ok(new APIResponse("Success ", productDTOList));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new APIResponse("Error:", e.getMessage()));
        }
    }

    @GetMapping("/by/product-name/{name}")
    public ResponseEntity<APIResponse> getProductsByName(@PathVariable String name) {
        try {
            List<Product> productList = iProductService.getProductsByName(name);
            if(productList.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new APIResponse("There is no product by this "+name, null));
            }
            List<ProductDTO> productDTOList = iProductService.getConvertedProducts(productList);
            return ResponseEntity.ok(new APIResponse("Success ", productDTOList));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new APIResponse("Error:", e.getMessage()));
        }
    }

    @GetMapping("/by/brand/{brand}")
    public ResponseEntity<APIResponse> getProductsByBrand(@PathVariable String brand) {
        try {
            List<Product> productList = iProductService.getProductsByBrand(brand);
            if(productList.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new APIResponse("There is no product by this "+brand, null));
            }
            List<ProductDTO> productDTOList = iProductService.getConvertedProducts(productList);
            return ResponseEntity.ok(new APIResponse("Success ", productDTOList));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new APIResponse("Error:", e.getMessage()));
        }
    }

    @GetMapping("/by/category/{category}")
    public ResponseEntity<APIResponse> getProductsByCategory(@PathVariable String category) {
        try {
            List<Product> productList = iProductService.getProductsByCategory(category);
            if(productList.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new APIResponse("There is no product by this name "+category, null));
            }
            List<ProductDTO> productDTOList = iProductService.getConvertedProducts(productList);
            return ResponseEntity.ok(new APIResponse("Success ", productDTOList));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new APIResponse("Error:", e.getMessage()));
        }
    }

    @GetMapping("/by/brand-and-name")
    public ResponseEntity<APIResponse> getProductsByBrandAndName(@RequestParam String brand, @RequestParam String name) {
        try {
            List<Product> productList = iProductService.getProductsByBrandAndName(brand, name);
            if(productList.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new APIResponse("There is no product by this "+brand +" and product "+name, null));
            }
            List<ProductDTO> productDTOList = iProductService.getConvertedProducts(productList);
            return ResponseEntity.ok(new APIResponse("Success ", productDTOList));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new APIResponse("Error:", e.getMessage()));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/brand-and-name/count")
    public ResponseEntity<APIResponse> countProductsByBrandAndName(@RequestParam String brand, @RequestParam String name) {
        try {
            Long productCount = iProductService.countProductsByBrandAndName(brand, name);
            return ResponseEntity.ok(new APIResponse("Product count ", productCount));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new APIResponse("Error:", e.getMessage()));
        }
    }






}
