package com.package1.affichage;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.package1.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PhotoRecycler extends AppCompatActivity {

    private List<FilterStruct> photoList = new ArrayList<>();
    private RecyclerView photoRecyclerView;
    private RecyclerViewHorizontalListAdapter photoAdapter;

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

    private Button button;
    private Uri filePath;
    private String imagepath = null;
    private int PICK_IMAGE_REQUEST = 1;

    public void initiate() {
        photoRecyclerView = findViewById(R.id.idRecyclerViewHorizontalList);
        // A MODIF
        imgView = findViewById(R.id.imageResult);
        button = findViewById(R.id.buttonList);

    }

    public void addListenerOnButton() {

        if (button != null) {
            button.setOnClickListener(new View.OnClickListener() {
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
                image = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //image = rotateBitmap(imagepath);
                imgView.setImageBitmap(image);
                image_copy = image.copy(Bitmap.Config.ARGB_8888, true);

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_recycle_view);

        initiate();
        addListenerOnButton();

        photoAdapter = new RecyclerViewHorizontalListAdapter(photoList, getApplicationContext(), image);
        // Choisir l'orientation de la barre
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(PhotoRecycler.this, LinearLayoutManager.HORIZONTAL, false);
        photoRecyclerView.setLayoutManager(horizontalLayoutManager);
        photoRecyclerView.setAdapter(photoAdapter);

        // Ajout d'une barre entre les items

        //photoRecyclerView.addItemDecoration(new DividerItemDecoration(PhotoRecycler.this, LinearLayoutManager.HORIZONTAL));

        photoList();
    }

    private void photoList() {
        FilterStruct red = new FilterStruct("red", R.drawable.mer, image);
        FilterStruct grey = new FilterStruct("grey", R.drawable.mer, image);
        FilterStruct colorize = new FilterStruct("colorize", R.drawable.mer, image);
        FilterStruct nothing = new FilterStruct("nothing", R.drawable.mer, image);

            photoList.add(red);
            photoList.add(grey);
            photoList.add(colorize);
            photoList.add(nothing);
            photoAdapter.notifyDataSetChanged();


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