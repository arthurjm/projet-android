#pragma version (1)
#pragma rs java_package_name (com.android.rssample)

rs_allocation input;
uint32_t width;
uint32_t height;

static int H[3][3] = {
   {-1, 0, 1},
   {-2, 0, 2},
   {-1, 0, 1} };

static int V[3][3] = {
   {-1, -2, -1},
   {0, 0, 0},
   {1, 2, 1} };

static int maskHeight = 3;
static int maskWidth = 3;

int precision;

uchar4 RS_KERNEL increaseBorder(uchar4 in, uint32_t x, uint32_t y) {
    float4 temp = 0.0f;
    float4 A = 0.0f; float4 B = 0.0f;
    int localWeightA = 0; int localWeightB = 0;
    //entre dans le if si le pixel central permet d'appliquer le masque en entier
    if (!(x < maskWidth / 2 || y < maskHeight / 2 || x > width - (maskWidth + 1) / 2 || y > height - (maskHeight + 1) / 2)) {
        for (int j = 0; j < maskHeight; j++) {
            for (int i = 0; i < maskWidth; i++) {
                float4 pixel = rsUnpackColor8888(rsGetElementAt_uchar4(input, x + i - maskWidth / 2, y + j - maskHeight / 2));
                A += pixel * (float) H[i][j];
                B += pixel * (float) V[i][j];
            }
        }
    }
    else { //sinon, on applique le traitement d'une bordure
        for (int j = 0; j < maskHeight; j++) {
            for (int i = 0; i < maskWidth; i++) {
                if ((x + i >= maskWidth / 2) && (x + i - maskWidth / 2) < width && (y + j >= maskHeight / 2) && (y + j - maskHeight / 2) < height) {
                    float4 pixel = rsUnpackColor8888(rsGetElementAt_uchar4(input, x + i - maskWidth / 2, y + j - maskHeight / 2));
                    A += pixel * (float) H[i][j];
                    B += pixel * (float) V[i][j];
                    localWeightA += H[i][j];
                    localWeightB += V[i][j];
                }
            }
        }

        if (localWeightA == 0) { //si le poid a appliquer est 0 on doit éviter la division par 0, on entre donc dans cette boucle
            A = 0;
        }
        else {
            A = A / localWeightA;
        }
        if (localWeightB == 0) { //si le poid a appliquer est 0 on doit éviter la division par 0, on entre donc dans cette boucle
            B = 0;
        }
        else {
            B = B / localWeightB;
        }
    }

    A.a = 1; B.a = 1;

    temp = sqrt(A*A + B*B);

    uchar4 border = rsPackColorTo8888(temp);
    uchar4 black = in;
    black.r = 0;
    black.g = 0;
    black.b = 0;

    if (border.r < precision && border.g < precision && border.b < precision) {
        return in;
    }
    return black;
}