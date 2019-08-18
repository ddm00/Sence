package com.turkcell.sence.models;

public class Survey {
    private String SurveyId;
    private String SurveyQuestion;
    private String SurveyFirstImage;
    private String SurveySecondImage;
    private String SurveyTime;
    private String SurveyCategory;
    private String SurveyPublisher;

    public Survey() {
    }

    public Survey(String surveyId, String surveyQuestion, String surveyFirstImage, String surveySecondImage, String surveyTime, String surveyCategory, String surveyPublisher) {
        this.SurveyId = surveyId;
        this.SurveyQuestion = surveyQuestion;
        this.SurveyFirstImage = surveyFirstImage;
        this.SurveySecondImage = surveySecondImage;
        this.SurveyTime = surveyTime;
        this.SurveyCategory = surveyCategory;
        this.SurveyPublisher = surveyPublisher;
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
