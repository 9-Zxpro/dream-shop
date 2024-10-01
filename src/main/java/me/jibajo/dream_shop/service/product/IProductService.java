package me.jibajo.dream_shop.service.product;

import me.jibajo.dream_shop.dto.ProductDTO;
import me.jibajo.dream_shop.model.Product;
import me.jibajo.dream_shop.requests.AddProductRequest;
import me.jibajo.dream_shop.requests.ProductUpdateRequest;

import java.util.List;

public interface IProductService {
    Product addProduct(AddProductRequest product);
    Product getProductById(Long id);
    void deleteProductById(Long id);
    Product updateProduct(ProductUpdateRequest product, Long productId);
    List<Product> getAllProducts();
    List<Product> getProductsByCategory(String category);
    List<Product> getProductsByBrand(String brand);
    List<Product> getProductsByCategoryAndBrand(String category, String brand);
    List<Product> getProductsByBrandAndName(String brand, String name);
    List<Product> getProductsByName(String name);
    Long countProductsByBrandAndName(String brand, String name);

    List<ProductDTO> getConvertedProducts(List<Product> products);

    ProductDTO convertToDTO(Product product);
}
