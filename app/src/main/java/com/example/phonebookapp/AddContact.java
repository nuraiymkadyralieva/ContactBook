package com.example.phonebookapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
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

public class AddContact extends AppCompatActivity {

    private EditText firstNameEdt, lastNameEdt, phoneNumberEdt;
    private Button addContactBtn;
    private ImageView avatarImageView;
    private Uri photoUri;

    private static final int REQUEST_GALLERY = 1;
    private static final int REQUEST_CAMERA = 2;
    private static final int REQUEST_PERMISSION = 100;

    private DbHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        // Инициализация элементов
        firstNameEdt = findViewById(R.id.idEdtFirstName);
        lastNameEdt = findViewById(R.id.idEdtLastName);
        phoneNumberEdt = findViewById(R.id.idEdtPhoneNumber);
        addContactBtn = findViewById(R.id.idBtnAddContact);
        avatarImageView = findViewById(R.id.idImgAvatar); // Аватарка

        dbHandler = new DbHandler(AddContact.this);

        // Нажатие на аватарку для выбора фото
        avatarImageView.setOnClickListener(view -> checkPermissionsAndOpenPicker());

        // Нажатие на кнопку "Добавить контакт"
        addContactBtn.setOnClickListener(v -> saveContact());
    }

    // Проверяем разрешения перед выбором фото
    private void checkPermissionsAndOpenPicker() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_PERMISSION);
                return;
            }
        } else { // Для Android 12 и ниже
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

    // Открываем диалог выбора фото (галерея или камера)
    private void openImagePicker() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent takePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePhoto.resolveActivity(getPackageManager()) != null) {
            File photoFile = createImageFile();
            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", photoFile);
                takePhoto.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            }
        }

        Intent chooser = Intent.createChooser(pickPhoto, "Выберите источник фото");
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{takePhoto});
        startActivityForResult(chooser, REQUEST_GALLERY);
    }

    // Создание временного файла для фото
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

    // Обработка результатов выбора фото
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_GALLERY && data != null) {
                Uri selectedImage = data.getData();
                avatarImageView.setImageURI(selectedImage);
            } else if (requestCode == REQUEST_CAMERA) {
                if (photoUri != null) {
                    avatarImageView.setImageURI(photoUri);
                }
            }
        }
    }

    // Обработка разрешений
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImagePicker();
            } else {
                Toast.makeText(this, "Разрешение отклонено!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Сохранение контакта в базу данных
    private void saveContact() {
        String firstName = firstNameEdt.getText().toString();
        String lastName = lastNameEdt.getText().toString();
        String phoneNumber = phoneNumberEdt.getText().toString();

        if (firstName.isEmpty() || lastName.isEmpty() || phoneNumber.isEmpty()) {
            Toast.makeText(AddContact.this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Добавляем контакт в базу
        dbHandler.addNewContact(firstName, lastName, phoneNumber);

        // Устанавливаем RESULT_OK, чтобы MainActivity узнал об обновлении
        setResult(RESULT_OK);

        Toast.makeText(AddContact.this, "Contact added!", Toast.LENGTH_SHORT).show();
        finish(); // Закрываем AddContact
    }
}
