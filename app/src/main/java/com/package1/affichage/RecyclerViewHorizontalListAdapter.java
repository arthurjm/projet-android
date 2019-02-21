package com.package1.affichage;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.package1.R;

import java.util.List;


public class RecyclerViewHorizontalListAdapter extends RecyclerView.Adapter<RecyclerViewHorizontalListAdapter.PhotoViewHolder> {

    private List<FilterStruct> horizontalPhotoList;
    Context context;

    // Test
    Bitmap image;

    public RecyclerViewHorizontalListAdapter(List<FilterStruct> horizontalPhotoList, Context context, Bitmap bmp) {
        this.horizontalPhotoList = horizontalPhotoList;
        this.context = context;
        this.image = bmp;
    }

    /**
     * Permet d'initialiser le viewHolder
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
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(PhotoViewHolder holder, final int position) {
        holder.imageView.setImageResource(horizontalPhotoList.get(position).getProductImage());
        holder.txtview.setText(horizontalPhotoList.get(position).getFilterName());

        holder.imageView.setImageBitmap(horizontalPhotoList.get(position).getImage());

        //test(holder, position);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Action lorsque l'on clique sur l'image
                String productName = horizontalPhotoList.get(position).getFilterName().toString();
                Toast.makeText(context, productName + " is selected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void test(PhotoViewHolder holder, final int position){

        holder.imageView.setImageBitmap(horizontalPhotoList.get(position).getImage());

    }

    /**
     * Le nombre d'element present dans la liste
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