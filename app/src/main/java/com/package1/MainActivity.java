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
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.package1.affichage.PhotoRecycler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


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
     *The image displayed on the screen
     */
    public static  ImageView imgView;
    /**
     *
     */
    private Uri filePath;
    /**
     *
     */
    private String imagepath = null;
    /**
     * We set the code "PICK_IMAGE_REQUEST" as 1
     */
    private int PICK_IMAGE_REQUEST = 1;

    /**
     * An arraylist of buttons.
     * @see Button
     */
    public ArrayList<Button> buttonList;


    /**
     * To set each button on the screen
     */
    public void initiateButton() {

        buttonList = new ArrayList<>();
        Button tb;

        // Test recycler
        tb = findViewById(R.id.thirdButton);
        buttonList.add(tb);

        tb = findViewById(R.id.gallery_mainActivity);
        buttonList.add(tb);

        tb = findViewById(R.id.camera_mainActivity);
        buttonList.add(tb);

        tb = findViewById(R.id.information);
        buttonList.add(tb);

    }

    /**
     * To link the corresponding action with each button
     */
    public void addListenerOnButton() {

        if (buttonList != null) {
            buttonList.get(0).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(image != null){
                        filterPage(view);
                    }
                }
            });
            buttonList.get(1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent, 1);

                }
            });
            buttonList.get(2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 0);
                }
            });
            buttonList.get(3).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    infoPage(view);
                }
            });
        }
    }

    /**
     *
     * @param requestCode the same code in the foction "startActivityForResult" to make sure the datas from which Activity
     * @param resultCode the code returned by the methode "setResult()" of Activity
     * @param data a variable of type data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

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
        }
        else {
            image = (Bitmap) data.getExtras().get("data");
        }

    }

    /**
     * To get the path of Uri
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
     *
     * @param view
     */
    public void filterPage(View view) {
        startActivity(new Intent(this, PhotoRecycler.class));
    }

    /**
     *
     * @param view
     */
    public void infoPage(View view) {
        startActivity(new Intent(this, Info.class));
    }

    /**
     *
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


