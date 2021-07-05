package com.kavramatik.kavramatik.model;

import java.io.Serializable;
import java.util.ArrayList;

public class STTPassModel implements Serializable {
    private ArrayList<STTModel> allModels;

    public STTPassModel(ArrayList<STTModel> allModels) {
        this.allModels = allModels;
    }

    public ArrayList<STTModel> getAllModels() {
        return allModels;
    }

    public void setAllModels(ArrayList<STTModel> allModels) {
        this.allModels = allModels;
    }
}
