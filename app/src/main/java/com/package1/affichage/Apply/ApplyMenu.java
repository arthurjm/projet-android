package com.package1.affichage.Apply;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

import com.package1.ChanelType;
import com.package1.FaceDetection;
import com.package1.HistogramManipulation;
import com.package1.Mask.BlurMask;
import com.package1.Mask.GaussianBlur;
import com.package1.Mask.LaplacienMask;
import com.package1.Mask.SobelMask;
import com.package1.R;
import com.package1.RS;
import com.package1.affichage.Adapter.FilterAdapter;
import com.package1.affichage.Struct.FilterStruct;
import com.package1.affichage.Struct.MenuStruct;
import com.package1.affichage.Type.FilterType;
import com.package1.affichage.Type.MenuType;

import java.util.ArrayList;
import java.util.List;

import static com.package1.MainActivity.image;
import static com.package1.MainActivity.imageEditingCopy;
import static com.package1.MainActivity.imgView;
import static com.package1.affichage.PhotoEditing.back;
import static com.package1.affichage.PhotoEditing.menuAdapter;
import static com.package1.affichage.PhotoEditing.menuRecyclerView;
import static com.package1.affichage.PhotoEditing.filterAdapter;
import static com.package1.affichage.PhotoEditing.filterRecyclerView;
import static com.package1.affichage.PhotoEditing.actualMiniImage;

/**
 * @author Mathieu
 * In this class, we can find all functions that we use to modify the menuRecyclerView
 * We have different list which contains filter's list
 * In colorList we have different filter's that we use to modify the basic color of a picture
 * In contrastList we have different filter's that we use to modify the contrast/saturation of a picture
 * In maskList we have different filter's that we use to apply blur effects
 * In extraList we have different filter's like the rotation or the faceDetection
 *
 * There is also some variables like renderscript or faceDetection. They are there to apply some effects
 */
public class ApplyMenu {

    public List<MenuStruct> menuList;
    public List<FilterStruct> colorList;
    public List<FilterStruct> contrastList;
    public List<FilterStruct> maskList;
    public List<FilterStruct> extraList;
    public Context ctx;
    public RS renderscript;
    public FaceDetection faceDetection;
    public HistogramManipulation hist;

    /**
     * Constructor
     * @param ctx
     * @param renderScript
     * @param faceDetection
     * @param hist
     */
    public ApplyMenu(Context ctx, RS renderScript, FaceDetection faceDetection, HistogramManipulation hist) {
        this.ctx = ctx;
        this.renderscript = renderScript;
        this.faceDetection = faceDetection;
        this.hist = hist;

        colorList = new ArrayList<>();
        menuList = new ArrayList<>();
        contrastList = new ArrayList<>();
        extraList = new ArrayList<>();
        maskList = new ArrayList<>();
        contrastList = new ArrayList<>();
    }

    /**
     * To create the menu
     */
    public void menuList() {

        MenuStruct ms;

        Bitmap imgMenu = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.color);

        ms = new MenuStruct("Color", imgMenu, MenuType.Color);
        menuList.add(ms);

        imgMenu = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.contrast);
        ms = new MenuStruct("Contrast", imgMenu, MenuType.Contrast);
        menuList.add(ms);

        imgMenu = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.mask);
        ms = new MenuStruct("Mask", imgMenu, MenuType.Mask);
        menuList.add(ms);

        imgMenu = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.extra);
        ms = new MenuStruct("Extras", imgMenu, MenuType.Extras);
        menuList.add(ms);

        menuAdapter.notifyDataSetChanged();
    }

    /**
     * To change the actual list in RecyclerView
     *
     * @param rt
     */
    public void changeList(MenuType rt) {
        switch (rt) {
            case Color:
                colorList();
                break;
            case Contrast:
                saturationList();
                break;
            case Mask:
                maskList();
                break;
            case Extras:
                extrasList();
                break;
            default:
                break;

        }
    }

    /**
     * Use a function to modify the actual image
     * @param type
     */
    public void useFunction(MenuType type) {
        imgView.setImageBitmap(imageEditingCopy);
        menuRecyclerView.setVisibility(View.GONE);
        filterRecyclerView.setVisibility(View.VISIBLE);
        back.setVisibility(View.VISIBLE);
        switch (type) {
            case Color:
                changeList(MenuType.Color);
                break;
            case Contrast:
                changeList(MenuType.Contrast);
                break;
            case Mask:
                changeList(MenuType.Mask);
                break;
            case Extras:
                changeList(MenuType.Extras);
                break;
            default:
                break;
        }
    }

    /**
     * To initiate different list
     */
    public void colorList() {

        filterAdapter = new FilterAdapter(colorList, ctx, MenuType.Color);
        filterRecyclerView.setAdapter(filterAdapter);
        actualMiniImage = image.copy(Bitmap.Config.ARGB_8888, true);

        if (colorList.isEmpty() == true) {
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
        }
        filterAdapter.notifyDataSetChanged();

    }

    public void saturationList() {

        filterAdapter = new FilterAdapter(contrastList, ctx, MenuType.Contrast);
        filterRecyclerView.setAdapter(filterAdapter);
        actualMiniImage = image.copy(Bitmap.Config.ARGB_8888, true);

        if (contrastList.isEmpty() == true) {

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
        }
        filterAdapter.notifyDataSetChanged();

    }

    public void maskList() {

        filterAdapter = new FilterAdapter(maskList, ctx, MenuType.Mask);
        filterRecyclerView.setAdapter(filterAdapter);
        actualMiniImage = image.copy(Bitmap.Config.ARGB_8888, true);

        if (maskList.isEmpty() == true) {
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

            // Increase Borders
            rediCopy = rediImageEditing.copy(Bitmap.Config.ARGB_8888, true);
            fs = new FilterStruct("Increase Border", renderscript.increaseBorder(rediCopy, 25), FilterType.IncreaseBorder);
            maskList.add(fs);
        }
        filterAdapter.notifyDataSetChanged();

    }

    public void extrasList() {
        filterAdapter = new FilterAdapter(extraList, ctx, MenuType.Extras);
        filterRecyclerView.setAdapter(filterAdapter);
        actualMiniImage = image.copy(Bitmap.Config.ARGB_8888, true);

        if (extraList.isEmpty() == true) {
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
        }
        filterAdapter.notifyDataSetChanged();

    }
}
