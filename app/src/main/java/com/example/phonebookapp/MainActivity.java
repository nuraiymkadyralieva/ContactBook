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
    private ArrayList<ContactsModel> contactsModelArrayList;
    private Button addContactBtn;
    private SearchView searchView;
    private DbHandler dbHandler;
    private ContactRVAdapter contactRVAdapter;
    private RecyclerView contactsRV;

    private static final int REQUEST_ADD_CONTACT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactsModelArrayList = new ArrayList<>();
        searchView = findViewById(R.id.search_view);
        searchView.setIconified(false);
        searchView.clearFocus();
        addContactBtn = findViewById(R.id.idBtnAddContact);
        contactsRV = findViewById(R.id.idRVContacts);
        dbHandler = new DbHandler(MainActivity.this);

        // Загружаем контакты и устанавливаем адаптер
        loadContacts();
        contactRVAdapter = new ContactRVAdapter(contactsModelArrayList, MainActivity.this);
        contactsRV.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        contactsRV.setAdapter(contactRVAdapter);

        setupSearchView();
        setupAddButton();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADD_CONTACT && resultCode == RESULT_OK) {
            loadContacts(); // Обновляем список контактов
        }
    }

    private void loadContacts() {
        contactsModelArrayList.clear();
        ArrayList<ContactsModel> newContacts = dbHandler.readContacts();
        contactsModelArrayList.addAll(newContacts);

        if (contactRVAdapter == null) {
            contactRVAdapter = new ContactRVAdapter(contactsModelArrayList, MainActivity.this);
            contactsRV.setAdapter(contactRVAdapter);
        } else {
            contactRVAdapter.notifyDataSetChanged();
        }
    }

    private void filterList(String text) {
        ArrayList<ContactsModel> filteredList = new ArrayList<>();
        String query = text.toLowerCase().trim(); // Приводим запрос к нижнему регистру и убираем лишние пробелы

        for (ContactsModel contact : contactsModelArrayList) {
            String firstName = contact.getFirstName().toLowerCase();
            String lastName = contact.getLastName().toLowerCase();
            String phoneNumber = contact.getPhoneNumber().toLowerCase();
            String fullName = firstName + " " + lastName; // Полное ФИО

            // Проверяем совпадение по имени, фамилии, номеру телефона или полному ФИО
            if (firstName.contains(query) ||
                    lastName.contains(query) ||
                    phoneNumber.contains(query) ||
                    fullName.contains(query)) {
                filteredList.add(contact);
            }
        }

        if (filteredList.isEmpty()) {
            Toast.makeText(MainActivity.this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            contactRVAdapter.filterList(filteredList);
        }
    }

    private void setupAddButton() {
        addContactBtn.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, AddContact.class);
            startActivityForResult(i, REQUEST_ADD_CONTACT);
        });
    }

    private void setupSearchView() {
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