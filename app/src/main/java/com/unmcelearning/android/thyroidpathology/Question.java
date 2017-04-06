package com.unmcelearning.android.thyroidpathology;

/**
 * Created by adamk_000 on 8/16/2016.
 */
public class Question {

    private String subjectString;
    private String levelString;
    private int questionId;
    private String question;
    private String correctAns;
    private String altCorrectAns1;
    private String altCorrectAns2;
    private String altCorrectAns3;
    private String incorrectAns1 = "empty12345";
    private String incorrectAns2;
    private String incorrectAns3;
    private int timesAnsweredCorrectly = 0;
    private int timesAnsweredIncorrectly = 0;
    private double percentAnsweredCorrectly;
    private int totalTimesQuestioned = timesAnsweredCorrectly + timesAnsweredIncorrectly;
    private boolean fillInTheBlank = false;
    private boolean multipleChoice = false;
    private boolean gradeSelf = false;

    public Question(String subjectCategory, String levelString, int questionId, String question,
                    String correctAns, String incorrectAns1, String incorrectAns2, String incorrectAns3,
                    boolean fillInTheBlank, boolean multipleChoice, boolean gradeSelf) {

        setSubjectString(subjectCategory);
        setLevelString(levelString);
        setQuestionId(questionId);
        setQuestion(question);
        setCorrectAns(correctAns);
        setIncorrectAns1(incorrectAns1);
        setIncorrectAns2(incorrectAns2);
        setIncorrectAns3(incorrectAns3);
        setFillInTheBlank(fillInTheBlank);
        setMultipleChoice(multipleChoice);
        timesAnsweredCorrectly = 0;

    }

    public Question(String subjectCategory, String levelString, int questionId, String question, String correctAns,
                    String altCorrectAns1, String incorrectAns1, String incorrectAns2, String incorrectAns3,
                    boolean fillInTheBlank, boolean multipleChoice, boolean gradeSelf) {

        setSubjectString(subjectCategory);
        setLevelString(levelString);
        setQuestionId(questionId);
        setQuestion(question);
        setCorrectAns(correctAns);
        setAltCorrectAns1(altCorrectAns1);
        setIncorrectAns1(incorrectAns1);
        setIncorrectAns2(incorrectAns2);
        setIncorrectAns3(incorrectAns3);
        setFillInTheBlank(fillInTheBlank);
        setMultipleChoice(multipleChoice);
        timesAnsweredCorrectly = 0;

    }

    public Question(String subjectCategory, String levelString, int questionId, String question, String correctAns,
                    String altCorrectAns1, String altCorrectAns2, String incorrectAns1,
                    String incorrectAns2, String incorrectAns3, boolean fillInTheBlank,
                    boolean multipleChoice, boolean gradeSelf) {

        setSubjectString(subjectCategory);
        setLevelString(levelString);
        setQuestionId(questionId);
        setQuestion(question);
        setCorrectAns(correctAns);
        setAltCorrectAns1(altCorrectAns1);
        setAltCorrectAns2(altCorrectAns2);
        setIncorrectAns1(incorrectAns1);
        setIncorrectAns2(incorrectAns2);
        setIncorrectAns3(incorrectAns3);
        setFillInTheBlank(fillInTheBlank);
        setMultipleChoice(multipleChoice);
        timesAnsweredCorrectly = 0;

    }

    public Question(String subjectCategory, String levelString, int questionId, String question, String correctAns,
                    String altCorrectAns1, String altCorrectAns2, String incorrectAns1,
                    String incorrectAns2, String incorrectAns3, boolean fillInTheBlank, boolean multipleChoice,
                    String altCorrectAns3, boolean gradeSelf) {

        setSubjectString(subjectCategory);
        setLevelString(levelString);
        setQuestionId(questionId);
        setQuestion(question);
        setCorrectAns(correctAns);
        setAltCorrectAns1(altCorrectAns1);
        setAltCorrectAns2(altCorrectAns2);
        setAltCorrectAns3(altCorrectAns3);
        setIncorrectAns1(incorrectAns1);
        setIncorrectAns2(incorrectAns2);
        setIncorrectAns3(incorrectAns3);
        setFillInTheBlank(fillInTheBlank);
        setMultipleChoice(multipleChoice);
        timesAnsweredCorrectly = 0;

    }

    public Question(String subjectCategory, String levelString, int questionId, String question, String correctAns,
                    String altCorrectAns1, String altCorrectAns2, boolean fillInTheBlank, boolean multipleChoice,
                    boolean gradeSelf) {

        setSubjectString(subjectCategory);
        setLevelString(levelString);
        setQuestionId(questionId);
        setQuestion(question);
        setCorrectAns(correctAns);
        setAltCorrectAns1(altCorrectAns1);
        setAltCorrectAns2(altCorrectAns2);
        setFillInTheBlank(fillInTheBlank);
        setMultipleChoice(multipleChoice);
        timesAnsweredCorrectly = 0;

    }

    public Question(String subjectCategory, String levelString, int questionId, String question, String correctAns,
                    String altCorrectAns1, String altCorrectAns2, boolean fillInTheBlank, boolean multipleChoice,
                    String altCorrectAns3, boolean gradeSelf) {

        setSubjectString(subjectCategory);
        setLevelString(levelString);
        setQuestionId(questionId);
        setQuestion(question);
        setCorrectAns(correctAns);
        setAltCorrectAns1(altCorrectAns1);
        setAltCorrectAns2(altCorrectAns2);
        setAltCorrectAns3(altCorrectAns3);
        setFillInTheBlank(fillInTheBlank);
        setMultipleChoice(multipleChoice);
        timesAnsweredCorrectly = 0;

    }

    public Question(String subjectCategory, String levelString, int questionId, String question, String correctAns,
                    String altCorrectAns1, boolean fillInTheBlank, boolean multipleChoice, boolean gradeSelf) {

        setSubjectString(subjectCategory);
        setLevelString(levelString);
        setQuestionId(questionId);
        setQuestion(question);
        setCorrectAns(correctAns);
        setAltCorrectAns1(altCorrectAns1);
        setFillInTheBlank(fillInTheBlank);
        setMultipleChoice(multipleChoice);
        timesAnsweredCorrectly = 0;

    }

    public Question(String subjectCategory, String levelString, int questionId, String question, String answer,
                    boolean fillInTheBlank, boolean multipleChoice, boolean gradeSelf) {
        setSubjectString(subjectCategory);
        setLevelString(levelString);
        setQuestionId(questionId);
        setQuestion(question);
        setCorrectAns(answer);
        setFillInTheBlank(fillInTheBlank);
        setMultipleChoice(multipleChoice);
        timesAnsweredCorrectly = 0;
    }

    public void setSubjectString(String subjectString) {
        this.subjectString = subjectString;
    }

    public void setFillInTheBlank(boolean fillInTheBlank) {
        this.fillInTheBlank = fillInTheBlank;
    }

    public void setMultipleChoice(boolean multipleChoice) {
        this.multipleChoice = multipleChoice;
    }

    public void setLevelString(String levelString) {
        this.levelString = levelString;
    }

    public void setGradeSelf(boolean gradeSelf) {

        this.gradeSelf = gradeSelf;
    }

    public void setAltCorrectAns1(String altCorrectAns1) {
        this.altCorrectAns1 = altCorrectAns1;
    }

    public void setAltCorrectAns2(String altCorrectAns2) {
        this.altCorrectAns2 = altCorrectAns2;
    }

    public void setAltCorrectAns3(String altCorrectAns3) {
        this.altCorrectAns3 = altCorrectAns3;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setCorrectAns(String correctAns) {
        this.correctAns = correctAns;
    }

    public void setIncorrectAns1(String incorrectAns1) {
        this.incorrectAns1 = incorrectAns1;
    }

    public void setIncorrectAns2(String incorrectAns2) {
        this.incorrectAns2 = incorrectAns2;
    }

    public void setIncorrectAns3(String incorrectAns3) {
        this.incorrectAns3 = incorrectAns3;
    }

    public void setTimesAnsweredCorrectly(int timesAnsweredCorrectly) {
        this.timesAnsweredCorrectly = timesAnsweredCorrectly;
    }

    public void setTimesAnsweredIncorrectly(int timesAnsweredIncorrectly) {
        this.timesAnsweredIncorrectly = timesAnsweredIncorrectly;
    }

    public void setPercentAnsweredCorrectly(double percentAnsweredCorrectly) {
        this.percentAnsweredCorrectly = percentAnsweredCorrectly;
    }

    public String getSubjectString() {
        return subjectString;
    }

    public int getQuestionId() {
        return questionId;
    }

    public String getQuestion() {
        return question;
    }

    public String getCorrectAns() {
        return correctAns;
    }

    public String getAltCorrectAns1() {
        return altCorrectAns1;
    }

    public boolean getFillInTheBlank() {
        return fillInTheBlank;
    }

    public boolean getGradeSelf() {
        return gradeSelf;
    }

    public boolean getMultipleChoice() {
        return multipleChoice;
    }

    public String getAltCorrectAns2() {
        return altCorrectAns2;
    }

    public String getAltCorrectAns3() {
        return altCorrectAns3;
    }

    public String getIncorrectAns1() {
        return incorrectAns1;
    }

    public String getIncorrectAns2() {
        return incorrectAns2;
    }

    public String getIncorrectAns3() {
        return incorrectAns3;
    }


    public int getTimesAnsweredCorrectly() {
        return timesAnsweredCorrectly;
    }

    public int getTimesAnsweredIncorrectly() {
        return timesAnsweredIncorrectly;
    }

    public double getPercentAnsweredCorrectly() {
        return percentAnsweredCorrectly;
    }

    public int getTotalTimesQuestioned() {
        return timesAnsweredCorrectly + timesAnsweredIncorrectly;
    }

    public String getLevelString() {
        return levelString;
    }


}
