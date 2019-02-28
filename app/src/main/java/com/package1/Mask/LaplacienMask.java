package com.package1.Mask;

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
