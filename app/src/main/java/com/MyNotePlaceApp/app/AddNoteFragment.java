package com.MyNotePlaceApp.app;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddNoteFragment extends DialogFragment {

    private FirebaseDatabase rootNode;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;

    private Button cancelNote;
    private Button saveNote;

    private EditText editTextTitleText, editTextContentText;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);

         View view = inflater.inflate(R.layout.fragment_add_note, container,false );

        editTextTitleText = (EditText) view.findViewById(R.id.titleText);
        editTextContentText = (EditText) view.findViewById(R.id.contentText);

         cancelNote = view.findViewById(R.id.cancelNoteButton);
         saveNote = view.findViewById(R.id.saveNoteButton);

         cancelNote.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Toast.makeText(getActivity(), "Note Canceled :(",Toast.LENGTH_LONG).show();
                 getDialog().dismiss();
             }
         });
         saveNote.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 mAuth = FirebaseAuth.getInstance();
                 reference = FirebaseDatabase.getInstance().getReference("Notes");
                 rootNode = FirebaseDatabase.getInstance();
                 Log.d("MAuth","user id is :"+ mAuth.getUid());

                 saveNote();
                 Toast.makeText(getActivity(), "Note Saved :)",Toast.LENGTH_LONG).show();
                 getDialog().dismiss();
             }
         });

        return view;
    }
    private void saveNote() {
        String titleNote, contentNote, userID;
        titleNote = editTextTitleText.getText().toString().trim();
        contentNote = editTextContentText.getText().toString().trim();
        userID = mAuth.getUid();

        Note note = new Note(titleNote,contentNote,userID);

        String noteId = reference.child(mAuth.getCurrentUser().getUid()).push().getKey();
        note.setNoteID(noteId);
        reference.child(mAuth.getCurrentUser().getUid()).child(noteId).setValue(note);

    }
}
