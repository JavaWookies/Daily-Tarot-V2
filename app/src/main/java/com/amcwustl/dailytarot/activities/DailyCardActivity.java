package com.amcwustl.dailytarot.activities;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.amcwustl.dailytarot.R;

import java.util.Calendar;
import java.util.Random;

public class DailyCardActivity extends BaseActivity {
    private static final String TAG = "Daily Card Activity";
    private static final String PREFS_NAME = "DailyTarotPrefs";
    private static final String CARD_ID_KEY = "cardId";
    private static final String LAST_CARD_DAY_KEY = "lastCardDay";
    private long todaysCardId;

    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_daily_card);
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

    }

    private long generateRandomCardId(){
        Random random = new Random();
        int maxCardId = 78;
        return random.nextInt(maxCardId) + 1;

    }

    private void checkAndGenerateCardForToday() {
        int today = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
        int lastCardDay = sharedPreferences.getInt(LAST_CARD_DAY_KEY, -1);

        // If a card has not been generated today, generate a new one.
        if (today != lastCardDay) {
            todaysCardId = generateRandomCardId();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(LAST_CARD_DAY_KEY, today);
            editor.putLong(CARD_ID_KEY, todaysCardId);
            editor.apply();
        } else {
            // Retrieve the stored card ID
            todaysCardId = sharedPreferences.getLong(CARD_ID_KEY, -1);
        }
    }
}