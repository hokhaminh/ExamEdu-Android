package models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class MarkReport {
    public int moduleID;
    public String moduleName;
    public String examName;
    public String examDate;
    public float mark;

    public MarkReport(int moduleID, String moduleName, String examName, String examDate, float mark, String comment) {
        this.moduleID = moduleID;
        this.moduleName = moduleName;
        this.examName = examName;
        this.examDate = examDate;
        this.mark = mark;
        this.comment = comment;
    }

    public String comment;

    public int getModuleID() {
        return moduleID;
    }

    public void setModuleID(int moduleID) {
        this.moduleID = moduleID;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public String getExamDate() {
        return examDate;
    }

    public void setExamDate(String examDate) {
        this.examDate = examDate;
    }

    public float getMark() {
        return mark;
    }

    public void setMark(float mark) {
        this.mark = mark;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
