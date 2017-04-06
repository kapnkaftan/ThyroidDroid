package com.unmcelearning.android.thyroidpathology;

/**
 * Created by adamk_000 on 1/5/2017.
 */

public class Case {

    private String caseName;
    private boolean completed;
    private String text;
    private String question;
    private String correctAns;
    private String incAns1;
    private String incAns2;
    private String incAns3;

    public Case(String caseName, boolean completed) {

        setCaseName(caseName);
        setCompleted(completed);

    }

    public Case(String caseName, String text, String question, String correctAns, String incAns1, String
            incAns2, String incAns3, boolean completed) {

        setCaseName(caseName);
        setCaseText(text);
        setCaseQuestion(question);
        setCaseCorrectAns(correctAns);
        setCaseIncAns1(incAns1);
        setCaseIncAns2(incAns2);
        setCaseIncAns3(incAns3);
        setCompleted(completed);


    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void setCaseText(String text) {
        this.text = text;
    }

    public void setCaseQuestion(String question) {
        this.question = question;
    }

    public void setCaseCorrectAns(String correctAns) {
        this.correctAns = correctAns;
    }

    public void setCaseIncAns1(String incAns1) {
        this.incAns1 = incAns1;
    }

    public void setCaseIncAns2(String incAns2) {
        this.incAns2 = incAns2;
    }

    public void setCaseIncAns3(String incAns3) {
        this.incAns3 = incAns3;
    }

    public String getCaseName() {
        return caseName;
    }

    public boolean getCompleted() {
        return completed;
    }

}
