package com.package1.affichage.Type;

/**
 * @author Mathieu
 * Different type in a FilterRecyclerView
 */
public enum FilterType {

    // Color
    Grey,
    Colorize,
    KeepHue,
    Invert,

    // Contrast
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
    Sobel,
    IncreaseBorder,

    // Extras
    FaceDetection,
    Rotate,
    NightMode,
    DayMode
}
