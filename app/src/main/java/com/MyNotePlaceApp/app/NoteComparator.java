package com.MyNotePlaceApp.app;

import java.util.Comparator;

public class NoteComparator implements Comparator<Note> {
    @Override
    public int compare(Note o1, Note o2) {
        int score = (int) (o1.getDate() - o2.getDate());
        //o1 is newer
        if (score < 0) {
            return 1;
        } else {
            return -1;
        }
    }
}

