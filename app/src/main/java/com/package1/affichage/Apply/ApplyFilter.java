package com.package1.affichage.Apply;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
import com.package1.affichage.Type.FilterType;
import com.package1.affichage.Type.MenuType;

import static com.package1.MainActivity.imageEditing;
import static com.package1.MainActivity.imageEditingCopy;
import static com.package1.MainActivity.imgView;
import static com.package1.affichage.PhotoEditing.applyMenu;
import static com.package1.affichage.PhotoEditing.back;
import static com.package1.affichage.PhotoEditing.context;
import static com.package1.affichage.PhotoEditing.dayMode;
import static com.package1.affichage.PhotoEditing.faceDetection;
import static com.package1.affichage.PhotoEditing.filterRecyclerView;
import static com.package1.affichage.PhotoEditing.hist;
import static com.package1.affichage.PhotoEditing.menuRecyclerView;
import static com.package1.affichage.PhotoEditing.nightMode;
import static com.package1.affichage.PhotoEditing.renderscript;
import static com.package1.affichage.PhotoEditing.seekBar1;
import static com.package1.affichage.PhotoEditing.seekBar2;

/**
 * @author Mathieu
 * In this class, we can find all functions that we use to modify the actual image
 * We have some variables :
 * filterType -> we use it to when we need to use seekBar, and we check in which case we we are in MyTask to apply the adequate function
 * recyclerType -> we use it to check in which type of FilterRecyclerView we are
 * progressBar1 / progressBar2 -> actual value of seekBars
 */
public class ApplyFilter extends AppCompatActivity {

    public FilterType filterType;
    public MenuType menuType;
    public Context ctx;
    public int progressBar1, progressBar2;

    /**
     * Constructor
     *
     * @param ctx
     * @param menuType
     */
    public ApplyFilter(Context ctx, MenuType menuType) {
        this.ctx = ctx;
        this.menuType = menuType;
    }

    public void setFilterType(FilterType filterType) {
        this.filterType = filterType;
    }

    /**
     * To set the view to gone
     *
     * @param sb
     * @see View#GONE
     */
    public void setGone(SeekBar sb) {
        sb.setVisibility(View.GONE);
    }

    /**
     * To set the view to gone
     *
     * @param sb
     * @param sb1
     */
    public void setGone(SeekBar sb, SeekBar sb1) {
        setGone(sb);
        setGone(sb1);
    }

    /**
     * To set the view to visible
     *
     * @param sb
     * @see View#VISIBLE
     */
    public void setVisible(SeekBar sb) {
        sb.setVisibility(View.VISIBLE);
    }

    /**
     * To set the view to visible
     *
     * @param sb
     * @param sb1
     * @see View#VISIBLE
     */
    public void setVisible(SeekBar sb, SeekBar sb1) {
        sb.setVisibility(View.VISIBLE);
        sb1.setVisibility(View.VISIBLE);
    }

    /**
     * To set the max value
     *
     * @param sb
     * @param max
     * @see SeekBar#setMax(int)
     */
    public void setBorn(SeekBar sb, int max) {
        sb.setMax(max);
    }

    /**
     * To set the background at RGB
     *
     * @param sb
     * @see SeekBar#setBackgroundResource(int)
     */
    public void setRGBBackground(SeekBar sb) {
        sb.setBackgroundResource(R.drawable.seekbar_progess);
    }

    /**
     * To set the background at normal
     *
     * @param sb
     * @see SeekBar#setBackgroundResource(int)
     */
    public void setNormalBackground(SeekBar sb) {
        sb.setBackgroundResource(0);
    }

    /**
     * To check in which case we are
     *
     * @param type
     */
    public void useFunction(FilterType type) {
        renderscript = new RS(ctx);
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
     * Different functions to set different elements in the layout and apply filters when we don't need to have seekbar's
     */
    public void colorFunction(FilterType type) {
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
                seekBar1.setProgress(0);
                seekBar2.setProgress(0);
                setFilterType(FilterType.KeepHue);
                break;
            case Invert:
                setGone(seekBar1, seekBar2);
                imageEditingCopy = renderscript.invert(imageEditing);
                break;
            default:
                break;
        }
    }

    public void saturationFunction(FilterType type) {
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
            case ShiftColor:
                setVisible(seekBar1);
                setGone(seekBar2);
                setBorn(seekBar1, 255);
                setRGBBackground(seekBar1);
                seekBar1.setProgress(0);
                setFilterType(FilterType.ShiftColor);
                break;
            case Isohelie:
                setVisible(seekBar1);
                setGone(seekBar2);
                setBorn(seekBar1, 8);
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

    public void maskFunction(FilterType type) {
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
                setBorn(seekBar1, 15);
                setFilterType(FilterType.IncreaseBorder);
                break;
            default:
                break;

        }
    }

    public void extraFunction(FilterType type) {
        switch (type) {
            case FaceDetection:
                setGone(seekBar1, seekBar2);
                imageEditingCopy = faceDetection.putSunglass(imageEditing);
                break;
            case Rotate:
                setGone(seekBar1, seekBar2);
                imageEditingCopy = RotateBitmap(imageEditingCopy, 90);
                imageEditing = imageEditingCopy.copy(Bitmap.Config.ARGB_8888, true);
                break;
            case NightMode:
                setGone(seekBar1, seekBar2);
                //seekBar2.getThumb().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
                applyMenu.menuList.clear();
                applyMenu.menuList();
                menuRecyclerView.setVisibility(View.VISIBLE);
                filterRecyclerView.setVisibility(View.GONE);
                back.setVisibility(View.GONE);
                nightMode();
                nightMode = true;
                break;
            case DayMode:
                setGone(seekBar1, seekBar2);
                dayMode();
                applyMenu.menuList.clear();
                applyMenu.menuList();
                menuRecyclerView.setVisibility(View.VISIBLE);
                filterRecyclerView.setVisibility(View.GONE);
                back.setVisibility(View.GONE);
                nightMode = false;
            default:
                break;

        }
    }

    /**
     * Rotate a bitmap
     *
     * @param source
     * @param angle
     * @return bitmap flip with angle
     */
    public Bitmap RotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
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

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                new ApplyFilter.MyTask().execute(imageEditing);
            }
        });
    }

    /**
     * Fonction "AsyncTask" enables proper and easy use of the UI thread
     * doInBackGround enables apply the function that we choose in background
     * and return the image edited when it finishes
     */
    private class MyTask extends AsyncTask<Bitmap, Void, Bitmap> {

        /**
         * show the message "function start" when the function is chosen
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Toast.makeText(ctx, "function start", Toast.LENGTH_SHORT).show();
        }

        /**
         * match the function that we choose and apply on the image
         *
         * @param Bitmap the image that we process
         * @return
         */
        @Override
        protected Bitmap doInBackground(Bitmap... Bitmap) {
            switch (filterType) {
                case EquaLight:
                    hist = new HistogramManipulation(Bitmap[0], ChanelType.V,renderscript);
                    hist.equalizationLUT();
                    imageEditingCopy = renderscript.applyLUT(Bitmap[0],hist);
                    break;
                case Colorize:
                    imageEditingCopy = renderscript.colorize(Bitmap[0], progressBar1);
                    break;
                case KeepHue:
                    imageEditingCopy = renderscript.keepHue(Bitmap[0], progressBar1, progressBar2);
                    break;
                case Contrast:
                    hist = new HistogramManipulation(Bitmap[0], ChanelType.V,renderscript);
                    hist.linearExtensionLUT(128 + progressBar1, 127 - progressBar1);
                    //imageEditingCopy = hist.applyLUT(imageEditing); //java version
                    imageEditingCopy = renderscript.applyLUT(Bitmap[0], hist);
                    break;
                case ShiftLight:
                    hist = new HistogramManipulation(Bitmap[0], ChanelType.V,renderscript);
                    hist.shiftLUT(progressBar1 - 100);
                    //imageEditingCopy = hist.applyLUT(imageEditing); //java version
                    imageEditingCopy = renderscript.applyLUT(Bitmap[0], hist);
                    break;
                case ShiftSaturation:
                    hist = new HistogramManipulation(Bitmap[0], ChanelType.S,renderscript);
                    hist.shiftLUT(progressBar1 - 100);
                    //imageEditingCopy = hist.applyLUT(imageEditing); //java version
                    imageEditingCopy = renderscript.applyLUT(Bitmap[0], hist);
                    break;
                case ShiftColor:
                    hist = new HistogramManipulation(Bitmap[0], ChanelType.H,renderscript);
                    hist.shiftCycleLUT(progressBar1);
                    //imageEditingCopy = hist.applyLUT(imageEditing); //java version
                    imageEditingCopy = renderscript.applyLUT(Bitmap[0], hist);
                    break;
                case Isohelie:
                    imageEditingCopy = renderscript.posterisation(Bitmap[0], progressBar1 + 2);
                    break;
                case Blur:
                    BlurMask mask = new BlurMask(progressBar1 + 1);
                    imageEditingCopy = renderscript.convolution(Bitmap[0], mask);
                    break;
                case Gaussian:
                    GaussianBlur maskGaussian = new GaussianBlur(progressBar1 * 2 + 1, 2.5);
                    imageEditingCopy = renderscript.convolution(Bitmap[0], maskGaussian);
                    break;
                case IncreaseBorder:
                    imageEditingCopy = renderscript.increaseBorder(Bitmap[0], progressBar1 * 10);
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
            imgView.setImageBitmap(bitmap);

            Toast.makeText(ctx, "function end", Toast.LENGTH_SHORT).show();
        }
    }
}
