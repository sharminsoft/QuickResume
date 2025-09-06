package com.suitexen.quickresume.ModelClass;

public class EducationItem extends BaseResumeItem {
    private String degreeName;
    private String universityName;
    private String startDate;
    private String endDate;
    private String cgpa;

    public EducationItem() {}


    public EducationItem(String degreeName, String universityName, String startDate, String endDate, String cgpa) {
        this.degreeName = degreeName;
        this.universityName = universityName;
        this.startDate = startDate;
        this.endDate = endDate;

        this.cgpa = cgpa;
    }

    public EducationItem(String id, String degreeName, String instituteName, String startDate, String endDate, String yearOfStudy, String cgpa) {
        super(id);
        this.degreeName = degreeName;
        this.universityName = instituteName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.cgpa = cgpa;
    }

    public String getDegreeName() {
        return degreeName;
    }

    public void setDegreeName(String degreeName) {
        this.degreeName = degreeName;
    }

    public String getUniversityName() {
        return universityName;
    }

    public void setUniversityName(String universityName) {
        this.universityName = universityName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }



    public String getCgpa() {
        return cgpa;
    }

    public void setCgpa(String cgpa) {
        this.cgpa = cgpa;
    }
}