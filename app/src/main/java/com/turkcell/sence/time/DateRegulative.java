package com.turkcell.sence.time;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateRegulative {

    private static DateRegulative build;

    public static DateRegulative getInstance() {
        if (build == null) {
            build = new DateRegulative();
        }
        return build;
    }

    public DateRegulative() {

    }

    public MyDateFormat getDifference(long longDate) {
        Date currentDate = new Date();
        long currentTime = currentDate.getTime();
        Calendar localCalendar = Calendar.getInstance();

        int cDay, cMonth, cYear, cHour, cMinute, cSecond;
        int oDay, oMonth, oYear, oHour, oMinute, oSecond;
        int sDay = 0, sMonth = 0, sYear = 0, sHour = 0, sMinute = 0, sSecond = 0;
        String oDate, cDate;
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd:MM:yyyy / HH:mm:ss");

        oDate = formatter.format(longDate);
        cDate = formatter.format(currentTime);

        oDay = Integer.valueOf(oDate.substring(0, 2));
        oMonth = Integer.valueOf(oDate.substring(3, 5));
        oYear = Integer.valueOf(oDate.substring(6, 10));

        oHour = Integer.valueOf(oDate.substring(13, 15));
        oMinute = Integer.valueOf(oDate.substring(16, 18));
        oSecond = Integer.valueOf(oDate.substring(19, 21));

        //--------------------------------

        cDay = Integer.valueOf(cDate.substring(0, 2));
        cMonth = Integer.valueOf(cDate.substring(3, 5));
        cYear = Integer.valueOf(cDate.substring(6, 10));

        cHour = Integer.valueOf(cDate.substring(13, 15));
        cMinute = Integer.valueOf(cDate.substring(16, 18));
        cSecond = Integer.valueOf(cDate.substring(19, 21));

        // saniye hesaplama
        if (cikarma(cSecond, oSecond)) {
            sSecond = getCikarma(cSecond, oSecond);
        } else {
            if (cMinute != 0) {
                cMinute = cMinute - 1;
                sSecond = getCikarma(cSecond + 60, oSecond);
            } else {
                if (cHour != 0) {
                    cHour = cHour - 1;
                    cMinute = 60;
                    cMinute = cMinute - 1;
                    sSecond = getCikarma(cSecond + 60, oSecond);
                } else {
                    if (cDay != 0) {
                        cDay = cDay - 1;
                        cHour = 24;
                        cHour = cHour - 1;
                        cMinute = 60;
                        cMinute = cMinute - 1;
                        sSecond = getCikarma(cSecond + 60, oSecond);
                    } else {
                        if (cMonth != 0) {
                            cMonth = cMonth - 1;
                            localCalendar.set(cYear, cMonth - 1, cDay);
                            cDay = localCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                            cDay = cDay - 1;
                            cHour = 24;
                            cHour = cHour - 1;
                            cMinute = 60;
                            cMinute = cMinute - 1;
                            sSecond = getCikarma(cSecond + 60, oSecond);
                        } else {
                            if (cYear != 0) {
                                cYear = cYear - 1;
                                cMonth = 12;
                                cMonth = cMonth - 1;
                                localCalendar.set(cYear, 10, cDay); // bir yıl 12 ay aktarıyor Calendar ayları 0 dan başlıyor yani 10 Kasıma denk geliyor oda kaç çekerse
                                cDay = localCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                                cDay = cDay - 1;
                                cHour = 24;
                                cHour = cHour - 1;
                                cMinute = 60;
                                cMinute = cMinute - 1;
                                sSecond = getCikarma(cSecond + 60, oSecond);
                            }
                        }
                    }
                }
            }
        }

        // dakika hesaplama
        if (cikarma(cMinute, oMinute)) {
            sMinute = getCikarma(cMinute, oMinute);
        } else {
            if (cHour != 0) {
                cHour = cHour - 1;
                sMinute = getCikarma(cMinute + 60, oMinute);
            } else {
                if (cDay != 0) {
                    cDay = cDay - 1;
                    cHour = 24;
                    cHour = cHour - 1;
                    sMinute = getCikarma(cMinute + 60, oMinute);
                } else {
                    if (cMonth != 0) {
                        cMonth = cMonth - 1;
                        localCalendar.set(cYear, cMonth - 1, cDay);
                        cDay = localCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                        cDay = cDay - 1;
                        cHour = 24;
                        cHour = cHour - 1;
                        sMinute = getCikarma(cMinute + 60, oMinute);
                    } else {
                        if (cYear != 0) {
                            cYear = cYear - 1;
                            cMonth = 12;
                            cMonth = cMonth - 1;
                            localCalendar.set(cYear, 10, cDay); // bir yıl 12 ay aktarıyor Calendar ayları 0 dan başlıyor yani 10 Kasıma denk geliyor oda kaç çekerse
                            cDay = localCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                            cDay = cDay - 1;
                            cHour = 24;
                            cHour = cHour - 1;
                            sMinute = getCikarma(cMinute + 60, oMinute);
                        }
                    }
                }
            }
        }

        // saat hesaplama
        if (cikarma(cHour, oHour)) {
            sHour = getCikarma(cHour, oHour);
        } else {
            if (cDay != 0) {
                cDay = cDay - 1;
                sHour = getCikarma(cHour + 24, oHour);
            } else {
                if (cMonth != 0) {
                    cMonth = cMonth - 1;
                    localCalendar.set(cYear, cMonth - 1, cDay);
                    cDay = localCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                    cDay = cDay - 1;
                    sHour = getCikarma(cHour + 24, oHour);
                } else {
                    if (cYear != 0) {
                        cYear = cYear - 1;
                        cMonth = 12;
                        cMonth = cMonth - 1;
                        localCalendar.set(cYear, 10, cDay); // bir yıl 12 ay aktarıyor Calendar ayları 0 dan başlıyor yani 10 Kasıma denk geliyor oda kaç çekerse
                        cDay = localCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                        cDay = cDay - 1;
                        sHour = getCikarma(cHour + 24, oHour);
                    }
                }
            }
        }

        // gün hesaplama
        if (cikarma(cDay, oDay)) {
            sDay = getCikarma(cDay, oDay);
        } else {
            if (cMonth != 0) {
                cMonth = cMonth - 1;
                localCalendar.set(cYear, cMonth - 1, cDay);
                int currentDayOfMonth = localCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                sDay = getCikarma(cDay + currentDayOfMonth, oDay);
            } else {
                if (cYear != 0) {
                    cYear = cYear - 1;
                    cMonth = 12;
                    cMonth = cMonth - 1;
                    localCalendar.set(cYear, 10, cDay); // bir yıl 12 ay aktarıyor Calendar ayları 0 dan başlıyor yani 10 Kasıma denk geliyor oda kaç çekerse
                    int currentDayOfMonth = localCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                    sDay = getCikarma(cDay + currentDayOfMonth, oDay);
                }
            }
        }

        // ay hesaplama
        if (cikarma(cMonth, oMonth)) {
            sMonth = getCikarma(cMonth, oMonth);
        } else {
            if (cYear != 0) {
                cYear = cYear - 1;
                sMonth = getCikarma(cMonth + 12, oMonth);
            }
        }

        //yıl hesaplama
        if (cikarma(cYear, oYear)) {
            sYear = getCikarma(cYear, oYear);
        }

        //Log.e("Date format", " // " + sYear + " " + sMonth + " " + sDay + " " + sHour + " " + sMinute + " " + sSecond + "----");

        return new MyDateFormat(sDay, sMonth, sYear, sHour, sMinute, sSecond);
    }

    public MyDateFormat getDifference(long c, long o) {
        Calendar localCalendar = Calendar.getInstance();
        localCalendar.setTimeInMillis(c);

        int cDay, cMonth, cYear, cHour, cMinute, cSecond;
        int oDay, oMonth, oYear, oHour, oMinute, oSecond;
        int sDay = 0, sMonth = 0, sYear = 0, sHour = 0, sMinute = 0, sSecond = 0;
        String oDate, cDate;
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd:MM:yyyy / HH:mm:ss");

        oDate = formatter.format(o);
        cDate = formatter.format(c);

        oDay = Integer.valueOf(oDate.substring(0, 2));
        oMonth = Integer.valueOf(oDate.substring(3, 5));
        oYear = Integer.valueOf(oDate.substring(6, 10));

        oHour = Integer.valueOf(oDate.substring(13, 15));
        oMinute = Integer.valueOf(oDate.substring(16, 18));
        oSecond = Integer.valueOf(oDate.substring(19, 21));

        //--------------------------------

        cDay = Integer.valueOf(cDate.substring(0, 2));
        cMonth = Integer.valueOf(cDate.substring(3, 5));
        cYear = Integer.valueOf(cDate.substring(6, 10));

        cHour = Integer.valueOf(cDate.substring(13, 15));
        cMinute = Integer.valueOf(cDate.substring(16, 18));
        cSecond = Integer.valueOf(cDate.substring(19, 21));

        // saniye hesaplama
        if (cikarma(cSecond, oSecond)) {
            sSecond = getCikarma(cSecond, oSecond);
        } else {
            if (cMinute != 0) {
                cMinute = cMinute - 1;
                sSecond = getCikarma(cSecond + 60, oSecond);
            } else {
                if (cHour != 0) {
                    cHour = cHour - 1;
                    cMinute = 60;
                    cMinute = cMinute - 1;
                    sSecond = getCikarma(cSecond + 60, oSecond);
                } else {
                    if (cDay != 0) {
                        cDay = cDay - 1;
                        cHour = 24;
                        cHour = cHour - 1;
                        cMinute = 60;
                        cMinute = cMinute - 1;
                        sSecond = getCikarma(cSecond + 60, oSecond);
                    } else {
                        if (cMonth != 0) {
                            cMonth = cMonth - 1;
                            localCalendar.set(cYear, cMonth - 1, cDay);
                            cDay = localCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                            cDay = cDay - 1;
                            cHour = 24;
                            cHour = cHour - 1;
                            cMinute = 60;
                            cMinute = cMinute - 1;
                            sSecond = getCikarma(cSecond + 60, oSecond);
                        } else {
                            if (cYear != 0) {
                                cYear = cYear - 1;
                                cMonth = 12;
                                cMonth = cMonth - 1;
                                localCalendar.set(cYear, 10, cDay); // bir yıl 12 ay aktarıyor Calendar ayları 0 dan başlıyor yani 10 Kasıma denk geliyor oda kaç çekerse
                                cDay = localCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                                cDay = cDay - 1;
                                cHour = 24;
                                cHour = cHour - 1;
                                cMinute = 60;
                                cMinute = cMinute - 1;
                                sSecond = getCikarma(cSecond + 60, oSecond);
                            }
                        }
                    }
                }
            }
        }

        // dakika hesaplama
        if (cikarma(cMinute, oMinute)) {
            sMinute = getCikarma(cMinute, oMinute);
        } else {
            if (cHour != 0) {
                cHour = cHour - 1;
                sMinute = getCikarma(cMinute + 60, oMinute);
            } else {
                if (cDay != 0) {
                    cDay = cDay - 1;
                    cHour = 24;
                    cHour = cHour - 1;
                    sMinute = getCikarma(cMinute + 60, oMinute);
                } else {
                    if (cMonth != 0) {
                        cMonth = cMonth - 1;
                        localCalendar.set(cYear, cMonth - 1, cDay);
                        cDay = localCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                        cDay = cDay - 1;
                        cHour = 24;
                        cHour = cHour - 1;
                        sMinute = getCikarma(cMinute + 60, oMinute);
                    } else {
                        if (cYear != 0) {
                            cYear = cYear - 1;
                            cMonth = 12;
                            cMonth = cMonth - 1;
                            localCalendar.set(cYear, 10, cDay); // bir yıl 12 ay aktarıyor Calendar ayları 0 dan başlıyor yani 10 Kasıma denk geliyor oda kaç çekerse
                            cDay = localCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                            cDay = cDay - 1;
                            cHour = 24;
                            cHour = cHour - 1;
                            sMinute = getCikarma(cMinute + 60, oMinute);
                        }
                    }
                }
            }
        }

        // saat hesaplama
        if (cikarma(cHour, oHour)) {
            sHour = getCikarma(cHour, oHour);
        } else {
            if (cDay != 0) {
                cDay = cDay - 1;
                sHour = getCikarma(cHour + 24, oHour);
            } else {
                if (cMonth != 0) {
                    cMonth = cMonth - 1;
                    localCalendar.set(cYear, cMonth - 1, cDay);
                    cDay = localCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                    cDay = cDay - 1;
                    sHour = getCikarma(cHour + 24, oHour);
                } else {
                    if (cYear != 0) {
                        cYear = cYear - 1;
                        cMonth = 12;
                        cMonth = cMonth - 1;
                        localCalendar.set(cYear, 10, cDay); // bir yıl 12 ay aktarıyor Calendar ayları 0 dan başlıyor yani 10 Kasıma denk geliyor oda kaç çekerse
                        cDay = localCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                        cDay = cDay - 1;
                        sHour = getCikarma(cHour + 24, oHour);
                    }
                }
            }
        }

        // gün hesaplama
        if (cikarma(cDay, oDay)) {
            sDay = getCikarma(cDay, oDay);
        } else {
            if (cMonth != 0) {
                cMonth = cMonth - 1;
                localCalendar.set(cYear, cMonth - 1, cDay);
                int currentDayOfMonth = localCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                sDay = getCikarma(cDay + currentDayOfMonth, oDay);
            } else {
                if (cYear != 0) {
                    cYear = cYear - 1;
                    cMonth = 12;
                    cMonth = cMonth - 1;
                    localCalendar.set(cYear, 10, cDay); // bir yıl 12 ay aktarıyor Calendar ayları 0 dan başlıyor yani 10 Kasıma denk geliyor oda kaç çekerse
                    int currentDayOfMonth = localCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                    sDay = getCikarma(cDay + currentDayOfMonth, oDay);
                }
            }
        }

        // ay hesaplama
        if (cikarma(cMonth, oMonth)) {
            sMonth = getCikarma(cMonth, oMonth);
        } else {
            if (cYear != 0) {
                cYear = cYear - 1;
                sMonth = getCikarma(cMonth + 12, oMonth);
            }
        }

        //yıl hesaplama
        if (cikarma(cYear, oYear)) {
            sYear = getCikarma(cYear, oYear);
        }

        //Log.e("Date format", sDay + ":" + sMonth + ":" + sYear + " / " + sHour + ":" + sMinute + ":" + sSecond);

        return new MyDateFormat(sDay, sMonth, sYear, sHour, sMinute, sSecond);
    }

    private boolean cikarma(int c, int o) {

        int s = c - o;
        return s >= 0;
    }

    private int getCikarma(int c, int o) {

        return c - o;
    }

    public String getStringFormat(MyDateFormat myDateFormat) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(myDateFormat.getsYear(), myDateFormat.getsMonth(), myDateFormat.getsDay(), myDateFormat.getsHour(), myDateFormat.getsMinute(), myDateFormat.getsSecond());
        Date date = calendar.getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");

        String gun;
        if (String.valueOf(myDateFormat.getsDay()).length() == 1) {
            gun = "0" + myDateFormat.getsDay();
        } else {
            gun = "" + myDateFormat.getsDay();
        }

        if (myDateFormat.getsDay() <= 0) {
            gun = "00";
        }

        return gun + " gün " + formatter.format(date) + " saniye kaldı.";

    }
}
