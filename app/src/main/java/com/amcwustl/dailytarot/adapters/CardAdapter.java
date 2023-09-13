package com.amcwustl.dailytarot.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amcwustl.dailytarot.R;
import com.amcwustl.dailytarot.models.Card;
import com.bumptech.glide.Glide;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
  private final List<Card> cards;

  public CardAdapter(List<Card> cards, Context context) {
    this.cards = cards;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    Card card = cards.get(position);

    int imageResId = holder.itemView.getContext().getResources().getIdentifier(
            card.getNameShort(),
            "drawable",
            holder.itemView.getContext().getPackageName()
    );

    Glide.with(holder.itemView.getContext())
            .load(imageResId)
            .placeholder(R.drawable.cover)
            .error(R.drawable.cover)
            .into(holder.cardImage);
  }

  @Override
  public int getItemCount() {
    return cards.size();
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {
//    TextView cardName;
//    TextView cardDescription;
    ImageView cardImage;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
//      cardName = itemView.findViewById(R.id.cardName);
//      cardDescription = itemView.findViewById(R.id.cardDescription);
      cardImage = itemView.findViewById(R.id.cardImage);
    }
  }
}
