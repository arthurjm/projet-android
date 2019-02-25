package com.package1.affichage;

import android.content.Context;
import android.graphics.Color;
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
import com.package1.R;
import com.package1.ColorManipulation;

import java.util.List;

import static com.package1.MainActivity.imageEditing;
import static com.package1.MainActivity.imageEditingCopy;
import static com.package1.MainActivity.imgView;
import static com.package1.affichage.PhotoRecycler.*;

public class RecyclerViewHorizontalListAdapter extends RecyclerView.Adapter<RecyclerViewHorizontalListAdapter.PhotoViewHolder> {

    private List<FilterStruct> horizontalPhotoList;
    private Context context;

    private ColorManipulation color;
    private HistogramManipulation hist;
    private int actualFunction;

    private int progressBar1;
    private int progresseBar2;


    public RecyclerViewHorizontalListAdapter(List<FilterStruct> horizontalPhotoList, Context context) {
        this.horizontalPhotoList = horizontalPhotoList;
        this.context = context;

    }

    /**
     * Permet d'initialiser le viewHolder
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View photoProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.hozirontal_list_photo, parent, false);
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

        final Context context = holder.itemView.getContext();

        // Recup la photo avec la fonction associes
        holder.imageView.setImageBitmap(horizontalPhotoList.get(position).getImage());
        holder.txtview.setTextColor(Color.WHITE);
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

    public void setGone(SeekBar sb) {
        sb.setVisibility(View.GONE);
    }

    public void setGone(SeekBar sb, SeekBar sb1) {
        setGone(sb);
        setGone(sb1);
    }

    public void setVisible(SeekBar sb) {
        sb.setVisibility(View.VISIBLE);
    }

    public void setBorn(SeekBar sb, int min, int max) {
        sb.setMin(min);
        sb.setMax(max);
    }

    public void setRGBBackground(SeekBar sb) {
        sb.setBackgroundResource(R.drawable.seekbar_progess);
    }

    public void setNormalBackground(SeekBar sb) {
        sb.setBackgroundResource(0);
    }

    public void useFonction(final int position) {
        color = new ColorManipulation();
        addListener();
        switch (position) {
            // togrey
            case 0:
                setGone(seekBar1);
                imgView.setImageBitmap(color.convertImageGreyScale(imageEditing));
                break;
            // Colorize
            case 1:
                imgView.setImageBitmap(imageEditing);
                setVisible(seekBar1);
                setBorn(seekBar1, 0, 359);
                setRGBBackground(seekBar1);
                seekBar1.setProgress(0);
                actualFunction = 0;
                break;
            // Keepcolor
            case 2:
                imgView.setImageBitmap(imageEditing);
                setVisible(seekBar1);
                setBorn(seekBar1, 0, 359);
                setRGBBackground(seekBar1);
                seekBar1.setProgress(0);
                actualFunction = 1;
                break;
            // Contrast
            case 3:
                imgView.setImageBitmap(imageEditing);
                setVisible(seekBar1);
                setBorn(seekBar1, 0, 127);
                setNormalBackground(seekBar1);
                seekBar1.setProgress(0);
                actualFunction = 2;
                break;
            // ShiftLight
            case 4:
                imgView.setImageBitmap(imageEditing);
                setVisible(seekBar1);
                setBorn(seekBar1, -100, 100);
                setNormalBackground(seekBar1);
                seekBar1.setProgress(0);
                actualFunction = 3;
                break;
            // Shift Saturation
            case 5:
                imgView.setImageBitmap(imageEditing);
                setVisible(seekBar1);
                setBorn(seekBar1, -100, 100);
                setNormalBackground(seekBar1);
                seekBar1.setProgress(0);
                actualFunction = 4;
                break;
            // Shift Color
            case 6:
                imgView.setImageBitmap(imageEditing);
                setVisible(seekBar1);
                setBorn(seekBar1, 0, 359);
                setRGBBackground(seekBar1);
                seekBar1.setProgress(0);
                actualFunction = 5;
                break;
            // isoHelie
            case 7:
                imgView.setImageBitmap(imageEditing);
                setVisible(seekBar1);
                setBorn(seekBar1, 2, 20);
                setNormalBackground(seekBar1);
                seekBar1.setProgress(2);
                actualFunction = 6;
                break;
            // Equa light
            case 8:
                setGone(seekBar1);
                hist = new HistogramManipulation(imageEditing, ChanelType.V);
                hist.equalizationLUT();
                imgView.setImageBitmap(hist.applyLUT(imageEditingCopy));
                // ftc
                break;
            default:
                break;
        }
    }

    public void applyFunction() {

        switch (actualFunction) {
            case 0:
                imgView.setImageBitmap(color.convertImageColorization(imageEditingCopy, progressBar1));
                break;
            case 1:

                break;
            // CONTRAST
            case 2:
                hist = new HistogramManipulation(imageEditingCopy, ChanelType.V);
                hist.linearExtensionLUT(progressBar1, 0);
                imgView.setImageBitmap(hist.applyLUT(imageEditingCopy));
                break;
            case 3:
                hist = new HistogramManipulation(imageEditingCopy, ChanelType.V);
                hist.shiftLUT(progressBar1);
                imgView.setImageBitmap(hist.applyLUT(imageEditingCopy));
                break;
            case 4:
                hist = new HistogramManipulation(imageEditingCopy, ChanelType.S);
                hist.shiftLUT(progressBar1);
                imgView.setImageBitmap(hist.applyLUT(imageEditingCopy));
                break;
            case 5:
                hist = new HistogramManipulation(imageEditingCopy, ChanelType.H);
                hist.shiftCycleLUT(progressBar1);
                imgView.setImageBitmap(hist.applyLUT(imageEditingCopy));
                break;
            case 6:
                hist = new HistogramManipulation(imageEditingCopy, ChanelType.H);
                hist.isohelieLUT(progressBar1);
                imgView.setImageBitmap(hist.applyLUT(imageEditingCopy));
                break;
            default:
                break;
        }
    }

    public void addListener() {

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

    /**
     * Le nombre d'element present dans la liste
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return horizontalPhotoList.size();
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