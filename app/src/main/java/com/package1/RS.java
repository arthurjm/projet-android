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
import com.android.rssample.ScriptC_invert;
import com.android.rssample.ScriptC_keepHue;

import java.util.Random;

/**
 * Created by amondon001 on 22/02/19.
 */

public class RS {

    Context ctx;

    public RS(Context ctx) {
        this.ctx = ctx;
    }

    /**
     * Gaussian blur
     *
     * @param bmp
     * @param radius
     */
    public Bitmap gaussianBlur(Bitmap bmp, float radius) {
        RenderScript rs = RenderScript.create(ctx);

        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());

        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        theIntrinsic.setInput(input);
        theIntrinsic.setRadius(radius);
        theIntrinsic.forEach(output);
        output.copyTo(bmp);

        input.destroy();
        output.destroy();
        theIntrinsic.destroy();
        rs.destroy();
        return bmp;
    }

    public Bitmap gaussianBlur(Bitmap bmp) {
        return gaussianBlur(bmp, 25);
    }

    /**
     * Change la teinte d'une image à une valeur définie (hue) comprise entre 0 et 359
     *
     * @param bmp
     * @param hue
     * @return
     */
    /*public Bitmap colorize(Bitmap bmp, int hue) {
        RenderScript rs = RenderScript.create(ctx);

        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());

        ScriptC_colorize colorizeScript = new ScriptC_colorize(rs);

        colorizeScript.set_hue(hue);

        colorizeScript.forEach_colorize(input, output);

        output.copyTo(bmp);

        input.destroy();
        output.destroy();
        colorizeScript.destroy();
        rs.destroy();

        return bmp;
    }*/

    /**
     * Passe une image couleur en gris
     *
     * @param bmp
     * @return
     */
    public Bitmap toGrey(Bitmap bmp) {
        RenderScript rs = RenderScript.create(ctx);

        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());

        ScriptIntrinsicColorMatrix theIntrinsic = ScriptIntrinsicColorMatrix.create(rs);

        theIntrinsic.setGreyscale();
        theIntrinsic.forEach(input, output);

        output.copyTo(bmp);

        input.destroy();
        output.destroy();
        theIntrinsic.destroy();
        rs.destroy();
        return bmp;
    }

    public Bitmap convolution3x3(Bitmap bmp, float[] v) {
        RenderScript rs = RenderScript.create(ctx);

        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());

        ScriptIntrinsicConvolve3x3 theIntrinsic = ScriptIntrinsicConvolve3x3.create(rs, Element.U8_4(rs));

        theIntrinsic.setCoefficients(v);
        theIntrinsic.setInput(input);
        theIntrinsic.forEach(output);

        output.copyTo(bmp);

        input.destroy();
        output.destroy();
        theIntrinsic.destroy();
        rs.destroy();
        return bmp;
    }

    public Bitmap convolution5x5(Bitmap bmp, float[] v) {
        RenderScript rs = RenderScript.create(ctx);

        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());

        ScriptIntrinsicConvolve5x5 theIntrinsic = ScriptIntrinsicConvolve5x5.create(rs, Element.U8_4(rs));

        theIntrinsic.setCoefficients(v);
        theIntrinsic.setInput(input);
        theIntrinsic.forEach(output);

        output.copyTo(bmp);

        input.destroy();
        output.destroy();
        theIntrinsic.destroy();
        rs.destroy();
        return bmp;
    }

   /* public Bitmap invert(Bitmap bmp) {

        // 1) Creer un contexte RenderScript
        RenderScript rs = RenderScript.create(ctx);

        // 2) Creer des Allocations pour passer les donnees
        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());

        // 3) Creer le script
        ScriptC_invert invertScript = new ScriptC_invert(rs);

        // 4) Copier les donnees dans les Allocations
        // ...
        // 5) Initialiser les variables globales potentielles
        // ...
        // 6) Lancer le noyau

        invertScript.forEach_invert(input, output);

        // 7) Recuperer les donnees des Allocation (s)
        output.copyTo(bmp);

        // 8) Detruire le context , les Allocation (s) et le script
        input.destroy();
        output.destroy();
        invertScript.destroy();
        rs.destroy();
        return bmp;
    }*/

    /*private void keepHue(Bitmap bmp, int hue, int precision) {
        RenderScript rs = RenderScript.create(this);

        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());

        ScriptC_keepHue keepHueScript = new ScriptC_keepHue(rs);

        keepHueScript.set_hue(hue*10);
        keepHueScript.set_precision(precision*10);
        keepHueScript.forEach_keepHue(input, output);

        output.copyTo(bmp);

        input.destroy(); output.destroy();
        keepHueScript.destroy(); rs.destroy();
    }*/

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