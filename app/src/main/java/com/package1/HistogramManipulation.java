package com.package1;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

public class HistogramManipulation {

    static private int NumberofValues = 256;

    int[] LUT = new int[NumberofValues];
    Histogram histogram;
    int R, G, B;

    public HistogramManipulation(Bitmap bitmap, ChanelType chanel) {
        int[] tab = new int[bitmap.getWidth() * bitmap.getHeight()];
        tab = getTab(bitmap, chanel);
        histogram = new Histogram(tab, chanel);
        Log.e("TAG", "" + getTab(bitmap, chanel).length);
    }

    private int[] getTab(Bitmap bitmap, ChanelType ct) {
        int[] tab = new int[bitmap.getWidth() * bitmap.getHeight()];
        bitmap.getPixels(tab, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        float[] hsv = new float[3];
        for (int i = 0; i < tab.length; i++) {
            convert(i, tab);
            //  Log.e("TAG","rgb : "+R+"\t"+G+"\t"+B);
            if (ct == ChanelType.V || ct == ChanelType.S || ct == ChanelType.H) {
                Color.RGBToHSV(R, G, B, hsv);
                if (ct == ChanelType.V || ct == ChanelType.S) {
                    if (ct == ChanelType.S) {
                        tab[i] = (int) (hsv[1] * (NumberofValues - 1));//only multiplied because "saturation" goes from 0 to 1
                    } else {
                        tab[i] = (int) (hsv[2] * (NumberofValues - 1));//only multiplied because "value" goes from 0 to 1
                    }
                } else {
                    tab[i] = (int) (hsv[0] * (NumberofValues - 1) / 359);//divided by 359 because the "hue" can take 360 values
                }
            } else {
                if (ct == ChanelType.R) {
                    tab[i] = R * (NumberofValues - 1) / 255;    //divided by 255 because the "hue" can take 256 values
                } else if (ct == ChanelType.G) {
                    tab[i] = G * (NumberofValues - 1) / 255;    //divided by 255 because the "hue" can take 256 values
                } else {
                    tab[i] = B * (NumberofValues - 1) / 255;    //divided by 255 because the "hue" can take 256 values
                }
            }
            //Log.e("TAG","test : "+tab[i]);
        }
        return tab;
    }


    public void convert(int index, int[] tabPixels) {
        R = (tabPixels[index] >> 16) & 0xff;
        G = (tabPixels[index] >> 8) & 0xff;
        B = tabPixels[index] & 0xff;
    }


    //a changer
    public void linear3segmentExtensionLUT(int startingValue, int segment1value, int segment2value, int endvalue, int segment1Indice, int segment2Indice) {
        if (segment1value != segment2value && segment1value != startingValue && segment2value != endvalue) {
            for (int i = 0; i < segment1Indice; i++) {
                LUT[i] = startingValue + ((segment1value - startingValue) * (i - startingValue)) / (segment1value - startingValue);
            }
            for (int i = segment1Indice; i < segment2Indice; i++) {
                LUT[i] = segment1value + ((segment2value - segment1value) * (i - segment1value)) / (segment2value - segment1value);
            }
            for (int i = segment2Indice; i < 256; i++) {
                LUT[i] = ((endvalue - segment2value) * (i - segment2value)) / (endvalue - segment2value);
            }
        }
    }

    public void linearExtensionLUT() {
        if (histogram.getMax() != histogram.getMin()) {
            for (int i = 0; i < NumberofValues; i++) {
                LUT[i] = (NumberofValues * (i - histogram.getMin())) / (histogram.getMax() - histogram.getMin());
            }
        }
    }

    public void isophelieLut(int depth) {
        int tempDepth = 1;
        int tempSeuil = 0;
        int nextValue = 0;
        for (int i = 0; i < NumberofValues; i++) {
            if (i >= tempSeuil + (tempDepth * (NumberofValues / depth))) {
                tempSeuil += tempDepth * (NumberofValues / depth);
                tempDepth++;
                nextValue += (NumberofValues - 1) / (depth - 1);
            }
            LUT[i] = nextValue;
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
    private void LUThistogramEq() {
        int average = histogram.getAverage();
        int tempCount = 0, nextValue = NumberofValues - 1;
        for (int i = 0; i < NumberofValues; i++) {
            LUT[255 - i] = nextValue;
            tempCount += histogram.getHistogramValue(NumberofValues - 1 - i);
            if ((tempCount / average) > 1) {
                nextValue -= (tempCount / average);
                if (nextValue < 0) {
                    nextValue = 0;
                }
                tempCount = 0;
            }
        }
    }

    public Bitmap applyLut(Bitmap original) {
        ChanelType ct = histogram.getChanel();
        int[] tab = new int[original.getWidth() * original.getHeight()];
        original.getPixels(tab, 0, original.getWidth(), 0, 0, original.getWidth(), original.getHeight());
        float[] hsv = new float[3];
        for (int i = 0; i < tab.length; i++) {
            convert(i, tab);
            if (ct == ChanelType.V || ct == ChanelType.S || ct == ChanelType.H) {
                Color.RGBToHSV(R, G, B, hsv);
                if (ct == ChanelType.V || ct == ChanelType.S) {
                    if (ct == ChanelType.S) {
                        // Log.e("TAG","erreur");
                        hsv[1] = (float) (LUT[(int) (hsv[1] * (NumberofValues - 1))] / (NumberofValues - 1));//only multiplied because "saturation" goes from 0 to 1
                    } else {
                        //   Log.e("TAG","erreur");
                        hsv[2] = (float) (LUT[(int) (hsv[2] * (NumberofValues - 1))] / (NumberofValues - 1));
                        ;//only multiplied because "value" goes from 0 to 1
                    }
                } else {
                    // Log.e("TAG","test : "+(int)(hsv[0]*(NumberofValues-1)/359)+"\n"+LUT[(int)(hsv[0]*(NumberofValues-1)/359)]);
                    hsv[0] = (float) (LUT[(int) (hsv[0] * (NumberofValues - 1) / 359)] * 359 / (NumberofValues - 1));
                    ;//divided by 359 because the "hue" can take 360 values
                }
                tab[i] = Color.HSVToColor(hsv);
            } else {
                if (ct == ChanelType.R) {
                    //    Log.e("TAG","erreur");
                    R = LUT[R * (NumberofValues - 1) / 255];    //divided by 255 because the "hue" can take 256 values
                } else if (ct == ChanelType.G) {
                    // Log.e("TAG","erreur");
                    G = LUT[G * (NumberofValues - 1) / 255];    //divided by 255 because the "hue" can take 256 values
                } else {
                    //Log.e("TAG","erreur");
                    B = LUT[B * (NumberofValues - 1) / 255];    //divided by 255 because the "hue" can take 256 values
                }
                tab[i] = ((R << 16) | (G << 8) | B);
            }
        }
        Bitmap resBitmap = Bitmap.createBitmap(original.getWidth(), original.getHeight(), original.getConfig());
        resBitmap.setPixels(tab, 0, original.getWidth(), 0, 0, original.getWidth(), original.getHeight());
        return resBitmap;
    }

    /*
    public Bitmap applicationEqHistogram(Bitmap original) {
        histogramSetup(original);
        histogramEqLUTRight();
        int width = original.getWidth();
        int height = original.getHeight();
        Bitmap res = Bitmap.createBitmap(width, height, original.getConfig());
        int index, valueTemp;
        tabPixels = new int[width * height];
        float[] hsv = new float[3];
        original.getPixels(tabPixels, 0, width, 0, 0, width, height);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                index = y * width + x;
                convert(index);
                Color.RGBToHSV(R, G, B, hsv);
                valueTemp = (int) (255 * hsv[2]);
                hsv[2] = (float) LUT[valueTemp] / 255;
                tabPixels[index] = Color.HSVToColor(hsv);
            }
        }
        res.setPixels(tabPixels, 0, width, 0, 0, width, height);
        return res;
    }

    public Bitmap applicationLinearExtension(Bitmap original, float newHistogramSpan) {
        histogramSetup(original);
        linearExtensionLUT();
        int width = original.getWidth();
        int height = original.getHeight();
        Bitmap res = Bitmap.createBitmap(width, height, original.getConfig());
        int index, valueTemp;
        tabPixels = new int[width * height];
        float[] hsv = new float[3];
        original.getPixels(tabPixels, 0, width, 0, 0, width, height);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                index = y * width + x;
                convert(index);
                Color.RGBToHSV(R, G, B, hsv);
                valueTemp = (int) (255 * hsv[2]);
                hsv[2] = (float) LUT[valueTemp] / 255;
                tabPixels[index] = Color.HSVToColor(hsv);
            }
        }
        res.setPixels(tabPixels, 0, width, 0, 0, width, height);
        return res;
    }
*/
}
