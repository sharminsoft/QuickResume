package com.suitexen.quickresume.ModelClass;

public class CertificationItem extends BaseResumeItem {
    private String courseName;
    private String instituteName;
    private String completionYear;

    public CertificationItem() {}

    public CertificationItem(String id, String courseName, String instituteName, String completionYear) {
        super(id);
        this.courseName = courseName;
        this.instituteName = instituteName;
        this.completionYear = completionYear;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getInstituteName() {
        return instituteName;
    }

    public void setInstituteName(String instituteName) {
        this.instituteName = instituteName;
    }

    public String getCompletionYear() {
        return completionYear;
    }

    public void setCompletionYear(String completionYear) {
        this.completionYear = completionYear;
    }
}