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
    public String filterName;
    /**
     * Image with filter
     *
     * @see FilterStruct#getImage()
     * @see FilterStruct#setImage(Bitmap)
     */
    public Bitmap image;
    /**
     * Type of filter
     *
     * @see FilterType
     */
    public FilterType filterType;

    /**
     * Constructeur
     *
     * @param filterName
     * @param image
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

    public void setFilterName(String productName) {
        this.filterName = productName;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
