package com.kavramatik.kavramatik.util;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

/**
 * this is for dataBinding Image views
 *
 * @version 1.0
 * @auth oktydeniz
 */
public class BindingImage {
    @BindingAdapter("imageUrl")
    public static void setImage(ImageView imageView, String url) {
        if (url != null) {
            imageView.setImageBitmap(Base64Util.decodeToBitmap(url));
        }
    }

}
