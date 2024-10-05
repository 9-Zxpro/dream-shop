package me.jibajo.dream_shop.service.product;

import me.jibajo.dream_shop.dto.ImageDTO;
import me.jibajo.dream_shop.dto.ProductDTO;
import me.jibajo.dream_shop.exception.AlreadyExistsException;
import me.jibajo.dream_shop.exception.ProductNotFoundException;
import me.jibajo.dream_shop.model.Category;
import me.jibajo.dream_shop.model.Image;
import me.jibajo.dream_shop.model.Product;
import me.jibajo.dream_shop.repository.CategoryRepository;
import me.jibajo.dream_shop.repository.ImageRepository;
import me.jibajo.dream_shop.repository.ProductRepository;
import me.jibajo.dream_shop.requests.AddProductRequest;
import me.jibajo.dream_shop.requests.ProductUpdateRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService implements IProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ImageRepository imageRepository;

    @Override
    public Product addProduct(AddProductRequest product) {
        if(isProductExist(product.getBrand(), product.getName())) {
            throw new AlreadyExistsException(product.getBrand()+" "+product.getName() +" already exists");
        }
        Category category = Optional.ofNullable(categoryRepository.findByName(product.getCategory().getName()))
                .orElseGet(() -> {
                    Category newCategory = new Category(product.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });
        product.setCategory(category);
        return productRepository.save(createProduct(product, category));
    }

    private boolean isProductExist(String brand, String name){
        return productRepository.existsByBrandAndName(brand, name);
    }

    private Product createProduct(AddProductRequest request, Category category) {
        return new Product(
            request.getName(),
            request.getPrice(),
            request.getBrand(),
            request.getDescription(),
            request.getInventory(),
            category
        );
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id)
                .ifPresentOrElse(productRepository::delete, () -> {throw new ProductNotFoundException("Product not found?");});
    }

    @Override
    public Product updateProduct(ProductUpdateRequest product, Long productId) {
        return productRepository.findById(productId)
                .map(existingProduct -> updateExistingProduct(existingProduct, product))
                .map(productRepository::save)
                .orElseThrow(() -> new ProductNotFoundException("Product not found."));
    }
    private Product updateExistingProduct(Product existingProduct, ProductUpdateRequest request) {
        existingProduct.setName(request.getName());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setInventory(request.getInventory());

        Category category = categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setCategory(category);
        return existingProduct;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category, brand);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand, name);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand, name);
    }

    @Override
    public List<ProductDTO> getConvertedProducts(List<Product> products) {
        return products.stream().map(this::convertToDTO).toList();
    }

    @Override
    public ProductDTO convertToDTO(Product product) {
        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
        List<Image> imageList = imageRepository.findByProductId(product.getId());
        List<ImageDTO> imageDTOList = imageList.stream()
                .map(image -> modelMapper.map(image, ImageDTO.class))
                .toList();
        productDTO.setImageList(imageDTOList);
        return productDTO;
    }

}
