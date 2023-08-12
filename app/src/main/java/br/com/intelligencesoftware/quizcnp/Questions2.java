package br.com.intelligencesoftware.quizcnp;

public class Questions2 {

    private int code;
    private String question;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private String comment;
    private int correct;
    private int incorrect;
    private int solved;
    private int answerNr;


    public Questions2 () {

    }


    public Questions2 (int code,String question, String option1, String option2, String comment, int correct, int incorrect, int solved, int answerNr) {
        this.code = code;
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.comment = comment;
        this.correct = correct;
        this.incorrect = incorrect;
        this.solved = solved;
        this.answerNr = answerNr;
    }


    public int getCorrect() {
        return correct;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }

    public int getIncorrect() {
        return incorrect;
    }

    public void setIncorrect(int incorrect) {
        this.incorrect = incorrect;
    }

    public int getSolved() {
        return solved;
    }

    public void setSolved(int solved) {
        this.solved = solved;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getAnswerNr() {
        return answerNr;
    }

    public void setAnswerNr(int answerNr) {
        this.answerNr = answerNr;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}