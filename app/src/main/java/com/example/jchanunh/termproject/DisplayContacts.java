package com.example.jchanunh.termproject;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class DisplayContacts extends AppCompatActivity {

    DatabaseHandler dbHandler = new DatabaseHandler(this);
    String queryname = " ";
    String querynumber =" ";
    String querytype = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_contacts);


        Bundle extras = getIntent().getExtras();
        ArrayList<String> phonebook = extras.getStringArrayList("nameposition");
        ArrayList<String> phonenumber = extras.getStringArrayList("phonenumber");
        int position = extras.getInt("selected_index");
        final String name = phonebook.get(position);
        final String number = phonenumber.get(position);


        //Takes information passed by Main Activity passes it to readInfo Method to obtain user info
        Cursor cursor = dbHandler.readInfo(number);
        while (cursor.moveToNext()) {
            queryname = cursor.getString(2);
            querynumber = cursor.getString(1);
            querytype = cursor.getString(3);

        }
        //Widgets and setting the textview with the information from database
        TextView displayname = (TextView) findViewById(R.id.name);
        TextView displaynumber = (TextView) findViewById(R.id.number);
        TextView displaytype = (TextView) findViewById(R.id.type);
        displayname.setText(queryname);
        displaynumber.setText(querynumber);
        displaytype.setText(querytype);

        //Button Widgets
        Button edit = (Button)findViewById(R.id.Edit);
        Button remove = (Button)findViewById(R.id.Remove);

        //Edit Button Programmed to pass information to EditContact activity
        edit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), EditContacts.class);
                i.putExtra("name", queryname);
                i.putExtra("number", querynumber);
                i.putExtra("type",querytype);
                startActivity(i);
            }
        });

        //Remove button deletes user information and goes back to main activity
        remove.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dbHandler.deleteInfo(querynumber);
                startActivity(new Intent(DisplayContacts.this, MainActivity.class));
            }
        });

    }
}

