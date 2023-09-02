package com.example.foodtoqu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminActivity extends AppCompatActivity {

    FloatingActionButton fab;
    RecyclerView recyclerView;
    List<DataClass> dataList;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    SearchView searchView;
    MyAdapter adapter;
    int processedReferences = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_admin);

        fab = findViewById(R.id.fab);
        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.search);
        searchView.clearFocus();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(AdminActivity.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        AlertDialog.Builder builder = new AlertDialog.Builder(AdminActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.loading_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        dataList = new ArrayList<>();

        adapter = new MyAdapter(AdminActivity.this, dataList);
        recyclerView.setAdapter(adapter);

        Map<String, DataClass> dataMap = new HashMap<>();
        // Create a list to store your database references
        List<DatabaseReference> databaseReferences = new ArrayList<>();

// Add all your references to the list
        databaseReferences.add(FirebaseDatabase.getInstance().getReference("Foods").child("Diabetes"));
        databaseReferences.add(FirebaseDatabase.getInstance().getReference("Foods").child("Gastrointestinal Disorder"));
        databaseReferences.add(FirebaseDatabase.getInstance().getReference("Foods").child("Irritable Bowel Syndrome"));
        databaseReferences.add(FirebaseDatabase.getInstance().getReference("Foods").child("High Blood Pressure"));
        databaseReferences.add(FirebaseDatabase.getInstance().getReference("Foods").child("Weight Management"));
        databaseReferences.add(FirebaseDatabase.getInstance().getReference("Foods").child("Anemia"));
        databaseReferences.add(FirebaseDatabase.getInstance().getReference("Foods").child("High Cholesterol"));
        databaseReferences.add(FirebaseDatabase.getInstance().getReference("Foods").child("Heart Disease"));
        databaseReferences.add(FirebaseDatabase.getInstance().getReference("Foods").child("Osteoporosis"));
        databaseReferences.add(FirebaseDatabase.getInstance().getReference("Foods").child("Celiac Disease"));
        databaseReferences.add(FirebaseDatabase.getInstance().getReference("Foods").child("Renal Disease"));
        databaseReferences.add(FirebaseDatabase.getInstance().getReference("Foods").child("Hypothyroidism"));
// ... add more references here ...

        databaseReferences.add(FirebaseDatabase.getInstance().getReference("Moods").child("Angry"));
        databaseReferences.add(FirebaseDatabase.getInstance().getReference("Moods").child("Calm"));
        databaseReferences.add(FirebaseDatabase.getInstance().getReference("Moods").child("Excited"));
        databaseReferences.add(FirebaseDatabase.getInstance().getReference("Moods").child("Happy"));
        databaseReferences.add(FirebaseDatabase.getInstance().getReference("Moods").child("In Love"));
        databaseReferences.add(FirebaseDatabase.getInstance().getReference("Moods").child("Nostalgia"));
        databaseReferences.add(FirebaseDatabase.getInstance().getReference("Moods").child("Sad"));
        databaseReferences.add(FirebaseDatabase.getInstance().getReference("Moods").child("Stress"));
// ... add more references here ...

        dialog.show();
        dataList.clear();

        int totalReferences = databaseReferences.size();


        for (DatabaseReference reference : databaseReferences) {
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot itemSnapShot : snapshot.getChildren()) {
                        String key = itemSnapShot.getKey();
                        DataClass dataClass = itemSnapShot.getValue(DataClass.class);
                        dataMap.put(key, dataClass);
                    }

                    processedReferences++;
                    // Check if all references have been processed
                    if (processedReferences == totalReferences) {
                        dataList.clear();
                        dataList.addAll(dataMap.values()); // Add the data to the dataList
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    processedReferences++;
                    // Check if all references have been processed
                    if (processedReferences == totalReferences) {
                        dialog.dismiss();
                    }
                }
            });
        }






        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });




        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, UploadActivity.class);
                startActivity(intent);
            }
        });
    }

    public void searchList(String tex){
        ArrayList<DataClass> searchList = new ArrayList<>();
        for (DataClass dataClass: dataList){
            if (dataClass.getDataName().toLowerCase().contains(tex.toLowerCase())){
                searchList.add(dataClass);
            }
        }
        adapter.searchDataList(searchList);
    }

}