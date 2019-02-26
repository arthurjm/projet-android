#pragma version (1)
#pragma rs java_package_name (com.android.rssample)

float v;

uchar4 RS_KERNEL luminosity ( uchar4 in ) {
  uchar4 out = in;

  float maxRGB = max(in.r, max(in.g, in.b));
  float minRGB = min(min(in.r, in.g), in.b);
  maxRGB = maxRGB/255;
  minRGB = minRGB/255;
  float delta = maxRGB - minRGB;

  float h, s;
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

  // s
  if (maxRGB == 0) {
    s = 0;
  }
  else {
    s = 1 - minRGB / maxRGB;
  }

  int h2;
  float f, l, m, n;
  float tmp_float = h/60;
  h2 = fmod(tmp_float, 6);

  f = (h/60) - h2;
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
