package com.MyNotePlaceApp.app;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {


    //firebase variables
    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    FirebaseUser fbUser;
    String userId;

    private Marker selectedMarker;


    //arraylist
    ArrayList<Note> noteArrayList;
    FusedLocationProviderClient fusedLocationProviderClient;

    LatLng latLng;

    GoogleMap map;
    @Nullable
    @Override
    public View onCreateView( LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map,container,false);

        //init fire base variables
        mAuth = FirebaseAuth.getInstance();
        fbUser = mAuth.getCurrentUser();
        userId = fbUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference("Notes").child(userId);


        loadNotes();

        //init fusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        SupportMapFragment mapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map));
        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.map = googleMap;
    }

    private void loadNotes() {

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                noteArrayList = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if(ds.exists()) {
                        Note note = ds.getValue(Note.class);
                        noteArrayList.add(note);
                        addMarkToMap(note);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void addMarkToMap(Note note){
        LatLng latLng = new LatLng(note.getNoteLatitudeLocation(),note.getNoteLongtitudeLocation());
        map.addMarker(new MarkerOptions().position(latLng).title(note.getNoteTitle()));
        float zoomLevel = 16.0f; //This goes up to 21
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoomLevel));
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                DialogFragment editFragment = new EditNoteFragment(note);
                editFragment.show(getParentFragmentManager(),"Edit Note");
                return false;
            }
        });
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        return false;
    }
}
