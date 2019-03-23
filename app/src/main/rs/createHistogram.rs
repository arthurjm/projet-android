#pragma version (1)
#pragma rs java_package_name (com.android.rssample )

int canal;
int32_t *histogram;

uchar4 RS_KERNEL createHistogram( uchar4 in , uint32_t x , uint32_t y ) {
    uchar4 out = in ;

    int index = 0;

    // Canal Red (1 == Red)
	if (canal == 1) {
	    index = in.r;
	}
	// Canal Green (2 == Green)
	if (canal == 2) {
	    index = in.g;
	}
	// Canal Blue (3 == Blue)
	if (canal == 3) {
	    index = in.b;
	}
	// Canaux HSV
	if (canal > 3) {
		float r = in.r / 255.0;
		float g = in.g / 255.0;
		float b = in.b / 255.0;

		float maxRGB = max(r, max(g, b));
		float minRGB = min(min(r, g), b);
		float delta = maxRGB - minRGB;

		float s, v;
		int h;
		//h
		if (minRGB == maxRGB) {
			h = 0;
		}
		if (maxRGB == r) {
			h = fmod( (60 * (g - b)/delta + 360), 360);
		}
		if (maxRGB == g) {
			h = fmod( (60 * (b - r)/delta + 120), 360);
		}
		if (maxRGB == b) {
			h = fmod( (60 * (r - g)/delta + 240), 360);
		}

		// s
		if (maxRGB == 0) {
			s = 0;
		}
		else {
			s = (maxRGB - minRGB) / maxRGB;
		}

		//v
		v = maxRGB;

        // Canal H (4 == H)
		if (canal == 4) {
		    index = (int) h * 255/360;
		}
		// Canal S (5 == S)
		if (canal == 5) {
		    index = (int) s * 255;
		}
		// Canal V (6 == V)
		if (canal == 6) {
		    index = (int) v * 255;
		}
    }
    volatile int32_t *addr = &histogram[index];
    rsAtomicInc(addr);
    return out ;
}