package com.package1.pictureManipulation;


import com.package1.pictureManipulation.ChanelType;

import static com.package1.MainActivity.NumberofValues;

/**
 * @author Elias
 * This class create histogram with numerical value
 */
public class Histogram {

    private int histogramValue[] = new int[NumberofValues];
    private int min, max, count;
    private ChanelType chanel;

    /**
     * This constructor is used with the renderscript version of the creation of the histogram that return the array already
     * in the good format. Although, we need to extract the min and max values.
     *
     * @param tab    The array which correspond to the histogram
     * @param chanel ChanelType (Red, Green, Blue, Hue, Saturation, Variance)
     * @see ChanelType
     */
     Histogram(int[] tab, ChanelType chanel, int count) {
        this.chanel = chanel;
        this.count = count;
        min = NumberofValues;
        max = -1;
        for (int i = 0; i < NumberofValues; i++) {
            histogramValue[i] = tab[i];
            if (histogramValue[i] != 0 && i < min) min = i;
            if (histogramValue[i] != 0 && i > max) max = i;
        }
    }

    int getMin() {
        return min;
    }

    int getMax() {
        return max;
    }

    public int getCount() {
        return count;
    }

    ChanelType getChanel() {
        return chanel;
    }

    int getHistogramValue(int index) {
        return histogramValue[index];
    }

    //Start Java version of the constructor and initialization functions unused with the renderscript version
     /*
     * @param tab
     * @param newChanel

    public Histogram(int[] tab, ChanelType newChanel) {
        chanel = newChanel;
        setHistogram(tab);
    }


     *
     * @param tab
     *
    public void setHistogram(int[] tab) {
        int tempTotalValue = 0;
        int valueTemp;
        max = -1;
        min = NumberofValues;
        count = 1;//to avoid dividing by 0
        for (int i = 0; i < tab.length; i++) {
            valueTemp = tab[i];
            histogramValue[valueTemp]++;
            count++;
            tempTotalValue += valueTemp;
            if (valueTemp < min) {
                min = valueTemp;
            }
            if (valueTemp > max) {
                max = valueTemp;
            }
        }
        average = tempTotalValue / count;
    }*/
    //End of the java unused functions

    @Override
    public String toString() {
        return "histogram of : " + chanel + "\tmin : " + min + "\tmax : " + max + "\tcount : " + count;
    }
}