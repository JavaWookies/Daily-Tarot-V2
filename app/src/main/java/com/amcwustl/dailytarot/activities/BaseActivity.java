package com.amcwustl.dailytarot.activities;

import android.content.Intent;
import android.os.Bundle;
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

        myNavView = findViewById(R.id.MainActivityNavigationView);
        drawerLayout = findViewById(R.id.my_drawer_layout);

        setupNavigation();
    }

    protected void setupNavigation() {
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

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
            if (itemId == R.id.nav_home) {
                Intent mainActivityIntent = new Intent(this, MainActivity.class);
                startActivity(mainActivityIntent);
                return true;
            } else if (itemId == R.id.nav_settings) {
                Intent userSettingsIntent = new Intent(this, UserSettingsActivity.class);
                startActivity(userSettingsIntent);
                return true;
            } else if (itemId == R.id.nav_library) {
                Intent viewAllCardsIntent = new Intent(this, ViewAllCardsActivity.class);
                startActivity(viewAllCardsIntent);
                return true;
            } else if (itemId == R.id.nav_reading) {
                Intent readingIntent = new Intent(this, ReadingActivity.class);
                startActivity(readingIntent);
                return true;
            } else if (itemId == R.id.nav_card_of_the_day) {
                Intent cardOfTheDayIntent = new Intent(this, DailyCardActivity.class);
                startActivity(cardOfTheDayIntent);
                return true;
            }
//      drawerLayout.closeDrawer();
            return true;
        });
    }
}

