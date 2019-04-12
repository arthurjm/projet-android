package com.package1.affichage.apply;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

import com.package1.pictureManipulation.ChanelType;
import com.package1.pictureManipulation.FaceDetection;
import com.package1.pictureManipulation.HistogramManipulation;
import com.package1.pictureManipulation.mask.BlurMask;
import com.package1.pictureManipulation.mask.GaussianBlur;
import com.package1.pictureManipulation.mask.LaplacienMask;
import com.package1.pictureManipulation.mask.SobelMask;
import com.package1.R;
import com.package1.pictureManipulation.RS;
import com.package1.affichage.adapter.FilterAdapter;
import com.package1.affichage.PhotoEditing;
import com.package1.affichage.struct.FilterStruct;
import com.package1.affichage.struct.MenuStruct;
import com.package1.affichage.type.FilterType;
import com.package1.affichage.type.MenuType;

import java.util.ArrayList;
import java.util.List;

import static com.package1.MainActivity.image;
import static com.package1.MainActivity.imageEditing;
import static com.package1.MainActivity.imageEditingCopy;
import static com.package1.MainActivity.imgView;

/**
 * @author Mathieu
 *         In this class, we can find all functions that we use to modify the menuRecyclerView
 *         We have different list which contains filter's list
 *         In colorList we have different filter's that we use to modify the basic color of a picture ...
 *         In contrastList we have different filter's that we use to modify the contrast/saturation of a picture ...
 *         In maskList we have different filter's that we use to apply blur effects ...
 *         In extraList we have different filter's like the rotation or the faceDetection ...
 *         There is also some variables like renderscript or faceDetection. They are there to apply some effects
 */
public class ApplyMenu {

    private List<MenuStruct> menuList;
    private List<FilterStruct> colorList;
    private List<FilterStruct> contrastList;
    private List<FilterStruct> maskList;
    private List<FilterStruct> extraList;
    private RS renderscript;
    private FaceDetection faceDetection;
    private HistogramManipulation hist;
    private Bitmap resizeImageEditing;
    private Bitmap resizeCopy;

    private PhotoEditing context;

    /**
     * Constructor
     *
     * @param ctx           The context
     * @param renderScript  the renderscript
     * @param faceDetection the facedection
     * @param hist          the histogram
     */
    public ApplyMenu(Context ctx, RS renderScript, FaceDetection faceDetection, HistogramManipulation hist) {
        this.renderscript = renderScript;
        this.faceDetection = faceDetection;
        this.hist = hist;

        colorList = new ArrayList<>();
        menuList = new ArrayList<>();
        contrastList = new ArrayList<>();
        extraList = new ArrayList<>();
        maskList = new ArrayList<>();
        contrastList = new ArrayList<>();
        resizeImageEditing = Bitmap.createScaledBitmap(image, 100, ((image.getHeight() * 100) / image.getWidth()), true);

        context = (PhotoEditing) ctx;

    }

    public List<MenuStruct> getMenuList() {
        return menuList;
    }

    public List<FilterStruct> getColorList() {
        return colorList;
    }

    /**
     * To create the principal menu
     */
    public void menuList() {

        MenuStruct ms;

        Bitmap imgMenu = BitmapFactory.decodeResource(context.getResources(), R.drawable.color);
        ms = new MenuStruct("Color", imgMenu, MenuType.Color);
        menuList.add(ms);

        imgMenu = BitmapFactory.decodeResource(context.getResources(), R.drawable.contrast);
        ms = new MenuStruct("Contrast", imgMenu, MenuType.Contrast);
        menuList.add(ms);

        imgMenu = BitmapFactory.decodeResource(context.getResources(), R.drawable.mask);
        ms = new MenuStruct("Mask", imgMenu, MenuType.Mask);
        menuList.add(ms);

        imgMenu = BitmapFactory.decodeResource(context.getResources(), R.drawable.extra);
        ms = new MenuStruct("Extras", imgMenu, MenuType.Extras);
        menuList.add(ms);

        context.menuAdapter.notifyDataSetChanged();
    }

    /**
     * To change the actual list in RecyclerView
     *
     * @param menuType the actual menuType
     */
    private void changeList(MenuType menuType) {
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
     * @param menuType the actual menuType
     */
    public void modifyList(MenuType menuType) {
        imgView.setImageBitmap(imageEditingCopy);
        context.menuRecyclerView.setVisibility(View.GONE);
        context.filterRecyclerView.setVisibility(View.VISIBLE);
        context.back.setVisibility(View.VISIBLE);
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
    private void colorList() {

        context.filterAdapter = new FilterAdapter(colorList, context, MenuType.Color);
        context.filterRecyclerView.setAdapter(context.filterAdapter);
        context.actualMiniImage = image.copy(Bitmap.Config.ARGB_8888, true);


        if (colorList.isEmpty()) {

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

            // ShiftColor
            resizeCopy = resizeImageEditing.copy(Bitmap.Config.ARGB_8888, true);
            hist = new HistogramManipulation(resizeCopy, ChanelType.H, renderscript);
            hist.shiftCycleLUT(120);
            //fs = new FilterStruct("shiftColor", hist.applyLUT(resizeCopy));
            fs = new FilterStruct("ShiftColor", renderscript.applyLUT(resizeCopy, hist), FilterType.ShiftColor);
            colorList.add(fs);

            // IsohelieRGB
            resizeCopy = resizeImageEditing.copy(Bitmap.Config.ARGB_8888, true);
            fs = new FilterStruct("IsohelieRGB", renderscript.posterisationRGB(resizeCopy, 25), FilterType.IsoHelieRGB);
            colorList.add(fs);
        }
        context.filterAdapter.notifyDataSetChanged();

    }

    private void contrastList() {

        context.filterAdapter = new FilterAdapter(contrastList, context, MenuType.Contrast);
        context.filterRecyclerView.setAdapter(context.filterAdapter);
        context.actualMiniImage = image.copy(Bitmap.Config.ARGB_8888, true);

        if (contrastList.isEmpty()) {

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

            // Isohelie
            resizeCopy = resizeImageEditing.copy(Bitmap.Config.ARGB_8888, true);
            fs = new FilterStruct("Isohelie", renderscript.posterisation(resizeCopy, 6), FilterType.Isohelie);
            contrastList.add(fs);

            // EqualizationLight
            resizeCopy = resizeImageEditing.copy(Bitmap.Config.ARGB_8888, true);
            hist = new HistogramManipulation(resizeCopy, ChanelType.V, renderscript);
            hist.equalizationLUT();
            //fs = new FilterStruct("equa light", hist.applyLUT(resizeCopy));
            fs = new FilterStruct("EquaLight", renderscript.applyLUT(resizeCopy, hist), FilterType.EquaLight);
            contrastList.add(fs);
        }
        context.filterAdapter.notifyDataSetChanged();

    }

    private void maskList() {

        context.filterAdapter = new FilterAdapter(maskList, context, MenuType.Mask);
        context.filterRecyclerView.setAdapter(context.filterAdapter);
        context.actualMiniImage = image.copy(Bitmap.Config.ARGB_8888, true);

        if (maskList.isEmpty()) {

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
        context.filterAdapter.notifyDataSetChanged();

    }

    private void extrasList() {
        context.filterAdapter = new FilterAdapter(extraList, context, MenuType.Extras);
        context.filterRecyclerView.setAdapter(context.filterAdapter);
        context.actualMiniImage = image.copy(Bitmap.Config.ARGB_8888, true);

        FilterStruct fs;
        if (extraList.isEmpty()) {
            // FaceDetection
            resizeCopy = resizeImageEditing.copy(Bitmap.Config.ARGB_8888, true);
            fs = new FilterStruct("Face Detection", faceDetection.drawOnImage(resizeCopy), FilterType.FaceDetection);
            extraList.add(fs);

            // Cartoon
            resizeCopy = resizeImageEditing.copy(Bitmap.Config.ARGB_8888, true);
            resizeCopy = renderscript.posterisation(resizeCopy, 10);
            resizeCopy = renderscript.increaseBorder(resizeCopy, 150);
            fs = new FilterStruct("Cartoon", resizeCopy, FilterType.Cartoon);
            extraList.add(fs);

            // Draw
            resizeCopy = resizeImageEditing.copy(Bitmap.Config.ARGB_8888, true);
            resizeCopy = renderscript.toGrey(resizeCopy);
            resizeCopy = renderscript.sobel(resizeCopy);
            resizeCopy = renderscript.invert(resizeCopy);
            fs = new FilterStruct("Draw", resizeCopy, FilterType.Draw);
            extraList.add(fs);

            // Rotate
            resizeCopy = BitmapFactory.decodeResource(context.getResources(), R.drawable.rotate);
            fs = new FilterStruct("Rotate", resizeCopy, FilterType.Rotate);
            extraList.add(fs);

            // Flip Horizontal
            resizeCopy = BitmapFactory.decodeResource(context.getResources(), R.drawable.flip_h);
            fs = new FilterStruct("FlipH", resizeCopy, FilterType.FlipHorizontal);
            extraList.add(fs);

            // Flip vertical
            resizeCopy = BitmapFactory.decodeResource(context.getResources(), R.drawable.flip_v);
            fs = new FilterStruct("FlipV", resizeCopy, FilterType.FlipVertical);
            extraList.add(fs);

            // Elias
            resizeCopy = resizeImageEditing.copy(Bitmap.Config.ARGB_8888, true);
            fs = new FilterStruct("Elias", resizeCopy, FilterType.FlipVertical);
            extraList.add(fs);

            // Elias1
            resizeCopy = resizeImageEditing.copy(Bitmap.Config.ARGB_8888, true);
            fs = new FilterStruct("Elias1", resizeCopy, FilterType.FlipVertical);
            extraList.add(fs);

            // Elias2
            resizeCopy = resizeImageEditing.copy(Bitmap.Config.ARGB_8888, true);
            fs = new FilterStruct("Elias2", resizeCopy, FilterType.FlipVertical);
            extraList.add(fs);

            // Day / Night mode
            nightDayMode();

        } else {

            // Remove Night/Day mode to update it
            for (FilterStruct filterStruct : extraList) {
                if (filterStruct.getFilterType() == FilterType.NightMode || filterStruct.getFilterType() == FilterType.DayMode) {
                    extraList.remove(filterStruct);
                    nightDayMode();
                }
            }
        }
        context.filterAdapter.notifyDataSetChanged();
    }

    /**
     * To change the filterStruct, if we are in night or day Mode
     */
    private void nightDayMode() {
        Bitmap bmp;
        FilterStruct fs;

        if (!context.nightMode) {
            bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.nightmode);
            fs = new FilterStruct("NightMode", bmp, FilterType.NightMode);
            extraList.add(fs);
        }
        // Return in normal case
        else {
            bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.daymode);
            fs = new FilterStruct("DayMode", bmp, FilterType.DayMode);
            extraList.add(fs);
        }
    }
}
