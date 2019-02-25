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

import com.package1.R;
import com.package1.ColorManipulation;
import java.util.List;

import static com.package1.MainActivity.imageEditing;
import static com.package1.MainActivity.imgView;
import static com.package1.affichage.PhotoRecycler.*;

public class RecyclerViewHorizontalListAdapter extends RecyclerView.Adapter<RecyclerViewHorizontalListAdapter.PhotoViewHolder> {

    private List<FilterStruct> horizontalPhotoList;
    private Context context;

    private ColorManipulation test;
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

    public void setRGBBackground(SeekBar sb) {
        sb.setBackgroundResource(R.drawable.seekbar_progess);
    }

    public void setNormalBackground(SeekBar sb) {
        sb.setBackgroundResource(0);
    }

    public void useFonction(final int position) {
        test = new ColorManipulation();
        addListener();
        switch (position) {
            case 0:
                setGone(seekBar1, seekBar2);
                imgView.setImageBitmap(test.convertImageSelectiveDesaturation(imageEditing, Color.RED, 100, 100, 100));
                break;
            case 1:
                setGone(seekBar1, seekBar2);
                imgView.setImageBitmap(test.convertImageSelectiveDesaturation(imageEditing, Color.GREEN, 100, 100, 100));
                break;
            case 2:
                setGone(seekBar1, seekBar2);
                imgView.setImageBitmap(test.convertImageSelectiveDesaturation(imageEditing, Color.BLUE, 150, 150, 150));
                break;
            case 3:
                imgView.setImageBitmap(test.convertImageGreyScale(imageEditing));
            case 4:
                setVisible(seekBar1);
                setRGBBackground(seekBar1);
                seekBar1.setMax(360);
                actualFunction = 0;
            default:
                break;


        }
    }

    public void applyFunction() {

        switch (actualFunction) {
            case 0:
                imgView.setImageBitmap(test.convertImageColorization(imageEditing, progressBar1));
                break;
            case 1:
                break;
            case 2:
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

        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progresseBar2 = progress;
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