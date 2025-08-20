package com.example.cs;

import com.bumptech.glide.Glide;

public class Listing {
    private String name;
    private String description;
    private String imageUrl;  // 🔹 Use String for image URL (instead of int for local drawable)

    public Listing() {

    }

    public Listing(String name, String description, String imageUrl) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
