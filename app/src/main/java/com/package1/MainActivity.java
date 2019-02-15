package com.package1;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView = null;
    private Button Album;
    private Button buttonImg2;
    private Button buttonImg3;
    private Bitmap bmp;
    private BitmapFactory.Options o;
    ColorManipulation colorManipulation;
    Histogram histogram;
    Convolution rightBlur;
    Convolution convolution;
    Convolution convolutionGauss;

    public int[] tabpix;

    private SeekBar seekbar;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        o = new BitmapFactory.Options();
        o.inMutable = true;
        o.inScaled = false;

        imageView = (ImageView) findViewById(R.id.imageViewSvg);

        Album = (Button) findViewById(R.id.Album);
        buttonImg2 = (Button) findViewById(R.id.buttonImage2);
        buttonImg3 = (Button) findViewById(R.id.buttonImage3);

        Album.setOnClickListener(listener);
        buttonImg2.setOnClickListener(listener);
        buttonImg3.setOnClickListener(listener);

        seekbar = (SeekBar) findViewById(R.id.seekBar);
        tv = (TextView) findViewById(R.id.text);
        seekbar.setOnSeekBarChangeListener(seekListerner);


        //initialisation des effets

        colorManipulation = new ColorManipulation();

        rightBlur = new Convolution((new int[][]{{0}, {0}, {0}, {0}, {0}, {0}, {0}, {1}, {1}, {1}, {1}, {1}, {1}, {1}, {1}}), 15, 1);
        convolution = new Convolution(7);
        convolutionGauss = new GaussienMask(7, 4.5);
    }


    private View.OnClickListener listener = new View.OnClickListener() {
        public void onClick(View v) {
            if (v == Album) {


            }
            if (v == buttonImg2) {
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.image2, o);
                imageView = (ImageView) findViewById(R.id.imageViewSvg);
                imageView.setImageBitmap(bmp);

            }
            if (v == buttonImg3) {
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.image3, o);
                imageView = (ImageView) findViewById(R.id.imageViewSvg);
                imageView.setImageBitmap(bmp);
            }

        }
    };

    private SeekBar.OnSeekBarChangeListener seekListerner = new SeekBar.OnSeekBarChangeListener() {


        public void onStopTrackingTouch(SeekBar seekBar) {


        }


        public void onStartTrackingTouch(SeekBar seekBar) {


        }


        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            tv.setText("Value:" + Integer.toString(progress));
        }
    };







    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.toGray:
                imageView.setImageBitmap(colorManipulation.convertImageGreyScale(bmp));
                break;
            case R.id.toGrayLDD:

                break;
            case R.id.colorLDE:

                break;
            case R.id.toGrayHistEqual:

                break;
            case R.id.colorHistEqual:

                break;
            case R.id.color_rest:
                imageView.setImageBitmap(colorManipulation.convertImageColorization(bmp,seekbar.getProgress()));
                break;
            case R.id.colorize:

                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        Toast.makeText(getApplicationContext(), "You have clicked on " + item.getTitle(), Toast.LENGTH_SHORT).show();
        return true;
    }

}

