package com.package1;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.renderscript.ScriptIntrinsicColorMatrix;
import android.renderscript.ScriptIntrinsicConvolve3x3;
import android.renderscript.ScriptIntrinsicConvolve5x5;
import android.renderscript.Type;
import android.util.Log;

import com.android.rssample.ScriptC_colorize;
import com.android.rssample.ScriptC_convolution;
import com.android.rssample.ScriptC_grey;
import com.android.rssample.ScriptC_keepHue;
import com.package1.Mask.Mask;

/**
 * Created by amondon001 on 22/02/19.
 */

public class RS {

    RenderScript rs;
    Allocation input;
    Allocation output;

    public RS(Context ctx) {
        rs = RenderScript.create(ctx);
    }

    private void setInputOutput(Bitmap bmp) {
        input = Allocation.createFromBitmap(rs, bmp);
        output = Allocation.createTyped(rs, input.getType());
    }

    /**
     * Change la teinte d'une image à une valeur définie (hue) comprise entre 0 et 359
     * @param bmp
     * @param hue
     * @return
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
     * Apply a convolution to an image
     * @param bmp
     * @param mask
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

        script.forEach_test(input, output);


        script.destroy();

        output.copyTo(res);
        return res;
    }
}
