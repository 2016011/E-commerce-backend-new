package com.example.sampleapplication.modal;

import java.util.Arrays;

public class Image {
    private Long imageId;
    private byte[] imageData;

    public Image() {
    }

    public Image(Long imageId, byte[] imageData) {
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

    @Override
    public String toString() {
        return "Image{" +
                "imageId=" + imageId +
                ", imageData=" + Arrays.toString(imageData) +
                '}';
    }
}
