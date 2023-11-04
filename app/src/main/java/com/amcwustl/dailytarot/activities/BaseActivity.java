package com.amcwustl.dailytarot.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.amcwustl.dailytarot.MainActivity;
import com.amcwustl.dailytarot.R;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class BaseActivity extends AppCompatActivity {

    public DrawerLayout drawerLayout;
    public NavigationView myNavView;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Note: Assuming you have the views set up in every child activity's layout
        myNavView = findViewById(R.id.MainActivityNavigationView);
        drawerLayout = findViewById(R.id.my_drawer_layout);

        // Setup the drawer toggle and other common navigation tasks
        setupNavigation();
    }

    protected void setupNavigation() {
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // Set up item click for navigation
        setupNavClick();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    protected void setupNavClick(){
        myNavView.setNavigationItemSelectedListener(item -> {

            int itemId = item.getItemId();
            Log.i("MainActivity", "the logged item is:" + itemId);
            if (itemId == R.id.nav_home) {
                Intent mainActivityIntent = new Intent(this, MainActivity.class);
                startActivity(mainActivityIntent);
                return true;
            } else if (itemId == R.id.nav_settings) {
                Intent userSettingsIntent = new Intent(this, UserSettingsActivity.class);
                startActivity(userSettingsIntent);
                Log.i("MainActivity", "User Settings Clicked");
                return true;
            } else if (itemId == R.id.nav_library) {
                Intent viewAllCardsIntent = new Intent(this, ViewAllCardsActivity.class);
                startActivity(viewAllCardsIntent);
                return true;
            } else if (itemId == R.id.nav_reading) {
                Intent readingIntent = new Intent(this, ReadingActivity.class);
                startActivity(readingIntent);
                return true;
            }
//      drawerLayout.closeDrawer();
            return true;
        });
    }
}

