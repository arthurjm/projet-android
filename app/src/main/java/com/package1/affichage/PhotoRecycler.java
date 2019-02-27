package com.package1.affichage;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.package1.ChanelType;
import com.package1.ColorManipulation;
import com.package1.CustomImageVIew;
import com.package1.HistogramManipulation;
import com.package1.R;
import com.package1.RS;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.package1.MainActivity.image;
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

    private RS renderscript;
    private ColorManipulation colorManipulation;
    private HistogramManipulation histogramManipulation;

    public static Context context;

    public static Bitmap rediImageEditing;
    public static Bitmap rediCopy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_recycle_view);

        initiate();

        photoAdapter = new RecyclerViewHorizontalListAdapter(photoList, getApplicationContext());
        // Choisir l'orientation de la barre
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(PhotoRecycler.this, LinearLayoutManager.HORIZONTAL, false);
        photoRecyclerView.setLayoutManager(horizontalLayoutManager);
        photoRecyclerView.setAdapter(photoAdapter);

        photoList();


        // Ajout d'une barre entre les items
        //photoRecyclerView.addItemDecoration(new DividerItemDecoration(PhotoRecycler.this, LinearLayoutManager.HORIZONTAL));

    }

    public void initiate() {

        context = getApplicationContext();

        // Filter
        colorManipulation = new ColorManipulation();
        renderscript = new RS(context);

        // Button
        undoBut = findViewById(R.id.undo);
        saveBut = findViewById(R.id.save);

        // RecyclerVIew
        photoRecyclerView = findViewById(R.id.idRecyclerViewHorizontalList);

        // Image
        imgView = findViewById(R.id.imageResult);

        imageEditing = Bitmap.createScaledBitmap(image, (int) (image.getWidth() * 0.5), (int) (image.getHeight() * 0.5), true);
        imageEditingCopy = imageEditing.copy(Bitmap.Config.ARGB_8888, true);

        imgView.setImageBitmap(imageEditing);

        //Seekbar
        seekBar1 = findViewById(R.id.seekBarFull);
        seekBar1.setVisibility(View.GONE);

        addListener();

    }

    public void addListener() {
        undoBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageEditingCopy = imageEditing.copy(Bitmap.Config.ARGB_8888, true);
                undo(imageEditing);
            }
        });
        saveBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveImageToGallery();
            }
        });

    }

    private void photoList() {

        // On redimensionne l'image
        Bitmap rediImageEditing = Bitmap.createScaledBitmap(image, (int) (image.getWidth() * 0.1), (int) (image.getHeight() * 0.1), true);
        Bitmap rediCopy = rediImageEditing.copy(Bitmap.Config.ARGB_8888, true);


        FilterStruct fs;

        // togrey
        fs = new FilterStruct("grey", renderscript.toGrey(rediCopy));
        photoList.add(fs);
        rediCopy = rediImageEditing.copy(Bitmap.Config.ARGB_8888, true);

        // Colorize
        fs = new FilterStruct("colorize", renderscript.colorize(rediCopy, 180));
        photoList.add(fs);
        rediCopy = rediImageEditing.copy(Bitmap.Config.ARGB_8888, true);

        // A REVOIR AVEC LE RS
        fs = new FilterStruct("keepColor", rediCopy);
        photoList.add(fs);

        // Constrast setting
        histogramManipulation = new HistogramManipulation(rediCopy, ChanelType.V);
        histogramManipulation.linearExtensionLUT(198, 50);
        fs = new FilterStruct("contrast", histogramManipulation.applyLUT(rediCopy));
        photoList.add(fs);
        rediCopy = rediImageEditing.copy(Bitmap.Config.ARGB_8888, true);

        // ShiftLight
        histogramManipulation = new HistogramManipulation(rediCopy, ChanelType.V);
        histogramManipulation.shiftLUT(45);
        fs = new FilterStruct("shiftLight", histogramManipulation.applyLUT(rediCopy));
        photoList.add(fs);
        rediCopy = rediImageEditing.copy(Bitmap.Config.ARGB_8888, true);

        // ShiftSaturation
        histogramManipulation = new HistogramManipulation(rediCopy, ChanelType.S);
        histogramManipulation.shiftLUT(45);
        fs = new FilterStruct("shiftSaturation", histogramManipulation.applyLUT(rediCopy));
        photoList.add(fs);
        rediCopy = rediImageEditing.copy(Bitmap.Config.ARGB_8888, true);

        // ShiftColor
        histogramManipulation = new HistogramManipulation(rediCopy, ChanelType.H);
        histogramManipulation.shiftCycleLUT(250);
        fs = new FilterStruct("shiftColor", histogramManipulation.applyLUT(rediCopy));
        photoList.add(fs);
        rediCopy = rediImageEditing.copy(Bitmap.Config.ARGB_8888, true);

        // Isohelie
        histogramManipulation = new HistogramManipulation(rediCopy, ChanelType.H);
        histogramManipulation.isohelieLUT(8);
        fs = new FilterStruct("isohelie", histogramManipulation.applyLUT(rediCopy));
        photoList.add(fs);
        rediCopy = rediImageEditing.copy(Bitmap.Config.ARGB_8888, true);

        // EqualizationLight
        histogramManipulation = new HistogramManipulation(rediCopy, ChanelType.V);
        histogramManipulation.equalizationLUT();
        fs = new FilterStruct("equa light", histogramManipulation.applyLUT(rediCopy));
        photoList.add(fs);

        // ARTHUR
        fs = new FilterStruct("arthur", rediCopy);
        photoList.add(fs);

        photoAdapter.notifyDataSetChanged();

    }

    public void undo(Bitmap bmp) {
        imgView.setImageBitmap(bmp);
    }

    private void saveImageToGallery() {
        //save image
        File dir = new File(Environment.getExternalStorageDirectory(),"image");
        System.out.println(dir);
        if (!dir.exists()) {
            dir.mkdir();
        }
        final String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(dir, fileName);
        //System.out.println(file);
        try {
            FileOutputStream out = new FileOutputStream(file);
            imageEditing.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            Log.i("isSucess", "?");
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println(e);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //add file to gallery
        try {
            MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if(Build.VERSION.SDK_INT< Build.VERSION_CODES.KITKAT) {
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file" + fileName)));
        }
        else{
            MediaScannerConnection.scanFile(this, new String[]{fileName},null,null);
        }
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