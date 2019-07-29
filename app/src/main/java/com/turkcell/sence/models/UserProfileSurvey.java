package com.turkcell.sence.models;

import java.util.Date;

public class UserProfileSurvey {

    private String soru;
    private String kategori;
    private Date baslangicZamani;
    private Date bitisZamani;
    private int image1;
    private int image2;
    private int toplamOySayisi;
    private int image1OySayisi;
    private int image2OySayisi;

    public UserProfileSurvey(String soru, String kategori, Date baslangicZamani, Date bitisZamani, int image1, int image2, int toplamOySayisi, int image1OySayisi, int image2OySayisi) {
        this.soru = soru;
        this.kategori = kategori;
        this.baslangicZamani = baslangicZamani;
        this.bitisZamani = bitisZamani;
        this.image1 = image1;
        this.image2 = image2;
        this.toplamOySayisi = toplamOySayisi;
        this.image1OySayisi = image1OySayisi;
        this.image2OySayisi = image2OySayisi;

    }

    public UserProfileSurvey() {
    }

    public String getSoru() {
        return soru;
    }
    public void setSoru(String soru) {
        this.soru = soru;
    }


    public String getKategori() { return kategori; }
    public void setKategori(String kategori) {
        this.kategori = kategori;
    }


    public Date getBaslangicZamani() {
        return baslangicZamani;
    }
    public void setBaslangicZamani(Date baslangicZamani) {
        this.baslangicZamani = baslangicZamani;
    }


    public Date getBitisZamani() {
        return bitisZamani;
    }
    public void setBitisZamani(Date bitisZamani) {
        this.bitisZamani = bitisZamani;
    }


    public int getImage1() { return image1; }
    public void setImage1(int image1) {
        this.image1 = image1;
    }


    public int getImage2() { return image2;  }
    public void setImage2(int image2) {
        this.image2 = image2;
    }


    public int getImage1OySayisi() { return image1OySayisi; }
    public void setImage1OySayisi(int image1OySayisi) {
        this.image1OySayisi = image1OySayisi;
    }


    public int getImage2OySayisi() { return image2OySayisi; }
    public void setImage2OySayisi(int image2OySayisi) {
        this.image2OySayisi = image2OySayisi;
    }


    public int getToplamOySayisi() { return toplamOySayisi; }
    public void setToplamOySayisi(int toplamOySayisi) {
        this.toplamOySayisi = toplamOySayisi;
    }
}
