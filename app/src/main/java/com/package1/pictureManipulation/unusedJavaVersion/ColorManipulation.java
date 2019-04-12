package com.package1.pictureManipulation.unusedJavaVersion;

import android.graphics.Bitmap;
import android.graphics.Color;

public class ColorManipulation {

    /**
     * a list of variable int
     * to create a list of pixels of the image processing
     */
    private int[] tabPixels;
    /**
     * variables of type int
     */
    private int A, R, G, B;

    /**
     * @param index Int
     * @param isA Boolean
     */
    private void convert(int index, boolean isA) {
        if (isA) {
            A = (tabPixels[index] >> 24) & 0xff;
            R = (tabPixels[index] >> 16) & 0xff;
            G = (tabPixels[index] >> 8) & 0xff;
            B = tabPixels[index] & 0xff;
        } else {
            R = (tabPixels[index] >> 16) & 0xff;
            G = (tabPixels[index] >> 8) & 0xff;
            B = tabPixels[index] & 0xff;
        }
    }

    private void passageNuanceGrey() {
        R = ((R * 30) + (G * 59) + B * 11) / 100;
        G = R;
        B = R;
    }


    /**
     * to gray the image
     *
     * @param original the original image
     * @return an image grayed
     */
    public Bitmap convertImageGreyScale(Bitmap original) {

        int width = original.getWidth();
        int height = original.getHeight();
        int index;
        tabPixels = new int[width * height];

        Bitmap res = Bitmap.createBitmap(width, height, original.getConfig());
        original.getPixels(tabPixels, 0, width, 0, 0, width, height);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                index = y * width + x;
                //ici on sépare les informations de la couleur du pixel en les répartissant corectement
                convert(index, true);
                //on applique le passage en nuances de gris selon les proportions données dans le cours
                passageNuanceGrey();
                tabPixels[index] = (A << 24) | (R << 16) | (G << 8) | B;
            }
        }
        res.setPixels(tabPixels, 0, width, 0, 0, width, height);
        return res;
    }


    /**
     * To gray an image except the pixels which are close with the param color
     *
     * @param original the original image
     * @param color    the color chosen
     * @param seuilR int
     * @param seuilG int
     * @param seuilB int
     * @return an image processed
     */
    public Bitmap convertImageSelectiveDesaturation(Bitmap original, int color, int seuilR, int seuilG, int seuilB) {
        int width = original.getWidth();
        int height = original.getHeight();
        int index;
        int colorR = Color.red(color);
        int colorG = Color.green(color);
        int colorB = Color.blue(color);
        tabPixels = new int[width * height];

        Bitmap res = Bitmap.createBitmap(width, height, original.getConfig());
        original.getPixels(tabPixels, 0, width, 0, 0, width, height);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                index = y * width + x;
                //ici on sépare les informations de la couleur du pixel en les repartissant correctement
                convert(index, true);
                //si le pixel n'est pas d'une couleur proche de color , le pixels passe en gris
                if (!(R > (colorR - seuilR) && R < (colorR + seuilR) &&
                        G > (colorG - seuilG) && G < (colorG + seuilG) &&
                        B > (colorB - seuilB) && B < (colorB + seuilB))) {
                    passageNuanceGrey();
                }
                tabPixels[index] = (A << 24) | (R << 16) | (G << 8) | B;
            }
        }
        res.setPixels(tabPixels, 0, width, 0, 0, width, height);
        return res;
    }

    /**
     * To colorize an image
     *
     * @param original  the original image
     * @param chosenHue the color chosen
     * @return an image processed (colorized)
     */
    public Bitmap convertImageColorization(Bitmap original, int chosenHue) {
        int width = original.getWidth();
        int height = original.getHeight();
        int index;
        tabPixels = new int[width * height];
        float[] hsv = new float[3];

        Bitmap res = Bitmap.createBitmap(width, height, original.getConfig());
        original.getPixels(tabPixels, 0, width, 0, 0, width, height);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                index = y * width + x;
                //ici on sépare les informations de la couleur du pixel en les répartissant corectement
                convert(index, false);
                //on passe en HSV puis on modifie le "hue" (la teinte)
                Color.RGBToHSV(R, G, B, hsv);
                hsv[0] = chosenHue;
                tabPixels[index] = Color.HSVToColor(hsv);
            }
        }
        res.setPixels(tabPixels, 0, width, 0, 0, width, height);
        return res;
    }

}
