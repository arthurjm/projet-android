package com.package1.affichage.Adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.package1.R;
import com.package1.affichage.Struct.FilterStruct;
import com.package1.affichage.Struct.MenuStruct;

import java.util.List;

import static com.package1.affichage.PhotoEditing.applyMenu;
import static com.package1.affichage.PhotoEditing.nightMode;

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

    public MenuAdapter(List<MenuStruct> menuList) {
        this.menuList = menuList;
    }

    /**
     * To initialise the viewHolder
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public MenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View photoProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pattern_recycler, parent, false);
        return new MenuViewHolder(photoProductView);

    }

    /**
     * Modify the actual case in the recyclerView
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final MenuViewHolder holder, final int position) {

        holder.imageView.setImageBitmap(menuList.get(position).getImage());
        holder.textView.setText(menuList.get(position).getFilterName());
        // Change the text's color depending the situation
        if (nightMode == true) {
            holder.textView.setTextColor(Color.WHITE);
        } else {
            holder.textView.setTextColor(Color.BLACK);
        }

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // use adequate function with the select image
                applyMenu.modifyList(menuList.get(position).getRecyclerType());
            }
        });

    }

    /**
     * The size of the list
     * @return
     */
    @Override
    public int getItemCount() {
        return menuList.size();
    }


    public class MenuViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public MenuViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.idProductImage);
            textView = view.findViewById(R.id.idProductName);
        }
    }
}
