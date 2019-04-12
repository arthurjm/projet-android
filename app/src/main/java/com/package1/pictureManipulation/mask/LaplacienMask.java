package com.package1.pictureManipulation.mask;

public class LaplacienMask extends Mask {

    public LaplacienMask() {
        mask = new int[]{
                1, 1, 1,
                1, -8, 1,
                1, 1, 1};

        width = 3;
        height = 3;
        weight = 1;
    }

}
