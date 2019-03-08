package com.package1.affichage;

import android.graphics.Bitmap;

/**
 * @author Mathieu
 */
public class FilterStruct {

    /**
     * Filter name
     * @see FilterStruct#getFilterName()
     * @see FilterStruct#setFilterName(String)
     */
    public String filterName;
    /**
     * Image with filter
     * @see FilterStruct#getImage()
     * @see FilterStruct#setImage(Bitmap)
     */
    public Bitmap image;

    /**
     * Constructeur
     * @param filterName
     * @param image
     */
    public FilterStruct(String filterName, Bitmap image) {
        this.filterName = filterName;
        this.image = image;
    }

    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String productName) {
        this.filterName = productName;
    }

    public Bitmap getImage(){return image; }

    public void setImage(Bitmap image){
        this.image = image;
    }
}
