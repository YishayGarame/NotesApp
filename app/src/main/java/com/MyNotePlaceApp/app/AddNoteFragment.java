package com.MyNotePlaceApp.app;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
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
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddNoteFragment extends DialogFragment {

    private FirebaseDatabase rootNode;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;

    private Button cancelNote;
    private Button saveNote;

    private EditText editTextTitleText, editTextContentText, editTextLongtitude, editTextLatitude;

    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;
    GoogleMap map;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
         super.onCreateView(inflater, container, savedInstanceState);

         View view = inflater.inflate(R.layout.fragment_add_note, container,false );


        editTextTitleText = (EditText) view.findViewById(R.id.titleText);
        editTextContentText = (EditText) view.findViewById(R.id.contentText);
        editTextLongtitude = (EditText) view.findViewById(R.id.longtitudeText);
        editTextLatitude = (EditText) view.findViewById(R.id.latitudeText);

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
        String latitude,longtitude;

        titleNote = editTextTitleText.getText().toString().trim();
        contentNote = editTextContentText.getText().toString().trim();
        latitude = editTextLatitude.getText().toString().trim();
        longtitude = editTextLongtitude.getText().toString().trim();

        userID = mAuth.getUid();

        Note note = new Note(titleNote,contentNote,userID);
        note.setNoteLatitudeLocation(Double.parseDouble(latitude));
        note.setNoteLongtitudeLocation(Double.parseDouble(longtitude));

        String noteId = reference.child(mAuth.getCurrentUser().getUid()).push().getKey();
        note.setNoteID(noteId);
        reference.child(mAuth.getCurrentUser().getUid()).child(noteId).setValue(note);

    }

}
