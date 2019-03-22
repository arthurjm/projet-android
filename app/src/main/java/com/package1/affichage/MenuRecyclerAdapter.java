package com.package1.affichage;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.package1.R;

import java.util.List;

import static com.package1.MainActivity.imageEditingCopy;
import static com.package1.MainActivity.imgView;
import static com.package1.affichage.PhotoRecycler.changeList;
import static com.package1.affichage.PhotoRecycler.menuRecyclerView;
import static com.package1.affichage.PhotoRecycler.photoRecyclerView;

public class MenuRecyclerAdapter extends RecyclerView.Adapter<MenuRecyclerAdapter.PhotoViewHolder> {

    /**
     * List of FilterStruct
     *
     * @see FilterStruct
     */
    private List<MenuStruct> menuList;
    /**
     * a variable of type Context
     *
     * @see Context
     */
    private Context context;


    /**
     * A Construction
     *
     * @param horizontalPhotoList
     * @param context
     */
    public MenuRecyclerAdapter(List<MenuStruct> horizontalPhotoList, Context context) {
        this.menuList = horizontalPhotoList;
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
        holder.imageView.setImageBitmap(menuList.get(position).getImage());
        holder.txtview.setText(menuList.get(position).getFilterName());

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Action lorsque l'on clique sur l'image
                useFunction(position);
                String productName = menuList.get(position).getFilterName().toString();
                Toast.makeText(context, productName + " is selected", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void useFunction(final int position) {
        imgView.setImageBitmap(imageEditingCopy);
        menuRecyclerView.setVisibility(View.GONE);
        photoRecyclerView.setVisibility(View.VISIBLE);
        switch (position) {
            // Color
            case 0:
                changeList(RecyclerType.Color);
                break;
            // Saturation
            case 1:
                changeList(RecyclerType.Contrast);
                break;
            // Mask
            case 2:
                changeList(RecyclerType.Mask);
                break;
            // Extras
            case 3:
                changeList(RecyclerType.Extras);
                break;
            default:
                break;
        }

    }

    @Override
    public int getItemCount() {
        return menuList.size();
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
