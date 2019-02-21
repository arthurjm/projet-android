package com.package1;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.package1.affichage.PhotoRecycler;

import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    /**
     * L'image que l'on souhaite traiter.
     */
    public static Bitmap image_retouche;
    private Uri filePath;
    private String imagepath = null;
    private int PICK_IMAGE_REQUEST = 1;
    ColorManipulation colorManipulation;
    Histogram histogram;
    Convolution rightBlur;
    Convolution convolution;
    Convolution convolutionGauss;

    /**
     * ArrayList de boutons.
     *
     * @see Button
     */
    public ArrayList<Button> buttonList;


    /**
     * Permet d'initialiser les boutons.
     */
    public void initiateButton() {

        buttonList = new ArrayList<>();
        Button tb;
        tb = findViewById(R.id.optionButton);
        buttonList.add(tb);
        tb = findViewById(R.id.imageButton);
        buttonList.add(tb);

        // Test recycler
        tb = findViewById(R.id.thirdButton);
        buttonList.add(tb);

        tb = findViewById(R.id.camera_mainActivity);
        buttonList.add(tb);

    }

    /**
     * Permet d'ajouter des actions à des boutons.
     */
    public void addListenerOnButton() {

        if (buttonList != null) {
            buttonList.get(0).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    image2Page(view);
                }
            });
            buttonList.get(1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imagePage(view);
                }
            });
            buttonList.get(2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    testPage(view);
                }
            });
            buttonList.get(3).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent, 1);

                }
            });
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        // Load photo
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            imagepath = getPath(filePath);

            try {
                image_retouche = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //image = rotateBitmap(imagepath);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    // Deuxieme affichage
    public void image2Page(View view) {
        startActivity(new Intent(this, Test.class));
    }

    // Premier affichage
    public void imagePage(View view) {
        startActivity(new Intent(this, Image.class));
    }

    public void testPage(View view) {
        startActivity(new Intent(this, PhotoRecycler.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("CV", "onCreate()");

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;

        // ANIMATION golri
        /*Animation zoomAnimation = AnimationUtils.loadAnimation(this, R.anim.zoom);
        imgView.startAnimation(zoomAnimation);*/

        // Permet d'initier tout les boutons
        initiateButton();
        addListenerOnButton();

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


