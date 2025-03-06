package com.example.phonebookapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UpdateContactActivity extends AppCompatActivity {
    private EditText firstNameEdt, lastNameEdt, phoneNumberEdt;
    private Button updateContactBtn, deleteContactBtn, callBtn, sendMessageBtn;
    private ImageView avatarImageView;
    private DbHandler dbHandler;
    private String firstName, lastName, phoneNumber, photoPath;
    private Uri photoUri;

    private static final int REQUEST_GALLERY = 1;
    private static final int REQUEST_PERMISSION = 100;
    private static final int REQUEST_CALL_PERMISSION = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_contact);

        firstNameEdt = findViewById(R.id.idEdtFirstName);
        lastNameEdt = findViewById(R.id.idEdtLastName);
        phoneNumberEdt = findViewById(R.id.idEdtPhoneNumber);
        updateContactBtn = findViewById(R.id.idBtnUpdateContact);
        deleteContactBtn = findViewById(R.id.idBtnDeleteContact);
        callBtn = findViewById(R.id.idBtnCall);
        sendMessageBtn = findViewById(R.id.idBtnSendMessage);
        avatarImageView = findViewById(R.id.idImgAvatar);

        dbHandler = new DbHandler(UpdateContactActivity.this);

        firstName = getIntent().getStringExtra("firstName");
        lastName = getIntent().getStringExtra("lastName");
        phoneNumber = getIntent().getStringExtra("phoneNumber");
        photoPath = getIntent().getStringExtra("photoPath");

        firstNameEdt.setText(firstName);
        lastNameEdt.setText(lastName);
        phoneNumberEdt.setText(phoneNumber);
        if (photoPath != null && !photoPath.isEmpty()) {
            setCircularImage(Uri.fromFile(new File(photoPath)));
        }

        avatarImageView.setOnClickListener(view -> checkPermissionsAndOpenPicker());

        updateContactBtn.setOnClickListener(v -> {
            String newFirstName = firstNameEdt.getText().toString();
            String newLastName = lastNameEdt.getText().toString();
            String newPhoneNumber = phoneNumberEdt.getText().toString();

            if (newFirstName.isEmpty() || newLastName.isEmpty() || newPhoneNumber.isEmpty()) {
                Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (newPhoneNumber.length() < 3 || newPhoneNumber.length() > 14) {
                Toast.makeText(this, "Phone number should be between 3 and 14 digits!", Toast.LENGTH_SHORT).show();
                return;
            }

            dbHandler.updateContact(firstName, lastName, newFirstName, newLastName, newPhoneNumber, photoPath);
            Toast.makeText(this, "Contact Updated Successfully!", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(UpdateContactActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        });

        deleteContactBtn.setOnClickListener(v -> {
            dbHandler.deleteContact(firstName, lastName, phoneNumber);
            Toast.makeText(this, "Contact Deleted Successfully!", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(UpdateContactActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        });

        // Обработчик кнопки Call
        callBtn.setOnClickListener(v -> {
            String phone = phoneNumberEdt.getText().toString();
            if (phone.isEmpty()) {
                Toast.makeText(this, "Phone number is empty!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
            } else {
                Intent callIntent = new Intent(Intent.ACTION_DIAL); // Используем ACTION_DIAL вместо ACTION_CALL для набора без прямого звонка
                callIntent.setData(Uri.parse("tel:" + phone));
                startActivity(callIntent);
            }
        });

        // Обработчик кнопки Send Message
        sendMessageBtn.setOnClickListener(v -> {
            String phone = phoneNumberEdt.getText().toString();
            if (phone.isEmpty()) {
                Toast.makeText(this, "Phone number is empty!", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent smsIntent = new Intent(Intent.ACTION_VIEW);
            smsIntent.setData(Uri.parse("sms:" + phone));
            startActivity(smsIntent);
        });
    }

    private void checkPermissionsAndOpenPicker() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_PERMISSION);
                return;
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                }, REQUEST_PERMISSION);
                return;
            }
        }
        openImagePicker();
    }

    private void openImagePicker() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent takePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePhoto.resolveActivity(getPackageManager()) != null) {
            File photoFile = createImageFile();
            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", photoFile);
                photoPath = photoFile.getAbsolutePath();
                takePhoto.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            }
        }

        Intent chooser = Intent.createChooser(pickPhoto, "Выберите источник фото");
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{takePhoto});
        startActivityForResult(chooser, REQUEST_GALLERY);
    }

    private File createImageFile() {
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File storageDir = getExternalFilesDir(null);
            return File.createTempFile("IMG_" + timeStamp, ".jpg", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUEST_GALLERY) {
            if (data != null) {
                photoUri = data.getData();
                photoPath = getRealPathFromURI(photoUri);
                setCircularImage(photoUri);
            } else if (photoUri != null) {
                setCircularImage(photoUri);
            }
        }
    }

    private void setCircularImage(Uri uri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            Bitmap circularBitmap = getCircularBitmap(bitmap);
            avatarImageView.setImageBitmap(circularBitmap);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Ошибка при загрузке фото", Toast.LENGTH_SHORT).show();
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

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        android.database.Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(column_index);
        cursor.close();
        return path;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openImagePicker();
        } else if (requestCode == REQUEST_CALL_PERMISSION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            String phone = phoneNumberEdt.getText().toString();
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + phone));
            startActivity(callIntent);
        } else {
            Toast.makeText(this, "Разрешение отклонено!", Toast.LENGTH_SHORT).show();
        }
    }
}