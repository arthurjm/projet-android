package com.package1;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

/**
 * Created by ekhatilefran on 09/11/18.
 */

public class Convolution {
    private int pxHeight; //taille de la convolution
    private int pxWidth;
    private int[][] mask;
    private int weightTotal =0;



    //constructeur associant un masque de convolution (tab) de hauteur et largeur différente
    public Convolution(int[][] tab, int newWidth, int newHeight){
        pxHeight =newHeight;
        pxWidth =newWidth;
        mask = new int[pxWidth][pxHeight];
        mask = tab.clone();
        for (int i=0; i < pxWidth; i++) {
            for (int j=0; j < pxHeight; j++) {
                weightTotal += tab[i][j];
            }
        }
        if (weightTotal==0){
            weightTotal=1;
        }
    }

    //constructeur associant un masque de convolution (tab) de hauteur et largeur égales
    public Convolution(int[][] tab, int newWidth){
        this(tab,newWidth,newWidth);
    }

    //constructeur associant un masque de convolution moyenneur de hauteur et largeur identique
    public Convolution(int newWidth){
        pxWidth=newWidth;
        pxHeight=newWidth;
        mask = new int[newWidth][newWidth];
        for(int i = 0; i< pxWidth; i++){
            for (int j = 0; j< pxWidth; j++){
                mask[i][j]=1;
                weightTotal++;
            }
        }
    }

    //constructeur associant un masque de convolution moyenneur de hauteur et largeur diférentes
    public Convolution(int newWidth, int newHeight){
        pxWidth=newWidth;
        pxHeight=newHeight;
        mask = new int[newWidth][newHeight];
        for(int i = 0; i< pxWidth; i++){
            for (int j = 0; j< pxHeight; j++){
                mask[i][j]=1;
                weightTotal++;
            }
        }
    }

    //initialise un masque de convolution pour obtenir un flou gaussien
    public Convolution(){
        //tableau de flou Gaussien
        int[][] tabGauss =new int[5][5];
        tabGauss[0][0]=1;tabGauss[0][1]=2;tabGauss[0][2]=3;tabGauss[0][3]=2;tabGauss[0][4]=1;
        tabGauss[1][0]=2;tabGauss[1][1]=6;tabGauss[1][2]=8;tabGauss[1][3]=6;tabGauss[1][4]=2;
        tabGauss[2][0]=3;tabGauss[2][1]=8;tabGauss[2][2]=10;tabGauss[2][3]=8;tabGauss[2][4]=3;
        tabGauss[3][0]=2;tabGauss[3][1]=6;tabGauss[3][2]=8;tabGauss[3][3]=6;tabGauss[3][4]=2;
        tabGauss[4][0]=1;tabGauss[4][1]=2;tabGauss[4][2]=3;tabGauss[4][3]=2;tabGauss[4][4]=1;
        pxHeight =5;
        pxWidth =5;
        mask = new int[pxWidth][pxHeight];
        mask = tabGauss.clone();
        for (int i=0; i < pxWidth; i++) {
            for (int j=0; j < pxHeight; j++) {
                weightTotal += mask[i][j];
            }
        }
    }

    /*
    Cette méthode va appliquer le masque de convolution (mask) au fichier bitmap passé en argument
    pour cela, on retire les informations des trois canaux R,G,B dans des tableaux distincs pour un accès plus simple,
    puis on applique le masque sur chacun des canaux en appliquant un traitement spécial pour les bordures où
    l'on manque de pixels pour appliquer l'ensemble du masque (ici le choix a été fait de réaliser la moyenne des pixels
    accessibles en respectant les poids du masque).
     */
    public Bitmap applicationConvolution(Bitmap original){
        Log.e("temp", "debut");
        int width = original.getWidth();
        int height = original.getHeight();
        Bitmap res = Bitmap.createBitmap(width,height,original.getConfig());
        int[][] tabPixR = new int[width][height];
        int[][] tabPixG = new int[width][height];
        int[][] tabPixB = new int[width][height];
        int R,G,B,index,tempR,tempG,tempB,localWeight;
        int[] tabPixels = new int[width*height];
        original.getPixels(tabPixels,0,width,0,0,width,height);
        //initialisation des tableaux des trois canaux R,G,B des pixels du bitmap passé en parametre
        for(int y=0; y<height;y++){
            for(int x=0; x<width;x++){
                index=y*width+x;
                R=(tabPixels[index] >> 16) & 0xff;
                G=(tabPixels[index] >> 8) & 0xff;
                B=tabPixels[index] & 0xff;
                tabPixR[x][y]=R;
                tabPixG[x][y]=G;
                tabPixB[x][y]=B;
            }
        }
        //boucle de l'application du masque
        for(int y=0; y<height;y++){
            for(int x=0; x<width;x++){
                index=y*width+x;
                tempR=0;
                tempG=0;
                tempB=0;
                //entre dans le if si le pixel central permet d'appliquer le masque en entier
                if(!(x<pxWidth/2 || y<pxHeight/2 || x>width-(pxWidth+1)/2 || y>height-(pxHeight+1)/2 )) {
                    for(int j=0; j<pxHeight; j++) {
                        for(int i=0; i<pxWidth; i++) {
                            tempR+=tabPixR[x+i-pxWidth/2][y+j-pxHeight/2]*mask[i][j];
                            tempG+=tabPixG[x+i-pxWidth/2][y+j-pxHeight/2]*mask[i][j];
                            tempB+=tabPixB[x+i-pxWidth/2][y+j-pxHeight/2]*mask[i][j];
                        }
                    }
                    tempR=tempR/weightTotal;
                    tempG=tempG/weightTotal;
                    tempB=tempB/weightTotal;
                }else { //sinon, on applique le traitement d'une bordure
                    localWeight = 0;
                    for (int j=0; j < pxHeight; j++) {
                        for (int i=0; i < pxWidth; i++) {
                            if ((x + i - pxWidth / 2) >= 0 && (x + i - pxWidth / 2) < width
                                    && (y + j - pxHeight / 2) >= 0 && (y + j - pxHeight / 2) < height) {
                                tempR+=tabPixR[x+i-pxWidth/2][y+j-pxHeight/2]*mask[i][j];
                                tempG+=tabPixG[x+i-pxWidth/2][y+j-pxHeight/2]*mask[i][j];
                                tempB+=tabPixB[x+i-pxWidth/2][y+j-pxHeight/2]*mask[i][j];
                                localWeight += mask[i][j];
                            }
                        }
                    }
                    if (localWeight == 0) {//si le poid a appliquer est 0 on doit éviter la division par 0, on entre donc dans cette boucle
                        tempR=0;
                        tempG=0;
                        tempB=0;
                    } else {
                        tempR=tempR/localWeight;
                        tempG=tempG/localWeight;
                        tempB=tempB/localWeight;
                    }
                }
                tabPixels[index]=Color.rgb(tempR,tempG,tempB);
            }
        }
        res.setPixels(tabPixels,0,width,0,0,width,height);
        Log.e("temp", "fin");
        return res;
    }
}









