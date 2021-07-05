package com.kavramatik.kavramatik.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class QuantityModel implements BaseModel {

    @PrimaryKey
    @SerializedName("id")
    private int id;

    @ColumnInfo
    @SerializedName("quantitiy_name")
    private String quantityName;

    @ColumnInfo
    @SerializedName("quantitiy_text")
    private String quantityText;

    @ColumnInfo
    @SerializedName("quantitiy_one_text")
    private String quantityOneText;

    @ColumnInfo
    @SerializedName("quantitiy_two_text")
    private String quantityTwoText;

    @ColumnInfo
    @SerializedName("quantitiy_one_image")
    private String quantityOneImage;

    @ColumnInfo
    @SerializedName("quantitiy_two_image")
    private String quantityTwoImage;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuantityName() {
        return quantityName;
    }

    public void setQuantityName(String quantityName) {
        this.quantityName = quantityName;
    }

    public String getQuantityText() {
        return quantityText;
    }

    public void setQuantityText(String quantityText) {
        this.quantityText = quantityText;
    }

    public String getQuantityOneText() {
        return quantityOneText;
    }

    public void setQuantityOneText(String quantityOneText) {
        this.quantityOneText = quantityOneText;
    }

    public String getQuantityTwoText() {
        return quantityTwoText;
    }

    public void setQuantityTwoText(String quantityTwoText) {
        this.quantityTwoText = quantityTwoText;
    }

    public String getQuantityOneImage() {
        return quantityOneImage;
    }

    public void setQuantityOneImage(String quantityOneImage) {
        this.quantityOneImage = quantityOneImage;
    }

    public String getQuantityTwoImage() {
        return quantityTwoImage;
    }

    public void setQuantityTwoImage(String quantityTwoImage) {
        this.quantityTwoImage = quantityTwoImage;
    }
}
