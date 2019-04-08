package com.package1;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.Type;
import android.util.Log;

import com.android.rssample.ScriptC_applyLUT;
import com.android.rssample.ScriptC_colorize;
import com.android.rssample.ScriptC_convolution;
import com.android.rssample.ScriptC_createHistogram;
import com.android.rssample.ScriptC_grey;
import com.android.rssample.ScriptC_increaseBorder;
import com.android.rssample.ScriptC_invert;
import com.android.rssample.ScriptC_keepHue;
import com.android.rssample.ScriptC_posterisationRGB;
import com.android.rssample.ScriptC_sobel;
import com.package1.Mask.LaplacienMask;
import com.package1.Mask.Mask;
import com.package1.Mask.SobelMask;

/**
 * Created by amondon001 on 22/02/19.
 * Classe regroupant l'appel des noyaux renderscript
 * Les fonctions ne modifient pas l'image passée en paramètre mais renvoient une nouvelle image
 */

public class RS {

    private RenderScript rs;
    private Allocation input; // L'image à traiter
    private Allocation output; // L'image traitée

    public RS(Context ctx) {
        rs = RenderScript.create(ctx);
    }

    /**
     * Initialise les allocations avec l'image passée en paramètre
     * @param bmp
     */
    private void setInputOutput(Bitmap bmp) {
        input = Allocation.createFromBitmap(rs, bmp);
        output = Allocation.createTyped(rs, input.getType());
    }

    /**
     * Change la teinte d'une image à une valeur définie (hue) comprise entre 0 et 359
     * @param bmp
     * @param hue
     * @return a new bitamp
     */
    public Bitmap colorize(Bitmap bmp, int hue) {
        setInputOutput(bmp);
        Bitmap res = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

        ScriptC_colorize script = new ScriptC_colorize(rs);

        script.set_hue(hue);
        script.forEach_colorize(input, output);
        script.destroy();

        output.copyTo(res);
        return res;
    }

    /**
     * Keep a giving hue to an image
     * @param bmp
     * @param hue
     * @param precision
     * @return
     */
    public Bitmap keepHue(Bitmap bmp, int hue, int precision) {
        setInputOutput(bmp);
        Bitmap res = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

        ScriptC_keepHue script = new ScriptC_keepHue(rs);

        script.set_hue(hue);
        script.set_precision(precision);
        script.forEach_keepHue(input, output);
        script.destroy();

        output.copyTo(res);
        return res;
    }

    /**
     * Passe une image couleur en gris
     * @param bmp
     * @return
     */
    public Bitmap toGrey(Bitmap bmp) {
        setInputOutput(bmp);
        Bitmap res = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

        ScriptC_grey script = new ScriptC_grey(rs);

        script.forEach_toGrey(input, output);
        script.destroy();

        output.copyTo(res);
        return res;
    }

    /**
     * Inverse les couleurs d'une image
     * @param bmp
     * @return
     */
    public Bitmap invert(Bitmap bmp) {
        setInputOutput(bmp);
        Bitmap res = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

        ScriptC_invert script = new ScriptC_invert(rs);

        script.forEach_invert(input, output);
        script.destroy();

        output.copyTo(res);
        return res;
    }

    /**
     * Réduit le nombre de valeurs possibles d'une image
     * Peut aussi être appelé Isohélie
     * @param bmp L'image à traiter
     * @param depth Le nombre de valeurs possibles
     * @return L'image traitée
     */
    public Bitmap posterisation(Bitmap bmp, int depth) {
        setInputOutput(bmp);
        Bitmap res = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

        ScriptC_posterisationRGB script = new ScriptC_posterisationRGB(rs);

        script.set_depth(depth);
        script.invoke_initPosterisationRGB(input, output);
        script.destroy();

        output.copyTo(res);
        return res;
    }

    /**
     * Apply a convolution to an image
     * @param bmp
     * @param mask The object Mask to apply
     * @return
     */
    public Bitmap convolution(Bitmap bmp, Mask mask) {
        setInputOutput(bmp);
        Bitmap res = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

        ScriptC_convolution script = new ScriptC_convolution(rs);

        Type.Builder maskType = new Type.Builder(rs, Element.I32(rs));
        maskType.setX(mask.getWidth());
        maskType.setY(mask.getHeight());
        Allocation maskAllocation = Allocation.createTyped(rs, maskType.create());
        maskAllocation.copyFrom(mask.getMask());

        script.set_input(input);
        script.set_width(bmp.getWidth());
        script.set_height(bmp.getHeight());

        script.set_mask(maskAllocation);
        script.set_maskWidth(mask.getWidth());
        script.set_maskHeight(mask.getHeight());
        script.set_weight(mask.getWeight());

        script.forEach_convolution(input, output);

        script.destroy();

        output.copyTo(res);
        return res;
    }

    public Bitmap sobel(Bitmap bmp) {
        setInputOutput(bmp);
        Bitmap res = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

        ScriptC_sobel script = new ScriptC_sobel(rs);

        script.set_input(input);
        script.set_width(bmp.getWidth());
        script.set_height(bmp.getHeight());

        script.forEach_sobel(input, output);

        script.destroy();

        output.copyTo(res);
        return res;
    }

    /**
     * Increase borders adding black pixels
     * @param bmp
     * @param precision The precision we want to add black pixels
     * @return
     */
    public Bitmap increaseBorder(Bitmap bmp, int precision) {
        setInputOutput(bmp);
        Bitmap res = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

        ScriptC_increaseBorder script = new ScriptC_increaseBorder(rs);

        Mask mask = new SobelMask(true);
        Type.Builder maskType = new Type.Builder(rs, Element.I32(rs));
        maskType.setX(mask.getWidth());
        maskType.setY(mask.getHeight());
        Allocation maskAllocation = Allocation.createTyped(rs, maskType.create());
        maskAllocation.copyFrom(mask.getMask());

        script.set_input(input);
        script.set_width(bmp.getWidth());
        script.set_height(bmp.getHeight());

        script.set_mask(maskAllocation);
        script.set_maskWidth(mask.getWidth());
        script.set_maskHeight(mask.getHeight());
        script.set_weight(mask.getWeight());
        script.set_precision(precision);

        script.forEach_increaseBorder(input, output);

        script.destroy();

        output.copyTo(res);
        return res;
    }

    /**
     * Create an histogram on a specified canal
     * @param bmp
     * @param channel R or G or B or H or S or V
     * @return Histogram int tab
     */
    public int[] createHistogram(Bitmap bmp, ChanelType channel) {
        setInputOutput(bmp);
        Bitmap res = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

        ScriptC_createHistogram script = new ScriptC_createHistogram(rs);

        int[] outputHistogram = new int[256];
        for (int i = 0; i < 256; i++) {
            outputHistogram[i] = 0;
        }
        Allocation histogram = Allocation.createSized(rs, Element.U8(rs), 256);
        histogram.copyFrom(histogram);

        int canal = 0;
        if (channel == ChanelType.R) {
            canal = 1;
        }
        if (channel == ChanelType.G) {
            canal = 2;
        }
        if (channel == ChanelType.B) {
            canal = 3;
        }
        if (channel == ChanelType.H) {
            canal = 4;
        }
        if (channel == ChanelType.S) {
            canal = 5;
        }
        if (channel == ChanelType.V) {
            canal = 6;
        }

        script.bind_histogram(histogram);
        script.set_canal(canal);
        script.forEach_createHistogram(input, output);

        script.destroy();
        histogram.destroy();

        histogram.copyTo(outputHistogram);
        return outputHistogram;
    }

    /**
     * Applique une LUT passée en paramètre à une image passée en paramètre
     * @param bmp
     * @param HM
     * @return
     */
    public Bitmap applyLUT(Bitmap bmp, HistogramManipulation HM) {
        setInputOutput(bmp);
        Bitmap res = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

        ScriptC_applyLUT script = new ScriptC_applyLUT(rs);

        Allocation LUT = Allocation.createSized(rs, Element.I32(rs), HM.LUT.length);
        LUT.copyFrom(HM.LUT);

        int canal = 0;
        if (HM.histogram.getChanel() == ChanelType.R) {
            canal = 1;
        }
        if (HM.histogram.getChanel() == ChanelType.G) {
            canal = 2;
        }
        if (HM.histogram.getChanel() == ChanelType.B) {
            canal = 3;
        }
        if (HM.histogram.getChanel() == ChanelType.H) {
            canal = 4;
        }
        if (HM.histogram.getChanel() == ChanelType.S) {
            canal = 5;
        }
        if (HM.histogram.getChanel() == ChanelType.V) {
            canal = 6;
        }
        script.set_LUT(LUT);
        script.set_canal(canal);
        script.forEach_applyLUT(input, output);

        script.destroy();

        output.copyTo(res);
        return res;
    }
}
