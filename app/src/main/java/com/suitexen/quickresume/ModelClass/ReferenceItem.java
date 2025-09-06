package com.suitexen.quickresume.ModelClass;

public class ReferenceItem extends BaseResumeItem {
    private String referenceName;
    private String referencePosition;
    private String referenceCompany;
    private String referenceContact;
    private String referenceEmail;

    public ReferenceItem() {}

    public ReferenceItem(String id, String referenceName, String referencePosition,
                        String referenceCompany, String referenceContact, String referenceEmail) {
        super(id);
        this.referenceName = referenceName;
        this.referencePosition = referencePosition;
        this.referenceCompany = referenceCompany;
        this.referenceContact = referenceContact;
        this.referenceEmail = referenceEmail;
    }

    public String getReferenceName() {
        return referenceName;
    }

    public void setReferenceName(String referenceName) {
        this.referenceName = referenceName;
    }

    public String getReferencePosition() {
        return referencePosition;
    }

    public void setReferencePosition(String referencePosition) {
        this.referencePosition = referencePosition;
    }

    public String getReferenceCompany() {
        return referenceCompany;
    }

    public void setReferenceCompany(String referenceCompany) {
        this.referenceCompany = referenceCompany;
    }

    public String getReferenceContact() {
        return referenceContact;
    }

    public void setReferenceContact(String referenceContact) {
        this.referenceContact = referenceContact;
    }

    public String getReferenceEmail() {
        return referenceEmail;
    }

    public void setReferenceEmail(String referenceEmail) {
        this.referenceEmail = referenceEmail;
    }
}