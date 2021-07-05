package com.kavramatik.kavramatik.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.kavramatik.kavramatik.model.ColorCompModel;
import com.kavramatik.kavramatik.model.ColorModel;
import com.kavramatik.kavramatik.model.DimensionModel;
import com.kavramatik.kavramatik.model.DirectionModel;
import com.kavramatik.kavramatik.model.EmotionModel;
import com.kavramatik.kavramatik.model.NumberModel;
import com.kavramatik.kavramatik.model.OppositesModel;
import com.kavramatik.kavramatik.model.QuantityModel;
import com.kavramatik.kavramatik.model.SenseModel;
import com.kavramatik.kavramatik.model.ShapeModel;

@Database(entities = {ColorModel.class, DimensionModel.class, DirectionModel.class,
        EmotionModel.class, NumberModel.class, OppositesModel.class, QuantityModel.class,
        SenseModel.class, ShapeModel.class, ColorCompModel.class}, version = 1)
@TypeConverters(DataConverter.class)
public abstract class EducationDatabase extends RoomDatabase {

    public abstract EducationDao educationDao();

    private static EducationDatabase INSTANCE;

    public static EducationDatabase getInstance(Context c) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(c.getApplicationContext(), EducationDatabase.class, "educationdatabase").allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }

}
