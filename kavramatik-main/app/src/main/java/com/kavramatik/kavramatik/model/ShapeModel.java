package com.kavramatik.kavramatik.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class ShapeModel implements BaseModel {
    @PrimaryKey
    @SerializedName("id")
    private int id;

    @ColumnInfo
    @SerializedName("shape_name")
    private String shapeName;

    @ColumnInfo
    @SerializedName("shape_text")
    private String ShapeText;

    @ColumnInfo
    @SerializedName("shape_image")
    private String shapeImage;

    @ColumnInfo
    @SerializedName("shape_two_text")
    private String shapeTwoText;

    @ColumnInfo
    @SerializedName("shape_two_image")
    private String shapeTwoImage;

    public String getShapeTwoText() {
        return shapeTwoText;
    }

    public void setShapeTwoText(String shapeTwoText) {
        this.shapeTwoText = shapeTwoText;
    }

    public String getShapeTwoImage() {
        return shapeTwoImage;
    }

    public void setShapeTwoImage(String shapeTwoImage) {
        this.shapeTwoImage = shapeTwoImage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShapeName() {
        return shapeName;
    }

    public void setShapeName(String shapeName) {
        this.shapeName = shapeName;
    }

    public String getShapeText() {
        return ShapeText;
    }

    public void setShapeText(String shapeText) {
        ShapeText = shapeText;
    }

    public String getShapeImage() {
        return shapeImage;
    }

    public void setShapeImage(String shapeImage) {
        this.shapeImage = shapeImage;
    }
}
