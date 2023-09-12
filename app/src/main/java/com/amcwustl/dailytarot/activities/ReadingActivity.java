package com.amcwustl.dailytarot.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.amcwustl.dailytarot.R;
import com.amcwustl.dailytarot.data.CardDbHelper;
import com.amcwustl.dailytarot.models.Card;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ReadingActivity extends AppCompatActivity {
    private Button drawCardsButton;
    private CardDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);

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
        // Initialize a list to store the drawn cards
        List<Card> drawnCards = new ArrayList<>();
        TextView cardOne = findViewById(R.id.PlaceHolderOne);
        TextView cardTwo = findViewById(R.id.PlaceHolderTwo);
        TextView cardThree = findViewById(R.id.PlaceHolderThree);

        Random random = new Random();
        int maxCardId = 78;
        while (drawnCards.size() < 3) {
            int randomCardId = random.nextInt(maxCardId) + 1;
            Card card = dbHelper.getCardById(randomCardId);
            if (card != null && !drawnCards.contains(card)) {
                drawnCards.add(card);
            }
        }
        cardOne.setText(drawnCards.get(0).getName());
        cardTwo.setText(drawnCards.get(1).getName());
        cardThree.setText(drawnCards.get(2).getName());

    }


}