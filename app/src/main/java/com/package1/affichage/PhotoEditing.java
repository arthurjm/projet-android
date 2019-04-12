package com.package1.affichage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.package1.FaceDetection;
import com.package1.HistogramManipulation;
import com.package1.R;
import com.package1.RS;
import com.package1.affichage.Adapter.FilterAdapter;
import com.package1.affichage.Adapter.MenuAdapter;
import com.package1.affichage.Apply.ApplyMenu;
import com.package1.affichage.Type.MenuType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.package1.MainActivity.image;
import static com.package1.MainActivity.imageEditing;
import static com.package1.MainActivity.imageEditingCopy;
import static com.package1.MainActivity.imgView;

/**
 * @author Mathieu
 * In this class, we can fin different function like share an image, or save it
 * This class coressponding to the layout apply_filter
 */
public class PhotoEditing extends AppCompatActivity {


    public RecyclerView menuRecyclerView;
    public MenuAdapter menuAdapter;
    public RecyclerView filterRecyclerView;
    public FilterAdapter filterAdapter;

    public ConstraintLayout applyFilterLayout;

    public Bitmap actualMiniImage;
    public ApplyMenu applyMenu;
    public SeekBar seekBar1;
    public SeekBar seekBar2;

    public RS renderscript;
    public FaceDetection faceDetection;
    public static HistogramManipulation hist;

    public Button back;
    public boolean nightMode = false;

    private Context context;
    private int adaptedWidth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apply_filter);

        adaptedWidth = 2000;
        initiate();
    }

    /**
     * To change the background color of the layout in Black
     * We set nightMode at true to change Text's color
     *
     * @see PhotoEditing#nightMode
     */
    public void nightMode() {
        menuRecyclerView.setVisibility(View.VISIBLE);
        filterRecyclerView.setVisibility(View.GONE);
        back.setVisibility(View.GONE);
        applyFilterLayout.setBackgroundColor(Color.BLACK);
        applyMenu.getMenuList().clear();
        applyMenu.menuList();
        nightMode = true;
    }

    /**
     * To change the background color of the layout in a nuance of white
     * We set nightMode at false to change Text's color
     *
     * @see PhotoEditing#nightMode
     */
    public void dayMode() {
        menuRecyclerView.setVisibility(View.VISIBLE);
        filterRecyclerView.setVisibility(View.GONE);
        back.setVisibility(View.GONE);
        applyFilterLayout.setBackgroundColor(Integer.parseInt("E3E2E3", 16));
        applyMenu.getMenuList().clear();
        applyMenu.menuList();
        nightMode = false;
    }

    /**
     * To initiate RecyclerViews
     */
    public void initiateRecyclerView() {
        // RecyclerView
        filterRecyclerView = findViewById(R.id.idRecyclerViewHorizontalList);
        filterAdapter = new FilterAdapter(applyMenu.getColorList(), this, MenuType.Nothing);
        LinearLayoutManager filterManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        filterRecyclerView.setLayoutManager(filterManager);
        filterRecyclerView.setAdapter(filterAdapter);

        menuRecyclerView = findViewById(R.id.idMenuViewHorizontalList);
        menuAdapter = new MenuAdapter(applyMenu.getMenuList(), this);
        LinearLayoutManager menuManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        menuRecyclerView.setLayoutManager(menuManager);
        menuRecyclerView.setAdapter(menuAdapter);

        // Visibility of recyclerView
        filterRecyclerView.setVisibility(View.GONE);
        menuRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * To initiate different things like buttons ..
     */
    public void initiate() {

        context = this.getApplicationContext();
        renderscript = new RS(context);
        faceDetection = new FaceDetection(context);
        applyMenu = new ApplyMenu(this, renderscript, faceDetection, hist);
        applyFilterLayout = findViewById(R.id.applyFilter);

        initiateRecyclerView();

        // Image
        imgView = findViewById(R.id.imageResult);
        //allow to change the limit on the picture's resolution, if the picture's width is bigger than the limit,
        // it will stay at the limit value instead, the picture's height will be changed accordingly
        if (image.getWidth() < adaptedWidth) {
            adaptedWidth = image.getWidth();
        }

        imageEditing = Bitmap.createScaledBitmap(image, adaptedWidth, ((image.getHeight() * adaptedWidth) / image.getWidth()), true);
        imageEditingCopy = imageEditing.copy(Bitmap.Config.ARGB_8888, true);
        imgView.setImageBitmap(imageEditing);

        // SeekBar and visibility
        seekBar1 = findViewById(R.id.seekBarRGB);
        seekBar1.setVisibility(View.GONE);
        seekBar2 = findViewById(R.id.seekBarNormal);
        seekBar2.setVisibility(View.GONE);

        // Button
        back = findViewById(R.id.back);
        back.setVisibility(View.GONE);

        addListener();
        // Create menu
        applyMenu.menuList();

    }

    /**
     * To link the corresponding action with each button
     */
    public void addListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seekBar1.setVisibility(View.GONE);
                seekBar2.setVisibility(View.GONE);
                menuRecyclerView.setVisibility(View.VISIBLE);
                filterRecyclerView.setVisibility(View.GONE);
                back.setVisibility(View.GONE);
            }
        });
        Button save = findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageEditing = imageEditingCopy.copy(Bitmap.Config.ARGB_8888, true);
                saveImageToGallery(imageEditing);
            }
        });
        Button undoBut = findViewById(R.id.undo);
        undoBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageEditing = Bitmap.createScaledBitmap(image, adaptedWidth, ((image.getHeight() * adaptedWidth) / image.getWidth()), true);
                imageEditingCopy = imageEditing.copy(Bitmap.Config.ARGB_8888, true);
                undo(imageEditing);
            }
        });
        Button applyBut = findViewById(R.id.apply);
        applyBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageEditing = imageEditingCopy.copy(Bitmap.Config.ARGB_8888, true);
            }
        });
        Button share = findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share(imageEditing);
            }
        });
    }

    /**
     * to share the image in all social's network
     * When we share the image, we save it too
     *
     * @param bitmap
     */
    public void share(Bitmap bitmap) {

        String bitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, System.currentTimeMillis() + "", null);
        Uri uri = Uri.parse(bitmapPath);
        Intent share = new Intent(Intent.ACTION_SEND);
        share.putExtra(Intent.EXTRA_STREAM, uri);
        share.setType("image/*");
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(Intent.createChooser(share, "Share image File"));

    }

    /**
     * to save the image
     *
     * @param bmp
     */
    private void saveImageToGallery(Bitmap bmp) {
        File dir = new File(Environment.getExternalStorageDirectory(), "Projet");
        if (!dir.exists()) {
            dir.mkdir();
        }
        System.out.println(dir);
        final String fileName = System.currentTimeMillis() + "";
        File file = new File(dir, fileName);

        try {
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //add file to gallery
        try {
            MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), fileName, null);
            Toast.makeText(context, "save", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * to define the imageView in layout
     *
     * @param bmp
     */
    public void undo(Bitmap bmp) {
        imgView.setImageBitmap(bmp);
        resetSeekbar();
    }

    /**
     * reset progression seekBar's progression
     *
     * @see SeekBar#setProgress(int)
     */
    public void resetSeekbar() {
        seekBar1.setProgress(0);
        seekBar2.setProgress(0);
    }

    @Override
    /**
     *
     */
    protected void onStart() {
        super.onStart();
        Log.i("CV", "onStart()");
    }

    @Override
    /**
     *
     */
    protected void onResume() {
        super.onResume();
        Log.i("CV", "onResume()");
        // reset color at black
        nightMode = false;
    }

    @Override
    /**
     *
     */
    protected void onPause() {
        super.onPause();
        Log.i("CV", "onPause()");
    }

    @Override
    /**
     *
     */
    protected void onStop() {
        super.onStop();
        Log.i("CV", "onStop()");
    }

    @Override
    /**
     *
     */
    protected void onRestart() {
        super.onRestart();
        Log.i("CV", "onRestart()");
    }

    @Override
    /**
     *
     */
    protected void onDestroy() {
        super.onDestroy();
        Log.i("CV", "onDestroy()");
    }
}