package com.turkcell.sence.models;

public class Complated {
    private int image;
    private int image2;
    private String question;
    private String time;
    private String participantsP;


    public Complated(int firstPhotoF_iv,int secondPhotoS_iv, String questionQ_Tv,String timeT_Tv, String participantsP)
    {
        this.image= firstPhotoF_iv;
        this.image2= secondPhotoS_iv;
        this.question= questionQ_Tv;
        this.time= timeT_Tv;
        this.participantsP = participantsP;

    }

    public int firstPhotoF_iv() {
        return image;
    }

    public void firstPhotoF_iv(int firstPhotoF_iv) {
        this.image = firstPhotoF_iv;
    }

    public int secondPhotoS_iv() {
        return image2;
    }

    public void secondPhotoS_iv(int secondPhotoS_iv) {
        this.image2 = secondPhotoS_iv;
    }

    public String questionQ_Tv() {
        return question;
    }

    public void questionQ_Tv(String questionQ_Tv) {
        this.question = questionQ_Tv;
    }

    public String timeT_Tv() {
        return time;
    }

    public void timeT_Tv(String timeT_Tv) {
        this.time = timeT_Tv;
    }

    public String participantsP() {
        return participantsP;
    }

    public void participantsP(String participants) {
        this.participantsP = participantsP;
    }
}
