package com.package1.affichage.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.package1.R;
import com.package1.affichage.PhotoEditing;
import com.package1.affichage.Struct.FilterStruct;
import com.package1.affichage.Struct.MenuStruct;

import java.util.List;

/**
 * @author Mathieu
 * Adapter of MenuAdapter
 */
public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    /**
     * List of FilterStruct
     *
     * @see FilterStruct
     */
    private List<MenuStruct> menuList;

    private PhotoEditing context;

    public MenuAdapter(List<MenuStruct> menuList, Context context) {
        this.menuList = menuList;
        this.context = (PhotoEditing) context;
    }

    /**
     * To initialise the viewHolder
     *
     * @param parent the ViewGroup
     *               @see ViewGroup
     *
     * @param viewType int
     *
     * @return MenuViewHolder
     * @see MenuViewHolder
     */
    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate the layout file
        View photoProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pattern_recycler, parent, false);
        return new MenuViewHolder(photoProductView);

    }

    /**
     * Modify the actual case in the recyclerView
     *
     * @param holder the holder
     * @param position the position where we are
     */
    @Override
    public void onBindViewHolder(@NonNull final MenuViewHolder holder, final int position) {

        holder.imageView.setImageBitmap(menuList.get(position).getImage());
        holder.textView.setText(menuList.get(position).getFilterName());
        // Change the text's color depending the situation
        if (context.nightMode) {
            holder.textView.setTextColor(Color.WHITE);
        } else {
            holder.textView.setTextColor(Color.BLACK);
        }

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // use adequate function with the select image
                context.applyMenu.modifyList(menuList.get(position).getRecyclerType());
            }
        });

    }

    /**
     * The size of the list
     *
     * @return the size of the list
     */
    @Override
    public int getItemCount() {
        return menuList.size();
    }


    class MenuViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        MenuViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.idProductImage);
            textView = view.findViewById(R.id.idProductName);
        }
    }
}
