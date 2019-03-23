package com.package1.affichage.Struct;

import android.graphics.Bitmap;

import com.package1.affichage.Type.FilterType;
import com.package1.affichage.Type.RecyclerType;

public class MenuStruct {

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
     * Type of filter
     * @see FilterType
     */
    public RecyclerType type;

    /**
     * Constructeur
     * @param filterName
     * @param image
     */
    public MenuStruct(String filterName, Bitmap image, RecyclerType type) {
        this.filterName = filterName;
        this.image = image;
        this.type = type;
    }

    public RecyclerType getRecyclerType(){ return type;}

    public String getFilterName() {
        return filterName;
    }

    public Bitmap getImage(){return image; }


}
