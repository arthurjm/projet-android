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
import android.widget.Toast;

import com.package1.ChanelType;
import com.package1.HistogramManipulation;
import com.package1.Mask.BlurMask;
import com.package1.Mask.GaussianBlur;
import com.package1.Mask.LaplacienMask;
import com.package1.Mask.SobelMask;
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

/**
 * @author Mathieu
 */
public class PhotoRecycler extends AppCompatActivity {

    /**
     * List of FilterStruct
     *
     * @see FilterStruct
     */
    private List<FilterStruct> photoList = new ArrayList<>();
    /**
     * RecyclerView of layout
     *
     * @see RecyclerView
     */
    private RecyclerView photoRecyclerView;
    /**
     * @see RecyclerViewHorizontalListAdapter
     */
    private RecyclerViewHorizontalListAdapter photoAdapter;
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
     * Button to back
     *
     * @see Button
     */
    private Button undoBut;
    /**
     * Button to save
     *
     * @see Button
     */
    private Button saveBut;
    /**
     * Button to apply a script
     *
     * @see Button
     */
    private Button applyBut;
    /**
     * a variable of type Renderscript
     *
     * @see RS
     */
    public static RS renderscript;
    /**
     * a variable of type HistogramManipulation
     *
     * @see HistogramManipulation
     */
    public static HistogramManipulation hist;
    /**
     * a variable of type Context
     *
     * @see Context
     */
    private Context context;
    /**
     * New dimension for the image
     */
    private int adaptedWidth;

    @Override
    /**
     *
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_recycle_view);

        initiate();

        photoAdapter = new RecyclerViewHorizontalListAdapter(photoList, getApplicationContext());
        // Choisir l'orientation de la barre
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(PhotoRecycler.this, LinearLayoutManager.HORIZONTAL, false);
        photoRecyclerView.setLayoutManager(horizontalLayoutManager);
        photoRecyclerView.setAdapter(photoAdapter);

        filterList();

        // Ajout d'une barre entre les items
        //photoRecyclerView.addItemDecoration(new DividerItemDecoration(PhotoRecycler.this, LinearLayoutManager.HORIZONTAL));

    }

    /**
     * To initiate buttons seekbars and  context in layout
     */
    public void initiate() {

        context = getApplicationContext();

        // Filter
        renderscript = new RS(context);

        // Button
        undoBut = findViewById(R.id.undo);
        saveBut = findViewById(R.id.save);
        applyBut = findViewById(R.id.apply);

        // RecyclerVIew
        photoRecyclerView = findViewById(R.id.idRecyclerViewHorizontalList);

        // Image
        imgView = findViewById(R.id.imageResult);

        adaptedWidth = 1500;
        if (image.getWidth() < adaptedWidth) {
            adaptedWidth = image.getWidth();
        }
        imageEditing = Bitmap.createScaledBitmap(image, adaptedWidth, (int) ((image.getHeight() * adaptedWidth) / image.getWidth()), true);
        imageEditingCopy = imageEditing.copy(Bitmap.Config.ARGB_8888, true);

        imgView.setImageBitmap(imageEditing);

        // SeekBar
        seekBar1 = findViewById(R.id.seekBarFull);
        seekBar1.setVisibility(View.GONE);
        seekBar2 = findViewById(R.id.seekBarDemi);
        seekBar2.setVisibility(View.GONE);

        addListener();

    }

    /**
     * To link the corresponding action with each button
     */
    public void addListener() {
        undoBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageEditing = Bitmap.createScaledBitmap(image, adaptedWidth, (int) ((image.getHeight() * adaptedWidth) / image.getWidth()), true);
                imageEditingCopy = imageEditing.copy(Bitmap.Config.ARGB_8888, true);
                undo(imageEditing);
            }
        });
        saveBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageEditing = imageEditingCopy.copy(Bitmap.Config.ARGB_8888, true);
                saveImageToGallery(imageEditing);
            }
        });
        applyBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageEditing = imageEditingCopy.copy(Bitmap.Config.ARGB_8888, true);
            }
        });

    }

    /**
     * To initiate photoList
     *
     * @see PhotoRecycler#photoList
     */
    private void filterList() {

        // On redimensionne l'image
        Bitmap rediImageEditing = Bitmap.createScaledBitmap(image, 100, (int) ((image.getHeight() * 100) / image.getWidth()), true);
        Bitmap rediCopy;

        FilterStruct fs;

        // ToGrey
        rediCopy = rediImageEditing.copy(Bitmap.Config.ARGB_8888, true);
        fs = new FilterStruct("Grey", renderscript.toGrey(rediCopy));
        photoList.add(fs);

        // Colorize
        rediCopy = rediImageEditing.copy(Bitmap.Config.ARGB_8888, true);
        fs = new FilterStruct("Colorize", renderscript.colorize(rediCopy, 180));
        photoList.add(fs);

        // KeepColor
        rediCopy = rediImageEditing.copy(Bitmap.Config.ARGB_8888, true);
        fs = new FilterStruct("Keep Hue", renderscript.keepHue(rediCopy, 180, 60));
        photoList.add(fs);

        // Constrast setting
        rediCopy = rediImageEditing.copy(Bitmap.Config.ARGB_8888, true);
        hist = new HistogramManipulation(rediCopy, ChanelType.V);
        hist.linearExtensionLUT(198, 50);
        //fs = new FilterStruct("contrast", hist.applyLUT(rediCopy));
        fs = new FilterStruct("contrast", renderscript.applyLUT(rediCopy, hist));
        photoList.add(fs);

        // ShiftLight
        rediCopy = rediImageEditing.copy(Bitmap.Config.ARGB_8888, true);
        hist = new HistogramManipulation(rediCopy, ChanelType.V);
        hist.shiftLUT(45);
        //fs = new FilterStruct("shiftLight", hist.applyLUT(rediCopy));
        fs = new FilterStruct("shiftLight", renderscript.applyLUT(rediCopy, hist));
        photoList.add(fs);

        // ShiftSaturation
        rediCopy = rediImageEditing.copy(Bitmap.Config.ARGB_8888, true);
        hist = new HistogramManipulation(rediCopy, ChanelType.S);
        hist.shiftLUT(45);
        //fs = new FilterStruct("shiftSaturation", hist.applyLUT(rediCopy));
        fs = new FilterStruct("shiftSaturation", renderscript.applyLUT(rediCopy, hist));
        photoList.add(fs);

        // ShiftColor
        rediCopy = rediImageEditing.copy(Bitmap.Config.ARGB_8888, true);
        hist = new HistogramManipulation(rediCopy, ChanelType.H);
        hist.shiftCycleLUT(120);
        //fs = new FilterStruct("shiftColor", hist.applyLUT(rediCopy));
        fs = new FilterStruct("shiftColor", renderscript.applyLUT(rediCopy, hist));
        photoList.add(fs);

        // Isohelie
        rediCopy = rediImageEditing.copy(Bitmap.Config.ARGB_8888, true);
        hist = new HistogramManipulation(rediCopy, ChanelType.V);
        hist.isohelieLUT(4);
        //fs = new FilterStruct("isohelie", hist.applyLUT(rediCopy));
        fs = new FilterStruct("isohelie", renderscript.applyLUT(rediCopy, hist));
        photoList.add(fs);

        // EqualizationLight
        rediCopy = rediImageEditing.copy(Bitmap.Config.ARGB_8888, true);
        hist = new HistogramManipulation(rediCopy, ChanelType.V);
        hist.equalizationLUT();
        //fs = new FilterStruct("equa light", hist.applyLUT(rediCopy));
        fs = new FilterStruct("equa light", renderscript.applyLUT(rediCopy, hist));
        photoList.add(fs);

        // Blur
        rediCopy = rediImageEditing.copy(Bitmap.Config.ARGB_8888, true);
        BlurMask mask = new BlurMask(9);
        fs = new FilterStruct("Blur", renderscript.convolution(rediCopy, mask));
        photoList.add(fs);

        // Gaussian
        rediCopy = rediImageEditing.copy(Bitmap.Config.ARGB_8888, true);
        GaussianBlur maskGaussian = new GaussianBlur(5, 2.5);
        fs = new FilterStruct("gaussian", renderscript.convolution(rediCopy, maskGaussian));
        photoList.add(fs);

        // Laplacien
        rediCopy = rediImageEditing.copy(Bitmap.Config.ARGB_8888, true);
        LaplacienMask maskLaplacien = new LaplacienMask();
        fs = new FilterStruct("laplacien", renderscript.convolution(rediCopy, maskLaplacien));
        photoList.add(fs);

        // Sobel vertical
        rediCopy = rediImageEditing.copy(Bitmap.Config.ARGB_8888, true);
        SobelMask sobelMaskVertical = new SobelMask(true);
        fs = new FilterStruct("Sobel V", renderscript.convolution(rediCopy, sobelMaskVertical));
        photoList.add(fs);

        // Sobel horizontal
        rediCopy = rediImageEditing.copy(Bitmap.Config.ARGB_8888, true);
        SobelMask sobelMaskHorizontal = new SobelMask(false);
        fs = new FilterStruct("Sobel H", renderscript.convolution(rediCopy, sobelMaskHorizontal));
        photoList.add(fs);

        // Invert
        rediCopy = rediImageEditing.copy(Bitmap.Config.ARGB_8888, true);
        fs = new FilterStruct("Invert", renderscript.invert(rediCopy));
        photoList.add(fs);

        photoAdapter.notifyDataSetChanged();

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
        File dir = new File(Environment.getExternalStorageDirectory(), "image");
        if (!dir.exists()) {
            dir.mkdir();
        }
        final String fileName = System.currentTimeMillis() + "";
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
            Toast.makeText(context,"save", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file" + fileName)));
        } else {
            MediaScannerConnection.scanFile(this, new String[]{fileName}, null, null);
        }
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