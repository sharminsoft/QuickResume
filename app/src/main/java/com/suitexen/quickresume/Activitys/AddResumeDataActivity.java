package com.suitexen.quickresume.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.suitexen.quickresume.R;

public class AddResumeDataActivity extends AppCompatActivity {

    ImageView back_btn, goTemplate_btn;
    TextView resume_profilesTV;
    LinearLayout career_objectiveLL, educational_qualificationsLL, work_experienceLL, skillsLL,
            projects_and_activitiesLL, certifications_and_trainingLL, awards_and_achievementsLL,
            volunteer_workLL, languages_knownLL, referencesLL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_resume_data);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });




        back_btn = findViewById(R.id.back_btn);
        goTemplate_btn = findViewById(R.id.goTemplate_btn);
        resume_profilesTV = findViewById(R.id.resume_profilesTV);

        career_objectiveLL = findViewById(R.id.career_objectiveLL);
        educational_qualificationsLL = findViewById(R.id.educational_qualificationsLL);
        work_experienceLL = findViewById(R.id.work_experienceLL);
        skillsLL = findViewById(R.id.skillsLL);
        projects_and_activitiesLL = findViewById(R.id.projects_and_activitiesLL);
        certifications_and_trainingLL = findViewById(R.id.certifications_and_trainingLL);
        awards_and_achievementsLL = findViewById(R.id.awards_and_achievementsLL);
        volunteer_workLL = findViewById(R.id.volunteer_and_extracurricular_activitiesLL);
        languages_knownLL = findViewById(R.id.languages_knownLL);
        referencesLL = findViewById(R.id.referencesLL);

        String profileName = getIntent().getStringExtra("fullName");
        String resumeId = getIntent().getStringExtra("resumeId");
        if (profileName != null){
            resume_profilesTV.setText(profileName);
        }


        back_btn.setOnClickListener(v -> finish());

        goTemplate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AddResumeDataActivity.this, TemplatesActivity.class);
                intent.putExtra("profileId", resumeId);
                intent.putExtra("fromActivity", "AddResumeDataActivity");
                startActivity(intent);
            }
        });

        career_objectiveLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AddResumeDataActivity.this, InputResumeDataActivity.class);
                intent.putExtra("resumeId", resumeId);
                intent.putExtra("section", "career_objective");
                startActivity(intent);
            }
        });

        educational_qualificationsLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AddResumeDataActivity.this, InputResumeDataActivity.class);
                intent.putExtra("resumeId", resumeId);
                intent.putExtra("section", "educational_qualifications");
                startActivity(intent);
            }
        });

        work_experienceLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AddResumeDataActivity.this, InputResumeDataActivity.class);
                intent.putExtra("resumeId", resumeId);
                intent.putExtra("section", "work_experience");
                startActivity(intent);
            }
        });

        skillsLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AddResumeDataActivity.this, InputResumeDataActivity.class);
                intent.putExtra("resumeId", resumeId);
                intent.putExtra("section", "skills");
                startActivity(intent);
            }
        });

        projects_and_activitiesLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AddResumeDataActivity.this, InputResumeDataActivity.class);
                intent.putExtra("resumeId", resumeId);
                intent.putExtra("section", "projects_and_activities");
                startActivity(intent);
            }
        });

        certifications_and_trainingLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AddResumeDataActivity.this, InputResumeDataActivity.class);
                intent.putExtra("resumeId", resumeId);
                intent.putExtra("section", "certifications_and_training");
                startActivity(intent);
            }
        });

        awards_and_achievementsLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AddResumeDataActivity.this, InputResumeDataActivity.class);
                intent.putExtra("resumeId", resumeId);
                intent.putExtra("section", "awards_and_achievements");
                startActivity(intent);
            }
        });

        volunteer_workLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AddResumeDataActivity.this, InputResumeDataActivity.class);
                intent.putExtra("resumeId", resumeId);
                intent.putExtra("section", "volunteer_work");
                startActivity(intent);
            }
        });

        languages_knownLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AddResumeDataActivity.this, InputResumeDataActivity.class);
                intent.putExtra("resumeId", resumeId);
                intent.putExtra("section", "languages_known");
                startActivity(intent);
            }
        });

        referencesLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AddResumeDataActivity.this, InputResumeDataActivity.class);
                intent.putExtra("resumeId", resumeId);
                intent.putExtra("section", "references");
                startActivity(intent);
            }
        });


    }//--
}