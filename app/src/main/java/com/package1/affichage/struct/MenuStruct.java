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
    private final MenuType menuType;

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
