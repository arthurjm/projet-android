package com.package1.affichage;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.package1.ColorManipulation;
import com.package1.R;

import java.util.ArrayList;
import java.util.List;

import static com.package1.MainActivity.image_retouche;
import static com.package1.MainActivity.imgView;
import static com.package1.MainActivity.image_retouche_copy;

public class PhotoRecycler extends AppCompatActivity {

    private List<FilterStruct> photoList = new ArrayList<>();
    private RecyclerView photoRecyclerView;
    private RecyclerViewHorizontalListAdapter photoAdapter;

    public static SeekBar seekBar1;
    public static SeekBar seekBar2;

    private Button undoBut;
    private Button saveBut;

    private ColorManipulation test;

    public void initiate() {

        // Button
        undoBut = findViewById(R.id.undo);
        saveBut = findViewById(R.id.save);

        // RecyclerVIew
        photoRecyclerView = findViewById(R.id.idRecyclerViewHorizontalList);

        // Image
        imgView = findViewById(R.id.imageResult);
        image_retouche_copy = image_retouche.copy(Bitmap.Config.ARGB_8888, true);

        if (image_retouche_copy.getHeight() >= 3500 && image_retouche_copy.getWidth() >= 3500) {
            image_retouche_copy = Bitmap.createScaledBitmap(image_retouche_copy, (int) (image_retouche_copy.getWidth() * 0.8), (int) (image_retouche_copy.getHeight() * 0.8), true);
        }

        imgView.setImageBitmap(image_retouche_copy);

        //Seekbar
        seekBar1 = findViewById(R.id.seekBarFull);
        seekBar2 = findViewById(R.id.seekBarDemi);

        seekBar1.setVisibility(View.GONE);
        seekBar2.setVisibility(View.GONE);

    }

    /**
     * Permet d'initialiser les actions aux boutons.
     */
    public void addListener() {

            undoBut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    undo(image_retouche);
                   // image_copy = image_retouche.copy(Bitmap.Config.ARGB_8888, true);
                }
            });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_recycle_view);

        initiate();
        addListener();

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
        Bitmap tes = Bitmap.createScaledBitmap(image_retouche_copy, (int) (image_retouche_copy.getWidth() * 0.2), (int) (image_retouche_copy.getHeight() * 0.2), true);
        test = new ColorManipulation();

        FilterStruct red = new FilterStruct("red", test.convertImageSelectiveDesaturation(tes, Color.RED, 100, 100, 100));
        FilterStruct green = new FilterStruct("green", test.convertImageSelectiveDesaturation(tes, Color.GREEN, 100, 100, 100));
        FilterStruct blue = new FilterStruct("blue", test.convertImageSelectiveDesaturation(tes, Color.BLUE, 150, 150, 150));
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

    public void undo(Bitmap bmp) {
        imgView.setImageBitmap(bmp);
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