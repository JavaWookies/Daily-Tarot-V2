package com.amcwustl.dailytarot.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.preference.PreferenceManager;

import com.amcwustl.dailytarot.R;
import com.amcwustl.dailytarot.data.CardDbHelper;
import com.amcwustl.dailytarot.models.Card;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Calendar;
import java.util.Random;

public class DailyCardActivity extends BaseActivity {
    private static final String TAG = "Daily Card Activity";
    private static final String CARD_ID_KEY = "cardId";
    private static final String LAST_CARD_DAY_KEY = "lastCardDay";
    private static final String HAS_SEEN_CARD_KEY = "hasSeenCard";
    private CardDbHelper dbHelper;
    private Button moreInfoButton;
    private ImageView card;
    private AdView mAdView;
    private long todaysCardId;

    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_daily_card);
        super.onCreate(savedInstanceState);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        card = findViewById(R.id.DailyCardActivityCardImageView);
        moreInfoButton = findViewById(R.id.DailyCardActivityViewCardDetailsButton);

        dbHelper = new CardDbHelper(this);

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        checkAndGenerateCardForToday();
        setupCardForDisplay();
        setupMoreInfoButton();

    }

    @Override
    protected void onResume() {
        super.onResume();


        if (mAdView != null) {
            mAdView.resume();
        }
    }

    private void setupCardForDisplay() {
        boolean hasSeenCard = sharedPreferences.getBoolean(HAS_SEEN_CARD_KEY, false);

        if (!hasSeenCard) {
            showCardBack();
            moreInfoButton.setVisibility(View.GONE);
            card.setOnClickListener(v -> {
                flipCard();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(HAS_SEEN_CARD_KEY, true);
                editor.apply();

                card.setOnClickListener(null);
            });
        } else {

            showCardFront();
        }
    }

    private void showCardBack() {
        String cardType = sharedPreferences.getString(UserSettingsActivity.CARD_TYPE_TAG, "");
        String resourceName = "cover" + cardType;

        int resourceId = getResources().getIdentifier(resourceName, "drawable", getPackageName());
        if (resourceId != 0) {
            card.setImageResource(resourceId);
        }
    }

    private void flipCard() {
        // Change the pivot point for the flip animation
        card.setCameraDistance(20 * getResources().getDisplayMetrics().density * card.getWidth());

        ObjectAnimator firstHalfFlip = ObjectAnimator.ofFloat(card, "rotationY", 0f, 90f);
        firstHalfFlip.setDuration(250);

        ObjectAnimator secondHalfFlip = ObjectAnimator.ofFloat(card, "rotationY", -90f, 0f);
        secondHalfFlip.setDuration(250);

        firstHalfFlip.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {

                showCardFront();

                secondHalfFlip.start();
            }
        });

        // Start the first half of the flip
        firstHalfFlip.start();
    }

    private void showCardFront() {
        String cardType = sharedPreferences.getString(UserSettingsActivity.CARD_TYPE_TAG, "");
        Card todaysCard = dbHelper.getCardById(todaysCardId);
        String resourceName = todaysCard.getNameShort() + cardType;

        int resourceId = getResources().getIdentifier(resourceName, "drawable", getPackageName());
        if (resourceId != 0) {
            card.setImageResource(resourceId);
        }

        card.setOnClickListener(v -> navigateToCardDetail(todaysCardId));
        moreInfoButton.setVisibility(View.VISIBLE);

    }

    private void setupMoreInfoButton(){
        moreInfoButton.setOnClickListener(v -> navigateToCardDetail(todaysCardId));
    }

    private long generateRandomCardId(){
        Random random = new Random();
        int maxCardId = 78;
        return random.nextInt(maxCardId) + 1;

    }

    private void checkAndGenerateCardForToday() {
        int today = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
        int lastCardDay = sharedPreferences.getInt(LAST_CARD_DAY_KEY, -1);

        if (today != lastCardDay) {
            todaysCardId = generateRandomCardId();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(LAST_CARD_DAY_KEY, today);
            editor.putLong(CARD_ID_KEY, todaysCardId);
            editor.putBoolean(HAS_SEEN_CARD_KEY, false);
            editor.apply();
        } else {
            todaysCardId = sharedPreferences.getLong(CARD_ID_KEY, -1);
        }
    }

    private void navigateToCardDetail(Long cardId) {
        Intent intent = new Intent(DailyCardActivity.this, CardDetailActivity.class);
        intent.putExtra("card_id", cardId);
        startActivity(intent);
    }
}