package com.example.android.newgames;

import android.support.annotation.NonNull;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by rymcg on 21/04/2018.
 */

public class Game implements Comparable<Game> {
    private String mtitle;
    private String mdate;
    private String mExpectedDate;
    private Date dateTime;
    private long mills;
    private int noDate;
    private boolean header;
    private String yearFilter;



    public Game(String title, String date,String day, String month, String year,String yearFilter){
        header = false;
        mtitle = title;
        mdate = date.replaceAll("00:00:00","");
        noDate = 0;
        if(mdate.equals("null")){
            if(month.equals("null")){
                mdate = "null";
            }
            else if(day.equals("null")) {
                if(Integer.parseInt(month)<10){
                    month = "0"+month;
                }
                mdate = year + "-" + month + "-"+ 01;
            }
            else {
                if(Integer.parseInt(month)<10){
                    month = "0"+month;
                }
                if(Integer.parseInt(day)<10){
                    day = "0"+day;
                }
                mdate =  year + "-" + month +"-"+day;
            }
        }
        if(mdate.equals("null")){
            mdate = yearFilter+"-31-12";
            noDate = 1;
        }
        mdate = mdate.replaceAll("00:00:00","");

        //Log.e("Is there a Date", mdate);
        setDateTime();
    }

    public Game(String monthString,String date){
        header = true;
        mtitle = monthString;
        mdate = date;
        noDate = 1;
        setDateTime();

    }


    public Date getDateTime() {
        return dateTime;
    }
    public boolean isHeader(){
        return header;
    }

    public void setDateTime() {
        String givenDateString = mdate;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            dateTime = sdf.parse(givenDateString);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getTitle(){return mtitle;}
    public String getDate(){return mdate;}
    public String getExpectedDate(){return mExpectedDate;}
    public int getNoDate(){return noDate;}

    @Override
    public int compareTo(@NonNull Game o) {
        return getDateTime().compareTo(o.getDateTime());
    }
}
