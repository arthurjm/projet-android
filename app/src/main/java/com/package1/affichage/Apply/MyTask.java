package com.package1.affichage.Apply;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.Toast;

import com.package1.ChanelType;
import com.package1.HistogramManipulation;
import com.package1.Mask.BlurMask;
import com.package1.Mask.GaussianBlur;
import com.package1.affichage.Type.FilterType;

import static com.package1.MainActivity.imageEditingCopy;
import static com.package1.MainActivity.imgView;
import static com.package1.affichage.PhotoEditing.hist;
import static com.package1.affichage.PhotoEditing.renderscript;

public class MyTask extends AsyncTask<Bitmap, Void, Bitmap> {

    public Context ctx;
    public int progressBar1, progressBar2;
    public FilterType filterType;

    public MyTask(Context ctx, int progressBar1, int progressBar2, FilterType filterType){
        this.ctx = ctx;
        this.progressBar1 = progressBar1;
        this.progressBar1 = progressBar2;
        this.filterType = filterType;

    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Toast.makeText(ctx, "  fonction start", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Bitmap doInBackground(Bitmap... Bitmap) {
        switch (filterType) {
            case EquaLight:
                hist = new HistogramManipulation(Bitmap[0], ChanelType.V,renderscript);
                hist.equalizationLUT();
                imageEditingCopy = hist.applyLUT(Bitmap[0]);
                break;

            case Colorize:
                imageEditingCopy = (renderscript.colorize(Bitmap[0], progressBar1));
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

    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        imgView.setImageBitmap(bitmap);
        Toast.makeText(ctx, "  fonction end", Toast.LENGTH_SHORT).show();
    }
}