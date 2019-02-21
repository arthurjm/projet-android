package com.package1.affichage;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class FilterStruct {

    public int filterImage;
    public String filterName;
    public Bitmap image;

    public FilterStruct(String filterName, int filterImage, Bitmap image) {
        this.filterImage = filterImage;
        this.filterName = filterName;
        this.image = image;
    }

    public int getProductImage() {
        return filterImage;
    }

    public void setFilterImage(int productImage) {
        this.filterImage = productImage;
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
