#pragma version (1)
#pragma rs java_package_name (com.android.rssample)

rs_allocation input;
uint32_t width;
uint32_t height;

rs_allocation mask;
int maskWidth;
int maskHeight;

int weight;

uchar4 RS_KERNEL convolution(uchar4 in, uint32_t x, uint32_t y) {
    uchar4 out = in;

    float4 temp = 0.0f;
    int localWeight = 0;
    // Works like applicationConvolution in Convolution.java
    //entre dans le if si le pixel central permet d'appliquer le masque en entier
    if (!(x < maskWidth / 2 || y < maskHeight / 2 || x > width - (maskWidth + 1) / 2 || y > height - (maskHeight + 1) / 2)) {
        for (int j = 0; j < maskHeight; j++) {
            for (int i = 0; i < maskWidth; i++) {
                float4 pixel = rsUnpackColor8888(rsGetElementAt_uchar4(input, x + i - maskWidth / 2, y + j - maskHeight / 2));
                temp += pixel * (float) rsGetElementAt_int(mask, i, j);
            }
        }
        temp = temp/weight;
    }
    else { //sinon, on applique le traitement d'une bordure

        for (int j = 0; j < maskHeight; j++) {
            for (int i = 0; i < maskWidth; i++) {
                if ((x + i >= maskWidth / 2) && (x + i - maskWidth / 2) < width && (y + j >= maskHeight / 2) && (y + j - maskHeight / 2) < height) {
                    float4 pixel = rsUnpackColor8888(rsGetElementAt_uchar4(input, x + i - maskWidth / 2, y + j - maskHeight / 2));
                    temp += pixel * (float) rsGetElementAt_int(mask, i, j);
                    localWeight += rsGetElementAt_int(mask, i, j);
                }
            }
        }

        if (localWeight == 0) { //si le poid a appliquer est 0 on doit éviter la division par 0, on entre donc dans cette boucle
            temp = 0;
        }
        else {
            temp = temp / localWeight;
        }
    }

    temp.a = 1;
    return rsPackColorTo8888(temp);
}