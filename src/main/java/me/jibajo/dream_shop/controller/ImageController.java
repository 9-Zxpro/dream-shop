package me.jibajo.dream_shop.controller;

import me.jibajo.dream_shop.dto.ImageDTO;
import me.jibajo.dream_shop.model.Image;
import me.jibajo.dream_shop.response.APIResponse;
import me.jibajo.dream_shop.service.image.IImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("${api-prefix}/images")
public class ImageController {

    @Autowired
    private IImageService iImageService;

    @PostMapping("/upload")
    public ResponseEntity<APIResponse> saveImages(@RequestParam List<MultipartFile> files, @RequestParam Long productId) {
        try {
            List<ImageDTO> imageDTOList = iImageService.saveImages(files, productId);
            return ResponseEntity.ok(new APIResponse("Uploaded", imageDTOList));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new APIResponse("Uploaded failed", e.getMessage()));
        }

    }

    @GetMapping("/image/download/{id}")
    public ResponseEntity<Resource> downloadImage(@PathVariable Long id) throws SQLException {
        Image image = iImageService.getImageById(id);
        ByteArrayResource byteArrayResource = new ByteArrayResource(image.getImage().getBytes(1, (int) image.getImage().length()));

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(image.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getName() + "\"")
                .body(byteArrayResource);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<APIResponse> updateImage(@RequestBody MultipartFile file, @PathVariable Long id) {
        try {
            Image image = iImageService.getImageById(id);
            if(image != null) {
                iImageService.updateImage(file, id);
                return ResponseEntity.ok(new APIResponse("Update successfully", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new APIResponse(e.getMessage(), null));
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new APIResponse("Update failed", INTERNAL_SERVER_ERROR));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<APIResponse> deleteImageById(@PathVariable Long id) {
        try {
            Image image = iImageService.getImageById(id);
            if(image != null) {
                iImageService.deleteImageById(id);
                return ResponseEntity.ok(new APIResponse("Deleted successfully", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new APIResponse(e.getMessage(), null));
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new APIResponse("Deletion failed", INTERNAL_SERVER_ERROR));
    }

}
