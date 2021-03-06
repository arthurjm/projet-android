#pragma version (1)
#pragma rs java_package_name (com.android.rssample)

int hue;
int precision;

uchar4 RS_KERNEL keepHue ( uchar4 in ) {
	uchar4 out = in;

	float r = in.r / 255.0;
	float g = in.g / 255.0;
	float b = in.b / 255.0;

	float maxRGB = max(r, max(g, b));
	float minRGB = min(min(r, g), b);
	float delta = maxRGB - minRGB;

	float h;
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

	if (min( fmod((h-hue+360), 360),  fmod((hue-h+360), 360) ) > precision) {
		float grey = (0.30 * in.r + 0.59 * in.g + 0.11 * in.b);
		out.r = grey;
		out.g = grey;
		out.b = grey;
	}

	return out;
}
