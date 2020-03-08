package com.taxi.friend.drivers.register;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.taxi.friend.drivers.R;
import com.taxi.friend.drivers.TaxiGlobalInfo;
import com.taxi.friend.drivers.utils.PhotoManager;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class BaseRegisterActivity extends AppCompatActivity {

    protected String labelRegisterTitle;
    protected Button btnNext;
    protected ImageView image;
    protected ImageButton btnCamera;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    protected String keyPhoto;
    HashMap<String, RegisterPhotoInfo> photoInfoMap;

    private static final int MY_CAMERA_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_driver_license_front);

        btnNext = findViewById(R.id.btnNext);
        btnNext.setEnabled(false);
        btnNext.setBackgroundResource(R.color.btnDisableColor);
        image = findViewById(R.id.imagePreview);
        btnCamera = findViewById(R.id.btnCamera);

        this.labelRegisterTitle = "Necesitamos una foto";
        photoInfoMap = new HashMap<>();
        photoInfoMap.put(TaxiGlobalInfo.CAR_BACK_PHOTO,
                new RegisterPhotoInfo("Del maletero de tu automovil.", R.drawable.ic_car_register_back));
        photoInfoMap.put(TaxiGlobalInfo.LICENSE_BACK_PHOTO,
                new RegisterPhotoInfo("Reverso de tu Licencia de conducir",  R.drawable.rectangular));
        photoInfoMap.put(TaxiGlobalInfo.CAR_FRONT_PHOTO,
                new RegisterPhotoInfo("Vista frontal de tu automovil", R.drawable.ic_car_register_front));
        photoInfoMap.put(TaxiGlobalInfo.CAR_SIDE_PHOTO,
                new RegisterPhotoInfo("Vista lateral de tu automovil.", R.drawable.ic_car_register_side));
        photoInfoMap.put(TaxiGlobalInfo.LICENSE_FRONT_PHOTO,
                new RegisterPhotoInfo("Licencia de conducir del lado frontal", R.drawable.ic_003_id_card_1));

    }

    protected <T> void initialize(final Class<T> nextClass, final String keyPhoto) {
        this.keyPhoto = keyPhoto;

        RegisterPhotoInfo info = photoInfoMap.get(keyPhoto);
        TextView messagePreview = findViewById(R.id.messageForm);
        ImageView imagePreview = findViewById(R.id.imagePreview);
        messagePreview.setText(info.getMessageForm());
        imagePreview.setImageResource(info.getPhotoPreview());

        if(TaxiGlobalInfo.photoBitmap.containsKey(this.keyPhoto)){
            image.setImageBitmap(TaxiGlobalInfo.photoBitmap.get(this.keyPhoto));
        }

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNextStep(nextClass);
            }
        });
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                dispatchTakePictureIntent(keyPhoto);
            }
        });

        if (checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    MY_CAMERA_REQUEST_CODE);
        }
    }


    public <T> void showNextStep(Class<T> nextClass) {
        Intent intent = new Intent(this, nextClass);
        startActivity(intent);
    }

    protected void dispatchTakePictureIntent(String keyPhoto) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        // Create the File where the photo should go
        File photoFile = null;
        try {
            photoFile = PhotoManager.createImageFile(storageDir);
        } catch (IOException ex) {
            // Error occurred while creating the File
            ex.printStackTrace();
        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            Uri photoURI = Uri.fromFile(photoFile);

            TaxiGlobalInfo.photoMap.put(keyPhoto, photoFile.getPath());
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Bitmap bitmap = getBitMapPhoto(requestCode, resultCode, image, this.keyPhoto);
        TaxiGlobalInfo.photoBitmap.put(this.keyPhoto, bitmap);
        image.setImageBitmap(bitmap);
        btnNext.setEnabled(true);
        btnNext.setBackgroundResource(R.color.btnEnableColor);

    }

    protected Bitmap getBitMapPhoto(int requestCode, int resultCode, ImageView image, String keyPhoto){
        Bitmap result = null;
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            String photoPath = TaxiGlobalInfo.photoMap.get(keyPhoto);

            Bitmap bitmap = PhotoManager.setPic(image, photoPath);

            int orientation = PhotoManager.getOrientationPhoto(photoPath);

            result = PhotoManager.rotateImage(bitmap, orientation);
        }

        return result;
    }
}
