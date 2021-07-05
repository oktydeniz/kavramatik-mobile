package com.kavramatik.kavramatik.database;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kavramatik.kavramatik.model.BaseModel;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

public class DataConverter implements Serializable {

    @TypeConverter
    public String fromValuesToJson(List<BaseModel> models) {
        if (models == null) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<BaseModel>>() {
        }.getType();
        return gson.toJson(models, type);
    }

    @TypeConverter
    public List<BaseModel> toOptionValuesList(String optionValuesString) {
        if (optionValuesString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<BaseModel>>() {
        }.getType();
        return gson.fromJson(optionValuesString, type);
    }
}
