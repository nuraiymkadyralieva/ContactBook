package com.example.phonebookapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;
import android.widget.Toast;
import android.widget.Button;
import android.content.Intent;
import android.widget.EditText;

public class AddContact extends AppCompatActivity {

    // creating variables for the editText fields, add button and dbHandler
    private EditText firstNameEdt, lastNameEdt, phoneNumberEdt;
    private Button addContactBtn;
    private DbHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        // initializing all variables
        firstNameEdt = findViewById(R.id.idEdtFirstName);
        lastNameEdt = findViewById(R.id.idEdtLastName);
        phoneNumberEdt = findViewById(R.id.idEdtPhoneNumber);
        addContactBtn = findViewById(R.id.idBtnAddContact);

        // creating a new dbHandler class and passing the context to it
        dbHandler = new DbHandler(AddContact.this);

        // adding on click listener to the add contact button
        addContactBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // getting data from all edit text fields
                String firstName = firstNameEdt.getText().toString();
                String lastName = lastNameEdt.getText().toString();
                String phoneNumber = phoneNumberEdt.getText().toString();

                // validating if the text fields are empty or not
                if (firstName.isEmpty() || lastName.isEmpty() || phoneNumber.isEmpty()) {
                    Toast.makeText(AddContact.this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // validating the phone number is the right amount of digits
                if (phoneNumber.length() < 3 || phoneNumber.length() > 14) {
                    Toast.makeText(AddContact.this, "Phone number should be between 3 and 14 digits!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // calling a method to add new contact to sqlite data and pass all values to it
                dbHandler.addNewContact(firstName, lastName, phoneNumber);

                // displaying a toast message after adding the data
                Toast.makeText(AddContact.this, "Contact has been added.", Toast.LENGTH_SHORT).show();
                firstNameEdt.setText("");
                lastNameEdt.setText("");
                phoneNumberEdt.setText("");

                // returning to the all contacts list
                Intent i = new Intent(AddContact.this, MainActivity.class);
                startActivity(i);
            }
        });
    }
}