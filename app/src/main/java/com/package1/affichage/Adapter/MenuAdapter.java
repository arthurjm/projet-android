package com.package1.affichage.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.package1.R;
import com.package1.affichage.Struct.FilterStruct;
import com.package1.affichage.Struct.MenuStruct;
import com.package1.affichage.Type.RecyclerType;

import java.util.List;

import static com.package1.MainActivity.imageEditingCopy;
import static com.package1.MainActivity.imgView;
import static com.package1.affichage.PhotoRecycler.back;
import static com.package1.affichage.PhotoRecycler.changeList;
import static com.package1.affichage.PhotoRecycler.menuRecyclerView;
import static com.package1.affichage.PhotoRecycler.photoRecyclerView;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.PhotoViewHolder> {

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
    public MenuAdapter(List<MenuStruct> horizontalPhotoList, Context context) {
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
        View photoProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pattern_recycler, parent, false);
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
                useFunction(menuList.get(position).getRecyclerType());
                String productName = menuList.get(position).getFilterName().toString();
                Toast.makeText(context, productName + " is selected", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void useFunction(RecyclerType type) {
        imgView.setImageBitmap(imageEditingCopy);
        menuRecyclerView.setVisibility(View.GONE);
        photoRecyclerView.setVisibility(View.VISIBLE);
        back.setVisibility(View.VISIBLE);
        switch (type) {
            case Color:
                changeList(RecyclerType.Color);
                break;
            case Contrast:
                changeList(RecyclerType.Contrast);
                break;
            case Mask:
                changeList(RecyclerType.Mask);
                break;
            case Extras:
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
