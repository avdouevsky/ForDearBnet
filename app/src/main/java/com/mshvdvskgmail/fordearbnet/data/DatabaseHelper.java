package com.mshvdvskgmail.fordearbnet.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.mshvdvskgmail.fordearbnet.utilities.Constants;

/**
 * DB keeps the session key only.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "for_dear_bnet.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_SESSION_NOTE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.SESSION_TABLE);
        onCreate(db);
    }


    private static final String CREATE_SESSION_NOTE = "create table "
            + Constants.SESSION_TABLE
            + "("
            + Constants.COLUMN_ID + " integer primary key autoincrement, "
            + Constants.COLUMN_SESSION_KEY + " text not null "
            + ")";

    public boolean saveSessionKey(String sessionKey){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.COLUMN_SESSION_KEY, sessionKey);
        db.insert(Constants.SESSION_TABLE, null, values);
        return true;
    }

    public String findSessionKey(){
        String sessionKey;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Constants.SESSION_TABLE, null, Constants.COLUMN_ID + "=?",
                new String[]{String.valueOf(1)}, null, null, null, null);
        if (cursor!=null){
            cursor.moveToFirst();
        }
        sessionKey = cursor.getString(cursor.getColumnIndex(Constants.COLUMN_SESSION_KEY));
        return sessionKey;
    }

}