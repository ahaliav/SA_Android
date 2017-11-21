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

        db.execSQL("create table settings " +
                "(id integer primary key, key_set text, value_set text)");

        db.execSQL("create table user " +
                "(id integer primary key, name text, email text, is_registered integer)");

        //insertSettings("notifications","true");
        //insertSettings("calldialog","true");
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("drop table contacts");

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

    public Cursor selectContacts (String id, String phoneNumber) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "";
        if(!id.isEmpty()){
            query = " where id=" + id;
        }

        if(phoneNumber.length() > 8){
            String last9numbers = phoneNumber.substring(phoneNumber.length() - 9);
            if(!phoneNumber.isEmpty()){
                query = " where phone LIKE '%" + phoneNumber + "'";
            }
        }


        Cursor res =  db.rawQuery( "select * from contacts" + query, null );
        return res;
    }

    //user
    public boolean insertUser (String name, String is_registered, String email) {

        Cursor cur = selectUser(email);
        if(cur != null && cur.getCount() > 0){
            updateUser(name, is_registered, email);
        }
        else {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put("name", name);
            contentValues.put("email", email);
            contentValues.put("is_registered", is_registered);

            db.insert("user", null, contentValues);
        }

        return true;
    }

    public boolean updateUser (String name, String is_registered, String email) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("email", email);
        contentValues.put("is_registered", is_registered);

        db.update("user", contentValues,"email="+email, null);
        return true;
    }

    public Cursor selectUser (String email) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "";
        if(!email.isEmpty()){
            query = " where email='" + email + "'";
        }

        Cursor res =  db.rawQuery( "select * from user" + query, null );
        return res;
    }

    //settings
    public boolean insertSettings (String key_set, String value_set) {
        Cursor cur = selectSettings(key_set);
        if(cur != null && cur.getCount() > 0){
            updateSettings(key_set, value_set);
        }
        else {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put("key_set", key_set);
            contentValues.put("value_set", value_set);

            db.insert("settings", null, contentValues);
        }

        return true;
    }

    public boolean updateSettings (String key_set, String value_set) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("key_set", key_set);
        contentValues.put("value_set", value_set);

        db.update("settings", contentValues,"key_set='"+key_set +"'", null);
        return true;
    }

    public Cursor selectSettings (String key_set) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "";
        if(!key_set.isEmpty()){
            query = " where key_set='" + key_set + "'";
        }

        Cursor res =  db.rawQuery( "select * from settings " + query, null );
        return res;
    }
}
