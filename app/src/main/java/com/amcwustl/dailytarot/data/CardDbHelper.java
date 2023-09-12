package com.amcwustl.dailytarot.data;

import static com.amcwustl.dailytarot.models.CardContract.SQL_CREATE_ENTRIES;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.amcwustl.dailytarot.R;
import com.amcwustl.dailytarot.models.Card;
import com.amcwustl.dailytarot.models.CardContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CardDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "TarotCardDatabase.db";

    public CardDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @SuppressLint("Range")
    public List<Card> getAllCards() {
        List<Card> cards = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + CardContract.CardEntry.TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Card card = new Card();
                card.setType(cursor.getString(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_TYPE)));
                card.setNameShort(cursor.getString(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_NAME_SHORT)));
                card.setName(cursor.getString(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_NAME)));
                card.setValue(cursor.getString(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_VALUE)));
                card.setValueInt(cursor.getInt(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_VALUE_INT)));
                card.setMeaningUp(cursor.getString(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_MEANING_UP)));
                card.setMeaningRev(cursor.getString(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_MEANING_REV)));
                card.setDesc(cursor.getString(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_DESC)));
                card.setOrientation(0);
                cards.add(card);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return cards;
    }

    public long insertCard(Card card) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CardContract.CardEntry.COLUMN_TYPE, card.getType());
        values.put(CardContract.CardEntry.COLUMN_NAME_SHORT, card.getNameShort());
        values.put(CardContract.CardEntry.COLUMN_NAME, card.getName());
        values.put(CardContract.CardEntry.COLUMN_VALUE, card.getValue());
        values.put(CardContract.CardEntry.COLUMN_VALUE_INT, card.getValueInt());
        values.put(CardContract.CardEntry.COLUMN_MEANING_UP, card.getMeaningUp());
        values.put(CardContract.CardEntry.COLUMN_MEANING_REV, card.getMeaningRev());
        values.put(CardContract.CardEntry.COLUMN_DESC, card.getDesc());

        return db.insert(CardContract.CardEntry.TABLE_NAME, null, values);
    }

    public void populateDatabaseWithJsonData(Context context) {
        CardDbHelper dbHelper = new CardDbHelper(context);
//        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String jsonData = loadJsonFromRawResource(context, R.raw.card_data);

        if (jsonData != null && !jsonData.isEmpty()) {
            try {
                JSONObject jsonRoot = new JSONObject(jsonData);
                JSONArray jsonCards = jsonRoot.getJSONArray("cards");

                for (int i = 0; i < jsonCards.length(); i++) {
                    JSONObject jsonCard = jsonCards.getJSONObject(i);
                    Card card = new Card();

                    card.setType(jsonCard.getString("type"));
                    card.setNameShort(jsonCard.getString("name_short"));
                    card.setName(jsonCard.getString("name"));
                    card.setValue(jsonCard.getString("value"));
                    card.setValueInt(jsonCard.getInt("value_int"));
                    card.setMeaningUp(jsonCard.getString("meaning_up"));
                    card.setMeaningRev(jsonCard.getString("meaning_rev"));
                    card.setDesc(jsonCard.getString("desc"));

                    // Insert the tarotCard into the database
                    dbHelper.insertCard(card);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                dbHelper.close();
            }
        }
    }

    private String loadJsonFromRawResource(Context context, int resourceId) {
        try {
            InputStream inputStream = context.getResources().openRawResource(resourceId);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder jsonStringBuilder = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                jsonStringBuilder.append(line);
            }

            bufferedReader.close();
            return jsonStringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Card getCardById(int cardId) {
        SQLiteDatabase db = getReadableDatabase();
        Card card = null;

        String[] projection = {
                CardContract.CardEntry._ID,
                CardContract.CardEntry.COLUMN_TYPE,
                CardContract.CardEntry.COLUMN_NAME_SHORT,
                CardContract.CardEntry.COLUMN_NAME,
                CardContract.CardEntry.COLUMN_VALUE,
                CardContract.CardEntry.COLUMN_VALUE_INT,
                CardContract.CardEntry.COLUMN_MEANING_UP,
                CardContract.CardEntry.COLUMN_MEANING_REV,
                CardContract.CardEntry.COLUMN_DESC
        };

        String selection = CardContract.CardEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(cardId)};

        Cursor cursor = db.query(
                CardContract.CardEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                card = new Card();
                card.setId(cursor.getLong(cursor.getColumnIndexOrThrow(CardContract.CardEntry._ID)));
                card.setType(cursor.getString(cursor.getColumnIndexOrThrow(CardContract.CardEntry.COLUMN_TYPE)));
                card.setNameShort(cursor.getString(cursor.getColumnIndexOrThrow(CardContract.CardEntry.COLUMN_NAME_SHORT)));
                card.setName(cursor.getString(cursor.getColumnIndexOrThrow(CardContract.CardEntry.COLUMN_NAME)));
                card.setValue(cursor.getString(cursor.getColumnIndexOrThrow(CardContract.CardEntry.COLUMN_VALUE)));
                card.setValueInt(cursor.getInt(cursor.getColumnIndexOrThrow(CardContract.CardEntry.COLUMN_VALUE_INT)));
                card.setMeaningUp(cursor.getString(cursor.getColumnIndexOrThrow(CardContract.CardEntry.COLUMN_MEANING_UP)));
                card.setMeaningRev(cursor.getString(cursor.getColumnIndexOrThrow(CardContract.CardEntry.COLUMN_MEANING_REV)));
                card.setDesc(cursor.getString(cursor.getColumnIndexOrThrow(CardContract.CardEntry.COLUMN_DESC)));
            }
            cursor.close();
        }

        return card;
    }
}
