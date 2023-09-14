package com.amcwustl.dailytarot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;

import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;


import com.amcwustl.dailytarot.activities.AboutUsActivity;
import com.amcwustl.dailytarot.activities.LibraryActivity;
import com.amcwustl.dailytarot.activities.LoginActivity;
import com.amcwustl.dailytarot.activities.ReadingActivity;

import com.amcwustl.dailytarot.activities.SettingsActivity;
import com.amcwustl.dailytarot.activities.SignUpActivity;
import com.amcwustl.dailytarot.activities.ViewAllCardsActivity;
import com.amcwustl.dailytarot.data.CardDbHelper;
import com.amplifyframework.core.Amplify;
import com.google.android.material.navigation.NavigationView;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;

public class MainActivity extends AppCompatActivity {
  private final String TAG = "MainActivity";
//  private Button goToReading;
  ImageView moveToSignUp;
//  Button viewAllCards;

  public DrawerLayout drawerLayout;
  public ActionBarDrawerToggle actionBarDrawerToggle;
//  private DrawerLayout mDrawer;
//  private Toolbar toolbar;
//  private NavitgationView nvDrawer;
//    private ActionBarDrawerToggle drawerToggle;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    drawerLayout = findViewById(R.id.my_drawer_layout);
    actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

    drawerLayout.addDrawerListener(actionBarDrawerToggle);
    actionBarDrawerToggle.syncState();

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    CardDbHelper dbHelper = new CardDbHelper(this);
    dbHelper.populateDatabaseWithJsonData(this);
  }
//    goToReading = findViewById(R.id.MainActivityViewReadingButton);
//    moveToSignUp = findViewById(R.id.MainActivitySignUpImageView);
//    moveToSignUp = findViewById(R.id.MainActivitySignUpImageView);
//    viewAllCards = findViewById(R.id.MainActivitySeeAllCardsButton);
//
//    setupMoveToSignup();
//    setupViewAllCards();
//    setupGoToReading();


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

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {

    if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
      return true;
    }
    return super.onOptionsItemSelected(item);
  }
// This is where Rey turned into a coward... and gave up on the drawer


//  @Override
//  public boolean onOptionsItemSelected(MenuItem item) {
//    //The action bar home/up action should open or close the drawer.
//    switch (item.getItemId()) {
//      case android.R.id.home:
//        mDrawer.openDrawer(GravityCompat.START);
//        return true;
//    }
//
//    return super.onOptionsItemSelected(item);
//  }

//  @Override
//  protected void onCreate(Bundle savedInstanceState) {
//    super.onCreate(savedInstanceState);
//    nvDrawer = (NavigationView) findViewById(R.id.nvView);
//    // Setup drawer view
//    setupDrawerContent(nvDrawer);
//  }

//  private void setupDrawerContent(NavigationView navigationView){
//    navigationView.setNavigationItemSelectedListener(
//            new NavigationView.OnNavigationItemSelectedListener(){
//              @Override
//              public boolean onNavigationItemSelected(MenuItem menuItem){
//                selectDrawerItem(menuItem);
//                return true;
//              }
//            });
//  }

//  public void selectDrawerItem(MenuItem menuItem){
//    // create switchboard that uses intents to switch between activities
//    switch(menuItem.getItemId()){
//      case R.id.nav_home:
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
//        break;
//      case R.id.nav_settings:
//        Intent intent2 = new Intent(this, SettingsActivity.class);
//        startActivity(intent2);
//        break;
//      case R.id.nav_reading:
//        Intent intent3 = new Intent(this, ReadingActivity.class);
//        startActivity(intent3);
//        break;
//      case R.id.nav_about:
//        Intent intent4 = new Intent(this, AboutUsActivity.class);
//        startActivity(intent4);
//        break;
//      case R.id.nav_library:
//        Intent intent5 = new Intent(this, LibraryActivity.class);
//        startActivity(intent5);
//        break;
//      default:
//        Intent intent1 = new Intent(this, MainActivity.class);
//    }
//    try {
//        menuItem.setChecked(true);
//        setTitle(menuItem.getTitle());
//        mDrawer.closeDrawers();
//        } catch (Exception e) {
//        e.printStackTrace();
//    }
//    // Highlight the selected item has been done by NavigationView
//    menuItem.setChecked(true);
//    // Set action bar title
//    setTitle(menuItem.getTitle());
//    // Close the navigation drawer
//    mDrawer.closeDrawers();
//  }



//  void setupMoveToSignup() {
//    moveToSignUp.setOnClickListener(v -> {
//      Intent goToSettingsActivityIntent = new Intent(MainActivity.this, SignUpActivity.class);
//      startActivity(goToSettingsActivityIntent);
//    });
//  }
//
//  void setupViewAllCards() {
//    viewAllCards.setOnClickListener(v -> {
//      Intent goToSettingsActivityIntent = new Intent(MainActivity.this, ViewAllCardsActivity.class);
//      startActivity(goToSettingsActivityIntent);
//    });
//  }
//
//  void setupGoToReading(){
//    goToReading.setOnClickListener(view -> {
//        Intent goToReadingActivityEvent = new Intent(MainActivity.this, ReadingActivity.class);
//        startActivity(goToReadingActivityEvent);
//    });
//  }
}