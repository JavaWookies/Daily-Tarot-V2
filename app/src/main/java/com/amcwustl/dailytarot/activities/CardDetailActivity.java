package com.amcwustl.dailytarot.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.amcwustl.dailytarot.R;
import com.amcwustl.dailytarot.data.CardDbHelper;
import com.amcwustl.dailytarot.models.Card;

public class CardDetailActivity extends BaseActivity {

  // Define your views
  private TextView nameTextView;
  private TextView descTextView;
  private ImageView cardImageView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    setContentView(R.layout.activity_card_detail);
    super.onCreate(savedInstanceState);

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

      int imageResId = getResources().getIdentifier(
              card.getNameShort(),
              "drawable",
              getPackageName()
      );
      cardImageView.setImageResource(imageResId);
    }
  }
}

