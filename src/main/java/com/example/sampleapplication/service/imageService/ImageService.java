package com.example.sampleapplication.service.imageService;

import com.example.sampleapplication.dto.ImageDto;
import org.springframework.http.HttpEntity;

public interface ImageService {
    Long storeImage(ImageDto imageDto);
    HttpEntity<byte[]> getImage (Long imageId);
    String deleteImage(Long imageId);
}
