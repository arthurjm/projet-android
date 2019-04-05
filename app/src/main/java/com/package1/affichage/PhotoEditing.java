package com.package1.affichage;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
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

import java.io.ByteArrayOutputStream;
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
 */
public class PhotoEditing extends AppCompatActivity {

    /**
     * MenuRecyclerView of layout
     *
     * @see RecyclerView
     */
    public static RecyclerView menuRecyclerView;
    /**
     * @see MenuAdapter
     */
    public static MenuAdapter menuAdapter;
    public static Bitmap actualMiniImage;
    /**
     * @see ApplyMenu
     */
    public static ApplyMenu applyMenu;

    /**
     * FilterRecyclerView of layout
     *
     * @see RecyclerView
     */
    public static RecyclerView filterRecyclerView;
    /**
     * @see FilterAdapter
     */
    public static FilterAdapter filterAdapter;
    /**
     * SeekBar of layout
     *
     * @see SeekBar
     */
    public static SeekBar seekBar1;
    /**
     * SeekBar of layout
     *
     * @see SeekBar
     */
    public static SeekBar seekBar2;
    /**
     * a variable of type Renderscript
     *
     * @see RS
     */
    public static RS renderscript;
    public static FaceDetection faceDetection;
    public static HistogramManipulation hist;

    public static Context context;
    /**
     * New dimension for the image
     */
    private int adaptedWidth;
    public static Button back;

    @Override
    /**
     *
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_recycle_view);

        initiate();

    }

    /**
     * To initiate buttons seekbars and context in layout
     */
    public void initiate() {

        context = getApplicationContext();
        renderscript = new RS(context);
        faceDetection = new FaceDetection(context);
        applyMenu = new ApplyMenu(context, renderscript, faceDetection, hist);

        // Button
        back = findViewById(R.id.back);
        back.setVisibility(View.GONE);

        // RecyclerView
        filterRecyclerView = findViewById(R.id.idRecyclerViewHorizontalList);

        filterAdapter = new FilterAdapter(applyMenu.colorList, context, MenuType.Nothing);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        filterRecyclerView.setLayoutManager(horizontalLayoutManager);
        filterRecyclerView.setAdapter(filterAdapter);

        menuRecyclerView = findViewById(R.id.idMenuViewHorizontalList);

        menuAdapter = new MenuAdapter(applyMenu.menuList, context);
        LinearLayoutManager horizontalLayoutManager2 = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        menuRecyclerView.setLayoutManager(horizontalLayoutManager2);
        menuRecyclerView.setAdapter(menuAdapter);

        // Visibility of recyclerView
        filterRecyclerView.setVisibility(View.GONE);
        menuRecyclerView.setVisibility(View.VISIBLE);

        // Image
        imgView = findViewById(R.id.imageResult);
        adaptedWidth = 1500;
        if (image.getWidth() < adaptedWidth) {
            adaptedWidth = image.getWidth();
        }
        imageEditing = Bitmap.createScaledBitmap(image, adaptedWidth, (int) ((image.getHeight() * adaptedWidth) / image.getWidth()), true);
        imageEditingCopy = imageEditing.copy(Bitmap.Config.ARGB_8888, true);

        imgView.setImageBitmap(imageEditing);

        // SeekBar and visibility
        seekBar1 = findViewById(R.id.seekBarFull);
        seekBar1.setVisibility(View.GONE);
        seekBar2 = findViewById(R.id.seekBarDemi);
        seekBar2.setVisibility(View.GONE);

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
                imageEditing = Bitmap.createScaledBitmap(image, adaptedWidth, (int) ((image.getHeight() * adaptedWidth) / image.getWidth()), true);
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
     * to share the image
     *
     * @param bitmap
     */
    public void share(Bitmap bitmap) {

        String bitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap,"title", null);
        Uri uri = Uri.parse(bitmapPath);
        Intent share = new Intent(Intent.ACTION_SEND);
        share.putExtra(Intent.EXTRA_STREAM, uri);
        share.setType("image/*");
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(Intent.createChooser(share, "Share image File"));

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
     * reset progression seekbar's progression
     *
     * @see SeekBar#setProgress(int)
     */
    public void resetSeekbar() {
        seekBar1.setProgress(0);
        seekBar2.setProgress(0);
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
        // final String fileName = System.currentTimeMillis() + "";
        final String fileName = "testMATHIEU";
        File file = new File(dir, fileName);

        try {
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
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
            Toast.makeText(context, "save", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        /*if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file" + fileName)));
        } else {
            MediaScannerConnection.scanFile(this, new String[]{fileName}, null, null);
        }*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestAlertWindowPermission();
            }
        }
    }

    private static final int REQUEST_CODE = 1;

    private void requestAlertWindowPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
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