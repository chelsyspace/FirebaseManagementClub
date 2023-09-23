package com.example.firebaseclubmanagement_a187991;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.firebaseclubmanagement_a187991.Objects.Club;
import com.example.firebaseclubmanagement_a187991.Objects.Student;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MemberRegistrationActivity extends AppCompatActivity {

    EditText etName, etMatric, etEmail;
    Spinner spinnerClubs;
    ListView lvMembers;
    ToggleButton toggleBtnStatus;
    Button btnAdd, btnUpdate;

    //list to store the club and member information from firebase
    List<Club> clubs;
    List<Student> students;

    //Firebase database reference
    DatabaseReference databaseClubs;
    DatabaseReference databaseMember;
    private ArrayAdapter<String> lvAdapter;
    private ArrayAdapter<String> spinnerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_registration);

        etName = (EditText) findViewById(R.id.member_registration_et_name);
        etMatric = (EditText) findViewById(R.id.member_registration_et_matric);
        etEmail = (EditText) findViewById(R.id.member_registration_et_email);

        toggleBtnStatus = (ToggleButton) findViewById(R.id.member_registration_toggle_btn_status);
        btnAdd = (Button) findViewById(R.id.member_registration_btn_add);
        btnUpdate = (Button) findViewById(R.id.member_registration_btn_update);

        spinnerClubs = (Spinner) findViewById(R.id.member_registration_spinner_club);
        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        spinnerClubs.setAdapter(spinnerAdapter);

        lvMembers = (ListView) findViewById(R.id.member_registration_lv_member);
        lvAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,android.R.id.text1);
        lvMembers.setAdapter(lvAdapter);

        //getting the reference of firebase clubs nad members node
        databaseClubs = FirebaseDatabase.getInstance().getReference("clubs");
        databaseMember = FirebaseDatabase.getInstance().getReference("members");

        clubs = new ArrayList<>();
        students = new ArrayList<>();

        databaseClubs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                clubs.clear();
                spinnerAdapter.clear();

                for (DataSnapshot clubDataSnapshot : dataSnapshot.getChildren()) {
                    Club club = clubDataSnapshot.getValue(Club.class);
                    clubs.add(club);
                    spinnerAdapter.add(club.getClubCode() + "\n" + club.getClubName());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseMember.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                students.clear();
                lvAdapter.clear();
                for (DataSnapshot memberDataSnapshot:dataSnapshot.getChildren())
                {
                   Student student = memberDataSnapshot.getValue(Student.class);

                   students.add(student);
                   lvAdapter.add(student.getStudentClub()
                           + "\n" + student.getStudentMatricNo() + " " + student.getStudentName()
                           + "\n" + student.getStudentEmail());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMember();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString().trim();
                String matric = etMatric.getText().toString().trim();
                String email = etEmail.getText().toString().trim();

                String club = clubs.get(spinnerClubs.getSelectedItemPosition()).getClubName();
                Boolean status = toggleBtnStatus.isChecked();

                if(!TextUtils.isEmpty(name)&&!TextUtils.isEmpty(matric)&&!TextUtils.isEmpty(email)){
                    updateStudent(matric,name,email,club,status);
                }
            }
        });


        lvMembers.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long id) {
                final Student student = students.get(i);

                AlertDialog.Builder alert = new AlertDialog.Builder(MemberRegistrationActivity.this);
                alert.setTitle("Delete Student");
                alert.setMessage("Are you sure you want to delete it?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("members").child(student.getStudentID());
                        dR.removeValue();
                        Toast.makeText(getApplicationContext(), "Member Deleted", Toast.LENGTH_SHORT).show();
                    }
                });

                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                alert.show();

                return true;
            }
        });
    }

    private void addMember(){
        String name = etName.getText().toString().trim();
        String matric = etMatric.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String club = clubs.get(spinnerClubs.getSelectedItemPosition()).getClubName();
        Boolean status = toggleBtnStatus.isChecked();

        if(!TextUtils.isEmpty(name)&&!TextUtils.isEmpty(matric)&&!TextUtils.isEmpty(email)){
            String id = databaseMember.push().getKey();

            Student newStudent = new Student(id,matric,name,email,club,status);

            databaseMember.child(id).setValue(newStudent);

            etName.setText("");
            etMatric.setText("");
            etEmail.setText("");

            Toast.makeText(MemberRegistrationActivity.this, "New member added", Toast.LENGTH_SHORT).show();

        }

    }

    private void updateStudent(String matric, String name, String email, String club, Boolean status){

        DatabaseReference dR = FirebaseDatabase.getInstance().getReference();

        //select all from members where matricNo = matric
        dR.child("members").orderByChild("studentMatricNo").equalTo(matric).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()){
                    DataSnapshot firstChild = dataSnapshot.getChildren().iterator().next();

                    firstChild.getRef().child("studentMatricNo").setValue(matric);
                    firstChild.getRef().child("studentName").setValue(name);
                    firstChild.getRef().child("studentEmail").setValue(email);
                    firstChild.getRef().child("studentClub").setValue(club);
                    firstChild.getRef().child("studentClubActiveStatus").setValue(status);

                    Toast.makeText(getApplicationContext(), "Member Updated", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


}