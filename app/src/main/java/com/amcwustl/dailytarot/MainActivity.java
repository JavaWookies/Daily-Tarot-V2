package com.amcwustl.dailytarot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import android.widget.ImageView;
import com.amcwustl.dailytarot.activities.SignUpActivity;
import com.amcwustl.dailytarot.data.CardDbHelper;
import com.amplifyframework.core.Amplify;

public class MainActivity extends AppCompatActivity {
  private final String TAG = "MainActivity";
  ImageView moveToSignUp;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

      CardDbHelper dbHelper = new CardDbHelper(this);
      dbHelper.populateDatabaseWithJsonData(this);

      moveToSignUp = findViewById(R.id.MainActivitySignUpImageView);



      setupMovetoSignup();
  }


    void setupMovetoSignup() {
        moveToSignUp.setOnClickListener(v -> {
            Intent goToSettingsActivityIntent = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(goToSettingsActivityIntent);
        });

    }
}