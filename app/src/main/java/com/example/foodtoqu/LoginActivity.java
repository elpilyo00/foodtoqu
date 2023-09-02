package com.example.foodtoqu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    AppCompatButton registerBtn, loginBtn;
    ImageView image;
    TextView slogan1, loginTxt;
    TextInputLayout username, password;

    private Boolean validateUsername() {
        String val = username.getEditText().getText().toString();
        if (val.isEmpty()) {
            username.setError("Field cannot be empty");
            return false;
        } else {
            username.setError(null);
            return true;
        }
    }

    private Boolean validatePassword() {
        String val = password.getEditText().getText().toString();
        if (val.isEmpty()) {
            password.setError("Field cannot be empty");
            return false;
        } else {
            password.setError(null);
            return true;
        }
    }

    private void isUser() {

        final String userEnteredUsername = username.getEditText().getText().toString().trim();
        final String userEnteredPassword = password.getEditText().getText().toString().trim();
        final String adminUser = username.getEditText().getText().toString().trim();
        final String adminPassword = password.getEditText().getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
        DatabaseReference adminReference = FirebaseDatabase.getInstance().getReference("Admin");


        Query checkUser = reference.orderByChild("username").equalTo(userEnteredUsername);
        Query checkAdmin = adminReference.orderByChild("adminUser").equalTo(adminUser);


        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    username.setError(null);
                    String passwordFromDB = snapshot.child(userEnteredUsername).child("password").getValue(String.class);

                    if (!Objects.equals(passwordFromDB, userEnteredPassword)) {
                        username.setError(null);
                        Intent intent = new Intent(LoginActivity.this, UserActivity.class);
                        startActivity(intent);
                    }
                    else {
                        password.setError("Wrong Password");
                        password.requestFocus();
                    }

                    }
                else {
                    username.setError("No such User exist");
                    username.requestFocus();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the onCancelled event if needed
            }
        });


        checkAdmin.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (adminUser.equals("adminUser")) {

                    username.setError(null);
                    username.setErrorEnabled(false);


                    if (adminPassword.equals("adminPassword")) {

                        username.setError(null);
                        username.setErrorEnabled(false);

                        Intent intent = new Intent(getApplicationContext(), AdminActivity.class);

                        startActivity(intent);
                    } else {
                        password.setError("Wrong Password");
                        password.requestFocus();
                    }
                } else {
                    username.setError("User doesn't exist");
                    username.requestFocus();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }



        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        registerBtn = findViewById(R.id.registerBtn);
        loginBtn = findViewById(R.id.loginBtn);
        image = findViewById(R.id.logoImg);
        slogan1 = findViewById(R.id.logoName);
        loginTxt = findViewById(R.id.login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);


            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                if (!validateUsername() || !validatePassword()){
                }

                else{
                    isUser();
                }


                }
            });



        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);

                Pair[] pairs=new Pair[7];
                pairs[0] = new Pair<View, String>(image, "logo_image");
                pairs[1] = new Pair<View, String>(slogan1, "slogan1");
                pairs[2] = new Pair<View, String>(loginTxt, "loginTxt");
                pairs[3] = new Pair<View, String>(username, "username");
                pairs[4] = new Pair<View, String>(password, "password");
                pairs[5] = new Pair<View, String>(loginBtn, "login_trans");
                pairs[6] = new Pair<View, String>(registerBtn, "register_trans");


                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this,pairs);
                startActivity(intent, options.toBundle());
            }
        });



    }
}