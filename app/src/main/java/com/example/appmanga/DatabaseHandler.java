package com.example.appmanga;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.appmanga.Model.Notify;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "notifyManager";
    private static final String TABLE = "notify";
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_TIME = "time";
    private static final String KEY_SEEN = "seen";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TITLE + " TEXT," + KEY_CONTENT + " TEXT," + KEY_TIME + " INTEGER," + KEY_SEEN + " INTEGER" +")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    public void addNotify(Notify notify) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID,notify.getId());
        values.put(KEY_TITLE,notify.getTitle());
        values.put(KEY_CONTENT,notify.getContent());
        values.put(KEY_TIME,notify.getReceivedTime());
        values.put(KEY_SEEN,notify.getSeen());
        db.insert(TABLE,null,values);
        db.close();
    }

    public Notify getNotify(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE,null,KEY_ID + " = ?", new String[] { String.valueOf(id) },null, null, null);
        if(cursor != null)
            cursor.moveToFirst();
        Notify notify = new Notify(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getLong(3), cursor.getInt(4));
        return notify;
    }

    public ArrayList<Notify> getAllNotify() {
        ArrayList<Notify> notifyList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while(cursor.isAfterLast() == false) {
            Notify contact = new Notify(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getLong(3), cursor.getInt(4));
            notifyList.add(contact);
            cursor.moveToNext();
        }
        return notifyList;
    }

    public int updateNotify(Notify notify) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, notify.getId());
        values.put(KEY_TITLE, notify.getTitle());
        values.put(KEY_CONTENT, notify.getContent());
        values.put(KEY_TIME,notify.getReceivedTime());
        values.put(KEY_SEEN,notify.getSeen());
        db.update(TABLE, values, KEY_ID + " = ?", new String[] { String.valueOf(notify.getId()) });
        db.close();
        return 0;
    }

    public void deleteNotify(Notify notify) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE, KEY_ID + " = ?", new String[] { String.valueOf(notify.getId()) });
        db.close();
    }
}
