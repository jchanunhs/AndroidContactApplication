package com.example.jchanunh.termproject;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //Database
    public static class MyDBHandler extends SQLiteOpenHelper {

        //Datatype values used for database
        private static final int DATABASE_VERSION = 1;
        private static final String DATABASE_NAME = "Phone.db";
        public static final String ID = "ID";
        public static final String TABLE = "PhoneBook";
        public static final String NAME = "Name";
        public static final String NUMBER = "PhoneNumber";
        public static final String PHONETYPE = "PhoneType";

        public MyDBHandler(Context context) {
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

    //Db handler and array list to store name and number
    MyDBHandler dbHandler ;
    ArrayList<String> phonebook = new ArrayList<>(); //Name
    ArrayList <String> phonenumber = new ArrayList<>(); //Number
    ArrayList <String> textinput = new ArrayList<>(); //Updated listview names
    ArrayList <String> textinputnumber = new ArrayList<>();//Updated listview names
    ListView list;
    public void updateList(ArrayAdapter adapter){
        list.setAdapter(adapter);
    }

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Widgets
        final EditText text = (EditText) findViewById(R.id.textbox);
        ImageView add = (ImageView) findViewById(R.id.add);
        list = (ListView) findViewById(R.id.listview);

        //Cursor calls the getList method to populate listview
        dbHandler = new MyDBHandler(this);
        final ArrayAdapter adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,phonebook);
        Cursor cursor = dbHandler.getList();
        while (cursor.moveToNext()) {
            phonebook.add(cursor.getString(2));
            phonenumber.add(cursor.getString(1));
            updateList(adapter);
        }

        //What happens when customer clicks on the listview
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), DisplayContacts.class);
                i.putExtra("selected_index", position);
                if(textinput.isEmpty()){ //if text input is null, we display all contacts
                    i.putStringArrayListExtra("nameposition", phonebook);
                    i.putStringArrayListExtra("phonenumber", phonenumber);
                }
                else{
                    i.putStringArrayListExtra("nameposition", textinput);
                    i.putStringArrayListExtra("phonenumber", textinputnumber);
                }

                startActivity(i);

            }
        });

        //ImageView programmed as button
        add.setClickable(true);
        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, EditContacts.class));

            }
        });
        final ArrayAdapter textadapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,textinput);
        text.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                String searchKey = s.toString();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textinput.clear();
                textinputnumber.clear();
                String search = s.toString();
                Cursor cursor = dbHandler.readPatternName(search);
                while (cursor.moveToNext()) {
                    textinput.add(cursor.getString(2));
                    textinputnumber.add(cursor.getString(1));
                }
                if(search.isEmpty()){
                    updateList(adapter); //If user inputs nothing in the text, we keep the current list
                }
                else{
                    updateList(textadapter);

                }
            }
        });

    }
}

