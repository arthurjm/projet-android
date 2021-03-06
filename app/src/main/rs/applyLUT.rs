#pragma version (1)
#pragma rs java_package_name (com.android.rssample)

rs_allocation LUT;
int canal;

uchar4 RS_KERNEL applyLUT(uchar4 in, uint32_t x, uint32_t y) {
	uchar4 out = in;

    // Canal Red (1 == Red)
	if (canal == 1) {
		out.r = rsGetElementAt_uchar(LUT, out.r);
	}

	// Canal Green (2 == Green)
	if (canal == 2) {
		out.g = rsGetElementAt_uchar(LUT, out.g);
	}

	// Canal Blue (3 == Blue)
	if (canal == 3) {
		out.b = rsGetElementAt_uchar(LUT, out.b);
	}

	// Canaux HSV
	if (canal > 3) {
	    // Passe le RGB en HSV
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
			int index = h * 255/360;
			h = rsGetElementAt_int(LUT, index) * 360/255;
		}

		// Canal S (5 == S)
		if (canal == 5) {
			int index = (int) (s * 255);
			s = rsGetElementAt_int(LUT, index) / 255.0;
		}

		// Canal V (6 == V)
		if (canal == 6) {
			int index = (int) (v * 255);
			v = rsGetElementAt_int(LUT, index) / 255.0;
		}

        // Repasser du HSV en RGB
		float f, l, m, n;
		float h2 = h/60.0;

		h = h/60;
		f = h2 - h;
		l = v * (1 - s);
		m = v * (1 - f * s);
		n = v * (1 - (1 - f) * s);

		if (h == 0) {
			out.r = v * 255;
			out.g = n * 255;
			out.b = l * 255;
		}
		if (h == 1) {
			out.r = m * 255;
			out.g = v * 255;
			out.b = l * 255;
		}
		if (h == 2) {
			out.r = l * 255;
			out.g = v * 255;
			out.b = n * 255;
		}
		if (h == 3) {
			out.r = l * 255;
			out.g = m * 255;
			out.b = v * 255;
		}
		if (h == 4) {
			out.r = n * 255;
			out.g = l * 255;
			out.b = v * 255;
		}
		if (h == 5) {
			out.r = v * 255;
			out.g = l * 255;
			out.b = m * 255;
		}
	}

	return out;
}