package com.turkcell.sence.models;

public class Ongoing {

    private int image;
    private int image2;
    private String question;
    private String time;
    private String participants;


    public Ongoing(int firstPhoto_iv,int secondPhoto_iv, String question_Tv,String time_Tv, String participants)
    {
        this.image= firstPhoto_iv;
        this.image2= secondPhoto_iv;
        this.question= question_Tv;
        this.time= time_Tv;
        this.participants = participants;

    }

    public int firstPhoto_iv() {
        return image;
    }

    public void firstPhoto_iv(int firstPhoto_iv) {
        this.image = firstPhoto_iv;
    }

    public int secondPhoto_iv() {
        return image2;
    }

    public void secondPhoto_iv(int secondPhoto_iv) {
        this.image2 = secondPhoto_iv;
    }

    public String question_Tv() {
        return question;
    }

    public void question_Tv(String question_Tv) {
        this.question = question_Tv;
    }

    public String time_Tv() {
        return time;
    }

    public void time_Tv(String time_Tv) {
        this.time = time_Tv;
    }

    public String participants() {
        return participants;
    }

    public void participants(String participants) {
        this.participants = participants;
    }
}
