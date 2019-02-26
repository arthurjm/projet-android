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
     * Intrinsic
     * Gaussian blur
     * @param bmp
     * @param radius
     */
    public Bitmap gaussianBlur(Bitmap bmp, float radius) {
        setInputOutput(bmp);
        Bitmap res = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

        ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        script.setInput(input);
        script.setRadius(radius);
        script.forEach(output);
        script.destroy();

        output.copyTo(res);
        return res;
    }

    public Bitmap gaussianBlur(Bitmap bmp) {
        return gaussianBlur(bmp, 25);
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
     * Intrinsic
     * Passe une image couleur en gris
     * @param bmp
     * @return
     */
    public Bitmap toGrey(Bitmap bmp) {
        setInputOutput(bmp);
        Bitmap res = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

        ScriptIntrinsicColorMatrix script = ScriptIntrinsicColorMatrix.create(rs);

        script.setGreyscale();
        script.forEach(input, output);
        script.destroy();

        output.copyTo(res);
        return res;
    }

    /**
     * Intrinsic
     * @param bmp
     * @param v
     * @return
     */
    public Bitmap convolution3x3(Bitmap bmp, float[] v) {
        setInputOutput(bmp);
        Bitmap res = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

        ScriptIntrinsicConvolve3x3 script = ScriptIntrinsicConvolve3x3.create(rs, Element.U8_4(rs));

        script.setCoefficients(v);
        script.setInput(input);
        script.forEach(output);
        script.destroy();

        output.copyTo(res);
        return res;
    }

    /**
     * Intrinsic
     * @param bmp
     * @param v
     * @return
     */
    public Bitmap convolution5x5(Bitmap bmp, float[] v) {
        setInputOutput(bmp);
        Bitmap res = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

        ScriptIntrinsicConvolve5x5 script = ScriptIntrinsicConvolve5x5.create(rs, Element.U8_4(rs));

        script.setCoefficients(v);
        script.setInput(input);
        script.forEach(output);

        script.destroy();

        output.copyTo(res);
        return res;
    }

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

    /**
     *
     * Prewitt
     float[] Prewitt3x3_h1 = new float[]{
            -1f, 0f, 1f,
            -1f, 0f, 1f,
            -1f, 0f, 1f};

    float[] Prewitt3x3_h2 = new float[]{
            -1f, -1f, -1f,
            0f, 0f, 0f,
            1f, 1f, 1f};

    * Sobel
    float[] Sobel3x3_h1 = new float[]{
            -1f, 0f, 1f,
            -2f, 0f, 2f,
            -1f, 0f, 1f};

    float[] Sobel3x3_h2 = new float[]{
            -1f, -2f, -1f,
            0f, 0f, 0f,
            1f, 2f, 1f};

    * Laplacien
    float[] Laplacien3x3_4cx = new float[]{
            0f, 1f, 0f,
            1f, -4f, 1f,
            0f, 1f, 0f};

    float[] Laplacien3x3_8cx = new float[]{
            1f, 1f, 1f,
            1f, -8f, 1f,
            1f, 1f, 1f};

     **/
}
