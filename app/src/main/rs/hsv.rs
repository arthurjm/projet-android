#pragma version (1)
#pragma rs java_package_name (com.package1.rssample)

uchar4 RS_KERNEL rgb_to_hsv ( uchar4 in ) {
  uchar4 out = in;

  float maxRGB = max(in.r, max(in.g, in.b));
  float minRGB = min(min(in.r, in.g), in.b);
  maxRGB = maxRGB/255;
  minRGB = minRGB/255;
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

  // s
  if (maxRGB == 0) {
    s = 0;
  }
  else {
    s = delta / maxRGB;
  }

  //v
  v = maxRGB;

  return out;
}

uchar4 RS_KERNEL hsv_to_rgb ( uchar4 in ) {
  int t2;
  float f, l, m, n;
  t2 = h/60;

  f = (h/60) - t2;
  l = v * (1 - s);
  m = v * (1 - f * s);
  n = v * (1 - (1 - f) * s);

  if (t2 == 0) {
    out.r = v * 255;
    out.g = n * 255;
    out.b = l * 255;
  }
  if (t2 == 1) {
    out.r = m * 255;
    out.g = v * 255;
    out.b = l * 255;
  }
  if (t2 == 2) {
    out.r = l * 255;
    out.g = v * 255;
    out.b = n * 255;
  }
  if (t2 == 3) {
    out.r = l * 255;
    out.g = m * 255;
    out.b = v * 255;
  }
  if (t2 == 4) {
    out.r = n * 255;
    out.g = l * 255;
    out.b = v * 255;
  }
  if (t2 == 5) {
    out.r = l * 255;
    out.g = v * 255;
    out.b = m * 255;
  }

  return out;
}
