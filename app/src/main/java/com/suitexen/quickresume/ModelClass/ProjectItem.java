package com.suitexen.quickresume.ModelClass;

public class ProjectItem extends BaseResumeItem {
    private String projectName;
    private String technologiesUsed;
    private String projectDescription;
    private String projectLink;

    public ProjectItem() {}

    public ProjectItem(String id, String projectName, String technologiesUsed,
                      String projectDescription, String projectLink) {
        super(id);
        this.projectName = projectName;
        this.technologiesUsed = technologiesUsed;
        this.projectDescription = projectDescription;
        this.projectLink = projectLink;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getTechnologiesUsed() {
        return technologiesUsed;
    }

    public void setTechnologiesUsed(String technologiesUsed) {
        this.technologiesUsed = technologiesUsed;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public String getProjectLink() {
        return projectLink;
    }

    public void setProjectLink(String projectLink) {
        this.projectLink = projectLink;
    }
}