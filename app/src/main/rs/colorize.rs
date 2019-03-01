#pragma version (1)
#pragma rs java_package_name (com.android.rssample)

int hue;

uchar4 RS_KERNEL colorize ( uchar4 in ) {
	uchar4 out = in;

	float r = in.r / 255.0;
	float g = in.g / 255.0;
	float b = in.b / 255.0;

	float maxRGB = max(r, max(g, b));
	float minRGB = min(min(r, g), b);
	float delta = maxRGB - minRGB;

	float s, v;
	// s
	if (maxRGB == 0) {
		s = 0;
	}
	else {
		s = (maxRGB - minRGB) / maxRGB;
	}

	//v
	v = maxRGB;

	int h2;
	float f, l, m, n;
	float h = hue/60.0;
	h2 = fmod(h, 6);

	f = h - h2;
	l = v * (1 - s);
	m = v * (1 - f * s);
	n = v * (1 - (1 - f) * s);

	if (h2 == 0) {
		out.r = v * 255;
		out.g = n * 255;
		out.b = l * 255;
	}
	if (h2 == 1) {
		out.r = m * 255;
		out.g = v * 255;
		out.b = l * 255;
	}
	if (h2 == 2) {
		out.r = l * 255;
		out.g = v * 255;
		out.b = n * 255;
	}
	if (h2 == 3) {
		out.r = l * 255;
		out.g = m * 255;
		out.b = v * 255;
	}
	if (h2 == 4) {
		out.r = n * 255;
		out.g = l * 255;
		out.b = v * 255;
	}
	if (h2 == 5) {
		out.r = v * 255;
		out.g = l * 255;
		out.b = m * 255;
	}

	return out;
}
