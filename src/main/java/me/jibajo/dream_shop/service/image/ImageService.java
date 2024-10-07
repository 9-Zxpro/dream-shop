package me.jibajo.dream_shop.service.image;

import me.jibajo.dream_shop.dto.ImageDTO;
import me.jibajo.dream_shop.exception.ResourceNotFoundException;
import me.jibajo.dream_shop.model.Image;
import me.jibajo.dream_shop.model.Product;
import me.jibajo.dream_shop.repository.ImageRepository;
import me.jibajo.dream_shop.service.product.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImageService implements IImageService{
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private IProductService iProductRService;

    @Override
    public Image getImageById(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found with id " + id));
    }

    @Override
    public void deleteImageById(Long id) {
        imageRepository.findById(id).ifPresentOrElse(imageRepository::delete, () -> {
            throw new ResourceNotFoundException("Image not found with id " + id);
        });
    }

    @Override
    public List<ImageDTO> saveImages(List<MultipartFile> files, Long productId) {
        Product product = iProductRService.getProductById(productId);
        List<ImageDTO> imageDTOList = new ArrayList<>();
        for(MultipartFile file : files) {
            try {
                Image image = new Image();
                image.setFileType(file.getContentType());
                image.setName(file.getOriginalFilename());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setProduct(product);

                String buildPath = "/api/v1/images/image/download/";
                image.setDownloadUrl(buildPath);
                Image savedImage = imageRepository.save(image);

                savedImage.setDownloadUrl(buildPath + savedImage.getId());
                imageRepository.save(savedImage);

                ImageDTO imageDTO = new ImageDTO();
                imageDTO.setImageId(savedImage.getId());
                imageDTO.setImageName(savedImage.getName());
                imageDTO.setDownloadUrl(savedImage.getDownloadUrl());
                imageDTOList.add(imageDTO);


            } catch (IOException | SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return imageDTOList;
    }

    @Override
    public void updateImage(MultipartFile file, Long imgId) {
        Image image = getImageById(imgId);
        try {
            image.setFileType(file.getContentType());
            image.setName((file.getOriginalFilename()));
            image.setImage(new SerialBlob(file.getBytes()));
            imageRepository.save(image);
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
