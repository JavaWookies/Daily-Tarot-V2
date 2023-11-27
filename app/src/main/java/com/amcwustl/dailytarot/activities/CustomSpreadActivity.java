package com.amcwustl.dailytarot.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.preference.PreferenceManager;

import com.amcwustl.dailytarot.R;
import com.amcwustl.dailytarot.data.CardDbHelper;
import com.amcwustl.dailytarot.models.Card;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class CustomSpreadActivity extends BaseActivity {
    private static final String TAG = "Custom Spread Activity";
    private CardDbHelper dbHelper;
    private ImageView deckImageView;
    private FrameLayout droppableArea;
    private AlertDialog instructionsDialog;
    SharedPreferences preferences;
    private String cardType;
    private HashMap<Integer, Long> drawnCards;
    private int drawnCardCount = 0;
    private List<ImageView> droppedImages;
    private LinearLayout lockLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_custom_spread);
        super.onCreate(savedInstanceState);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        cardType = preferences.getString(UserSettingsActivity.CARD_TYPE_TAG, "");

        lockLayout = findViewById(R.id.lockLayout);

        deckImageView = findViewById(R.id.deckImageView);
        droppableArea = findViewById(R.id.droppableArea);

        dbHelper = new CardDbHelper(this);

        drawnCards = new HashMap<>();
        droppedImages = new ArrayList<>();

        setupCardType();
        setupDeckTouchListener();
        setupDroppableArea();
        setupLockLayoutButton();
    }

    private void setupCardType(){
        String resourceName = "cover" + cardType;
        @SuppressLint("DiscouragedApi") int resourceId = getResources().getIdentifier(resourceName, "drawable", getPackageName());
        if (resourceId != 0) {
            deckImageView.setImageResource(resourceId);
        } else {
            Log.e(TAG, "Resource not found for card type: " + cardType);
        }
    }

    private void setupDeckTouchListener() {
        deckImageView.setOnLongClickListener(v -> {
            ImageView newCard = createNewCardImageView();

            // Initially, set the card at the deck's position or make it invisible
            newCard.setX(deckImageView.getX());
            newCard.setY(deckImageView.getY());
            droppableArea.addView(newCard);

            ClipData.Item item = new ClipData.Item((CharSequence) v.getTag());
            ClipData dragData = new ClipData(
                    (CharSequence) v.getTag(),
                    new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN},
                    item);
            View.DragShadowBuilder myShadow = new View.DragShadowBuilder(deckImageView);

            newCard.startDragAndDrop(dragData, myShadow, newCard, 0);

            return true;
        });
    }


    private ImageView createNewCardImageView() {
        ImageView imageView = new ImageView(this);
        int widthInPx = convertDpToPx(100); // Convert 100dp to pixels
        int heightInPx = convertDpToPx(145); // Convert 145dp to pixels

        imageView.setLayoutParams(new FrameLayout.LayoutParams(widthInPx, heightInPx));
        if(Objects.equals(cardType, "")){
            imageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.cover, null));
        }else {
            imageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.cover_light, null));
        }
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

        return imageView;
    }

    private int convertDpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

    private void setupDroppableArea() {
        droppableArea.setOnDragListener((view, dragEvent) -> {
            switch (dragEvent.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // Retrieve the dragged view and set it visible
                    View draggedView = (View) dragEvent.getLocalState();
                    draggedView.setVisibility(View.VISIBLE);
                    return true;
                case DragEvent.ACTION_DROP:
                    draggedView = (View) dragEvent.getLocalState();
                    draggedView.setX(dragEvent.getX() - draggedView.getWidth() / 2f);
                    draggedView.setY(dragEvent.getY() - draggedView.getHeight() / 2f);

                    long cardId = drawRandomCard();
                    drawnCards.put(drawnCardCount, cardId);
                    drawnCardCount ++;
                    Log.d(TAG, "Drawn card count is: " + drawnCardCount);
                    droppedImages.add((ImageView) draggedView);
                    if (drawnCardCount < 9){
//                        setupDeckTouchListener();
                    } else {
                        lockLayout();
                    }
                    return true;
            }
            return false;
        });
    }

    private long drawRandomCard(){
        Random random = new Random();
        long maxCardId = 78;
        long randomValue;
        do {
            randomValue = (Math.abs(random.nextLong()) % maxCardId) + 1;
        } while (drawnCards.containsValue(randomValue));

        return randomValue;

    }

    private void setupLockLayoutButton(){
        lockLayout.setOnClickListener(view -> {
            ImageView lockIcon = findViewById(R.id.lockIcon);
            TextView lockText = findViewById(R.id.lockText);

            int goldColor = Color.parseColor("#FFD700");

            lockIcon.setColorFilter(goldColor, PorterDuff.Mode.SRC_IN);

            lockText.setTextColor(goldColor);

            lockLayout();

        });
    }

    private void lockLayout(){
        deckImageView.setOnLongClickListener(null);
        Log.d(TAG, "Method is being called");

        for (int i = 0; i < drawnCardCount;i++){
            int finalI = i;
            ImageView card = droppedImages.get(i);
            card.setOnClickListener(view -> {
                flipCard(card, drawnCards.get(finalI));
            });
        }

    }

    private void flipCard(ImageView card, long cardId) {

        Random random = new Random();
        int randomOrientation = random.nextInt(2);
        Card drawnCard = dbHelper.getCardById(cardId);
        drawnCard.setOrientation(randomOrientation);
        String cardName = drawnCard.getNameShort();
        String cardType = preferences.getString(UserSettingsActivity.CARD_TYPE_TAG, "");
        String resourceName = cardName + cardType;

        @SuppressLint("DiscouragedApi") int resourceId = getResources().getIdentifier(resourceName, "drawable", getPackageName());

        if (resourceId != 0) {
            float cameraDistance = 20 * getResources().getDisplayMetrics().density * card.getWidth();
            card.setCameraDistance(cameraDistance);

            ObjectAnimator firstHalfFlip = ObjectAnimator.ofFloat(card, "rotationY", 0f, 90f);
            firstHalfFlip.setDuration(250);

            ObjectAnimator secondHalfFlip = ObjectAnimator.ofFloat(card, "rotationY", -90f, 0f);
            secondHalfFlip.setDuration(250);

            firstHalfFlip.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    card.setImageResource(resourceId);
                    if (drawnCard.getOrientation() == 1) {
                        card.setRotation(180f);
                    }
                    secondHalfFlip.start();
                }
            });

            firstHalfFlip.start();
            card.setOnLongClickListener(view -> {
                navigateToCardDetail(drawnCard.getId());
                return true;
            });
        } else {
            Log.e(TAG, "Resource not found for card: " + cardName);
        }
    }

    private void navigateToCardDetail(Long cardId) {
        Intent intent = new Intent(CustomSpreadActivity.this, CardDetailActivity.class);
        intent.putExtra("card_id", cardId);
        startActivity(intent);
    }

}