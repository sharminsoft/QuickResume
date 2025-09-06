package com.suitexen.quickresume.Activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.suitexen.quickresume.Adapters.AllResumeProfileAdapter;
import com.suitexen.quickresume.ModelClass.ResumeProfileModel;
import com.suitexen.quickresume.R;

import java.util.ArrayList;
import java.util.List;

public class AllResumeProfileActivity extends AppCompatActivity {

    ImageView back_btn;
    RecyclerView all_resume_profilesRV;
    LinearLayout no_data_layout;
    CardView create_profile_btn;
    ProgressDialog progressDialog;

    private FirebaseFirestore db;
    private List<ResumeProfileModel> allresumeProfileModelList;
    private AllResumeProfileAdapter allResumeProfileAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_all_resume_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        showProgressDialog("Loading Resume Profiles...");

        // Initialize views
        back_btn = findViewById(R.id.back_btn);
        all_resume_profilesRV = findViewById(R.id.all_resume_profilesRV);
        no_data_layout = findViewById(R.id.no_data_layout);
        create_profile_btn = findViewById(R.id.create_profile_btn);

        // Click listeners
        back_btn.setOnClickListener(v -> finish());

        create_profile_btn.setOnClickListener(v -> {
            Intent intent = new Intent(AllResumeProfileActivity.this, ResumeProfilesActivity.class);
            startActivity(intent);
        });

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();

        // Initialize RecyclerView and its adapter
        allresumeProfileModelList = new ArrayList<>();
        allResumeProfileAdapter = new AllResumeProfileAdapter(allresumeProfileModelList, this);
        all_resume_profilesRV.setLayoutManager(new GridLayoutManager(this, 2));
        all_resume_profilesRV.setAdapter(allResumeProfileAdapter);

        loadResumeProfiles();
    }

    private void loadResumeProfiles() {
        // Get current user ID
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Query only documents where userId matches current user
        db.collection("All Resume Profiles")
                .whereEqualTo("userId", currentUserId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    allresumeProfileModelList.clear();
                    for (com.google.firebase.firestore.DocumentSnapshot document : queryDocumentSnapshots) {
                        ResumeProfileModel profile = document.toObject(ResumeProfileModel.class);
                        if (profile != null) {
                            allresumeProfileModelList.add(profile);
                        }
                    }

                    // Check if data is empty and show/hide appropriate views
                    if (allresumeProfileModelList.isEmpty()) {
                        showNoDataLayout();
                    } else {
                        showRecyclerView();
                    }

                    allResumeProfileAdapter.notifyDataSetChanged();
                    hideProgressDialog();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error loading profiles: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                    showNoDataLayout(); // Show no data layout on error too
                    hideProgressDialog();
                });
    }

    private void showNoDataLayout() {
        no_data_layout.setVisibility(View.VISIBLE);
        all_resume_profilesRV.setVisibility(View.GONE);
    }

    private void showRecyclerView() {
        no_data_layout.setVisibility(View.GONE);
        all_resume_profilesRV.setVisibility(View.VISIBLE);
    }

    private void showProgressDialog(String message) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false); // ব্যাক বাটনে বন্ধ হবে না
        progressDialog.show();
    }

    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload data when returning from ResumeProfilesActivity
        loadResumeProfiles();
    }
}