package com.example.foodtoqu;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class UploadActivity extends AppCompatActivity {
    CheckBox diabetesCB, gastroCB, bowelCB, highBloodCB, weightCB, anemiaCB, cholesterolCB, heartCB, osteoporosisCB, celiacCB, renalCB, hypothyroidismCB;
    CheckBox happyCB, sadCB, angryCB, stressCB, excitedCB, nostalgiaCB, inLoveCB, calmCB;
    CircleImageView uploadImage;
    Button saveBtn;
    EditText uploadName, uploadCalorie, uploadFat, uploadCholesterol, uploadSodium, uploadCarbo, uploadSugar, uploadProtein;
    String imageURL;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_upload);

        uploadImage = findViewById(R.id.uploadImage);
        uploadName = findViewById(R.id.foodName);
        uploadCalorie = findViewById(R.id.calorie);
        uploadFat = findViewById(R.id.totalFat);
        uploadCholesterol = findViewById(R.id.cholesterol);
        uploadSodium = findViewById(R.id.sodium);
        uploadCarbo = findViewById(R.id.carbo);
        uploadSugar = findViewById(R.id.totalSugar);
        uploadProtein = findViewById(R.id.protein);
        saveBtn = findViewById(R.id.uploadBtn);

        //CheckBoxes
        //Health Conditions
        diabetesCB = findViewById(R.id.diabetes);
        gastroCB = findViewById(R.id.gastrointestinal);
        bowelCB = findViewById(R.id.bowel);
        highBloodCB = findViewById(R.id.highBlood);
        weightCB = findViewById(R.id.weight);
        anemiaCB = findViewById(R.id.anemia);
        cholesterolCB = findViewById(R.id.highCholesterol);
        heartCB = findViewById(R.id.heartDisease);
        osteoporosisCB = findViewById(R.id.osteoporosis);
        celiacCB = findViewById(R.id.celiac);
        renalCB = findViewById(R.id.renal);
        hypothyroidismCB = findViewById(R.id.hypothyroidism);
        //Moods
        happyCB = findViewById(R.id.happy);
        sadCB = findViewById(R.id.sad);
        angryCB = findViewById(R.id.angry);
        stressCB = findViewById(R.id.stress);
        excitedCB = findViewById(R.id.excited);
        nostalgiaCB = findViewById(R.id.nostalgia);
        inLoveCB = findViewById(R.id.inLove);
        calmCB = findViewById(R.id.calm);




        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            uri = data.getData();
                            uploadImage.setImageURI(uri);
                        }
                        else {
                            Toast.makeText(UploadActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
    }


    public void saveData(){

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Food Images").child(uri.getLastPathSegment());

        AlertDialog.Builder builder = new AlertDialog.Builder(UploadActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.dismiss();

        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while(!uriTask.isComplete());
                Uri urlImage = uriTask.getResult();
                imageURL = urlImage.toString();
                uploadData();
                dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
            }
        });
    }

    public void uploadData(){

        String foodName = uploadName.getText().toString();
        String foodCalorie = uploadCalorie.getText().toString();
        String foodFat = uploadFat.getText().toString();
        String foodCholesterol = uploadCholesterol.getText().toString();
        String foodSodium = uploadSodium.getText().toString();
        String foodCarbo = uploadCarbo.getText().toString();
        String foodSugar = uploadSugar.getText().toString();
        String foodProtein = uploadProtein.getText().toString();

        DataClass dataClass = new DataClass(foodName, foodCalorie, foodFat, foodCholesterol, foodSodium, foodCarbo, foodSugar, foodProtein, imageURL);

        if (diabetesCB.isChecked()) {
            saveFoods("Diabetes", foodName, dataClass);
        }


        if (gastroCB.isChecked()) {
            saveFoods("Gastrointestinal Disorder", foodName, dataClass);
        }

        if (bowelCB.isChecked()) {
            saveFoods("Irritable Bowel Syndrome", foodName, dataClass);
        }

        if (highBloodCB.isChecked()) {
            saveFoods("High Blood Pressure", foodName, dataClass);

        }

        if (weightCB.isChecked()) {
            saveFoods("Weight Management", foodName, dataClass);
        }

        if (anemiaCB.isChecked()) {
            saveFoods("Anemia", foodName, dataClass);
        }

        if (cholesterolCB.isChecked()) {
            saveFoods("High Cholesterol", foodName, dataClass);
        }

        if (heartCB.isChecked()) {
            saveFoods("Heart Disease", foodName, dataClass);
        }

        if (osteoporosisCB.isChecked()) {
            saveFoods("Osteoporosis", foodName, dataClass);
        }
        if (celiacCB.isChecked()) {
            saveFoods("Celiac Disease", foodName, dataClass);
        }

        if (renalCB.isChecked()) {
            saveFoods("Renal Disease", foodName, dataClass);
        }

        if (hypothyroidismCB.isChecked()) {
            saveFoods("Hypothyroidism", foodName, dataClass);
        }

        if (happyCB.isChecked()) {
            saveMood("Happy", foodName, dataClass);
        }

        if (sadCB.isChecked()) {
            saveMood("Sad", foodName, dataClass);
        }

        if (angryCB.isChecked()) {
            saveMood("Angry", foodName, dataClass);
        }

        if (stressCB.isChecked()) {
            saveMood("Stress", foodName, dataClass);
        }

        if (excitedCB.isChecked()) {
            saveMood("Excited", foodName, dataClass);

        }

        if (nostalgiaCB.isChecked()) {
            saveMood("Nostalgia", foodName, dataClass);

        }
        if (inLoveCB.isChecked()) {
            saveMood("In Love", foodName, dataClass);
        }

        if (calmCB.isChecked()) {
            saveMood("Calm", foodName, dataClass);
        }

    }

    private void saveFoods(String category, String foodName, DataClass dataClass) {
        FirebaseDatabase.getInstance().getReference("Foods")
                .child(category)
                .child(foodName)
                .setValue(dataClass)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(UploadActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UploadActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveMood(String category, String foodName, DataClass dataClass) {
        FirebaseDatabase.getInstance().getReference("Moods")
                .child(category)
                .child(foodName)
                .setValue(dataClass)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(UploadActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UploadActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }





}