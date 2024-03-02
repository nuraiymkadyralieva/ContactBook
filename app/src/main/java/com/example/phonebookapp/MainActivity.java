package com.example.phonebookapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // creating variables for our edittext, button and dbhandler
    private EditText firstNameEdt, lastNameEdt, phoneNumberEdt;
    private Button addContactBtn, readContactsBtn;
    private DbHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initializing all our variables.
        firstNameEdt = findViewById(R.id.idEdtFirstName);
        lastNameEdt = findViewById(R.id.idEdtLastName);
        phoneNumberEdt = findViewById(R.id.idEdtPhoneNumber);
        addContactBtn = findViewById(R.id.idBtnAddContact);
        readContactsBtn = findViewById(R.id.idBtnReadContacts);

        // creating a new dbhandler class
        // and passing our context to it.
        dbHandler = new DbHandler(MainActivity.this);

        // below line is to add on click listener for our add course button.
        addContactBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // below line is to get data from all edit text fields.
                String firstName = firstNameEdt.getText().toString();
                String lastName = lastNameEdt.getText().toString();
                String phoneNumber = phoneNumberEdt.getText().toString();

                // validating if the text fields are empty or not.
                if (firstName.isEmpty() && lastName.isEmpty() && phoneNumber.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter all the data..", Toast.LENGTH_SHORT).show();
                    return;
                }

                // on below line we are calling a method to add new
                // course to sqlite data and pass all our values to it.
                dbHandler.addNewContact(firstName, lastName, phoneNumber);

                // after adding the data we are displaying a toast message.
                Toast.makeText(MainActivity.this, "Contact has been added.", Toast.LENGTH_SHORT).show();
                firstNameEdt.setText("");
                lastNameEdt.setText("");
                phoneNumberEdt.setText("");
            }
        });

        readContactsBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // opening a new activity via a intent.
                Intent i = new Intent(MainActivity.this, ViewContacts.class);
                startActivity(i);
            }
        });
    }
}