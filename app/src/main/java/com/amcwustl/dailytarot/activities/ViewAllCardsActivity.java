package com.amcwustl.dailytarot.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.amcwustl.dailytarot.R;
import com.amcwustl.dailytarot.adapters.CardAdapter;
import com.amcwustl.dailytarot.data.CardDbHelper;
import com.amcwustl.dailytarot.models.Card;

import java.util.ArrayList;
import java.util.List;

public class ViewAllCardsActivity extends AppCompatActivity {

  RecyclerView recyclerView;
  CardAdapter cardAdapter;
  List<Card> tarotCardsList = new ArrayList<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_view_all_cards);

    recyclerView = findViewById(R.id.ViewAllCardsActivityRecyclerView);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));

    CardDbHelper dbHelper = new CardDbHelper(this);
    tarotCardsList.addAll(dbHelper.getAllCards());

    cardAdapter = new CardAdapter(tarotCardsList, this);
    recyclerView.setAdapter(cardAdapter);
  }
}