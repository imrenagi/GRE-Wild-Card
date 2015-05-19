package com.imrenagi.greflashcard.db;

import android.provider.BaseColumns;

/**
 * Created by imrenagi on 5/18/15.
 */
public class FeedReaderContract {

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.COLUMN_NAME_WORD_NAME + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_MEANING + TEXT_TYPE + " )";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;

    public FeedReaderContract() {

    }

    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "words";
        public static final String COLUMN_NAME_ENTRY_ID = "wordid";
        public static final String COLUMN_NAME_WORD_NAME = "name";
        public static final String COLUMN_NAME_MEANING = "meaning";
        public static final String COLUMN_NAME_NULLABLE = "null";
    }
}
