package com.MyNotePlaceApp.app;


import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
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
    FirebaseUser fbUser;
    String userId;

    ArrayList<Note> noteArrayList;
    ListView listView;
    ArrayAdapter<Note> adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.fragment_list, container, false);

        listView = view.findViewById(R.id.listView);

        adapter = new ArrayAdapter<>(getContext(),R.layout.list_item);

        mAuth = FirebaseAuth.getInstance();
        fbUser = mAuth.getCurrentUser();
        userId = fbUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference("Notes").child(userId);


        loadNotes();

       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               //holding note selected by user
               Note noteSelected = noteArrayList.get(position);
               Toast.makeText(getContext(), "Note selected is:"+ noteSelected.getNoteTitle(), Toast.LENGTH_SHORT).show();
               DialogFragment editFragment = new EditNoteFragment(noteSelected);
               editFragment.show(getParentFragmentManager(),"Edit Note");

               loadNotes();
               adapter.notifyDataSetChanged();

           }
       });

       //delete note when long press
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Note noteSelected = noteArrayList.get(position);
                deleteNote(noteSelected);

                return true;
            }
        });


        return view;


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
                    }
                }
                adapter.clear();
                //sort
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    noteArrayList.sort(new NoteComparator());
                }

                adapter.addAll(noteArrayList);
                listView.setAdapter(adapter);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

    private  void deleteNote(Note noteToDelete){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Delete "+noteToDelete.getNoteTitle());
        builder.setMessage("Are you sure?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                reference.child(noteToDelete.getNoteID()).removeValue();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();

    }
}
