package com.MyNotePlaceApp.app;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ListFragment extends Fragment {

    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    FirebaseUser fbUser ;
    String userId;

    @Nullable
    @Override
    public View onCreateView( LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();
        fbUser = mAuth.getCurrentUser();
        userId = fbUser.getUid();
        //reference = FirebaseDatabase.getInstance().getReference("Notes").child(userId);
        reference = FirebaseDatabase.getInstance().getReference("Notes");


        ArrayList<Note> noteArrayList = new ArrayList<>();

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Note> noteList= new ArrayList<>();

                Note note = new Note();
                HashMap<String, Note> mapNoteList =  new HashMap<>();
                GenericTypeIndicator<HashMap<String, Note>> t = new GenericTypeIndicator<HashMap<String, Note>>() {
                };

                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    if(ds.exists()) {
                        mapNoteList = ds.child(userId).getValue(t);
                        Log.d("TAGggggggggggggg", mapNoteList.toString());

//                          note.setNoteID(ds.child(userId).getValue(Note.class).getNoteID());
//                        note.setDate(ds.child(mAuth.getUid()).getValue(Note.class).getDate());
//                        note.setNoteContent(ds.child(mAuth.getUid()).getValue(Note.class).getNoteContent());
//                        note.setNoteLocation(ds.child(mAuth.getUid()).getValue(Note.class).getNoteLocation());
//                        note.setNoteTitle(ds.child(mAuth.getUid()).getValue(Note.class).getNoteTitle());
//                        note.setUserID(ds.child(mAuth.getUid()).getValue(Note.class).getUserID());
//                        noteList.add(note);
//                        mapNoteList = (HashMap)ds.getValue();
//                        Log.d("TAGggggggggggggg", mapNoteList.toString());
//                        Log.d("noteee", mapNoteList.get(Note.class).getNoteContent());
////                        Log.d("TAGggggggggggggg", mapNoteList.getDate());
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        };
        reference.addListenerForSingleValueEvent(valueEventListener);
        return inflater.inflate(R.layout.fragment_list,container,false);


    }
}
