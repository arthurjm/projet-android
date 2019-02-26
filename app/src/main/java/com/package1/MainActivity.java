package com.package1;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView = null;
    private Button Album;
    private Button save;
    private Button camera;

    private Bitmap image;
    private BitmapFactory.Options o;
    ColorManipulation colorManipulation;
    Convolution rightBlur;
    Convolution convolution;
    Convolution convolutionGauss;
    private SeekBar seekbar;
    private TextView tv;
    private int PICK_IMAGE_REQUEST = 1;
    private Uri filePath;
    private String imagepath = null;
    private Bitmap image_copy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        o = new BitmapFactory.Options();
        o.inMutable = true;
        o.inScaled = false;

        imageView = (ImageView) findViewById(R.id.imageViewSvg);

        Album = (Button) findViewById(R.id.Album);
        save = (Button) findViewById(R.id.save);
        camera = (Button) findViewById(R.id.camera);

        Album.setOnClickListener(listener);
        save.setOnClickListener(listener);
        camera.setOnClickListener(listener);

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
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
                imageView.setImageBitmap(image_copy);
            }
            if (v == save) {
               saveImageToGallery();
                Toast.makeText(getApplicationContext(), "image saved", Toast.LENGTH_SHORT).show();
            }
            if (v == camera) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }

        }
    };

    private void saveImageToGallery() {
        //save image
        File dir = new File(Environment.getExternalStorageDirectory(),"image");
        System.out.println(dir);
        if (!dir.exists()) {
            dir.mkdir();
        }
        final String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(dir, fileName);
        //System.out.println(file);
        try {
            FileOutputStream out = new FileOutputStream(file);
            System.out.println(out);
            image_copy.compress(Bitmap.CompressFormat.JPEG, 100, out);
            System.out.println(image);
            out.flush();
            Log.i("isSucess", "?");
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println(e);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //add file to gallery
        try {
            MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.KITKAT) {
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file" + fileName)));
        }
        else{
            MediaScannerConnection.scanFile(this, new String[]{fileName},null,null);
        }
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Prendre photo
        else {
            image = (Bitmap) data.getExtras().get("data");
        }

        imageView.setImageBitmap(image);
        image_copy = image.copy(Bitmap.Config.ARGB_8888, true);
    }

    /**
     * Permet de recuperer le chemin de l'image.
     *
     * @param uri
     * @return
     */
    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    
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
                imageView.setImageBitmap(colorManipulation.convertImageGreyScale(image));
                break;
            case R.id.toGrayLDD:

                break;
            case R.id.colorLDE:

                break;
            case R.id.toGrayHistEqual:

                break;
            case R.id.colorHistEqual:
                HistogramManipulation hist=new HistogramManipulation(image,ChanelType.B);
                hist.isophelieLut(2);
                Log.e("TAG",hist.histogram.toString());
                imageView.setImageBitmap(hist.applyLut(image));
                Log.e("TAG", "FINI");
                break;
            case R.id.color_rest:
                imageView.setImageBitmap(colorManipulation.convertImageColorization(image,seekbar.getProgress()));
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

