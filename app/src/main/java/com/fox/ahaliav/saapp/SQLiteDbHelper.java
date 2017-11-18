package com.fox.ahaliav.saapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ahaliav_fox on 26 אוקטובר 2017.
 */

public class SQLiteDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FeedReader.db";

    public SQLiteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table subrieties " +
        "(id integer primary key, name text, subrietdate text)");

        db.execSQL("create table contacts " +
                "(id integer primary key, name text, phone text, email text,comments text)");
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table contacts");

        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public boolean insertSubrieties (String name, Date subrietdate) {
        SQLiteDatabase db = this.getWritableDatabase();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(subrietdate);

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("subrietdate", date);
        db.insert("subrieties", null, contentValues);
        return true;
    }

    public boolean updateSubriety (Integer id, String name, Date subrietdate) {
        SQLiteDatabase db = this.getWritableDatabase();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(subrietdate);

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("subrietdate", date);
        db.update("subrieties", contentValues,"id="+id.toString(), null);
        return true;
    }

    public boolean deleteSubriety (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete("subrieties", "id="+id.toString(), null);
        return true;
    }

    public Cursor selectSubrieties (String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "";
        if(!id.isEmpty()){
            query = " where id=" + id;
        }

        Cursor res =  db.rawQuery( "select * from subrieties" + query, null );
        return res;
    }

    //Contacts

    public boolean insertContact (String name, String phone, String comments, String email) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        contentValues.put("comments", comments);
        contentValues.put("email", email);

        db.insert("contacts", null, contentValues);
        return true;
    }

    public boolean updateContact (Integer id, String name, String phone, String comments, String email) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        contentValues.put("comments", comments);
        contentValues.put("email", email);

        db.update("contacts", contentValues,"id="+id.toString(), null);
        return true;
    }

    public boolean deleteContact (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete("contacts", "id="+id.toString(), null);
        return true;
    }

    public Cursor selectContacts (String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "";
        if(!id.isEmpty()){
            query = " where id=" + id;
        }

        Cursor res =  db.rawQuery( "select * from contacts" + query, null );
        return res;
    }
}
