package com.package1.affichage.apply;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.view.View;
import android.widget.SeekBar;

import com.package1.pictureManipulation.ChanelType;
import com.package1.pictureManipulation.HistogramManipulation;
import com.package1.pictureManipulation.mask.BlurMask;
import com.package1.pictureManipulation.mask.GaussianBlur;
import com.package1.pictureManipulation.mask.LaplacienMask;
import com.package1.pictureManipulation.mask.SobelMask;
import com.package1.R;
import com.package1.pictureManipulation.RS;
import com.package1.affichage.PhotoEditing;
import com.package1.affichage.type.FilterType;
import com.package1.affichage.type.MenuType;

import static com.package1.MainActivity.imageEditing;
import static com.package1.MainActivity.imageEditingCopy;
import static com.package1.MainActivity.imgView;
import static com.package1.affichage.PhotoEditing.hist;
/**
 * @author Mathieu
 *         In this class, we can find all functions that we use to modify the actual image
 *         We have some variables :
 *         filterType -> we use it to when we need to use seekBar, and we check in which case we we are in MyTask to apply the adequate function
 *         recyclerType -> we use it to check in which type of FilterRecyclerView we are
 *         progressBar1 / progressBar2 -> actual value of seekBars
 */
public class ApplyFilter {

    private FilterType filterType;
    private MenuType menuType;
    private int progressBar1, progressBar2;

    private PhotoEditing context;

    private SeekBar seekBar1;
    private SeekBar seekBar2;
    private RS renderscript;

    /**
     * Constructor
     *
     * @param ctx the context
     * @param menuType the menuType
     */
    public ApplyFilter(Context ctx, MenuType menuType) {
        this.context = (PhotoEditing) ctx;
        this.menuType = menuType;

        this.seekBar1 = context.seekBar1;
        this.seekBar2 = context.seekBar2;
        this.renderscript = context.renderscript;
    }

    private void setFilterType(FilterType filterType) {
        this.filterType = filterType;
    }

    /**
     * To set the view of the seekBar to gone
     *
     * @param sb the seekBar
     * @see View#GONE
     */
    private void setGone(SeekBar sb) {
        sb.setVisibility(View.GONE);
    }

    /**
     * To set the view of seekBars to gone
     *
     * @param sb the seekBar
     * @param sb1 the seekBar
     */
    private void setGone(SeekBar sb, SeekBar sb1) {
        setGone(sb);
        setGone(sb1);
    }

    /**
     * To set the view of the seekBar to visible
     *
     * @param sb the seekBar
     * @see View#VISIBLE
     */
    private void setVisible(SeekBar sb) {
        sb.setVisibility(View.VISIBLE);
    }

    /**
     * To set the view of seekBars to visible
     *
     * @param sb the seekBar
     * @param sb1 the seekBar
     * @see View#VISIBLE
     */
    private void setVisible(SeekBar sb, SeekBar sb1) {
        sb.setVisibility(View.VISIBLE);
        sb1.setVisibility(View.VISIBLE);
    }

    /**
     * To set the max value of the seekBar
     *
     * @param sb the seekBar
     * @param max the max value
     * @see SeekBar#setMax(int)
     */
    private void setBorn(SeekBar sb, int max) {
        sb.setMax(max);
    }

    /**
     * To set the background of the seekBar at RGB
     *
     * @param sb the seekBar
     * @see SeekBar#setBackgroundResource(int)
     */
    private void setRGBBackground(SeekBar sb) {
        sb.setBackgroundResource(R.drawable.seekbar_progess);
    }

    /**
     * To set the background of the seekBar at normal
     *
     * @param sb the seekBar
     * @see SeekBar#setBackgroundResource(int)
     */
    private void setNormalBackground(SeekBar sb) {
        sb.setBackgroundResource(R.drawable.icon);
    }

    /**
     * To check in which case we are, and call the good function
     *
     * @param type the current filter
     */
    public void useFunction(FilterType type) {
        renderscript = new RS(context);
        addListener();
        imgView.setImageBitmap(imageEditingCopy);

        // Which functions we want to use with the adequate List
        switch (menuType) {
            case Color:
                colorFunction(type);
                break;
            case Contrast:
                saturationFunction(type);
                break;
            case Mask:
                maskFunction(type);
                break;
            case Extras:
                extraFunction(type);
                break;
            default:
                break;
        }
        imgView.setImageBitmap(imageEditingCopy);
    }


    /**
     * Different functions to set the views of seekBars, their max and min values, their background following the current filter
     * And if we don't need to have a seekBar, we apply the filter
     * If we need to use one or many seekBars, we set the local variable FilterType
     *
     * @see FilterType
     */
    private void colorFunction(FilterType type) {
        switch (type) {
            case Grey:
                setGone(seekBar1, seekBar2);
                imageEditingCopy = renderscript.toGrey(imageEditing);
                break;
            case Colorize:
                setVisible(seekBar1);
                setGone(seekBar2);
                setBorn(seekBar1, 359);
                setRGBBackground(seekBar1);
                seekBar1.setProgress(0);
                setFilterType(FilterType.Colorize);
                break;
            case KeepHue:
                setVisible(seekBar1, seekBar2);
                setBorn(seekBar1, 359);
                setBorn(seekBar2, 180);
                setRGBBackground(seekBar1);
                setNormalBackground(seekBar2);
                seekBar1.setProgress(0);
                seekBar2.setProgress(0);
                setFilterType(FilterType.KeepHue);
                break;
            case Invert:
                setGone(seekBar1, seekBar2);
                imageEditingCopy = renderscript.invert(imageEditing);
                break;
            case ShiftColor:
                setVisible(seekBar1);
                setGone(seekBar2);
                setBorn(seekBar1, 255);
                setRGBBackground(seekBar1);
                seekBar1.setProgress(0);
                setFilterType(FilterType.ShiftColor);
                break;
            case IsoHelieRGB:
                setVisible(seekBar1);
                setGone(seekBar2);
                setBorn(seekBar1, 8);
                setNormalBackground(seekBar1);
                seekBar1.setProgress(2);
                setFilterType(FilterType.IsoHelieRGB);
            default:
                break;
        }
    }

    private void saturationFunction(FilterType type) {
        switch (type) {
            case Contrast:
                setVisible(seekBar1);
                setGone(seekBar2);
                setBorn(seekBar1, 127);
                setNormalBackground(seekBar1);
                seekBar1.setProgress(0);
                setFilterType(FilterType.Contrast);
                break;
            case ShiftLight:
                setVisible(seekBar1);
                setGone(seekBar2);
                setBorn(seekBar1, 200);
                setNormalBackground(seekBar1);
                seekBar1.setProgress(0);
                setFilterType(FilterType.ShiftLight);
                break;
            case ShiftSaturation:
                setVisible(seekBar1);
                setGone(seekBar2);
                setBorn(seekBar1, 200);
                setNormalBackground(seekBar1);
                seekBar1.setProgress(0);
                setFilterType(FilterType.ShiftSaturation);
                break;
            case Isohelie:
                setVisible(seekBar1);
                setGone(seekBar2);
                setBorn(seekBar1, 12);
                setNormalBackground(seekBar1);
                seekBar1.setProgress(2);
                setFilterType(FilterType.Isohelie);
                break;
            case EquaLight:
                setGone(seekBar1, seekBar2);
                setFilterType(FilterType.EquaLight);
                new ApplyFilter.MyTask().execute(imageEditing);
                break;
            default:
                break;

        }
    }

    private void maskFunction(FilterType type) {
        switch (type) {
            case Blur:
                setNormalBackground(seekBar1);
                setVisible(seekBar1);
                setGone(seekBar2);
                seekBar1.setProgress(0);
                setBorn(seekBar1, 10);
                setFilterType(FilterType.Blur);
                break;
            case Gaussian:
                setNormalBackground(seekBar1);
                setVisible(seekBar1);
                setGone(seekBar2);
                seekBar1.setProgress(0);
                setBorn(seekBar1, 5);
                setFilterType(FilterType.Gaussian);
                break;
            case Laplacien:
                setGone(seekBar1, seekBar2);
                LaplacienMask maskLaplacien = new LaplacienMask();
                imageEditingCopy = renderscript.convolution(imageEditing, maskLaplacien);
                break;
            case SobelV:
                setGone(seekBar1, seekBar2);
                SobelMask sobelMaskVertical = new SobelMask(true);
                imageEditingCopy = renderscript.convolution(imageEditing, sobelMaskVertical);
                break;
            case SobelH:
                setGone(seekBar1, seekBar2);
                SobelMask sobelMaskHorizontal = new SobelMask(false);
                imageEditingCopy = renderscript.convolution(imageEditing, sobelMaskHorizontal);
                break;
            case Sobel:
                setGone(seekBar1, seekBar2);
                imageEditingCopy = renderscript.sobel(imageEditing);
                break;
            case IncreaseBorder:
                setNormalBackground(seekBar1);
                setVisible(seekBar1);
                setGone(seekBar2);
                seekBar1.setProgress(0);
                setBorn(seekBar1, 12);
                setFilterType(FilterType.IncreaseBorder);
                break;
            default:
                break;

        }
    }

    private void extraFunction(FilterType type) {
        switch (type) {
            case FaceDetection:
                setGone(seekBar1, seekBar2);
                setFilterType(FilterType.FaceDetection);
                new ApplyFilter.MyTask().execute(imageEditing);
                break;
            case Rotate:
                setGone(seekBar1, seekBar2);
                imageEditingCopy = RotateBitmap(imageEditingCopy);
                imageEditing = imageEditingCopy.copy(Bitmap.Config.ARGB_8888, true);
                break;
            case NightMode:
                setGone(seekBar1, seekBar2);
                context.nightMode();
                break;
            case DayMode:
                setGone(seekBar1, seekBar2);
                context.dayMode();
                break;
            case FlipHorizontal:
                setGone(seekBar1, seekBar2);
                imageEditingCopy = flipH(imageEditingCopy);
                imageEditing = imageEditingCopy.copy(Bitmap.Config.ARGB_8888, true);
                break;
            case FlipVertical:
                setGone(seekBar1, seekBar2);
                imageEditingCopy = flipV(imageEditingCopy);
                imageEditing = imageEditingCopy.copy(Bitmap.Config.ARGB_8888, true);
                break;
            case Cartoon:
                setGone(seekBar1, seekBar2);
                imageEditingCopy = renderscript.posterisation(imageEditing, 5);
                imageEditingCopy = renderscript.increaseBorder(imageEditingCopy, 140);
                imageEditingCopy = renderscript.increaseBorder(imageEditingCopy, 80);//applied a second time to get bigger borders.
                break;
            case Draw:
                setGone(seekBar1, seekBar2);
                imageEditingCopy = renderscript.toGrey(imageEditing);
                imageEditingCopy = renderscript.sobel(imageEditingCopy);
                imageEditingCopy = renderscript.invert(imageEditingCopy);
                imageEditingCopy = renderscript.increaseBorder(imageEditingCopy, 120);
                break;
            case Elias:
                setGone(seekBar1, seekBar2);
                break;
            case Elias1:
                setGone(seekBar1, seekBar2);
                break;
            case Elias2:
                setGone(seekBar1, seekBar2);
                break;
            default:
                break;

        }
    }

    /**
     * Flip an image in vertical
     *
     * @param source bitmap we want to change
     * @return the bitmap modify
     */
    private Bitmap flipH(Bitmap source) {
        Matrix matrix = new Matrix();
        matrix.preScale(1.0f, -1.0f);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    /**
     * Flip in image in horizontal
     *
     * @param source bitmap we want to change
     * @return the bitmap modify
     */
    private Bitmap flipV(Bitmap source) {
        Matrix matrix = new Matrix();
        matrix.preScale(-1.0f, 1.0f);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);

    }

    /**
     * Rotate a bitmap at 90°
     *
     * @param source the Bitmap
     * @return bitmap flip with angle
     */
    private Bitmap RotateBitmap(Bitmap source) {
        Matrix matrix = new Matrix();
        matrix.postRotate((float) 90);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    /**
     * Add actions
     */
    private void addListener() {

        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressBar1 = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            /**
             * Apply the good filter when we need to have one or many seekBar
             * @param seekBar the seekBar
             */
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                new ApplyFilter.MyTask().execute(imageEditing);
            }
        });

        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressBar2 = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            /**
             * Apply the good filter when we need to have one or many seekBar
             * @param seekBar the seekBar
             */
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                new ApplyFilter.MyTask().execute(imageEditing);
            }
        });
    }

    /**
     * Function "AsyncTask" enables proper and easy use of the UI thread
     * doInBackGround enables apply the function that we choose in background
     * and return the image edited when it finishes
     */
    @SuppressLint("StaticFieldLeak")
    private class MyTask extends AsyncTask<Bitmap, Void, Bitmap> {

        /**
         * show the message "function start" when the function is chosen
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            imgView.setVisibility(View.GONE);
            context.animationIV.setVisibility(View.VISIBLE);
            //animationDrawable = (AnimationDrawable) animationIV.getDrawable();
            //animationDrawable.start();
            FrameAnimationDrawable.create().animateRawManuallyFromXML(R.drawable.animation1, context.animationIV);


        }

        /**
         * match the function that we choose and apply on the image
         *
         * @param Bitmap the image that we process
         * @return the bitmap
         */
        @Override
        protected Bitmap doInBackground(Bitmap... Bitmap) {
            switch (filterType) {
                case IsoHelieRGB:
                    imageEditingCopy = renderscript.posterisationRGB(Bitmap[0], progressBar1 + 2);
                    break;
                case EquaLight:
                    hist = new HistogramManipulation(Bitmap[0], ChanelType.V, renderscript);
                    hist.equalizationLUT();
                    imageEditingCopy = renderscript.applyLUT(Bitmap[0], hist);
                    break;
                case Colorize:
                    imageEditingCopy = renderscript.colorize(Bitmap[0], progressBar1);
                    break;
                case KeepHue:
                    imageEditingCopy = renderscript.keepHue(Bitmap[0], progressBar1, progressBar2);
                    break;
                case Contrast:
                    hist = new HistogramManipulation(Bitmap[0], ChanelType.V, renderscript);
                    hist.linearExtensionLUT(128 + progressBar1, 127 - progressBar1);
                    //imageEditingCopy = hist.applyLUT(imageEditing); //java version
                    imageEditingCopy = renderscript.applyLUT(Bitmap[0], hist);
                    break;
                case ShiftLight:
                    hist = new HistogramManipulation(Bitmap[0], ChanelType.V, renderscript);
                    hist.shiftLUT(progressBar1 - 100);
                    //imageEditingCopy = hist.applyLUT(imageEditing); //java version
                    imageEditingCopy = renderscript.applyLUT(Bitmap[0], hist);
                    break;
                case ShiftSaturation:
                    hist = new HistogramManipulation(Bitmap[0], ChanelType.S, renderscript);
                    hist.shiftLUT(progressBar1 - 100);
                    //imageEditingCopy = hist.applyLUT(imageEditing); //java version
                    imageEditingCopy = renderscript.applyLUT(Bitmap[0], hist);
                    break;
                case ShiftColor:
                    hist = new HistogramManipulation(Bitmap[0], ChanelType.H, renderscript);
                    hist.shiftCycleLUT(progressBar1);
                    //imageEditingCopy = hist.applyLUT(imageEditing); //java version
                    imageEditingCopy = renderscript.applyLUT(Bitmap[0], hist);
                    break;
                case Isohelie:
                    imageEditingCopy = renderscript.posterisation(Bitmap[0], progressBar1 + 2);
                    break;
                case Blur:
                    BlurMask mask = new BlurMask(progressBar1 + 10);
                    imageEditingCopy = renderscript.convolution(Bitmap[0], mask);
                    break;
                case Gaussian:
                    GaussianBlur maskGaussian = new GaussianBlur(progressBar1 * 2 + 1, 2.5);
                    imageEditingCopy = renderscript.convolution(Bitmap[0], maskGaussian);
                    break;
                case IncreaseBorder:
                    imageEditingCopy = renderscript.increaseBorder(Bitmap[0], (progressBar1 + 3) * 10);
                    break;
                case FaceDetection:
                    imageEditingCopy = context.faceDetection.drawOnImage(imageEditing);
                    break;
                default:
                    break;
            }
            return imageEditingCopy;
        }

        /**
         * return the image processed and show the message "function end"
         *
         * @param bitmap the image processed
         */
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            //animationDrawable.stop();
            FrameAnimationDrawable.create().stopAnimation(true);
            context.animationIV.setVisibility(View.GONE);
            imgView.setImageBitmap(bitmap);
            imgView.setVisibility(View.VISIBLE);

        }
    }
}
