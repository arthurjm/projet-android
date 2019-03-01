package com.package1.affichage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.package1.ChanelType;
import com.package1.HistogramManipulation;
import com.package1.Mask.BlurMask;
import com.package1.Mask.GaussianBlur;
import com.package1.Mask.LaplacienMask;
import com.package1.R;
import com.package1.RS;

import java.util.List;

import static com.package1.MainActivity.imageEditing;
import static com.package1.MainActivity.imageEditingCopy;
import static com.package1.MainActivity.imgView;
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
    /**
     * a variable of type string to represent the fonction actual
     */
    private String actualFunction;
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

    /**
     * A Construction
     *
     * @param horizontalPhotoList
     * @param context
     */
    public RecyclerViewHorizontalListAdapter(List<FilterStruct> horizontalPhotoList, Context context) {
        this.horizontalPhotoList = horizontalPhotoList;
        this.context = context;
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
                useFunction(position);
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
     * @param position
     * @see PhotoRecycler#seekBar1
     * @see com.package1.MainActivity#imgView
     * - si la fonction a besoin d'une seekbar, alors on l'affiche et on definit ses parametres , et on change l'était actualFonction
     * @see PhotoRecycler#seekBar1
     * @see RecyclerViewHorizontalListAdapter#actualFunction
     */
    private void useFunction(final int position) {
        renderscript = new RS(context);
        addListener();
        // If we want reset imageView
        //imageEditingCopy = imageEditing.copy(Bitmap.Config.ARGB_8888, true);
        imgView.setImageBitmap(imageEditingCopy);
        switch (position) {
            // ToGrey
            case 0:
                setGone(seekBar1, seekBar2);
                imgView.setImageBitmap(renderscript.toGrey(imageEditing));
                imageEditingCopy = renderscript.toGrey(imageEditing);
                break;
            // Colorize
            case 1:
                setVisible(seekBar1);
                setGone(seekBar2);
                setBorn(seekBar1, 359);
                setRGBBackground(seekBar1);
                seekBar1.setProgress(0);
                actualFunction = "colorize";
                break;
            // KeepColor
            case 2:
                setVisible(seekBar1, seekBar2);
                setBorn(seekBar1, 359);
                setBorn(seekBar2, 180);
                setRGBBackground(seekBar1);
                seekBar1.setProgress(0);
                seekBar2.setProgress(0);
                actualFunction = "keepColor";
                break;
            // Contrast
            case 3:
                setVisible(seekBar1);
                setGone(seekBar2);
                setBorn(seekBar1, 127);
                setNormalBackground(seekBar1);
                seekBar1.setProgress(0);
                actualFunction = "contrast";
                break;
            // ShiftLight
            case 4:
                setVisible(seekBar1);
                setGone(seekBar2);
                setBorn(seekBar1, 200);
                setNormalBackground(seekBar1);
                seekBar1.setProgress(0);
                actualFunction = "shiftLight";
                break;
            // Shift Saturation
            case 5:
                setVisible(seekBar1);
                setGone(seekBar2);
                setBorn(seekBar1, 200);
                setNormalBackground(seekBar1);
                seekBar1.setProgress(0);
                actualFunction = "shiftSaturation";
                break;
            // Shift Color
            case 6:
                setVisible(seekBar1);
                setGone(seekBar2);
                setBorn(seekBar1, 255);
                setRGBBackground(seekBar1);
                seekBar1.setProgress(0);
                actualFunction = "shiftColor";
                break;
            // isoHelie
            case 7:
                setVisible(seekBar1);
                setGone(seekBar2);
                setBorn(seekBar1, 8);
                setNormalBackground(seekBar1);
                seekBar1.setProgress(2);
                actualFunction = "isohelie";
                break;
            // Equa light
            case 8:
                setGone(seekBar1, seekBar2);
                hist = new HistogramManipulation(imageEditing, ChanelType.V);
                hist.equalizationLUT();
                imgView.setImageBitmap(hist.applyLUT(imageEditing));
                imageEditingCopy = hist.applyLUT(imageEditing);
                break;
            // Blur
            case 9:
                setNormalBackground(seekBar1);
                setVisible(seekBar1);
                setGone(seekBar2);
                seekBar1.setProgress(0);
                setBorn(seekBar1, 14);
                actualFunction = "blur";
                break;
            // Gaussian
            case 10:
                setNormalBackground(seekBar1);
                setVisible(seekBar1);
                setGone(seekBar2);
                seekBar1.setProgress(0);
                setBorn(seekBar1, 5);
                actualFunction = "gaussian";
                break;
            // Laplacien
            case 11:
                setGone(seekBar1, seekBar2);
                LaplacienMask maskLaplacien = new LaplacienMask();
                imgView.setImageBitmap(renderscript.convolution(imageEditing, maskLaplacien));
                imageEditingCopy = renderscript.convolution(imageEditing, maskLaplacien);
                break;
            default:
                break;
        }
    }

    /**
     * Permet d'appliquer les fonctions par rapport a la valeur de la seekBar
     *
     * @see PhotoRecycler#seekBar1
     */
    private void applyFunction() {

        switch (actualFunction) {
            case "colorize":
                imageEditingCopy = (renderscript.colorize(imageEditing, progressBar1));
                break;
            case "keepColor":
                imageEditingCopy = renderscript.keepHue(imageEditing, progressBar1, progressBar2);
                break;
            case "contrast":
                hist = new HistogramManipulation(imageEditing, ChanelType.V);
                hist.linearExtensionLUT(128 + progressBar1, 127 - progressBar1);
                imageEditingCopy = hist.applyLUT(imageEditing);
                break;
            case "shiftLight":
                hist = new HistogramManipulation(imageEditing, ChanelType.V);
                hist.shiftLUT(progressBar1 - 100);
                imageEditingCopy = hist.applyLUT(imageEditing);
                break;
            case "shiftSaturation":
                hist = new HistogramManipulation(imageEditing, ChanelType.S);
                hist.shiftLUT(progressBar1 - 100);
                imageEditingCopy = hist.applyLUT(imageEditing);
                break;
            case "shiftColor":
                hist = new HistogramManipulation(imageEditing, ChanelType.H);
                hist.shiftCycleLUT(progressBar1);
                imageEditingCopy = hist.applyLUT(imageEditing);
                break;
            case "isohelie":
                hist = new HistogramManipulation(imageEditing, ChanelType.V);
                hist.isohelieLUT(progressBar1 + 2);
                imageEditingCopy = hist.applyLUT(imageEditing);
                break;
            case "blur":
                BlurMask mask = new BlurMask(progressBar1 + 1);
                imageEditingCopy = renderscript.convolution(imageEditing, mask);
                break;
            case "gaussian":
                GaussianBlur maskGaussian = new GaussianBlur(progressBar1 * 2 + 1, 2.5);
                imageEditingCopy = renderscript.convolution(imageEditing, maskGaussian);
            default:
                break;
        }

        imgView.setImageBitmap(imageEditingCopy);
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
                applyFunction();
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
                applyFunction();
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