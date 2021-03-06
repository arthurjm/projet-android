package com.package1.pictureManipulation;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.Type;

import com.android.rssample.ScriptC_applyLUT;
import com.android.rssample.ScriptC_colorize;
import com.android.rssample.ScriptC_convolution;
import com.android.rssample.ScriptC_createHistogram;
import com.android.rssample.ScriptC_grey;
import com.android.rssample.ScriptC_increaseBorder;
import com.android.rssample.ScriptC_invert;
import com.android.rssample.ScriptC_keepHue;
import com.android.rssample.ScriptC_posterisation;
import com.android.rssample.ScriptC_posterisationRGB;
import com.android.rssample.ScriptC_sobel;
import com.package1.pictureManipulation.mask.Mask;

/**
 * Created by amondon001 on 22/02/19.
 * Class with all the renderscript functions and call
 * Those functions don't modify the source bitmap but they return a new one
 * Can be very slow
 */

public class RS {

    private final RenderScript rs;
    private Allocation input; // Source image
    private Allocation output; // Returned image

    public RS(Context ctx) {
        rs = RenderScript.create(ctx);
    }

    /**
     * Initialise les allocations avec l'image passée en paramètre
     *
     * @param bmp Source image
     */
    private void setInputOutput(Bitmap bmp) {
        input = Allocation.createFromBitmap(rs, bmp);
        output = Allocation.createTyped(rs, input.getType());
    }

    /**
     * Change image's hue, between 0 and 360
     *
     * @param bmp Source image
     * @param hue [0;360]
     * @return Modified image
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
     *
     * @param bmp Source image
     * @param hue The hue we want to keep [0;360]
     * @param precision The precision we want [0;360]
     * @return Modified image
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
     * Transform a colored image into a grey image
     *
     * @param bmp Source image
     * @return Modified image
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
     * Invert the colors
     *
     * @param bmp Source image
     * @return Modified image
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
     * Reduce the possible values a pixel can have on channel V
     * Also called Isohelie
     *
     * @param bmp Source image
     * @param depth Number of possible values [2;256]
     * @return Modified image
     */
    public Bitmap posterisation(Bitmap bmp, int depth) {
        setInputOutput(bmp);
        Bitmap res = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

        ScriptC_posterisation script = new ScriptC_posterisation(rs);

        script.set_depth(depth);
        script.invoke_initPosterisation(input, output);
        script.destroy();

        output.copyTo(res);
        return res;
    }

    /**
     * Reduce the possible values a pixel can have on channels R, G and B
     *
     * @param bmp Source image
     * @param depth Number of possible values [2;256]
     * @return Modified image
     */
    public Bitmap posterisationRGB(Bitmap bmp, int depth) {
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
     *
     * @param bmp Source image
     * @param mask The object Mask to apply
     * @return Modified image
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

    /**
     *  Apply both horizontal and vertical Sobel mask to an image using convolution
     *
     * @param bmp Source image
     * @return Modified image
     */
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
     *
     * @param bmp Source image
     * @param precision The precision we want to add black pixels
     * @return Modified image
     */
    public Bitmap increaseBorder(Bitmap bmp, int precision) {
        setInputOutput(bmp);
        Bitmap res = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

        ScriptC_increaseBorder script = new ScriptC_increaseBorder(rs);

        script.set_input(input);
        script.set_width(bmp.getWidth());
        script.set_height(bmp.getHeight());
        script.set_precision(precision);

        script.forEach_increaseBorder(input, output);

        script.destroy();

        output.copyTo(res);
        return res;
    }

    /**
     * Create an histogram on a specified canal
     *
     * @param bmp Source image
     * @param channel R or G or B or H or S or V
     * @return Histogram int tab
     */
    int[] createHistogram(Bitmap bmp, ChanelType channel) {
        setInputOutput(bmp);

        ScriptC_createHistogram script = new ScriptC_createHistogram(rs);

        int[] outputHistogram = new int[256];
        for (int i = 0; i < 256; i++) {
            outputHistogram[i] = 0;
        }
        Allocation histogram = Allocation.createSized(rs, Element.I32(rs), 256);
        histogram.copyFrom(outputHistogram);

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

        histogram.copyTo(outputHistogram);

        histogram.destroy();
        script.destroy();

        return outputHistogram;
    }

    /**
     * Apply a LUT to an image
     *
     * @param bmp Source image
     * @param HM The LUT to use
     * @return Modified image
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
