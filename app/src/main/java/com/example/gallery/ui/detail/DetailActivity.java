package com.example.gallery.ui.detail;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.gallery.R;
import com.example.gallery.data.model.ImageItem;
import com.example.gallery.data.repository.ImageRepository;
import com.example.gallery.ui.web.WebActivity;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";

    private ImageSwitcher imageSwitcher;
    private TextView titleText;
    private MaterialButton btnInfo;
    private GestureDetector gestureDetector;
    private List<ImageItem> items;
    private int currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        imageSwitcher = findViewById(R.id.imageSwitcher);
        titleText = findViewById(R.id.textTitle);
        btnInfo = findViewById(R.id.btnInfo);

        items = ImageRepository.getInstance().getItemsList();
        currentPosition = getIntent().getIntExtra(EXTRA_POSITION, 0);

        setupImageSwitcher();
        setupGestureDetector();
        showImage(currentPosition);

        btnInfo.setOnClickListener(v -> openWebView());
    }

    private void setupImageSwitcher() {
        imageSwitcher.setFactory(() -> {
            ImageView imageView = new ImageView(DetailActivity.this);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(new ImageSwitcher.LayoutParams(
                    ImageSwitcher.LayoutParams.MATCH_PARENT,
                    ImageSwitcher.LayoutParams.MATCH_PARENT));
            return imageView;
        });

        imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
        imageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));

        imageSwitcher.setOnTouchListener((v, event) -> {
            gestureDetector.onTouchEvent(event);
            return true;
        });
    }

    private void setupGestureDetector() {
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            private static final int SWIPE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e1 == null || e2 == null) return false;

                float diffX = e2.getX() - e1.getX();
                float diffY = e2.getY() - e1.getY();

                if (Math.abs(diffX) > Math.abs(diffY) &&
                        Math.abs(diffX) > SWIPE_THRESHOLD &&
                        Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX < 0) {
                        showNext();
                    } else {
                        showPrevious();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void showImage(int position) {
        if (items == null || items.isEmpty()) return;
        currentPosition = position;
        ImageItem item = items.get(currentPosition);
        imageSwitcher.setImageResource(item.getImageRes());
        titleText.setText(item.getTitle());
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(item.getTitle());
        }
    }

    private void openWebView() {
        if (items == null || currentPosition >= items.size()) return;
        ImageItem item = items.get(currentPosition);
        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra(WebActivity.EXTRA_URL, item.getDescriptionUrl());
        intent.putExtra(WebActivity.EXTRA_TITLE, item.getTitle());
        startActivity(intent);
    }

    private void showNext() {
        if (currentPosition < items.size() - 1) {
            imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_right));
            imageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_out_left));
            showImage(currentPosition + 1);
        }
    }

    private void showPrevious() {
        if (currentPosition > 0) {
            imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left));
            imageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right));
            showImage(currentPosition - 1);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
