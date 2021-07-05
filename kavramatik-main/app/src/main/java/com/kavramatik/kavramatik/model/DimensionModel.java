package com.kavramatik.kavramatik.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class DimensionModel  implements BaseModel{
    @PrimaryKey
    @SerializedName("id")
    private int id;

    @ColumnInfo
    @SerializedName("dimension_name")
    private String dimensionName;

    @ColumnInfo
    @SerializedName("dimension_text")
    private String dimensionText;

    @ColumnInfo
    @SerializedName("dimension_one_text")
    private String dimensionImageTextOne;

    @ColumnInfo
    @SerializedName("dimension_two_text")
    private String dimensionImageTextTwo;

    @ColumnInfo
    @SerializedName("dimension_one_image")
    private String imageOne;

    @ColumnInfo
    @SerializedName("dimension_two_image")
    private String imageTwo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDimensionName() {
        return dimensionName;
    }

    public void setDimensionName(String dimensionName) {
        this.dimensionName = dimensionName;
    }

    public String getDimensionText() {
        return dimensionText;
    }

    public void setDimensionText(String dimensionText) {
        this.dimensionText = dimensionText;
    }

    public String getDimensionImageTextOne() {
        return dimensionImageTextOne;
    }

    public void setDimensionImageTextOne(String dimensionImageTextOne) {
        this.dimensionImageTextOne = dimensionImageTextOne;
    }

    public String getDimensionImageTextTwo() {
        return dimensionImageTextTwo;
    }

    public void setDimensionImageTextTwo(String dimensionImageTextTwo) {
        this.dimensionImageTextTwo = dimensionImageTextTwo;
    }

    public String getImageOne() {
        return imageOne;
    }

    public void setImageOne(String imageOne) {
        this.imageOne = imageOne;
    }

    public String getImageTwo() {
        return imageTwo;
    }

    public void setImageTwo(String imageTwo) {
        this.imageTwo = imageTwo;
    }
}
