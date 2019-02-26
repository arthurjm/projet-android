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

import com.android.rssample.ScriptC_colorize;

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

    /**
     * Gaussian blur
     * @param bmp
     * @param radius
     */
    public Bitmap gaussianBlur(Bitmap bmp, float radius) {
        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());

        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        theIntrinsic.setInput(input);
        theIntrinsic.setRadius(radius);
        theIntrinsic.forEach(output);
        output.copyTo(bmp);

        input.destroy(); output.destroy();
        theIntrinsic.destroy(); rs.destroy();
        return bmp;
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
        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());

        ScriptC_colorize colorizeScript = new ScriptC_colorize(rs);

        colorizeScript.set_hue(hue);

        colorizeScript.forEach_colorize(input, output);

        output.copyTo(bmp);

        input.destroy(); output.destroy();
        colorizeScript.destroy(); rs.destroy();

        return bmp;
    }


    /**
     * Passe une image couleur en gris
     * @param bmp
     * @return
     */
    public Bitmap toGrey(Bitmap bmp) {
        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());

        ScriptIntrinsicColorMatrix theIntrinsic = ScriptIntrinsicColorMatrix.create(rs);

        theIntrinsic.setGreyscale();
        theIntrinsic.forEach(input, output);

        output.copyTo(bmp);

        input.destroy(); output.destroy();
        theIntrinsic.destroy(); rs.destroy();
        return bmp;
    }

    public Bitmap convolution3x3(Bitmap bmp, float[] v) {
        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());

        ScriptIntrinsicConvolve3x3 theIntrinsic = ScriptIntrinsicConvolve3x3.create(rs, Element.U8_4(rs));

        theIntrinsic.setCoefficients(v);
        theIntrinsic.setInput(input);
        theIntrinsic.forEach(output);

        output.copyTo(bmp);

        input.destroy(); output.destroy();
        theIntrinsic.destroy(); rs.destroy();
        return bmp;
    }

    public Bitmap convolution5x5(Bitmap bmp, float[] v) {
        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());

        ScriptIntrinsicConvolve5x5 theIntrinsic = ScriptIntrinsicConvolve5x5.create(rs, Element.U8_4(rs));

        theIntrinsic.setCoefficients(v);
        theIntrinsic.setInput(input);
        theIntrinsic.forEach(output);

        output.copyTo(bmp);

        input.destroy(); output.destroy();
        theIntrinsic.destroy(); rs.destroy();
        return bmp;
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
