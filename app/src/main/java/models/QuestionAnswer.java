package models;

import java.util.List;

public class QuestionAnswer {
    private int examQuestionId;
    private String questionContent, questionImageURL;
    private List<Answer> answers;

    public QuestionAnswer(int examQuestionId, String questionContent, String questionImageURL, List<Answer> answers) {
        this.examQuestionId = examQuestionId;
        this.questionContent = questionContent;
        this.questionImageURL = questionImageURL;
        this.answers = answers;
    }

    public int getExamQuestionId() {
        return examQuestionId;
    }

    public void setExamQuestionId(int examQuestionId) {
        this.examQuestionId = examQuestionId;
    }

    public String getQuestionContent() {
        return questionContent;
    }

    public void setQuestionContent(String questionContent) {
        this.questionContent = questionContent;
    }

    public String getQuestionImageURL() {
        return questionImageURL;
    }

    public void setQuestionImageURL(String questionImageURL) {
        this.questionImageURL = questionImageURL;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }
}
