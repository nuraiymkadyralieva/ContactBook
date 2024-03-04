package com.example.phonebookapp;

import java.util.ArrayList;

import android.view.View;
import android.content.Intent;
import android.view.ViewGroup;
import android.content.Context;
import android.widget.TextView;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ContactRVAdapter extends RecyclerView.Adapter<ContactRVAdapter.ViewHolder> {
    // variable for the array list and context
    private ArrayList<ContactsModel> contactsModelArrayList;

    private final Context context;

    // constructor
    public ContactRVAdapter(ArrayList<ContactsModel> contactsModelArrayList, Context context) {
        this.contactsModelArrayList = contactsModelArrayList;
        this.context = context;
    }

    // method for filtering the recycler view data
    public void filterList(ArrayList<ContactsModel> filteredList) {
        contactsModelArrayList = filteredList;

        // notify the adapter for a change in the recycler view
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflating the layout file for the recycler view items
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // setting data to the views of recycler view item
        ContactsModel model = contactsModelArrayList.get(position);

        holder.contactFirstName.setText(model.getFirstName());
        holder.contactLastName.setText(model.getLastName());
        holder.contactPhoneNumber.setText(model.getPhoneNumber());

        // add on click listener for the recycler view item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // calling an intent
                Intent i = new Intent(context, UpdateContactActivity.class);

                // passing all values
                i.putExtra("firstName", model.getFirstName());
                i.putExtra("lastName", model.getLastName());
                i.putExtra("phoneNumber", model.getPhoneNumber());

                // starting activity
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        // returning the size of the array list
        return contactsModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for the text views
        private final TextView contactFirstName;
        private final TextView contactLastName;
        private final TextView contactPhoneNumber;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing the text views
            contactFirstName = itemView.findViewById(R.id.idFirstName);
            contactLastName = itemView.findViewById(R.id.idLastName);
            contactPhoneNumber = itemView.findViewById(R.id.idPhoneNumber);
        }
    }
}
