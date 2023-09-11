package com.amcwustl.dailytarot.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.amcwustl.dailytarot.R;

public class VerifyActivity extends AppCompatActivity {
  private static final String TAG = "VerifyActivity";
  EditText emailEditText;
  EditText verificationCodeEditText;
  Button submitButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_verify);

    emailEditText = findViewById(R.id.VerifyActivityEmailEditTextTextEmailAddress);
    verificationCodeEditText = findViewById(R.id.VerifyActivityCodeEditTextNumber);
    submitButton = findViewById(R.id.VerifyActivityVerifyButton);

    setupSubmitButton();

  }

  //  TODO: Setup intent and onclick confirm sign-up with AWS
  void setupSubmitButton() {
    submitButton.setOnClickListener(v -> {
      Intent goToLoginIntent = new Intent(VerifyActivity.this, LoginActivity.class);
      startActivity(goToLoginIntent);
    });
  }
}
