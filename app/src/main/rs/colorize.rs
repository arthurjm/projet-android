#pragma version (1)
#pragma rs java_package_name (com.android.rssample)

int hue;

uchar4 RS_KERNEL colorize ( uchar4 in ) {
  uchar4 out = in;

  float maxRGB = max(in.r, max(in.g, in.b));
  float minRGB = min(min(in.r, in.g), in.b);
  maxRGB = maxRGB/255;
  minRGB = minRGB/255;
  float delta = maxRGB - minRGB;

  float s, v;
  // s
  if (maxRGB == 0) {
    s = 0;
  }
  else {
    s = 1 - minRGB / maxRGB;
  }

  //v
  v = maxRGB;

  int h2;
  float f, l, m, n;
  float test = hue/60;
  h2 = fmod(test, 6);

  f = (hue/60) - h2;
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
