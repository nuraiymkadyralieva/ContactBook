package com.example.phonebookapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;
import android.widget.Toast;
import android.widget.Button;
import android.content.Intent;
import android.widget.EditText;

public class UpdateContactActivity extends AppCompatActivity {
    // variables for the editText, update and delete buttons, local strings and dbHandler
    private EditText firstNameEdt, lastNameEdt, phoneNumberEdt;
    private Button updateContactBtn, deleteContactBtn;
    private DbHandler dbHandler;
    String firstName, lastName, phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_contact);

        // initializing all variables
        firstNameEdt = findViewById(R.id.idEdtFirstName);
        lastNameEdt = findViewById(R.id.idEdtLastName);
        phoneNumberEdt = findViewById(R.id.idEdtPhoneNumber);
        updateContactBtn = findViewById(R.id.idBtnUpdateContact);
        deleteContactBtn = findViewById(R.id.idBtnDeleteContact);

        // initializing the dbHandler class
        dbHandler = new DbHandler(UpdateContactActivity.this);

        // getting data which is passed in the ContactRVAdapter
        firstName = getIntent().getStringExtra("firstName");
        lastName = getIntent().getStringExtra("lastName");
        phoneNumber = getIntent().getStringExtra("phoneNumber");

        // setting data to edit text
        firstNameEdt.setText(firstName);
        lastNameEdt.setText(lastName);
        phoneNumberEdt.setText(phoneNumber);

        // adding on click listener to the update contact button
        updateContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // validating if the text fields are empty or not
                if (firstNameEdt.getText().toString().isEmpty() ||
                        lastNameEdt.getText().toString().isEmpty() ||
                        phoneNumberEdt.getText().toString().isEmpty()) {
                    Toast.makeText(UpdateContactActivity.this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // validating the phone number is the right amount of digits
                if (phoneNumberEdt.getText().toString().length() < 3 || phoneNumberEdt.getText().toString().length() > 14) {
                    Toast.makeText(UpdateContactActivity.this, "Phone number should be between 3 and 14 digits!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // calling update contact method from the dbHandler and passing all edited values
                dbHandler.updateContact(firstName,
                                        lastName,
                                        firstNameEdt.getText().toString(),
                                        lastNameEdt.getText().toString(),
                                        phoneNumberEdt.getText().toString());

                // displaying a toast message that the contact has been updated
                Toast.makeText(UpdateContactActivity.this, "Contact Updated Successfully!", Toast.LENGTH_SHORT).show();

                // returning to the all contacts list
                Intent i = new Intent(UpdateContactActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        // adding on click listener to the delete contact button
        deleteContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calling a method from the dbHandler to delete the contact
                dbHandler.deleteContact(firstName, lastName, phoneNumber);

                // displaying a toast message that the contact has been deleted
                Toast.makeText(UpdateContactActivity.this, "Contact Deleted Successfully!", Toast.LENGTH_SHORT).show();

                // returning to the all contacts list
                Intent i = new Intent(UpdateContactActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }
}