package com.example.gallery.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.gallery.R;
import com.example.gallery.data.model.ImageItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
public class ImageRepository {
    private static ImageRepository instance;

    private final MutableLiveData<List<ImageItem>> itemsLiveData = new MutableLiveData<>();
    private final List<ImageItem> items = new ArrayList<>();

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

        items.add(new ImageItem(1, "Barcelona", R.drawable.img_barcelona, "https://en.wikipedia.org/wiki/Barcelona", day));
        items.add(new ImageItem(2, "Dubai", R.drawable.img_dubai, "https://en.wikipedia.org/wiki/Dubai", day));
        items.add(new ImageItem(3, "London", R.drawable.img_london, "https://en.wikipedia.org/wiki/London", day));
        items.add(new ImageItem(4, "New York", R.drawable.img_newyork, "https://en.wikipedia.org/wiki/New_York_City", day));
        items.add(new ImageItem(5, "Paris", R.drawable.img_paris, "https://en.wikipedia.org/wiki/Paris", day));
        items.add(new ImageItem(6, "Rome", R.drawable.img_rome, "https://en.wikipedia.org/wiki/Rome", day));
        items.add(new ImageItem(7, "Sydney", R.drawable.img_sydney, "https://en.wikipedia.org/wiki/Sydney", day));
        items.add(new ImageItem(8, "Tokyo", R.drawable.img_tokyo, "https://en.wikipedia.org/wiki/Tokyo", day));

        itemsLiveData.setValue(new ArrayList<>(items));
    }

    public LiveData<List<ImageItem>> getImages() {
        return itemsLiveData;
    }

    public List<ImageItem> getItemsList() {
        return new ArrayList<>(items);
    }

    public void deleteImage(int id) {
        items.removeIf(item -> item.getId() == id);
        itemsLiveData.setValue(new ArrayList<>(items));
    }

    public void deleteImage(String id) {
        if (id == null) return;
        try {
            deleteImage(Integer.parseInt(id));
        } catch (NumberFormatException ignored) {
        }
    }
    public void sortByDate() {
        Collections.sort(items,
                (a, b) -> Long.compare(
                        a.getDateAdded(),
                        b.getDateAdded()
                )
        );

        itemsLiveData.setValue(new ArrayList<>(items));
    }
    public void sortByName() {
        Collections.sort(items, (a, b) -> a.getTitle().compareToIgnoreCase(b.getTitle()));
        itemsLiveData.setValue(new ArrayList<>(items));
    }
    public void addImage(ImageItem item) {
        items.add(item);
        itemsLiveData.setValue(new ArrayList<>(items));
    }

    public void deleteImage(ImageItem item) {
        if (item == null) return;

        items.remove(item);

        itemsLiveData.setValue(new ArrayList<>(items));
    }

    public void searchImages(String query) {

        if (query == null || query.trim().isEmpty()) {
            itemsLiveData.setValue(new ArrayList<>(items));
            return;
        }

        List<ImageItem> filtered = new ArrayList<>();

        for (ImageItem item : items) {

            if (item.getTitle().toLowerCase()
                    .contains(query.toLowerCase())) {

                filtered.add(item);
            }
        }

        itemsLiveData.setValue(filtered);
    }

}