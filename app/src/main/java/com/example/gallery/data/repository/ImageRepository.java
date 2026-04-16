package com.example.gallery.data.repository;

import com.example.gallery.R;
import com.example.gallery.data.model.imageItem;

import java.util.ArrayList;
import java.util.List;

public class ImageRepository {
    private static ImageRepository instance;
    private final List<imageItem> items = new ArrayList<>();

    private ImageRepository() {
        loadInitialData();
    }

    public static synchronized ImageRepository getInstance() {
        if (instance == null) {
            instance = new ImageRepository();
        }
        return instance;
    }

    private void loadInitialData() {
        long now = System.currentTimeMillis();
        String day = String.valueOf(now / (24L * 60L * 60L * 1000L));

        items.add(new imageItem(1, "Barcelona", R.drawable.img_barcelona, "https://en.wikipedia.org/wiki/Barcelona", day));
        items.add(new imageItem(2, "Dubai", R.drawable.img_dubai, "https://en.wikipedia.org/wiki/Dubai", day));
        items.add(new imageItem(3, "London", R.drawable.img_london, "https://en.wikipedia.org/wiki/London", day));
        items.add(new imageItem(4, "New York", R.drawable.img_newyork, "https://en.wikipedia.org/wiki/New_York_City", day));
        items.add(new imageItem(5, "Paris", R.drawable.img_paris, "https://en.wikipedia.org/wiki/Paris", day));
        items.add(new imageItem(6, "Rome", R.drawable.img_rome, "https://en.wikipedia.org/wiki/Rome", day));
        items.add(new imageItem(7, "Sydney", R.drawable.img_sydney, "https://en.wikipedia.org/wiki/Sydney", day));
        items.add(new imageItem(8, "Tokyo", R.drawable.img_tokyo, "https://en.wikipedia.org/wiki/Tokyo", day));
    }

    public List<imageItem> getItemsList() {
        return new ArrayList<>(items);
    }
}
