package models;

import java.util.List;

public class ExamQuestion {
    private int examId, durationInMinute;
    private boolean isFinalExam;
    private String moduleCode;
    private List<QuestionAnswer> questionAnswer;

    public ExamQuestion(int examId, int durationInMinute, boolean isFinalExam, String moduleCode, List<QuestionAnswer> questionAnswer) {
        this.examId = examId;
        this.durationInMinute = durationInMinute;
        this.isFinalExam = isFinalExam;
        this.moduleCode = moduleCode;
        this.questionAnswer = questionAnswer;
    }

    public int getExamId() {
        return examId;
    }

    public void setExamId(int examId) {
        this.examId = examId;
    }

    public int getDurationInMinute() {
        return durationInMinute;
    }

    public void setDurationInMinute(int durationInMinute) {
        this.durationInMinute = durationInMinute;
    }

    public boolean isFinalExam() {
        return isFinalExam;
    }

    public void setFinalExam(boolean finalExam) {
        isFinalExam = finalExam;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public List<QuestionAnswer> getQuestionAnswer() {
        return questionAnswer;
    }

    public void setQuestionAnswer(List<QuestionAnswer> questionAnswer) {
        this.questionAnswer = questionAnswer;
    }
}
