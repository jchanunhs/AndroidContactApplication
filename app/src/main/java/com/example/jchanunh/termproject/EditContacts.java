package com.example.jchanunh.termproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class EditContacts extends AppCompatActivity {
    DatabaseHandler dbHandler = new DatabaseHandler(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contacts);

        //Widget Displays
        final EditText name = (EditText) findViewById(R.id.name);
        final EditText number = (EditText) findViewById(R.id.number);
        final Spinner spinner = (Spinner) findViewById(R.id.type);

        //Check to see if there's any values passed by Display Contact Activity
        Button save = (Button) findViewById(R.id.Finish);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String nameextra = extras.getString("name");
            String numberextras = extras.getString("number");
            String spinnername = extras.getString("type");
            if(spinnername.equals("Home")){ //set spinner position based on the type
                spinner.setSelection(0);
            }
            else if(spinnername.equals("Mobile")){
                spinner.setSelection(1);
            }
            else if (spinnername.equals("Work")){
                spinner.setSelection(2);
            }
            name.setText(nameextra);
            number.setText(numberextras);
        }


        //Save button function
        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String Name = name.getText().toString();
                String Num = number.getText().toString();
                String Type = spinner.getSelectedItem().toString();
                dbHandler.saveInfo(Name,Num,Type);
                startActivity(new Intent(EditContacts.this, MainActivity.class));

            }
        });

    }
}
