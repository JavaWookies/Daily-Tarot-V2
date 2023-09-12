package com.amcwustl.dailytarot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import android.widget.Button;
import android.widget.ImageView;

import com.amcwustl.dailytarot.activities.ReadingActivity;
import com.amcwustl.dailytarot.activities.SignUpActivity;
import com.amcwustl.dailytarot.data.CardDbHelper;
import com.amplifyframework.core.Amplify;

public class MainActivity extends AppCompatActivity {
  private final String TAG = "MainActivity";
  private Button goToReading;
  ImageView moveToSignUp;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

      CardDbHelper dbHelper = new CardDbHelper(this);
      dbHelper.populateDatabaseWithJsonData(this);

      goToReading = findViewById(R.id.MainActivityViewReadingButton);

      moveToSignUp = findViewById(R.id.MainActivitySignUpImageView);



      setupMovetoSignup();
      setupGoToReading();
  }


    void setupMovetoSignup() {
        moveToSignUp.setOnClickListener(v -> {
            Intent goToSettingsActivityIntent = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(goToSettingsActivityIntent);
        });

    }

    void setupGoToReading(){
      goToReading.setOnClickListener(view -> {
          Intent goToReadingActivityEvent = new Intent(MainActivity.this, ReadingActivity.class);
          startActivity(goToReadingActivityEvent);
      });
    }
}