package com.package1;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * This class will be used to build histogram of chosen chanel on a bitmap, and allow to change it with LUT.
 */
public class HistogramManipulation {

    /**
     * The Number of values that can be taken by the values of the histogram, must be the same value as in the "Histogram" class
     */
    static private int NumberofValues = 256;

    int[] LUT = new int[NumberofValues];
    Histogram histogram;
    int R, G, B;

    /**
     * Constructor taking a bitmap and making the histogram associated with the selected chanel
     *
     * @param bitmap Bitmap from which we will get the histogram
     * @param chanel The selected chanel
     */
    public HistogramManipulation(Bitmap bitmap, ChanelType chanel) {
        int[] tab = new int[bitmap.getWidth() * bitmap.getHeight()];
        tab = getTab(bitmap, chanel);
        histogram = new Histogram(tab, chanel);
    }

    /**
     * Function used in order to get an array that contains the values on which we want an histogram on.
     * This function allows to get the values on the right format depending on the origin (which chanel they are from).
     *
     * @param bitmap The bitmap form which the values are extracted.
     * @param ct     The chanel (from "ChanelType") on which we wish to get the histogram.
     * @return The array with the values of the chanel selected, taken from the bitmap at the right format needed to get an histogram on.
     */
    private int[] getTab(Bitmap bitmap, ChanelType ct) {
        int[] tab = new int[bitmap.getWidth() * bitmap.getHeight()];
        bitmap.getPixels(tab, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        float[] hsv = new float[3];
        for (int i = 0; i < tab.length; i++) {
            convert(i, tab);
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
        }
        return tab;
    }

    public Histogram getHistogram() {
        return histogram;
    }

    public void convert(int index, int[] tabPixels) {
        R = (tabPixels[index] >> 16) & 0xff;
        G = (tabPixels[index] >> 8) & 0xff;
        B = tabPixels[index] & 0xff;
    }


    /**
     * Make a linear extension of the values of the histogram with specific values for the minimum and maximum.
     * Can be used to increase or decrease contrast for example.
     *
     * @param MaxValue the new maximum value
     * @param MinValue the new minimum value
     */
    public void linearExtensionLUT(int MaxValue, int MinValue) {
        if (MaxValue >= NumberofValues) {
            MaxValue = NumberofValues - 1;
        }
        if (MaxValue == MinValue) {
            MinValue--;
        } else if (MaxValue < MinValue) {
            int temp = MaxValue;
            MaxValue = MinValue;
            MinValue = temp;
        }
        for (int i = 0; i < NumberofValues; i++) {
            LUT[i] = MinValue + ((MaxValue - MinValue) * (i - histogram.getMin())) / (histogram.getMax() - histogram.getMin());
            if (LUT[i] > 255) {
                LUT[i] = 255;
            } else if (LUT[i] < 0) {
                LUT[i] = 0;
            }
        }
    }

    /**
     * Function used to make a shift with a cyclic chanel ("hue" from HSV for example).
     *
     * @param shiftValue value by which the shift is made
     */
    public void shiftCycleLUT(int shiftValue) {
        for (int i = 0; i < NumberofValues; i++) {
            LUT[i] = (i + shiftValue) % NumberofValues;
        }
    }

    /**
     * Function used to make a shift up for all values in the histogram,
     * the shift can not be greater than (NumberofValues/2), or lower than(NumberofValues/2)
     * if the shift make a value bigger than the maximum (NuberofValues), it get the maximum value instead,
     * if the shift make a value lower than the minimum (0), it get the minimum value instead.
     *
     * @param shiftValue value by which the shift is made
     */
    public void shiftLUT(int shiftValue) {
        if (shiftValue > NumberofValues / 2) {
            shiftValue = NumberofValues / 2;
        } else if (shiftValue < (-NumberofValues) / 2) {
            shiftValue = (-NumberofValues) / 2;
        }
        for (int i = 0; i < NumberofValues; i++) {
            if (i + shiftValue > NumberofValues - 1) {
                LUT[i] = NumberofValues - 1;
            } else if (i + shiftValue < 0) {
                LUT[i] = 0;
            } else {
                LUT[i] = i + shiftValue;
            }
        }
    }

    /**
     * This function will reduce the number of values that can be taken by the values stored in the histogram.
     * May also be called "Posterisation".
     * the depth must be at least 2 and if greater than NumberofValues/5, the effect will not be seen.
     *
     * @param depth the number of values that the stored values in the histogram will take
     */
    public void isohelieLUT(int depth) {
        if(depth<2){
            depth=2;
        }else if(depth>NumberofValues/5){
            depth=NumberofValues/5;
        }
        int tempDepth = 1;
        int nextValue = 0;
        int nbofValues = 1;
        for (int i = 0; i < NumberofValues; i++) {
            if ((i >= (tempDepth * ((NumberofValues - 1) / depth))) && nbofValues < depth) {//we need to be sure that we still have that we need to process (so that nbofValues<depth
                tempDepth++;
                nextValue += ((NumberofValues - 1) / (depth - 1));
                nbofValues++;
            }
            LUT[i] = nextValue;
        }
    }


    /**
     * Function used to "equalize" the histogram.
     */
    public void equalizationLUT() {
        int average = histogram.getAverage();
        int tempCount = 0, nextValue = 0;
        for (int i = 0; i < NumberofValues; i++) {
            LUT[i] = nextValue;
            tempCount += histogram.getHistogramValue(i);

            if ((tempCount / average) > 1) {
                nextValue += (tempCount / average);
                if (nextValue > NumberofValues - 1) {
                    nextValue = NumberofValues - 1;
                }
                tempCount = 0;
            }
        }
    }

    public void LUThistogramEq() {
        int average = histogram.getAverage();
        int tempCount = 0, nextValue = NumberofValues - 1;
        for (int i = 0; i < NumberofValues; i++) {
            LUT[NumberofValues - 1 - i] = nextValue;
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

    /**
     * Function that apply the LUT stored to the bitmap in parameter
     *
     * @param original The bitmap on which the LUT is applied
     * @return The new Bitmap corresponding at "original" with the changed of the LUT applied
     */
    public Bitmap applyLUT(Bitmap original) {
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
                        hsv[1] = (float) LUT[(int) (hsv[1] * (NumberofValues - 1))] / (float) (NumberofValues - 1);//only multiplied because "saturation" goes from 0 to 1
                    } else {
                        hsv[2] = (float) LUT[(int) (hsv[2] * (NumberofValues - 1))] / (float) (NumberofValues - 1);//only multiplied because "value" goes from 0 to 1
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
                tab[i] = ((255 << 24) | (R << 16) | (G << 8) | B); //the pixel is stocked in ARGB format. The A is 255 for the pixel to be visible.
            }
        }
        original.setPixels(tab, 0, original.getWidth(), 0, 0, original.getWidth(), original.getHeight());
        return original;
    }
}