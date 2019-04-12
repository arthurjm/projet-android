package com.package1.Mask;

public class GaussianBlur extends Mask{

    public GaussianBlur(int size, double gamma) {
        mask = new int[size * size];
        int t = size/2;
        int index = 0;
        for (int i = -t; i <= t; i++) {
            for (int j = -t; j <= t; j++) {
                double v = 10 * (Math.exp(-(i * i + j * j) / (2 * gamma * gamma)));
                mask[index] = (int) v;
                weight += mask[index];
                index++;
            }
        }
        width = size;
        height = size;
    }
}