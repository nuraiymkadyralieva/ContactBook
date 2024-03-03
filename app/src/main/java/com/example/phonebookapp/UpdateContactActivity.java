package com.example.phonebookapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateContactActivity extends AppCompatActivity {
    // variables for the editText, update button, strings and dbHandler
    private EditText firstNameEdt, lastNameEdt, phoneNumberEdt;
    private Button updateContactBtn;
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

                // calling update contact method from the dbHandler and passing all edited values
                dbHandler.updateContact(firstName,
                                        lastName,
                                        firstNameEdt.getText().toString(),
                                        lastNameEdt.getText().toString(),
                                        phoneNumberEdt.getText().toString());

                // displaying a toast message that the contact has been updated
                Toast.makeText(UpdateContactActivity.this, "Contact Updated Successfully!", Toast.LENGTH_SHORT).show();

                // returning to the all contacts list
                Intent i = new Intent(UpdateContactActivity.this, ViewContacts.class);
                startActivity(i);
            }
        });
    }
}