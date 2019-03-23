package com.package1.affichage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.package1.FaceDetection;
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
public class    PhotoRecycler extends AppCompatActivity {

    private List<MenuStruct> menuList = new ArrayList<>();
    public static RecyclerView menuRecyclerView;
    private MenuRecyclerAdapter menuAdapter;

    public static Bitmap actualMiniImage;

    /**
     * List of FilterStruct
     *
     * @see FilterStruct
     */
    public static List<FilterStruct> colorList = new ArrayList<>();
    public static List<FilterStruct> extraList = new ArrayList<>();
    public static List<FilterStruct> maskList = new ArrayList<>();
    public static List<FilterStruct> contrastList = new ArrayList<>();

    /**
     * RecyclerView of layout
     *
     * @see RecyclerView
     */
    public static RecyclerView photoRecyclerView;
    /**
     * @see RecyclerViewHorizontalListAdapter
     */
    public static RecyclerViewHorizontalListAdapter photoAdapter;
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
        faceDetection = new FaceDetection(context);

        // Button
        back = findViewById(R.id.back);
        back.setVisibility(View.GONE);

        // RecyclerView
        photoRecyclerView = findViewById(R.id.idRecyclerViewHorizontalList);

        photoAdapter = new RecyclerViewHorizontalListAdapter(colorList, context, RecyclerType.Nothing);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        photoRecyclerView.setLayoutManager(horizontalLayoutManager);
        photoRecyclerView.setAdapter(photoAdapter);

        // Menu RecyclerView
        menuRecyclerView = findViewById(R.id.idMenuViewHorizontalList);

        menuAdapter = new MenuRecyclerAdapter(menuList, context);
        LinearLayoutManager horizontalLayoutManager2 = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        menuRecyclerView.setLayoutManager(horizontalLayoutManager2);
        menuRecyclerView.setAdapter(menuAdapter);

        photoRecyclerView.setVisibility(View.GONE);
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

        // SeekBar
        seekBar1 = findViewById(R.id.seekBarFull);
        seekBar1.setVisibility(View.GONE);
        seekBar2 = findViewById(R.id.seekBarDemi);
        seekBar2.setVisibility(View.GONE);

        addListener();
        menuList();

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
                photoRecyclerView.setVisibility(View.GONE);
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

    }

    /**
     * To change the actual list in RecyclerView
     *
     * @param rt
     */
    public static void changeList(RecyclerType rt) {
        switch (rt) {
            case Color:
                back.setVisibility(View.VISIBLE);
                colorList();
                break;
            case Contrast:
                back.setVisibility(View.VISIBLE);
                saturationList();
                break;
            case Mask:
                back.setVisibility(View.VISIBLE);
                maskList();
                break;
            case Extras:
                back.setVisibility(View.VISIBLE);
                extrasList();
                break;
            default:
                break;

        }
    }

    /**
     * Implement first recyclerView "menu"
     */
    private void menuList() {

        MenuStruct ms;

        Bitmap imgMenu = BitmapFactory.decodeResource(context.getResources(), R.drawable.color);

        // CHANGER LES IMAGES
        ms = new MenuStruct("Color", imgMenu, RecyclerType.Color);
        menuList.add(ms);

        imgMenu = BitmapFactory.decodeResource(context.getResources(), R.drawable.contrast);
        ms = new MenuStruct("Contrast", imgMenu, RecyclerType.Contrast);
        menuList.add(ms);

        imgMenu = BitmapFactory.decodeResource(context.getResources(), R.drawable.mask);
        ms = new MenuStruct("Mask", imgMenu, RecyclerType.Mask);
        menuList.add(ms);

        imgMenu = BitmapFactory.decodeResource(context.getResources(), R.drawable.extra);
        ms = new MenuStruct("Extras", imgMenu, RecyclerType.Extras);
        menuList.add(ms);

        menuAdapter.notifyDataSetChanged();
    }

    /**
     * To initiate different list
     */
    public static void colorList() {

        photoAdapter = new RecyclerViewHorizontalListAdapter(colorList, context, RecyclerType.Color);
        photoRecyclerView.setAdapter(photoAdapter);
        actualMiniImage = image.copy(Bitmap.Config.ARGB_8888, true);
        // On redimensionne l'image
        Bitmap rediImageEditing = Bitmap.createScaledBitmap(image, 100, (int) ((image.getHeight() * 100) / image.getWidth()), true);
        Bitmap rediCopy;

        FilterStruct fs;

        // ToGrey
        rediCopy = rediImageEditing.copy(Bitmap.Config.ARGB_8888, true);
        fs = new FilterStruct("Grey", renderscript.toGrey(rediCopy), FilterType.Grey);
        colorList.add(fs);

        // Colorize
        rediCopy = rediImageEditing.copy(Bitmap.Config.ARGB_8888, true);
        fs = new FilterStruct("Colorize", renderscript.colorize(rediCopy, 180), FilterType.Colorize);
        colorList.add(fs);

        // KeepColor
        rediCopy = rediImageEditing.copy(Bitmap.Config.ARGB_8888, true);
        fs = new FilterStruct("KeepHue", renderscript.keepHue(rediCopy, 180, 60), FilterType.KeepHue);
        colorList.add(fs);

        // Invert
        rediCopy = rediImageEditing.copy(Bitmap.Config.ARGB_8888, true);
        fs = new FilterStruct("Invert", renderscript.invert(rediCopy), FilterType.Invert);
        colorList.add(fs);

        photoAdapter.notifyDataSetChanged();

    }

    public static void saturationList() {

        photoAdapter = new RecyclerViewHorizontalListAdapter(contrastList, context, RecyclerType.Contrast);
        photoRecyclerView.setAdapter(photoAdapter);
        actualMiniImage = image.copy(Bitmap.Config.ARGB_8888, true);

        Bitmap rediImageEditing = Bitmap.createScaledBitmap(image, 100, (int) ((image.getHeight() * 100) / image.getWidth()), true);
        Bitmap rediCopy;

        FilterStruct fs;

        // Constrast setting
        rediCopy = rediImageEditing.copy(Bitmap.Config.ARGB_8888, true);
        hist = new HistogramManipulation(rediCopy, ChanelType.V);
        hist.linearExtensionLUT(198, 50);
        //fs = new FilterStruct("contrast", hist.applyLUT(rediCopy));
        fs = new FilterStruct("Contrast", renderscript.applyLUT(rediCopy, hist), FilterType.Contrast);
        contrastList.add(fs);

        // ShiftLight
        rediCopy = rediImageEditing.copy(Bitmap.Config.ARGB_8888, true);
        hist = new HistogramManipulation(rediCopy, ChanelType.V);
        hist.shiftLUT(45);
        //fs = new FilterStruct("shiftLight", hist.applyLUT(rediCopy));
        fs = new FilterStruct("ShiftLight", renderscript.applyLUT(rediCopy, hist), FilterType.ShiftLight);
        contrastList.add(fs);

        // ShiftSaturation
        rediCopy = rediImageEditing.copy(Bitmap.Config.ARGB_8888, true);
        hist = new HistogramManipulation(rediCopy, ChanelType.S);
        hist.shiftLUT(45);
        //fs = new FilterStruct("shiftSaturation", hist.applyLUT(rediCopy));
        fs = new FilterStruct("ShiftSaturation", renderscript.applyLUT(rediCopy, hist), FilterType.ShiftSaturation);
        contrastList.add(fs);

        // ShiftColor
        rediCopy = rediImageEditing.copy(Bitmap.Config.ARGB_8888, true);
        hist = new HistogramManipulation(rediCopy, ChanelType.H);
        hist.shiftCycleLUT(120);
        //fs = new FilterStruct("shiftColor", hist.applyLUT(rediCopy));
        fs = new FilterStruct("ShiftColor", renderscript.applyLUT(rediCopy, hist), FilterType.ShiftColor);
        contrastList.add(fs);

        // Isohelie
        rediCopy = rediImageEditing.copy(Bitmap.Config.ARGB_8888, true);
        hist = new HistogramManipulation(rediCopy, ChanelType.V);
        hist.isohelieLUT(4);
        //fs = new FilterStruct("isohelie", hist.applyLUT(rediCopy));
        fs = new FilterStruct("Isohelie", renderscript.applyLUT(rediCopy, hist), FilterType.Isohelie);
        contrastList.add(fs);

        // EqualizationLight
        rediCopy = rediImageEditing.copy(Bitmap.Config.ARGB_8888, true);
        hist = new HistogramManipulation(rediCopy, ChanelType.V);
        hist.equalizationLUT();
        //fs = new FilterStruct("equa light", hist.applyLUT(rediCopy));
        fs = new FilterStruct("EquaLight", renderscript.applyLUT(rediCopy, hist), FilterType.EquaLight);
        contrastList.add(fs);

        photoAdapter.notifyDataSetChanged();

    }

    public static void maskList() {

        photoAdapter = new RecyclerViewHorizontalListAdapter(maskList, context, RecyclerType.Mask);
        photoRecyclerView.setAdapter(photoAdapter);
        actualMiniImage = image.copy(Bitmap.Config.ARGB_8888, true);

        // On redimensionne l'image
        Bitmap rediImageEditing = Bitmap.createScaledBitmap(image, 100, (int) ((image.getHeight() * 100) / image.getWidth()), true);
        Bitmap rediCopy;

        FilterStruct fs;

        // Blur
        rediCopy = rediImageEditing.copy(Bitmap.Config.ARGB_8888, true);
        BlurMask mask = new BlurMask(9);
        fs = new FilterStruct("Blur", renderscript.convolution(rediCopy, mask), FilterType.Blur);
        maskList.add(fs);

        // Gaussian
        rediCopy = rediImageEditing.copy(Bitmap.Config.ARGB_8888, true);
        GaussianBlur maskGaussian = new GaussianBlur(5, 2.5);
        fs = new FilterStruct("Gaussian", renderscript.convolution(rediCopy, maskGaussian), FilterType.Gaussian);
        maskList.add(fs);

        // Laplacien
        rediCopy = rediImageEditing.copy(Bitmap.Config.ARGB_8888, true);
        LaplacienMask maskLaplacien = new LaplacienMask();
        fs = new FilterStruct("Laplacien", renderscript.convolution(rediCopy, maskLaplacien), FilterType.Laplacien);
        maskList.add(fs);

        // Sobel vertical
        rediCopy = rediImageEditing.copy(Bitmap.Config.ARGB_8888, true);
        SobelMask sobelMaskVertical = new SobelMask(true);
        fs = new FilterStruct("Sobel V", renderscript.convolution(rediCopy, sobelMaskVertical), FilterType.SobelV);
        maskList.add(fs);

        // Sobel horizontal
        rediCopy = rediImageEditing.copy(Bitmap.Config.ARGB_8888, true);
        SobelMask sobelMaskHorizontal = new SobelMask(false);
        fs = new FilterStruct("Sobel H", renderscript.convolution(rediCopy, sobelMaskHorizontal), FilterType.SobelH);
        maskList.add(fs);

        photoAdapter.notifyDataSetChanged();

    }

    public static void extrasList() {
        photoAdapter = new RecyclerViewHorizontalListAdapter(extraList, context, RecyclerType.Extras);
        photoRecyclerView.setAdapter(photoAdapter);
        actualMiniImage = image.copy(Bitmap.Config.ARGB_8888, true);

        // On redimensionne l'image
        Bitmap rediImageEditing = Bitmap.createScaledBitmap(image, 100, (int) ((image.getHeight() * 100) / image.getWidth()), true);
        Bitmap rediCopy;

        FilterStruct fs;

        // FaceDetection
        rediCopy = rediImageEditing.copy(Bitmap.Config.ARGB_8888, true);
        fs = new FilterStruct("Face Detection", faceDetection.putSunglass(rediCopy), FilterType.FaceDetection);
        extraList.add(fs);

        // Rotate
        rediCopy = rediImageEditing.copy(Bitmap.Config.ARGB_8888, true);
        fs = new FilterStruct("Rotate", rediCopy, FilterType.Rotate);
        extraList.add(fs);

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
            Toast.makeText(context, "save", Toast.LENGTH_SHORT).show();
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