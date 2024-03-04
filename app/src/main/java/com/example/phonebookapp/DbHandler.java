package com.example.phonebookapp;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHandler extends SQLiteOpenHelper {

    // creating a constant variables for the database
    // final = constant
    // database name
    private static final String DB_NAME = "phoneBookDb";

    // database version
    private static final int DB_VERSION = 1;

    // table name
    private static final String TABLE_NAME = "contacts";

    // id column
    private static final String ID_COL = "id";

    // firstName column
    private static final String FIRST_NAME_COL = "firstName";

    // lastName column
    private static final String LAST_NAME_COL = "lastName";

    // phoneNumber column
    private static final String PHONE_NUMBER_COL = "phoneNumber";

    // constructor for the database handler
    public DbHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // method for creating the database by running a sqlite query
    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating an sqlite query and setting our column names along with their data types.
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FIRST_NAME_COL + " TEXT,"
                + LAST_NAME_COL + " TEXT,"
                + PHONE_NUMBER_COL + " TEXT)";

        // calling a exec sql method to execute the sql query
        db.execSQL(query);
    }

    // method to add new contact to the database
    public void addNewContact(String firstName, String lastName, String phoneNumber) {
        // creating a variable for the sqlite database and calling writable method
        // to write data in the database
        SQLiteDatabase db = this.getWritableDatabase();

        // variable for content values
        ContentValues values = new ContentValues();

        // passing all values along with its key and value pair
        values.put(FIRST_NAME_COL, firstName);
        values.put(LAST_NAME_COL, lastName);
        values.put(PHONE_NUMBER_COL, phoneNumber);

        // passing content values to the table
        db.insert(TABLE_NAME, null, values);

        // closing the database after adding the data
        db.close();
    }

    // method for reading all contacts
    public ArrayList<ContactsModel> readContacts()
    {
        // calling getReadableDatabase to get a database to read from
        SQLiteDatabase db = this.getReadableDatabase();

        // creating a cursor with query to read data from the database
        Cursor cursorContacts
                = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        //  creating a new array list
        ArrayList<ContactsModel> contactsModelArrayList
                = new ArrayList<>();

        // moving cursor to first position
        if (cursorContacts.moveToFirst()) {
            do {
                // adding the data from cursor to the array list
                contactsModelArrayList.add(new ContactsModel(
                        cursorContacts.getString(1),
                        cursorContacts.getString(2),
                        cursorContacts.getString(3)));
            } while (cursorContacts.moveToNext());
            // moving the cursor to the next contact
        }

        // closing the cursor and returning the array list
        cursorContacts.close();
        return contactsModelArrayList;
    }

    //method for updating the contacts
    public void updateContact(String originalFirstName,
                              String originalLastName,
                              String firstName,
                              String lastName,
                              String phoneNumber) {

        // calling a method to get writable database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // passing all values along with its key and value pair.
        values.put(FIRST_NAME_COL, firstName);
        values.put(LAST_NAME_COL, lastName);
        values.put(PHONE_NUMBER_COL, phoneNumber);

        // calling an update method to update the database with the passed values
        // and comparing it with firstName and lastName of the contact
        db.update(TABLE_NAME, values, "firstName=? AND lastName=?", new String[]{originalFirstName, originalLastName});
        db.close();
    }

    // method for deleting a contact
    public void deleteContact(String firstName, String lastName, String phoneNumber) {
        // creating a variable a writable database
        SQLiteDatabase db = this.getWritableDatabase();

        // calling a method to delete the contact by comparing its first and last name
        db.delete(TABLE_NAME, "firstName=? AND lastName=? AND phoneNumber=?", new String[]{firstName, lastName, phoneNumber});
        db.close();
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table already exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
