package com.donnfelker.android.bootstrap.core;

/**
 * Created by tulga on 9/12/14.
 */
public class SPSEvent {
    String title;
    int thumbnail;

    public SPSEvent(String title, int thumbnail) {
        this.title = title;
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public int getThumbnail() {
        return thumbnail;
    }
}
