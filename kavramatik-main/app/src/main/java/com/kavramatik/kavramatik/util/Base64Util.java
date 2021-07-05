package com.kavramatik.kavramatik.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import java.io.ByteArrayOutputStream;

public class Base64Util {

    public static byte[] getPictureByteOfArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    public static Bitmap getBitmapFromByte(byte[] bytes) {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

    }

    private static Bitmap makeSmallerImage(Bitmap img, int maxsize) {
        int width = img.getWidth();
        int height = img.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxsize;
            height = (int) (width / bitmapRatio);
        } else {
            width = (int) (height * bitmapRatio);
            height = maxsize;
        }

        return Bitmap.createScaledBitmap(img, width, height, true);
    }

    public static Bitmap decodeToBitmap(String path) {
        byte[] decodedString = Base64.decode(path, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
    private static CircularProgressDrawable placeHolderProgressBar(Context context) {
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.setStrokeWidth(8f);
        circularProgressDrawable.setCenterRadius(48f);
        circularProgressDrawable.start();
        return circularProgressDrawable;
    }
}
