package com.package1.affichage;

import android.graphics.Bitmap;

public class FilterStruct {

    public String filterName;
    public Bitmap image;

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
