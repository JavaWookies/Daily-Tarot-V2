package com.amcwustl.dailytarot.activities;



import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

import com.amcwustl.dailytarot.MainActivity;
import com.amcwustl.dailytarot.R;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class UserSettingsActivity extends BaseActivity {
    public static final String CARD_TYPE_TAG = "cardType";
    private static final String TAG = "UserSettingsActivity";
    SharedPreferences preferences;
    Spinner cardDisplaySpinner;
    Button saveSettingsButton;
    private InterstitialAd mInterstitialAd;
    private AdView mAdViewBanner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_user_settings);
        super.onCreate(savedInstanceState);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        cardDisplaySpinner = findViewById(R.id.UserSettingsCardTypeSpinner);
        saveSettingsButton = findViewById(R.id.UserSettingsActivitySaveButton);


        setupCardDisplaySpinner();
        setupSaveSettingsButton();

        mAdViewBanner = findViewById(R.id.adView);
        AdRequest adRequestBanner = new AdRequest.Builder().build();
        mAdViewBanner.loadAd(adRequestBanner);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        mInterstitialAd = interstitialAd;
                        Log.i(TAG, "onAdLoaded");
                        setupAdCallbacks();
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        Log.d(TAG, loadAdError.toString());
                        mInterstitialAd = null;
                    }
                });
    }


    @Override
    public void onBackPressed() {
        if (mInterstitialAd != null) {
            mInterstitialAd.show(this);
            setupAdCallbacks();
        } else {
            super.onBackPressed();
        }
    }


    void setupCardDisplaySpinner(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.card_display_options,
                android.R.layout.simple_spinner_item
        );


        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        cardDisplaySpinner.setAdapter(adapter);
    }

    void setupSaveSettingsButton() {
        saveSettingsButton.setOnClickListener(v -> {
            String selectedValue = cardDisplaySpinner.getSelectedItem().toString();
            String tagValue;

            if(selectedValue.equals("Gold")){
                tagValue="";
            } else {
                tagValue="_light";
            }

            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(CARD_TYPE_TAG, tagValue);
            editor.apply();

            Toast.makeText(UserSettingsActivity.this, "Settings saved!", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void setupNavClick() {
        myNavView.setNavigationItemSelectedListener(item -> {
            if (mInterstitialAd != null) {
                mInterstitialAd.show(UserSettingsActivity.this);
                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                    @Override
                    public void onAdClicked() {
                        Log.d(TAG, "Ad was clicked.");
                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        Log.d(TAG, "Ad dismissed fullscreen content.");
                        mInterstitialAd = null;
                        handleNavigationItemClick(item); // Perform navigation after ad dismissal
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        Log.e(TAG, "Ad failed to show fullscreen content.");
                        mInterstitialAd = null;
                        handleNavigationItemClick(item); // Consider performing navigation if ad fails to show
                    }

                    @Override
                    public void onAdImpression() {
                        Log.d(TAG, "Ad recorded an impression.");
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        Log.d(TAG, "Ad showed fullscreen content.");
                    }
                });
                return true;
            } else {
                return handleNavigationItemClick(item);
            }
        });
    }


    private boolean handleNavigationItemClick(MenuItem item) {
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
        return true;
    }

    private void setupAdCallbacks() {
        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
            @Override
            public void onAdClicked() {
                Log.d(TAG, "Ad was clicked.");
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                Log.d(TAG, "Ad dismissed fullscreen content.");
                mInterstitialAd = null;
            }

            @Override
            public void onAdFailedToShowFullScreenContent(AdError adError) {
                Log.e(TAG, "Ad failed to show fullscreen content.");
                mInterstitialAd = null;
            }

            @Override
            public void onAdImpression() {
                Log.d(TAG, "Ad recorded an impression.");
            }

            @Override
            public void onAdShowedFullScreenContent() {
                Log.d(TAG, "Ad showed fullscreen content.");
            }
        });
    }
}

