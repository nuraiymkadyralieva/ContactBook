package com.example.phonebookapp;

        import androidx.appcompat.app.AppCompatActivity;

        import android.content.Intent;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;

        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;
        import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    // creating variables for the array list, add contact button, db handler, contact adapter and recycler view
    private ArrayList<ContactsModal> contactsModalArrayList;

    private Button addContactBtn;
    private DbHandler dbHandler;
    private ContactRVAdapter contactRVAdapter;
    private RecyclerView contactsRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initializing all variables
        contactsModalArrayList = new ArrayList<>();

        addContactBtn = findViewById(R.id.idBtnAddContact);

        dbHandler = new DbHandler(MainActivity.this);

        // getting the course array list from the dbHandler class
        contactsModalArrayList = dbHandler.readContacts();

        // passing the array list to the ContactRVAdapter class
        contactRVAdapter = new ContactRVAdapter(contactsModalArrayList, MainActivity.this);
        contactsRV = findViewById(R.id.idRVContacts);

        // setting layout manager for the recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false);

        contactsRV.setLayoutManager(linearLayoutManager);

        // setting the adapter to the recycler view
        contactsRV.setAdapter(contactRVAdapter);

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
}