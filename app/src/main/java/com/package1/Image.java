package com.package1;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


import java.io.IOException;
import java.util.ArrayList;
public class Image extends AppCompatActivity {

    /**
     * L'image que l'on souhaite traiter.
     */
    private Bitmap image;
    /**
     * L'image que l'on souhaite afficher apres application d'une fonction.
     */
    private Bitmap image_copy;
    /**
     * Load l'image que l'on veut.
     */
    private ImageView imgView;
    /**
     * ArrayList de boutons.
     *
     * @see Button
     */
    public ArrayList<Button> buttonList;


    private RS renderScript;


    /**
     * Nombre de bouton sur l'application.
     */
    public static int nbButton = 2;
    private Uri filePath;
    private String imagepath = null;
    private int PICK_IMAGE_REQUEST = 1;

    protected void onCreate(Bundle savedUnstanceState) {
        super.onCreate(savedUnstanceState);
        setContentView(R.layout.image);

        renderScript = new RS(getApplicationContext());
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;

        imgView = findViewById(R.id.imageResult);

        initiateButton();
        addListenerOnButton();

    }

    /**
     * Permet d'initialiser les boutons.
     */
    public void initiateButton() {

        buttonList = new ArrayList<>();
        Button tb;
        for (int i = 0; i < nbButton; i++) {
            tb = findViewById(R.id.button + i);
            buttonList.add(tb);
        }
    }

    /**
     * Permet d'initialiser les actions aux boutons.
     */
    public void addListenerOnButton() {

        if (buttonList != null) {
            buttonList.get(0).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent, 1);
                    imgView.setImageBitmap(image_copy);

                }
            });
            buttonList.get(1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 0);
                }
            });
        }
    }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        // Load photo
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            imagepath = getPath(filePath);

            try {
                image = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //image = rotateBitmap(imagepath);
               /* imgView.setImageBitmap(image);
                image_copy = image.copy(Bitmap.Config.ARGB_8888, true);*/


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Prendre photo
        else {
            image = (Bitmap) data.getExtras().get("data");
        }

        imgView.setImageBitmap(image);
        image_copy = image.copy(Bitmap.Config.ARGB_8888, true);
    }

    /**
     * Permet de recuperer le chemin de l'image.
     *
     * @param uri
     * @return
     */
    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //ajoute les entrées de menu_test à l'ActionBar
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (image_copy != null) {
            switch (item.getItemId()) {
                case R.id.grisAction:
                    renderScript.toGrey(image_copy);
                    imgView.setImageBitmap(image_copy);
                    return true;
                case R.id.colorizeRsAction:
                    //renderScript.colorize(image_copy,120);
                    imgView.setImageBitmap(image_copy);
                    return true;
                case R.id.keepRedAction:

                    //keepRed(image_copy);
                    imgView.setImageBitmap(image_copy);
                    return true;
                case R.id.keepRedAction2:
                    //keepRed2(image_copy);
                    imgView.setImageBitmap(image_copy);
                    return true;
                case R.id.undoAction:
                    undo(image, imgView);
                    image_copy = image.copy(Bitmap.Config.ARGB_8888, true);
                    return true;
                case R.id.invertAction:
                   // renderScript.invert(image_copy);
                    imgView.setImageBitmap(image_copy);
                    return true;
            }
        }
        return super.onOptionsItemSelected(item);
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

    public void undo(Bitmap bmp, ImageView img) {
        img.setImageBitmap(bmp);
    }

}
