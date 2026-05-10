package com.example.gallery.ui.gallery;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;


import com.example.gallery.data.model.ImageItem;
import com.example.gallery.data.repository.ImageRepository;

import java.util.List;

public class GalleryViewModel extends ViewModel {

    private final ImageRepository repository = ImageRepository.getInstance();

    public final LiveData<List<ImageItem>> images = repository.getImages();

    // Dodavanje slike
    public void addImage(ImageItem item) {
        repository.addImage(item);
    }

    // Brisanje slike
    public void deleteImage(String item) {
        repository.deleteImage(item);
    }

    // Sortiranje po datumu
    public void sortByDate() {
        repository.sortByDate();
    }
    public void sortByName() {
        repository.sortByName();
    }

    // Pretraga po nazivu
    public void searchImages(String query) {
        repository.searchImages(query);
    }
}