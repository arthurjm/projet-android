package com.package1.pictureManipulation.unusedJavaVersion;

import android.graphics.Bitmap;
import android.graphics.Color;


class Convolution {
    /**
     * the height of convolution
     */
    private int pxHeight;
    /**
     * the width of convolution
     */
    private int pxWidth;
    /**
     * the mask for the convolution
     */
    private int[][] mask;
    /**
     * the weight total
     */
    private int weightTotal = 0;


    /**
     * construction of a convolution with a mask (newHeight and newWidth are different)
     *
     * @param tab Convolution mask
     * @param newWidth  the width of mask
     * @param newHeight the height of height
     */
    private Convolution(int[][] tab, int newWidth, int newHeight) {
        pxHeight = newHeight;
        pxWidth = newWidth;
        mask = new int[pxWidth][pxHeight];
        mask = tab.clone();
        for (int i = 0; i < pxWidth; i++) {
            for (int j = 0; j < pxHeight; j++) {
                weightTotal += tab[i][j];
            }
        }
        if (weightTotal == 0) {
            weightTotal = 1;
        }

    }

    /**
     * construction of a convolution with a mask (same newHeight and newWidth)
     *
     * @param tab Convolution mask
     * @param newWidth the width of mask
     */
    public Convolution(int[][] tab, int newWidth) {
        this(tab, newWidth, newWidth);
    }

    /**
     * construction of a convolution with a mask (same newHeight and newWidth)
     *
     * @param newWidth the width of mask
     */
    public Convolution(int newWidth) {
        this(newWidth, newWidth);
    }

    /**
     * construction of a convolution with a mask (newHeight and newWidth are different)
     *
     * @param newWidth  the width of mask
     * @param newHeight the height of mask
     */
    private Convolution(int newWidth, int newHeight) {
        pxWidth = newWidth;
        pxHeight = newHeight;
        mask = new int[newWidth][newHeight];
        for (int i = 0; i < pxWidth; i++) {
            for (int j = 0; j < pxHeight; j++) {
                mask[i][j] = 1;
                weightTotal++;
            }
        }
    }

    /**
     * creat a Gaussien mask
     *
     * @param gamma to decide each indice of the mask
     */
    public void setGaussienMask(double gamma) {
        int tempWeight = 0;
        int x = pxWidth / 2;
        for (int i = -x; i <= x; i++) {
            for (int j = -x; j <= x; j++) {
                double v = 10 * (Math.exp(-(i * i + j * j) / (2 * gamma * gamma)));
                mask[i + x][j + x] = (int) v;
                tempWeight += (int) v;
            }
        }
        weightTotal = tempWeight;
    }

    /**
     * to creat a sobel mask
     *
     * @param isVertical True = vertical, False = horizontal
     */
    public void setSobelMask(boolean isVertical) {
        weightTotal = 1;
        for (int i = 0; i < pxWidth; i++) {
            for (int j = 0; j < pxHeight; j++) {
                if (isVertical) {
                    if (i == (pxWidth - 1) / 2) {//it means that it is in the middle of the mask (as mask are always odd in width)
                        mask[i][j] = 0;
                    } else if (i < (pxWidth - 1) / 2) {
                        mask[i][j] = -1;
                    } else {
                        mask[i][j] = 1;
                    }
                } else {
                    if (j == (pxHeight - 1) / 2) {//it means that it is in the middle of the mask (as mask are always odd in height)
                        mask[i][j] = 0;
                    } else if (j < (pxHeight - 1) / 2) {
                        mask[i][j] = -1;
                    } else {
                        mask[i][j] = 1;
                    }
                }
            }
        }
    }

    /**
     * to creat a laplacian mask
     */
    public void setLaplacianMask() {
        weightTotal = 1;
        for (int i = 0; i < pxWidth; i++) {
            for (int j = 0; j < pxHeight; j++) {
                if (i == (pxWidth - 1) / 2 && j == (pxHeight - 1) / 2) {
                    mask[i][j] = pxHeight * pxWidth - 1;
                } else {
                    mask[i][j] = -1;
                }
            }
        }
    }

    /**
     * to set the height with the value pxHeight
     *
     * @param pxHeight the value to set
     */
    public void setPxHeight(int pxHeight) {
        this.pxHeight = pxHeight;
    }

    /**
     * to set the width with the value pxWidth
     *
     * @param pxWidth the value to set
     */
    public void setPxWidth(int pxWidth) {
        this.pxWidth = pxWidth;
    }

    /**
     * to get the mask
     *
     * @return Convolution mask
     */
    public int[][] getMask() {
        return mask;
    }

    /**
     * to set the mask
     *
     * @param mask Convolution mask
     */
    public void setMask(int[][] mask) {
        this.mask = mask;
    }

    /**
     * to limit the value not exceeding a value limit
     *
     * @param value the value to limit
     * @return the limitValue
     */
    private int limitValue(int value) {
        if (value > 255) {
            value = 255;
        } else if (value < 0) {
            value = 0;
        }
        return value;
    }

    /*
            Cette méthode va appliquer le masque de convolution (mask) au fichier bitmap passé en argument
            pour cela, on retire les informations des trois canaux R,G,B dans des tableaux distincs pour un accès plus simple,
            puis on applique le masque sur chacun des canaux en appliquant un traitement spécial pour les bordures où
            l'on manque de pixels pour appliquer l'ensemble du masque (ici le choix a été fait de réaliser la moyenne des pixels
            accessibles en respectant les poids du masque).
             */

    /**
     * to apply the mask to the image
     *
     * @param original the original image
     * @return image processed
     */
    public Bitmap applicationConvolution(Bitmap original) {
        int width = original.getWidth();
        int height = original.getHeight();
        int[][] tabPixR = new int[width][height];
        int[][] tabPixG = new int[width][height];
        int[][] tabPixB = new int[width][height];
        int R, G, B, index, tempR, tempG, tempB, localWeight;
        int[] tabPixels = new int[width * height];
        Bitmap res = Bitmap.createBitmap(width, height, original.getConfig());
        original.getPixels(tabPixels, 0, width, 0, 0, width, height);
        //initialisation des tableaux des trois canaux R,G,B des pixels du bitmap passé en parametre
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                index = y * width + x;
                R = (tabPixels[index] >> 16) & 0xff;
                G = (tabPixels[index] >> 8) & 0xff;
                B = tabPixels[index] & 0xff;
                tabPixR[x][y] = R;
                tabPixG[x][y] = G;
                tabPixB[x][y] = B;
            }
        }
        //boucle de l'application du masque
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                index = y * width + x;
                tempR = 0;
                tempG = 0;
                tempB = 0;
                //entre dans le if si le pixel central permet d'appliquer le masque en entier
                if (!(x < pxWidth / 2 || y < pxHeight / 2 || x > width - (pxWidth + 1) / 2 || y > height - (pxHeight + 1) / 2)) {
                    for (int j = 0; j < pxHeight; j++) {
                        for (int i = 0; i < pxWidth; i++) {
                            tempR += tabPixR[x + i - pxWidth / 2][y + j - pxHeight / 2] * mask[i][j];
                            tempG += tabPixG[x + i - pxWidth / 2][y + j - pxHeight / 2] * mask[i][j];
                            tempB += tabPixB[x + i - pxWidth / 2][y + j - pxHeight / 2] * mask[i][j];
                        }
                    }
                    tempR = tempR / weightTotal;
                    tempG = tempG / weightTotal;
                    tempB = tempB / weightTotal;
                } else { //sinon, on applique le traitement d'une bordure
                    localWeight = 0;
                    for (int j = 0; j < pxHeight; j++) {
                        for (int i = 0; i < pxWidth; i++) {
                            if ((x + i - pxWidth / 2) >= 0 && (x + i - pxWidth / 2) < width
                                    && (y + j - pxHeight / 2) >= 0 && (y + j - pxHeight / 2) < height) {
                                tempR += tabPixR[x + i - pxWidth / 2][y + j - pxHeight / 2] * mask[i][j];
                                tempG += tabPixG[x + i - pxWidth / 2][y + j - pxHeight / 2] * mask[i][j];
                                tempB += tabPixB[x + i - pxWidth / 2][y + j - pxHeight / 2] * mask[i][j];
                                localWeight += mask[i][j];
                            }
                        }
                    }
                    if (localWeight == 0) {//si le poid a appliquer est 0 on doit éviter la division par 0, on entre donc dans cette boucle
                        tempR = 0;
                        tempG = 0;
                        tempB = 0;
                    } else {
                        tempR = tempR / localWeight;
                        tempG = tempG / localWeight;
                        tempB = tempB / localWeight;
                    }
                }
                tabPixels[index] = Color.rgb(tempR, tempG, tempB);
            }
        }
        res.setPixels(tabPixels, 0, width, 0, 0, width, height);
        return res;
    }

    /**
     * apply the mask to the image except edge of the image
     *
     * @param original the original image
     * @return the image processed
     */
    public Bitmap edgeDetection(Bitmap original) {
        int width = original.getWidth();
        int height = original.getHeight();
        int[][] tabPixR = new int[width][height];
        int[][] tabPixG = new int[width][height];
        int[][] tabPixB = new int[width][height];
        int R, G, B, index, tempR, tempG, tempB, localWeight;
        int[] tabPixels = new int[width * height];
        Bitmap res = Bitmap.createBitmap(width, height, original.getConfig());
        original.getPixels(tabPixels, 0, width, 0, 0, width, height);
        //initialisation des tableaux des trois canaux R,G,B des pixels du bitmap passé en parametre
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                index = y * width + x;
                R = (tabPixels[index] >> 16) & 0xff;
                G = (tabPixels[index] >> 8) & 0xff;
                B = tabPixels[index] & 0xff;
                tabPixR[x][y] = R;
                tabPixG[x][y] = G;
                tabPixB[x][y] = B;
            }
        }
        //boucle de l'application du masque
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                index = y * width + x;
                tempR = 0;
                tempG = 0;
                tempB = 0;
                //entre dans le if si le pixel central permet d'appliquer le masque en entier
                if (!(x < pxWidth / 2 || y < pxHeight / 2 || x > width - (pxWidth + 1) / 2 || y > height - (pxHeight + 1) / 2)) {
                    for (int j = 0; j < pxHeight; j++) {
                        for (int i = 0; i < pxWidth; i++) {
                            tempR += tabPixR[x + i - pxWidth / 2][y + j - pxHeight / 2] * mask[i][j];
                            tempG += tabPixG[x + i - pxWidth / 2][y + j - pxHeight / 2] * mask[i][j];
                            tempB += tabPixB[x + i - pxWidth / 2][y + j - pxHeight / 2] * mask[i][j];
                        }
                    }

                    tempR = limitValue(tempR);
                    tempG = limitValue(tempG);
                    tempB = limitValue(tempB);
                } else { //sinon, on applique le traitement d'une bordure
                    localWeight = 0;
                    for (int j = 0; j < pxHeight; j++) {
                        for (int i = 0; i < pxWidth; i++) {
                            if ((x + i - pxWidth / 2) >= 0 && (x + i - pxWidth / 2) < width
                                    && (y + j - pxHeight / 2) >= 0 && (y + j - pxHeight / 2) < height) {
                                tempR += tabPixR[x + i - pxWidth / 2][y + j - pxHeight / 2] * mask[i][j];
                                tempG += tabPixG[x + i - pxWidth / 2][y + j - pxHeight / 2] * mask[i][j];
                                tempB += tabPixB[x + i - pxWidth / 2][y + j - pxHeight / 2] * mask[i][j];
                                localWeight += mask[i][j];
                            }
                        }
                    }
                    if (localWeight == 0) {//si le poid a appliquer est 0 on doit éviter la division par 0, on entre donc dans cette boucle
                        tempR = 0;
                        tempG = 0;
                        tempB = 0;
                    } else {
                        tempR = limitValue(tempR / localWeight);
                        tempG = limitValue(tempG / localWeight);
                        tempB = limitValue(tempB / localWeight);
                    }
                }
                tabPixels[index] = Color.rgb(tempR, tempG, tempB);
            }
        }
        res.setPixels(tabPixels, 0, width, 0, 0, width, height);
        return res;
    }
}









