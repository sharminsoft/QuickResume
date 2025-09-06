package com.suitexen.quickresume.ModelClass;

public class LanguageItem extends BaseResumeItem {
    private String nativeLanguage;


    public LanguageItem() {}

    public LanguageItem(String id, String nativeLanguage) {
        super(id);
        this.nativeLanguage = nativeLanguage;

    }

    public String getNativeLanguage() {
        return nativeLanguage;
    }

    public void setNativeLanguage(String nativeLanguage) {
        this.nativeLanguage = nativeLanguage;
    }


}