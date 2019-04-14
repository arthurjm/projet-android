package com.package1.pictureManipulation.mask;

import android.support.annotation.NonNull;

import java.util.Arrays;


public class Mask {

    int[] mask;
    int width;
    int height;
    int weight;

    public Mask() {
        mask = new int[]{
                0, 0, 0,
                0, 1, 0,
                0, 0, 0};
        width = 3;
        height = 3;
        weight = 1;
    }

    public Mask(int[] mask, int width, int height) {
        this.mask = new int[width * height];
        for (int i = 0; i < width * height; i++) {
            this.mask[i] = mask[i];
            this.weight += mask[i];
        }
        this.width = width;
        this.height = height;
    }

    public int[] getMask() {
        return mask;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getWeight() {
        return weight;
    }

    @NonNull
    @Override
    public String toString() {
        return "Mask{" +
                "mask=" + Arrays.toString(mask) +
                ", width=" + width +
                ", height=" + height +
                ", weight=" + weight +
                '}';
    }
}
