package com.kavramatik.kavramatik.model;

import com.google.gson.annotations.SerializedName;

public class ResponseModel {
    @SerializedName("response")
    private Boolean response;

    @SerializedName("user_name")
    private String userName;

    @SerializedName("user_email")
    private String userEmail;

    @SerializedName("id")
    private int userId;

    @SerializedName("score")
    private int score;

    public Boolean getResponse() {
        return response;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public int getUserId() {
        return userId;
    }

    public int getScore() {
        return score;
    }

}
