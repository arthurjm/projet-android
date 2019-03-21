package com.package1.affichage;

public enum FilterType {

    // Color
    Grey,
    Colorize,
    KeepHue,
    Invert,

    // Saturation
    Contrast,
    ShiftLight,
    ShiftSaturation,
    ShiftColor,
    Isohelie,
    EquaLight,

    // Mask
    Blur,
    Gaussian,
    Laplacien,
    SobelV,
    SobelH,

    // Extras
    FaceDetection,
    Rotate;
}
