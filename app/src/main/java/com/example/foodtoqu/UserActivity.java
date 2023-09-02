package com.example.foodtoqu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserActivity extends AppCompatActivity {
    AppCompatButton filterBtn, recommendBtn;
    RelativeLayout filterLayout;
    RecyclerView recyclerView2;
    DatabaseReference databaseReference;
    ArrayList<DataClass>list;
    UserAdapter adapter;
    int processedReferences = 0;
    CheckBox diabetesCB, gastroCB, bowelCB, highBloodCB, weightCB, anemiaCB, cholesterolCB, heartCB, osteoporosisCB, celiacCB, renalCB, hypothyroidismCB;
    CheckBox happyCB, sadCB, angryCB, stressCB, excitedCB, nostalgiaCB, inLoveCB, calmCB;
    ValueEventListener eventListener;


    boolean filterHidden = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user);

        initWidgets();
        hideFilter();

        list = new ArrayList<>();
        recyclerView2 = findViewById(R.id.recyclerView2);


        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView2.setLayoutManager(gridLayoutManager);

        AlertDialog.Builder builder = new AlertDialog.Builder(UserActivity.this);
        builder.setCancelable(true);
        builder.setView(R.layout.loading_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        list = new ArrayList<>();

        adapter = new UserAdapter(UserActivity.this, list);
        recyclerView2.setAdapter(adapter);

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
        list.clear();

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
                        list.clear();
                        list.addAll(dataMap.values()); // Add the data to the dataList
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
        recommendBtn = findViewById(R.id.recommendBtn);


        recommendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (diabetesCB.isChecked()) {
                    list = new ArrayList<>();
                    adapter = new UserAdapter(UserActivity.this, list);
                    recyclerView2.setAdapter(adapter);

                    databaseReference = FirebaseDatabase.getInstance().getReference("Foods").child("Diabetes");
                    dialog.show();

                    eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            list.clear();
                            for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                                DataClass dataClass = dataSnapshot.getValue(DataClass.class);
                                list.add(dataClass);
                            }
                            adapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                            dialog.dismiss();
                        }
                    });
                }


                if (gastroCB.isChecked()) {
                    list = new ArrayList<>();
                    adapter = new UserAdapter(UserActivity.this, list);
                    recyclerView2.setAdapter(adapter);

                    databaseReference = FirebaseDatabase.getInstance().getReference("Foods").child("Gastrointestinal Disorder");
                    dialog.show();

                    eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            list.clear();
                            for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                                DataClass dataClass = dataSnapshot.getValue(DataClass.class);
                                list.add(dataClass);
                            }
                            adapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                            dialog.dismiss();
                        }
                    });
                }

                if (bowelCB.isChecked()) {
                    list = new ArrayList<>();
                    adapter = new UserAdapter(UserActivity.this, list);
                    recyclerView2.setAdapter(adapter);

                    databaseReference = FirebaseDatabase.getInstance().getReference("Foods").child("Irritable Bowel Syndrome");
                    dialog.show();

                    eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            list.clear();
                            for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                                DataClass dataClass = dataSnapshot.getValue(DataClass.class);
                                list.add(dataClass);
                            }
                            adapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                            dialog.dismiss();
                        }
                    });
                }

                if (highBloodCB.isChecked()) {
                    list = new ArrayList<>();
                    adapter = new UserAdapter(UserActivity.this, list);
                    recyclerView2.setAdapter(adapter);

                    databaseReference = FirebaseDatabase.getInstance().getReference("Foods").child("High Blood Pressure");
                    dialog.show();

                    eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            list.clear();
                            for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                                DataClass dataClass = dataSnapshot.getValue(DataClass.class);
                                list.add(dataClass);
                            }
                            adapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                            dialog.dismiss();
                        }
                    });

                }

                if (weightCB.isChecked()) {
                    list = new ArrayList<>();
                    adapter = new UserAdapter(UserActivity.this, list);
                    recyclerView2.setAdapter(adapter);

                    databaseReference = FirebaseDatabase.getInstance().getReference("Foods").child("Weight Management");
                    dialog.show();

                    eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            list.clear();
                            for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                                DataClass dataClass = dataSnapshot.getValue(DataClass.class);
                                list.add(dataClass);
                            }
                            adapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                            dialog.dismiss();
                        }
                    });
                }

                if (anemiaCB.isChecked()) {
                    list = new ArrayList<>();
                    adapter = new UserAdapter(UserActivity.this, list);
                    recyclerView2.setAdapter(adapter);

                    databaseReference = FirebaseDatabase.getInstance().getReference("Foods").child("Anemia");
                    dialog.show();

                    eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            list.clear();
                            for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                                DataClass dataClass = dataSnapshot.getValue(DataClass.class);
                                list.add(dataClass);
                            }
                            adapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                            dialog.dismiss();
                        }
                    });
                }

                if (cholesterolCB.isChecked()) {
                    list = new ArrayList<>();
                    adapter = new UserAdapter(UserActivity.this, list);
                    recyclerView2.setAdapter(adapter);

                    databaseReference = FirebaseDatabase.getInstance().getReference("Foods").child("High Cholesterol");
                    dialog.show();

                    eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            list.clear();
                            for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                                DataClass dataClass = dataSnapshot.getValue(DataClass.class);
                                list.add(dataClass);
                            }
                            adapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                            dialog.dismiss();
                        }
                    });
                }

                if (heartCB.isChecked()) {
                    list = new ArrayList<>();
                    adapter = new UserAdapter(UserActivity.this, list);
                    recyclerView2.setAdapter(adapter);

                    databaseReference = FirebaseDatabase.getInstance().getReference("Foods").child("High Cholesterol");
                    dialog.show();

                    eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            list.clear();
                            for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                                DataClass dataClass = dataSnapshot.getValue(DataClass.class);
                                list.add(dataClass);
                            }
                            adapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                            dialog.dismiss();
                        }
                    });
                }

                if (osteoporosisCB.isChecked()) {
                    list = new ArrayList<>();
                    adapter = new UserAdapter(UserActivity.this, list);
                    recyclerView2.setAdapter(adapter);

                    databaseReference = FirebaseDatabase.getInstance().getReference("Foods").child("Osteoporosis");
                    dialog.show();

                    eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            list.clear();
                            for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                                DataClass dataClass = dataSnapshot.getValue(DataClass.class);
                                list.add(dataClass);
                            }
                            adapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                            dialog.dismiss();
                        }
                    });
                }
                if (celiacCB.isChecked()) {
                    list = new ArrayList<>();
                    adapter = new UserAdapter(UserActivity.this, list);
                    recyclerView2.setAdapter(adapter);

                    databaseReference = FirebaseDatabase.getInstance().getReference("Foods").child("Celiac Disease");
                    dialog.show();

                    eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            list.clear();
                            for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                                DataClass dataClass = dataSnapshot.getValue(DataClass.class);
                                list.add(dataClass);
                            }
                            adapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                            dialog.dismiss();
                        }
                    });
                }

                if (renalCB.isChecked()) {
                    list = new ArrayList<>();
                    adapter = new UserAdapter(UserActivity.this, list);
                    recyclerView2.setAdapter(adapter);

                    databaseReference = FirebaseDatabase.getInstance().getReference("Foods").child("Renal Disease");
                    dialog.show();

                    eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            list.clear();
                            for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                                DataClass dataClass = dataSnapshot.getValue(DataClass.class);
                                list.add(dataClass);
                            }
                            adapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                            dialog.dismiss();
                        }
                    });
                }

                if (hypothyroidismCB.isChecked()) {
                    list = new ArrayList<>();
                    adapter = new UserAdapter(UserActivity.this, list);
                    recyclerView2.setAdapter(adapter);

                    databaseReference = FirebaseDatabase.getInstance().getReference("Foods").child("Hypothyroidism");
                    dialog.show();

                    eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            list.clear();
                            for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                                DataClass dataClass = dataSnapshot.getValue(DataClass.class);
                                list.add(dataClass);
                            }
                            adapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                            dialog.dismiss();
                        }
                    });
                }

                if (happyCB.isChecked()) {
                    list = new ArrayList<>();
                    adapter = new UserAdapter(UserActivity.this, list);
                    recyclerView2.setAdapter(adapter);

                    databaseReference = FirebaseDatabase.getInstance().getReference("Moods").child("Happy");
                    dialog.show();

                    eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            list.clear();
                            for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                                DataClass dataClass = dataSnapshot.getValue(DataClass.class);
                                list.add(dataClass);
                            }
                            adapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                            dialog.dismiss();
                        }
                    });
                }

                if (sadCB.isChecked()) {
                    list = new ArrayList<>();
                    adapter = new UserAdapter(UserActivity.this, list);
                    recyclerView2.setAdapter(adapter);

                    databaseReference = FirebaseDatabase.getInstance().getReference("Moods").child("Sad");
                    dialog.show();

                    eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            list.clear();
                            for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                                DataClass dataClass = dataSnapshot.getValue(DataClass.class);
                                list.add(dataClass);
                            }
                            adapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                            dialog.dismiss();
                        }
                    });
                }

                if (angryCB.isChecked()) {
                    list = new ArrayList<>();
                    adapter = new UserAdapter(UserActivity.this, list);
                    recyclerView2.setAdapter(adapter);

                    databaseReference = FirebaseDatabase.getInstance().getReference("Moods").child("Angry");
                    dialog.show();

                    eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            list.clear();
                            for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                                DataClass dataClass = dataSnapshot.getValue(DataClass.class);
                                list.add(dataClass);
                            }
                            adapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                            dialog.dismiss();
                        }
                    });
                }

                if (stressCB.isChecked()) {
                    list = new ArrayList<>();
                    adapter = new UserAdapter(UserActivity.this, list);
                    recyclerView2.setAdapter(adapter);

                    databaseReference = FirebaseDatabase.getInstance().getReference("Moods").child("Stress");
                    dialog.show();

                    eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            list.clear();
                            for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                                DataClass dataClass = dataSnapshot.getValue(DataClass.class);
                                list.add(dataClass);
                            }
                            adapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                            dialog.dismiss();
                        }
                    });
                }

                if (excitedCB.isChecked()) {
                    list = new ArrayList<>();
                    adapter = new UserAdapter(UserActivity.this, list);
                    recyclerView2.setAdapter(adapter);

                    databaseReference = FirebaseDatabase.getInstance().getReference("Moods").child("Excited");
                    dialog.show();

                    eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            list.clear();
                            for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                                DataClass dataClass = dataSnapshot.getValue(DataClass.class);
                                list.add(dataClass);
                            }
                            adapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                            dialog.dismiss();
                        }
                    });

                }

                if (nostalgiaCB.isChecked()) {
                    list = new ArrayList<>();
                    adapter = new UserAdapter(UserActivity.this, list);
                    recyclerView2.setAdapter(adapter);

                    databaseReference = FirebaseDatabase.getInstance().getReference("Moods").child("Nostalgia");
                    dialog.show();

                    eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            list.clear();
                            for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                                DataClass dataClass = dataSnapshot.getValue(DataClass.class);
                                list.add(dataClass);
                            }
                            adapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                            dialog.dismiss();
                        }
                    });

                }
                if (inLoveCB.isChecked()) {
                    list = new ArrayList<>();
                    adapter = new UserAdapter(UserActivity.this, list);
                    recyclerView2.setAdapter(adapter);

                    databaseReference = FirebaseDatabase.getInstance().getReference("Moods").child("In Love");
                    dialog.show();

                    eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            list.clear();
                            for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                                DataClass dataClass = dataSnapshot.getValue(DataClass.class);
                                list.add(dataClass);
                            }
                            adapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                            dialog.dismiss();
                        }
                    });
                }

                if (calmCB.isChecked()) {
                    list = new ArrayList<>();
                    adapter = new UserAdapter(UserActivity.this, list);
                    recyclerView2.setAdapter(adapter);

                    databaseReference = FirebaseDatabase.getInstance().getReference("Moods").child("Calm");
                    dialog.show();

                    eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            list.clear();
                            for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                                DataClass dataClass = dataSnapshot.getValue(DataClass.class);
                                list.add(dataClass);
                            }
                            adapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                            dialog.dismiss();
                        }
                    });
                }

                filterLayout.setVisibility(View.GONE);
            }
        });



    }



    private void initWidgets() {

        filterBtn = findViewById(R.id.filterBtn);
        filterLayout = findViewById(R.id.filterTab);
    }

    public void showFilterTapped(View view) {
        if (filterHidden == true){
            filterHidden = false;
            showFilter();
        }
        else {
            filterHidden = true;
            hideFilter();
        }

    }
    private void hideFilter() {
        filterLayout.setVisibility(View.GONE);
    }
    private void showFilter() {
        filterLayout.setVisibility(View.VISIBLE);
    }
}