package com.package1;

import android.util.Log;

/**
 * Class permettant de créer des histogrammes à partir de tableaux de valeurs numériques
 */
public class Histogram {

    /**
     *  The Number of values that can be taken by the values of the histogram, must be the same value as in the "HistogramManipulation" class
     */
    static private int NumberofValues = 256;

    private int histogramValue[] = new int[NumberofValues];
    private int min, max, count, average;
    private ChanelType chanel;


    /**
     *
     * @param tab
     * @param newChanel
     */
    public Histogram(int[] tab, ChanelType newChanel) {
        chanel = newChanel;
        setHistogram(tab);
    }


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
    }

    /**
     * Getter of "min"
     * @return
     *      the value of "min"
     */
    public int getMin() {
        return min;
    }

    /**
     * Getter of "max"
     * @return
     *      the value of "max"
     */
    public int getMax() {
        return max;

    }

    public int getCount() {
        return count;
    }

    /**
     * Getter of "chanel"
     * @return
     *      the value of "chanel"
     */
    public ChanelType getChanel() {
        return chanel;
    }

    /**
     * Getter of "average"
     * @return
     *      the value of "average"
     */
    public int getAverage() {
        return average;
    }

    /**
     * Getter of the value of the histogram at the chosen index
     * @return
     *      the value of the histogram at the chosen index
     */
    public int getHistogramValue(int index) {
        return histogramValue[index];
    }

    @Override
    public String toString() {
        return "histogram of : " + chanel + "\tmin : " + min + "\tmax : " + max + "\taverage : " + average + "\tcount : " + count;
    }
}
