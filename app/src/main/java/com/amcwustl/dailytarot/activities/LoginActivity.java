package com.amcwustl.dailytarot.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.amcwustl.dailytarot.MainActivity;
import com.amcwustl.dailytarot.R;

public class LoginActivity extends AppCompatActivity {
  private static final String TAG = "LoginActivity";

  EditText emailEditText;
  EditText passwordEditText;
  Button submitButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    emailEditText = findViewById(R.id.LoginActivityLoginEditTextTextEmailAddress);
    passwordEditText = findViewById(R.id.LoginActivityPasswordEditTextTextPassword);
    submitButton = findViewById(R.id.LoginActivityLoginButton);

    String email = getIntent().getStringExtra("email");
    if (email != null && !email.isEmpty()) {
      emailEditText.setText(email);
    } else {
      Log.i(TAG, "Email was not passed or is empty");
    }

    setupSubmitButton();

  }

// TODO: Setup intent and onclick confirm sign-in with AWS
void setupSubmitButton() {
  submitButton.setOnClickListener(v -> {
    Intent goToLoginIntent = new Intent(LoginActivity.this, MainActivity.class);
    startActivity(goToLoginIntent);
  });
}

}