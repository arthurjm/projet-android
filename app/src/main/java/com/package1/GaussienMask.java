package com.package1;

public class GaussienMask extends Convolution {
    private int[][] mask;

    public GaussienMask(int width, double gamma) {
        super(width);
        int poidTemp=0;
        mask = this.getMask();
        int x = width / 2;
        for (int i = -x; i <= x; i++) {
            for (int j = -x; j <= x; j++) {
                double v = 10 * (Math.exp(-(i * i + j * j) / (2 * gamma * gamma)));
                mask[i + x][j + x] = (int) v;
                poidTemp+=(int) v;
            }
        }
        setWeightTotal(poidTemp);
    }

}
