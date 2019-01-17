package com.package1;

import android.graphics.Bitmap;
import android.graphics.Color;


public class Histogram {

    //l'histogramme de la "value" tirée de la couleur HSV (passé sur une plage de 0 à 255)
    private int histogramValue[] = new int[256];
    private int LUT[] = new int[256];
    private int min, max, count;


    private void histogramSetup(Bitmap bitmap) {
        for (int i = 0; i < 256; i++) {
            histogramValue[i] = 0;
            LUT[i] = i;
        }
        count = 0;
        min = 256;
        max = -1;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int R, G, B, index, valueTemp;
        int[] tabPixels = new int[width * height];
        float[] hsv = new float[3];
        bitmap.getPixels(tabPixels, 0, width, 0, 0, width, height);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                index = y * width + x;
                R = (tabPixels[index] >> 16) & 0xff;
                G = (tabPixels[index] >> 8) & 0xff;
                B = tabPixels[index] & 0xff;
                Color.RGBToHSV(R, G, B, hsv);
                valueTemp = (int) (255 * hsv[2]);
                histogramValue[valueTemp]++;
                count++;
                if (valueTemp < min) {
                    min = valueTemp;
                }
                if (valueTemp > max) {
                    max = valueTemp;
                }
            }
        }
    }


    private void linearExtensionLUT() {
        if (max != min) {
            for (int i = 0; i < 256; i++) {
                LUT[i] = (255 * (i - min)) / (max - min);
            }
        }
    }

    // Version de création d'une LUT pour egaliser un histogramme en commencant par les valeurs les plus faibles
    //non utilisé, voir rapport
    /*private void histogramEqLUTLeft() {
        int average = count / 256;
        int tempCount = 0, nextValue = 0;
        for (int i = 0; i < 256; i++) {
            LUT[i] = nextValue;
            tempCount += histogramValue[i];
            if ((tempCount/average) > 1) {
                nextValue += (tempCount/average);
                if(nextValue>255){
                    nextValue=255;
                }
                tempCount = 0;
            }
        }
    }*/
    // Version de création d'une LUT pour egaliser un histogramme en commencant par les valeurs les plus grandes
    private void histogramEqLUTRight() {
        int average = count / 256;
        int tempCount = 0, nextValue = 255;
        for (int i = 0; i < 256; i++) {
            LUT[255 - i] = nextValue;
            tempCount += histogramValue[255 - i];
            if ((tempCount / average) > 1) {
                nextValue -= (tempCount / average);
                if (nextValue < 0) {
                    nextValue = 0;
                }
                tempCount = 0;
            }
        }
    }

    public Bitmap applicationEqHistogram(Bitmap original) {
        histogramSetup(original);
        histogramEqLUTRight();
        int width = original.getWidth();
        int height = original.getHeight();
        Bitmap res = Bitmap.createBitmap(width, height, original.getConfig());
        int R, G, B, index, valueTemp;
        int[] tabPixels = new int[width * height];
        float[] hsv = new float[3];
        original.getPixels(tabPixels, 0, width, 0, 0, width, height);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                index = y * width + x;
                R = (tabPixels[index] >> 16) & 0xff;
                G = (tabPixels[index] >> 8) & 0xff;
                B = tabPixels[index] & 0xff;
                Color.RGBToHSV(R, G, B, hsv);
                valueTemp = (int) (255 * hsv[2]);
                hsv[2] = (float) LUT[valueTemp] / 255;
                tabPixels[index] = Color.HSVToColor(hsv);
            }
        }
        res.setPixels(tabPixels, 0, width, 0, 0, width, height);
        return res;
    }

    public Bitmap applicationLinearExtension(Bitmap original) {
        histogramSetup(original);
        linearExtensionLUT();
        int width = original.getWidth();
        int height = original.getHeight();
        Bitmap res = Bitmap.createBitmap(width, height, original.getConfig());
        int R, G, B, index, valueTemp;
        int[] tabPixels = new int[width * height];
        float[] hsv = new float[3];
        original.getPixels(tabPixels, 0, width, 0, 0, width, height);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                index = y * width + x;
                R = (tabPixels[index] >> 16) & 0xff;
                G = (tabPixels[index] >> 8) & 0xff;
                B = tabPixels[index] & 0xff;
                Color.RGBToHSV(R, G, B, hsv);
                valueTemp = (int) (255 * hsv[2]);
                hsv[2] = (float) LUT[valueTemp] / 255;
                tabPixels[index] = Color.HSVToColor(hsv);
            }
        }
        res.setPixels(tabPixels, 0, width, 0, 0, width, height);
        return res;
    }

}
