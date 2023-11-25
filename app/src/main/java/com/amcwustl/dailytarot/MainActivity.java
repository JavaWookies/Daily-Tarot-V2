package com.amcwustl.dailytarot;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.preference.PreferenceManager;

import com.amcwustl.dailytarot.activities.BaseActivity;
import com.amcwustl.dailytarot.activities.DailyCardActivity;
import com.amcwustl.dailytarot.activities.ReadingActivity;
import com.amcwustl.dailytarot.activities.UserSettingsActivity;
import com.amcwustl.dailytarot.activities.ViewAllCardsActivity;
import com.amcwustl.dailytarot.data.CardDbHelper;



@SuppressWarnings("resource")
public class MainActivity extends BaseActivity {
  private static final String TAG = "Home Activity";

  private FrameLayout cardDailyReading;
  private FrameLayout cardOfTheDay;
  private FrameLayout viewAllCards;
  private ImageView cardOne;
  private ImageView cardTwo;
  private ImageView cardThree;
  private ImageView cardFour;

  SharedPreferences preferences;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    setContentView(R.layout.activity_main);
    super.onCreate(savedInstanceState);

    preferences = PreferenceManager.getDefaultSharedPreferences(this);
    myNavView = findViewById(R.id.MainActivityNavigationView);
    cardDailyReading = findViewById(R.id.card_daily_reading);
    cardOfTheDay = findViewById(R.id.card_of_the_day);
    viewAllCards = findViewById(R.id.view_all_cards);

    cardOne = findViewById(R.id.imageView_card_1);
    cardTwo = findViewById(R.id.imageView_card_2);
    cardThree = findViewById(R.id.imageView_card_3);
    cardFour = findViewById(R.id.imageView_card_4);




    setupCardNavigation();
    setupCardTypes();

    CardDbHelper dbHelper = new CardDbHelper(this);

    if (dbHelper.isDatabaseEmpty()) {
      dbHelper.populateDatabaseWithJsonData(this);
      Log.d("MainActivity", "Database populated with data.");
    }
  }

  private void setupCardNavigation() {
    cardDailyReading.setOnClickListener(v -> {
      Intent readingIntent = new Intent(MainActivity.this, ReadingActivity.class);
      startActivity(readingIntent);
    });

    cardOfTheDay.setOnClickListener(v -> {
      Intent cardOfDayIntent = new Intent(MainActivity.this, DailyCardActivity.class);
      startActivity(cardOfDayIntent);
    });

    viewAllCards.setOnClickListener(v -> {
      Intent viewAllCardsIntent = new Intent(MainActivity.this, ViewAllCardsActivity.class);
      startActivity(viewAllCardsIntent);
    });


  }

  private void setupCardTypes() {
    String cardType = preferences.getString(UserSettingsActivity.CARD_TYPE_TAG, "");
    String resourceName = "cover" + cardType;

    @SuppressLint("DiscouragedApi") int resourceId = getResources().getIdentifier(resourceName, "drawable", getPackageName());
    if (resourceId != 0) {
      cardOne.setImageResource(resourceId);
      cardTwo.setImageResource(resourceId);
      cardThree.setImageResource(resourceId);
      cardFour.setImageResource(resourceId);
    } else {
      Log.e(TAG, "Resource not found for card type: " + cardType);
    }

  }

}





