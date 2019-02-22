package com.package1;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.RenderScript;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.rssample.ScriptC_colorize;
import com.android.rssample.ScriptC_grey;
import com.android.rssample.ScriptC_keepHue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
public class Test extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    /**
     * Seekbar
     */
    private SeekBar seekBar;

    private SeekBar seekBarArthur;
    /**
     * TextView
     */
    private TextView text;
    /**
     * Valeur actuelle grace à la seekBar
     */
    private int actualValue;
    /**
     * Spinner
     */
    private Spinner spinner;
    /**
     * L'image que l'on souhaite traiter.
     */
    private Bitmap image;
    /**
     * L'image que l'on souhaite afficher apres application d'une fonction.
     */
    private Bitmap image_copy;
    /**
     * Load l'image que l'on veut.
     */
    private ImageView imgView;
    /**
     * ArrayList de boutons.
     *
     * @see Button
     */
    public ArrayList<Button> buttonList;
    private Uri filePath;
    private String imagepath = null;
    private int PICK_IMAGE_REQUEST = 1;

    /**
     * Permet d'initialiser les trucs
     */
    public void initiate() {
        // Test
        buttonList = new ArrayList<>();
        Button tb;
        tb = findViewById(R.id.buttonLoadPhoto);
        buttonList.add(tb);
        tb = findViewById(R.id.buttonTakePhoto);
        buttonList.add(tb);
        tb = findViewById(R.id.buttonUndo);
        buttonList.add(tb);
        // Fin test

        seekBar = findViewById(R.id.seekBar);
        text = findViewById(R.id.textView);
        spinner = findViewById(R.id.spinner);
        seekBar.setMax(100);
        text.setText("Value : " + seekBar.getProgress() + "/" + seekBar.getMax());
        seekBar.setVisibility(View.GONE);
        text.setVisibility(View.GONE);

        // ARTHUR

        seekBarArthur = findViewById(R.id.seekBar2);

        // Test spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Test_fonctions, android.R.layout.test_list_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }


    public void addListener() {

        // Les fonctions sont la automatiquement
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                actualValue = progress;
                text.setText("Value : " + progress + "/" + seekBar.getMax());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                text.setText("Value : " + actualValue + "/" + seekBar.getMax());
                keepHueRs(image_copy, actualValue);
                imgView.setImageBitmap(image_copy);
            }
        });

        buttonList.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
                imgView.setImageBitmap(image_copy);

            }
        });
        buttonList.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }
        });
        buttonList.get(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //seekBar.setVisibility(View.GONE);
                //text.setVisibility(View.GONE);
                if(image != null) {
                    undo(image, imgView);
                    image_copy = image.copy(Bitmap.Config.ARGB_8888, true);
                }
            }
        });
    }

    protected void onCreate(Bundle savedUnstanceState) {
        super.onCreate(savedUnstanceState);
        setContentView(R.layout.test);

        imgView = findViewById(R.id.imageTest2);

        initiate();
        addListener();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String sSelected = parent.getItemAtPosition(position).toString();
        String test = "test";

        if (image_copy != null) {
            switch (position) {
                case 0 :
                    seekBar.setVisibility(View.GONE);
                    text.setVisibility(View.GONE);
                    break;
                case 1:
                    seekBar.setVisibility(View.GONE);
                    text.setVisibility(View.GONE);
                    toGreyRS(image_copy);
                    imgView.setImageBitmap(image_copy);
                    break;
                case 2:
                    seekBar.setVisibility(View.GONE);
                    text.setVisibility(View.GONE);
                    keepRed(image_copy);
                    imgView.setImageBitmap(image_copy);
                    break;
                case 3 :
                    seekBar.setVisibility(View.VISIBLE);
                    text.setVisibility(View.VISIBLE);
                    seekBar.setMax(255);
                    //keepHue(image_copy, actualValue);
                    //imgView.setImageBitmap(image_copy);
                    break;
                case 4:
                    seekBar.setVisibility(View.VISIBLE);
                    text.setVisibility(View.VISIBLE);
                    seekBar.setMax(360);
                    colorizeRS(image_copy);
                    imgView.setImageBitmap(image_copy);
                    break;
                    // CAS ARTHUR

                case 5 :
                    seekBar.setVisibility(View.VISIBLE);
                    seekBarArthur.setVisibility(View.VISIBLE);
                    text.setVisibility(View.VISIBLE);
                    seekBar.setMax(360);
                    seekBarArthur.setMax(150);

                    break;
            }
        }
        Toast.makeText(this, test, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        // Load photo
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            imagepath = getPath(filePath);

            try {
                image = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //image = rotateBitmap(imagepath);
               /* imgView.setImageBitmap(image);
                image_copy = image.copy(Bitmap.Config.ARGB_8888, true);*/


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Prendre photo
        else {
            image = (Bitmap) data.getExtras().get("data");
        }

        imgView.setImageBitmap(image);
        image_copy = image.copy(Bitmap.Config.ARGB_8888, true);
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    // ZZZ

    public void toGreyRS(Bitmap bmp) {

        RenderScript rs = RenderScript.create(this);

        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());

        ScriptC_grey greyScript = new ScriptC_grey(rs);

        greyScript.forEach_toGrey(input, output);

        output.copyTo(bmp);

        input.destroy();
        output.destroy();
        greyScript.destroy();
        rs.destroy();
    }

    public void keepRed(Bitmap bmp) {
        int w = bmp.getWidth();
        int h = bmp.getHeight();
        int[] pixels = new int[w * h];
        bmp.getPixels(pixels, 0, w, 0, 0, w, h);

        for (int i = 0; i < h * w; i++) {

            // On prends les references couleurs
            int r = Color.red(pixels[i]);
            int g = Color.green(pixels[i]);
            int b = Color.blue(pixels[i]);

            // Rouge
            if (r < g + b) {
                int gray = (int) Math.round(0.3 * Color.red(pixels[i]) + 0.59 * Color.green(pixels[i]) + 0.11 * Color.blue(pixels[i]));
                pixels[i] = Color.rgb(gray, gray, gray);
            }

        }
        bmp.setPixels(pixels, 0, w, 0, 0, w, h);
        imgView.setImageBitmap(bmp);

    }

    public void undo(Bitmap bmp, ImageView img) {
        img.setImageBitmap(bmp);
    }

    // Garde seulement une certaine teinte, passée en paramètre, sur une image

    private void colorizeRS(Bitmap bmp) {
        RenderScript rs = RenderScript.create(this);

        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());

        ScriptC_colorize colorizeScript = new ScriptC_colorize(rs);

        Random r = new Random();
        int hue = r.nextInt(360);
        colorizeScript.set_hue(hue);

        colorizeScript.forEach_colorize(input, output);

        output.copyTo(bmp);

        input.destroy();
        output.destroy();
        colorizeScript.destroy();
        rs.destroy();
    }

    // Garde seulement une certaine teinte, passée en paramètre, sur une image
    private void keepHueRs(Bitmap bmp, int hue) {
        RenderScript rs = RenderScript.create(this);

        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());

        ScriptC_keepHue keepHueScript = new ScriptC_keepHue(rs);

        keepHueScript.set_hue(hue);
        keepHueScript.forEach_keepHue(input, output);

        output.copyTo(bmp);

        input.destroy();
        output.destroy();
        keepHueScript.destroy();
        rs.destroy();
    }
}
