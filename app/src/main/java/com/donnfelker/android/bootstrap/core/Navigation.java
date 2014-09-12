package com.donnfelker.android.bootstrap.core;

/**
 * Created by tulga on 9/10/14.
 */
public class Navigation {
    int imageId;
    String text;

    public Navigation(int imageId, String text) {
        this.imageId = imageId;
        this.text = text;
    }

    public int getImageId() {
        return imageId;
    }

    public String getText() {
        return text;
    }
}
