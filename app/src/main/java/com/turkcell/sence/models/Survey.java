package com.turkcell.sence.models;

public class Survey {
    private String SurveyId;
    private String SurveyQuestion;
    private String SurveyFirstImage;
    private String SurveySecondImage;
    private String SurveyTime;
    private String SurveyCategory;
    private String SurveyPublisher;
    private Long t;
    private User user;
    private Boolean isWhichOne;
    private boolean isSecret;
    private int reySize;

    public Survey(String surveyId, String surveyQuestion, String surveyFirstImage, String surveySecondImage, String surveyTime, String surveyCategory, String surveyPublisher, Long t) {
        SurveyId = surveyId;
        SurveyQuestion = surveyQuestion;
        SurveyFirstImage = surveyFirstImage;
        SurveySecondImage = surveySecondImage;
        SurveyTime = surveyTime;
        SurveyCategory = surveyCategory;
        SurveyPublisher = surveyPublisher;
        this.t = t;
    }

    public Survey(String surveyId, String surveyQuestion, String surveyFirstImage, String surveySecondImage, String surveyTime, String surveyCategory, String surveyPublisher, Long t, Boolean isWhichOne, int reySize, boolean isSecret) {
        SurveyId = surveyId;
        SurveyQuestion = surveyQuestion;
        SurveyFirstImage = surveyFirstImage;
        SurveySecondImage = surveySecondImage;
        SurveyTime = surveyTime;
        SurveyCategory = surveyCategory;
        SurveyPublisher = surveyPublisher;
        this.t = t;
        this.isWhichOne = isWhichOne;
        this.reySize = reySize;
        this.isSecret = isSecret;

    }

    public boolean getSecret() {
        return isSecret;
    }

    public void setSecret(boolean secret) {
        isSecret = secret;
    }

    public int getReySize() {
        return reySize;
    }

    public void setReySize(int reySize) {
        this.reySize = reySize;
    }

    public Boolean getWhichOne() {
        return isWhichOne;
    }

    public void setWhichOne(Boolean whichOne) {
        isWhichOne = whichOne;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getT() {
        return t;
    }

    public void setT(Long t) {
        this.t = t;
    }

    public String getSurveyId() {
        return SurveyId;
    }

    public void setSurveyId(String surveyId) {
        SurveyId = surveyId;
    }

    public String getSurveyQuestion() {
        return SurveyQuestion;
    }

    public void setSurveyQuestion(String surveyQuestion) {
        SurveyQuestion = surveyQuestion;
    }

    public String getSurveyFirstImage() {
        return SurveyFirstImage;
    }

    public void setSurveyFirstImage(String surveyFirstImage) {
        SurveyFirstImage = surveyFirstImage;
    }

    public String getSurveySecondImage() {
        return SurveySecondImage;
    }

    public void setSurveySecondImage(String surveySecondImage) {
        SurveySecondImage = surveySecondImage;
    }

    public String getSurveyTime() {
        return SurveyTime;
    }

    public void setSurveyTime(String surveyTime) {
        SurveyTime = surveyTime;
    }

    public String getSurveyCategory() {
        return SurveyCategory;
    }

    public void setSurveyCategory(String surveyCategory) {
        SurveyCategory = surveyCategory;
    }

    public String getSurveyPublisher() {
        return SurveyPublisher;
    }

    public void setSurveyPublisher(String surveyPublisher) {
        SurveyPublisher = surveyPublisher;
    }

}
