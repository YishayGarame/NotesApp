package com.MyNotePlaceApp.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter {

    TextView noteTitle;
    TextView date;

    public  ListAdapter(Context context, ArrayList<Note> noteArrayList){
        super(context,R.layout.list_item, noteArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Note note = (Note)getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }

        noteTitle = convertView.findViewById(R.id.noteTitle);
        date = convertView.findViewById(R.id.timeAndDate);


        noteTitle.setText(note.getNoteTitle());
        date.setText(note.getDate().toString());

        return super.getView(position, convertView, parent);
    }
}


