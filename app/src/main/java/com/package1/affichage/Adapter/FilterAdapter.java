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
import com.package1.affichage.Apply.ApplyFilter;
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
    private List<FilterStruct> horizontalPhotoList;
    /**
     * a variable of type Context
     *
     * @see Context
     */
    private Context context;
    public ApplyFilter applyFilter;

    /**
     * Constructor
     *
     * @param horizontalPhotoList
     * @param context
     */
    public FilterAdapter(List<FilterStruct> horizontalPhotoList, Context context, MenuType rt) {
        this.horizontalPhotoList = horizontalPhotoList;
        this.context = context;
        this.applyFilter = new ApplyFilter(context, rt);

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

        holder.imageView.setImageBitmap(horizontalPhotoList.get(position).getImage());
        holder.txtview.setText(horizontalPhotoList.get(position).getFilterName());

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // use adequate function with the select image
                applyFilter.useFunction(horizontalPhotoList.get(position).getFilterType());
                String productName = horizontalPhotoList.get(position).getFilterName().toString();
                Toast.makeText(context, productName + " is selected", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return horizontalPhotoList.size();
    }

    public class FilterViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView txtview;

        public FilterViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.idProductImage);
            txtview = view.findViewById(R.id.idProductName);
        }
    }
}