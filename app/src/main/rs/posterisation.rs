#pragma version (1)
#pragma rs java_package_name (com.android.rssample )

int depth;
int tab[256];

uchar4 RS_KERNEL posterisation( uchar4 in , uint32_t x , uint32_t y ) {
    uchar4 out = in ;

    // On initialise avec l'indice de la premiere case du tableau (pour chaque canal indépendamment)
    out.r = 0;
    out.g = 0;
    out.b = 0;

    // On cherche l'indice qui a sa valeur la plus proche de la valeur initiale (pour chaque canal indépendamment)
    for (int i = 1; i < depth; i++) {
        if ( abs(in.r - tab[i]) < abs(in.r - tab[out.r]) ) {
            out.r = i;
        }
        if ( abs(in.g - tab[i]) < abs(in.g - tab[out.g]) ) {
            out.g = i;
        }
        if ( abs(in.b - tab[i]) < abs(in.b - tab[out.b]) ) {
            out.b = i;
        }
    }

    // Une fois l'indice trouvé on récupère la valeur du tableau
    out.r = tab[out.r];
    out.g = tab[out.g];
    out.b = tab[out.b];

    return out ;
}

void initPosterisation(rs_allocation inputImage, rs_allocation outputImage) {
    // Initialise le tableau de valeurs possibles (s'execute qu'une fois au lancement)
    int delta = (int)((float)255 / (float)(depth - 1));
    for (int i = 0; i < depth; i++) {
        tab[i] = i * delta;
    }

    // Lance le noyau
    rsForEach(posterisation, inputImage, outputImage);
}