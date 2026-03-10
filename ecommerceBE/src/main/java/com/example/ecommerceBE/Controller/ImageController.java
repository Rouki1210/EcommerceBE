package com.example.ecommerceBE.Controller;

import com.example.ecommerceBE.Service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {

    private final CloudinaryService cloudinaryService;

    // API nhận file ảnh và trả về Link (String)
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            return cloudinaryService.uploadImage(file);
        } catch (IOException e) {
            throw new RuntimeException("Error when upload image: " + e.getMessage());
        }
    }
}