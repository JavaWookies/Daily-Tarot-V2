package com.amcwustl.dailytarot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import android.widget.Button;
import android.widget.ImageView;


import com.amcwustl.dailytarot.activities.LoginActivity;
import com.amcwustl.dailytarot.activities.PastReadingsActivity;
import com.amcwustl.dailytarot.activities.ReadingActivity;

import com.amcwustl.dailytarot.activities.SignUpActivity;
import com.amcwustl.dailytarot.activities.UserSettingsActivity;
import com.amcwustl.dailytarot.activities.ViewAllCardsActivity;
import com.amcwustl.dailytarot.data.CardDbHelper;
import com.amplifyframework.core.Amplify;

public class MainActivity extends AppCompatActivity {
  private final String TAG = "MainActivity";
  Button goToReading;
  ImageView moveToSignUp;
  Button viewAllCards;
  Button goToSettings;
  Button goToPastReadings;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);


    CardDbHelper dbHelper = new CardDbHelper(this);

    if (dbHelper.isDatabaseEmpty()) {
      dbHelper.populateDatabaseWithJsonData(this);
      Log.d("MainActivity", "Database populated with data.");
    }

    goToReading = findViewById(R.id.MainActivityViewReadingButton);
    moveToSignUp = findViewById(R.id.MainActivitySignUpImageView);
    viewAllCards = findViewById(R.id.MainActivitySeeAllCardsButton);
    goToSettings = findViewById(R.id.TempGoToSettingsButton);
    goToPastReadings = findViewById(R.id.GoToPastReading);

    setupMoveToSignup();
    setupViewAllCards();
    setupGoToReading();
    setupTempGoToSettings();
    setupGoToPastReading();
  }

  @Override
  protected void onResume() {
    super.onResume();

    Amplify.Auth.getCurrentUser(
            authUser -> {
            },
            error -> {
              Intent signInIntent = new Intent(MainActivity.this, LoginActivity.class);
              startActivity(signInIntent);

              finish();
            }
    );
  }

  void setupTempGoToSettings(){
    goToSettings.setOnClickListener(view -> {
      Intent goToUserSettingsActivityIntent = new Intent(MainActivity.this, UserSettingsActivity.class);
      startActivity(goToUserSettingsActivityIntent);
    });
  }


  void setupMoveToSignup() {
    moveToSignUp.setOnClickListener(v -> {
      Intent goToSettingsActivityIntent = new Intent(MainActivity.this, SignUpActivity.class);
      startActivity(goToSettingsActivityIntent);
    });
  }

  void setupViewAllCards() {
    viewAllCards.setOnClickListener(v -> {
      Intent goToSettingsActivityIntent = new Intent(MainActivity.this, ViewAllCardsActivity.class);
      startActivity(goToSettingsActivityIntent);
    });
  }

  void setupGoToReading() {
    goToReading.setOnClickListener(view -> {
      Intent goToReadingActivityEvent = new Intent(MainActivity.this, ReadingActivity.class);
      startActivity(goToReadingActivityEvent);
    });
  }

  void setupGoToPastReading() {
    goToPastReadings.setOnClickListener(view -> {
      Intent goToPastReadingActivityEvent = new Intent(MainActivity.this, PastReadingsActivity.class);
      startActivity(goToPastReadingActivityEvent);
    });
  }
}