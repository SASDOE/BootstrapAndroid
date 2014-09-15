package com.donnfelker.android.bootstrap.core;

import java.util.List;

/**
 * Created by tulga on 9/10/14.
 */
public class Navigation {
    int imageId;
    String text;
    List<Category> categories;

    public Navigation(int imageId, String text) {
        this.imageId = imageId;
        this.text = text;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public int getImageId() {
        return imageId;
    }

    public String getText() {
        return text;
    }
}
