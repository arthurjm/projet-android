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
import com.package1.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.package1.MainActivity.image_retouche;

public class PhotoRecycler extends AppCompatActivity {

    private List<FilterStruct> photoList = new ArrayList<>();
    private RecyclerView photoRecyclerView;
    private RecyclerViewHorizontalListAdapter photoAdapter;


    /**
     * L'image que l'on souhaite afficher apres application d'une fonction.
     */
    public static Bitmap image_copy;
    /**
     * Load l'image que l'on veut.
     */
    public static ImageView imgView;

    public void initiate() {
        photoRecyclerView = findViewById(R.id.idRecyclerViewHorizontalList);
        imgView = findViewById(R.id.imageResult);

        imgView.setImageBitmap(image_retouche);
        image_copy = image_retouche.copy(Bitmap.Config.ARGB_8888, true);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_recycle_view);

        initiate();

        if (image_retouche != null) {

            photoAdapter = new RecyclerViewHorizontalListAdapter(photoList, getApplicationContext(), image_retouche);
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
        //FilterStruct red = new FilterStruct("red", R.drawable.mer, keepRed2(image_retouche));
        FilterStruct red = new FilterStruct("red", R.drawable.mer, image_retouche);
        FilterStruct grey = new FilterStruct("grey", R.drawable.mer, image_retouche);
        FilterStruct colorize = new FilterStruct("colorize", R.drawable.mer, image_retouche);
        FilterStruct nothing = new FilterStruct("nothing", R.drawable.mer, image_retouche);
        FilterStruct gaussien = new FilterStruct("gaussien", R.drawable.mer, image_retouche);


        photoList.add(red);
        photoList.add(grey);
        photoList.add(colorize);
        photoList.add(nothing);
        photoList.add(gaussien);
        photoAdapter.notifyDataSetChanged();


    }

    public Bitmap keepRed2(Bitmap bmp) {
        int w = bmp.getWidth();
        int h = bmp.getHeight();
        int[] pixels = new int[w * h];
        bmp.getPixels(pixels, 0, w, 0, 0, w, h);

        for (int i = 0; i < h * w; i++) {

            // On prends les references couleurs
            int r = Color.red(pixels[i]);
            int g = Color.green(pixels[i]);
            int b = Color.blue(pixels[i]);

            // Rouge
            if (r < g + b) {
                int gray = (int) Math.round(0.3 * Color.red(pixels[i]) + 0.59 * Color.green(pixels[i]) + 0.11 * Color.blue(pixels[i]));
                pixels[i] = Color.rgb(gray, gray, gray);
            }

        }
        bmp.setPixels(pixels, 0, w, 0, 0, w, h);
        return bmp;
        //imgView.setImageBitmap(bmp);

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