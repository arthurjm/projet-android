package com.package1;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.media.ExifInterface;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.package1.affichage.PhotoEditing;

import java.io.File;
import java.io.IOException;

/**
 * This class corresponding to the layout activity_main
 */
public class MainActivity extends AppCompatActivity {
    /**
     * The original image
     */
    public static Bitmap image;
    /**
     * The image processing
     */
    public static Bitmap imageEditing;
    /**
     * A copy of the image processing
     */
    public static Bitmap imageEditingCopy;
    /**
     * The image displayed on the screen
     */
    @SuppressLint("StaticFieldLeak")
    public static ImageView imgView;
    /**
     * The Number of values that can be taken by the values of the histogram,
     * must be the same value as in the "HistogramManipulation" class
     */
    public static final int NumberofValues = 256;
    private String photoPath;
    private final int PICK_IMAGE_REQUEST = 1;
    private final int TAKE_PHOTO = 1;

    /**
     * @param savedInstanceState Bundle object
     * @see Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("CV", "onCreate()");

        addListenerOnButton();
        permission();
    }

    /**
     * To check permissions
     */
    private void permission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestAlertWindowPermission();
        }

    }

    private void requestAlertWindowPermission() {
        int REQUEST_CODE = 1;
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
    }

    /**
     * To link the corresponding action with each button
     */
    private void addListenerOnButton() {

        Button camera = findViewById(R.id.camera);
        Button gallery = findViewById(R.id.gallery);

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadPhoto();

            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto();
            }
        });

    }

    /**
     * To take a photo with the camera
     */
    private void takePhoto() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intent.resolveActivity(getPackageManager()) != null) {
            // Create unique name
            File photoDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            try {
                File photoFile = File.createTempFile("photo" + System.currentTimeMillis() + "", ".jpg", photoDir);
                // Register complet path
                photoPath = photoFile.getAbsolutePath();
                // create URI
                Uri photoUri;
                photoUri = FileProvider.getUriForFile(getApplicationContext(), "com.package1.fileprovider", photoFile);
                // transfert URI to intent for register photo in temp file
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, TAKE_PHOTO);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * To load a photo in the smartphone
     */
    private void loadPhoto() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (intent.resolveActivity(getPackageManager()) != null) {
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        }

    }

    /**
     * To start an activity
     *
     * @param requestCode the same code in the function "startActivityForResult" to make sure the datas from which Activity
     * @param resultCode  the code returned by the method "setResult()" of Activity
     * @param data        a variable of type data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Uri filePath;
        String imagepath;

        super.onActivityResult(requestCode, resultCode, data);

        // Camera
        if (requestCode == TAKE_PHOTO && resultCode == RESULT_OK) {
            if (photoPath != null) {
                image = rotateBitmap(photoPath);
                filterPage();
            }
        }

        // Load photo
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            imagepath = getPath(filePath);

            try {
                if (filePath != null) {
                    image = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    image = rotateBitmap(imagepath);
                    filterPage();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * To get the path of Uri
     *
     * @param uri Uri object
     * @return a variable of type String
     * @see Uri
     */
    private String getPath(Uri uri) {
        String res = "";
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        assert cursor != null;
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    /**
     * To have a good axe in our photo
     *
     * @param photoFilePath The file's path
     * @return a Bitmap
     */
    private Bitmap rotateBitmap(String photoFilePath) {

        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoFilePath, bounds);

        BitmapFactory.Options opts = new BitmapFactory.Options();
        Bitmap bmp = BitmapFactory.decodeFile(photoFilePath, opts);

        ExifInterface exif = null;
        try {
            exif = new ExifInterface(photoFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert exif != null;
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        Matrix matrix = new Matrix();
        switch (orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.postRotate(90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.postRotate(180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.postRotate(270);
                break;
        }

        return Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
    }

    /**
     * To go on apply_filter layout
     */
    private void filterPage() {
        startActivity(new Intent(this, PhotoEditing.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("CV", "onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("CV", "onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("CV", "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("CV", "onStop()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("CV", "onRestart()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("CV", "onDestroy()");
    }

}


