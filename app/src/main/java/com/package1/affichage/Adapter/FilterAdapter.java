package com.package1.affichage.Adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.package1.R;
import com.package1.affichage.Apply.ApplyFilter;
import com.package1.affichage.PhotoEditing;
import com.package1.affichage.Struct.FilterStruct;
import com.package1.affichage.Type.MenuType;

import java.util.List;


/**
 * @author Mathieu
 * Adapter of FilterAdapter
 */
public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.FilterViewHolder> {

    /**
     * List of FilterStruct
     *
     * @see FilterStruct
     */
    private List<FilterStruct> FilterList;

    private ApplyFilter applyFilter;

    private PhotoEditing context;
    /**
     * Constructor
     *
     * @param FilterList
     * @param context
     */
    public FilterAdapter(List<FilterStruct> FilterList, PhotoEditing context, MenuType menuType) {
        this.FilterList = FilterList;
        this.applyFilter = new ApplyFilter(context, menuType);
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
    public FilterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View photoProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pattern_recycler, parent, false);
        return new FilterViewHolder(photoProductView);

    }

    /**
     * Modify the actual case in the recyclerView
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final FilterViewHolder holder, final int position) {

        holder.imageView.setImageBitmap(FilterList.get(position).getImage());
        holder.textView.setText(FilterList.get(position).getFilterName());
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
                applyFilter.useFunction(FilterList.get(position).getFilterType());
            }
        });

    }

    /**
     * The size of the list
     * @return
     */
    @Override
    public int getItemCount() {
        return FilterList.size();
    }

    public class FilterViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public FilterViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.idProductImage);
            textView = view.findViewById(R.id.idProductName);
        }
    }
}