package com.amcwustl.dailytarot.activities;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;


import com.amcwustl.dailytarot.R;
import com.amcwustl.dailytarot.data.CardDbHelper;
import com.amcwustl.dailytarot.models.Card;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.core.Amplify;

import com.amplifyframework.datastore.generated.model.Reading;
import com.google.android.material.snackbar.Snackbar;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;



public class ReadingActivity extends AppCompatActivity {
    private Button drawCardsButton;
    private CardDbHelper dbHelper;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);

        Amplify.Auth.getCurrentUser(
                authUser -> userId = authUser.getUserId(),
                error -> Log.e("MyAmplifyApp", "Error getting current user", error)
        );

        drawCardsButton = findViewById(R.id.ReadingActivityDrawCardsButton);
        dbHelper = new CardDbHelper(this);
        setupDrawCardsButton();
    }

    private void setupDrawCardsButton(){
        drawCardsButton.setOnClickListener(view -> {
            drawThreeRandomCards();
        });
    }

    private void drawThreeRandomCards() {

        List<Card> drawnCards = new ArrayList<>();
        TextView cardOne = findViewById(R.id.PlaceHolderOne);
        TextView cardTwo = findViewById(R.id.PlaceHolderTwo);
        TextView cardThree = findViewById(R.id.PlaceHolderThree);
        TextView description = findViewById(R.id.InterpretationPlaceHolder);

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
        cardOne.setText(drawnCards.get(0).getName());
        cardTwo.setText(drawnCards.get(1).getName());
        cardThree.setText(drawnCards.get(2).getName());

        StringBuilder interpretation = new StringBuilder();
        for(Card card : drawnCards) {
            interpretation.append(card.getName()).append(": ");
            if (card.getOrientation() == 0){
                interpretation.append(card.getMeaningUp()).append("\n");
            } else {
                interpretation.append(card.getMeaningRev()).append("\n");
            }
        }

        String result = interpretation.toString();

        description.setText(result);

        pushReadingToDynamo(drawnCards, result);

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


}