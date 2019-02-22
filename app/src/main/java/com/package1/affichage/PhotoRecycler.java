package com.package1.affichage;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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

import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.package1.ColorManipulation;
import com.package1.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.package1.MainActivity.image_retouche;

public class PhotoRecycler extends AppCompatActivity {

    private List<FilterStruct> photoList = new ArrayList<>();
    private RecyclerView photoRecyclerView;
    private RecyclerViewHorizontalListAdapter photoAdapter;


    private ColorManipulation test;

    public static Bitmap image_copy;

    public static ImageView imgView;

    public void initiate() {
        photoRecyclerView = findViewById(R.id.idRecyclerViewHorizontalList);
        imgView = findViewById(R.id.imageResult);

        if(image_retouche.getHeight() >= 3500 && image_retouche.getWidth() >= 3500){
            image_retouche = Bitmap.createScaledBitmap(image_retouche, (int) (image_copy.getWidth() * 0.001), (int) (image_copy.getHeight() * 0.001), true);
        }

        imgView.setImageBitmap(image_retouche);
        image_copy = image_retouche.copy(Bitmap.Config.ARGB_8888, true);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_recycle_view);

        initiate();

        if (image_retouche != null) {

            photoAdapter = new RecyclerViewHorizontalListAdapter(photoList, getApplicationContext());
            // Choisir l'orientation de la barre
            LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(PhotoRecycler.this, LinearLayoutManager.HORIZONTAL, false);
            photoRecyclerView.setLayoutManager(horizontalLayoutManager);
            photoRecyclerView.setAdapter(photoAdapter);

            photoList();
        }

        // Ajout d'une barre entre les items

        //photoRecyclerView.addItemDecoration(new DividerItemDecoration(PhotoRecycler.this, LinearLayoutManager.HORIZONTAL));

    }

    private void photoList() {

        // A REVOIR
        // On redimensionne l'image
        Bitmap tes = Bitmap.createScaledBitmap(image_copy, (int) (image_copy.getWidth() * 0.2), (int) (image_copy.getHeight() * 0.2 ), true);
        test = new ColorManipulation();
        FilterStruct red = new FilterStruct("red", test.convertImageSelectiveDesaturation(tes, Color.RED, 100, 100, 100));
        FilterStruct green = new FilterStruct("green", test.convertImageSelectiveDesaturation(tes, Color.GREEN, 100, 100, 100));
        FilterStruct blue = new FilterStruct("blue",  test.convertImageSelectiveDesaturation(tes, Color.BLUE, 150, 150, 150));
        FilterStruct grey = new FilterStruct("grey", test.convertImageGreyScale(tes));
        FilterStruct colorize = new FilterStruct("colorize", image_retouche);
        FilterStruct nothing = new FilterStruct("nothing", image_retouche);
        FilterStruct gaussien = new FilterStruct("gaussien", image_retouche);


        photoList.add(red);
        photoList.add(green);
        photoList.add(blue);
        photoList.add(grey);
        photoList.add(colorize);
        photoList.add(nothing);
        photoList.add(gaussien);
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