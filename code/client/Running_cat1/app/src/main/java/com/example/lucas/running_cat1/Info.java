package com.example.lucas.running_cat1;

/**
 * Created by yanzhensong on 4/25/16.
 */
public class Info {
    private String brief;
    private int imageId;
    public Info(String brief, int imageId) {
        this.brief = brief;
        this.imageId = imageId;
    }
    public String getBrief() {
        return brief;
    }
    public int getImageId() {
        return imageId;
    }
}
