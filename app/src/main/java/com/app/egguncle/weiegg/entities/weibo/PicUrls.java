package com.app.egguncle.weiegg.entities.weibo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by egguncle on 16.10.15.
 */
public class PicUrls  implements Serializable {
    private String thumbnail_pic;

    public String getThumbnail_pic() {
        return thumbnail_pic;
    }

    public void setThumbnail_pic(String thumbnail_pic) {
        this.thumbnail_pic = thumbnail_pic;
    }
}
