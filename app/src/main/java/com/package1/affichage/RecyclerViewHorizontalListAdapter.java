package com.package1.affichage;

import android.content.Context;
import android.graphics.Bitmap;
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
import com.package1.ColorManipulation;
import com.package1.HistogramManipulation;
import com.package1.R;
import com.package1.RS;

import java.util.List;

import static com.package1.MainActivity.imageEditing;
import static com.package1.MainActivity.imageEditingCopy;
import static com.package1.MainActivity.imgView;
import static com.package1.affichage.PhotoRecycler.seekBar1;

public class RecyclerViewHorizontalListAdapter extends RecyclerView.Adapter<RecyclerViewHorizontalListAdapter.PhotoViewHolder> {

    private List<FilterStruct> horizontalPhotoList;
    private Context context;

    private HistogramManipulation hist;
    private RS renderscript;
    private int actualFunction;

    private int progressBar1;

    /**
     * TODOO : FAIRE UN ENUM POUR LE SWITCH DE LA SEEKBAR
     */

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

    private void setGone(SeekBar sb) {
        sb.setVisibility(View.GONE);
    }

    private void setGone(SeekBar sb, SeekBar sb1) {
        setGone(sb);
        setGone(sb1);
    }

    private void setVisible(SeekBar sb) {
        sb.setVisibility(View.VISIBLE);
    }

    private void setBorn(SeekBar sb, int max) {
        sb.setMax(max);
    }

    private void setRGBBackground(SeekBar sb) {
        sb.setBackgroundResource(R.drawable.seekbar_progess);
    }

    private void setNormalBackground(SeekBar sb) {
        sb.setBackgroundResource(0);
    }

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
                actualFunction = 0;
                break;
            // Keepcolor
            case 2:
                imageEditingCopy = imageEditing.copy(Bitmap.Config.ARGB_8888, true);
                imgView.setImageBitmap(imageEditingCopy);
                setVisible(seekBar1);
                setBorn(seekBar1, 359);
                setRGBBackground(seekBar1);
                seekBar1.setProgress(0);
                actualFunction = 1;
                break;
            // Contrast
            case 3:
                imageEditingCopy = imageEditing.copy(Bitmap.Config.ARGB_8888, true);
                imgView.setImageBitmap(imageEditingCopy);
                setVisible(seekBar1);
                setBorn(seekBar1, 127);
                setNormalBackground(seekBar1);
                seekBar1.setProgress(0);
                actualFunction = 2;
                break;
            // ShiftLight
            case 4:
                imageEditingCopy = imageEditing.copy(Bitmap.Config.ARGB_8888, true);
                imgView.setImageBitmap(imageEditingCopy);
                setVisible(seekBar1);
                setBorn(seekBar1, 200);
                setNormalBackground(seekBar1);
                seekBar1.setProgress(0);
                actualFunction = 3;
                break;
            // Shift Saturation
            case 5:
                imageEditingCopy = imageEditing.copy(Bitmap.Config.ARGB_8888, true);
                imgView.setImageBitmap(imageEditingCopy);
                setVisible(seekBar1);
                setBorn(seekBar1, 200);
                setNormalBackground(seekBar1);
                seekBar1.setProgress(0);
                actualFunction = 4;
                break;
            // Shift Color
            case 6:
                imageEditingCopy = imageEditing.copy(Bitmap.Config.ARGB_8888, true);
                imgView.setImageBitmap(imageEditingCopy);
                setVisible(seekBar1);
                setBorn(seekBar1, 359);
                setRGBBackground(seekBar1);
                seekBar1.setProgress(0);
                actualFunction = 5;
                break;
            // isoHelie
            case 7:
                imageEditingCopy = imageEditing.copy(Bitmap.Config.ARGB_8888, true);
                imgView.setImageBitmap(imageEditingCopy);
                setVisible(seekBar1);
                setBorn(seekBar1, 22);
                setNormalBackground(seekBar1);
                seekBar1.setProgress(2);
                actualFunction = 6;
                break;
            // Equa light
            case 8:
                imageEditingCopy = imageEditing.copy(Bitmap.Config.ARGB_8888, true);
                setGone(seekBar1);
                hist = new HistogramManipulation(imageEditingCopy, ChanelType.V);
                hist.equalizationLUT();
                imgView.setImageBitmap(hist.applyLUT(imageEditingCopy));
                break;
            default:
                break;
        }
    }

    private void applyFunction() {

        switch (actualFunction) {
            case 0:
                imgView.setImageBitmap(renderscript.colorize(imageEditingCopy, progressBar1));
                imageEditingCopy = imageEditing.copy(Bitmap.Config.ARGB_8888, true);
                break;
            case 1:
                break;
            // CONTRAST
            case 2:
                hist = new HistogramManipulation(imageEditingCopy, ChanelType.V);
                hist.linearExtensionLUT(128 + progressBar1, 127 - progressBar1);
                imgView.setImageBitmap(hist.applyLUT(imageEditingCopy));
                imageEditingCopy = imageEditing.copy(Bitmap.Config.ARGB_8888, true);
                break;
            case 3:
                hist = new HistogramManipulation(imageEditingCopy, ChanelType.V);
                hist.shiftLUT(progressBar1 - 100);
                imgView.setImageBitmap(hist.applyLUT(imageEditingCopy));
                imageEditingCopy = imageEditing.copy(Bitmap.Config.ARGB_8888, true);
                break;
            case 4:
                hist = new HistogramManipulation(imageEditingCopy, ChanelType.S);
                hist.shiftLUT(progressBar1 - 100);
                imgView.setImageBitmap(hist.applyLUT(imageEditingCopy));
                imageEditingCopy = imageEditing.copy(Bitmap.Config.ARGB_8888, true);
                break;
            case 5:
                hist = new HistogramManipulation(imageEditingCopy, ChanelType.H);
                hist.shiftCycleLUT(progressBar1);
                imgView.setImageBitmap(hist.applyLUT(imageEditingCopy));
                imageEditingCopy = imageEditing.copy(Bitmap.Config.ARGB_8888, true);
                break;
            case 6:
                hist = new HistogramManipulation(imageEditingCopy, ChanelType.H);
                hist.isohelieLUT(progressBar1 - 2);
                imgView.setImageBitmap(hist.applyLUT(imageEditingCopy));
                imageEditingCopy = imageEditing.copy(Bitmap.Config.ARGB_8888, true);
                break;
            default:
                break;
        }
    }

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