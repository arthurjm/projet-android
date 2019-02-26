package com.package1;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

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
    private int precisionbar;
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
    private RS renderScript;

    public ArrayList<Button> buttonList;
    private Uri filePath;
    private String imagepath = null;
    private int PICK_IMAGE_REQUEST = 1;

    /**
     * Permet d'initialiser les trucs
     */
    public void initiate() {
        buttonList = new ArrayList<>();
        Button tb;

        tb = findViewById(R.id.buttonLoadPhoto);
        buttonList.add(tb);
        tb = findViewById(R.id.buttonTakePhoto);
        buttonList.add(tb);
        tb = findViewById(R.id.buttonUndo);
        buttonList.add(tb);

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
                image_copy = image.copy(Bitmap.Config.ARGB_8888, true);
                //keepHue(image_copy, actualValue, precisionbar);
                // changeLuminosite(image_copy, actualValue);
                imgView.setImageBitmap(image_copy);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                text.setText("Value : " + actualValue + "/" + seekBar.getMax());
                image_copy = image.copy(Bitmap.Config.ARGB_8888, true);
               // keepHue(image_copy, actualValue, precisionbar);
                // changeLuminosite(image_copy, actualValue);
                imgView.setImageBitmap(image_copy);
            }
        });

        seekBarArthur.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                precisionbar = progress;

                text.setText("Value : " + progress + "/" + seekBar.getMax());
                image_copy = image.copy(Bitmap.Config.ARGB_8888, true);
                //keepHue(image_copy, actualValue, precisionbar);
                imgView.setImageBitmap(image_copy);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                text.setText("Value : " + actualValue + "/" + seekBar.getMax());
                image_copy = image.copy(Bitmap.Config.ARGB_8888, true);
                //keepHue(image_copy, actualValue, precisionbar);
                imgView.setImageBitmap(image_copy);
            }
        });

        buttonList.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, 1);

                // ZZZZ

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
                if (image != null) {
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
        renderScript = new RS(getApplicationContext());

        if (image_copy != null) {
            switch (position) {
                case 0:
                    seekBar.setVisibility(View.GONE);
                    text.setVisibility(View.GONE);
                    break;
                case 1:
                    seekBar.setVisibility(View.GONE);
                    text.setVisibility(View.GONE);
                    renderScript.toGrey(image_copy);
                    imgView.setImageBitmap(image_copy);
                    break;
                case 2:
                    seekBar.setVisibility(View.GONE);
                    text.setVisibility(View.GONE);

                    //keepRed(image_copy);
                    imgView.setImageBitmap(image_copy);
                    break;
                case 3:
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
                    //renderScript.colorize(image_copy, 150);
                    imgView.setImageBitmap(image_copy);
                    break;
                // CAS ARTHUR

                case 5:
                    seekBar.setVisibility(View.VISIBLE);
                    seekBarArthur.setVisibility(View.VISIBLE);
                    text.setVisibility(View.VISIBLE);
                    seekBar.setMax(36);
                    seekBarArthur.setMax(10);

                    break;
                case 6:
                    seekBar.setVisibility(View.VISIBLE);
                    seekBarArthur.setVisibility(View.INVISIBLE);
                    text.setVisibility(View.VISIBLE);
                    seekBar.setMax(100);
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


    public void undo(Bitmap bmp, ImageView img) {
        img.setImageBitmap(bmp);
    }


}
