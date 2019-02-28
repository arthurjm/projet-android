package com.package1.Mask;

public class SobelMask extends Mask {

    public SobelMask(boolean vertical) {
        if (vertical) {
            mask = new int[]{
                    -1, 0, 1,
                    -2, 0, 2,
                    -1, 0, 1};
        }
        else {
            mask = new int[]{
                    -1, -2, -1,
                    0, 0, 0,
                    1, 2, 1};
        }
        width = 3;
        height = 3;
        weight = 1;
    }
}
