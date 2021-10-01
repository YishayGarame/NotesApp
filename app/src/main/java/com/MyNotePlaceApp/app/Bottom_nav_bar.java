package com.MyNotePlaceApp.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Bottom_nav_bar extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    private Button logout;
    private FloatingActionButton floatingAddBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_nav_bar);


        BottomNavigationView bottonNav = findViewById(R.id.bottom_navigation_bar);
        bottonNav.setOnNavigationItemReselectedListener(navListener);


        //floating button
        floatingAddBtn = findViewById(R.id.floatingAddButton);
        floatingAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragmentNote myDialogFragment = new DialogFragmentNote();
                myDialogFragment.show(getSupportFragmentManager(),"MyFragment");
            }
        });

        //logout button
        logout = (Button)findViewById(R.id.buttonLogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Bottom_nav_bar.this, MainActivity.class));
            }
        });

        //firebase
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        //greeting
        final TextView greetingTextView = (TextView)findViewById(R.id.greeting);
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if(userProfile != null)
                {
                    String fullName = userProfile.fullName;
                    greetingTextView.setText("Welcome, "+fullName+ "!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Bottom_nav_bar.this, "something wrong happend!", Toast.LENGTH_LONG).show();
            }
        });
        //default List mode fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ListFragment()).commit();
    }


    private BottomNavigationView.OnNavigationItemReselectedListener navListener =
            new BottomNavigationView.OnNavigationItemReselectedListener(){

                @Override
                public void onNavigationItemReselected(@NonNull MenuItem item) {
                    Fragment selectedFragment =null;
                    switch (item.getItemId()){
                        case R.id.nav_list:
                            selectedFragment = new ListFragment();
                            break;
                        case R.id.nav_map:
                            selectedFragment = new MapFragment();
                            break;
//                        case R.id.nav_add:
//                            selectedFragment = new AddFragment();
//                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                }
            };

}