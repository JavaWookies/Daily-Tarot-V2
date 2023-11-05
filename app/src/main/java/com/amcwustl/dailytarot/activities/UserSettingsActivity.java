package com.amcwustl.dailytarot.activities;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.preference.PreferenceManager;

import com.amcwustl.dailytarot.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class UserSettingsActivity extends BaseActivity {
    public static final String CARD_TYPE_TAG = "cardType";
    private static final String TAG = "UserSettingsActivity";
    SharedPreferences preferences;
    Spinner cardDisplaySpinner;
    Button saveSettingsButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_user_settings);
        super.onCreate(savedInstanceState);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        cardDisplaySpinner = findViewById(R.id.UserSettingsCardTypeSpinner);
        saveSettingsButton = findViewById(R.id.UserSettingsActivitySaveButton);


        setupCardDisplaySpinner();
        setupSaveSettingsButton();

        AdView mAdViewBanner = findViewById(R.id.adView);
        AdRequest adRequestBanner = new AdRequest.Builder().build();
        mAdViewBanner.loadAd(adRequestBanner);

    }



    void setupCardDisplaySpinner(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.card_display_options,
                R.layout.spinner_item
        );


        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

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

}

