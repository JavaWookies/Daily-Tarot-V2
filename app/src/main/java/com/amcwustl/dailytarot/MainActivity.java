package com.amcwustl.dailytarot;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.preference.PreferenceManager;

import com.amcwustl.dailytarot.activities.BaseActivity;
import com.amcwustl.dailytarot.activities.CustomSpreadActivity;
import com.amcwustl.dailytarot.activities.DailyCardActivity;
import com.amcwustl.dailytarot.activities.QuizActivity;
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
  private FrameLayout tarotQuiz;
  private FrameLayout customSpread;
  private ImageView cardOne;
  private ImageView cardTwo;
  private ImageView cardThree;
  private ImageView cardFour;
  private ImageView cardFive;

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
    tarotQuiz = findViewById(R.id.tarot_quiz);
    customSpread = findViewById(R.id.custom_spread);


    cardOne = findViewById(R.id.imageView_card_1);
    cardTwo = findViewById(R.id.imageView_card_2);
    cardThree = findViewById(R.id.imageView_card_3);
    cardFour = findViewById(R.id.imageView_card_4);
    cardFive = findViewById(R.id.imageView_card_5);




    setupCardNavigation();
    setupCardTypes();
    storeFirstLaunchDate();
    showRatingDialogIfNeeded();

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

    tarotQuiz.setOnClickListener(v -> {
      Intent tarotQuizIntent = new Intent(MainActivity.this, QuizActivity.class);
      startActivity(tarotQuizIntent);
    });

    customSpread.setOnClickListener(v -> {
      Intent customSpreadIntent = new Intent(MainActivity.this, CustomSpreadActivity.class);
      startActivity(customSpreadIntent);
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
      cardFive.setImageResource(resourceId);
    } else {
      Log.e(TAG, "Resource not found for card type: " + cardType);
    }

  }

  private void storeFirstLaunchDate() {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    if (!prefs.contains("first_launch_date")) {
      SharedPreferences.Editor editor = prefs.edit();
      editor.putLong("first_launch_date", System.currentTimeMillis());
      editor.apply();
    }
  }

  private boolean shouldShowRatingDialog() {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    long firstLaunchDate = prefs.getLong("first_launch_date", 0);
    long fourteenDaysInMillis = 1 * 24 * 60 * 60 * 1000L;
    return System.currentTimeMillis() - firstLaunchDate >= fourteenDaysInMillis;
  }

  private void showRatingDialogIfNeeded() {
//    if (shouldShowRatingDialog()) {
      new AlertDialog.Builder(this)
              .setTitle("Enjoying the app?")
              .setMessage("Please help us out by providing a rating and review!")

              .setPositiveButton("Rate us", (dialog, which) -> {
                redirectToStoreForRating();
              })
              .setNegativeButton("Not now", (dialog, which) -> dialog.dismiss())
              .create()
              .show();
//    }
  }

  private void redirectToStoreForRating() {
    final String appPackageName = getPackageName();
    try {
      startActivity(new Intent(Intent.ACTION_VIEW,
              Uri.parse("market://details?id=" + appPackageName)));
    } catch (android.content.ActivityNotFoundException anfe) {
      startActivity(new Intent(Intent.ACTION_VIEW,
              Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
    }
  }

}





