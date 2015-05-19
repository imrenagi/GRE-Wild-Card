package com.imrenagi.greflashcard.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ListView;

import com.imrenagi.greflashcard.model.Word;

import java.util.ArrayList;
import java.util.List;

import static com.imrenagi.greflashcard.db.FeedReaderContract.SQL_CREATE_ENTRIES;
import static com.imrenagi.greflashcard.db.FeedReaderContract.SQL_DELETE_ENTRIES;

/**
 * Created by imrenagi on 5/18/15.
 */
public class FeedReaderDbHelper extends SQLiteOpenHelper {

    private SQLiteDatabase db;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 4;
    public static final String DATABASE_NAME = "FeedReader.db";

    public FeedReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        db = this.getWritableDatabase();
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void addWords(Word word) {
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_WORD_NAME, word.name);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_MEANING, word.meaning);

        long newRowId;
        newRowId = db.insert(
                FeedReaderContract.FeedEntry.TABLE_NAME,
                FeedReaderContract.FeedEntry.COLUMN_NAME_NULLABLE,
                values);
    }

    public List<Word> getWords() {
        List<Word> wordList = new ArrayList<Word>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + FeedReaderContract.FeedEntry.TABLE_NAME;

        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Word out = new Word();
                out.id = cursor.getInt(0);
                out.name = cursor.getString(1);
                out.meaning = cursor.getString(2);

                // Adding contact to list
                wordList.add(out);
            } while (cursor.moveToNext());
        }
        return wordList;
    }

}
