package com.example.phonebookapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.ArrayList;

import android.view.View;
import android.widget.Toast;
import android.widget.Button;
import android.content.Intent;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

public class MainActivity extends AppCompatActivity {
    // creating variables for the array list, add contact button, search view, db handler,
    // contact adapter and recycler view
    private ArrayList<ContactsModel> contactsModelArrayList;
    private Button addContactBtn;
    private SearchView searchView;
    private DbHandler dbHandler;
    private ContactRVAdapter contactRVAdapter;
    private RecyclerView contactsRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initializing all variables
        contactsModelArrayList = new ArrayList<>();

        searchView = findViewById(R.id.search_view);

        // setting the search bar to not be iconified and clearing the focus of it
        searchView.setIconified(false);
        searchView.clearFocus();

        addContactBtn = findViewById(R.id.idBtnAddContact);

        contactsRV = findViewById(R.id.idRVContacts);

        dbHandler = new DbHandler(MainActivity.this);

        // getting the contacts array list from the dbHandler class
        contactsModelArrayList = dbHandler.readContacts();

        // passing the array list to the ContactRVAdapter class
        contactRVAdapter = new ContactRVAdapter(contactsModelArrayList, MainActivity.this);

        // setting layout manager for the recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false);

        contactsRV.setLayoutManager(linearLayoutManager);

        // setting the adapter to the recycler view
        contactsRV.setAdapter(contactRVAdapter);

        // adding on text change listener to the search view
        setupSearchView();

        // adding on click listener to the add contact button
        setupAddButton();
    }

    private void filterList(String text) {
        // creating a new array list which will hold the filtered data
        ArrayList<ContactsModel> filteredList = new ArrayList<ContactsModel>();

        // comparing elements
        for (ContactsModel contact : contactsModelArrayList) {
            // checking if the entered string matched with any item of our recycler view.
            if (contact.getFirstName().toLowerCase().contains(text.toLowerCase())) {
                // if the contact is matched it is added it to the filtered list
                filteredList.add(contact);
            }
        }

        if (filteredList.isEmpty()) {
            // if no contacts in the filtered list a toast message is displayed
            Toast.makeText(MainActivity.this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            // passing the filtered list to the adapter class
            contactRVAdapter.filterList(filteredList);
        }
    }

    private void setupAddButton() {
        // adding on click listener to the add contact button
        addContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // returning to the all contacts list
                Intent i = new Intent(MainActivity.this, AddContact.class);
                startActivity(i);
            }
        });
    }

    private void setupSearchView() {
        // adding text listener to the search view
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterList(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return false;
            }
        });
    }
}