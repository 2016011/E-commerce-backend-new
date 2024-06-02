package com.example.sampleapplication.dto;

public class ImageDto {
    private Long imageId;
    private byte[] imageData;

    public ImageDto() {
    }

    public ImageDto(Long imageId, byte[] imageData) {
        this.imageId = imageId;
        this.imageData = imageData;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }


    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }
}
