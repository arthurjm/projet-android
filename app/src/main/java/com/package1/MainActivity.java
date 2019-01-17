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

    Button buttonImg1;
    Button buttonImg2;
    Button buttonImg3;
    Button buttonMenu;

    Button buttonActionGrey;
    Button buttonActionColorization;
    Button buttonActionPartialDesaturation;
    Button buttonActionConvolution;
    Button buttonActionConvolutionGauss;
    Button buttonActionFlouLateral;
    Button buttonActionEqHistogram;
    Button buttonActionLinearExtension;

    ImageView imageSvg;
    ImageView imageChanged;

    public void changeView() {
        buttonActionGrey.setVisibility(View.INVISIBLE);
        buttonActionPartialDesaturation.setVisibility(View.INVISIBLE);
        buttonActionColorization.setVisibility(View.INVISIBLE);
        buttonActionConvolution.setVisibility(View.INVISIBLE);
        buttonActionConvolutionGauss.setVisibility(View.INVISIBLE);
        buttonActionFlouLateral.setVisibility(View.INVISIBLE);
        buttonActionEqHistogram.setVisibility(View.INVISIBLE);
        buttonActionLinearExtension.setVisibility(View.INVISIBLE);
        buttonImg1.setVisibility(View.INVISIBLE);
        buttonImg2.setVisibility(View.INVISIBLE);
        buttonImg3.setVisibility(View.INVISIBLE);
    }

    public void changeViewVisible() {
        buttonActionGrey.setVisibility(View.VISIBLE);
        buttonActionPartialDesaturation.setVisibility(View.VISIBLE);
        buttonActionColorization.setVisibility(View.VISIBLE);
        buttonActionConvolution.setVisibility(View.VISIBLE);
        buttonActionConvolutionGauss.setVisibility(View.VISIBLE);
        buttonActionFlouLateral.setVisibility(View.VISIBLE);
        buttonActionEqHistogram.setVisibility(View.VISIBLE);
        buttonActionLinearExtension.setVisibility(View.VISIBLE);
        buttonImg1.setVisibility(View.VISIBLE);
        buttonImg2.setVisibility(View.VISIBLE);
        buttonImg3.setVisibility(View.VISIBLE);
    }

    public void imageVisibility() {
        imageSvg.setVisibility(View.VISIBLE);
        imageChanged.setVisibility(View.VISIBLE);
        buttonMenu.setVisibility(View.VISIBLE);
    }

    public void imageVisibilityInvisible() {
        imageSvg.setVisibility(View.INVISIBLE);
        imageChanged.setVisibility(View.INVISIBLE);
        buttonMenu.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        //initialisation des images et bitmaps
        imageSvg = (ImageView) findViewById(R.id.imageViewSvg);
        imageChanged = (ImageView) findViewById(R.id.imageViewChange);
        Drawable draw = ContextCompat.getDrawable((this), R.drawable.image1);
        final Bitmap bitmapImageOg1 = ((BitmapDrawable) draw).getBitmap();
        draw = ContextCompat.getDrawable((this), R.drawable.image2);
        final Bitmap bitmapImageOg2 = ((BitmapDrawable) draw).getBitmap();
        draw = ContextCompat.getDrawable((this), R.drawable.image3);
        final Bitmap bitmapImageOg3 = ((BitmapDrawable) draw).getBitmap();

        final Bitmap bitmapImageChanged = bitmapImageOg1.copy(bitmapImageOg1.getConfig(), true);
        final Bitmap bitmapImageSvg = bitmapImageOg1.copy(bitmapImageOg1.getConfig(), true);

        //initialisation des boutons
        buttonMenu = findViewById(R.id.buttonBackMenu);

        buttonImg1 = findViewById(R.id.buttonImage1);
        buttonImg2 = findViewById(R.id.buttonImage2);
        buttonImg3 = findViewById(R.id.buttonImage3);

        buttonActionGrey = findViewById(R.id.buttonActionGrey);
        buttonActionPartialDesaturation = findViewById(R.id.buttonActionDesaturation);
        buttonActionColorization = findViewById(R.id.buttonActionColorization);
        buttonActionConvolution = findViewById(R.id.buttonActionConvo);
        buttonActionConvolutionGauss = findViewById(R.id.buttonActionGauss);
        buttonActionFlouLateral = findViewById(R.id.buttonActionBlur);
        buttonActionEqHistogram = findViewById(R.id.buttonActionEq);
        buttonActionLinearExtension = findViewById(R.id.buttonActionExtension);

        //initialisation des effets
        final ColorManipulation colorManipulation = new ColorManipulation();
        final Histogram histogram = new Histogram();
        final Convolution rightBlur = new Convolution((new int[][]{{0}, {0}, {0}, {0}, {0}, {0}, {0}, {1}, {1}, {1}, {1}, {1}, {1}, {1}, {1}}), 15, 1);
        final Convolution convolution = new Convolution(7);
        final Convolution convolutionGauss = new Convolution();

        //initialisation des elements visibles
        imageSvg.setImageBitmap(bitmapImageOg1);
        imageChanged.setImageBitmap(bitmapImageChanged);
        imageSvg.setVisibility(View.INVISIBLE);
        imageChanged.setVisibility(View.INVISIBLE);
        buttonMenu.setVisibility(View.INVISIBLE);

        changeViewVisible();

        //debut de la gestion des interactions avec les boutons
        buttonActionGrey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageVisibility();

                int[] tabpix = new int[bitmapImageChanged.getHeight() * bitmapImageChanged.getWidth()];
                bitmapImageChanged.getPixels(tabpix, 0, bitmapImageChanged.getWidth(), 0, 0, bitmapImageChanged.getWidth(), bitmapImageChanged.getHeight());
                bitmapImageSvg.setPixels(tabpix, 0, bitmapImageChanged.getWidth(), 0, 0, bitmapImageChanged.getWidth(), bitmapImageChanged.getHeight());
                colorManipulation.convertImageGreyScale(bitmapImageChanged).getPixels(tabpix, 0, bitmapImageChanged.getWidth(), 0, 0, bitmapImageChanged.getWidth(), bitmapImageChanged.getHeight());
                bitmapImageChanged.setPixels(tabpix, 0, bitmapImageChanged.getWidth(), 0, 0, bitmapImageChanged.getWidth(), bitmapImageChanged.getHeight());
                imageSvg.setImageBitmap(bitmapImageSvg);
                imageChanged.setImageBitmap(bitmapImageChanged);

                changeView();
            }
        });

        buttonActionEqHistogram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageVisibility();

                int[] tabpix = new int[bitmapImageChanged.getHeight() * bitmapImageChanged.getWidth()];
                bitmapImageChanged.getPixels(tabpix, 0, bitmapImageChanged.getWidth(), 0, 0, bitmapImageChanged.getWidth(), bitmapImageChanged.getHeight());
                bitmapImageSvg.setPixels(tabpix, 0, bitmapImageChanged.getWidth(), 0, 0, bitmapImageChanged.getWidth(), bitmapImageChanged.getHeight());
                histogram.applicationEqHistogram(bitmapImageChanged).getPixels(tabpix, 0, bitmapImageChanged.getWidth(), 0, 0, bitmapImageChanged.getWidth(), bitmapImageChanged.getHeight());
                bitmapImageChanged.setPixels(tabpix, 0, bitmapImageChanged.getWidth(), 0, 0, bitmapImageChanged.getWidth(), bitmapImageChanged.getHeight());
                imageSvg.setImageBitmap(bitmapImageSvg);
                imageChanged.setImageBitmap(bitmapImageChanged);

                changeView();
            }
        });

        buttonActionLinearExtension.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageVisibility();

                int[] tabpix = new int[bitmapImageChanged.getHeight() * bitmapImageChanged.getWidth()];
                bitmapImageChanged.getPixels(tabpix, 0, bitmapImageChanged.getWidth(), 0, 0, bitmapImageChanged.getWidth(), bitmapImageChanged.getHeight());
                bitmapImageSvg.setPixels(tabpix, 0, bitmapImageChanged.getWidth(), 0, 0, bitmapImageChanged.getWidth(), bitmapImageChanged.getHeight());
                histogram.applicationLinearExtension(bitmapImageChanged).getPixels(tabpix, 0, bitmapImageChanged.getWidth(), 0, 0, bitmapImageChanged.getWidth(), bitmapImageChanged.getHeight());
                bitmapImageChanged.setPixels(tabpix, 0, bitmapImageChanged.getWidth(), 0, 0, bitmapImageChanged.getWidth(), bitmapImageChanged.getHeight());
                imageSvg.setImageBitmap(bitmapImageSvg);
                imageChanged.setImageBitmap(bitmapImageChanged);

                changeView();
            }
        });

        buttonActionConvolution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageVisibility();

                int[] tabpix = new int[bitmapImageChanged.getHeight() * bitmapImageChanged.getWidth()];
                bitmapImageChanged.getPixels(tabpix, 0, bitmapImageChanged.getWidth(), 0, 0, bitmapImageChanged.getWidth(), bitmapImageChanged.getHeight());
                bitmapImageSvg.setPixels(tabpix, 0, bitmapImageChanged.getWidth(), 0, 0, bitmapImageChanged.getWidth(), bitmapImageChanged.getHeight());
                convolution.applicationConvolution(bitmapImageChanged).getPixels(tabpix, 0, bitmapImageChanged.getWidth(), 0, 0, bitmapImageChanged.getWidth(), bitmapImageChanged.getHeight());
                bitmapImageChanged.setPixels(tabpix, 0, bitmapImageChanged.getWidth(), 0, 0, bitmapImageChanged.getWidth(), bitmapImageChanged.getHeight());
                imageSvg.setImageBitmap(bitmapImageSvg);
                imageChanged.setImageBitmap(bitmapImageChanged);

                changeView();
            }
        });

        buttonActionConvolutionGauss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageVisibility();
                int[] tabpix = new int[bitmapImageChanged.getHeight() * bitmapImageChanged.getWidth()];
                bitmapImageChanged.getPixels(tabpix, 0, bitmapImageChanged.getWidth(), 0, 0, bitmapImageChanged.getWidth(), bitmapImageChanged.getHeight());
                bitmapImageSvg.setPixels(tabpix, 0, bitmapImageChanged.getWidth(), 0, 0, bitmapImageChanged.getWidth(), bitmapImageChanged.getHeight());
                convolutionGauss.applicationConvolution(bitmapImageChanged).getPixels(tabpix, 0, bitmapImageChanged.getWidth(), 0, 0, bitmapImageChanged.getWidth(), bitmapImageChanged.getHeight());
                bitmapImageChanged.setPixels(tabpix, 0, bitmapImageChanged.getWidth(), 0, 0, bitmapImageChanged.getWidth(), bitmapImageChanged.getHeight());
                imageSvg.setImageBitmap(bitmapImageSvg);
                imageChanged.setImageBitmap(bitmapImageChanged);

                changeView();
            }
        });

        buttonActionFlouLateral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageVisibility();

                int[] tabpix = new int[bitmapImageChanged.getHeight() * bitmapImageChanged.getWidth()];
                bitmapImageChanged.getPixels(tabpix, 0, bitmapImageChanged.getWidth(), 0, 0, bitmapImageChanged.getWidth(), bitmapImageChanged.getHeight());
                bitmapImageSvg.setPixels(tabpix, 0, bitmapImageChanged.getWidth(), 0, 0, bitmapImageChanged.getWidth(), bitmapImageChanged.getHeight());
                rightBlur.applicationConvolution(bitmapImageChanged).getPixels(tabpix, 0, bitmapImageChanged.getWidth(), 0, 0, bitmapImageChanged.getWidth(), bitmapImageChanged.getHeight());
                bitmapImageChanged.setPixels(tabpix, 0, bitmapImageChanged.getWidth(), 0, 0, bitmapImageChanged.getWidth(), bitmapImageChanged.getHeight());
                imageSvg.setImageBitmap(bitmapImageSvg);
                imageChanged.setImageBitmap(bitmapImageChanged);

                changeView();
            }
        });

        buttonActionColorization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageVisibility();

                int[] tabpix = new int[bitmapImageChanged.getHeight() * bitmapImageChanged.getWidth()];
                bitmapImageChanged.getPixels(tabpix, 0, bitmapImageChanged.getWidth(), 0, 0, bitmapImageChanged.getWidth(), bitmapImageChanged.getHeight());
                bitmapImageSvg.setPixels(tabpix, 0, bitmapImageChanged.getWidth(), 0, 0, bitmapImageChanged.getWidth(), bitmapImageChanged.getHeight());
                colorManipulation.convertImageColorization(bitmapImageChanged).getPixels(tabpix, 0, bitmapImageChanged.getWidth(), 0, 0, bitmapImageChanged.getWidth(), bitmapImageChanged.getHeight());
                bitmapImageChanged.setPixels(tabpix, 0, bitmapImageChanged.getWidth(), 0, 0, bitmapImageChanged.getWidth(), bitmapImageChanged.getHeight());
                imageSvg.setImageBitmap(bitmapImageSvg);
                imageChanged.setImageBitmap(bitmapImageChanged);

                changeView();
            }
        });

        buttonActionPartialDesaturation.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageVisibility();

                int[] tabpix = new int[bitmapImageChanged.getHeight() * bitmapImageChanged.getWidth()];
                bitmapImageChanged.getPixels(tabpix, 0, bitmapImageChanged.getWidth(), 0, 0, bitmapImageChanged.getWidth(), bitmapImageChanged.getHeight());
                bitmapImageSvg.setPixels(tabpix, 0, bitmapImageChanged.getWidth(), 0, 0, bitmapImageChanged.getWidth(), bitmapImageChanged.getHeight());
                colorManipulation.convertImageSelectiveDesaturation(bitmapImageChanged, Color.RED, 50, 70, 50).getPixels(tabpix, 0, bitmapImageChanged.getWidth(), 0, 0, bitmapImageChanged.getWidth(), bitmapImageChanged.getHeight());
                bitmapImageChanged.setPixels(tabpix, 0, bitmapImageChanged.getWidth(), 0, 0, bitmapImageChanged.getWidth(), bitmapImageChanged.getHeight());
                imageSvg.setImageBitmap(bitmapImageSvg);
                imageChanged.setImageBitmap(bitmapImageChanged);

                changeView();
            }
        }));

        buttonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageVisibilityInvisible();
                changeViewVisible();
            }
        });

        buttonImg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int[] tabpix = new int[bitmapImageOg1.getHeight() * bitmapImageOg1.getWidth()];
                bitmapImageOg1.getPixels(tabpix, 0, bitmapImageOg1.getWidth(), 0, 0, bitmapImageOg1.getWidth(), bitmapImageOg1.getHeight());
                bitmapImageSvg.setPixels(tabpix, 0, bitmapImageOg1.getWidth(), 0, 0, bitmapImageOg1.getWidth(), bitmapImageOg1.getHeight());
                bitmapImageChanged.setPixels(tabpix, 0, bitmapImageOg1.getWidth(), 0, 0, bitmapImageOg1.getWidth(), bitmapImageOg1.getHeight());

                imageSvg.setImageBitmap(bitmapImageSvg);
                imageChanged.setImageBitmap(bitmapImageChanged);

            }
        });

        buttonImg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int[] tabpix = new int[bitmapImageOg2.getHeight() * bitmapImageOg2.getWidth()];
                bitmapImageOg2.getPixels(tabpix, 0, bitmapImageOg2.getWidth(), 0, 0, bitmapImageOg2.getWidth(), bitmapImageOg2.getHeight());
                bitmapImageSvg.setPixels(tabpix, 0, bitmapImageOg2.getWidth(), 0, 0, bitmapImageOg2.getWidth(), bitmapImageOg2.getHeight());
                bitmapImageChanged.setPixels(tabpix, 0, bitmapImageOg2.getWidth(), 0, 0, bitmapImageOg2.getWidth(), bitmapImageOg2.getHeight());

                imageSvg.setImageBitmap(bitmapImageSvg);
                imageChanged.setImageBitmap(bitmapImageChanged);

            }
        });

        buttonImg3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int[] tabpix = new int[bitmapImageOg3.getHeight() * bitmapImageOg3.getWidth()];
                bitmapImageOg3.getPixels(tabpix, 0, bitmapImageOg3.getWidth(), 0, 0, bitmapImageOg3.getWidth(), bitmapImageOg3.getHeight());
                bitmapImageSvg.setPixels(tabpix, 0, bitmapImageOg3.getWidth(), 0, 0, bitmapImageOg3.getWidth(), bitmapImageOg3.getHeight());
                bitmapImageChanged.setPixels(tabpix, 0, bitmapImageOg3.getWidth(), 0, 0, bitmapImageOg3.getWidth(), bitmapImageOg3.getHeight());

                imageSvg.setImageBitmap(bitmapImageSvg);
                imageChanged.setImageBitmap(bitmapImageChanged);

            }
        });

        //fin de la gestion des interactions avec les boutons
    }
}
