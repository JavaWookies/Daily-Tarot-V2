package com.amcwustl.dailytarot;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;

import com.amcwustl.dailytarot.activities.BaseActivity;

import com.amcwustl.dailytarot.data.CardDbHelper;



public class MainActivity extends BaseActivity {
  private final String TAG = "MainActivity";



  @Override
  protected void onCreate(Bundle savedInstanceState) {
    setContentView(R.layout.activity_main);
    super.onCreate(savedInstanceState);

    myNavView = findViewById(R.id.MainActivityNavigationView);



    CardDbHelper dbHelper = new CardDbHelper(this);

    if (dbHelper.isDatabaseEmpty()) {
      dbHelper.populateDatabaseWithJsonData(this);
      Log.d("MainActivity", "Database populated with data.");
    }


  }

}





