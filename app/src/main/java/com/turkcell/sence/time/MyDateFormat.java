package com.turkcell.sence.time;

public class MyDateFormat {

    private int sDay ;
    private int sMonth;
    private int sYear;
    private int sHour;
    private int sMinute;
    private int sSecond;

    public MyDateFormat(int sDay, int sMonth, int sYear, int sHour, int sMinute, int sSecond) {
        this.sDay = sDay;
        this.sMonth = sMonth;
        this.sYear = sYear;
        this.sHour = sHour;
        this.sMinute = sMinute;
        this.sSecond = sSecond;
    }

    public int getsDay() {
        return sDay;
    }

    public void setsDay(int sDay) {
        this.sDay = sDay;
    }

    public int getsMonth() {
        return sMonth;
    }

    public void setsMonth(int sMonth) {
        this.sMonth = sMonth;
    }

    public int getsYear() {
        return sYear;
    }

    public void setsYear(int sYear) {
        this.sYear = sYear;
    }

    public int getsHour() {
        return sHour;
    }

    public void setsHour(int sHour) {
        this.sHour = sHour;
    }

    public int getsMinute() {
        return sMinute;
    }

    public void setsMinute(int sMinute) {
        this.sMinute = sMinute;
    }

    public int getsSecond() {
        return sSecond;
    }

    public void setsSecond(int sSecond) {
        this.sSecond = sSecond;
    }
}
