package com.amcwustl.dailytarot.activities;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import com.amcwustl.dailytarot.R;
import com.amcwustl.dailytarot.data.CardDbHelper;
import com.amcwustl.dailytarot.models.Card;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

@SuppressWarnings("resource")
public class CardDetailActivity extends BaseActivity {

  private AdView mAdView;
  private int contentColor;
  private int titleColor;
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
    // Define your views
    TextView nameTextView = findViewById(R.id.CardDetailActivityCardNameTextView);
    TextView descTextView = findViewById(R.id.CardDetailActivityCardDescTextView);
    ImageView cardImageView = findViewById(R.id.CardDetailActivitySingleCardImageView);

    Long cardId = getIntent().getLongExtra("card_id", -1);

    contentColor = ContextCompat.getColor(this, R.color.translucent_off_white);
    titleColor = ContextCompat.getColor(this, R.color.white);

    // Retrieve the card details using this ID
    CardDbHelper dbHelper = new CardDbHelper(this);
    Card card = dbHelper.getCardById(cardId);

    if (card != null) {
      nameTextView.setText(card.getName());

      SpannableStringBuilder cardInfoBuilder = new SpannableStringBuilder();
      cardInfoBuilder.append(getStyledTitles("Card Description: "));
      cardInfoBuilder.append(getStyledCardInfo(card.getDesc()));
      cardInfoBuilder.append("\n\n");
      cardInfoBuilder.append(getStyledTitles("Card Meaning Face Up: "));
      cardInfoBuilder.append(getStyledCardInfo(card.getMeaningUp()));
      cardInfoBuilder.append("\n\n");
      cardInfoBuilder.append(getStyledTitles("Card Meaning Reversed: "));
      cardInfoBuilder.append(card.getMeaningRev());

      descTextView.setText(cardInfoBuilder);

      String cardName = card.getNameShort();
      String cardType = preferences.getString(UserSettingsActivity.CARD_TYPE_TAG, "");
      String resourceName = cardName + cardType;
      @SuppressLint("DiscouragedApi") int resourceId = getResources().getIdentifier(resourceName, "drawable", getPackageName());

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

  private SpannableString getStyledTitles(String title) {
    SpannableString spannableTitle = new SpannableString(title);

    spannableTitle.setSpan(new ForegroundColorSpan(titleColor), 0, title.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
    spannableTitle.setSpan(new StyleSpan(Typeface.BOLD), 0, title.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
    spannableTitle.setSpan(new UnderlineSpan(), 0, title.length() - 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
    return spannableTitle;
  }

  private SpannableString getStyledCardInfo(String content) {
    SpannableString spannableContent = new SpannableString(content);

    spannableContent.setSpan(new ForegroundColorSpan(contentColor), 0, content.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

    return spannableContent;
  }

}

