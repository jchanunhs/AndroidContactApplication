package com.example.jchanunh.termproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

    //Datatype values used for database
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Phone.db";
    public static final String ID = "ID";
    public static final String TABLE = "PhoneBook";
    public static final String NAME = "Name";
    public static final String NUMBER = "PhoneNumber";
    public static final String PHONETYPE = "PhoneType";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    //Create Table
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE + "("
                + ID + " INTEGER PRIMARY KEY, "                 //0
                + NUMBER + " TEXT, "                            //1
                + NAME + " TEXT, "                              //2
                + PHONETYPE + " TEXT)";                         //3
        db.execSQL(CREATE_TABLE);
    }

    //Save user information and place to database
    public void saveInfo(String name, String number, String type) {
        String selection = NUMBER + " = ? ";
        String[] selectionArgs = { number };
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE,selection,selectionArgs);
        ContentValues values = new ContentValues();
        values.put(NAME, name);
        values.put(NUMBER, number);
        values.put(PHONETYPE, type);
        db.insert(TABLE, null, values);  // save the new record
        db.close();
    }

    //To get the cursor for populating listview
    public Cursor getList() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor info = db.rawQuery("SELECT * FROM " + TABLE, null);
        return info;
    }

    //Reads database based on phone name
    public Cursor readInfo(String number){
        String query = "Select * FROM " + TABLE + " WHERE "
                + NUMBER + " =  \"" + number + "\"";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor info = db.rawQuery(query,null);
        return info;
    }


    //Deletes row based on specified phone number
    public void deleteInfo(String number){
        String selection = NUMBER + " = ? ";
        String[] selectionArgs = { number };
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE,selection,selectionArgs);
    }

    // For textfield to read pattern
    public Cursor readPatternName(String name) {
        String query = "Select * FROM " + TABLE + " WHERE "
                + NAME + " LIKE  \"" + name+ "%" + "\"";
        SQLiteDatabase db = this.getWritableDatabase();

        StringBuilder s = new StringBuilder();
        Cursor cursor = db.rawQuery(query, null);
        if(name.equals(" ")){
            return(getList()); //If textfield is empty then we display previous listview
        }
        else{
            return cursor;
        }

    }



}