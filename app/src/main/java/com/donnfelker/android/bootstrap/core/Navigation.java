package com.donnfelker.android.bootstrap.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tulga on 9/10/14.
 */
public class Navigation {
    int imageId;
    String text;
    List<Category> categories = new ArrayList<Category>();

    public Navigation(int imageId, String text) {
        this.imageId = imageId;
        this.text = text;
    }

    public Navigation(int imageId, String text, List<Category> categories) {
        this.imageId = imageId;
        this.text = text;
        this.categories = categories;
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
