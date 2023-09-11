package com.amcwustl.dailytarot.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.amcwustl.dailytarot.R;

public class SignUpActivity extends AppCompatActivity {
  public static final String TAG = "SignupActivity";

  EditText emailEditText;
  EditText passwordEditText;
  Button submitButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sign_up);

    emailEditText = findViewById(R.id.SignUpActivityEmailEditTextTextEmailAddress);
    passwordEditText = findViewById(R.id.SignUpActivityPasswordEditTextTextPassword);
    submitButton = findViewById(R.id.SignUpActivitySignUpButton);

    setupSubmitButton();

  }

// TODO: Setup intent and onclick confirm sign-up with AWS
void setupSubmitButton() {
  submitButton.setOnClickListener(v -> {
    Intent goToLoginIntent = new Intent(SignUpActivity.this, VerifyActivity.class);
    startActivity(goToLoginIntent);
  });
}

}