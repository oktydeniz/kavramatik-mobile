package com.kavramatik.kavramatik.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class ColorCompModel implements BaseModel {


    @PrimaryKey
    @SerializedName("id")
    private int id;

    @ColumnInfo
    @SerializedName("color")
    private String mainColor;

    @ColumnInfo
    @SerializedName("color_name")
    private String colorName;

    @ColumnInfo
    @SerializedName("color_tag")
    private String colorTag;

    @ColumnInfo
    @SerializedName("color_one")
    private String colorOne;

    @ColumnInfo
    @SerializedName("color_two")
    private String colorTwo;

    @ColumnInfo
    @SerializedName("one_tag")
    private String oneTag;

    @ColumnInfo
    @SerializedName("two_tag")
    private String twoTag;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMainColor() {
        return mainColor;
    }

    public void setMainColor(String mainColor) {
        this.mainColor = mainColor;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public String getColorTag() {
        return colorTag;
    }

    public void setColorTag(String colorTag) {
        this.colorTag = colorTag;
    }

    public String getColorOne() {
        return colorOne;
    }

    public void setColorOne(String colorOne) {
        this.colorOne = colorOne;
    }

    public String getColorTwo() {
        return colorTwo;
    }

    public void setColorTwo(String colorTwo) {
        this.colorTwo = colorTwo;
    }

    public String getOneTag() {
        return oneTag;
    }

    public void setOneTag(String oneTag) {
        this.oneTag = oneTag;
    }

    public String getTwoTag() {
        return twoTag;
    }

    public void setTwoTag(String twoTag) {
        this.twoTag = twoTag;
    }
}
