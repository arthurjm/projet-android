package com.package1;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.package1.affichage.PhotoRecycler;

import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    public static Bitmap imageEditing;
    public static Bitmap imageEditingCopy;
    public static ImageView imgView;

    private Uri filePath;
    private String imagepath = null;
    private int PICK_IMAGE_REQUEST = 1;
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
     * Permet d'ajouter des actions à des boutons.
     */
    public void addListenerOnButton() {

        if (buttonList != null) {
            buttonList.get(0).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    filterPage(view);
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


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        // Load photo
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            imagepath = getPath(filePath);

            try {
                imageEditing = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //image = rotateBitmap(imagepath);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            imageEditing = (Bitmap) data.getExtras().get("data");
        }


    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    public void filterPage(View view) {
        startActivity(new Intent(this, PhotoRecycler.class));
    }

    public void infoPage(View view) {
        startActivity(new Intent(this, Info.class));
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


