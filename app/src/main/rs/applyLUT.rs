#pragma version (1)
#pragma rs java_package_name (com.android.rssample)

rs_allocation LUT;
int canal;

uchar4 RS_KERNEL applyLUT(uchar4 in, uint32_t x, uint32_t y) {
    uchar4 out = in;

    if (canal == 1) {
        out.r = rsGetElementAt_uchar(LUT, out.r);
    }
    if (canal == 2) {
        out.g = rsGetElementAt_uchar(LUT, out.g);
    }
    if (canal == 3) {
        out.b = rsGetElementAt_uchar(LUT, out.b);
    }

    return out;
}