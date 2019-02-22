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

    public void linearExtensionLUT(int newMaxValue, int newMinValue) {
        if (newMaxValue>=NumberofValues){
            newMaxValue=NumberofValues-1;
        }
        if (newMaxValue==newMinValue){
            newMinValue--;
        }else if(newMaxValue<newMinValue){
            int temp=newMaxValue;
            newMaxValue=newMinValue;
            newMinValue=temp;
        }
        for (int i = 0; i < NumberofValues; i++) {
            LUT[i] = newMinValue+((newMaxValue-newMinValue) * (i - histogram.getMin())) / (histogram.getMax() - histogram.getMin());
        }
    }

    public void shiftLut(int newStartValue){

    }

    /**
     * this function will reduce the number of values that can be taken by the values stored in the histogram
     * @param depth
     *      the number of values that the stored values in the histogram will take
     */
    public void isophelieLut(int depth) {
        int tempDepth = 1;
        int nextValue = 0;
        int nbofValues =1;
        for (int i = 0; i < NumberofValues; i++) {
            if ((i >=  (tempDepth * ((NumberofValues-1) / depth))) && nbofValues<depth) {//we need to be sure that we still have that we need to process (so that nbofValues<depth
                tempDepth++;
                nextValue += ((NumberofValues - 1) / (depth - 1));
                nbofValues++;
            }
            LUT[i] = nextValue;
        }
    }

    // Version de création d'une LUT pour egaliser un histogramme en commencant par les valeurs les plus faibles
    //non utilisé, voir rapport
    public void histogramEqLUTLeft() {
        int average = histogram.getAverage();
        int tempCount = 0, nextValue = 0;
        for (int i = 0; i < NumberofValues; i++) {
            LUT[i] = nextValue;
            tempCount += histogram.getHistogramValue(i);;
            if ((tempCount/average) > 1) {
                nextValue += (tempCount/average);
                if(nextValue>NumberofValues-1){
                    nextValue=NumberofValues-1;
                }
                tempCount = 0;
            }
        }
    }

    // Version de création d'une LUT pour egaliser un histogramme en commencant par les valeurs les plus grandes
    public void LUThistogramEq() {
        int average = histogram.getAverage();
        int tempCount = 0, nextValue = NumberofValues - 1;
        for (int i = 0; i < NumberofValues; i++) {
            LUT[NumberofValues -1 - i] = nextValue;
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
                        hsv[1] = (float) LUT[(int) (hsv[1] * (NumberofValues - 1))] / (float)(NumberofValues - 1);//only multiplied because "saturation" goes from 0 to 1
                    } else {
                        hsv[2] = (float) LUT[(int) (hsv[2] * (NumberofValues - 1))] / (float)(NumberofValues - 1);//only multiplied because "value" goes from 0 to 1
                    }
                } else {
                    hsv[0] = (float) LUT[(int) (hsv[0] * (NumberofValues - 1) / 359)] * 359 / (NumberofValues - 1);//divided by 359 because the "hue" can take 360 values
                }
                tab[i] = Color.HSVToColor(hsv);
            } else {
                if (ct == ChanelType.R) {
                    R = LUT[R * (NumberofValues - 1) / 255];    //divided by 255 because the "hue" can take 256 values
                } else if (ct == ChanelType.G) {
                    G = LUT[G * (NumberofValues - 1) / 255];    //divided by 255 because the "hue" can take 256 values
                } else {
                    B = LUT[B * (NumberofValues - 1) / 255];    //divided by 255 because the "hue" can take 256 values
                }
                tab[i] = ((255 << 24) | (R << 16) | (G << 8) | B); //the pixel is stocked in ARGB format
            }
        }
        Bitmap resBitmap = Bitmap.createBitmap(original.getWidth(), original.getHeight(), original.getConfig());
        resBitmap.setPixels(tab, 0, original.getWidth(), 0, 0, original.getWidth(), original.getHeight());
        return resBitmap;
    }
}
