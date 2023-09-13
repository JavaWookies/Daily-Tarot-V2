package com.amcwustl.dailytarot.activities;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.amcwustl.dailytarot.R;
import com.amcwustl.dailytarot.data.CardDbHelper;
import com.amcwustl.dailytarot.models.Card;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;

import com.amplifyframework.core.model.query.Where;
import com.amplifyframework.datastore.generated.model.Reading;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;

import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;



public class ReadingActivity extends AppCompatActivity {
    private static final String TAG = "Reading Activity";
    private Button drawCardsButton;
    private Button rewardAdButton;
    private CardDbHelper dbHelper;
    private String userId;
    private RewardedAd rewardedAd;
    private int userCoinCount =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);

        Amplify.Auth.getCurrentUser(
                authUser -> userId = authUser.getUserId(),
                error -> Log.e("MyAmplifyApp", "Error getting current user", error)
        );

        initializeMobileAds();

        drawCardsButton = findViewById(R.id.ReadingActivityDrawCardsButton);
        rewardAdButton = findViewById(R.id.ReadingActivityRewardAdButton);
        dbHelper = new CardDbHelper(this);
        setupDrawCardsButton();
        setupRewardAd();
        setupRewardAdButton();
        checkReadingForToday();

    }

    private void checkReadingForToday() {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(currentDate);

        List<Reading> readings = new ArrayList<>();

        Amplify.API.query(
                ModelQuery.list(Reading.class),
                response -> {
                    for (Reading reading : response.getData()){
                        if(reading.getUserId().equals(userId) && reading.getDateCreated().equals(formattedDate)){
                            readings.add(reading);
                        }
                    }

                    if (!(readings.size() > 0)){
                        userCoinCount = 10;
                    }

                    runOnUiThread(this::updateUIBasedOnCoinCount);
                },
                error -> {
                    Log.e(TAG, "Error retrieving readings for today: " + error);
                    // Handle the error appropriately.
                }
        );
    }

    private void updateUIBasedOnCoinCount() {
        // Update the visibility of the "Draw Cards" button and "Watch Ad" button based on userCoinCount.
        if (userCoinCount > 0) {

            drawCardsButton.setVisibility(View.VISIBLE);
            rewardAdButton.setVisibility(View.GONE); // Hide the "Watch Ad" button.
        } else {
            // User has no coins, so hide the "Draw Cards" button.
            drawCardsButton.setVisibility(View.GONE);
            rewardAdButton.setVisibility(View.VISIBLE); // Show the "Watch Ad" button.
        }
    }

    private void initializeMobileAds(){
        MobileAds.initialize(this, initializationStatus -> {
        });
    }

    private void setupDrawCardsButton(){
        drawCardsButton.setOnClickListener(view -> {
            drawThreeRandomCards();
        });
    }

    private void drawThreeRandomCards() {

        List<Card> drawnCards = new ArrayList<>();
        List<ImageView> imageViewList = new ArrayList<>();
        ImageView cardOne = findViewById(R.id.ReadingActivityDrawnCardOne);
        cardOne.setRotation(0);
        ImageView cardTwo = findViewById(R.id.ReadingActivityDrawnCardTwo);
        cardTwo.setRotation(0);
        ImageView cardThree = findViewById(R.id.ReadingActivityDrawnCardThree);
        cardThree.setRotation(0);
        imageViewList.add(cardOne);
        imageViewList.add(cardTwo);
        imageViewList.add(cardThree);


        TextView description = findViewById(R.id.ReadingActivityInterpretationPlaceHolder);

        Random random = new Random();
        int maxCardId = 78;
        HashSet<Integer> seenCards = new HashSet<>();
        while (drawnCards.size() < 3) {
            Integer randomCardId = random.nextInt(maxCardId) + 1;
            Card card = dbHelper.getCardById(randomCardId);
            if (card != null && !seenCards.contains(randomCardId)) {
                seenCards.add(randomCardId);
                drawnCards.add(card);
            }
        }

        for (Card card : drawnCards) {

            int randomOrientation = random.nextInt(2);
            card.setOrientation(randomOrientation);
        }

        for (int i = 0; i < 3; i++) {
            Card card = drawnCards.get(i);
            String resourceName = card.getNameShort(); // Get the name_short value of the card
            int resourceId = getResources().getIdentifier(resourceName, "drawable", getPackageName());

            if (resourceId != 0) {

                ImageView imageView = imageViewList.get(i);
                imageView.setImageResource(resourceId);

                if (card.getOrientation() == 1) {
                    imageView.setRotation(180f);
                }
            } else {

            }
        }

        StringBuilder interpretation = new StringBuilder();
        for(Card c : drawnCards) {
            interpretation.append(c.getName()).append(": ");
            if (c.getOrientation() == 0){
                interpretation.append(c.getMeaningUp()).append("\n");
            } else {
                interpretation.append(c.getMeaningRev()).append("\n");
            }
        }

        String result = interpretation.toString();
        description.setText(result);
        userCoinCount -= 10;
        pushReadingToDynamo(drawnCards, result);
        setupRewardAd();
        runOnUiThread(this::updateUIBasedOnCoinCount);


    }

    private void pushReadingToDynamo(List<Card> drawnCards, String interpretation){
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(currentDate);


        Reading readingToSave = Reading.builder()
                .userId(userId)
                .dateCreated(formattedDate)
                .cardOneId(drawnCards.get(0).getId().intValue())
                .cardOneOrientation(drawnCards.get(0).getOrientation())
                .cardTwoId(drawnCards.get(1).getId().intValue())
                .cardTwoOrientation(drawnCards.get(1).getOrientation())
                .cardThreeId(drawnCards.get(2).getId().intValue())
                .cardThreeOrientation(drawnCards.get(2).getOrientation())
                .interpretation(interpretation)
                .build();

        Amplify.API.mutate(
                ModelMutation.create(readingToSave),
                successResponse -> {
                    Log.i(TAG, "ReadingActivity.pushReadingToDynamo(): made Reading successfully");
                },
                failureResponse -> Log.i(TAG, "ReadingActivity.pushReadingToDynamo(): failed with this response" + failureResponse)
        );
    }


    public void setupRewardAd(){
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(this, "ca-app-pub-3940256099942544/5224354917",
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d(TAG, loadAdError.toString());
                        rewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd ad) {
                        rewardedAd = ad;
                        Log.d(TAG, "Ad was loaded.");
                    }
                });
    }

    public void setupRewardAdButton(){
        rewardAdButton.setOnClickListener(view -> {
            if (rewardedAd != null) {
                Activity activityContext = ReadingActivity.this;
                rewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                    @Override
                    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                        // Handle the reward.
                        Log.d(TAG, "The user earned the reward.");
                        int rewardAmount = rewardItem.getAmount();
                        String rewardType = rewardItem.getType();
                        Log.d(TAG, "Earned " + rewardAmount + " " + rewardType);
                        userCoinCount += rewardAmount;
                        runOnUiThread(() -> updateUIBasedOnCoinCount());

                        setupRewardAd();
                    }
                });

                rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdClicked() {
                        // Called when a click is recorded for an ad.
                        Log.d(TAG, "Ad was clicked.");

                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when ad is dismissed.
                        // Set the ad reference to null so you don't show the ad a second time.
                        Log.d(TAG, "Ad dismissed fullscreen content.");
                        rewardedAd = null;

                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        // Called when ad fails to show.
                        Log.e(TAG, "Ad failed to show fullscreen content.");
                        rewardedAd = null;

                    }

                    @Override
                    public void onAdImpression() {
                        // Called when an impression is recorded for an ad.
                        Log.d(TAG, "Ad recorded an impression.");
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        // Called when ad is shown.
                        Log.d(TAG, "Ad showed fullscreen content.");
                    }
                });
            } else {
                Log.d(TAG, "The rewarded ad wasn't ready yet.");
            }
        });
    }

}