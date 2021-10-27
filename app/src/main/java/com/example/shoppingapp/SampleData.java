package com.example.shoppingapp;

public class SampleData {
    private int cloth;
    private String clothName;
    private String clothPrice;

    public SampleData(int cloth, String clothName, String clothPrice){
        this.cloth = cloth;
        this.clothName = clothName;
        this.clothPrice = clothPrice;
    }

    public int getCloth()
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