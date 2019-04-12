package com.package1.affichage.Struct;

import android.graphics.Bitmap;

import com.package1.affichage.Type.FilterType;

/**
 * @author Mathieu
 * Structure of a FilterRecyclerView
 */
public class FilterStruct {

    /**
     * Filter name
     *
     * @see FilterStruct#getFilterName()
     * @see FilterStruct#setFilterName(String)
     */
    private String filterName;
    /**
     * Image with filter
     *
     * @see FilterStruct#getImage()
     * @see FilterStruct#setImage(Bitmap)
     */
    private Bitmap image;
    /**
     * Type of filter
     *
     * @see FilterType
     */
    private FilterType filterType;

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

    void setFilterName(String productName) {
        this.filterName = productName;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
