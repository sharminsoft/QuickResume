package com.suitexen.quickresume.ModelClass;

public class SkillsItem extends BaseResumeItem {
    private String skill;


    public SkillsItem() {}

    public SkillsItem(String skill) {
        this.skill = skill;
    }

    public SkillsItem(String id, String skill) {
        super(id);
        this.skill = skill;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }
}