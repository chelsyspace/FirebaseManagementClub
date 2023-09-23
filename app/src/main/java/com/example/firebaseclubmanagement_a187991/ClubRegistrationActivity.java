package com.example.firebaseclubmanagement_a187991;

import android.os.Bundle;

import com.example.firebaseclubmanagement_a187991.Objects.Club;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
//import android.widget.Toolbar;
import androidx.appcompat.widget.Toolbar;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.firebaseclubmanagement_a187991.databinding.ActivityClubRegistrationBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ClubRegistrationActivity extends AppCompatActivity {

    //Declare the views
    EditText etClubName, etClubCode;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    CoordinatorLayout coordinatorLayout;

    //a list to store all the club from firebase database
    List<Club> clubs;

    DatabaseReference databaseClubs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_registration);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        etClubName = findViewById(R.id.club_registration_et_name);
        etClubCode = findViewById(R.id.club_registration_et_code);
        listView = findViewById(R.id.club_registration_list_view);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.club_registration_coordinator_layout);

        clubs = new ArrayList<>();

        loadListView();

        databaseClubs= FirebaseDatabase.getInstance().getReference("clubs");

        databaseClubs.addValueEventListener(new ValueEventListener() {
          //  clubs.clear();
            //adapter.clear();
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot clubDataSnapshot:dataSnapshot.getChildren())
                {
                    //getting club information
                    Club club = clubDataSnapshot.getValue(Club.class);

                    //adding club to the list
                    clubs.add(club);
                    adapter.add(club.getClubCode() + "\n" + club.getClubName());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.club_registration_fab);
        fab.setOnClickListener((view -> {
            addClub();
            loadListView();
        }));
    }

    private void addClub(){
        String name = etClubName.getText().toString().trim();
        String code = etClubCode.getText().toString().trim();

        if (!TextUtils.isEmpty(name)||!TextUtils.isEmpty(code)){
            //getting a unique id using push().getText() method
            //it will create  unique id and we will use it as the Primary Key for our club

            String id = databaseClubs.push().getKey();
            //create the club object
            Club newClub = new Club(id,code,name);
            //save club information into the firebase
            databaseClubs.child(id).setValue(newClub);

            etClubCode.setText("");
            etClubName.setText("");

            Snackbar.make(coordinatorLayout,"Club is added", Snackbar.LENGTH_LONG).show();

        }

    }


    private void loadListView(){
        listView = (ListView) findViewById(R.id.club_registration_list_view);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,android.R.id.text1);
        listView.setAdapter(adapter);
    }
}