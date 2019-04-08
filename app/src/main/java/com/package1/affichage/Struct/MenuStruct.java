package com.package1.affichage.Struct;

import android.graphics.Bitmap;

import com.package1.affichage.Type.FilterType;
import com.package1.affichage.Type.MenuType;

/**
 * @author Mathieu
 * Structure of a MenuRecyclerView
 */
public class MenuStruct {

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
    public MenuType menuType;

    /**
     * Constructor
     *
     * @param filterName
     * @param image
     */
    public MenuStruct(String filterName, Bitmap image, MenuType menuType) {
        this.filterName = filterName;
        this.image = image;
        this.menuType = menuType;
    }

    public MenuType getRecyclerType() {
        return menuType;
    }

    public String getFilterName() {
        return filterName;
    }

    public Bitmap getImage() {
        return image;
    }

}
