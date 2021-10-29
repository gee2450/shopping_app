package com.example.shoppingapp;

import android.graphics.Bitmap;

public class SampleData {
    private Bitmap cloth;
    private String clothName;
    private String clothPrice;

    public SampleData(Bitmap  cloth, String clothName, String clothPrice){
        this.cloth = cloth;
        this.clothName = clothName;
        this.clothPrice = clothPrice;
    }

    public Bitmap getCloth()
    {
        return this.cloth;
    }

    public String getClothName()
    {
        return this.clothName;
    }

    public String getClothPrice()
    {
        return this.clothPrice;
    }
}