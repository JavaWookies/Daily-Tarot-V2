package com.amcwustl.dailytarot;

import android.os.Bundle;
import android.util.Log;

import com.amcwustl.dailytarot.activities.BaseActivity;
import com.amcwustl.dailytarot.data.CardDbHelper;



@SuppressWarnings("resource")
public class MainActivity extends BaseActivity {


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





