package com.package1.affichage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
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
import com.package1.R;
import com.package1.RS;
import java.util.List;
import static com.package1.MainActivity.imageEditing;
import static com.package1.MainActivity.imageEditingCopy;
import static com.package1.MainActivity.imgView;
import static com.package1.affichage.PhotoRecycler.hist;
import static com.package1.affichage.PhotoRecycler.renderscript;
import static com.package1.affichage.PhotoRecycler.seekBar1;

/**
 * @author Mathieu
 */
public class RecyclerViewHorizontalListAdapter extends RecyclerView.Adapter<RecyclerViewHorizontalListAdapter.PhotoViewHolder> {

    /**
     * List of FilterStruct
     * @see FilterStruct
     */
    private List<FilterStruct> horizontalPhotoList;
    /**
     * a variable of type Context
     * @see Context
     */
    private Context context;
    /**
     * a variable of type string to represent the fonction actual
     */
    private String actualFunction;
    /**
     * Vale of the seekBar
     * @see PhotoRecycler#seekBar1
     */
    private int progressBar1;

    /**
     * A Construction
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
                useFonction(position);
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
     * @see View#GONE
     * @param sb
     */
    private void setGone(SeekBar sb) {
        sb.setVisibility(View.GONE);
    }

    /**
     * Permet de mettre a Gone la view de deux seekBar
     * @param sb
     * @param sb1
     */
    private void setGone(SeekBar sb, SeekBar sb1) {
        setGone(sb);
        setGone(sb1);
    }

    /**
     * Permet de mettre a Visible la view d'une seekBar
     * @see View#VISIBLE
     * @param sb
     */
    private void setVisible(SeekBar sb) {
        sb.setVisibility(View.VISIBLE);
    }

    /**
     * Permet de choisir la valeur max d'une seekBar
     * @see SeekBar#setMax(int)
     * @param sb
     * @param max
     */
    private void setBorn(SeekBar sb, int max) {
        sb.setMax(max);
    }

    /**
     * Permet de mettre l'arriere plan d'une seekBar en RGB
     * @see SeekBar#setBackgroundResource(int)
     * @param sb
     */
    private void setRGBBackground(SeekBar sb) {
        sb.setBackgroundResource(R.drawable.seekbar_progess);
    }

    /**
     * Permet de mettre l'arriere plan d'un seekBar à normal
     * @see SeekBar#setBackgroundResource(int)
     * @param sb
     */
    private void setNormalBackground(SeekBar sb) {
        sb.setBackgroundResource(0);
    }

    /**
     * Permet 2 choses :
     *  - si la fonction n'a pas besoin de seekBar, alors on n'affiche pas de seekBar, et on change l'imageView avec le resultat de la fonction
     * @see PhotoRecycler#seekBar1
     * @see com.package1.MainActivity#imgView
     *  - si la fonction a besoin d'une seekbar, alors on l'affiche et on definit ses parametres , et on change l'était actualFonction
     * @see PhotoRecycler#seekBar1
     * @see RecyclerViewHorizontalListAdapter#actualFunction
     * @param position
     */
    private void useFonction(final int position) {
        renderscript = new RS(context);
        addListener();
        switch (position) {
            // togrey
            case 0:
                imageEditingCopy = imageEditing.copy(Bitmap.Config.ARGB_8888, true);
                setGone(seekBar1);
                imgView.setImageBitmap(renderscript.toGrey(imageEditingCopy));
                break;
            // Colorize
            case 1:
                imageEditingCopy = imageEditing.copy(Bitmap.Config.ARGB_8888, true);
                imgView.setImageBitmap(imageEditingCopy);
                setVisible(seekBar1);
                setBorn(seekBar1, 359);
                setRGBBackground(seekBar1);
                seekBar1.setProgress(0);
                actualFunction = "colorize";
                break;
            // Keepcolor
            case 2:
                imageEditingCopy = imageEditing.copy(Bitmap.Config.ARGB_8888, true);
                imgView.setImageBitmap(imageEditingCopy);
                setVisible(seekBar1);
                setBorn(seekBar1, 359);
                setRGBBackground(seekBar1);
                seekBar1.setProgress(0);
                actualFunction = "keepColor";
                break;
            // Contrast
            case 3:
                imageEditingCopy = imageEditing.copy(Bitmap.Config.ARGB_8888, true);
                imgView.setImageBitmap(imageEditingCopy);
                setVisible(seekBar1);
                setBorn(seekBar1, 127);
                setNormalBackground(seekBar1);
                seekBar1.setProgress(0);
                actualFunction = "contrast";
                break;
            // ShiftLight
            case 4:
                imageEditingCopy = imageEditing.copy(Bitmap.Config.ARGB_8888, true);
                imgView.setImageBitmap(imageEditingCopy);
                setVisible(seekBar1);
                setBorn(seekBar1, 200);
                setNormalBackground(seekBar1);
                seekBar1.setProgress(0);
                actualFunction = "shiftLight";
                break;
            // Shift Saturation
            case 5:
                imageEditingCopy = imageEditing.copy(Bitmap.Config.ARGB_8888, true);
                imgView.setImageBitmap(imageEditingCopy);
                setVisible(seekBar1);
                setBorn(seekBar1, 200);
                setNormalBackground(seekBar1);
                seekBar1.setProgress(0);
                actualFunction = "shitSaturation";
                break;
            // Shift Color
            case 6:
                imageEditingCopy = imageEditing.copy(Bitmap.Config.ARGB_8888, true);
                imgView.setImageBitmap(imageEditingCopy);
                setVisible(seekBar1);
                setBorn(seekBar1, 255);
                setRGBBackground(seekBar1);
                seekBar1.setProgress(0);
                actualFunction = "shiftColor";
                break;
            // isoHelie
            case 7:
                imageEditingCopy = imageEditing.copy(Bitmap.Config.ARGB_8888, true);
                imgView.setImageBitmap(imageEditingCopy);
                setVisible(seekBar1);
                setBorn(seekBar1, 8);
                setNormalBackground(seekBar1);
                seekBar1.setProgress(2);
                actualFunction = "isohelie";
                break;
            // Equa light
            case 8:
                imageEditingCopy = imageEditing.copy(Bitmap.Config.ARGB_8888, true);
                setGone(seekBar1);
                hist = new HistogramManipulation(imageEditingCopy, ChanelType.V);
                hist.equalizationLUT();
                imgView.setImageBitmap(hist.applyLUT(imageEditingCopy));
                break;
            // Blur
            case 9:
                imageEditingCopy = imageEditing.copy(Bitmap.Config.ARGB_8888, true);
                setNormalBackground(seekBar1);
                setVisible(seekBar1);
                seekBar1.setProgress(0);
                setBorn(seekBar1, 14);
                actualFunction = "blur";
            default:
                break;
        }
    }

    /**
     * Permet d'appliquer les fonctions par rapport a la valeur de la seekBar
     * @see PhotoRecycler#seekBar1
     */
    private void applyFunction() {

        switch (actualFunction) {
            case "colorize":
                imgView.setImageBitmap(renderscript.colorize(imageEditingCopy, progressBar1));
                imageEditingCopy = renderscript.colorize(imageEditingCopy, progressBar1);
                break;
            case "keepColor":
                break;
            // CONTRAST
            case "contrast":
                hist = new HistogramManipulation(imageEditingCopy, ChanelType.V);
                hist.linearExtensionLUT(128 + progressBar1, 127 - progressBar1);
                imgView.setImageBitmap(hist.applyLUT(imageEditingCopy));
                imageEditingCopy = hist.applyLUT(imageEditingCopy);
                break;
            case "shiftLight":
                hist = new HistogramManipulation(imageEditingCopy, ChanelType.V);
                hist.shiftLUT(progressBar1 - 100);
                imgView.setImageBitmap(hist.applyLUT(imageEditingCopy));
                imageEditingCopy = hist.applyLUT(imageEditingCopy);
                break;
            case "shiftSaturation":
                hist = new HistogramManipulation(imageEditingCopy, ChanelType.S);
                hist.shiftLUT(progressBar1 - 100);
                imgView.setImageBitmap(hist.applyLUT(imageEditingCopy));
                imageEditingCopy = hist.applyLUT(imageEditingCopy);
                break;
            case "shiftColor":
                hist = new HistogramManipulation(imageEditingCopy, ChanelType.H);
                hist.shiftCycleLUT(progressBar1);
                imgView.setImageBitmap(hist.applyLUT(imageEditingCopy));
                imageEditingCopy = hist.applyLUT(imageEditingCopy);
                break;
            case "isohelie":
                hist = new HistogramManipulation(imageEditingCopy, ChanelType.V);
                hist.isohelieLUT(progressBar1+2);
                imgView.setImageBitmap(hist.applyLUT(imageEditingCopy));
                imageEditingCopy = hist.applyLUT(imageEditingCopy);
                break;
            case "blur":
                BlurMask mask = new BlurMask(progressBar1 + 1);
                imgView.setImageBitmap(renderscript.convolution(imageEditingCopy, mask));
                break;
            default:
                break;
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