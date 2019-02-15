#pragma version (1)
#pragma rs java_package_name (com.package1.rssample)

int hue;

uchar4 RS_KERNEL keepHue ( uchar4 in ) {
  uchar4 out = in;

  float maxRGB = max(in.r, max(in.g, in.b));
  float minRGB = min(min(in.r, in.g), in.b);
  maxRGB = maxRGB;
  minRGB = minRGB;
  float delta = maxRGB - minRGB;

  float h, s, v;

  //h
  if (minRGB == maxRGB) {
    h = 0;
  }
  if (maxRGB == in.r) {
    h = fmod( (60 * (in.g - in.b)/delta + 360), 360);
  }
  if (maxRGB == in.g) {
    h = fmod( (60 * (in.b - in.r)/delta + 120), 360);
  }
  if (maxRGB == in.b) {
    h = fmod( (60 * (in.r - in.g)/delta + 240), 360);
  }

  if (!( h < (hue + 10)%360 && h > (hue - 10)%360 )) {
    float grey = (0.30 * in.r + 0.59 * in.g + 0.11 * in.b);
    out.r = grey;
    out.g = grey;
    out.b = grey;
  }

  return out;
}
