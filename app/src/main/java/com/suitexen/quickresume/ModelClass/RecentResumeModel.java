package com.suitexen.quickresume.ModelClass;

import android.graphics.Bitmap;

public class RecentResumeModel {
    private String fileName;
    private String filePath;
    private Bitmap thumbnail;
    private String exportDateTime;

    public RecentResumeModel(String fileName, String filePath, Bitmap thumbnail, String exportDateTime) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.thumbnail = thumbnail;
        this.exportDateTime = exportDateTime;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getExportDateTime() {
        return exportDateTime;
    }

    public void setExportDateTime(String exportDateTime) {
        this.exportDateTime = exportDateTime;
    }
}

