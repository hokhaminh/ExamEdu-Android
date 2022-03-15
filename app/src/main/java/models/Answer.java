package models;

public class Answer {
    private int answerId;
    private String answerContent;

    public Answer(int answerId, String answerContent) {
        this.answerId = answerId;
        this.answerContent = answerContent;
    }

    public int getAnswerId() {
        return answerId;
    }

    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }

    public String getAnswerContent() {
        return answerContent;
    }

    public void setAnswerContent(String answerContent) {
        this.answerContent = answerContent;
    }
}
