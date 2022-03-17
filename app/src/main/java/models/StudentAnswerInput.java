package models;

public class StudentAnswerInput {
    public String studentAnswerContent ;
    public int studentId ;
    public int ExamQuestionId ;

    public StudentAnswerInput(String studentAnswerContent, int studentId, int examQuestionId) {
        this.studentAnswerContent = studentAnswerContent;
        this.studentId = studentId;
        ExamQuestionId = examQuestionId;
    }

    public String getStudentAnswerContent() {
        return studentAnswerContent;
    }

    public void setStudentAnswerContent(String studentAnswerContent) {
        this.studentAnswerContent = studentAnswerContent;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getExamQuestionId() {
        return ExamQuestionId;
    }

    public void setExamQuestionId(int examQuestionId) {
        ExamQuestionId = examQuestionId;
    }
}
