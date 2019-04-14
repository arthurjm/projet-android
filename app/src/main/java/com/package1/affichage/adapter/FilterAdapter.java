package com.package1.affichage.adapter;

import android.annotation.SuppressLint;
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
import com.package1.affichage.apply.ApplyFilter;
import com.package1.affichage.PhotoEditing;
import com.package1.affichage.struct.FilterStruct;
import com.package1.affichage.type.MenuType;

import java.util.List;


/**
 * @author Mathieu
 *         Adapter of FilterAdapter
 */
public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.FilterViewHolder> {

    /**
     * List of FilterStruct
     *
     * @see FilterStruct
     */
    private final List<FilterStruct> FilterList;

    private final ApplyFilter applyFilter;

    private final PhotoEditing context;

    /**
     * Constructor
     *
     * @param FilterList the list that we need
     * @param context    the context
     */
    public FilterAdapter(List<FilterStruct> FilterList, Context context, MenuType menuType) {
        this.FilterList = FilterList;
        this.applyFilter = new ApplyFilter(context, menuType);
        this.context = (PhotoEditing) context;
    }

    /**
     * To initialise the viewHolder
     *
     * @param parent   the ViewGroup
     * @param viewType int
     * @return FilterViewHolder
     * @see ViewGroup
     * @see FilterViewHolder
     */
    @NonNull
    @Override
    public FilterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate the layout file
        View photoProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pattern_recycler, parent, false);
        return new FilterViewHolder(photoProductView);

    }

    /**
     * Modify the actual case in the recyclerView
     *
     * @param holder the holder
     * @param position the position where we are
     */
    @Override
    public void onBindViewHolder(@NonNull FilterViewHolder holder, @SuppressLint("RecyclerView") final int position) {

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
     *
     * @return the size of the list
     */
    @Override
    public int getItemCount() {
        return FilterList.size();
    }

    class FilterViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageView;
        final TextView textView;

        FilterViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.idProductImage);
            textView = view.findViewById(R.id.idProductName);
        }
    }
}