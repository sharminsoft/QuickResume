package com.suitexen.quickresume.ModelClass;

public class TemplatesModel {
    private String templateName;
    private String category;
    private String imageUrl;
    private long template_score;




    public TemplatesModel() {
    }


    public TemplatesModel(String templateName, String category, String imageUrl, long template_score) {
        this.templateName = templateName;
        this.category = category;
        this.imageUrl = imageUrl;
        this.template_score = template_score;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getTemplate_score() {
        return template_score;
    }

    public void setTemplate_score(long template_score) {
        this.template_score = template_score;
    }
}
