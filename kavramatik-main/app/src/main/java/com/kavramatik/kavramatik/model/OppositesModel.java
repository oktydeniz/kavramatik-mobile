package com.kavramatik.kavramatik.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class OppositesModel implements BaseModel {

    @PrimaryKey
    @SerializedName("id")
    private int id;

    @ColumnInfo
    @SerializedName("opposite_name")
    private String oppositeName;

    @ColumnInfo
    @SerializedName("opposite_one_image_text")
    private String oppositeOneImageText;

    @ColumnInfo
    @SerializedName("opposite_two_image_text")
    private String oppositeTwoImageText;

    @ColumnInfo
    @SerializedName("opposite_one_image")
    private String oppositeOneImage;

    @ColumnInfo
    @SerializedName("opposite_two_image")
    private String oppositeTwoImage;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOppositeName() {
        return oppositeName;
    }

    public void setOppositeName(String oppositeName) {
        this.oppositeName = oppositeName;
    }

    public String getOppositeOneImageText() {
        return oppositeOneImageText;
    }

    public void setOppositeOneImageText(String oppositeOneImageText) {
        this.oppositeOneImageText = oppositeOneImageText;
    }

    public String getOppositeTwoImageText() {
        return oppositeTwoImageText;
    }

    public void setOppositeTwoImageText(String oppositeTwoImageText) {
        this.oppositeTwoImageText = oppositeTwoImageText;
    }

    public String getOppositeOneImage() {
        return oppositeOneImage;
    }

    public void setOppositeOneImage(String oppositeOneImage) {
        this.oppositeOneImage = oppositeOneImage;
    }

    public String getOppositeTwoImage() {
        return oppositeTwoImage;
    }

    public void setOppositeTwoImage(String oppositeTwoImage) {
        this.oppositeTwoImage = oppositeTwoImage;
    }
}
