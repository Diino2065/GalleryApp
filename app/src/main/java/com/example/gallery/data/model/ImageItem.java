package com.example.gallery.data.model;

public class ImageItem {
    private final int id;
    private final String title;
    private final int imageRes;
    private final String descriptionUrl;
    private final Long dateAdded;

    public ImageItem(int id, String title, int imageRes, String descriptionUrl, String dateAdded) {
        this.id = id;
        this.title = title;
        this.imageRes = imageRes;
        this.descriptionUrl = descriptionUrl;
        this.dateAdded = Long.valueOf(dateAdded);
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getImageRes() {
        return imageRes;
    }

    public String getDescriptionUrl() {
        return descriptionUrl;
    }

    public Long getDateAdded() {
        return dateAdded;
    }
}
