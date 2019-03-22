package com.package1.affichage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.package1.ChanelType;
import com.package1.HistogramManipulation;
import com.package1.Mask.BlurMask;
import com.package1.Mask.GaussianBlur;
import com.package1.Mask.LaplacienMask;
import com.package1.Mask.SobelMask;
import com.package1.R;
import com.package1.RS;

import java.util.List;

import static com.package1.MainActivity.imageEditing;
import static com.package1.MainActivity.imageEditingCopy;
import static com.package1.MainActivity.imgView;
import static com.package1.affichage.PhotoRecycler.faceDetection;
import static com.package1.affichage.PhotoRecycler.hist;
import static com.package1.affichage.PhotoRecycler.renderscript;
import static com.package1.affichage.PhotoRecycler.seekBar1;
import static com.package1.affichage.PhotoRecycler.seekBar2;

/**
 * @author Mathieu
 */
public class RecyclerViewHorizontalListAdapter extends RecyclerView.Adapter<RecyclerViewHorizontalListAdapter.PhotoViewHolder> {

    /**
     * List of FilterStruct
     *
     * @see FilterStruct
     */
    private List<FilterStruct> horizontalPhotoList;
    /**
     * a variable of type Context
     *
     * @see Context
     */
    private Context context;

    private FilterType filterType;
    /**
     * Vale of the seekBar
     *
     * @see PhotoRecycler#seekBar1
     */
    private int progressBar1;
    /**
     * Vale of the seekBar
     *
     * @see PhotoRecycler#seekBar2
     */
    private int progressBar2;

    private RecyclerType rt;

    public void setFilterType(FilterType filterType) {
        this.filterType = filterType;
    }

    /**
     * A Construction
     *
     * @param horizontalPhotoList
     * @param context
     */
    public RecyclerViewHorizontalListAdapter(List<FilterStruct> horizontalPhotoList, Context context, RecyclerType rt) {
        this.horizontalPhotoList = horizontalPhotoList;
        this.context = context;
        this.rt = rt;

    }

    /**
     * To initialise the viewHolder
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View photoProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_list_photo, parent, false);
        return new PhotoViewHolder(photoProductView);

    }

    /**
     * Permet de spécifier le continu de notre RecyclerView
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final PhotoViewHolder holder, final int position) {

        // Recup la photo avec la fonction associes
        holder.imageView.setImageBitmap(horizontalPhotoList.get(position).getImage());
        holder.txtview.setText(horizontalPhotoList.get(position).getFilterName());

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                useFunction(horizontalPhotoList.get(position).getFilterType());
                // Action lorsque l'on clique sur l'image
                String productName = horizontalPhotoList.get(position).getFilterName().toString();
                Toast.makeText(context, productName + " is selected", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * Le nombre d'element present dans la liste
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return horizontalPhotoList.size();
    }

    /**
     * Permet de mettre a Gone la view d'une seekBar
     *
     * @param sb
     * @see View#GONE
     */
    private void setGone(SeekBar sb) {
        sb.setVisibility(View.GONE);
    }

    /**
     * Permet de mettre a Gone la view de deux seekBar
     *
     * @param sb
     * @param sb1
     */
    private void setGone(SeekBar sb, SeekBar sb1) {
        setGone(sb);
        setGone(sb1);
    }

    /**
     * Permet de mettre a Visible la view d'une seekBar
     *
     * @param sb
     * @see View#VISIBLE
     */
    private void setVisible(SeekBar sb) {
        sb.setVisibility(View.VISIBLE);
    }

    /**
     * Permet de mettre a Visible la view de deux seekbar
     *
     * @param sb
     * @param sb1
     * @see View#VISIBLE
     */
    private void setVisible(SeekBar sb, SeekBar sb1) {
        sb.setVisibility(View.VISIBLE);
        sb1.setVisibility(View.VISIBLE);
    }

    /**
     * Permet de choisir la valeur max d'une seekBar
     *
     * @param sb
     * @param max
     * @see SeekBar#setMax(int)
     */
    private void setBorn(SeekBar sb, int max) {
        sb.setMax(max);
    }

    /**
     * Permet de mettre l'arriere plan d'une seekBar en RGB
     *
     * @param sb
     * @see SeekBar#setBackgroundResource(int)
     */
    private void setRGBBackground(SeekBar sb) {
        sb.setBackgroundResource(R.drawable.seekbar_progess);
    }

    /**
     * Permet de mettre l'arriere plan d'un seekBar à normal
     *
     * @param sb
     * @see SeekBar#setBackgroundResource(int)
     */
    private void setNormalBackground(SeekBar sb) {
        sb.setBackgroundResource(0);
    }

    /**
     * Permet 2 choses :
     * - si la fonction n'a pas besoin de seekBar, alors on n'affiche pas de seekBar, et on change l'imageView avec le resultat de la fonction
     *
     * @see PhotoRecycler#seekBar1
     * @see com.package1.MainActivity#imgView
     * - si la fonction a besoin d'une seekbar, alors on l'affiche et on definit ses parametres , et on change l'était actualFonction
     * @see PhotoRecycler#
     */
    private void useFunction(FilterType type) {
        renderscript = new RS(context);
        addListener();
        // If we want reset imageView
        //imageEditingCopy = imageEditing.copy(Bitmap.Config.ARGB_8888, true);
        imgView.setImageBitmap(imageEditingCopy);

        // Which functions we want to use with the adequat List
        switch (rt) {
            case Color:
                colorFunction(type);
                break;
            case Contrast:
                saturationFunction(type);
                break;
            case Mask:
                maskFunction(type);
                break;
            case Extras:
                extraFunction(type);
                break;
            default:
                break;


        }
        imgView.setImageBitmap(imageEditingCopy);
    }

    private void colorFunction(FilterType type) {
        switch (type) {
            case Grey:
                setGone(seekBar1, seekBar2);
                imageEditingCopy = renderscript.toGrey(imageEditing);
                break;
            case Colorize:
                setVisible(seekBar1);
                setGone(seekBar2);
                setBorn(seekBar1, 359);
                setRGBBackground(seekBar1);
                seekBar1.setProgress(0);
                setFilterType(FilterType.Colorize);
                break;
            case KeepHue:
                setVisible(seekBar1, seekBar2);
                setBorn(seekBar1, 359);
                setBorn(seekBar2, 180);
                setRGBBackground(seekBar1);
                seekBar1.setProgress(0);
                seekBar2.setProgress(0);
                setFilterType(FilterType.KeepHue);
                break;
            case Invert:
                setGone(seekBar1, seekBar2);
                imageEditingCopy = renderscript.invert(imageEditing);
                break;
            default:
                break;
        }
    }

    private void saturationFunction(FilterType type) {
        switch (type) {
            case Contrast:
                setVisible(seekBar1);
                setGone(seekBar2);
                setBorn(seekBar1, 127);
                setNormalBackground(seekBar1);
                seekBar1.setProgress(0);
                setFilterType(FilterType.Contrast);
                break;
            case ShiftLight:
                setVisible(seekBar1);
                setGone(seekBar2);
                setBorn(seekBar1, 200);
                setNormalBackground(seekBar1);
                seekBar1.setProgress(0);
                setFilterType(FilterType.ShiftLight);
                break;
            case ShiftSaturation:
                setVisible(seekBar1);
                setGone(seekBar2);
                setBorn(seekBar1, 200);
                setNormalBackground(seekBar1);
                seekBar1.setProgress(0);
                setFilterType(FilterType.ShiftSaturation);
                break;
            case ShiftColor:
                setVisible(seekBar1);
                setGone(seekBar2);
                setBorn(seekBar1, 255);
                setRGBBackground(seekBar1);
                seekBar1.setProgress(0);
                setFilterType(FilterType.ShiftColor);
                break;
            case Isohelie:
                setVisible(seekBar1);
                setGone(seekBar2);
                setBorn(seekBar1, 8);
                setNormalBackground(seekBar1);
                seekBar1.setProgress(2);
                setFilterType(FilterType.Isohelie);
                break;
            case EquaLight:
                setGone(seekBar1, seekBar2);
                setFilterType(FilterType.EquaLight);
                /*hist = new HistogramManipulation(imageEditing, ChanelType.V);
                hist.equalizationLUT();
                imageEditingCopy = hist.applyLUT(imageEditing);
                imageEditingCopy = renderscript.applyLUT(imageEditing, hist);*/
                new MyTask().execute(imageEditing);
                break;
            default:
                break;

        }
    }

    private void maskFunction(FilterType type) {
        switch (type) {
            case Blur:
                setNormalBackground(seekBar1);
                setVisible(seekBar1);
                setGone(seekBar2);
                seekBar1.setProgress(0);
                setBorn(seekBar1, 14);
                setFilterType(FilterType.Blur);
                break;
            case Gaussian:
                setNormalBackground(seekBar1);
                setVisible(seekBar1);
                setGone(seekBar2);
                seekBar1.setProgress(0);
                setBorn(seekBar1, 5);
                setFilterType(FilterType.Gaussian);
                break;
            case Laplacien:
                setGone(seekBar1, seekBar2);
                LaplacienMask maskLaplacien = new LaplacienMask();
                imageEditingCopy = renderscript.convolution(imageEditing, maskLaplacien);
                break;
            case SobelV:
                setGone(seekBar1, seekBar2);
                SobelMask sobelMaskVertical = new SobelMask(true);
                imageEditingCopy = renderscript.convolution(imageEditing, sobelMaskVertical);
                break;
            case SobelH:
                setGone(seekBar1, seekBar2);
                SobelMask sobelMaskHorizontal = new SobelMask(false);
                imageEditingCopy = renderscript.convolution(imageEditing, sobelMaskHorizontal);
                break;
            default:
                break;

        }
    }

    private void extraFunction(FilterType type) {
        switch (type) {
            case FaceDetection:
                setGone(seekBar1, seekBar2);
                imageEditingCopy = faceDetection.putSunglass(imageEditing);
                break;
            case Rotate:
                setGone(seekBar1, seekBar2);
                imageEditingCopy = RotateBitmap(imageEditingCopy, 90);
                break;
            default:
                break;

        }
    }

    /**
     * Rotate a bitmap
     *
     * @param source
     * @param angle
     * @return bitmap flip with angle
     */
    public Bitmap RotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    /**
     * Apply functions with value of seekBar
     *
     * @see PhotoRecycler#seekBar1
     */
  /*  private void applyFunction() {

        switch (filterType) {
            case Colorize:
                imageEditingCopy = (renderscript.colorize(imageEditing, progressBar1));
                break;
            case KeepHue:
                imageEditingCopy = renderscript.keepHue(imageEditing, progressBar1, progressBar2);
                break;
            case Contrast:
                hist = new HistogramManipulation(imageEditing, ChanelType.V);
                hist.linearExtensionLUT(128 + progressBar1, 127 - progressBar1);
                imageEditingCopy = hist.applyLUT(imageEditing); //java version
                imageEditingCopy = renderscript.applyLUT(imageEditing, hist);
                break;
            case ShiftLight:
                hist = new HistogramManipulation(imageEditing, ChanelType.V);
                hist.shiftLUT(progressBar1 - 100);
                imageEditingCopy = hist.applyLUT(imageEditing); //java version
                imageEditingCopy = renderscript.applyLUT(imageEditing, hist);
                break;
            case ShiftSaturation:
                hist = new HistogramManipulation(imageEditing, ChanelType.S);
                hist.shiftLUT(progressBar1 - 100);
                imageEditingCopy = hist.applyLUT(imageEditing); //java version
                imageEditingCopy = renderscript.applyLUT(imageEditing, hist);
                break;
            case ShiftColor:
                hist = new HistogramManipulation(imageEditing, ChanelType.H);
                hist.shiftCycleLUT(progressBar1);
                imageEditingCopy = hist.applyLUT(imageEditing); //java version
                imageEditingCopy = renderscript.applyLUT(imageEditing, hist);
                break;
            case Isohelie:
                hist = new HistogramManipulation(imageEditing, ChanelType.V);
                hist.isohelieLUT(progressBar1 + 2);
                imageEditingCopy = hist.applyLUT(imageEditing); //java version
                imageEditingCopy = renderscript.applyLUT(imageEditing, hist);
                break;
            case Blur:
                BlurMask mask = new BlurMask(progressBar1 + 1);
                imageEditingCopy = renderscript.convolution(imageEditing, mask);
                break;
            case Gaussian:
                GaussianBlur maskGaussian = new GaussianBlur(progressBar1 * 2 + 1, 2.5);
                imageEditingCopy = renderscript.convolution(imageEditing, maskGaussian);
            default:
                break;
        }

        imgView.setImageBitmap(imageEditingCopy);
    }*/


    private class MyTask extends AsyncTask<Bitmap,Void,Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(context, "  fonction start", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Bitmap doInBackground(Bitmap... Bitmap) {
            switch (filterType) {
                case EquaLight:
                    hist = new HistogramManipulation(Bitmap[0], ChanelType.V);
                    hist.equalizationLUT();
                    imageEditingCopy = hist.applyLUT(Bitmap[0]);
                    break;
                case Colorize:
                    imageEditingCopy = (renderscript.colorize(Bitmap[0], progressBar1));
                    break;
                case KeepHue:
                    imageEditingCopy = renderscript.keepHue(Bitmap[0], progressBar1, progressBar2);
                    break;
                case Contrast:
                    hist = new HistogramManipulation(Bitmap[0], ChanelType.V);
                    hist.linearExtensionLUT(128 + progressBar1, 127 - progressBar1);
                    //imageEditingCopy = hist.applyLUT(imageEditing); //java version
                    imageEditingCopy = renderscript.applyLUT(Bitmap[0], hist);
                    break;

                case ShiftLight:
                    hist = new HistogramManipulation(Bitmap[0], ChanelType.V);
                    hist.shiftLUT(progressBar1 - 100);
                    //imageEditingCopy = hist.applyLUT(imageEditing); //java version
                    imageEditingCopy = renderscript.applyLUT(Bitmap[0], hist);
                    break;

                case ShiftSaturation:
                    hist = new HistogramManipulation(Bitmap[0], ChanelType.S);
                    hist.shiftLUT(progressBar1 - 100);
                    //imageEditingCopy = hist.applyLUT(imageEditing); //java version
                    imageEditingCopy = renderscript.applyLUT(Bitmap[0], hist);
                    break;

                case ShiftColor:
                    hist = new HistogramManipulation(Bitmap[0], ChanelType.H);
                    hist.shiftCycleLUT(progressBar1);
                    //imageEditingCopy = hist.applyLUT(imageEditing); //java version
                    imageEditingCopy = renderscript.applyLUT(Bitmap[0], hist);

                    break;
                case Isohelie:
                    hist = new HistogramManipulation(Bitmap[0], ChanelType.V);
                    hist.isohelieLUT(progressBar1 + 2);
                    //imageEditingCopy = hist.applyLUT(imageEditing); //java version
                    imageEditingCopy = renderscript.applyLUT(Bitmap[0], hist);
                    break;

                case Blur:
                    BlurMask mask = new BlurMask(progressBar1 + 1);
                    imageEditingCopy = renderscript.convolution(Bitmap[0], mask);
                    break;

                case Gaussian:
                    GaussianBlur maskGaussian = new GaussianBlur(progressBar1 * 2 + 1, 2.5);
                    imageEditingCopy = renderscript.convolution(Bitmap[0], maskGaussian);
                    break;

                default:
                    break;

            }
            return imageEditingCopy;
        }
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imgView.setImageBitmap(bitmap);
            Toast.makeText(context, "  fonction end", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Permet d'ajouter des actions
     */
    private void addListener() {

        // Les fonctions sont la automatiquement
        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressBar1 = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                new MyTask().execute(imageEditing);
                //applyFunction();
            }
        });

        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressBar2 = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                new MyTask().execute(imageEditing);
                //applyFunction();
            }
        });


    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView txtview;

        public PhotoViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.idProductImage);
            txtview = view.findViewById(R.id.idProductName);
        }
    }
}