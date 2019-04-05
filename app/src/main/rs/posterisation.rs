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

	//v
	float v = maxRGB;



    // Une fois l'indice trouvé on récupère la valeur du tableau
    out.r = tab[out.r];
    out.g = tab[out.g];
    out.b = tab[out.b];

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