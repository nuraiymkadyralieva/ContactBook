package com.example.phonebookapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ViewContacts extends AppCompatActivity {
    // creating variables for the array list, dbHandler, contactAdapter and recycler view
    private ArrayList<ContactsModal> contactsModalArrayList;
    private DbHandler dbHandler;
    private ContactRVAdapter contactRVAdapter;
    private RecyclerView contactsRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contacts);

        // initializing all variables
        contactsModalArrayList = new ArrayList<>();

        dbHandler = new DbHandler(ViewContacts.this);

        // getting the course array list from the dbHandler class
        contactsModalArrayList = dbHandler.readContacts();

        // passing the array list to the ContactRVAdapter class
        contactRVAdapter = new ContactRVAdapter(contactsModalArrayList, ViewContacts.this);
        contactsRV = findViewById(R.id.idRVContacts);

        // setting layout manager for the recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ViewContacts.this, RecyclerView.VERTICAL, false);

        contactsRV.setLayoutManager(linearLayoutManager);

        // setting the adapter to the recycler view
        contactsRV.setAdapter(contactRVAdapter);
    }
}