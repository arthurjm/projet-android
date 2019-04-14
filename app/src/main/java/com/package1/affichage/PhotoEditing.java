package com.package1.affichage;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.package1.pictureManipulation.HistogramManipulation;
import com.package1.R;
import com.package1.pictureManipulation.RS;
import com.package1.affichage.adapter.FilterAdapter;
import com.package1.affichage.adapter.MenuAdapter;
import com.package1.affichage.apply.ApplyMenu;
import com.package1.affichage.type.MenuType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import static android.content.ContentUris.*;
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


    public static HistogramManipulation hist;
    public RecyclerView menuRecyclerView;
    public MenuAdapter menuAdapter;
    public RecyclerView filterRecyclerView;
    public FilterAdapter filterAdapter;
    private ConstraintLayout applyFilterLayout;
    public ApplyMenu applyMenu;
    public SeekBar seekBar1;
    public SeekBar seekBar2;
    public RS renderscript;
    public FaceDetection faceDetection;
    public Button back;
    public boolean nightMode = false;

    private Context context;
    private int adaptedWidth;
    public ImageView animationIV;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apply_filter);

        adaptedWidth = 1500;
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
     * @see PhotoEditing#dayMode
     */
    public void dayMode() {
        menuRecyclerView.setVisibility(View.VISIBLE);
        filterRecyclerView.setVisibility(View.GONE);
        back.setVisibility(View.GONE);
        applyFilterLayout.setBackgroundColor(context.getColor(R.color.whiteNuance));
        applyMenu.getMenuList().clear();
        applyMenu.menuList();
        nightMode = false;
    }

    /**
     * To initiate RecyclerViews
     */
    private void initiateRecyclerView() {
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
    private void initiate() {

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

        animationIV = findViewById(R.id.animationIV);
        animationIV.setVisibility(View.GONE);

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
    private void addListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seekBar1.setVisibility(View.GONE);
                seekBar2.setVisibility(View.GONE);
                menuRecyclerView.setVisibility(View.VISIBLE);
                filterRecyclerView.setVisibility(View.GONE);
                back.setVisibility(View.GONE);
                imageEditingCopy = imageEditing.copy(Bitmap.Config.ARGB_8888, true);
                reset(imageEditing);
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
        Button resetBut = findViewById(R.id.reset);
        resetBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageEditing = Bitmap.createScaledBitmap(image, adaptedWidth, ((image.getHeight() * adaptedWidth) / image.getWidth()), true);
                imageEditingCopy = imageEditing.copy(Bitmap.Config.ARGB_8888, true);
                reset(imageEditing);
                Toast.makeText(context, "Remise à zero", Toast.LENGTH_SHORT).show();
            }
        });
        Button applyBut = findViewById(R.id.apply);
        applyBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageEditing = imageEditingCopy.copy(Bitmap.Config.ARGB_8888, true);
                Toast.makeText(context, "Effet appliqué", Toast.LENGTH_SHORT).show();
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
     * @param bitmap the bitmap that we want to share
     */
    private void share(Bitmap bitmap) {

        String bitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, System.currentTimeMillis() + "", null);
        Uri uri = Uri.parse(bitmapPath);
        Intent share = new Intent(Intent.ACTION_SEND);
        share.putExtra(Intent.EXTRA_STREAM, uri);
        share.setType("image/*");
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(Intent.createChooser(share, "Share image File"));

    }

    /**
     * Open an image in default image viewer app
     *
     * @param path the path of the image
     */
    private void openImage(String path) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(path), "image/*");
        startActivity(intent);
    }

    /**
     * to save the image
     *
     * @param bmp the bitmap that we want to save
     */
    private void saveImageToGallery(Bitmap bmp) {
        File dir = new File(Environment.getExternalStorageDirectory(), "Projet");
        if (dir.exists()) {
            dir.mkdirs();
        }

        final String path = insertImage(getContentResolver(), bmp, System.currentTimeMillis() + "_profile.jpg");
        ConstraintLayout test = findViewById(R.id.applyFilter);
        Snackbar snackbar = Snackbar.make(test, "Image sauvegardée dans la galerie !", Snackbar.LENGTH_LONG).setAction("OPEN", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImage(path);
            }
        });
        snackbar.show();

    }

    /**
     * Storing image to device gallery
     *
     * @param cr the layout
     * @param source the bitmap that we want to save
     * @param title the name of the image
     * @return the path of this bitmap
     */
    private String insertImage(ContentResolver cr, Bitmap source, String title) {

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, title);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, title);
        values.put(MediaStore.Images.Media.DESCRIPTION, (String) null);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        // Add the date meta data to ensure the image is added at the front of the gallery
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());

        Uri url = null;
        String stringUrl = null;    /* value to be returned */

        try {
            url = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            if (source != null) {
                assert url != null;
                try (OutputStream imageOut = cr.openOutputStream(url)) {
                    source.compress(Bitmap.CompressFormat.JPEG, 50, imageOut);
                }

                long id = parseId(url);
                // Wait until MINI_KIND thumbnail is generated.
                Bitmap miniThumb = MediaStore.Images.Thumbnails.getThumbnail(cr, id, MediaStore.Images.Thumbnails.MINI_KIND, null);
                // This is for backward compatibility.
                storeThumbnail(cr, miniThumb, id);
            } else {
                assert url != null;
                cr.delete(url, null, null);
                url = null;
            }
        } catch (Exception e) {
            if (url != null) {
                cr.delete(url, null, null);
                url = null;
            }
        }

        if (url != null) {
            stringUrl = url.toString();
        }

        return stringUrl;
    }


    /**
     * A copy of the Android internals StoreThumbnail method, it used with the insertImage to
     * populate the android.provider.MediaStore.Images.Media#insertImage with all the correct
     * meta data. The StoreThumbnail method is private so it must be duplicated here.
     *
     * @see android.provider.MediaStore.Images.Media (StoreThumbnail private method)
     */
    private void storeThumbnail(ContentResolver cr, Bitmap source, long id) {

        // create the matrix to scale it
        Matrix matrix = new Matrix();

        float scaleX = 50F / source.getWidth();
        float scaleY = 50F / source.getHeight();

        matrix.setScale(scaleX, scaleY);

        Bitmap thumb = Bitmap.createBitmap(source, 0, 0,
                source.getWidth(),
                source.getHeight(), matrix,
                true
        );

        ContentValues values = new ContentValues(4);
        values.put(MediaStore.Images.Thumbnails.KIND, MediaStore.Images.Thumbnails.MICRO_KIND);
        values.put(MediaStore.Images.Thumbnails.IMAGE_ID, (int) id);
        values.put(MediaStore.Images.Thumbnails.HEIGHT, thumb.getHeight());
        values.put(MediaStore.Images.Thumbnails.WIDTH, thumb.getWidth());

        Uri url = cr.insert(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, values);

        try {
            assert url != null;
            OutputStream thumbOut = cr.openOutputStream(url);
            thumb.compress(Bitmap.CompressFormat.JPEG, 100, thumbOut);
            assert thumbOut != null;
            thumbOut.close();
        } catch (FileNotFoundException ignored) {
        } catch (IOException ignored) {
        }
    }

    /**
     * to define the imageView in layout
     *
     * @param bmp the bitmap
     */
    private void reset(Bitmap bmp) {
        imgView.setImageBitmap(bmp);
        resetseekbar();
    }

    /**
     * reset progression seekBar's progression
     *
     * @see SeekBar#setProgress(int)
     */
    private void resetseekbar() {
        seekBar1.setProgress(0);
        seekBar2.setProgress(0);
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
        // reset color at black
        nightMode = false;
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