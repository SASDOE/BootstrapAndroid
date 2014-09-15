package com.donnfelker.android.bootstrap.core;

import java.io.Serializable;

public class News implements Serializable {

    private static final long serialVersionUID = -6641292855569752036L;

    private String title;
    private String thumbnail;
    private String content;
    private String objectId;

    public News(String content, String thumbnail, String title) {
        this.content = content;
        this.thumbnail = thumbnail;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(final String objectId) {
        this.objectId = objectId;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
