package com.package1.Mask;

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

    // TO DO
    public Mask(int[] mask, int width, int height, int weight) {
        this.mask = new int[ width * height];
        for (int i = 0; i < width * height; i++) {
            this.mask[i] = mask[i];
        }
        this.width = width;
        this.height = height;
        this.weight = weight;
    }

    public Mask(int[] mask, int width, int height) {
        this.mask = new int[width * height];
        for (int i = 0; i < width * height; i++) {
            this.mask[i] = mask[i];
        }
        this.width = width;
        this.height = height;
        this.weight = calculateWeight(mask, width, height);
    }

    public int calculateWeight(int[] mask, int width, int height) {
        int weight = 0;
        for (int i = 0; i < height * width; i++) {
            weight += mask[i];
        }
        if (weight == 0 ) {
            weight = 1;
        }
        return weight;
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
}
