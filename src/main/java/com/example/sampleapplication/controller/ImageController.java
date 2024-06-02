package com.example.sampleapplication.controller;
import com.example.sampleapplication.dto.ImageDto;
import com.example.sampleapplication.service.imageService.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/image")
public class ImageController {
    @Autowired
    private ImageService imageService;

    @CrossOrigin
    @PostMapping("/setImage")
    public Long storeImage(@RequestBody ImageDto imageDto) {
        return imageService.storeImage(imageDto);
    }

    @CrossOrigin
    @GetMapping("/getImage/{imageId}")
    public HttpEntity<byte[]> getImage(@PathVariable("imageId") Long imageId){
        return imageService.getImage(imageId);
    }

    @CrossOrigin
    @DeleteMapping("/delete/{imageId}")
    public String deleteImage (@PathVariable Long imageId){
        try {
            return imageService.deleteImage(imageId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "An error occurred while trying to delete A image with ID " + imageId;
    }
}
