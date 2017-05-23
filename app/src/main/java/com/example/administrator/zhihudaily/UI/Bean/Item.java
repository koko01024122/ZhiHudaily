package com.example.administrator.zhihudaily.UI.Bean;

/**
 * Created by Administrator on 2017/5/22 0022.
 */

public class Item {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getHeadtitle() {
        return headtitle;
    }

    public void setHeadtitle(String headtitle) {
        this.headtitle = headtitle;
    }

    String headtitle;
    String id;
    String title;
    String date;
    String imgurl;
}
