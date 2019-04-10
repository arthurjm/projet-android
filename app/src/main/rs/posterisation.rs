#pragma version (1)
#pragma rs java_package_name (com.android.rssample )

int depth;
int tab[256];

uchar4 RS_KERNEL posterisation( uchar4 in , uint32_t x , uint32_t y ) {
    uchar4 out = in ;

    // On initialise avec l'indice de la premiere case du tableau (pour chaque canal indépendamment)
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

    int index = 0;
    int v_int = (int) (v * 255.0);
    // On cherche l'indice qui a sa valeur la plus proche de la valeur initiale (pour chaque canal indépendamment)
    for (int i = 1; i < depth; i++) {
        if ( abs(v_int - tab[i]) < abs(v_int - tab[index]) ) {
            index = i;
        }
    }

    // Une fois l'indice trouvé on récupère la valeur du tableau
    v = tab[index] / 255.0;

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

    return out ;
}

void initPosterisation(rs_allocation inputImage, rs_allocation outputImage) {
    // Initialise le tableau de valeurs possibles (s'execute qu'une fois au lancement)
    int delta = 255 / depth - 1;
    for (int i = 0; i < depth; i++) {
        tab[i] = i * delta;
    }

    // Lance le noyau
    rsForEach(posterisation, inputImage, outputImage);
}