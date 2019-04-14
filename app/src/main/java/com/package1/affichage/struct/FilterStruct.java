package com.package1.affichage.struct;

import android.graphics.Bitmap;

import com.package1.affichage.type.FilterType;

/**
 * @author Mathieu
 * Structure of a FilterRecyclerView
 */
public class FilterStruct {

    /**
     * Filter name
     *
     * @see FilterStruct#getFilterName()
     */
    private final String filterName;
    /**
     * Image with filter
     *
     * @see FilterStruct#getImage()
     */
    private final Bitmap image;
    /**
     * Type of filter
     *
     * @see FilterType
     */
    private final FilterType filterType;

    /**
     * Constructor
     * @param filterName the name of the filter
     * @param image the image
     * @param filterType the FilterType
     */
    public FilterStruct(String filterName, Bitmap image, FilterType filterType) {
        this.filterName = filterName;
        this.image = image;
        this.filterType = filterType;
    }

    public FilterType getFilterType() {
        return filterType;
    }

    public String getFilterName() {
        return filterName;
    }

    public Bitmap getImage() {
        return image;
    }

}
