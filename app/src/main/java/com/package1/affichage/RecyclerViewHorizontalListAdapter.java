package com.package1.affichage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.provider.MediaStore;
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

import static com.package1.MainActivity.image_retouche;
import static com.package1.MainActivity.imgView;
import static com.package1.affichage.PhotoRecycler.*;

public class RecyclerViewHorizontalListAdapter extends RecyclerView.Adapter<RecyclerViewHorizontalListAdapter.PhotoViewHolder> {

    private List<FilterStruct> horizontalPhotoList;
    private Context context;

    private ColorManipulation test;
    private int actualFunction;

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


    public void useFonction(final int position) {
        test = new ColorManipulation();
        switch (position) {
            case 0:
                seekBar1.setVisibility(View.VISIBLE);
                seekBar2.setVisibility(View.VISIBLE);
                imgView.setImageBitmap(test.convertImageSelectiveDesaturation(image_retouche, Color.RED, 100, 100, 100));

                actualFunction = 0;
                break;
            case 1:
                imgView.setImageBitmap(test.convertImageSelectiveDesaturation(image_retouche, Color.GREEN, 100, 100, 100));
                break;
            case 2:
                imgView.setImageBitmap(test.convertImageSelectiveDesaturation(image_retouche, Color.BLUE, 150, 150, 150));
                break;
            case 3:
                imgView.setImageBitmap(test.convertImageGreyScale(image_retouche));
            default:
                break;


        }
    }

    public void applyFunction() {
        switch (actualFunction) {
            case 0:
                // Appel de la fonction

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
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

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