package com.MyNotePlaceApp.app;

import com.google.android.gms.maps.model.LatLng;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Note {
    private String userID;
    private String noteID;
    private String noteTitle;
    private String noteContent;
    private double noteLatitudeLocation;
    private double noteLongtitudeLocation;

    private long date;

    public Note(){}

    public Note(String noteTitle, String noteContent, String userID)
    {
        this.noteTitle = noteTitle;
        this.noteContent = noteContent;
        this.userID = userID;
        this.noteID = null;
        this.noteLatitudeLocation = 0;
        this.noteLongtitudeLocation = 0;
        this.date = new Date().getTime();
    }


    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getNoteID() {
        return noteID;
    }

    public void setNoteID(String noteID) {
        this.noteID = noteID;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }

    public double getNoteLatitudeLocation() {
        return noteLatitudeLocation;
    }

    public void setNoteLatitudeLocation(double noteLatitudeLocation) {
        this.noteLatitudeLocation = noteLatitudeLocation;
    }

    public double getNoteLongtitudeLocation() {
        return noteLongtitudeLocation;
    }

    public void setNoteLongtitudeLocation(double noteLongtitudeLocation) {
        this.noteLongtitudeLocation = noteLongtitudeLocation;
    }

    @Override
    public String toString() {
        String timeStamp = new SimpleDateFormat("dd.MM.yyyy , HH:mm:ss").format(new Timestamp(getDate()));

        return noteTitle +"  " +timeStamp;
    }

}
