package com.kavramatik.kavramatik.model;

import java.io.Serializable;

public class STTModel implements Serializable {

    private String text;
    private String image;
    private String questions;

    public STTModel(String text, String image, String questions) {
        this.text = text;
        this.image = image;
        this.questions = questions;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }
}
