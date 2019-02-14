#pragma version (1)
#pragma rs java_package_name (com.package1.rssample)

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
    s = delta / maxRGB;
  }

  //v
  v = maxRGB;

  int t2;
  float f, l, m, n;
  t2 = hue/60;

  f = (hue/60) - t2;
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
