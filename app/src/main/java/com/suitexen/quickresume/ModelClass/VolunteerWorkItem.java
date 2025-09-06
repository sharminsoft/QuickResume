package com.suitexen.quickresume.ModelClass;

public class VolunteerWorkItem extends BaseResumeItem {
    private String organizationName;
    private String role;
    private String duration;

    private String description;

    public VolunteerWorkItem() {}

    public VolunteerWorkItem(String organizationName, String role, String duration, String description) {
        this.organizationName = organizationName;
        this.role = role;
        this.duration = duration;
        this.description = description;
    }

    public VolunteerWorkItem(String id, String organizationName, String role, String duration, String description) {
        super(id);
        this.organizationName = organizationName;
        this.role = role;
        this.duration = duration;
        this.description = description;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}