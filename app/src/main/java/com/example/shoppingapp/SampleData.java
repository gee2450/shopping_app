package com.example.shoppingapp;

public class SampleData {
    private int cloth;
    private String clothName;

    public SampleData(int cloth, String clothName){
        this.cloth = cloth;
        this.clothName = clothName;
    }

    public int getCloth()
    {
        return this.cloth;
    }

    public String getClothName()
    {
        return this.clothName;
    }
}