package com.amcwustl.dailytarot.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.preference.PreferenceManager;

import com.amcwustl.dailytarot.R;
import com.amcwustl.dailytarot.data.CardDbHelper;
import com.amcwustl.dailytarot.models.Card;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class QuizActivity extends BaseActivity {
    private static final String TAG = "Quiz Activity";
    private int correctCardIndex;
    private int selectedCardIndex = -1;
    private ImageView cardOne;
    private ImageView cardTwo;
    private ImageView cardThree;
    private ImageView cardZero;
    private TextView quizHeader;
    private TextView associatedWords;
    private CardDbHelper dbHelper;
    private Button startQuizButton;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_quiz);
        super.onCreate(savedInstanceState);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        cardZero = findViewById(R.id.cardZero);
        cardOne = findViewById(R.id.cardOne);
        cardTwo = findViewById(R.id.cardTwo);
        cardThree = findViewById(R.id.cardThree);


        startQuizButton = findViewById(R.id.test_button);

        dbHelper = new CardDbHelper(this);

        associatedWords = findViewById(R.id.tv_placeholder);
        associatedWords.setVisibility(View.INVISIBLE);
        quizHeader = findViewById(R.id.tv_quiz_question);
        quizHeader.setVisibility(View.INVISIBLE);

        setupCardTypes();
        setupStartQuizButton();

    }

    private void setupCardTypes() {
        String cardType = preferences.getString(UserSettingsActivity.CARD_TYPE_TAG, "");
        String resourceName = "cover" + cardType;

        @SuppressLint("DiscouragedApi") int resourceId = getResources().getIdentifier(resourceName, "drawable", getPackageName());
        if (resourceId != 0) {
            cardZero.setImageResource(resourceId);
            cardOne.setImageResource(resourceId);
            cardTwo.setImageResource(resourceId);
            cardThree.setImageResource(resourceId);

        } else {
            Log.e(TAG, "Resource not found for card type: " + cardType);
        }
    }

    private void setupStartQuizButton() {
        startQuizButton.setOnClickListener(view -> {
            drawFourRandomCards();
            associatedWords.setVisibility(View.VISIBLE);
            quizHeader.setVisibility(View.VISIBLE);
            startQuizButton.setVisibility(View.GONE);
        });
    }


    private void drawFourRandomCards() {
        resetCardBorders();
        selectedCardIndex = -1;
        List<Card> drawnCards = new ArrayList<>();
        Random random = new Random();
        int maxCardId = 78;
        HashSet<Integer> seenCards = new HashSet<>();

        while (drawnCards.size() < 4) {
            Integer randomCardId = random.nextInt(maxCardId) + 1;
            if (!seenCards.contains(randomCardId)) {
                seenCards.add(randomCardId);
                Card card = dbHelper.getCardById(Long.valueOf(randomCardId));
                drawnCards.add(card);
            }
        }
        setupCardImages(drawnCards);
        setupQuiz(drawnCards);
    }

    private void setupCardImages(List<Card> drawnCards) {
        List<ImageView> imageViewList = new ArrayList<>();
        imageViewList.add(cardZero);
        imageViewList.add(cardOne);
        imageViewList.add(cardTwo);
        imageViewList.add(cardThree);

        for (int i = 0; i < 4; i++) {
            Card card = drawnCards.get(i);
            String cardName = card.getNameShort();
            String cardType = preferences.getString(UserSettingsActivity.CARD_TYPE_TAG, "");
            String resourceName = cardName + cardType;

            @SuppressLint("DiscouragedApi")
            int resourceId = getResources().getIdentifier(resourceName, "drawable", getPackageName());

            if (resourceId != 0) {
                ImageView imageView = imageViewList.get(i);
                float cameraDistance = 20 * getResources().getDisplayMetrics().density * imageView.getWidth();
                imageView.setCameraDistance(cameraDistance);

                ObjectAnimator firstHalfFlip = ObjectAnimator.ofFloat(imageView, "rotationY", 0f, 90f);
                firstHalfFlip.setDuration(250);

                ObjectAnimator secondHalfFlip = ObjectAnimator.ofFloat(imageView, "rotationY", -90f, 0f);
                secondHalfFlip.setDuration(250);

                firstHalfFlip.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        imageView.setImageResource(resourceId);
                        secondHalfFlip.start();
                    }
                });

                firstHalfFlip.start();
            } else {
                Log.e(TAG, "Resource not found for card: " + cardName);
            }
        }
    }

    private void setupQuiz(List<Card> drawnCards) {
        Random random = new Random();
        int randomCardIndex = random.nextInt(4);
        correctCardIndex = randomCardIndex;
        associatedWords.setText(drawnCards.get(randomCardIndex).getAssociatedWords());
        setupSubmitButton();
        setupCardSelection();

    }

    private void setupCardSelection() {
        List<ImageView> cards = Arrays.asList(cardZero, cardOne, cardTwo, cardThree);
        for (int i = 0; i < cards.size(); i++) {
            int finalI = i;
            cards.get(i).setOnClickListener(v -> {
                selectedCardIndex = finalI;
                highlightSelectedCard(finalI);
            });
        }
    }

    private void highlightSelectedCard(int cardIndex) {
        resetCardBorders();
        // Add yellow border to the selected card
        ImageView selectedCard = Arrays.asList(cardZero, cardOne, cardTwo, cardThree).get(cardIndex);
        // Assuming you have a drawable for the yellow border
        selectedCard.setBackground(getResources().getDrawable(R.drawable.yellow_border));

    }

    private void resetCardBorders() {
        List<ImageView> cards = Arrays.asList(cardZero, cardOne, cardTwo, cardThree);
        for (ImageView card : cards) {
            card.setBackground(null); // Remove any border
        }
    }

    private void setupSubmitButton() {
        Button submitButton = findViewById(R.id.submit_button);
        submitButton.setOnClickListener(v -> {
            if (selectedCardIndex != -1) {
                checkAnswerAndProvideFeedback();
            }
        });
    }

    private void checkAnswerAndProvideFeedback() {
        ImageView selectedCard = Arrays.asList(cardZero, cardOne, cardTwo, cardThree).get(selectedCardIndex);
        ImageView correctCard = Arrays.asList(cardZero, cardOne, cardTwo, cardThree).get(correctCardIndex);

        if (selectedCardIndex == correctCardIndex) {
            // User selected the correct card
            selectedCard.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.green_border, null));
            showResultBanner(true);
        } else {
            // User selected the wrong card
            selectedCard.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.red_border, null));
            correctCard.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.green_border, null));
            showResultBanner(false);
        }
    }

    private void showResultBanner(boolean isCorrect) {
        String message = isCorrect ? "Correct! Well done." : "Wrong answer. Try again!";
        Snackbar snackbar = Snackbar.make(findViewById(R.id.my_drawer_layout), message, Snackbar.LENGTH_INDEFINITE);

        snackbar.setAction("Next Question", view -> {
            // Reset borders and start a new round
            resetCardBorders();
            drawFourRandomCards();
        });

        snackbar.show();
    }

}