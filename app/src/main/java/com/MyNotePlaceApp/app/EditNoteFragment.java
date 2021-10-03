package com.MyNotePlaceApp.app;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditNoteFragment extends DialogFragment {

    private DatabaseReference reference;

    private Button cancelNote;
    private Button saveNote;

    private EditText editTextTitleText, editTextContentText,editTextLongtitude, editTextLatitude;

    private Note note;

    //for setting the location;
    String latitude,longtitude;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_edit_note, container, false);


        //init title and body
        editTextTitleText = view.findViewById(R.id.editTitleText);
        editTextContentText = view.findViewById(R.id.editContentText);

        //init cancel save button
        cancelNote = view.findViewById(R.id.editCancelNoteButton);
        saveNote = view.findViewById(R.id.editSaveNoteButton);

        //init location variables
        editTextLongtitude = (EditText) view.findViewById(R.id.editLongtitudeText);
        editTextLatitude = (EditText) view.findViewById(R.id.editLatitudeText);

        //show variables: title body and locaiton
        editTextTitleText.setText(note.getNoteTitle());
        editTextContentText.setText(note.getNoteContent());
        editTextLongtitude.setText(""+note.getNoteLongtitudeLocation());
        editTextLatitude.setText(""+note.getNoteLatitudeLocation());

        checkLocationValidation();

        cancelNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Edit note canceled !",Toast.LENGTH_LONG).show();
                getDialog().dismiss();
            }
        });
        saveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference = FirebaseDatabase.getInstance().getReference("Notes").child(note.getUserID()).child(note.getNoteID());
                String title = editTextTitleText.getText().toString();
                String content = editTextContentText.getText().toString();

                //edit note to be changed in database
                Note editedNote = new Note(title,content,note.getUserID());
                editedNote.setNoteID(note.getNoteID());

                //after validation
                editedNote.setNoteLongtitudeLocation(Double.parseDouble(longtitude));
                editedNote.setNoteLatitudeLocation(Double.parseDouble(latitude));

                note = editedNote;

                reference.setValue(editedNote);

                Toast.makeText(getActivity(), "Note Edited :)",Toast.LENGTH_LONG).show();
                getDialog().dismiss();
            }
        });

        return view;
    }
    public EditNoteFragment(Note note) {
        this.note = note;
    }

    private void checkLocationValidation(){
        double lat,lang;

        try {
            latitude = editTextLatitude.getText().toString().trim();
            longtitude = editTextLongtitude.getText().toString().trim();
            lat = Double.parseDouble(latitude);
            lang = Double.parseDouble(longtitude);
            Log.d("TAG", "ok locaiton: ");

        }catch (Exception e){
            latitude = "32.164456";
            longtitude = "34.856073";
            Log.d("TAG", "wrong location:::::::::::::::::::::::: ");
        }
        if(latitude.matches("") || longtitude.matches("")){
            latitude = "32.164456";
            longtitude = "34.856073";
        }
    }


}
