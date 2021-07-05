package com.kavramatik.kavramatik.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class NumberModel implements BaseModel {

    @PrimaryKey
    @SerializedName("id")
    private int id;

    @ColumnInfo
    @SerializedName("number_name")
    private String numberName;

    @ColumnInfo
    @SerializedName("number_one_text")
    private String numberText;


    @ColumnInfo
    @SerializedName("number_one_image")
    private String numberImage;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumberName() {
        return numberName;
    }

    public void setNumberName(String numberName) {
        this.numberName = numberName;
    }

    public String getNumberText() {
        return numberText;
    }

    public void setNumberText(String numberText) {
        this.numberText = numberText;
    }


    public String getNumberImage() {
        return numberImage;
    }

    public void setNumberImage(String numberImage) {
        this.numberImage = numberImage;
    }

}
