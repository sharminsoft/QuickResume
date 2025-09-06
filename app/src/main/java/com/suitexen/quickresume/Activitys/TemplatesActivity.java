package com.suitexen.quickresume.Activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.suitexen.quickresume.Adapters.TemplatesAdapter;
import com.suitexen.quickresume.ModelClass.TemplatesModel;
import com.suitexen.quickresume.R;

import java.util.ArrayList;
import java.util.List;

public class TemplatesActivity extends AppCompatActivity {

    RecyclerView templateRecycler;
    ProgressBar loadingPB;
    private FirebaseFirestore db;
    private List<TemplatesModel> templatesList;
    private TemplatesAdapter templatesAdapter;
    ImageView back_btn;

    String profileId, fromActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_templates);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        profileId = getIntent().getStringExtra("profileId");
        fromActivity = getIntent().getStringExtra("fromActivity");


        templateRecycler = findViewById(R.id.templateRV);
        loadingPB = findViewById(R.id.loadingPB);
        back_btn = findViewById(R.id.back_btn);


        // Initialize Firestore
        db = FirebaseFirestore.getInstance();
        templatesList = new ArrayList<>();


        back_btn.setOnClickListener(v -> finish());

        // Setup RecyclerView
        templateRecycler.setLayoutManager(new GridLayoutManager(this, 2));
        templatesAdapter = new TemplatesAdapter(templatesList, this, fromActivity, profileId);
        templateRecycler.setAdapter(templatesAdapter);

        // Load templates from Firestore
        loadTemplates();


    }

    private void loadTemplates() {
        loadingPB.setVisibility(View.VISIBLE);

        db.collection("Templates")
                .get()
                .addOnCompleteListener(task -> {
                    loadingPB.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        templatesList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            TemplatesModel template = document.toObject(TemplatesModel.class);
                            templatesList.add(template);
                        }
                        templatesAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "Error loading templates", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}