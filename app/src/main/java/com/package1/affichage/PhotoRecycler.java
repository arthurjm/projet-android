package com.package1.affichage;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.package1.ChanelType;
import com.package1.ColorManipulation;
import com.package1.HistogramManipulation;
import com.package1.R;

import java.util.ArrayList;
import java.util.List;

import static com.package1.MainActivity.imageEditing;
import static com.package1.MainActivity.imageEditingCopy;
import static com.package1.MainActivity.imgView;

public class PhotoRecycler extends AppCompatActivity {

    private List<FilterStruct> photoList = new ArrayList<>();
    private RecyclerView photoRecyclerView;
    private RecyclerViewHorizontalListAdapter photoAdapter;

    public static SeekBar seekBar1;

    private Button undoBut;
    private Button saveBut;

    private ColorManipulation colorManipulation;
    private HistogramManipulation histogramManipulation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_recycle_view);

        initiate();
        addListener();

        if (imageEditing != null) {

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

    public void initiate() {

        // Filter
        colorManipulation = new ColorManipulation();

        // Button
        undoBut = findViewById(R.id.undo);
        saveBut = findViewById(R.id.save);

        // RecyclerVIew
        photoRecyclerView = findViewById(R.id.idRecyclerViewHorizontalList);

        // Image
        imgView = findViewById(R.id.imageResult);
        imageEditingCopy = imageEditing.copy(Bitmap.Config.ARGB_8888, true);

       /* if (imageEditingCopy.getHeight() >= 3500 && imageEditingCopy.getWidth() >= 3500) {
            imageEditingCopy = Bitmap.createScaledBitmap(imageEditingCopy, (int) (imageEditingCopy.getWidth() * 0.8), (int) (imageEditingCopy.getHeight() * 0.8), true);
        }*/
        imageEditingCopy = Bitmap.createScaledBitmap(imageEditing, (int) (imageEditing.getWidth() * 0.4), (int) (imageEditing.getHeight() * 0.4), true);

        imgView.setImageBitmap(imageEditingCopy);

        //Seekbar
        seekBar1 = findViewById(R.id.seekBarFull);

        seekBar1.setVisibility(View.GONE);

    }

    public void addListener() {
        undoBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                undo(imageEditing);
            }
        });
    }

    private void photoList() {

        // On redimensionne l'image
        Bitmap redimensionImageEditing = Bitmap.createScaledBitmap(imageEditingCopy, (int) (imageEditing.getWidth() * 0.1), (int) (imageEditing.getHeight() * 0.1), true);

        FilterStruct fs ;

        fs = new FilterStruct("grey", colorManipulation.convertImageGreyScale(redimensionImageEditing));
        photoList.add(fs);
        fs = new FilterStruct("colorize", colorManipulation.convertImageColorization(redimensionImageEditing, 50));
        photoList.add(fs);
        // A REVOIR AVEC LE RS
        fs = new FilterStruct("keepColor", redimensionImageEditing);
        photoList.add(fs);
        // Constrast setting
        histogramManipulation = new HistogramManipulation(redimensionImageEditing, ChanelType.V);
        histogramManipulation.linearExtensionLUT(198, 50);
        fs = new FilterStruct("contrast", histogramManipulation.applyLUT(redimensionImageEditing));
        photoList.add(fs);
        // ShiftLight
        histogramManipulation = new HistogramManipulation(redimensionImageEditing, ChanelType.V);
        histogramManipulation.shiftLUT(45);
        fs = new FilterStruct("shiftLight", histogramManipulation.applyLUT(redimensionImageEditing));
        photoList.add(fs);
        // ShiftSaturation
        histogramManipulation = new HistogramManipulation(redimensionImageEditing, ChanelType.S);
        histogramManipulation.shiftLUT(45);
        fs = new FilterStruct("shiftSaturation", histogramManipulation.applyLUT(redimensionImageEditing));
        photoList.add(fs);
        // ShiftColor
        histogramManipulation = new HistogramManipulation(redimensionImageEditing, ChanelType.H);
        histogramManipulation.shiftCycleLUT(250);
        fs = new FilterStruct("shiftColor", histogramManipulation.applyLUT(redimensionImageEditing));
        photoList.add(fs);
        // Isohelie
        histogramManipulation = new HistogramManipulation(redimensionImageEditing, ChanelType.H);
        histogramManipulation.isohelieLUT(8);
        fs = new FilterStruct("isohelie", histogramManipulation.applyLUT(redimensionImageEditing));
        photoList.add(fs);
        // EqualizationLight
        histogramManipulation = new HistogramManipulation(redimensionImageEditing, ChanelType.V);
        histogramManipulation.equalizationLUT();
        fs = new FilterStruct("equa light", histogramManipulation.applyLUT(redimensionImageEditing));
        photoList.add(fs);

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