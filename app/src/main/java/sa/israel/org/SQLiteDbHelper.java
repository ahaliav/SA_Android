package sa.israel.org;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

        db.execSQL("create table groups " +
                "(id integer primary key, day text, fromtime text, tomtime text, location text, comment text, lang text, latitude float,longitude float, km float, dayNum integer DEFAULT 0)");

        db.execSQL("create table subrieties " +
                "(id integer primary key, name text, subrietdate text)");

        db.execSQL("create table contacts " +
                "(id integer primary key, name text, phone text, email text, comments text, orderby INTEGER DEFAULT 3000)");

        db.execSQL("create table settings " +
                "(id integer primary key, key_set text, value_set text)");

        db.execSQL("create table user " +
                "(id integer primary key, name text, email text, is_registered integer, password text, isconfirmed text)");

        db.execSQL("create table token " +
                "(token text, tokendate text)");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public boolean insertSubrieties(String name, Date subrietdate) {

        SQLiteDatabase db = this.getWritableDatabase();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(subrietdate);

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("subrietdate", date);
        db.insert("subrieties", null, contentValues);
        return true;
    }

    public boolean updateSubriety(Integer id, String name, Date subrietdate) {
        SQLiteDatabase db = this.getWritableDatabase();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(subrietdate);

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("subrietdate", date);
        db.update("subrieties", contentValues, "id=" + id.toString(), null);
        return true;
    }

    public boolean deleteSubriety(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete("subrieties", "id=" + id.toString(), null);
        return true;
    }

    public Cursor selectSubrieties(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "";
        if (!id.isEmpty()) {
            query = " where id=" + id;
        }

        Cursor res = db.rawQuery("select * from subrieties" + query, null);
        return res;
    }

    //Contacts

    public boolean insertContact(String name, String phone, String comments, String email) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor result = selectContacts(-1, phone, "");

        if (result != null) {
            while (result.moveToNext()) {
                int id = result.getInt(0);
                deleteContact(id, "");
                break;
            }

            if (!result.isClosed()) {
                result.close();
            }
        }

        ContentValues contentValues = new ContentValues();

        if (phone != null && !phone.isEmpty()) {
            phone = phone.replace("-", "");
            phone = phone.replace(" ", "");
        }

        contentValues.put("name", name);
        contentValues.put("phone", phone);
        contentValues.put("comments", comments);
        contentValues.put("email", email);

        db.insert("contacts", null, contentValues);
        return true;
    }

    public boolean updateContact(Integer id, String name, String phone, String comments, String email) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        contentValues.put("comments", comments);
        contentValues.put("email", email);

        db.update("contacts", contentValues, "id=" + id.toString(), null);
        return true;
    }

    public boolean updateContactOrderBy(String name, int orderby) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("orderby", orderby);

        Cursor result = db.rawQuery("select * from contacts where orderby < 3000 order by orderby", null);
        int order=1;
        if (result != null) {
            while (result.moveToNext()) {
                ContentValues content = new ContentValues();
                content.put("orderby",order++);
                int id = result.getInt(0);
                db.update("contacts", content, "id=" + id, null);
                break;
            }

            if (!result.isClosed()) {
                result.close();
            }
        }

        db.update("contacts", contentValues, "name='" + name + "'", null);
        return true;
    }

    public boolean deleteContact(Integer id, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();

        if (id > 0) {
            db.delete("contacts", "id=" + id.toString(), null);
        } else {
            db.delete("contacts", "phone='" + phone + "'", null);
        }


        return true;
    }

    public Cursor selectContacts(int id, String phoneNumber, String name) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor dbCursor = db.query("contacts", null, null, null, null, null, null);
        String[] columnNames = dbCursor.getColumnNames();

        boolean found = false;
        for(int i=0; i< columnNames.length;i++){
            if(columnNames[i].equals("orderby")){
                found = true;
            }
        }
        if(found == false)
            db.execSQL("ALTER TABLE contacts ADD COLUMN orderby INTEGER DEFAULT 3000");

        String query = "";
        if (id > 0) {
            query = " where id=" + id;
        }
        else if (phoneNumber.length() > 0) {
            if (!phoneNumber.isEmpty()) {
                query = " where phone LIKE '%" + phoneNumber + "'";
            }
        }
        else if (name.length() > 1) {
             query = " where name LIKE '%" + name + "%' OR comments LIKE '%" + name + "%' order by orderby, name";
        }

        Cursor res;
        if (query.equals(""))
            res = db.rawQuery("select * from contacts order by orderby, name", null);
        else
            res = db.rawQuery("select * from contacts " + query, null);

        return res;
    }

    public boolean isContactExist(String phoneNumber) {
        SQLiteDatabase db = this.getReadableDatabase();

        phoneNumber = phoneNumber.replace("-","").replace(" ","");
        Cursor res = db.rawQuery("select * from contacts where phone like'%" + phoneNumber + "%'", null);

        return (res != null && res.getCount() > 0);
    }

    //user
    public boolean insertUser(String name, String is_registered, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("delete from user");


        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("email", email);
        contentValues.put("is_registered", is_registered);
        contentValues.put("password", password);
        contentValues.put("isconfirmed", "true");

        db.insert("user", null, contentValues);

        db.close();

        return true;
    }

    public Cursor selectUser(String email) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "";
        if (!email.isEmpty()) {
            query = " where email='" + email + "'";
        }

        Cursor res = db.rawQuery("select * from user" + query, null);
        return res;
    }

    //settings
    public boolean insertSettings(String key_set, String value_set) {
        Cursor cur = selectSettings(key_set);
        if (cur != null && cur.getCount() > 0) {
            updateSettings(key_set, value_set);
        } else {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put("key_set", key_set);
            contentValues.put("value_set", value_set);

            db.insert("settings", null, contentValues);
        }

        return true;
    }

    public boolean updateSettings(String key_set, String value_set) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("key_set", key_set);
        contentValues.put("value_set", value_set);

        db.update("settings", contentValues, "key_set='" + key_set + "'", null);
        return true;
    }

    public Cursor selectSettings(String key_set) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "";
        if (!key_set.isEmpty()) {
            query = " where key_set='" + key_set + "'";
        }

        Cursor res = db.rawQuery("select * from settings " + query, null);

        if ((res == null || res.getCount() == 0) && (key_set == "notifications" || key_set == "calldialog")) {

            ContentValues contentValues = new ContentValues();
            contentValues.put("key_set", "notifications");
            contentValues.put("value_set", "true");
            db.insert("settings", null, contentValues);

            contentValues = new ContentValues();
            contentValues.put("key_set", "calldialog");
            contentValues.put("value_set", "true");
            db.insert("settings", null, contentValues);

            res = db.rawQuery("select * from settings " + query, null);
        }

        return res;
    }


    public String selectSettingsString(String key_set) {

        String val = "";
        try {
            SQLiteDatabase db = this.getReadableDatabase();

            String query = "";
            if (!key_set.isEmpty()) {
                query = " where key_set='" + key_set + "'";
            }

            Cursor result = db.rawQuery("select * from settings " + query, null);

            if (result != null) {

                while (result.moveToNext()) {
                    val = result.getString(2);
                    break;
                }

                if (!result.isClosed()) {
                    result.close();
                }
            }
        } catch (Exception ex) {
            String err = ex.getMessage();

        }

        return val;
    }


    //groups
    public Cursor selectGroups() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor dbCursor = db.query("groups", null, null, null, null, null, null);
        String[] columnNames = dbCursor.getColumnNames();

        boolean found = false;
        for(int i=0; i< columnNames.length;i++){
            if(columnNames[i].equals("dayNum")){
                found = true;
            }
        }
        if(found == false)
            db.execSQL("ALTER TABLE groups ADD COLUMN dayNum integer DEFAULT 0");

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        ContentValues contentValues = new ContentValues();
        contentValues.put("dayNum", 0);
        db.update("groups", contentValues, "dayNum=" + Integer.toString(day), null);

        Cursor res = db.rawQuery("select * from groups order by dayNum", null);
        return res;
    }

    public boolean deleteGroups() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor dbCursor = db.query("groups", null, null, null, null, null, null);
        String[] columnNames = dbCursor.getColumnNames();

        boolean found = false;
        for(int i=0; i< columnNames.length;i++){
            if(columnNames[i].equals("dayNum")){
                found = true;
            }
        }
        if(found == false)
            db.execSQL("ALTER TABLE groups ADD COLUMN dayNum integer DEFAULT 0");

        db.execSQL("delete from groups");

        return true;
    }

    public boolean insertGroups(ArrayList<Group> list) {

        deleteGroups();

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        for (Group g : list) {
            contentValues.put("day", g.getDay());
            contentValues.put("fromtime", g.getFromTime());
            contentValues.put("tomtime", g.getToTime());
            contentValues.put("location", g.getLocation());
            contentValues.put("comment", g.getComment());
            contentValues.put("lang", g.getLang());
            contentValues.put("latitude", g.getLatitude());
            contentValues.put("longitude", g.getLongitude());
            contentValues.put("km", g.getKm());
            contentValues.put("dayNum", g.getDayNum());
            db.insert("groups", null, contentValues);
        }

        return true;
    }

    public boolean insertToken(String token, Date tokendate) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("delete from token");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String date = sdf.format(tokendate);

        ContentValues contentValues = new ContentValues();
        contentValues.put("token", token);
        contentValues.put("tokendate", date);
        db.insert("token", null, contentValues);
        return true;
    }


    public String selectToken() {

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor result = db.rawQuery("select * from token", null);
        String token = "";
        String tokendate = "";
        if (result != null) {

            while (result.moveToNext()) {
                token = result.getString(0);
                tokendate = result.getString(1);
                break;
            }

            if (!result.isClosed()) {
                result.close();
            }
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            Date date = sdf.parse(tokendate);
            Date currentTime = Calendar.getInstance().getTime();
            long mills = date.getTime() - currentTime.getTime();
            int hours = (int) (mills / (1000 * 60 * 60));
            if (hours < 2)
                return token;
            else
                return "";
        } catch (ParseException e) {
            return "";
        }
    }
}
