package com.package1.affichage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.package1.R;

import java.util.List;

import static com.package1.affichage.PhotoRecycler.image_copy;
import static com.package1.affichage.PhotoRecycler.imgView;

/**
 * TODO :
 * je peux enlever le Image puisqu'il est en variable globale
 */

public class RecyclerViewHorizontalListAdapter extends RecyclerView.Adapter<RecyclerViewHorizontalListAdapter.PhotoViewHolder> {

    private List<FilterStruct> horizontalPhotoList;
    private Context context;
    private Bitmap image;

    public RecyclerViewHorizontalListAdapter(List<FilterStruct> horizontalPhotoList, Context context, Bitmap bmp) {
        this.horizontalPhotoList = horizontalPhotoList;
        this.context = context;
        this.image = bmp;
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
        PhotoViewHolder gvh = new PhotoViewHolder(photoProductView);
        return gvh;
    }

    /**
     * Permet de spécifier le continu de notre RecyclerView
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(PhotoViewHolder holder, final int position) {

        if (image != null) {

            holder.imageView.setImageBitmap(image);
            // holder.imageView.setImageResource(horizontalPhotoList.get(position).getProductImage());
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

    }

    public void useFonction(final int position) {

        switch (position) {
            case 0:
                keepRed2(image_copy);
                imgView.setImageBitmap(image_copy);
                break;
            case 1:
                break;
            default :
                break;


        }
    }

    public void keepRed2(Bitmap bmp) {
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