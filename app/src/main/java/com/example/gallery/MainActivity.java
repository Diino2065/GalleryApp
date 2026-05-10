
package com.example.gallery;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupMenu;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gallery.data.model.ImageItem;
import com.example.gallery.ui.detail.DetailActivity;
import com.example.gallery.ui.gallery.GalleryAdapter;
import com.example.gallery.ui.gallery.GalleryViewModel;
import com.example.gallery.utils.PreferenceManager;

public class MainActivity extends AppCompatActivity {

    private GalleryViewModel viewModel;
    private GalleryAdapter adapter;
    private GridLayoutManager layoutManager;
    private PreferenceManager preferenceManager;
    private DrawerLayout drawerLayout;

    private boolean isGridMode;

    private TextView navLayoutValue;
    private TextView navSortValue;
    private TextView navThemeValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // DARK MODE PRIJE super.onCreate()
        preferenceManager = new PreferenceManager(this);
        applyDarkMode(preferenceManager.isDarkMode());

        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_main);

        // TOOLBAR
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // RECYCLER VIEW
        isGridMode = preferenceManager.isGridMode();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        layoutManager = new GridLayoutManager(
                this,
                isGridMode ? 2 : 1
        );

        recyclerView.setLayoutManager(layoutManager);

        adapter = new GalleryAdapter();

        recyclerView.setAdapter(adapter);

        registerForContextMenu(recyclerView);

        // ITEM CLICK
        adapter.setOnItemClickListener((item, position) -> {

            Intent intent = new Intent(
                    this,
                    DetailActivity.class
            );

            intent.putExtra(
                    DetailActivity.EXTRA_POSITION,
                    position
            );

            startActivity(intent);
        });

        // LONG CLICK
        adapter.setOnItemLongClickListener((item, position) -> {

            openContextMenu(recyclerView);

            return true;
        });

        // VIEWMODEL
        viewModel = new ViewModelProvider(this)
                .get(GalleryViewModel.class);

        viewModel.images.observe(
                this,
                items -> adapter.setItems(items)
        );

        // DRAWER
        drawerLayout = findViewById(R.id.drawerLayout);

        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(
                        this,
                        drawerLayout,
                        toolbar,
                        R.string.nav_open,
                        R.string.nav_close
                );

        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();

        // BACK BUTTON
        getOnBackPressedDispatcher().addCallback(
                this,
                new OnBackPressedCallback(true) {

                    @Override
                    public void handleOnBackPressed() {

                        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {

                            drawerLayout.closeDrawer(GravityCompat.START);

                        } else {

                            setEnabled(false);

                            getOnBackPressedDispatcher().onBackPressed();
                        }
                    }
                }
        );

        // NAVIGATION DRAWER OPCIJE
        navLayoutValue = findViewById(R.id.navLayoutValue);
        navSortValue = findViewById(R.id.navSortValue);
        navThemeValue = findViewById(R.id.navThemeValue);

        navLayoutValue.setText(
                isGridMode
                        ? R.string.action_grid_view
                        : R.string.action_list_view
        );

        navThemeValue.setText(
                preferenceManager.isDarkMode()
                        ? R.string.theme_dark
                        : R.string.theme_light
        );

        findViewById(R.id.navItemLayout)
                .setOnClickListener(v -> showLayoutPopup(v));

        findViewById(R.id.navItemSort)
                .setOnClickListener(v -> showSortPopup(v));

        findViewById(R.id.navItemTheme)
                .setOnClickListener(v -> showThemePopup(v));

        // EDGE TO EDGE
        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (v, insets) -> {

                    Insets systemBars =
                            insets.getInsets(
                                    WindowInsetsCompat.Type.systemBars()
                            );

                    v.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom
                    );

                    return insets;
                }
        );
    }

    // TOOLBAR MENU

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(
                R.menu.menu_main,
                menu
        );

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.context_help) {

            Intent intent = new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://support.google.com/android")
            );

            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // CONTEXT MENU

    @Override
    public void onCreateContextMenu(
            ContextMenu menu,
            View v,
            ContextMenu.ContextMenuInfo menuInfo
    ) {

        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(
                R.menu.menu_context,
                menu
        );

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

            viewModel.deleteImage(
                    String.valueOf(imageItem.getId())
            );

            Toast.makeText(
                    this,
                    getString(
                            R.string.deleted,
                            imageItem.getTitle()
                    ),
                    Toast.LENGTH_SHORT
            ).show();

            return true;

        } else if (id == R.id.context_share) {

            Intent shareIntent =
                    new Intent(Intent.ACTION_SEND);

            shareIntent.setType("text/plain");

            shareIntent.putExtra(
                    Intent.EXTRA_SUBJECT,
                    imageItem.getTitle()
            );

            shareIntent.putExtra(
                    Intent.EXTRA_TEXT,
                    imageItem.getTitle()
                            + "\n"
                            + imageItem.getDescriptionUrl()
            );

            startActivity(
                    Intent.createChooser(
                            shareIntent,
                            getString(R.string.share_via)
                    )
            );

            return true;

        } else if (id == R.id.context_view_details) {

            Intent intent = new Intent(
                    this,
                    DetailActivity.class
            );

            intent.putExtra(
                    DetailActivity.EXTRA_POSITION,
                    pos
            );

            startActivity(intent);

            return true;
        }

        return super.onContextItemSelected(item);
    }

    // LAYOUT POPUP

    private void showLayoutPopup(View anchor) {

        PopupMenu popup = new PopupMenu(this, anchor);

        popup.getMenuInflater().inflate(
                R.menu.menu_popup_layout,
                popup.getMenu()
        );

        popup.getMenu()
                .findItem(
                        isGridMode
                                ? R.id.action_grid_view
                                : R.id.action_list_view
                )
                .setChecked(true);

        popup.setOnMenuItemClickListener(menuItem -> {

            int id = menuItem.getItemId();

            if (id == R.id.action_grid_view) {

                isGridMode = true;

                preferenceManager.setGridMode(true);

                layoutManager.setSpanCount(2);

                navLayoutValue.setText(
                        R.string.action_grid_view
                );

            } else if (id == R.id.action_list_view) {

                isGridMode = false;

                preferenceManager.setGridMode(false);

                layoutManager.setSpanCount(1);

                navLayoutValue.setText(
                        R.string.action_list_view
                );
            }

            drawerLayout.closeDrawer(GravityCompat.START);

            return true;
        });

        popup.show();
    }

    // SORT POPUP

    private void showSortPopup(View anchor) {

        PopupMenu popup = new PopupMenu(this, anchor);

        popup.getMenuInflater().inflate(
                R.menu.menu_popup_sort,
                popup.getMenu()
        );

        popup.setOnMenuItemClickListener(menuItem -> {

            int id = menuItem.getItemId();

            if (id == R.id.action_sort_name) {

                viewModel.sortByName();

                navSortValue.setText(
                        R.string.sort_by_name
                );

            } else if (id == R.id.action_sort_date) {

                viewModel.sortByDate();

                navSortValue.setText(
                        R.string.sort_by_date
                );
            }

            drawerLayout.closeDrawer(GravityCompat.START);

            return true;
        });

        popup.show();
    }

    // THEME POPUP

    private void showThemePopup(View anchor) {

        PopupMenu popup = new PopupMenu(this, anchor);

        popup.getMenuInflater().inflate(
                R.menu.menu_popup_theme,
                popup.getMenu()
        );

        popup.getMenu()
                .findItem(
                        preferenceManager.isDarkMode()
                                ? R.id.action_theme_dark
                                : R.id.action_theme_light
                )
                .setChecked(true);

        popup.setOnMenuItemClickListener(menuItem -> {

            int id = menuItem.getItemId();

            if (id == R.id.action_theme_light) {

                preferenceManager.setDarkMode(false);

                navThemeValue.setText(
                        R.string.theme_light
                );

                applyDarkMode(false);

            } else if (id == R.id.action_theme_dark) {

                preferenceManager.setDarkMode(true);

                navThemeValue.setText(
                        R.string.theme_dark
                );

                applyDarkMode(true);
            }

            drawerLayout.closeDrawer(GravityCompat.START);

            return true;
        });

        popup.show();
    }

    // DARK MODE

    private void applyDarkMode(boolean enabled) {

        AppCompatDelegate.setDefaultNightMode(
                enabled
                        ? AppCompatDelegate.MODE_NIGHT_YES
                        : AppCompatDelegate.MODE_NIGHT_NO
        );
    }
}

