package com.amcwustl.dailytarot.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.amcwustl.dailytarot.R;
import com.amcwustl.dailytarot.data.CardDbHelper;
import com.amcwustl.dailytarot.models.Card;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class CardDetailActivity extends BaseActivity {

  // Define your views
  private TextView nameTextView;
  private TextView descTextView;
  private ImageView cardImageView;
  private AdView mAdView;
  SharedPreferences preferences;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    setContentView(R.layout.activity_card_detail);
    super.onCreate(savedInstanceState);

    mAdView = findViewById(R.id.adView);
    AdRequest adRequest = new AdRequest.Builder().build();
    mAdView.loadAd(adRequest);

    preferences = PreferenceManager.getDefaultSharedPreferences(this);

    // Initialize your views
    nameTextView = findViewById(R.id.CardDetailActivityCardNameTextView);
    descTextView = findViewById(R.id.CardDetailActivityCardDescTextView);
    cardImageView = findViewById(R.id.CardDetailActivitySingleCardImageView);

    Long cardId = getIntent().getLongExtra("card_id", -1);

    // Retrieve the card details using this ID
    CardDbHelper dbHelper = new CardDbHelper(this);
    Card card = dbHelper.getCardById(cardId);

    if (card != null) {
      nameTextView.setText(card.getName());

      StringBuilder cardInfoBuilder = new StringBuilder();
      cardInfoBuilder.append("Card Description: ").append(card.getDesc()).append("\n\n")
              .append("Card Meaning Face Up: ").append(card.getMeaningUp()).append("\n\n")
              .append("Card Meaning Reversed: ").append(card.getMeaningRev());

      descTextView.setText(cardInfoBuilder.toString());

      String cardName = card.getNameShort();
      String cardType = preferences.getString(UserSettingsActivity.CARD_TYPE_TAG, "");
      String resourceName = cardName + cardType;
      int resourceId = getResources().getIdentifier(resourceName, "drawable", getPackageName());

      cardImageView.setImageResource(resourceId);
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    if (mAdView != null) {
      mAdView.resume();
    }
  }

  @Override
  public void onPause() {
    if (mAdView != null) {
      mAdView.pause();
    }
    super.onPause();
  }

  @Override
  public void onDestroy() {
    if (mAdView != null) {
      mAdView.destroy();
    }
    super.onDestroy();
  }
}

