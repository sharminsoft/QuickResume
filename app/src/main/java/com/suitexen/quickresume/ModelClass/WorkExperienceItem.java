package com.suitexen.quickresume.ModelClass;

public class WorkExperienceItem extends BaseResumeItem {
    private String jobTitle;
    private String companyName;
    private String startDate;
    private String endDate;
    private String responsibilities;


    public WorkExperienceItem() {}

    public WorkExperienceItem(String id, String jobTitle, String companyName,
                            String startDate, String endDate, String responsibilities) {
        super(id);
        this.jobTitle = jobTitle;
        this.companyName = companyName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.responsibilities = responsibilities;

    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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

    public String getResponsibilities() {
        return responsibilities;
    }

    public void setResponsibilities(String responsibilities) {
        this.responsibilities = responsibilities;
    }

}