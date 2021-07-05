package com.kavramatik.kavramatik.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class DirectionModel implements BaseModel {

    @PrimaryKey
    @SerializedName("id")
    private int id;

    @ColumnInfo
    @SerializedName("direction_name")
    private String directionName;

    @ColumnInfo
    @SerializedName("direction_text")
    private String directionText;

    @ColumnInfo
    @SerializedName("direction_image")
    private String directionImage;

    @ColumnInfo
    @SerializedName("direction_two")
    private String directionTwo;

    @ColumnInfo
    @SerializedName("direction_two_text")
    private String directionTwoText;

    public String getDirectionTwo() {
        return directionTwo;
    }

    public void setDirectionTwo(String directionTwo) {
        this.directionTwo = directionTwo;
    }

    public String getDirectionTwoText() {
        return directionTwoText;
    }

    public void setDirectionTwoText(String directionTwoText) {
        this.directionTwoText = directionTwoText;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDirectionName() {
        return directionName;
    }

    public void setDirectionName(String directionName) {
        this.directionName = directionName;
    }

    public String getDirectionText() {
        return directionText;
    }

    public void setDirectionText(String directionText) {
        this.directionText = directionText;
    }

    public String getDirectionImage() {
        return directionImage;
    }

    public void setDirectionImage(String directionImage) {
        this.directionImage = directionImage;
    }
}
