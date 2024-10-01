package me.jibajo.dream_shop.service.image;

import me.jibajo.dream_shop.dto.ImageDTO;
import me.jibajo.dream_shop.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
    Image getImageById(Long id);
    void deleteImageById(Long id);
    List<ImageDTO> saveImages(List<MultipartFile> files, Long productId);
    void updateImage(MultipartFile file, Long imgId);

}
