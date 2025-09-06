package com.suitexen.quickresume.ModelClass;

public class AwardItem extends BaseResumeItem {
    private String awardName;
    private String yearReceived;
    private String awardDescription;

    public AwardItem() {}

    public AwardItem(String id, String awardName, String yearReceived, String awardDescription) {
        super(id);
        this.awardName = awardName;
        this.yearReceived = yearReceived;
        this.awardDescription = awardDescription;
    }

    public String getAwardName() {
        return awardName;
    }

    public void setAwardName(String awardName) {
        this.awardName = awardName;
    }

    public String getYearReceived() {
        return yearReceived;
    }

    public void setYearReceived(String yearReceived) {
        this.yearReceived = yearReceived;
    }

    public String getAwardDescription() {
        return awardDescription;
    }

    public void setAwardDescription(String awardDescription) {
        this.awardDescription = awardDescription;
    }
}