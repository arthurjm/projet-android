package com.package1.Mask;

public class BlurMask extends Mask{

    public BlurMask(int size) {
        mask = new int[size * size];
        for (int i = 0; i < size * size; i++) {
            mask[i] = 1;
        }
        height = size;
        width = size;
        weight = size * size;
    }
}
