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
import static com.package1.affichage.PhotoEditing.nightMode;

/**
 * @author Mathieu
 * In this class, we can find all functions that we use to modify the menuRecyclerView
 * We have different list which contains filter's list
 * In colorList we have different filter's that we use to modify the basic color of a picture ...
 * In contrastList we have different filter's that we use to modify the contrast/saturation of a picture ...
 * In maskList we have different filter's that we use to apply blur effects ...
 * In extraList we have different filter's like the rotation or the faceDetection ...
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
    public Bitmap resizeImageEditing;
    public Bitmap resizeCopy;

    /**
     * Constructor
     *
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
        resizeImageEditing = Bitmap.createScaledBitmap(image, 100, (int) ((image.getHeight() * 100) / image.getWidth()), true);

    }

    /**
     * To create the principal menu
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
     * @param menuType
     */
    public void changeList(MenuType menuType) {
        switch (menuType) {
            case Color:
                colorList();
                break;
            case Contrast:
                contrastList();
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
     * Use a function to modify the actual recyclerView
     *
     * @param menuType
     */
    public void modifyList(MenuType menuType) {
        imgView.setImageBitmap(imageEditingCopy);
        menuRecyclerView.setVisibility(View.GONE);
        filterRecyclerView.setVisibility(View.VISIBLE);
        back.setVisibility(View.VISIBLE);
        switch (menuType) {
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

            FilterStruct fs;

            // ToGrey
            resizeCopy = resizeImageEditing.copy(Bitmap.Config.ARGB_8888, true);
            fs = new FilterStruct("Grey", renderscript.toGrey(resizeCopy), FilterType.Grey);
            colorList.add(fs);

            // Colorize
            resizeCopy = resizeImageEditing.copy(Bitmap.Config.ARGB_8888, true);
            fs = new FilterStruct("Colorize", renderscript.colorize(resizeCopy, 180), FilterType.Colorize);
            colorList.add(fs);

            // KeepColor
            resizeCopy = resizeImageEditing.copy(Bitmap.Config.ARGB_8888, true);
            fs = new FilterStruct("KeepHue", renderscript.keepHue(resizeCopy, 180, 60), FilterType.KeepHue);
            colorList.add(fs);

            // Invert
            resizeCopy = resizeImageEditing.copy(Bitmap.Config.ARGB_8888, true);
            fs = new FilterStruct("Invert", renderscript.invert(resizeCopy), FilterType.Invert);
            colorList.add(fs);
        }
        filterAdapter.notifyDataSetChanged();

    }

    public void contrastList() {

        filterAdapter = new FilterAdapter(contrastList, ctx, MenuType.Contrast);
        filterRecyclerView.setAdapter(filterAdapter);
        actualMiniImage = image.copy(Bitmap.Config.ARGB_8888, true);

        if (contrastList.isEmpty() == true) {

            FilterStruct fs;

            // Constrast setting
            resizeCopy = resizeImageEditing.copy(Bitmap.Config.ARGB_8888, true);
            hist = new HistogramManipulation(resizeCopy, ChanelType.V, renderscript);
            hist.linearExtensionLUT(198, 50);
            //fs = new FilterStruct("contrast", hist.applyLUT(resizeCopy));
            fs = new FilterStruct("Contrast", renderscript.applyLUT(resizeCopy, hist), FilterType.Contrast);
            contrastList.add(fs);

            // ShiftLight
            resizeCopy = resizeImageEditing.copy(Bitmap.Config.ARGB_8888, true);
            hist = new HistogramManipulation(resizeCopy, ChanelType.V, renderscript);
            hist.shiftLUT(45);
            //fs = new FilterStruct("shiftLight", hist.applyLUT(resizeCopy));
            fs = new FilterStruct("ShiftLight", renderscript.applyLUT(resizeCopy, hist), FilterType.ShiftLight);
            contrastList.add(fs);

            // ShiftSaturation
            resizeCopy = resizeImageEditing.copy(Bitmap.Config.ARGB_8888, true);
            hist = new HistogramManipulation(resizeCopy, ChanelType.S, renderscript);
            hist.shiftLUT(45);
            //fs = new FilterStruct("shiftSaturation", hist.applyLUT(resizeCopy));
            fs = new FilterStruct("ShiftSaturation", renderscript.applyLUT(resizeCopy, hist), FilterType.ShiftSaturation);
            contrastList.add(fs);

            // ShiftColor
            resizeCopy = resizeImageEditing.copy(Bitmap.Config.ARGB_8888, true);
            hist = new HistogramManipulation(resizeCopy, ChanelType.H, renderscript);
            hist.shiftCycleLUT(120);
            //fs = new FilterStruct("shiftColor", hist.applyLUT(resizeCopy));
            fs = new FilterStruct("ShiftColor", renderscript.applyLUT(resizeCopy, hist), FilterType.ShiftColor);
            contrastList.add(fs);


            // Isohelie
            resizeCopy = resizeImageEditing.copy(Bitmap.Config.ARGB_8888, true);
            hist = new HistogramManipulation(resizeCopy, ChanelType.V, renderscript);
            hist.isohelieLUT(4);
            //fs = new FilterStruct("isohelie", hist.applyLUT(resizeCopy));
            fs = new FilterStruct("Isohelie", renderscript.applyLUT(resizeCopy, hist), FilterType.Isohelie);
            contrastList.add(fs);

            // EqualizationLight
            resizeCopy = resizeImageEditing.copy(Bitmap.Config.ARGB_8888, true);
            hist = new HistogramManipulation(resizeCopy, ChanelType.V, renderscript);
            hist.equalizationLUT();
            //fs = new FilterStruct("equa light", hist.applyLUT(resizeCopy));
            fs = new FilterStruct("EquaLight", renderscript.applyLUT(resizeCopy, hist), FilterType.EquaLight);
            contrastList.add(fs);
        }
        filterAdapter.notifyDataSetChanged();

    }

    public void maskList() {

        filterAdapter = new FilterAdapter(maskList, ctx, MenuType.Mask);
        filterRecyclerView.setAdapter(filterAdapter);
        actualMiniImage = image.copy(Bitmap.Config.ARGB_8888, true);

        if (maskList.isEmpty() == true) {

            FilterStruct fs;

            // Blur
            resizeCopy = resizeImageEditing.copy(Bitmap.Config.ARGB_8888, true);
            BlurMask mask = new BlurMask(9);
            fs = new FilterStruct("Blur", renderscript.convolution(resizeCopy, mask), FilterType.Blur);
            maskList.add(fs);

            // Gaussian
            resizeCopy = resizeImageEditing.copy(Bitmap.Config.ARGB_8888, true);
            GaussianBlur maskGaussian = new GaussianBlur(5, 2.5);
            fs = new FilterStruct("Gaussian", renderscript.convolution(resizeCopy, maskGaussian), FilterType.Gaussian);
            maskList.add(fs);

            // Laplacien
            resizeCopy = resizeImageEditing.copy(Bitmap.Config.ARGB_8888, true);
            LaplacienMask maskLaplacien = new LaplacienMask();
            fs = new FilterStruct("Laplacien", renderscript.convolution(resizeCopy, maskLaplacien), FilterType.Laplacien);
            maskList.add(fs);

            // Sobel vertical
            resizeCopy = resizeImageEditing.copy(Bitmap.Config.ARGB_8888, true);
            SobelMask sobelMaskVertical = new SobelMask(true);
            fs = new FilterStruct("Sobel V", renderscript.convolution(resizeCopy, sobelMaskVertical), FilterType.SobelV);
            maskList.add(fs);

            // Sobel horizontal
            resizeCopy = resizeImageEditing.copy(Bitmap.Config.ARGB_8888, true);
            SobelMask sobelMaskHorizontal = new SobelMask(false);
            fs = new FilterStruct("Sobel H", renderscript.convolution(resizeCopy, sobelMaskHorizontal), FilterType.SobelH);
            maskList.add(fs);

            // Sobel
            resizeCopy = resizeImageEditing.copy(Bitmap.Config.ARGB_8888, true);
            fs = new FilterStruct("Sobel", renderscript.sobel(resizeCopy), FilterType.Sobel);
            maskList.add(fs);

            // Increase Borders
            resizeCopy = resizeImageEditing.copy(Bitmap.Config.ARGB_8888, true);
            fs = new FilterStruct("Increase Border", renderscript.increaseBorder(resizeCopy, 25), FilterType.IncreaseBorder);
            maskList.add(fs);
        }
        filterAdapter.notifyDataSetChanged();

    }

    public void extrasList() {
        filterAdapter = new FilterAdapter(extraList, ctx, MenuType.Extras);
        filterRecyclerView.setAdapter(filterAdapter);
        actualMiniImage = image.copy(Bitmap.Config.ARGB_8888, true);

        FilterStruct fs;
        if (extraList.isEmpty() == true) {
            // FaceDetection
            resizeCopy = resizeImageEditing.copy(Bitmap.Config.ARGB_8888, true);
            fs = new FilterStruct("Face Detection", faceDetection.drawOnImage(resizeCopy), FilterType.FaceDetection);
            extraList.add(fs);

            // Rotate
            resizeCopy = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.rotate);
            fs = new FilterStruct("Rotate", resizeCopy, FilterType.Rotate);
            extraList.add(fs);

            // Flip Horizontal
            resizeCopy = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.flipH);
            fs = new FilterStruct("FlipH", resizeCopy, FilterType.FlipHorizontal);
            extraList.add(fs);

            // Flip vertical
            resizeCopy = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.flipV);
            fs = new FilterStruct("FlipV", resizeCopy, FilterType.FlipVertical);
            extraList.add(fs);

            nightDayMode();

        } else {
            // Remove Night/Day mode to update it
            extraList.remove(extraList.get(extraList.size() - 1));
            nightDayMode();
        }
        filterAdapter.notifyDataSetChanged();

    }

    /**
     * To change the filterStruct, if we are in night or day Mode
     */
    public void nightDayMode() {
        Bitmap bmp;
        FilterStruct fs;

        if (nightMode == false) {
            bmp = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.nightmode);
            fs = new FilterStruct("NightMode", bmp, FilterType.NightMode);
            extraList.add(fs);
        }
        // Return in normal case
        else {
            bmp = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.daymode);
            fs = new FilterStruct("DayMode", bmp, FilterType.DayMode);
            extraList.add(fs);
        }
    }
}
