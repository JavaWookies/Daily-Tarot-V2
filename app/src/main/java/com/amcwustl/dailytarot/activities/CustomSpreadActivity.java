package com.amcwustl.dailytarot.activities;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.core.content.res.ResourcesCompat;

import com.amcwustl.dailytarot.R;

public class CustomSpreadActivity extends BaseActivity {
    private static final String TAG = "Custom Spread Activity";
    private ImageView deckImageView;
    private FrameLayout droppableArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_custom_spread);
        super.onCreate(savedInstanceState);

        deckImageView = findViewById(R.id.deckImageView);
        droppableArea = findViewById(R.id.droppableArea);

        setupDeckTouchListener();
        setupDroppableArea();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupDeckTouchListener() {
        deckImageView.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                view.performClick();
                return true; // Indicate the view is ready for further action
            }
            return false;
        });

        deckImageView.setOnLongClickListener(view -> {
            ImageView newCard = createNewCardImageView();

            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(deckImageView);


            newCard.startDragAndDrop(data, shadowBuilder, newCard, 0);

            return true;
        });
    }

    private ImageView createNewCardImageView() {
        ImageView imageView = new ImageView(this);
        int widthInPx = convertDpToPx(100); // Convert 100dp to pixels
        int heightInPx = convertDpToPx(145); // Convert 145dp to pixels

        imageView.setLayoutParams(new FrameLayout.LayoutParams(widthInPx, heightInPx));
        imageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.cover, null));
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

        return imageView;
    }

    private int convertDpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

    private void setupDroppableArea() {
        droppableArea.setOnDragListener((view, dragEvent) -> {
            if (dragEvent.getAction() == DragEvent.ACTION_DROP) {
                View draggedView = (View) dragEvent.getLocalState();
                droppableArea.addView(draggedView);
                draggedView.setX(dragEvent.getX() - draggedView.getWidth() / (float) 2);
                draggedView.setY(dragEvent.getY() - draggedView.getHeight() / (float) 2);
            }
            return true;
        });
    }
}