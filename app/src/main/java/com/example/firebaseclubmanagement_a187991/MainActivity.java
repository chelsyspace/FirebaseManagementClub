package com.example.firebaseclubmanagement_a187991;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.ktx.Firebase;

public class MainActivity extends AppCompatActivity {

    Button btnRegisterClub, btnRegisterMember;
    //reference for the firebase authetication
    private FirebaseAuth mFirebaseAuth;
    //reference for user in the firebase
    private FirebaseUser mFirebaseUser;
    private MenuItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //find the view
        btnRegisterClub = (Button) findViewById(R.id.main_btn_register_club);
        btnRegisterMember = (Button) findViewById(R.id.main_btn_register_member);

        btnRegisterClub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ClubRegistrationActivity.class);
                startActivity(intent);
            }
        });

        btnRegisterMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,MemberRegistrationActivity.class);
                startActivity(intent);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        if(mFirebaseUser==null)
        {
            //go to login page
            Intent intent = new Intent(MainActivity.this,LogInActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        Intent intent;

        if (id == R.id.action_logout) {
            mFirebaseAuth.signOut();
            intent = new Intent(MainActivity.this,LogInActivity.class);
            startActivity(intent);

        }

        else if(id == R.id.menu_report){
            Uri webpage = Uri.parse("https://forms.gle/56YA2o1DKeZTdvZf8");
            intent = new Intent(Intent.ACTION_VIEW,webpage);
            startActivity(intent);
            }

        else if(id == R.id.menu_help){
            intent = new Intent(MainActivity.this, HelpActivity.class);
            startActivity(intent);
        }
        return false;
    }

       //return super.onOptionsItemSelected(item);
}
