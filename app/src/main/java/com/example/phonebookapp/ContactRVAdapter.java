package com.example.phonebookapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContactRVAdapter extends RecyclerView.Adapter<ContactRVAdapter.ViewHolder> {
    private ArrayList<ContactsModel> contactsModelArrayList;
    private Context context;

    public ContactRVAdapter(ArrayList<ContactsModel> contactsModelArrayList, Context context) {
        this.contactsModelArrayList = contactsModelArrayList;
        this.context = context;
    }

    public void filterList(ArrayList<ContactsModel> filteredList) {
        contactsModelArrayList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ContactsModel model = contactsModelArrayList.get(position);
        holder.contactFirstName.setText(model.getFirstName());
        holder.contactLastName.setText(model.getLastName());
        holder.contactPhoneNumber.setText(model.getPhoneNumber());

        // Загрузка аватарки
        if (model.getPhotoPath() != null && !model.getPhotoPath().isEmpty()) {
            try {
                Uri uri = Uri.parse(model.getPhotoPath());
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
                Bitmap circularBitmap = getCircularBitmap(bitmap);
                holder.avatarImageView.setImageBitmap(circularBitmap);
            } catch (Exception e) {
                e.printStackTrace();
                holder.avatarImageView.setImageResource(R.drawable.avatar); // Загрузка дефолтной аватарки при ошибке
            }
        } else {
            holder.avatarImageView.setImageResource(R.drawable.avatar);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent i = new Intent(context, UpdateContactActivity.class);
            i.putExtra("firstName", model.getFirstName());
            i.putExtra("lastName", model.getLastName());
            i.putExtra("phoneNumber", model.getPhoneNumber());
            i.putExtra("photoPath", model.getPhotoPath());
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return contactsModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView contactFirstName;
        private TextView contactLastName;
        private TextView contactPhoneNumber;
        private ImageView avatarImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            contactFirstName = itemView.findViewById(R.id.idFirstName);
            contactLastName = itemView.findViewById(R.id.idLastName);
            contactPhoneNumber = itemView.findViewById(R.id.idPhoneNumber);
            avatarImageView = itemView.findViewById(R.id.idImgAvatar); // Предполагаем, что ImageView есть в contact_item.xml
        }
    }

    private Bitmap getCircularBitmap(Bitmap bitmap) {
        int size = Math.min(bitmap.getWidth(), bitmap.getHeight());
        Bitmap output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        android.graphics.Canvas canvas = new android.graphics.Canvas(output);
        android.graphics.Paint paint = new android.graphics.Paint();
        paint.setAntiAlias(true);
        canvas.drawCircle(size / 2f, size / 2f, size / 2f, paint);
        paint.setXfermode(new android.graphics.PorterDuffXfermode(android.graphics.PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, (size - bitmap.getWidth()) / 2f, (size - bitmap.getHeight()) / 2f, paint);
        return output;
    }
}