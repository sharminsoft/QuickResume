package com.suitexen.quickresume.ModelClass;

public class CareerObjectiveItem extends BaseResumeItem {
    private String careerObjective;

    public CareerObjectiveItem() {}

    public CareerObjectiveItem(String id, String careerObjective) {
        super(id);
        this.careerObjective = careerObjective;
    }

    public String getCareerObjective() {
        return careerObjective;
    }

    public void setCareerObjective(String careerObjective) {
        this.careerObjective = careerObjective;
    }
}