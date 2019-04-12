package com.package1.affichage.struct;

import android.graphics.Bitmap;

import com.package1.affichage.type.FilterType;
import com.package1.affichage.type.MenuType;

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
    private MenuType menuType;

    /**
     * Constructor
     *
     * @param filterName the filterName
     * @param image the Bitmap
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
