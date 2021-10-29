package com.example.shoppingapp;

import android.graphics.Bitmap;

public class SampleData {
    private int clothId;
    private Bitmap cloth;
    private String clothName;
    private String clothPrice;
    private boolean click;

    public SampleData(int clothId, Bitmap  cloth, String clothName, String clothPrice){
        this.clothId = clothId;
        this.cloth = cloth;
        this.clothName = clothName;
        this.clothPrice = clothPrice;
    }

    public int getClothId() {
        return this.clothId;
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

    public void setClick(boolean after) {
        click = after;
    }

    public boolean isClick() {
        return click;
    }
}