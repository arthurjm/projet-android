package com.package1;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        final ImageView imageSvg = (ImageView) findViewById(R.id.imageViewSvg);
        final ImageView imageChanged = (ImageView) findViewById(R.id.imageViewChange);
        Drawable draw = ContextCompat.getDrawable((this),R.drawable.image3);
        final Bitmap bitmapImageOg = ((BitmapDrawable) draw).getBitmap();
        final Bitmap bitmapImageChanged = bitmapImageOg.copy(bitmapImageOg.getConfig(),true);
        final Bitmap bitmapImageSvg = bitmapImageOg.copy(bitmapImageOg.getConfig(),true);


        final Button buttonMenu = (Button) findViewById(R.id.buttonBackMenu);
        final Button buttonReset = (Button) findViewById(R.id.buttonReset);
        final Button buttonActionGrey = (Button) findViewById(R.id.buttonActionGrey);
        final Button buttonActionPartialDesaturation = (Button) findViewById(R.id.buttonActionDesaturation);
        final Button buttonActionColorization = (Button) findViewById(R.id.buttonActionColorization);
        final Button buttonActionConvolution = (Button) findViewById(R.id.buttonActionConvo);
        final Button buttonActionConvolutionGauss = (Button) findViewById(R.id.buttonActionGauss);
        final Button buttonActionFlouLateral = (Button) findViewById(R.id.buttonActionBlur);
        final Button buttonActionEqHistogram = (Button) findViewById(R.id.buttonActionEq);
        final Button buttonActionLinearExtension = (Button) findViewById(R.id.buttonActionExtension);

        final ColorManipulation colorManipulation = new ColorManipulation();
        final Histogram histogram = new Histogram();
        final Convolution rightBlur = new Convolution((new int[][]{{0},{0},{0},{0},{0},{0},{0},{1},{1},{1},{1},{1},{1},{1},{1}}),15,1);
        final Convolution convolution = new Convolution(7);
        final Convolution convolutionGauss = new Convolution();

        imageSvg.setImageBitmap(bitmapImageOg);
        imageChanged.setImageBitmap(bitmapImageChanged);
        imageSvg.setVisibility(View.INVISIBLE);
        imageChanged.setVisibility(View.INVISIBLE);
        buttonMenu.setVisibility(View.INVISIBLE);
        buttonReset.setVisibility(View.INVISIBLE);

        buttonActionGrey.setVisibility(View.VISIBLE);
        buttonActionPartialDesaturation.setVisibility(View.VISIBLE);
        buttonActionColorization.setVisibility(View.VISIBLE);
        buttonActionConvolution.setVisibility(View.VISIBLE);
        buttonActionConvolutionGauss.setVisibility(View.VISIBLE);
        buttonActionFlouLateral.setVisibility(View.VISIBLE);
        buttonActionEqHistogram.setVisibility(View.VISIBLE);
        buttonActionLinearExtension.setVisibility(View.VISIBLE);

        buttonActionGrey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageSvg.setVisibility(View.VISIBLE);
                imageChanged.setVisibility(View.VISIBLE);
                buttonMenu.setVisibility(View.VISIBLE);
                buttonReset.setVisibility(View.VISIBLE);


                int[] tabpix = new int[bitmapImageChanged.getHeight()*bitmapImageChanged.getWidth()];
                bitmapImageChanged.getPixels(tabpix,0,bitmapImageChanged.getWidth(),0,0,bitmapImageChanged.getWidth(),bitmapImageChanged.getHeight());
                bitmapImageSvg.setPixels(tabpix,0,bitmapImageChanged.getWidth(),0,0,bitmapImageChanged.getWidth(),bitmapImageChanged.getHeight());
                colorManipulation.convertImageGreyScale(bitmapImageChanged).getPixels(tabpix,0,bitmapImageChanged.getWidth(),0,0,bitmapImageChanged.getWidth(),bitmapImageChanged.getHeight());
                bitmapImageChanged.setPixels(tabpix,0,bitmapImageChanged.getWidth(),0,0,bitmapImageChanged.getWidth(),bitmapImageChanged.getHeight());
                imageSvg.setImageBitmap(bitmapImageSvg);
                imageChanged.setImageBitmap(bitmapImageChanged);

                buttonActionGrey.setVisibility(View.INVISIBLE);
                buttonActionPartialDesaturation.setVisibility(View.INVISIBLE);
                buttonActionColorization.setVisibility(View.INVISIBLE);
                buttonActionConvolution.setVisibility(View.INVISIBLE);
                buttonActionConvolutionGauss.setVisibility(View.INVISIBLE);
                buttonActionFlouLateral.setVisibility(View.INVISIBLE);
                buttonActionEqHistogram.setVisibility(View.INVISIBLE);
                buttonActionLinearExtension.setVisibility(View.INVISIBLE);
            }
        });

        buttonActionEqHistogram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageSvg.setVisibility(View.VISIBLE);
                imageChanged.setVisibility(View.VISIBLE);
                buttonMenu.setVisibility(View.VISIBLE);
                buttonReset.setVisibility(View.VISIBLE);


                int[] tabpix = new int[bitmapImageChanged.getHeight()*bitmapImageChanged.getWidth()];
                bitmapImageChanged.getPixels(tabpix,0,bitmapImageChanged.getWidth(),0,0,bitmapImageChanged.getWidth(),bitmapImageChanged.getHeight());
                bitmapImageSvg.setPixels(tabpix,0,bitmapImageChanged.getWidth(),0,0,bitmapImageChanged.getWidth(),bitmapImageChanged.getHeight());
                histogram.applicationEqHistogram(bitmapImageChanged).getPixels(tabpix,0,bitmapImageChanged.getWidth(),0,0,bitmapImageChanged.getWidth(),bitmapImageChanged.getHeight());
                bitmapImageChanged.setPixels(tabpix,0,bitmapImageChanged.getWidth(),0,0,bitmapImageChanged.getWidth(),bitmapImageChanged.getHeight());
                imageSvg.setImageBitmap(bitmapImageSvg);
                imageChanged.setImageBitmap(bitmapImageChanged);

                buttonActionGrey.setVisibility(View.INVISIBLE);
                buttonActionPartialDesaturation.setVisibility(View.INVISIBLE);
                buttonActionColorization.setVisibility(View.INVISIBLE);
                buttonActionConvolution.setVisibility(View.INVISIBLE);
                buttonActionConvolutionGauss.setVisibility(View.INVISIBLE);
                buttonActionFlouLateral.setVisibility(View.INVISIBLE);
                buttonActionEqHistogram.setVisibility(View.INVISIBLE);
                buttonActionLinearExtension.setVisibility(View.INVISIBLE);
            }
        });

        buttonActionLinearExtension.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageSvg.setVisibility(View.VISIBLE);
                imageChanged.setVisibility(View.VISIBLE);
                buttonMenu.setVisibility(View.VISIBLE);
                buttonReset.setVisibility(View.VISIBLE);


                int[] tabpix = new int[bitmapImageChanged.getHeight()*bitmapImageChanged.getWidth()];
                bitmapImageChanged.getPixels(tabpix,0,bitmapImageChanged.getWidth(),0,0,bitmapImageChanged.getWidth(),bitmapImageChanged.getHeight());
                bitmapImageSvg.setPixels(tabpix,0,bitmapImageChanged.getWidth(),0,0,bitmapImageChanged.getWidth(),bitmapImageChanged.getHeight());
                histogram.applicationLinearExtension(bitmapImageChanged).getPixels(tabpix,0,bitmapImageChanged.getWidth(),0,0,bitmapImageChanged.getWidth(),bitmapImageChanged.getHeight());
                bitmapImageChanged.setPixels(tabpix,0,bitmapImageChanged.getWidth(),0,0,bitmapImageChanged.getWidth(),bitmapImageChanged.getHeight());
                imageSvg.setImageBitmap(bitmapImageSvg);
                imageChanged.setImageBitmap(bitmapImageChanged);

                buttonActionGrey.setVisibility(View.INVISIBLE);
                buttonActionPartialDesaturation.setVisibility(View.INVISIBLE);
                buttonActionColorization.setVisibility(View.INVISIBLE);
                buttonActionConvolution.setVisibility(View.INVISIBLE);
                buttonActionConvolutionGauss.setVisibility(View.INVISIBLE);
                buttonActionFlouLateral.setVisibility(View.INVISIBLE);
                buttonActionEqHistogram.setVisibility(View.INVISIBLE);
                buttonActionLinearExtension.setVisibility(View.INVISIBLE);
            }
        });

        buttonActionConvolution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageSvg.setVisibility(View.VISIBLE);
                imageChanged.setVisibility(View.VISIBLE);
                buttonMenu.setVisibility(View.VISIBLE);
                buttonReset.setVisibility(View.VISIBLE);


                int[] tabpix = new int[bitmapImageChanged.getHeight()*bitmapImageChanged.getWidth()];
                bitmapImageChanged.getPixels(tabpix,0,bitmapImageChanged.getWidth(),0,0,bitmapImageChanged.getWidth(),bitmapImageChanged.getHeight());
                bitmapImageSvg.setPixels(tabpix,0,bitmapImageChanged.getWidth(),0,0,bitmapImageChanged.getWidth(),bitmapImageChanged.getHeight());
                convolution.applicationConvolution(bitmapImageChanged).getPixels(tabpix,0,bitmapImageChanged.getWidth(),0,0,bitmapImageChanged.getWidth(),bitmapImageChanged.getHeight());
                bitmapImageChanged.setPixels(tabpix,0,bitmapImageChanged.getWidth(),0,0,bitmapImageChanged.getWidth(),bitmapImageChanged.getHeight());
                imageSvg.setImageBitmap(bitmapImageSvg);
                imageChanged.setImageBitmap(bitmapImageChanged);

                buttonActionGrey.setVisibility(View.INVISIBLE);
                buttonActionPartialDesaturation.setVisibility(View.INVISIBLE);
                buttonActionColorization.setVisibility(View.INVISIBLE);
                buttonActionConvolution.setVisibility(View.INVISIBLE);
                buttonActionConvolutionGauss.setVisibility(View.INVISIBLE);
                buttonActionFlouLateral.setVisibility(View.INVISIBLE);
                buttonActionEqHistogram.setVisibility(View.INVISIBLE);
                buttonActionLinearExtension.setVisibility(View.INVISIBLE);
            }
        });

        buttonActionConvolutionGauss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageSvg.setVisibility(View.VISIBLE);
                imageChanged.setVisibility(View.VISIBLE);
                buttonMenu.setVisibility(View.VISIBLE);
                buttonReset.setVisibility(View.VISIBLE);


                int[] tabpix = new int[bitmapImageChanged.getHeight()*bitmapImageChanged.getWidth()];
                bitmapImageChanged.getPixels(tabpix,0,bitmapImageChanged.getWidth(),0,0,bitmapImageChanged.getWidth(),bitmapImageChanged.getHeight());
                bitmapImageSvg.setPixels(tabpix,0,bitmapImageChanged.getWidth(),0,0,bitmapImageChanged.getWidth(),bitmapImageChanged.getHeight());
                convolutionGauss.applicationConvolution(bitmapImageChanged).getPixels(tabpix,0,bitmapImageChanged.getWidth(),0,0,bitmapImageChanged.getWidth(),bitmapImageChanged.getHeight());
                bitmapImageChanged.setPixels(tabpix,0,bitmapImageChanged.getWidth(),0,0,bitmapImageChanged.getWidth(),bitmapImageChanged.getHeight());
                imageSvg.setImageBitmap(bitmapImageSvg);
                imageChanged.setImageBitmap(bitmapImageChanged);

                buttonActionGrey.setVisibility(View.INVISIBLE);
                buttonActionPartialDesaturation.setVisibility(View.INVISIBLE);
                buttonActionColorization.setVisibility(View.INVISIBLE);
                buttonActionConvolution.setVisibility(View.INVISIBLE);
                buttonActionConvolutionGauss.setVisibility(View.INVISIBLE);
                buttonActionFlouLateral.setVisibility(View.INVISIBLE);
                buttonActionEqHistogram.setVisibility(View.INVISIBLE);
                buttonActionLinearExtension.setVisibility(View.INVISIBLE);
            }
        });

        buttonActionFlouLateral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageSvg.setVisibility(View.VISIBLE);
                imageChanged.setVisibility(View.VISIBLE);
                buttonMenu.setVisibility(View.VISIBLE);
                buttonReset.setVisibility(View.VISIBLE);


                int[] tabpix = new int[bitmapImageChanged.getHeight()*bitmapImageChanged.getWidth()];
                bitmapImageChanged.getPixels(tabpix,0,bitmapImageChanged.getWidth(),0,0,bitmapImageChanged.getWidth(),bitmapImageChanged.getHeight());
                bitmapImageSvg.setPixels(tabpix,0,bitmapImageChanged.getWidth(),0,0,bitmapImageChanged.getWidth(),bitmapImageChanged.getHeight());
                rightBlur.applicationConvolution(bitmapImageChanged).getPixels(tabpix,0,bitmapImageChanged.getWidth(),0,0,bitmapImageChanged.getWidth(),bitmapImageChanged.getHeight());
                bitmapImageChanged.setPixels(tabpix,0,bitmapImageChanged.getWidth(),0,0,bitmapImageChanged.getWidth(),bitmapImageChanged.getHeight());
                imageSvg.setImageBitmap(bitmapImageSvg);
                imageChanged.setImageBitmap(bitmapImageChanged);

                buttonActionGrey.setVisibility(View.INVISIBLE);
                buttonActionPartialDesaturation.setVisibility(View.INVISIBLE);
                buttonActionColorization.setVisibility(View.INVISIBLE);
                buttonActionConvolution.setVisibility(View.INVISIBLE);
                buttonActionConvolutionGauss.setVisibility(View.INVISIBLE);
                buttonActionFlouLateral.setVisibility(View.INVISIBLE);
                buttonActionEqHistogram.setVisibility(View.INVISIBLE);
                buttonActionLinearExtension.setVisibility(View.INVISIBLE);
            }
        });

        buttonActionColorization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageSvg.setVisibility(View.VISIBLE);
                imageChanged.setVisibility(View.VISIBLE);
                buttonMenu.setVisibility(View.VISIBLE);
                buttonReset.setVisibility(View.VISIBLE);


                int[] tabpix = new int[bitmapImageChanged.getHeight()*bitmapImageChanged.getWidth()];
                bitmapImageChanged.getPixels(tabpix,0,bitmapImageChanged.getWidth(),0,0,bitmapImageChanged.getWidth(),bitmapImageChanged.getHeight());
                bitmapImageSvg.setPixels(tabpix,0,bitmapImageChanged.getWidth(),0,0,bitmapImageChanged.getWidth(),bitmapImageChanged.getHeight());
                colorManipulation.convertImageColorization(bitmapImageChanged).getPixels(tabpix,0,bitmapImageChanged.getWidth(),0,0,bitmapImageChanged.getWidth(),bitmapImageChanged.getHeight());
                bitmapImageChanged.setPixels(tabpix,0,bitmapImageChanged.getWidth(),0,0,bitmapImageChanged.getWidth(),bitmapImageChanged.getHeight());
                imageSvg.setImageBitmap(bitmapImageSvg);
                imageChanged.setImageBitmap(bitmapImageChanged);

                buttonActionGrey.setVisibility(View.INVISIBLE);
                buttonActionPartialDesaturation.setVisibility(View.INVISIBLE);
                buttonActionColorization.setVisibility(View.INVISIBLE);
                buttonActionConvolution.setVisibility(View.INVISIBLE);
                buttonActionConvolutionGauss.setVisibility(View.INVISIBLE);
                buttonActionFlouLateral.setVisibility(View.INVISIBLE);
                buttonActionEqHistogram.setVisibility(View.INVISIBLE);
                buttonActionLinearExtension.setVisibility(View.INVISIBLE);
            }
        });

        buttonActionPartialDesaturation.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageSvg.setVisibility(View.VISIBLE);
                imageChanged.setVisibility(View.VISIBLE);
                buttonMenu.setVisibility(View.VISIBLE);
                buttonReset.setVisibility(View.VISIBLE);


                int[] tabpix = new int[bitmapImageChanged.getHeight()*bitmapImageChanged.getWidth()];
                bitmapImageChanged.getPixels(tabpix,0,bitmapImageChanged.getWidth(),0,0,bitmapImageChanged.getWidth(),bitmapImageChanged.getHeight());
                bitmapImageSvg.setPixels(tabpix,0,bitmapImageChanged.getWidth(),0,0,bitmapImageChanged.getWidth(),bitmapImageChanged.getHeight());
                colorManipulation.convertImageSelectiveDesaturation(bitmapImageChanged,Color.RED,50, 70,50).getPixels(tabpix,0,bitmapImageChanged.getWidth(),0,0,bitmapImageChanged.getWidth(),bitmapImageChanged.getHeight());
                bitmapImageChanged.setPixels(tabpix,0,bitmapImageChanged.getWidth(),0,0,bitmapImageChanged.getWidth(),bitmapImageChanged.getHeight());
                imageSvg.setImageBitmap(bitmapImageSvg);
                imageChanged.setImageBitmap(bitmapImageChanged);

                buttonActionGrey.setVisibility(View.INVISIBLE);
                buttonActionPartialDesaturation.setVisibility(View.INVISIBLE);
                buttonActionColorization.setVisibility(View.INVISIBLE);
                buttonActionConvolution.setVisibility(View.INVISIBLE);
                buttonActionConvolutionGauss.setVisibility(View.INVISIBLE);
                buttonActionFlouLateral.setVisibility(View.INVISIBLE);
                buttonActionEqHistogram.setVisibility(View.INVISIBLE);
                buttonActionLinearExtension.setVisibility(View.INVISIBLE);
            }
        }));

        buttonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageSvg.setVisibility(View.INVISIBLE);
                imageChanged.setVisibility(View.INVISIBLE);
                buttonMenu.setVisibility(View.INVISIBLE);
                buttonReset.setVisibility(View.INVISIBLE);

                buttonActionGrey.setVisibility(View.VISIBLE);
                buttonActionPartialDesaturation.setVisibility(View.VISIBLE);
                buttonActionColorization.setVisibility(View.VISIBLE);
                buttonActionConvolution.setVisibility(View.VISIBLE);
                buttonActionConvolutionGauss.setVisibility(View.VISIBLE);
                buttonActionFlouLateral.setVisibility(View.VISIBLE);
                buttonActionEqHistogram.setVisibility(View.VISIBLE);
                buttonActionLinearExtension.setVisibility(View.VISIBLE);
            }
        });

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageSvg.setVisibility(View.INVISIBLE);
                imageChanged.setVisibility(View.INVISIBLE);
                buttonMenu.setVisibility(View.INVISIBLE);
                buttonReset.setVisibility(View.INVISIBLE);

                int[] tabpix = new int[bitmapImageOg.getHeight()*bitmapImageOg.getWidth()];
                bitmapImageOg.getPixels(tabpix,0,bitmapImageChanged.getWidth(),0,0,bitmapImageChanged.getWidth(),bitmapImageChanged.getHeight());
                bitmapImageSvg.setPixels(tabpix,0,bitmapImageChanged.getWidth(),0,0,bitmapImageChanged.getWidth(),bitmapImageChanged.getHeight());
                bitmapImageChanged.setPixels(tabpix,0,bitmapImageOg.getWidth(),0,0,bitmapImageOg.getWidth(),bitmapImageOg.getHeight());

                buttonActionGrey.setVisibility(View.VISIBLE);
                buttonActionPartialDesaturation.setVisibility(View.VISIBLE);
                buttonActionColorization.setVisibility(View.VISIBLE);
                buttonActionConvolution.setVisibility(View.VISIBLE);
                buttonActionConvolutionGauss.setVisibility(View.VISIBLE);
                buttonActionFlouLateral.setVisibility(View.VISIBLE);
                buttonActionEqHistogram.setVisibility(View.VISIBLE);
                buttonActionLinearExtension.setVisibility(View.VISIBLE);
            }
        });
    }
}
