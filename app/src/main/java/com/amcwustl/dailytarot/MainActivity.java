package com.amcwustl.dailytarot;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import com.amcwustl.dailytarot.activities.BaseActivity;
import com.amcwustl.dailytarot.activities.DailyCardActivity;
import com.amcwustl.dailytarot.activities.ReadingActivity;
import com.amcwustl.dailytarot.activities.ViewAllCardsActivity;
import com.amcwustl.dailytarot.data.CardDbHelper;



@SuppressWarnings("resource")
public class MainActivity extends BaseActivity {

  private FrameLayout cardDailyReading;
  private FrameLayout cardOfTheDay;
  private FrameLayout viewAllCards;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    setContentView(R.layout.activity_main);
    super.onCreate(savedInstanceState);

    myNavView = findViewById(R.id.MainActivityNavigationView);

    cardDailyReading = findViewById(R.id.card_daily_reading);
    cardOfTheDay = findViewById(R.id.card_of_the_day);
    viewAllCards = findViewById(R.id.view_all_cards);

    setupCardNavigation();

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

}





