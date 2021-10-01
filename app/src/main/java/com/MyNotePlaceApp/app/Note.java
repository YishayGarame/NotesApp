package com.MyNotePlaceApp.app;

import android.util.Log;

import java.util.Date;

public class Note {
    private String userID;
    private String noteID;
    private String noteTitle;
    private String noteContent;
    private String noteLocation;
    private String date;

    public Note(){}

    public Note(String noteTitle, String noteContent, String userID)
    {
        this.noteTitle = noteTitle;
        this.noteContent = noteContent;
        this.userID = userID;
        this.noteID = null;
        this.noteLocation ="";
        this.date = new Date().toString();
        Log.d("Time is","is"+date);
    }

    public Note(String userID, String noteID, String noteTitle, String noteContent, String noteLocation, String date) {
        this.userID = userID;
        this.noteID = noteID;
        this.noteTitle = noteTitle;
        this.noteContent = noteContent;
        this.noteLocation = noteLocation;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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

    public String getNoteLocation() {
        return noteLocation;
    }

    public void setNoteLocation(String noteLocation) {
        this.noteLocation = noteLocation;
    }
}
