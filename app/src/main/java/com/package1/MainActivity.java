package com.package1;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.package1.affichage.PhotoRecycler;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


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
    public static ImageView imgView;
    /**
     *
     */
    private Uri filePath;
    /**
     *
     */
    private String imagepath = null;
    private String photoPath;
    /**
     * We set the code "PICK_IMAGE_REQUEST" as 1
     */
    private int PICK_IMAGE_REQUEST = 1;
    private static final int RETOUR_PRENDRE_PHOTO = 1;
    private int REQUEST_IMAGE_CAPTURE = 1;
    /**
     * Access to gallery
     */
    private Button gallery;
    /**
     * Access to camera
     */
    private Button camera;
    /**
     * @// TODO: 08/03/19 CHANGE THIS
     * Not permanent
     * Access to apply filter
     */
    private Button apply;


    /**
     * To set each button on the screen
     */
    public void initiateButton() {

        gallery = findViewById(R.id.gallery);
        camera  =findViewById(R.id.camera);
        apply = findViewById(R.id.thirdButton);

    }

    /**
     * To link the corresponding action with each button
     */
    public void addListenerOnButton() {

        if (gallery != null && camera != null) {
            apply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (image != null) {
                        filterPage(view);
                    } else {
                        Toast.makeText(getApplicationContext(), "You need to choose a picture", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            gallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent, 1);

                }
            });
            camera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //takePhoto();
                   // dispatchTakePictureIntent();
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 0);
                }
            });
        }
    }

    public void takePhoto() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intent.resolveActivity(getPackageManager()) != null){
            // Create unique name

            String time = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File photoDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            try{
                File photoFile = File.createTempFile("photo" + time,".jpg" , photoDir);
                // Register complet path
                photoPath = photoFile.getAbsolutePath();
                // create URI
                Uri photoUri;
                photoUri = FileProvider.getUriForFile(MainActivity.this,
                        MainActivity.this.getApplicationContext().getPackageName()+".provider",
                        photoFile);

                // transfert URI to intent for register photo in temp file
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, RETOUR_PRENDRE_PHOTO);
            } catch (IOException e){
                e.printStackTrace();
            }

        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        photoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.package1.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }
    /**
     * @param requestCode the same code in the foction "startActivityForResult" to make sure the datas from which Activity
     * @param resultCode  the code returned by the methode "setResult()" of Activity
     * @param data        a variable of type data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        // TEST

        if (requestCode == RETOUR_PRENDRE_PHOTO && resultCode == RESULT_OK){

            image = BitmapFactory.decodeFile(photoPath);
            imgView.setImageBitmap(image);

        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            image = (Bitmap) extras.get("data");

        }


        // Load photo
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            imagepath = getPath(filePath);

            try {
                image = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //image = rotateBitmap(imagepath);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            image = (Bitmap) data.getExtras().get("data");
        }



    }

    /**
     * To get the path of Uri
     *
     * @param uri
     * @return a variable of type String
     */
    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    /**
     * To rotate the image
     *
     * @param photoFilePath
     * @return a vaibale of type Bitmap
     */
    public Bitmap rotateBitmap(String photoFilePath) {

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

        Bitmap rotatedBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);

        return rotatedBitmap;
    }

    /**
     * @param view
     */
    public void filterPage(View view) {
        startActivity(new Intent(this, PhotoRecycler.class));
    }

    /**
     * @param view
     */
    public void infoPage(View view) {
        startActivity(new Intent(this, Info.class));
    }

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("CV", "onCreate()");

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;

        initiateButton();
        addListenerOnButton();

    }

    /**
     *
     */
    @Override
    protected void onStart() {
        super.onStart();
        Log.i("CV", "onStart()");
    }

    /**
     *
     */
    @Override
    protected void onResume() {
        super.onResume();
        Log.i("CV", "onResume()");
    }

    /**
     *
     */
    @Override
    protected void onPause() {
        super.onPause();
        Log.i("CV", "onPause()");
    }

    /**
     *
     */
    @Override
    protected void onStop() {
        super.onStop();
        Log.i("CV", "onStop()");
    }

    /**
     *
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("CV", "onRestart()");
    }

    /**
     *
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("CV", "onDestroy()");
    }


}


