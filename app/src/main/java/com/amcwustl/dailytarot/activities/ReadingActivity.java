package com.amcwustl.dailytarot.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.preference.PreferenceManager;

import com.amcwustl.dailytarot.R;
import com.amcwustl.dailytarot.data.CardDbHelper;
import com.amcwustl.dailytarot.models.Card;
import com.amcwustl.dailytarot.utilities.CardStateUtil;
import com.amcwustl.dailytarot.utilities.DemoActivityListener;
import com.facebook.ads.AdSize;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.model.Placement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;


public class ReadingActivity extends BaseActivity implements DemoActivityListener {
  private static final String TAG = "Reading Activity";
  private static final String HAS_READING_FOR_TODAY = "HAS_READING_FOR_TODAY";
  private Button drawCardsButton;
  private Button rewardAdButton;
  private ImageView deck;
  private ImageView cardOne;
  private ImageView cardTwo;
  private ImageView cardThree;
  private CardDbHelper dbHelper;
  private int userCoinCount = 0;
  private AlertDialog interpretationDialog;
  private Button btnGetInterpretation;
  private String currentInterpretation;

  private com.facebook.ads.AdView adView;

  SharedPreferences preferences;
  private int cardWidth = 0;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    setContentView(R.layout.activity_reading);
    super.onCreate(savedInstanceState);

    preferences = PreferenceManager.getDefaultSharedPreferences(this);

    cardOne = findViewById(R.id.ReadingActivityDrawnCardOne);
    cardTwo = findViewById(R.id.ReadingActivityDrawnCardTwo);
    cardThree = findViewById(R.id.ReadingActivityDrawnCardThree);
    deck = findViewById(R.id.ReadingActivityDeckImage);

    calculateCardDimensions();

    IronSource.loadRewardedVideo();

    btnGetInterpretation = findViewById(R.id.DailyCardActivityViewCardDetailsButton);
    btnGetInterpretation.setVisibility(View.INVISIBLE);
    btnGetInterpretation.setOnClickListener(view -> showInterpretationModal());
    drawCardsButton = findViewById(R.id.ReadingActivityDrawCardsButton);
    rewardAdButton = findViewById(R.id.ReadingActivityRewardAdButton);
    dbHelper = new CardDbHelper(this);
    setupCardTypes();
    setupDrawCardsButton();
    setupRewardAdButton();
    checkReadingForToday();

    List<Card> restoredCards = CardStateUtil.restoreReadingState(preferences, dbHelper, "ReadingActivity");
    if (!restoredCards.isEmpty() ) {
      positionCardsFaceUp(restoredCards);
      currentInterpretation = generateInterpretation(restoredCards);
      btnGetInterpretation.setVisibility(View.VISIBLE);
      drawCardsButton.setVisibility(View.GONE);
      rewardAdButton.setVisibility(View.VISIBLE);
    } else {
      deck.post(this::positionCardsOnDeck);
    }

    adView = new com.facebook.ads.AdView(this, "351150507666328_378616521586393", AdSize.BANNER_HEIGHT_50);
    loadMetaBannerAd();

  }

  @Override
  protected void onResume() {
    super.onResume();
    IronSource.onResume(this);
  }

  @Override
  public void onPause() {
    super.onPause();
    IronSource.onPause(this);
  }

  @Override
  public void onDestroy() {
    if (adView != null) {
      adView.destroy();
    }
    super.onDestroy();
  }

  private void setupCardTypes() {
    String cardType = preferences.getString(UserSettingsActivity.CARD_TYPE_TAG, "");
    String resourceName = "cover" + cardType;

    @SuppressLint("DiscouragedApi") int resourceId = getResources().getIdentifier(resourceName, "drawable", getPackageName());
    if (resourceId != 0) {
      deck.setImageResource(resourceId);
      cardOne.setImageResource(resourceId);
      cardTwo.setImageResource(resourceId);
      cardThree.setImageResource(resourceId);
    } else {
      Log.e(TAG, "Resource not found for card type: " + cardType);
    }

  }

  private void checkReadingForToday() {
    SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);

    @SuppressLint("SimpleDateFormat") String todayKey = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

    if(!sharedPreferences.getBoolean(todayKey + HAS_READING_FOR_TODAY, false)) {
      userCoinCount = 10;
    }
    runOnUiThread(this::updateUIBasedOnCoinCount);
  }

  private void updateUIBasedOnCoinCount() {
    if (userCoinCount > 0) {
      positionCardsOnDeck();
      drawCardsButton.setVisibility(View.VISIBLE);
      rewardAdButton.setVisibility(View.GONE);
    } else {
      drawCardsButton.setVisibility(View.GONE);
      rewardAdButton.setVisibility(View.VISIBLE);
    }
  }

  private void setupDrawCardsButton() {
    drawCardsButton.setOnClickListener(view -> drawThreeRandomCards());
  }

  private void drawThreeRandomCards() {
    List<Card> drawnCards = new ArrayList<>();
    cardOne.setRotation(0);
    cardTwo.setRotation(0);
    cardThree.setRotation(0);

    Random random = new Random();
    int maxCardId = 78;
    HashSet<Integer> seenCards = new HashSet<>();

    while (drawnCards.size() < 3) {
      Integer randomCardId = random.nextInt(maxCardId) + 1;
      Card card = dbHelper.getCardById(Long.valueOf(randomCardId));
      if (card != null && !seenCards.contains(randomCardId)) {
        seenCards.add(randomCardId);
        drawnCards.add(card);
      }
    }

    for (Card card : drawnCards) {
      int randomOrientation = random.nextInt(2);
      card.setOrientation(randomOrientation);
    }

    setupCardImages(drawnCards);
    btnGetInterpretation.setVisibility(View.VISIBLE);
    CardStateUtil.saveReadingState(preferences, drawnCards, "ReadingActivity");
    currentInterpretation = generateInterpretation(drawnCards);
    userCoinCount -= 10;
    markReadingForToday();
    runOnUiThread(this::updateUIBasedOnCoinCount);
  }


  private String generateInterpretation(List<Card> drawnCards) {
    StringBuilder interpretation = new StringBuilder();
    for (int i = 0; i < drawnCards.size(); i++) {
      switch (i) {
        case 0:
          if(drawnCards.get(i).getOrientation() == 0){
            interpretation.append(drawnCards.get(i).getIntPast()).append(" ");
          } else{
            interpretation.append(drawnCards.get(i).getIntPastRev()).append(" ");
          }
          break;
        case 1:
          if(drawnCards.get(i).getOrientation() == 0){
            interpretation.append(drawnCards.get(i).getIntPresent()).append(" ");
          } else {
            interpretation.append(drawnCards.get(i).getIntPresentRev()).append(" ");
          }
          break;
        case 2:
          if(drawnCards.get(i).getOrientation() == 0){
            interpretation.append(drawnCards.get(i).getIntFuture());
          } else {
            interpretation.append(drawnCards.get(i).getIntFutureRev());
          }
          break;
      }
    }
    return interpretation.toString();
  }


  private void markReadingForToday() {
    SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPreferences.edit();

    @SuppressLint("SimpleDateFormat") String todayKey = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

    editor.putBoolean(todayKey + HAS_READING_FOR_TODAY, true);
    editor.apply();
  }

  private void setupCardImages(List<Card> drawnCards) {
    float finalY = getResources().getDimension(R.dimen.margin_32dp);

    List<ImageView> cardViews = Arrays.asList(cardOne, cardTwo, cardThree);
    for (int i = 0; i < drawnCards.size(); i++) {
      Card card = drawnCards.get(i);
      ImageView cardView = cardViews.get(i);

      String cardType = preferences.getString(UserSettingsActivity.CARD_TYPE_TAG, "");
      String resourceName = "cover" + cardType;

      @SuppressLint("DiscouragedApi") int resourceId = getResources().getIdentifier(resourceName, "drawable", getPackageName());

      if (resourceId != 0) {
        cardView.setImageResource(resourceId);
        cardView.setVisibility(View.VISIBLE);

        float finalX = calculateCardFinalXPosition(i, getResources().getDisplayMetrics().widthPixels, cardWidth);

        ObjectAnimator moveX = ObjectAnimator.ofFloat(cardView, "x", finalX);
        ObjectAnimator moveY = ObjectAnimator.ofFloat(cardView, "y", finalY);
        moveX.setDuration(1000);
        moveY.setDuration(1000);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(moveX, moveY);
        animatorSet.addListener(new AnimatorListenerAdapter() {
          @Override
          public void onAnimationEnd(Animator animation) {
            flipCard(cardView, card);
          }
        });
        animatorSet.start();
      } else {
        Log.e(TAG, "Resource not found for card: " + resourceName);
      }
    }
  }

  private void positionCardsFaceUp(List<Card> cards) {
    List<ImageView> cardViews = Arrays.asList(cardOne, cardTwo, cardThree);
    btnGetInterpretation.setVisibility(View.VISIBLE);
    currentInterpretation = generateInterpretation(cards);

    for (int i = 0; i < cards.size(); i++) {
      Card card = cards.get(i);
      ImageView cardView = cardViews.get(i);

      String cardType = preferences.getString(UserSettingsActivity.CARD_TYPE_TAG, "");
      String resourceName = card.getNameShort() + cardType;
      @SuppressLint("DiscouragedApi") int resourceId = getResources().getIdentifier(resourceName, "drawable", getPackageName());
      cardView.setImageResource(resourceId);
      cardView.setVisibility(View.VISIBLE);
      cardView.setRotation(card.getOrientation() == 1 ? 180 : 0);
      cardView.setOnLongClickListener(v -> {
        navigateToCardDetail(card.getId());
        return true;
      });
    }
  }


  private void positionCardsOnDeck() {
    List<ImageView> cardViews = Arrays.asList(cardOne, cardTwo, cardThree);
    for (ImageView cardView : cardViews) {
      cardView.setX(deck.getX());
      cardView.setY(deck.getY());
      cardView.setVisibility(View.INVISIBLE); // Make them invisible initially
      btnGetInterpretation.setVisibility(View.INVISIBLE);
    }
  }

  private float calculateCardFinalXPosition(int cardIndex, int screenWidth, int cardWidth) {
    float spacing = (screenWidth - (3 * cardWidth)) / 4f;
    float secondCardX = spacing + cardWidth + spacing;
    float thirdCardX = secondCardX + cardWidth + spacing;

    if (cardIndex == 0) return spacing;
    if (cardIndex == 1) return secondCardX;
    if (cardIndex == 2) return thirdCardX;

    throw new IllegalArgumentException("Invalid card index");
  }

  private void flipCard(ImageView cardView, Card card) {
    String cardName = card.getNameShort();
    String cardType = preferences.getString(UserSettingsActivity.CARD_TYPE_TAG, "");
    String resourceName = cardName + cardType;
    @SuppressLint("DiscouragedApi") int resourceId = getResources().getIdentifier(resourceName, "drawable", getPackageName());

    ObjectAnimator flip1 = ObjectAnimator.ofFloat(cardView, "rotationY", 0f, 90f);
    flip1.setDuration(500);

    ObjectAnimator flip2 = ObjectAnimator.ofFloat(cardView, "rotationY", -90f, 0f);
    flip2.setDuration(500);

    flip1.addListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(Animator animation) {
        cardView.setImageResource(resourceId);
        flip2.start();

        cardView.setOnLongClickListener(v -> {
          navigateToCardDetail(card.getId());
          return true;
        });
        if (card.getOrientation() == 1) {
          cardView.setRotation(180);
        }
      }
    });

    flip1.start();
  }


  private void calculateCardDimensions() {
    ConstraintLayout constraintLayout = findViewById(R.id.DailyReadingConstraintLayout);
    ViewTreeObserver viewTreeObserver = constraintLayout.getViewTreeObserver();
    viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
      @Override
      public void onGlobalLayout() {
        constraintLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);

        cardWidth = deck.getWidth();

      }
    });
  }

  private void showInterpretationModal() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    View view = getLayoutInflater().inflate(R.layout.interpretation_modal, null);

    TextView tvInterpretationContent = view.findViewById(R.id.tv_interpretation_content);
    if (currentInterpretation != null) {
      tvInterpretationContent.setText(currentInterpretation);
    }
    Button btnCloseModal = view.findViewById(R.id.btn_close_modal);
    btnCloseModal.setOnClickListener(v -> interpretationDialog.dismiss());

    builder.setView(view);
    interpretationDialog = builder.create();
    interpretationDialog.show();
  }

  private void navigateToCardDetail(Long cardId) {
    Intent intent = new Intent(ReadingActivity.this, CardDetailActivity.class);
    intent.putExtra("card_id", cardId);
    startActivity(intent);
  }

  private void handlePotentialAdBlocker() {
    runOnUiThread(() -> {
      positionCardsOnDeck();
      userCoinCount += 10;
      updateUIBasedOnCoinCount();
    });
  }


  public void setupRewardAdButton() {
    rewardAdButton.setOnClickListener(view -> {
      if (IronSource.isRewardedVideoAvailable()){
        Log.d(TAG, "IronSource Ad Shown");
        IronSource.showRewardedVideo();
        Log.d(TAG, "The user earned the reward.");
        int rewardAmount = 10;
        positionCardsOnDeck();
        userCoinCount += rewardAmount;
        runOnUiThread(this::updateUIBasedOnCoinCount);
      } else {
        handlePotentialAdBlocker();
      }
    });
  }

  @Override
  public void setPlacementInfo(Placement placementInfo) {

  }

  private void loadMetaBannerAd() {
    LinearLayout adContainer = (LinearLayout) findViewById(R.id.meta_banner_container);
    adContainer.addView(adView);

    adView.loadAd();
  }
}