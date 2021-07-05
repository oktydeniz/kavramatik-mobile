package com.kavramatik.kavramatik.model;

import com.google.gson.annotations.SerializedName;

public class StatusModel {

    @SerializedName("status")
    private boolean status;

    public boolean getStatus() {
        return status;
    }
}
