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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;

public class RegisterActivity extends AppCompatActivity {
    public void checkButton(View v){
        int radioId = radioGroup.getCheckedRadioButtonId();

        radioButton = findViewById(radioId);
    }

    AppCompatButton registerBtn;
    Button loginBtn;
    ImageView image;
    TextView slogan1, loginTxt;
    TextInputLayout username, password, name, age;
    RadioButton male, female, radioButton;
    RadioGroup radioGroup;

    int counter = 0;

    UserHelperClass userHelperClass;

    FirebaseDatabase database;
    DatabaseReference reference;



    private Boolean validateName(){
        String val = name.getEditText().getText().toString();

        if(val.isEmpty()){
            name.setError("Field cannot be empty");
            return false;
        }
        else{
            name.setError(null);
            name.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateUsername(){
        String val = username.getEditText().getText().toString();

        if(val.isEmpty()){
            username.setError("Field cannot be empty");
            return false;
        }
        else if (val.length()>=15) {
            username.setError("Username too long");
            return false;
        }
        else{
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateAge(){
        String val = age.getEditText().getText().toString();

        if(val.isEmpty()){
            age.setError("Field cannot be empty");
            return false;
        }
        else{
            age.setError(null);
            age.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword(){
        String val = password.getEditText().getText().toString();
        String passwordVal = "^" + "(?=.*[a-zA-Z])" + "(?=.*[@#$%^+=])" + "(?=.*[0-9])" + ".{4,}" + "$";

        if(val.isEmpty()){
            password.setError("Field cannot be empty");
            return false;
        }
        else if(!val.matches(passwordVal)){
            password.setError("Password is to weak");
            return false;
        }
        else{
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        registerBtn = findViewById(R.id.registerBtn);
        loginBtn = findViewById(R.id.loginBtn);
        image = findViewById(R.id.logoImg);
        slogan1 = findViewById(R.id.logoName);
        loginTxt = findViewById(R.id.login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        name = findViewById(R.id.name);
        age = findViewById(R.id.age);
        radioGroup = findViewById(R.id.radioGroup);

        userHelperClass = new UserHelperClass();


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);

                Pair[] pairs=new Pair[7];
                pairs[0] = new Pair<View, String>(image, "logo_image");
                pairs[1] = new Pair<View, String>(slogan1, "slogan1");
                pairs[2] = new Pair<View, String>(loginTxt, "loginTxt");
                pairs[3] = new Pair<View, String>(username, "username");
                pairs[4] = new Pair<View, String>(password, "password");
                pairs[5] = new Pair<View, String>(loginBtn, "login_trans");
                pairs[6] = new Pair<View, String>(registerBtn, "register_trans");


                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RegisterActivity.this,pairs);
                startActivity(intent, options.toBundle());
                finish();
            }
        });




        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                database = FirebaseDatabase.getInstance();
                reference = database.getReference("User");

                if(!validateName() || !validateUsername() || !validateAge() || !validatePassword()){
                    return;
                }

                int radioId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(radioId);
                String regName = name.getEditText().getText().toString();
                String regUsername = username.getEditText().getText().toString();
                String regAge = age.getEditText().getText().toString();
                String regPassword = password.getEditText().getText().toString();
                String maleGender = male.getText().toString();
                String femaleGender = female.getText().toString();

                userHelperClass.setName(regName);
                userHelperClass.setUsername(regUsername);
                userHelperClass.setAge(regAge);
                userHelperClass.setPassword(regPassword);
                reference.child(regName).setValue(userHelperClass);



                if (male.isChecked()){
                    userHelperClass.setGender(maleGender);
                    reference.child(regName).setValue(userHelperClass);
                }
                else {
                    userHelperClass.setGender(femaleGender);
                    reference.child(regName).setValue(userHelperClass);
                }


                Toast.makeText(RegisterActivity.this, "Registered Successfully!", Toast.LENGTH_SHORT).show();


                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);

                Pair[] pairs=new Pair[7];
                pairs[0] = new Pair<View, String>(image, "logo_image");
                pairs[1] = new Pair<View, String>(slogan1, "slogan1");
                pairs[2] = new Pair<View, String>(loginTxt, "loginTxt");
                pairs[3] = new Pair<View, String>(username, "username");
                pairs[4] = new Pair<View, String>(password, "password");
                pairs[5] = new Pair<View, String>(loginBtn, "login_trans");
                pairs[6] = new Pair<View, String>(registerBtn, "register_trans");


                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RegisterActivity.this,pairs);
                startActivity(intent, options.toBundle());
                finish();



            }
        });

    }
}