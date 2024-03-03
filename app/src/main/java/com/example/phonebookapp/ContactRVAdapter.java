package com.example.phonebookapp;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ContactRVAdapter extends RecyclerView.Adapter<ContactRVAdapter.ViewHolder>{
    // variable for our array list and context
    private ArrayList<ContactsModal> contactsModalArrayList;
    private Context context;

    // constructor
    public ContactRVAdapter(ArrayList<ContactsModal> contactsModalArrayList, Context context) {
        this.contactsModalArrayList = contactsModalArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflating our layout file for our recycler view items.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // setting data to our views of recycler view item.
        ContactsModal modal = contactsModalArrayList.get(position);
        holder.contactFirstName.setText(modal.getFirstName());
        holder.contactLastName.setText(modal.getLastName());
        holder.contactPhoneNumber.setText(modal.getPhoneNumber());

        // add on click listener for our recycler view item.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // calling an intent.
                Intent i = new Intent(context, UpdateContactActivity.class);

                // passing all our values.
                i.putExtra("firstName", modal.getFirstName());
                i.putExtra("lastName", modal.getLastName());
                i.putExtra("phoneNumber", modal.getPhoneNumber());

                // starting our activity.
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        // returning the size of our array list
        return contactsModalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our text views.
        private TextView contactFirstName, contactLastName, contactPhoneNumber;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views
            contactFirstName = itemView.findViewById(R.id.idFirstName);
            contactLastName = itemView.findViewById(R.id.idLastName);
            contactPhoneNumber = itemView.findViewById(R.id.idPhoneNumber);
        }
    }
}
