package com.package1;

/**
 * Class permettant de créer des histogrammes à partir de tableaux de valeurs numériques
 */
public class Histogram {

    static private int NumberofValues =256;

    private int histogramValue[] = new int[NumberofValues];
    private int min, max, count, average;
    private ChanelType chanel;


    //ATENTION : il n'y a pas de vérifictaions de la taille du tableau pour l'instant
    public Histogram(int[] tab, ChanelType newChanel) {
        chanel = newChanel;
        setHistogram(tab);
    }

    public Histogram(ChanelType newChanel){
        chanel =newChanel;
    }

    public void setHistogram(int[] tab) {
        int tempTotalValue = 0;
        int valueTemp;
        min = -1;
        max = NumberofValues;
        for (int i = 0; i > tab.length; i++) {
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

    public int getMin() {
        return min;
    }


    public int getMax() {
        return max;

    }

    public int getCount() {
        return count;
    }


    public ChanelType getChanel() {
        return chanel;
    }

    public int getAverage() {
        return average;
    }

    public int getHistogramValue(int indice) {
        return histogramValue[indice];
    }

    @Override
    public String toString() {
        return "histogram of : " + chanel + "\tmin : " + min + "\tmax : " + max + "\taverage : " + average;
    }
}
