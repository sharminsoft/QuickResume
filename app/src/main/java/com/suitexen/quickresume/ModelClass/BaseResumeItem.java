package com.suitexen.quickresume.ModelClass;

public abstract class BaseResumeItem {
    private String id;

    public BaseResumeItem() {}

    public BaseResumeItem(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}