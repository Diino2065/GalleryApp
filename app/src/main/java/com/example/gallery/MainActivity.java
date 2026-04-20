package com.example.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gallery.data.model.ImageItem;
import com.example.gallery.data.repository.ImageRepository;
import com.example.gallery.ui.detail.DetailActivity;
import com.example.gallery.ui.gallery.GalleryAdapter;

public class MainActivity extends AppCompatActivity {
    private GalleryAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        adapter = new GalleryAdapter();
        adapter.setOnItemClickListener((item, position) -> {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(DetailActivity.EXTRA_POSITION, position);
            startActivity(intent);
        });
        adapter.setOnItemLongClickListener((item, position) -> {
            openContextMenu(recyclerView);
            return true;
        });
        recyclerView.setAdapter(adapter);

        registerForContextMenu(recyclerView);

        ImageRepository.getInstance().getImages().observe(this, items -> adapter.setItems(items));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_context, menu);
        int pos = adapter.getContextMenuPosition();
        if (pos >= 0) {
            ImageItem item = adapter.getItemAt(pos);
            if (item != null) {
                menu.setHeaderTitle(item.getTitle());
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int pos = adapter.getContextMenuPosition();
        if (pos < 0) return false;

        ImageItem imageItem = adapter.getItemAt(pos);
        if (imageItem == null) return false;

        int id = item.getItemId();
        if (id == R.id.context_delete) {
            ImageRepository.getInstance().deleteImage(String.valueOf(imageItem.getId()));
            Toast.makeText(this, getString(R.string.deleted, imageItem.getTitle()), Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.context_share) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, imageItem.getTitle());
            shareIntent.putExtra(Intent.EXTRA_TEXT, imageItem.getTitle() + "\n" + imageItem.getDescriptionUrl());
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_via)));
            return true;
        } else if (id == R.id.context_view_details) {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(DetailActivity.EXTRA_POSITION, pos);
            startActivity(intent);
            return true;
        }
        return super.onContextItemSelected(item);
    }
}