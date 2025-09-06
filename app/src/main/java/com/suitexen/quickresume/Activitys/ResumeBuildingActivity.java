package com.suitexen.quickresume.Activitys;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import com.suitexen.quickresume.Adapters.AwardAdapter;
import com.suitexen.quickresume.Adapters.CertificationsAdapter;
import com.suitexen.quickresume.Adapters.EducationAdapter;
import com.suitexen.quickresume.Adapters.LanguagesAdapter;
import com.suitexen.quickresume.Adapters.ProjectsAdapter;
import com.suitexen.quickresume.Adapters.ReferencesAdapter;
import com.suitexen.quickresume.Adapters.SkillsAdapter;
import com.suitexen.quickresume.Adapters.VolunteerWorkAdapter;
import com.suitexen.quickresume.Adapters.WorkExperienceAdapter;
import com.suitexen.quickresume.ModelClass.AwardItem;
import com.suitexen.quickresume.ModelClass.CertificationItem;
import com.suitexen.quickresume.ModelClass.EducationItem;
import com.suitexen.quickresume.ModelClass.LanguageItem;
import com.suitexen.quickresume.ModelClass.ProjectItem;
import com.suitexen.quickresume.ModelClass.ReferenceItem;
import com.suitexen.quickresume.ModelClass.ResumeProfileModel;
import com.suitexen.quickresume.ModelClass.SkillsItem;
import com.suitexen.quickresume.ModelClass.VolunteerWorkItem;
import com.suitexen.quickresume.ModelClass.WorkExperienceItem;
import com.suitexen.quickresume.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ResumeBuildingActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;


    String resumeName;


    // Constants
    private static final int STORAGE_PERMISSION_CODE = 100;

    // UI Components
    private LinearLayout main_template_layout, template_olivia_wilson, template_lizzie, template_jack_william, template_david_anderson,
            template_dyrection_pesh, template_jennifer_anderson, template_karen_richards, template_kelly_white, template_khalil_richardson,
            template_laura_parker, template_noel_taylor, template_samuel_white, template_thomas_smith, template_usman_ali;
    private ImageView backButton, shareButton;


    // Declare variables for all the views you want to access from templates
    private CircleImageView resumeProfileImg;
    private TextView resumeFullName;
    private TextView resumePosition;
    private TextView resumePhoneNumber;
    private TextView resumeEmail;
    private TextView resumeWebsite;
    private TextView resumeAddress;
    private RecyclerView resumeEducationRecyclerView;
    private RecyclerView resumeCertificationsRecyclerView;
    private RecyclerView resumeExpertiseRecyclerView;
    private RecyclerView resumeLanguageRecyclerView;
    private TextView resumeProfileTxt;
    private RecyclerView resumeWorkExperienceRecyclerView;
    private TextView resumeProjectTxt;
    private RecyclerView resumeProjectRecyclerView;
    private RecyclerView resumeReferencesRecyclerView;
    private RecyclerView resumeAwardsRecyclerView;;
    private RecyclerView resumeExtraCurricularRecyclerView;

    private TextView resume_certification_txt;
    private TextView resume_expertise_txt;
    private TextView resume_language_txt;
    private TextView resume_workExperience_txt;
    private TextView resume_education_txt;
    private TextView resume_project_txt;
    private TextView resume_references_txt;
    private TextView resume_award_txt;
    private TextView resume_extra_curricular_txt;

    //for template lizzie
//    private NestedScrollView nestedScrollView;
//    private LinearLayout parentLinearLayout;
//    private LinearLayout leftLinearLayout;
    private ImageView lizzieResumeProfileImg;
    private TextView lizzieResumeProfileTxt;
    private TextView lizzieResumeAddress;
    private TextView lizzieResumePhoneNumber;
    private TextView lizzieResumeEmail;
    private TextView lizzieResumeWebsite;
    private RecyclerView lizzieResumeExpertiseRecyclerView;
    private RecyclerView lizzieResumeLanguageRecyclerView;
    private RecyclerView lizzieResumeReferencesRecyclerView;
//    private LinearLayout rightLinearLayout;
//    private RelativeLayout rightRelativeLayout;
    private TextView lizzieResumeFullName;
    private TextView lizzieResumePosition;
    private RecyclerView lizzieResumeWorkExperienceRecyclerView;
    private RecyclerView lizzieResumeEducationRecyclerView;
    private RecyclerView lizzieResumeProjectRecyclerView;
    private RecyclerView lizzieResumeCertificationsRecyclerView;
    private RecyclerView lizzieResumeAwardsRecyclerView;
    private RecyclerView lizzieResumeExtraCurricularRecyclerView;

    private TextView lizzie_resume_certifications_txt;
    private TextView lizzie_resume_expertise_txt;
    private TextView lizzie_resume_language_txt;
    private TextView lizzie_resume_award_txt;
    private TextView lizzie_resume_workExperience_txt;
    private TextView lizzie_resume_education_txt;
    private TextView lizzie_resume_project_txt;
    private TextView lizzie_resume_references_txt;
    private TextView lizzie_resume_extra_curricular_txt;






    // Declare all variables for jack williams template

    private TextView jackResumeFullName;
    private TextView jackResumePosition;
    //private LinearLayout jackRightTopVerticalLayout;
    private TextView jackResumeAddress;
    private TextView jackResumePhoneNumber;
    private TextView jackResumeEmail;
    private TextView jackResumeWebsite;
//    private LinearLayout jackBottomHorizontalLayout;
//    private LinearLayout jackLeftBottomVerticalLayout;
    private RecyclerView jackResumeEducationRecyclerView;
    private RecyclerView jackResumeExpertiseRecyclerView;
    private RecyclerView jackResumeLanguageRecyclerView;
    //private LinearLayout jackRightBottomVerticalLayout;
    private TextView jackResumeProfileTxt;
    private RecyclerView jackResumeWorkExperienceRecyclerView;
    private RecyclerView jackResumeProjectRecyclerView;
    private RecyclerView jackResumeCertificationsRecyclerView;
    private RecyclerView jackResumeReferencesRecyclerView;
    private RecyclerView jackResumeAwardsRecyclerView;
    private RecyclerView jackResumeExtraCurricularRecyclerView;

    private TextView jack_resume_certifications_txt;
    private TextView jack_resume_expertise_txt;
    private TextView jack_resume_language_txt;
    private TextView jack_resume_award_txt;
    private TextView jack_resume_workExperience_txt;
    private TextView jack_resume_education_txt;
    private TextView jack_resume_project_txt;
    private TextView jack_resume_extra_curricular_txt;
    private TextView jack_resume_references_txt;












    // Declare David Anderson template all variables
    //private LinearLayout leftVerticalLayout;
    private CircleImageView davidResumeProfileImg;
    private TextView davidResumeAddress;
    private TextView davidResumePhoneNumber;
    private TextView davidResumeEmail;
    private TextView davidResumeWebsite;
    private RecyclerView davidResumeCertificationsRecyclerView;
    private RecyclerView davidResumeExpertiseRecyclerView;
    private RecyclerView davidResumeLanguageRecyclerView;
    //private LinearLayout rightVerticalLayout;
    private TextView davidResumeFullName;
    private TextView davidResumePosition;
    private TextView davidResumeProfileTxt;
    private RecyclerView davidResumeWorkExperienceRecyclerView;
    private RecyclerView davidResumeEducationRecyclerView;
    private RecyclerView davidResumeProjectRecyclerView;
    private RecyclerView davidResumeReferencesRecyclerView;
    private RecyclerView davidResumeAwardsRecyclerView;
    private RecyclerView davidResumeExtraCurricularRecyclerView;

    private View david_resume_profile_line_view, david_resume_certifications_line_view, david_resume_expertise_line_view, david_resume_language_line_view,
            david_resume_workExperience_line_view, david_resume_education_line_view, david_resume_project_line_view, david_resume_references_line_view,
            david_resume_awards_line_view, david_resume_extra_curricular_line_view;


    private TextView david_resume_certifications_txt;
    private TextView david_resume_expertise_txt;
    private TextView david_resume_language_txt;
    private TextView david_resume_work_experience_txt;
    private TextView david_resume_education_txt;
    private TextView david_resume_project_txt;
    private TextView david_resume_references_txt;
    private TextView david_resume_awards_txt;
    private TextView david_resume_extra_curricular_txt;


    // Declare all variables for dyrection pesh template

    private CircleImageView dyrectionResumeProfileImg;
    private TextView dyrectionResumeProfileTxt;
    private TextView dyrectionResumeAddress;
    private TextView dyrectionResumePhoneNumber;
    private TextView dyrectionResumeEmail;
    private TextView dyrectionResumeWebsite;
    private RecyclerView dyrectionResumeExpertiseRecyclerView;
    private RecyclerView dyrectionResumeLanguageRecyclerView;
    private RecyclerView dyrectionResumeCertificationsRecyclerView;
    private TextView dyrectionResumeFullName;
    private TextView dyrectionResumePosition;
    private RecyclerView dyrectionResumeWorkExperienceRecyclerView;
    private RecyclerView dyrectionResumeEducationRecyclerView;
    private RecyclerView dyrectionResumeProjectRecyclerView;
    private RecyclerView dyrectionResumeReferencesRecyclerView;
    private RecyclerView dyrectionResumeAwardsRecyclerView;
    private RecyclerView dyrectionResumeExtraCurricularRecyclerView;

    private TextView dyrection_resume_certifications_txt;
    private TextView dyrection_resume_expertise_txt;
    private TextView dyrection_resume_language_txt;
    private TextView dyrection_resume_work_experience_txt;
    private TextView dyrection_resume_education_txt;
    private TextView dyrection_resume_project_txt;
    private TextView dyrection_resume_references_txt;
    private TextView dyrection_resume_awards_txt;
    private TextView dyrection_resume_extra_curricular_txt;




    // Declare all variables for jennifer anderson template

    private CircleImageView jenniferResumeProfileImg;
    private TextView jenniferResumeAddress;
    private TextView jenniferResumePhoneNumber;
    private TextView jenniferResumeEmail;
    private TextView jenniferResumeWebsite;
    private RecyclerView jenniferResumeExpertiseRecyclerView;
    private RecyclerView jenniferResumeLanguageRecyclerView;
    private RecyclerView jenniferResumeCertificationsRecyclerView;
    private TextView jenniferResumeFullName;
    private TextView jenniferResumePosition;
    private TextView jenniferResumeProfileTxt;
    private RecyclerView jenniferResumeWorkExperienceRecyclerView;
    private RecyclerView jenniferResumeEducationRecyclerView;
    private RecyclerView jenniferResumeProjectRecyclerView;
    private RecyclerView jenniferResumeReferencesRecyclerView;
    private RecyclerView jenniferResumeAwardsRecyclerView;
    private RecyclerView jenniferResumeExtraCurricularRecyclerView;


    private TextView jennifer_resume_certifications_txt;
    private TextView jennifer_resume_expertise_txt;
    private TextView jennifer_resume_language_txt;
    private TextView jennifer_resume_work_experience_txt;
    private TextView jennifer_resume_education_txt;
    private TextView jennifer_resume_project_txt;
    private TextView jennifer_resume_references_txt;
    private TextView jennifer_resume_awards_txt;
    private TextView jennifer_resume_extra_curricular_txt;



    // Declare all variables for karen richards template

    private ImageView karenResumeProfileImg;
    private TextView karenResumeAddress;
    private TextView karenResumePhoneNumber;
    private TextView karenResumeEmail;
    private TextView karenResumeWebsite;
    private RecyclerView karenResumeExpertiseRecyclerView;
    private RecyclerView karenResumeLanguageRecyclerView;
    private RecyclerView karenResumeCertificationsRecyclerView;
    private TextView karenResumeFullName;
    private TextView karenResumePosition;
    private TextView karenResumeProfileTxt;
    private RecyclerView karenResumeWorkExperienceRecyclerView;
    private RecyclerView karenResumeEducationRecyclerView;
    private RecyclerView karenResumeProjectRecyclerView;
    private RecyclerView karenResumeReferencesRecyclerView;
    private RecyclerView karenResumeAwardsRecyclerView;
    private RecyclerView karenResumeExtraCurricularRecyclerView;


    private TextView karen_resume_certifications_txt;
    private TextView karen_resume_expertise_txt;
    private TextView karen_resume_language_txt;
    private TextView karen_resume_work_experience_txt;
    private TextView karen_resume_education_txt;
    private TextView karen_resume_project_txt;
    private TextView karen_resume_references_txt;
    private TextView karen_resume_awards_txt;
    private TextView karen_resume_extra_curricular_txt;

    private View karen_resume_contact_view, karen_resume_expertise_view, karen_resume_language_view, karen_resume_certifications_view, karen_resume_award_view,
            karen_resume_work_experience_view, karen_resume_education_view, karen_resume_project_view, karen_resume_references_view, karen_resume_profile_view,
            karen_resume_extra_curricular_view, karen_resume_about_me_view;


    // Declare all variables for kelly white template
    //private RelativeLayout parentRelativeLayout;
    private ConstraintLayout constraintLayout;
    private LinearLayout linearLayout;
    private CircleImageView kellyCircleImageView;
    private TextView kellyResumeFullName;
    private TextView kellyResumePosition;
    private RecyclerView kellyResumeEducationRecyclerView;
    private RecyclerView kellyResumeWorkExperienceRecyclerView;
    private RecyclerView kellyResumeProjectRecyclerView;
    private RecyclerView kellyResumeReferencesRecyclerView;
    private TextView kellyResumeProfileTxt;
    private TextView kellyResumeAddress;
    private TextView kellyResumePhoneNumber;
    private TextView kellyResumeEmail;
    private TextView kellyResumeWebsite;
    private RecyclerView kellyResumeExpertiseRecyclerView;
    private RecyclerView kellyResumeLanguageRecyclerView;
    private RecyclerView kellyResumeCertificationsRecyclerView;
    private RecyclerView kellyResumeAwardsRecyclerView;
    private RecyclerView kellyResumeExtraCurricularRecyclerView;


    private TextView kelly_resume_certifications_txt;
    private TextView kelly_resume_expertise_txt;
    private TextView kelly_resume_language_txt;
    private TextView kelly_resume_work_experience_txt;
    private TextView kelly_resume_education_txt;
    private TextView kelly_resume_project_txt;
    private TextView kelly_resume_references_txt;
    private TextView kelly_resume_awards_txt;
    private TextView kelly_resume_extra_curricular_txt;


    // Declare all variables for khalil richardson template

    private CircleImageView khalilCircleImageView;
    private TextView khalilResumeFullName;
    private TextView khalilResumePosition;
    private TextView khalilResumeProfileTxt;
    private TextView khalilResumeAddress;
    private TextView khalilResumePhoneNumber;
    private TextView khalilResumeEmail;
    private TextView khalilResumeWebsite;
    private RecyclerView khalilResumeExpertiseRecyclerView;
    private RecyclerView khalilResumeLanguageRecyclerView;
    private RecyclerView khalilResumeCertificationsRecyclerView;
    private RecyclerView khalilResumeWorkExperienceRecyclerView;
    private RecyclerView khalilResumeEducationRecyclerView;
    private RecyclerView khalilResumeProjectRecyclerView;
    private RecyclerView khalilResumeReferencesRecyclerView;
    private RecyclerView khalilResumeAwardsRecyclerView;
    private RecyclerView khalilResumeExtraCurricularRecyclerView;

    private TextView khalil_resume_certifications_txt;
    private TextView khalil_resume_expertise_txt;
    private TextView khalil_resume_language_txt;
    private TextView khalil_resume_work_experience_txt;
    private TextView khalil_resume_education_txt;
    private TextView khalil_resume_project_txt;
    private TextView khalil_resume_references_txt;
    private TextView khalil_resume_awards_txt;
    private TextView khalil_resume_extra_curricular_txt;

    // Declare all variables for laura parker template

    private TextView lauraResumeFullName;
    private TextView lauraResumePosition;
    private TextView lauraResumePhoneNumber;
    private TextView lauraResumeEmail;
    private TextView lauraResumeWebsite;
    private TextView lauraResumeAddress;
    private TextView lauraResumeProfileTxt;
    private RecyclerView lauraResumeWorkExperienceRecyclerView;
    private RecyclerView lauraResumeEducationRecyclerView;
    private RecyclerView lauraResumeProjectRecyclerView;
    private RecyclerView lauraResumeReferencesRecyclerView;
    private ImageView lauraResumeProfileImg;
    private TextView certificateTxt;
    private RecyclerView lauraResumeCertificationsRecyclerView;
    private RecyclerView lauraResumeExpertiseRecyclerView;
    private RecyclerView lauraResumeLanguageRecyclerView;
    private RecyclerView lauraResumeAwardsRecyclerView;
    private RecyclerView lauraResumeExtraCurricularRecyclerView;

    private TextView laura_resume_certifications_txt;
    private TextView laura_resume_expertise_txt;
    private TextView laura_resume_language_txt;
    private TextView laura_resume_work_experience_txt;
    private TextView laura_resume_education_txt;
    private TextView laura_resume_project_txt;
    private TextView laura_resume_references_txt;
    private TextView laura_resume_awards_txt;
    private TextView laura_resume_extra_curricular_txt;


    // Declare all variables for noel taylor template
    private TextView noelResumeFullName;
    private TextView noelResumePosition;
    private CircleImageView noelResumeProfileImg;
    private TextView noelResumeAddress;
    private TextView noelResumePhoneNumber;
    private TextView noelResumeEmail;
    private TextView noelResumeWebsite;
    private RecyclerView noelResumeEducationRecyclerView;
    private RecyclerView noelResumeExpertiseRecyclerView;
    private RecyclerView noelResumeLanguageRecyclerView;
    private TextView noelResumeProfileTxt;
    private RecyclerView noelResumeWorkExperienceRecyclerView;
    private RecyclerView noelResumeProjectRecyclerView;
    private RecyclerView noelResumeCertificationsRecyclerView;
    private RecyclerView noelResumeReferencesRecyclerView;
    private RecyclerView noelResumeAwardsRecyclerView;
    private RecyclerView noelResumeExtraCurricularRecyclerView;

    private TextView noel_resume_certifications_txt;
    private TextView noel_resume_expertise_txt;
    private TextView noel_resume_language_txt;
    private TextView noel_resume_work_experience_txt;
    private TextView noel_resume_education_txt;
    private TextView noel_resume_project_txt;
    private TextView noel_resume_references_txt;
    private TextView noel_resume_awards_txt;
    private TextView noel_resume_extra_curricular_txt;


    // Declare all variables for samuel white template
    private ImageView samuelProfileImage;
    private TextView samuelResumeAddress;
    private TextView samuelResumePhoneNumber;
    private TextView samuelResumeEmail;
    private TextView samuelResumeWebsite;
    private RecyclerView samuelResumeCertificationsRecyclerView;
    private RecyclerView samuelResumeExpertiseRecyclerView;
    private RecyclerView samuelResumeLanguageRecyclerView;
    private TextView samuelResumePosition;
    private TextView samuelResumeFullName;
    private TextView samuelResumeProfileTxt;
    private RecyclerView samuelResumeWorkExperienceRecyclerView;
    private RecyclerView samuelResumeProjectRecyclerView;
    private RecyclerView samuelResumeEducationRecyclerView;
    private RecyclerView samuelResumeReferencesRecyclerView;
    private RecyclerView samuelResumeAwardsRecyclerView;
    private RecyclerView samuelResumeExtraCurricularRecyclerView;


    private TextView samuel_resume_certifications_txt;
    private TextView samuel_resume_expertise_txt;
    private TextView samuel_resume_language_txt;
    private TextView samuel_resume_work_experience_txt;
    private TextView samuel_resume_education_txt;
    private TextView samuel_resume_project_txt;
    private TextView samuel_resume_references_txt;
    private TextView samuel_resume_awards_txt;
    private TextView samuel_resume_extra_curricular_txt;


    // Declare all variables for usman ali template
    private CircleImageView usmanResumeProfileImg;
    private TextView usmanResumeAddress;
    private TextView usmanResumePhoneNumber;
    private TextView usmanResumeEmail;
    private TextView usmanResumeWebsite;
    private RecyclerView usmanResumeCertificationsRecyclerView;
    private RecyclerView usmanResumeExpertiseRecyclerView;
    private RecyclerView usmanResumeLanguageRecyclerView;
    private TextView usmanResumeFullName;
    private TextView usmanResumePosition;
    private TextView usmanResumeProfileTxt;
    private RecyclerView usmanResumeWorkExperienceRecyclerView;
    private RecyclerView usmanResumeEducationRecyclerView;
    private RecyclerView usmanResumeProjectRecyclerView;
    private RecyclerView usmanResumeReferencesRecyclerView;
    private RecyclerView usmanResumeAwardsRecyclerView;
    private RecyclerView usmanResumeExtraCurricularRecyclerView;

    private TextView usman_resume_certifications_txt;
    private TextView usman_resume_expertise_txt;
    private TextView usman_resume_language_txt;
    private TextView usman_resume_work_experience_txt;
    private TextView usman_resume_education_txt;
    private TextView usman_resume_project_txt;
    private TextView usman_resume_references_txt;
    private TextView usman_resume_awards_txt;
    private TextView usman_resume_extra_curricular_txt;


    // Declare all variables for thomas smith template
    private CircleImageView thomasProfileImage;
    private TextView thomasResumeAddress;
    private TextView thomasResumePhoneNumber;
    private TextView thomasResumeEmail;
    private TextView thomasResumeWebsite;
    private RecyclerView thomasResumeCertificationsRecyclerView;
    private RecyclerView thomasResumeExpertiseRecyclerView;
    private RecyclerView thomasResumeLanguageRecyclerView;
    private TextView thomasResumeFullName;
    private TextView thomasResumePosition;
    private TextView thomasResumeProfileTxt;
    private RecyclerView thomasResumeWorkExperienceRecyclerView;
    private RecyclerView thomasResumeProjectRecyclerView;
    private RecyclerView thomasResumeEducationRecyclerView;
    private RecyclerView thomasResumeReferencesRecyclerView;
    private RecyclerView thomasResumeAwardsRecyclerView;
    private RecyclerView thomasResumeExtraCurricularRecyclerView;

    private TextView thomas_resume_certifications_txt;
    private TextView thomas_resume_expertise_txt;
    private TextView thomas_resume_language_txt;
    private TextView thomas_resume_work_experience_txt;
    private TextView thomas_resume_education_txt;
    private TextView thomas_resume_project_txt;
    private TextView thomas_resume_references_txt;
    private TextView thomas_resume_awards_txt;
    private TextView thomas_resume_extra_curricular_txt;



    // Data
    private String profileId, templateName;
    private FirebaseFirestore db;
//    private ResumeProfileModel currentProfile;
//    private List<String> careerObjectives;
//    private List<CertificationItem> certifications;
//    private List<EducationItem> educationList;
//    private List<LanguageItem> languages;
//    private List<ProjectItem> projects;
//    private List<ReferenceItem> references;
//    private List<SkillsItem> skills;
//    private List<VolunteerWorkItem> volunteerWork;
//    private List<WorkExperienceItem> workExperience;

    // Adapters
    private SkillsAdapter skillsAdapter;
    private EducationAdapter educationAdapter;
    private WorkExperienceAdapter workExperienceAdapter;
    private ProjectsAdapter projectsAdapter;
    private CertificationsAdapter certificationsAdapter;
    private LanguagesAdapter languagesAdapter;
    private ReferencesAdapter referencesAdapter;
    private VolunteerWorkAdapter volunteerWorkAdapter;
    private AwardAdapter awardAdapter;


    // Permission handling
    private ActivityResultLauncher<String> requestPermissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        // Enable hardware acceleration for better rendering
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
        );
        setContentView(R.layout.activity_resume_building);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        showProgressDialog("Generating Resume...");

        // Get intent data
        profileId = getIntent().getStringExtra("profileId");
        templateName = getIntent().getStringExtra("templateName");

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();





        // Initialize UI components
        initializeViews();

        initializeTemplateViews();
        initializeLizzieTemplateViews();
        initializeJackTemplateViews();
        initializeDavidTemplateViews();
        initializeDyrectionTemplateViews();
        initializeJenniferTemplateViews();
        initializeKarenTemplateViews();
        initializeKellyTemplateViews();
        initializeKhalilTemplateViews();
        initializeLauraTemplateViews();
        initializeNoelTemplateViews();
        initializeSamuelTemplateViews();
        initializeThomasTemplateViews();
        initializeUsmanTemplateViews();

        setupPermissionLauncher();
        setupListeners();

        // Show appropriate template layout
        if (templateName.equals("samuel_white")) {

            SkillsAdapter.template_tag = "white";
            LanguagesAdapter.template_tag = "white";
            CertificationsAdapter.template_tag = "white";
            AwardAdapter.template_tag = "white";

            template_samuel_white.setVisibility(View.VISIBLE);

        } else if (templateName.equals("jennifer_anderson")) {

            SkillsAdapter.template_tag = "white";
            LanguagesAdapter.template_tag = "white";
            CertificationsAdapter.template_tag = "white";
            AwardAdapter.template_tag = "white";
            template_jennifer_anderson.setVisibility(View.VISIBLE);


        }else if (templateName.equals("dyrection_pesh")) {

            SkillsAdapter.template_tag = "black";
            LanguagesAdapter.template_tag = "black";
            CertificationsAdapter.template_tag = "dyrection_pesh";
            AwardAdapter.template_tag = "dyrection_pesh";

            EducationAdapter.template_tag = "dyrection_pesh";
            WorkExperienceAdapter.template_tag = "dyrection_pesh";
            ProjectsAdapter.template_tag = "dyrection_pesh";
            VolunteerWorkAdapter.template_tag = "dyrection_pesh";


            template_dyrection_pesh.setVisibility(View.VISIBLE);

        }else if (templateName.equals("karen_richards")) {

            SkillsAdapter.template_tag = "white";
            LanguagesAdapter.template_tag = "white";
            CertificationsAdapter.template_tag = "white";
            AwardAdapter.template_tag = "white";

            template_karen_richards.setVisibility(View.VISIBLE);

        }else if (templateName.equals("thomas_smith")) {

            SkillsAdapter.template_tag = "thomas_smith";
            LanguagesAdapter.template_tag = "thomas_smith";
            CertificationsAdapter.template_tag = "thomas_smith";
            template_thomas_smith.setVisibility(View.VISIBLE);

        }else if (templateName.equals("lizzie_johan")) {

            SkillsAdapter.template_tag = "white";
            LanguagesAdapter.template_tag = "white";
            CertificationsAdapter.template_tag = "white";
            EducationAdapter.template_tag = "lizzie_johan";
            WorkExperienceAdapter.template_tag = "lizzie_johan";
            ProjectsAdapter.template_tag = "lizzie_johan";
            VolunteerWorkAdapter.template_tag = "lizzie_johan";
            AwardAdapter.template_tag = "lizzie_johan";
            ReferencesAdapter.template_tag = "lizzie_johan";

            template_lizzie.setVisibility(View.VISIBLE);


        }else if (templateName.equals("kelly_white")) {

            SkillsAdapter.template_tag = "white";
            LanguagesAdapter.template_tag = "white";
            CertificationsAdapter.template_tag = "white";
            AwardAdapter.template_tag = "white";
            template_kelly_white.setVisibility(View.VISIBLE);

        }else if (templateName.equals("jack_william")) {

            SkillsAdapter.template_tag = "black";
            LanguagesAdapter.template_tag = "black";
            CertificationsAdapter.template_tag = "jack_william";
            AwardAdapter.template_tag = "jack_william";

            template_jack_william.setVisibility(View.VISIBLE);

        }else if (templateName.equals("david_anderson")) {

            SkillsAdapter.template_tag = "white";
            LanguagesAdapter.template_tag = "white";
            CertificationsAdapter.template_tag = "white";
            AwardAdapter.template_tag = "david_anderson";
            EducationAdapter.template_tag = "david_anderson";
            WorkExperienceAdapter.template_tag = "david_anderson";
            ProjectsAdapter.template_tag = "david_anderson";
            VolunteerWorkAdapter.template_tag = "david_anderson";
            ReferencesAdapter.template_tag = "david_anderson";

            template_david_anderson.setVisibility(View.VISIBLE);

        }else if (templateName.equals("olivia_wilson")) {

            SkillsAdapter.template_tag = "black";
            LanguagesAdapter.template_tag = "black";
            CertificationsAdapter.template_tag = "black";
            AwardAdapter.template_tag = "olivia_wilson";
            ReferencesAdapter.template_tag = "olivia_wilson";
            EducationAdapter.template_tag = "olivia_wilson";
            WorkExperienceAdapter.template_tag = "olivia_wilson";
            ProjectsAdapter.template_tag = "olivia_wilson";
            VolunteerWorkAdapter.template_tag = "olivia_wilson";

            template_olivia_wilson.setVisibility(View.VISIBLE);

        }else if (templateName.equals("usman_ali")) {
            SkillsAdapter.template_tag = "black";
            LanguagesAdapter.template_tag = "black";
            CertificationsAdapter.template_tag = "black";
            AwardAdapter.template_tag = "black";
            template_usman_ali.setVisibility(View.VISIBLE);

        }else if (templateName.equals("laura_parker")) {

            SkillsAdapter.template_tag = "black";
            LanguagesAdapter.template_tag = "black";
            CertificationsAdapter.template_tag = "black";
            AwardAdapter.template_tag = "black";

            template_laura_parker.setVisibility(View.VISIBLE);


        }else if (templateName.equals("noel_taylor")) {

            SkillsAdapter.template_tag = "white";
            LanguagesAdapter.template_tag = "white";
            CertificationsAdapter.template_tag = "white";
            //AwardAdapter.template_tag = "white";
            template_noel_taylor.setVisibility(View.VISIBLE);

        }else if (templateName.equals("khalil_richardson")) {

            SkillsAdapter.template_tag = "white";
            LanguagesAdapter.template_tag = "white";
            CertificationsAdapter.template_tag = "white";
            AwardAdapter.template_tag = "white";
            template_khalil_richardson.setVisibility(View.VISIBLE);


        } else {
            template_olivia_wilson.setVisibility(View.GONE);
            template_lizzie.setVisibility(View.GONE);
            template_jack_william.setVisibility(View.GONE);
            template_david_anderson.setVisibility(View.GONE);
            template_dyrection_pesh.setVisibility(View.GONE);
            template_jennifer_anderson.setVisibility(View.GONE);
            template_karen_richards.setVisibility(View.GONE);
            template_kelly_white.setVisibility(View.GONE);
            template_khalil_richardson.setVisibility(View.GONE);
            template_laura_parker.setVisibility(View.GONE);
            template_noel_taylor.setVisibility(View.GONE);
            template_samuel_white.setVisibility(View.GONE);
            template_thomas_smith.setVisibility(View.GONE);
            template_usman_ali.setVisibility(View.GONE);

            // Handle other templates here
        }

        setupResumeCarreerObjectives();

        setupResumeProfileData();
        setupResumeEducation();
        setupResumeCertifications();
        setupResumeExpertise();
        setupResumeLanguage();
        setupResumeWorkExperience();
        setupResumeProjects();
        setupResumeAwards();
        setupExtraCurricular();
        setupResumeReferences();



    }//-----------

    private void setupResumeCarreerObjectives() {
        if (profileId == null || profileId.isEmpty()) {
            Toast.makeText(this, "Profile ID is missing.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Fetch the "career_objective" sub-collection from Firestore
        db.collection("All Resume Profiles")
                .document(profileId)
                .collection("career_objective")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    String careerObjective = "";

                    // Check if there are any documents in the collection
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // Get the first document (assuming there's only one document in career_objective collection)
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);

                        // Get the "careerObjective" field from the document
                        if (documentSnapshot.contains("careerObjective")) {
                            careerObjective = documentSnapshot.getString("careerObjective");
                            // Now you can use the careerObjective string as needed
                            // For example, set it to a TextView
                            // careerObjectiveTextView.setText(careerObjective);

                            resumeProfileTxt.setText(careerObjective);
                            lizzieResumeProfileTxt.setText(careerObjective);
                            jackResumeProfileTxt.setText(careerObjective);
                            davidResumeProfileTxt.setText(careerObjective);
                            dyrectionResumeProfileTxt.setText(careerObjective);
                            jenniferResumeProfileTxt.setText(careerObjective);
                            karenResumeProfileTxt.setText(careerObjective);
                            kellyResumeProfileTxt.setText(careerObjective);
                            khalilResumeProfileTxt.setText(careerObjective);
                            lauraResumeProfileTxt.setText(careerObjective);
                            noelResumeProfileTxt.setText(careerObjective);
                            samuelResumeProfileTxt.setText(careerObjective);
                            thomasResumeProfileTxt.setText(careerObjective);
                            usmanResumeProfileTxt.setText(careerObjective);

                        }
                    }

                    // Log the retrieved value (optional)
                    Log.d("CareerObjective", "Retrieved objective: " + careerObjective);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error fetching Career Objective data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


    private void initializeUsmanTemplateViews() {

        // Initialize all views using findViewById
        usmanResumeProfileImg = findViewById(R.id.usman_resume_profile_img);
        usmanResumeAddress = findViewById(R.id.usman_resume_address);
        usmanResumePhoneNumber = findViewById(R.id.usman_resume_phone_number);
        usmanResumeEmail = findViewById(R.id.usman_resume_email);
        usmanResumeWebsite = findViewById(R.id.usman_resume_website);
        usmanResumeCertificationsRecyclerView = findViewById(R.id.usman_resume_certifications_recyclearview);
        usmanResumeExpertiseRecyclerView = findViewById(R.id.usman_resume_expertise_recyclearView);
        usmanResumeLanguageRecyclerView = findViewById(R.id.usman_resume_language_recyclearView);
        usmanResumeFullName = findViewById(R.id.usman_resume_full_name);
        usmanResumePosition = findViewById(R.id.usman_resume_position);
        usmanResumeProfileTxt = findViewById(R.id.usman_resume_profile_txt);
        usmanResumeWorkExperienceRecyclerView = findViewById(R.id.usman_resume_workExperience_recyclearView);
        usmanResumeEducationRecyclerView = findViewById(R.id.usman_resume_education_recyclearview);
        usmanResumeProjectRecyclerView = findViewById(R.id.usman_resume_project_recyclearView);
        usmanResumeReferencesRecyclerView = findViewById(R.id.usman_resume_references_recyclearView);
        usmanResumeAwardsRecyclerView = findViewById(R.id.usman_resume_awards_recyclearView);
        usmanResumeExtraCurricularRecyclerView = findViewById(R.id.usman_resume_extra_curricular_recyclearView);

        usman_resume_awards_txt = findViewById(R.id.usman_resume_awards_txt);
        usman_resume_certifications_txt = findViewById(R.id.usman_resume_certifications_txt);
        usman_resume_education_txt = findViewById(R.id.usman_resume_education_txt);
        usman_resume_expertise_txt = findViewById(R.id.usman_resume_expertise_txt);
        usman_resume_language_txt = findViewById(R.id.usman_resume_language_txt);
        usman_resume_project_txt = findViewById(R.id.usman_resume_project_txt);
        usman_resume_references_txt = findViewById(R.id.usman_resume_references_txt);
        usman_resume_work_experience_txt = findViewById(R.id.usman_resume_work_experience_txt);
        usman_resume_extra_curricular_txt = findViewById(R.id.usman_resume_extra_curricular_txt);


        setupRecyclerView(usmanResumeCertificationsRecyclerView);
        setupRecyclerView(usmanResumeExpertiseRecyclerView);
        setupRecyclerView(usmanResumeLanguageRecyclerView);
        setupRecyclerView(usmanResumeWorkExperienceRecyclerView);
        setupRecyclerView(usmanResumeEducationRecyclerView);
        setupRecyclerView(usmanResumeProjectRecyclerView);
        setupRecyclerView(usmanResumeReferencesRecyclerView);
        setupRecyclerView(usmanResumeAwardsRecyclerView);
        setupRecyclerView(usmanResumeExtraCurricularRecyclerView);
    }

    private void initializeThomasTemplateViews() {


        // Initialize all views using findViewById
        thomasProfileImage = findViewById(R.id.thomas_profile_image);
        thomasResumeAddress = findViewById(R.id.thomas_resume_address);
        thomasResumePhoneNumber = findViewById(R.id.thomas_resume_phone_number);
        thomasResumeEmail = findViewById(R.id.thomas_resume_email);
        thomasResumeWebsite = findViewById(R.id.thomas_resume_website);
        thomasResumeCertificationsRecyclerView = findViewById(R.id.thomas_resume_certifications_recyclearview);
        thomasResumeExpertiseRecyclerView = findViewById(R.id.thomas_resume_expertise_recyclearView);
        thomasResumeLanguageRecyclerView = findViewById(R.id.thomas_resume_language_recyclearView);
        thomasResumeFullName = findViewById(R.id.thomas_resume_full_name);
        thomasResumePosition = findViewById(R.id.thomas_resume_position);
        thomasResumeProfileTxt = findViewById(R.id.thomas_resume_profile_txt);
        thomasResumeWorkExperienceRecyclerView = findViewById(R.id.thomas_resume_workExperience_recyclearView);
        thomasResumeProjectRecyclerView = findViewById(R.id.thomas_resume_project_recyclearView);
        thomasResumeEducationRecyclerView = findViewById(R.id.thomas_resume_education_recyclearview);
        thomasResumeReferencesRecyclerView = findViewById(R.id.thomas_resume_references_recyclearView);
        thomasResumeAwardsRecyclerView = findViewById(R.id.thomas_resume_awards_recyclearView);
        thomasResumeExtraCurricularRecyclerView = findViewById(R.id.thomas_resume_extra_curricular_recyclearView);

        thomas_resume_awards_txt = findViewById(R.id.thomas_resume_awards_txt);
        thomas_resume_certifications_txt = findViewById(R.id.thomas_resume_certifications_txt);
        thomas_resume_education_txt = findViewById(R.id.thomas_resume_education_txt);
        thomas_resume_extra_curricular_txt = findViewById(R.id.thomas_resume_extra_curricular_txt);
        thomas_resume_expertise_txt = findViewById(R.id.thomas_resume_expertise_txt);
        thomas_resume_language_txt = findViewById(R.id.thomas_resume_language_txt);
        thomas_resume_project_txt = findViewById(R.id.thomas_resume_project_txt);
        thomas_resume_references_txt = findViewById(R.id.thomas_resume_references_txt);
        thomas_resume_work_experience_txt = findViewById(R.id.thomas_resume_work_experience_txt);

        setupRecyclerView(thomasResumeCertificationsRecyclerView);
        setupRecyclerView(thomasResumeExpertiseRecyclerView);
        setupRecyclerView(thomasResumeLanguageRecyclerView);
        setupRecyclerView(thomasResumeWorkExperienceRecyclerView);
        setupRecyclerView(thomasResumeProjectRecyclerView);
        setupRecyclerView(thomasResumeEducationRecyclerView);
        setupRecyclerView(thomasResumeReferencesRecyclerView);
        setupRecyclerView(thomasResumeAwardsRecyclerView);
        setupRecyclerView(thomasResumeExtraCurricularRecyclerView);



    }

    private void initializeSamuelTemplateViews() {

        // Initialize all views using findViewById
        samuelProfileImage = findViewById(R.id.samuel_profile_image);
        samuelResumeAddress = findViewById(R.id.samuel_resume_address);
        samuelResumePhoneNumber = findViewById(R.id.samuel_resume_phone_number);
        samuelResumeEmail = findViewById(R.id.samuel_resume_email);
        samuelResumeWebsite = findViewById(R.id.samuel_resume_website);
        samuelResumeCertificationsRecyclerView = findViewById(R.id.samuel_resume_certifications_recyclearview);
        samuelResumeExpertiseRecyclerView = findViewById(R.id.samuel_resume_expertise_recyclearView);
        samuelResumeLanguageRecyclerView = findViewById(R.id.samuel_resume_language_recyclearView);
        samuelResumePosition = findViewById(R.id.samuel_resume_position);
        samuelResumeFullName = findViewById(R.id.samuel_resume_full_name);
        samuelResumeProfileTxt = findViewById(R.id.samuel_resume_profile_txt);
        samuelResumeWorkExperienceRecyclerView = findViewById(R.id.samuel_resume_workExperience_recyclearView);
        samuelResumeProjectRecyclerView = findViewById(R.id.samuel_resume_project_recyclearView);
        samuelResumeEducationRecyclerView = findViewById(R.id.samuel_resume_education_recyclearview);
        samuelResumeReferencesRecyclerView = findViewById(R.id.samuel_resume_references_recyclearView);
        samuelResumeAwardsRecyclerView = findViewById(R.id.samuel_resume_awards_recyclearView);
        samuelResumeExtraCurricularRecyclerView = findViewById(R.id.samuel_resume_extra_curricular_recyclearView);

        samuel_resume_awards_txt = findViewById(R.id.samuel_resume_award_txt);
        samuel_resume_extra_curricular_txt = findViewById(R.id.samuel_resume_extra_curricular_txt);
        samuel_resume_references_txt = findViewById(R.id.samuel_resume_references_txt);
        samuel_resume_education_txt = findViewById(R.id.samuel_resume_education_txt);
        samuel_resume_project_txt = findViewById(R.id.samuel_resume_project_txt);
        samuel_resume_work_experience_txt = findViewById(R.id.samuel_resume_workExperience_txt);
        samuel_resume_language_txt = findViewById(R.id.samuel_resume_language_txt);
        samuel_resume_expertise_txt = findViewById(R.id.samuel_resume_expertise_txt);
        samuel_resume_certifications_txt = findViewById(R.id.samuel_resume_certifications_txt);

        setupRecyclerView(samuelResumeCertificationsRecyclerView);
        setupRecyclerView(samuelResumeExpertiseRecyclerView);
        setupRecyclerView(samuelResumeLanguageRecyclerView);
        setupRecyclerView(samuelResumeWorkExperienceRecyclerView);
        setupRecyclerView(samuelResumeProjectRecyclerView);
        setupRecyclerView(samuelResumeEducationRecyclerView);
        setupRecyclerView(samuelResumeReferencesRecyclerView);
        setupRecyclerView(samuelResumeAwardsRecyclerView);
        setupRecyclerView(samuelResumeExtraCurricularRecyclerView);

    }

    private void initializeNoelTemplateViews() {

        // Initialize all views using findViewById
        noelResumeFullName = findViewById(R.id.noel_resume_full_name);
        noelResumePosition = findViewById(R.id.noel_resume_position);
        noelResumeProfileImg = findViewById(R.id.noel_resume_profile_img);
        noelResumeAddress = findViewById(R.id.noel_resume_address);
        noelResumePhoneNumber = findViewById(R.id.noel_resume_phone_number);
        noelResumeEmail = findViewById(R.id.noel_resume_email);
        noelResumeWebsite = findViewById(R.id.noel_resume_website);
        noelResumeEducationRecyclerView = findViewById(R.id.noel_resume_education_recyclearview);
        noelResumeExpertiseRecyclerView = findViewById(R.id.noel_resume_expertise_recyclearView);
        noelResumeLanguageRecyclerView = findViewById(R.id.noel_resume_language_recyclearView);
        noelResumeProfileTxt = findViewById(R.id.noel_resume_profile_txt);
        noelResumeWorkExperienceRecyclerView = findViewById(R.id.noel_resume_workExperience_recyclearView);
        noelResumeProjectRecyclerView = findViewById(R.id.noel_resume_project_recyclearView);
        noelResumeCertificationsRecyclerView = findViewById(R.id.noel_resume_certifications_recyclearview);
        noelResumeReferencesRecyclerView = findViewById(R.id.noel_resume_references_recyclearView);
        noelResumeAwardsRecyclerView = findViewById(R.id.noel_resume_awards_recyclearView);
        noelResumeExtraCurricularRecyclerView = findViewById(R.id.noel_resume_extra_curricular_recyclearview);



        noel_resume_awards_txt = findViewById(R.id.noel_resume_award_txt);
        noel_resume_certifications_txt = findViewById(R.id.noel_resume_certifications_txt);
        noel_resume_education_txt = findViewById(R.id.noel_resume_education_txt);
        noel_resume_expertise_txt = findViewById(R.id.noel_resume_expertise_txt);
        noel_resume_language_txt = findViewById(R.id.noel_resume_language_txt);
        noel_resume_project_txt = findViewById(R.id.noel_resume_project_txt);
        noel_resume_references_txt = findViewById(R.id.noel_resume_references_txt);
        noel_resume_work_experience_txt = findViewById(R.id.noel_resume_workExperience_txt);
        noel_resume_extra_curricular_txt = findViewById(R.id.noel_resume_extra_curricular_txt);


        setupRecyclerView(noelResumeEducationRecyclerView);
        setupRecyclerView(noelResumeExpertiseRecyclerView);
        setupRecyclerView(noelResumeLanguageRecyclerView);
        setupRecyclerView(noelResumeWorkExperienceRecyclerView);
        setupRecyclerView(noelResumeProjectRecyclerView);
        setupRecyclerView(noelResumeCertificationsRecyclerView);
        setupRecyclerView(noelResumeReferencesRecyclerView);
        setupRecyclerView(noelResumeAwardsRecyclerView);
        setupRecyclerView(noelResumeExtraCurricularRecyclerView);

    }

    private void initializeLauraTemplateViews() {

        // Initialize all views using findViewById
        lauraResumeFullName = findViewById(R.id.laura_resume_full_name);
        lauraResumePosition = findViewById(R.id.laura_resume_position);
        lauraResumePhoneNumber = findViewById(R.id.laura_resume_phone_number);
        lauraResumeEmail = findViewById(R.id.laura_resume_email);
        lauraResumeWebsite = findViewById(R.id.laura_resume_website);
        lauraResumeAddress = findViewById(R.id.laura_resume_address);
        lauraResumeProfileTxt = findViewById(R.id.laura_resume_profile_txt);
        lauraResumeWorkExperienceRecyclerView = findViewById(R.id.laura_resume_workExperience_recyclearView);
        lauraResumeEducationRecyclerView = findViewById(R.id.laura_resume_education_recyclearView);
        resumeProjectTxt = findViewById(R.id.resume_project_txt);
        lauraResumeProjectRecyclerView = findViewById(R.id.laura_resume_project_recyclearView);
        lauraResumeReferencesRecyclerView = findViewById(R.id.laura_resume_references_recyclearView);
        lauraResumeProfileImg = findViewById(R.id.laura_resume_profile_img);
        certificateTxt = findViewById(R.id.certificate_txt);
        lauraResumeCertificationsRecyclerView = findViewById(R.id.laura_resume_certifications_recyclearview);
        lauraResumeExpertiseRecyclerView = findViewById(R.id.laura_resume_expertise_recyclearView);
        lauraResumeLanguageRecyclerView = findViewById(R.id.laura_resume_language_recyclearView);
        lauraResumeAwardsRecyclerView = findViewById(R.id.laura_resume_awards_recyclearView);
        lauraResumeExtraCurricularRecyclerView = findViewById(R.id.laura_resume_extra_curricular_recyclearView);

        laura_resume_education_txt = findViewById(R.id.laura_resume_education_txt);
        laura_resume_awards_txt = findViewById(R.id.laura_resume_award_txt);
        laura_resume_extra_curricular_txt = findViewById(R.id.laura_resume_extra_curricular_txt);
        laura_resume_certifications_txt = findViewById(R.id.laura_resume_certifications_txt);
        laura_resume_expertise_txt = findViewById(R.id.laura_resume_expertise_txt);
        laura_resume_language_txt = findViewById(R.id.laura_resume_language_txt);
        laura_resume_project_txt = findViewById(R.id.laura_resume_project_txt);
        laura_resume_references_txt = findViewById(R.id.laura_resume_references_txt);
        laura_resume_work_experience_txt = findViewById(R.id.laura_resume_work_experience_txt);



        setupRecyclerView(lauraResumeWorkExperienceRecyclerView);
        setupRecyclerView(lauraResumeEducationRecyclerView);
        setupRecyclerView(lauraResumeProjectRecyclerView);
        setupRecyclerView(lauraResumeReferencesRecyclerView);
        setupRecyclerView(lauraResumeCertificationsRecyclerView);
        setupRecyclerView(lauraResumeExpertiseRecyclerView);
        setupRecyclerView(lauraResumeLanguageRecyclerView);
        setupRecyclerView(lauraResumeAwardsRecyclerView);
        setupRecyclerView(lauraResumeExtraCurricularRecyclerView);

    }

    private void initializeKhalilTemplateViews() {

        // Initialize all views using findViewById
        khalilCircleImageView = findViewById(R.id.khalil_circleImageView);
        khalilResumeFullName = findViewById(R.id.khalil_resume_full_name);
        khalilResumePosition = findViewById(R.id.khalil_resume_position);
        khalilResumeProfileTxt = findViewById(R.id.khalil_resume_profile_txt);
        khalilResumeAddress = findViewById(R.id.khalil_resume_address);
        khalilResumePhoneNumber = findViewById(R.id.khalil_resume_phone_number);
        khalilResumeEmail = findViewById(R.id.khalil_resume_email);
        khalilResumeWebsite = findViewById(R.id.khalil_resume_website);
        khalilResumeExpertiseRecyclerView = findViewById(R.id.khalil_resume_expertise_recyclearView);
        khalilResumeLanguageRecyclerView = findViewById(R.id.khalil_resume_language_recyclearView);
        khalilResumeCertificationsRecyclerView = findViewById(R.id.khalil_resume_certifications_recyclearview);
        khalilResumeWorkExperienceRecyclerView = findViewById(R.id.khalil_resume_workExperience_recyclearview);
        khalilResumeEducationRecyclerView = findViewById(R.id.khalil_resume_education_recyclearview);
        khalilResumeProjectRecyclerView = findViewById(R.id.khalil_resume_project_recyclearview);
        khalilResumeReferencesRecyclerView = findViewById(R.id.khalil_resume_references_recyclearView);
        khalilResumeAwardsRecyclerView = findViewById(R.id.khalil_resume_awards_recyclearview);
        khalilResumeExtraCurricularRecyclerView = findViewById(R.id.khalil_resume_extra_curricular_recyclearview);


        khalil_resume_expertise_txt = findViewById(R.id.khalil_resume_expertise_txt);
        khalil_resume_language_txt = findViewById(R.id.khalil_resume_language_txt);
        khalil_resume_certifications_txt = findViewById(R.id.khalil_resume_certifications_txt);
        khalil_resume_work_experience_txt = findViewById(R.id.khalil_resume_workExperience_txt);
        khalil_resume_education_txt = findViewById(R.id.khalil_resume_education_txt);
        khalil_resume_project_txt = findViewById(R.id.khalil_resume_project_txt);
        khalil_resume_references_txt = findViewById(R.id.khalil_resume_references_txt);
        khalil_resume_awards_txt = findViewById(R.id.khalil_resume_awards_txt);
        khalil_resume_extra_curricular_txt = findViewById(R.id.khalil_resume_extra_curricular_txt);

        setupRecyclerView(khalilResumeExpertiseRecyclerView);
        setupRecyclerView(khalilResumeLanguageRecyclerView);
        setupRecyclerView(khalilResumeCertificationsRecyclerView);
        setupRecyclerView(khalilResumeWorkExperienceRecyclerView);
        setupRecyclerView(khalilResumeEducationRecyclerView);
        setupRecyclerView(khalilResumeProjectRecyclerView);
        setupRecyclerView(khalilResumeReferencesRecyclerView);
        setupRecyclerView(khalilResumeAwardsRecyclerView);
        setupRecyclerView(khalilResumeExtraCurricularRecyclerView);


    }

    private void initializeKellyTemplateViews() {

        // Initialize all views using findViewById
        constraintLayout = findViewById(R.id.constraintLayout);
        linearLayout = findViewById(R.id.linearLayout);
        kellyCircleImageView = findViewById(R.id.kelly_circleImageView);
        kellyResumeFullName = findViewById(R.id.kelly_resume_full_name);
        kellyResumePosition = findViewById(R.id.kelly_resume_position);
        kellyResumeEducationRecyclerView = findViewById(R.id.kelly_resume_education_recyclearview);
        kellyResumeWorkExperienceRecyclerView = findViewById(R.id.kelly_resume_workExperience_recyclearview);
        kellyResumeProjectRecyclerView = findViewById(R.id.kelly_resume_project_recyclearview);
        kellyResumeReferencesRecyclerView = findViewById(R.id.kelly_resume_references_recyclearView);
        kellyResumeProfileTxt = findViewById(R.id.kelly_resume_profile_txt);
        kellyResumeAddress = findViewById(R.id.kelly_resume_address);
        kellyResumePhoneNumber = findViewById(R.id.kelly_resume_phone_number);
        kellyResumeEmail = findViewById(R.id.kelly_resume_email);
        kellyResumeWebsite = findViewById(R.id.kelly_resume_website);
        kellyResumeExpertiseRecyclerView = findViewById(R.id.kelly_resume_expertise_recyclearView);
        kellyResumeLanguageRecyclerView = findViewById(R.id.kelly_resume_language_recyclearView);
        kellyResumeCertificationsRecyclerView = findViewById(R.id.kelly_resume_certifications_recyclearview);
        kellyResumeAwardsRecyclerView = findViewById(R.id.kelly_resume_awards_recyclearview);
        kellyResumeExtraCurricularRecyclerView = findViewById(R.id.kelly_resume_extra_curricular_recyclearview);

        kelly_resume_education_txt = findViewById(R.id.kelly_resume_education_txt);
        kelly_resume_work_experience_txt = findViewById(R.id.kelly_resume_workExperience_txt);
        kelly_resume_project_txt = findViewById(R.id.kelly_resume_project_txt);
        kelly_resume_references_txt = findViewById(R.id.kelly_resume_references_txt);
        kelly_resume_expertise_txt = findViewById(R.id.kelly_resume_expertise_txt);
        kelly_resume_language_txt = findViewById(R.id.kelly_resume_language_txt);
        kelly_resume_certifications_txt = findViewById(R.id.kelly_resume_certification_txt);
        kelly_resume_awards_txt = findViewById(R.id.kelly_resume_award_txt);
        kelly_resume_extra_curricular_txt = findViewById(R.id.kelly_resume_extra_curricular_txt);


        setupRecyclerView(kellyResumeEducationRecyclerView);
        setupRecyclerView(kellyResumeWorkExperienceRecyclerView);
        setupRecyclerView(kellyResumeProjectRecyclerView);
        setupRecyclerView(kellyResumeReferencesRecyclerView);
        setupRecyclerView(kellyResumeExpertiseRecyclerView);
        setupRecyclerView(kellyResumeLanguageRecyclerView);
        setupRecyclerView(kellyResumeCertificationsRecyclerView);
        setupRecyclerView(kellyResumeAwardsRecyclerView);
        setupRecyclerView(kellyResumeExtraCurricularRecyclerView);
    }

    private void initializeKarenTemplateViews() {

        // Initialize all views using findViewById
        karenResumeProfileImg = findViewById(R.id.karen_resume_profile_img);
        karenResumeAddress = findViewById(R.id.karen_resume_address);
        karenResumePhoneNumber = findViewById(R.id.karen_resume_phone_number);
        karenResumeEmail = findViewById(R.id.karen_resume_email);
        karenResumeWebsite = findViewById(R.id.karen_resume_website);
        karenResumeExpertiseRecyclerView = findViewById(R.id.karen_resume_expertise_recyclearView);
        karenResumeLanguageRecyclerView = findViewById(R.id.karen_resume_language_recyclearView);
        karenResumeCertificationsRecyclerView = findViewById(R.id.karen_resume_certifications_recyclearview);
        karenResumeFullName = findViewById(R.id.karen_resume_full_name);
        karenResumePosition = findViewById(R.id.karen_resume_position);
        karenResumeProfileTxt = findViewById(R.id.karen_resume_profile_txt);
        karenResumeWorkExperienceRecyclerView = findViewById(R.id.karen_resume_workExperience_recyclearView);
        karenResumeEducationRecyclerView = findViewById(R.id.karen_resume_education_recyclearview);
        karenResumeProjectRecyclerView = findViewById(R.id.karen_resume_project_recyclearView);
        karenResumeReferencesRecyclerView = findViewById(R.id.karen_resume_references_recyclearView);
        karenResumeAwardsRecyclerView = findViewById(R.id.karen_resume_awards_recyclearview);
        karenResumeExtraCurricularRecyclerView = findViewById(R.id.karen_resume_extra_circular_recyclearView);

        karen_resume_expertise_txt = findViewById(R.id.karen_resume_expertise_txt);
        karen_resume_language_txt = findViewById(R.id.karen_resume_language_txt);
        karen_resume_certifications_txt = findViewById(R.id.karen_resume_certifications_txt);
        karen_resume_work_experience_txt = findViewById(R.id.karen_resume_workExperience_txt);
        karen_resume_education_txt = findViewById(R.id.karen_resume_education_txt);
        karen_resume_project_txt = findViewById(R.id.karen_resume_project_txt);
        karen_resume_references_txt = findViewById(R.id.karen_resume_references_txt);
        karen_resume_awards_txt = findViewById(R.id.karen_resume_award_txt);
        karen_resume_extra_curricular_txt = findViewById(R.id.karen_resume_extra_circular_txt);

        karen_resume_contact_view = findViewById(R.id.karen_resume_contact_view);
        karen_resume_expertise_view = findViewById(R.id.karen_resume_expertise_view);
        karen_resume_language_view = findViewById(R.id.karen_resume_language_view);
        karen_resume_certifications_view = findViewById(R.id.karen_resume_certifications_view);
        karen_resume_work_experience_view = findViewById(R.id.karen_resume_workExperience_view);
        karen_resume_education_view = findViewById(R.id.karen_resume_education_view);
        karen_resume_project_view = findViewById(R.id.karen_resume_project_view);
        karen_resume_references_view = findViewById(R.id.karen_resume_references_view);
        karen_resume_award_view = findViewById(R.id.karen_resume_award_view);
        karen_resume_extra_curricular_view = findViewById(R.id.karen_resume_extra_circular_view);
        karen_resume_profile_view = findViewById(R.id.karen_resume_profile_view);
        karen_resume_contact_view = findViewById(R.id.karen_resume_contact_view);




        setupRecyclerView(karenResumeExpertiseRecyclerView);
        setupRecyclerView(karenResumeLanguageRecyclerView);
        setupRecyclerView(karenResumeCertificationsRecyclerView);
        setupRecyclerView(karenResumeWorkExperienceRecyclerView);
        setupRecyclerView(karenResumeEducationRecyclerView);
        setupRecyclerView(karenResumeProjectRecyclerView);
        setupRecyclerView(karenResumeReferencesRecyclerView);
        setupRecyclerView(karenResumeAwardsRecyclerView);
        setupRecyclerView(karenResumeExtraCurricularRecyclerView);

    }

    private void initializeJenniferTemplateViews() {

        // Initialize all views using findViewById
        jenniferResumeProfileImg = findViewById(R.id.jennifer_resume_profile_img);
        jenniferResumeAddress = findViewById(R.id.jennifer_resume_address);
        jenniferResumePhoneNumber = findViewById(R.id.jennifer_resume_phone_number);
        jenniferResumeEmail = findViewById(R.id.jennifer_resume_email);
        jenniferResumeWebsite = findViewById(R.id.jennifer_resume_website);
        jenniferResumeExpertiseRecyclerView = findViewById(R.id.jennifer_resume_expertise_recyclearView);
        jenniferResumeLanguageRecyclerView = findViewById(R.id.jennifer_resume_language_recyclearView);
        jenniferResumeCertificationsRecyclerView = findViewById(R.id.jennifer_resume_certifications_recyclearview);
        jenniferResumeFullName = findViewById(R.id.jennifer_resume_full_name);
        jenniferResumePosition = findViewById(R.id.jennifer_resume_position);
        jenniferResumeProfileTxt = findViewById(R.id.jennifer_resume_profile_txt);
        jenniferResumeWorkExperienceRecyclerView = findViewById(R.id.jennifer_resume_workExperience_recyclearView);
        jenniferResumeEducationRecyclerView = findViewById(R.id.jennifer_resume_education_recyclearview);
        jenniferResumeProjectRecyclerView = findViewById(R.id.jennifer_resume_project_recyclearView);
        jenniferResumeReferencesRecyclerView = findViewById(R.id.jennifer_resume_references_recyclearView);
        jenniferResumeAwardsRecyclerView = findViewById(R.id.jennifer_resume_awards_recyclearview);
        jenniferResumeExtraCurricularRecyclerView = findViewById(R.id.jennifer_resume_extra_cirricular_recyclearView);

        jennifer_resume_certifications_txt = findViewById(R.id.jennifer_resume_certifications_txt);
        jennifer_resume_expertise_txt = findViewById(R.id.jennifer_resume_expertise_txt);
        jennifer_resume_language_txt = findViewById(R.id.jennifer_resume_language_txt);
        jennifer_resume_work_experience_txt = findViewById(R.id.jennifer_resume_workExperience_txt);
        jennifer_resume_education_txt = findViewById(R.id.jennifer_resume_education_txt);
        jennifer_resume_project_txt = findViewById(R.id.jennifer_resume_project_txt);
        jennifer_resume_references_txt = findViewById(R.id.jennifer_resume_references_txt);
        jennifer_resume_awards_txt = findViewById(R.id.jennifer_resume_awards_txt);
        jennifer_resume_extra_curricular_txt = findViewById(R.id.jennifer_resume_extra_cirricular_txt);

        setupRecyclerView(jenniferResumeExpertiseRecyclerView);
        setupRecyclerView(jenniferResumeLanguageRecyclerView);
        setupRecyclerView(jenniferResumeCertificationsRecyclerView);
        setupRecyclerView(jenniferResumeWorkExperienceRecyclerView);
        setupRecyclerView(jenniferResumeEducationRecyclerView);
        setupRecyclerView(jenniferResumeProjectRecyclerView);
        setupRecyclerView(jenniferResumeReferencesRecyclerView);
        setupRecyclerView(jenniferResumeAwardsRecyclerView);
        setupRecyclerView(jenniferResumeExtraCurricularRecyclerView);


    }

    private void initializeDyrectionTemplateViews() {

        // Initialize all views using findViewById
        dyrectionResumeProfileImg = findViewById(R.id.dyrection_resume_profile_img);
        dyrectionResumeProfileTxt = findViewById(R.id.dyrection_resume_profile_txt);
        dyrectionResumeAddress = findViewById(R.id.dyrection_resume_address);
        dyrectionResumePhoneNumber = findViewById(R.id.dyrection_resume_phone_number);
        dyrectionResumeEmail = findViewById(R.id.dyrection_resume_email);
        dyrectionResumeWebsite = findViewById(R.id.dyrection_resume_website);
        dyrectionResumeExpertiseRecyclerView = findViewById(R.id.dyrection_resume_expertise_recyclearView);
        dyrectionResumeLanguageRecyclerView = findViewById(R.id.dyrection_resume_language_recyclearView);
        dyrectionResumeCertificationsRecyclerView = findViewById(R.id.dyrection_resume_certifications_recyclearview);
        dyrectionResumeFullName = findViewById(R.id.dyrection_resume_full_name);
        dyrectionResumePosition = findViewById(R.id.dyrection_resume_position);
        dyrectionResumeWorkExperienceRecyclerView = findViewById(R.id.dyrection_resume_workExperience_recyclearView);
        dyrectionResumeEducationRecyclerView = findViewById(R.id.dyrection_resume_education_recyclearview);
        dyrectionResumeProjectRecyclerView = findViewById(R.id.dyrection_resume_project_recyclearView);
        dyrectionResumeReferencesRecyclerView = findViewById(R.id.dyrection_resume_references_recyclearView);
        dyrectionResumeAwardsRecyclerView = findViewById(R.id.dyrection_resume_awards_recyclearview);
        dyrectionResumeExtraCurricularRecyclerView = findViewById(R.id.dyrection_resume_extra_curricular_recyclearView);

        dyrection_resume_certifications_txt = findViewById(R.id.dyrection_resume_certifications_txt);
        dyrection_resume_expertise_txt = findViewById(R.id.dyrection_resume_expertise_txt);
        dyrection_resume_language_txt = findViewById(R.id.dyrection_resume_language_txt);
        dyrection_resume_work_experience_txt = findViewById(R.id.dyrection_resume_workExperience_txt);
        dyrection_resume_education_txt = findViewById(R.id.dyrection_resume_education_txt);
        dyrection_resume_project_txt = findViewById(R.id.dyrection_resume_project_txt);
        dyrection_resume_references_txt = findViewById(R.id.dyrection_resume_references_txt);
        dyrection_resume_awards_txt = findViewById(R.id.dyrection_resume_awards_txt);
        dyrection_resume_extra_curricular_txt = findViewById(R.id.dyrection_resume_extra_curricular_txt);



        setupRecyclerView(dyrectionResumeExpertiseRecyclerView);
        setupRecyclerView(dyrectionResumeLanguageRecyclerView);
        setupRecyclerView(dyrectionResumeCertificationsRecyclerView);
        setupRecyclerView(dyrectionResumeWorkExperienceRecyclerView);
        setupRecyclerView(dyrectionResumeEducationRecyclerView);
        setupRecyclerView(dyrectionResumeProjectRecyclerView);
        setupRecyclerView(dyrectionResumeReferencesRecyclerView);
        setupRecyclerView(dyrectionResumeAwardsRecyclerView);
        setupRecyclerView(dyrectionResumeExtraCurricularRecyclerView);


    }

    private void initializeDavidTemplateViews() {

        // Initialize all views using findViewById

        davidResumeProfileImg = findViewById(R.id.david_resume_profile_img);
        davidResumeAddress = findViewById(R.id.david_resume_address);
        davidResumePhoneNumber = findViewById(R.id.david_resume_phone_number);
        davidResumeEmail = findViewById(R.id.david_resume_email);
        davidResumeWebsite = findViewById(R.id.david_resume_website);
        davidResumeCertificationsRecyclerView = findViewById(R.id.david_resume_certifications_recyclearview);
        davidResumeExpertiseRecyclerView = findViewById(R.id.david_resume_expertise_recyclearView);
        davidResumeLanguageRecyclerView = findViewById(R.id.david_resume_language_recyclearView);
        davidResumeFullName = findViewById(R.id.david_resume_full_name);
        davidResumePosition = findViewById(R.id.david_resume_position);
        davidResumeProfileTxt = findViewById(R.id.david_resume_profile_txt);
        davidResumeWorkExperienceRecyclerView = findViewById(R.id.david_resume_workExperience_recyclearView);
        davidResumeEducationRecyclerView = findViewById(R.id.david_resume_education_recyclearview);
        davidResumeProjectRecyclerView = findViewById(R.id.david_resume_project_recyclearView);
        davidResumeReferencesRecyclerView = findViewById(R.id.david_resume_references_recyclearView);
        davidResumeAwardsRecyclerView = findViewById(R.id.david_resume_awards_recyclearView);
        davidResumeExtraCurricularRecyclerView = findViewById(R.id.david_resume_extra_curricular_recyclearView);

        david_resume_certifications_txt = findViewById(R.id.david_resume_certifications_txt);
        david_resume_expertise_txt = findViewById(R.id.david_resume_expertise_txt);
        david_resume_language_txt = findViewById(R.id.david_resume_language_txt);
        david_resume_work_experience_txt = findViewById(R.id.david_resume_workExperience_txt);
        david_resume_education_txt = findViewById(R.id.david_resume_education_txt);
        david_resume_project_txt = findViewById(R.id.david_resume_projects_txt);
        david_resume_references_txt = findViewById(R.id.david_resume_references_txt);
        david_resume_awards_txt = findViewById(R.id.david_resume_awards_txt);
        david_resume_extra_curricular_txt = findViewById(R.id.david_resume_extra_curricular_txt);

        david_resume_certifications_line_view = findViewById(R.id.david_resume_certifications_line_view);
        david_resume_expertise_line_view = findViewById(R.id.david_resume_expertise_line_view);
        david_resume_language_line_view = findViewById(R.id.david_resume_language_line_view);
        david_resume_workExperience_line_view = findViewById(R.id.david_resume_workExperience_line_view);
        david_resume_education_line_view = findViewById(R.id.david_resume_education_line_view);
        david_resume_project_line_view = findViewById(R.id.david_resume_projects_line_view);
        david_resume_references_line_view = findViewById(R.id.david_resume_references_line_view);
        david_resume_awards_line_view = findViewById(R.id.david_resume_awards_line_view);
        david_resume_extra_curricular_line_view = findViewById(R.id.david_resume_extra_curricular_line_view);
        david_resume_profile_line_view = findViewById(R.id.david_resume_profile_line_view);


        setupRecyclerView(davidResumeCertificationsRecyclerView);
        setupRecyclerView(davidResumeExpertiseRecyclerView);
        setupRecyclerView(davidResumeLanguageRecyclerView);
        setupRecyclerView(davidResumeWorkExperienceRecyclerView);
        setupRecyclerView(davidResumeEducationRecyclerView);
        setupRecyclerView(davidResumeProjectRecyclerView);
        setupRecyclerView(davidResumeReferencesRecyclerView);
        setupRecyclerView(davidResumeAwardsRecyclerView);
        setupRecyclerView(davidResumeExtraCurricularRecyclerView);
    }

    private void initializeJackTemplateViews() {

        // Initialize all views using findViewById
        jackResumeFullName = findViewById(R.id.jack_resume_full_name);
        jackResumePosition = findViewById(R.id.jack_resume_position);
        jackResumeAddress = findViewById(R.id.jack_resume_address);
        jackResumePhoneNumber = findViewById(R.id.jack_resume_phone_number);
        jackResumeEmail = findViewById(R.id.jack_resume_email);
        jackResumeWebsite = findViewById(R.id.jack_resume_website);
        jackResumeEducationRecyclerView = findViewById(R.id.jack_resume_education_recyclearview);
        jackResumeExpertiseRecyclerView = findViewById(R.id.jack_resume_expertise_recyclearView);
        jackResumeLanguageRecyclerView = findViewById(R.id.jack_resume_language_recyclearView);
        jackResumeProfileTxt = findViewById(R.id.jack_resume_profile_txt);
        jackResumeWorkExperienceRecyclerView = findViewById(R.id.jack_resume_workExperience_recyclearView);
        jackResumeProjectRecyclerView = findViewById(R.id.jack_resume_project_recyclearView);
        jackResumeCertificationsRecyclerView = findViewById(R.id.jack_resume_certifications_recyclearview);
        jackResumeReferencesRecyclerView = findViewById(R.id.jack_resume_references_recyclearView);
        jackResumeAwardsRecyclerView = findViewById(R.id.jack_resume_award_recyclearView);
        jackResumeExtraCurricularRecyclerView = findViewById(R.id.jack_resume_extra_curricular_recyclearView);

        jack_resume_certifications_txt = findViewById(R.id.jack_resume_certifications_txt);
        jack_resume_education_txt = findViewById(R.id.jack_resume_education_txt);
        jack_resume_expertise_txt = findViewById(R.id.jack_resume_expertise_txt);
        jack_resume_language_txt = findViewById(R.id.jack_resume_language_txt);
        jack_resume_workExperience_txt = findViewById(R.id.jack_resume_workExperience_txt);
        jack_resume_project_txt = findViewById(R.id.jack_resume_project_txt);
        jack_resume_references_txt = findViewById(R.id.jack_resume_references_txt);
        jack_resume_award_txt = findViewById(R.id.jack_resume_award_txt);
        jack_resume_extra_curricular_txt = findViewById(R.id.jack_resume_extra_curricular_txt);

        setupRecyclerView(jackResumeEducationRecyclerView);
        setupRecyclerView(jackResumeExpertiseRecyclerView);
        setupRecyclerView(jackResumeLanguageRecyclerView);
        setupRecyclerView(jackResumeWorkExperienceRecyclerView);
        setupRecyclerView(jackResumeProjectRecyclerView);
        setupRecyclerView(jackResumeCertificationsRecyclerView);
        setupRecyclerView(jackResumeReferencesRecyclerView);
        setupRecyclerView(jackResumeAwardsRecyclerView);
        setupRecyclerView(jackResumeExtraCurricularRecyclerView);

    }

    private void initializeLizzieTemplateViews() {

        // Initialize all views using findViewById

        lizzieResumeProfileImg = findViewById(R.id.lizzie_resume_profile_img);
        lizzieResumeProfileTxt = findViewById(R.id.lizzie_resume_profile_txt);
        lizzieResumeAddress = findViewById(R.id.lizzie_resume_address);
        lizzieResumePhoneNumber = findViewById(R.id.lizzie_resume_phone_number);
        lizzieResumeEmail = findViewById(R.id.lizzie_resume_email);
        lizzieResumeWebsite = findViewById(R.id.lizzie_resume_website);
        lizzieResumeExpertiseRecyclerView = findViewById(R.id.lizzie_resume_expertise_recyclearView);
        lizzieResumeLanguageRecyclerView = findViewById(R.id.lizzie_resume_language_recyclearView);
        lizzieResumeReferencesRecyclerView = findViewById(R.id.lizzie_resume_references_recyclearView);
        lizzieResumeFullName = findViewById(R.id.lizzie_resume_full_name);
        lizzieResumePosition = findViewById(R.id.lizzie_resume_position);
        lizzieResumeWorkExperienceRecyclerView = findViewById(R.id.lizzie_resume_workExperience_recyclearView);
        lizzieResumeEducationRecyclerView = findViewById(R.id.lizzie_resume_education_recyclearview);
        lizzieResumeProjectRecyclerView = findViewById(R.id.lizzie_resume_project_recyclearView);
        lizzieResumeCertificationsRecyclerView = findViewById(R.id.lizzie_resume_certifications_recyclearview);
        lizzieResumeAwardsRecyclerView = findViewById(R.id.lizzie_resume_awards_recyclearview);
        lizzieResumeExtraCurricularRecyclerView = findViewById(R.id.lizzie_resume_extra_curricular_recyclearView);

        lizzie_resume_award_txt=findViewById(R.id.lizzie_resume_awards_txt);
        lizzie_resume_certifications_txt=findViewById(R.id.lizzie_resume_certifications_txt);
        lizzie_resume_education_txt=findViewById(R.id.lizzie_resume_education_txt);
        lizzie_resume_expertise_txt=findViewById(R.id.lizzie_resume_expertise_txt);
        lizzie_resume_language_txt=findViewById(R.id.lizzie_resume_language_txt);
        lizzie_resume_project_txt=findViewById(R.id.lizzie_resume_project_txt);
        lizzie_resume_references_txt=findViewById(R.id.lizzie_resume_references_txt);
        lizzie_resume_workExperience_txt=findViewById(R.id.lizzie_resume_work_experience_txt);
        lizzie_resume_extra_curricular_txt=findViewById(R.id.lizzie_resume_extra_curricular_txt);




        // Set up LinearLayoutManager for each RecyclerView
        setupRecyclerView(lizzieResumeExpertiseRecyclerView);
        setupRecyclerView(lizzieResumeLanguageRecyclerView);
        setupRecyclerView(lizzieResumeReferencesRecyclerView);
        setupRecyclerView(lizzieResumeWorkExperienceRecyclerView);
        setupRecyclerView(lizzieResumeEducationRecyclerView);
        setupRecyclerView(lizzieResumeProjectRecyclerView);
        setupRecyclerView(lizzieResumeCertificationsRecyclerView);
        setupRecyclerView(lizzieResumeAwardsRecyclerView);
        setupRecyclerView(lizzieResumeExtraCurricularRecyclerView);
    }

    // Helper method to set up LinearLayoutManager for a RecyclerView
    private void setupRecyclerView(RecyclerView recyclerView) {
        // Create a LinearLayoutManager with vertical orientation
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);

        if (recyclerView.getId() == R.id.thomas_resume_references_recyclearView) {
            recyclerView.setLayoutManager(gridLayoutManager);
        } else if (recyclerView.getId() == R.id.usman_resume_references_recyclearView ) {

            recyclerView.setLayoutManager(gridLayoutManager);
            
        } else if (recyclerView.getId() == R.id.samuel_resume_references_recyclearView ) {

            recyclerView.setLayoutManager(gridLayoutManager);

        } else if (recyclerView.getId() == R.id.jennifer_resume_references_recyclearView ) {

            recyclerView.setLayoutManager(gridLayoutManager);

        } else if (recyclerView.getId() == R.id.dyrection_resume_references_recyclearView ) {

            recyclerView.setLayoutManager(gridLayoutManager);

        } else if (recyclerView.getId() == R.id.karen_resume_references_recyclearView ) {

            recyclerView.setLayoutManager(gridLayoutManager);

        } else if (recyclerView.getId() == R.id.lizzie_resume_references_recyclearView ) {

            recyclerView.setLayoutManager(gridLayoutManager);

        } else if (recyclerView.getId() == R.id.jack_resume_references_recyclearView ) {

            recyclerView.setLayoutManager(gridLayoutManager);

        } else if (recyclerView.getId() == R.id.david_resume_references_recyclearView ) {

            recyclerView.setLayoutManager(gridLayoutManager);

        } else if (recyclerView.getId() == R.id.resume_references_recyclearView ) {

            recyclerView.setLayoutManager(gridLayoutManager);

        } else if (recyclerView.getId() == R.id.laura_resume_references_recyclearView ) {

            recyclerView.setLayoutManager(gridLayoutManager);

        } else if (recyclerView.getId() == R.id.noel_resume_references_recyclearView ) {

            recyclerView.setLayoutManager(gridLayoutManager);

        } else if (recyclerView.getId() == R.id.khalil_resume_references_recyclearView ) {

            recyclerView.setLayoutManager(gridLayoutManager);

        } else if (recyclerView.getId() == R.id.kelly_resume_references_recyclearView ) {

            recyclerView.setLayoutManager(gridLayoutManager);

        } else {
            recyclerView.setLayoutManager(layoutManager);

        }

//        // Optional: If you want to improve performance and the RecyclerView has a fixed size
//        recyclerView.setHasFixedSize(true);
    }

    private void setupResumeReferences() {


        if (profileId == null || profileId.isEmpty()) {
            Toast.makeText(this, "Profile ID is missing.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Fetch the "skills" sub-collection from Firestore
        db.collection("All Resume Profiles")
                .document(profileId)
                .collection("references")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<ReferenceItem> referenceList = new ArrayList<>();

                    // Iterate through the documents in the sub-collection
                    for (com.google.firebase.firestore.DocumentSnapshot document : queryDocumentSnapshots) {
                        // Convert each document to a SkillsItem object
                        ReferenceItem referenceItem = document.toObject(ReferenceItem.class);
                        if (referenceItem != null) {
                            referenceList.add(referenceItem);
                        }
                    }

                    // Set up the RecyclerView with the fetched data
                    if (!referenceList.isEmpty()) {
                        referencesAdapter = new ReferencesAdapter(this, referenceList);
                        resumeReferencesRecyclerView.setAdapter(referencesAdapter);
                        lizzieResumeReferencesRecyclerView.setAdapter(referencesAdapter);
                        jackResumeReferencesRecyclerView.setAdapter(referencesAdapter);
                        davidResumeReferencesRecyclerView.setAdapter(referencesAdapter);
                        dyrectionResumeReferencesRecyclerView.setAdapter(referencesAdapter);
                        jenniferResumeReferencesRecyclerView.setAdapter(referencesAdapter);
                        karenResumeReferencesRecyclerView.setAdapter(referencesAdapter);
                        kellyResumeReferencesRecyclerView.setAdapter(referencesAdapter);
                        khalilResumeReferencesRecyclerView.setAdapter(referencesAdapter);
                        lauraResumeReferencesRecyclerView.setAdapter(referencesAdapter);
                        noelResumeReferencesRecyclerView.setAdapter(referencesAdapter);
                        samuelResumeReferencesRecyclerView.setAdapter(referencesAdapter);
                        thomasResumeReferencesRecyclerView.setAdapter(referencesAdapter);
                        usmanResumeReferencesRecyclerView.setAdapter(referencesAdapter);
                    } else {

                        referenceTxtAndReferencesRecyclerViewHide();

                        Toast.makeText(this, "No skills data found.", Toast.LENGTH_SHORT).show();
                    }

                    hideProgressDialog();

                })
                .addOnFailureListener(e -> {

                    referenceTxtAndReferencesRecyclerViewHide();

                    Toast.makeText(this, "Error fetching skills data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });





    }

    private void referenceTxtAndReferencesRecyclerViewHide() {

        resumeReferencesRecyclerView.setVisibility(View.GONE);
        lizzieResumeReferencesRecyclerView.setVisibility(View.GONE);
        jackResumeReferencesRecyclerView.setVisibility(View.GONE);

        davidResumeReferencesRecyclerView.setVisibility(View.GONE);
        david_resume_references_line_view.setVisibility(View.GONE);

        dyrectionResumeReferencesRecyclerView.setVisibility(View.GONE);
        jenniferResumeReferencesRecyclerView.setVisibility(View.GONE);
        karenResumeReferencesRecyclerView.setVisibility(View.GONE);
        kellyResumeReferencesRecyclerView.setVisibility(View.GONE);
        khalilResumeReferencesRecyclerView.setVisibility(View.GONE);
        lauraResumeReferencesRecyclerView.setVisibility(View.GONE);
        noelResumeReferencesRecyclerView.setVisibility(View.GONE);
        samuelResumeReferencesRecyclerView.setVisibility(View.GONE);
        thomasResumeReferencesRecyclerView.setVisibility(View.GONE);
        usmanResumeReferencesRecyclerView.setVisibility(View.GONE);

        resume_references_txt.setVisibility(View.GONE);
        lizzie_resume_references_txt.setVisibility(View.GONE);
        jack_resume_references_txt.setVisibility(View.GONE);
        david_resume_references_txt.setVisibility(View.GONE);
        dyrection_resume_references_txt.setVisibility(View.GONE);
        jennifer_resume_references_txt.setVisibility(View.GONE);
        karen_resume_references_txt.setVisibility(View.GONE);
        kelly_resume_references_txt.setVisibility(View.GONE);
        khalil_resume_references_txt.setVisibility(View.GONE);
        laura_resume_references_txt.setVisibility(View.GONE);
        noel_resume_references_txt.setVisibility(View.GONE);
        samuel_resume_references_txt.setVisibility(View.GONE);
        thomas_resume_references_txt.setVisibility(View.GONE);
        usman_resume_references_txt.setVisibility(View.GONE);

        karen_resume_references_view.setVisibility(View.GONE);
    }


    private void setupExtraCurricular() {

        if (profileId == null || profileId.isEmpty()) {
            Toast.makeText(this, "Profile ID is missing.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Fetch the "skills" sub-collection from Firestore
        db.collection("All Resume Profiles")
                .document(profileId)
                .collection("volunteer_work")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<VolunteerWorkItem> volunteerWorkItemList = new ArrayList<>();

                    // Iterate through the documents in the sub-collection
                    for (com.google.firebase.firestore.DocumentSnapshot document : queryDocumentSnapshots) {
                        // Convert each document to a SkillsItem object
                        VolunteerWorkItem volunteerWorkItem = document.toObject(VolunteerWorkItem.class);
                        if (volunteerWorkItem != null) {
                            volunteerWorkItemList.add(volunteerWorkItem);
                        }
                    }

                    // Set up the RecyclerView with the fetched data
                    if (!volunteerWorkItemList.isEmpty()) {
                        volunteerWorkAdapter = new VolunteerWorkAdapter(this, volunteerWorkItemList);

                        resumeExtraCurricularRecyclerView.setAdapter(volunteerWorkAdapter);
                        lizzieResumeExtraCurricularRecyclerView.setAdapter(volunteerWorkAdapter);
                        jackResumeExtraCurricularRecyclerView.setAdapter(volunteerWorkAdapter);
                        davidResumeExtraCurricularRecyclerView.setAdapter(volunteerWorkAdapter);
                        dyrectionResumeExtraCurricularRecyclerView.setAdapter(volunteerWorkAdapter);
                        jenniferResumeExtraCurricularRecyclerView.setAdapter(volunteerWorkAdapter);
                        karenResumeExtraCurricularRecyclerView.setAdapter(volunteerWorkAdapter);
                        kellyResumeExtraCurricularRecyclerView.setAdapter(volunteerWorkAdapter);
                        khalilResumeExtraCurricularRecyclerView.setAdapter(volunteerWorkAdapter);
                        lauraResumeExtraCurricularRecyclerView.setAdapter(volunteerWorkAdapter);
                        noelResumeExtraCurricularRecyclerView.setAdapter(volunteerWorkAdapter);
                        samuelResumeExtraCurricularRecyclerView.setAdapter(volunteerWorkAdapter);
                        thomasResumeExtraCurricularRecyclerView.setAdapter(volunteerWorkAdapter);
                        usmanResumeExtraCurricularRecyclerView.setAdapter(volunteerWorkAdapter);



                    } else {

                        extracurricularTxtAndExtracurricularRecyclerviewHide();

                        Toast.makeText(this, "No Extracurricular data found.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {

                    extracurricularTxtAndExtracurricularRecyclerviewHide();

                    Toast.makeText(this, "Error fetching Extracurricular data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });



    }

    private void extracurricularTxtAndExtracurricularRecyclerviewHide() {

        resumeExtraCurricularRecyclerView.setVisibility(View.GONE);
        lizzieResumeExtraCurricularRecyclerView.setVisibility(View.GONE);
        jackResumeExtraCurricularRecyclerView.setVisibility(View.GONE);

        davidResumeExtraCurricularRecyclerView.setVisibility(View.GONE);
        david_resume_extra_curricular_line_view.setVisibility(View.GONE);

        dyrectionResumeExtraCurricularRecyclerView.setVisibility(View.GONE);
        jenniferResumeExtraCurricularRecyclerView.setVisibility(View.GONE);
        karenResumeExtraCurricularRecyclerView.setVisibility(View.GONE);
        kellyResumeExtraCurricularRecyclerView.setVisibility(View.GONE);
        khalilResumeExtraCurricularRecyclerView.setVisibility(View.GONE);
        lauraResumeExtraCurricularRecyclerView.setVisibility(View.GONE);
        noelResumeExtraCurricularRecyclerView.setVisibility(View.GONE);
        samuelResumeExtraCurricularRecyclerView.setVisibility(View.GONE);
        thomasResumeExtraCurricularRecyclerView.setVisibility(View.GONE);
        usmanResumeExtraCurricularRecyclerView.setVisibility(View.GONE);

        resume_extra_curricular_txt.setVisibility(View.GONE);
        lizzie_resume_extra_curricular_txt.setVisibility(View.GONE);
        jack_resume_extra_curricular_txt.setVisibility(View.GONE);
        david_resume_extra_curricular_txt.setVisibility(View.GONE);
        dyrection_resume_extra_curricular_txt.setVisibility(View.GONE);
        jennifer_resume_extra_curricular_txt.setVisibility(View.GONE);
        karen_resume_extra_curricular_txt.setVisibility(View.GONE);
        kelly_resume_extra_curricular_txt.setVisibility(View.GONE);
        khalil_resume_extra_curricular_txt.setVisibility(View.GONE);
        laura_resume_extra_curricular_txt.setVisibility(View.GONE);
        noel_resume_extra_curricular_txt.setVisibility(View.GONE);
        samuel_resume_extra_curricular_txt.setVisibility(View.GONE);
        thomas_resume_extra_curricular_txt.setVisibility(View.GONE);
        usman_resume_extra_curricular_txt.setVisibility(View.GONE);

        karen_resume_extra_curricular_view.setVisibility(View.GONE);


    }


    private void setupResumeAwards() {

        if (profileId == null || profileId.isEmpty()) {
            Toast.makeText(this, "Profile ID is missing.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Fetch the "skills" sub-collection from Firestore
        db.collection("All Resume Profiles")
                .document(profileId)
                .collection("awards_and_achievements")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<AwardItem> awardItemList = new ArrayList<>();

                    // Iterate through the documents in the sub-collection
                    for (com.google.firebase.firestore.DocumentSnapshot document : queryDocumentSnapshots) {
                        // Convert each document to a SkillsItem object
                        AwardItem awardItem = document.toObject(AwardItem.class);
                        if (awardItem != null) {
                            awardItemList.add(awardItem);
                        }
                    }

                    // Set up the RecyclerView with the fetched data
                    if (!awardItemList.isEmpty()) {
                        awardAdapter = new AwardAdapter(this, awardItemList);

                        resumeAwardsRecyclerView.setAdapter(awardAdapter);
                        dyrectionResumeAwardsRecyclerView.setAdapter(awardAdapter);
                        lizzieResumeAwardsRecyclerView.setAdapter(awardAdapter);
                        jackResumeAwardsRecyclerView.setAdapter(awardAdapter);
                        davidResumeAwardsRecyclerView.setAdapter(awardAdapter);
                        jenniferResumeAwardsRecyclerView.setAdapter(awardAdapter);
                        karenResumeAwardsRecyclerView.setAdapter(awardAdapter);
                        kellyResumeAwardsRecyclerView.setAdapter(awardAdapter);
                        khalilResumeAwardsRecyclerView.setAdapter(awardAdapter);
                        lauraResumeAwardsRecyclerView.setAdapter(awardAdapter);
                        noelResumeAwardsRecyclerView.setAdapter(awardAdapter);
                        samuelResumeAwardsRecyclerView.setAdapter(awardAdapter);
                        thomasResumeAwardsRecyclerView.setAdapter(awardAdapter);
                        usmanResumeAwardsRecyclerView.setAdapter(awardAdapter);


                    } else {

                        awardsTxtAndAwardsRecyclearViewHide();

                        Toast.makeText(this, "No Awards data found.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {

                    awardsTxtAndAwardsRecyclearViewHide();

                    Toast.makeText(this, "Error fetching Awards data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });



    }

    private void awardsTxtAndAwardsRecyclearViewHide() {


        resumeAwardsRecyclerView.setVisibility(View.GONE);
        lizzieResumeAwardsRecyclerView.setVisibility(View.GONE);
        jackResumeAwardsRecyclerView.setVisibility(View.GONE);

        davidResumeAwardsRecyclerView.setVisibility(View.GONE);
        david_resume_awards_line_view.setVisibility(View.GONE);

        dyrectionResumeAwardsRecyclerView.setVisibility(View.GONE);
        jenniferResumeAwardsRecyclerView.setVisibility(View.GONE);
        karenResumeAwardsRecyclerView.setVisibility(View.GONE);
        kellyResumeAwardsRecyclerView.setVisibility(View.GONE);
        khalilResumeAwardsRecyclerView.setVisibility(View.GONE);
        lauraResumeAwardsRecyclerView.setVisibility(View.GONE);
        noelResumeAwardsRecyclerView.setVisibility(View.GONE);
        samuelResumeAwardsRecyclerView.setVisibility(View.GONE);
        thomasResumeAwardsRecyclerView.setVisibility(View.GONE);
        usmanResumeAwardsRecyclerView.setVisibility(View.GONE);

        resume_award_txt.setVisibility(View.GONE);
        lizzie_resume_award_txt.setVisibility(View.GONE);
        jack_resume_award_txt.setVisibility(View.GONE);
        david_resume_awards_txt.setVisibility(View.GONE);
        dyrection_resume_awards_txt.setVisibility(View.GONE);
        jennifer_resume_awards_txt.setVisibility(View.GONE);
        karen_resume_awards_txt.setVisibility(View.GONE);
        kelly_resume_awards_txt.setVisibility(View.GONE);
        khalil_resume_awards_txt.setVisibility(View.GONE);
        laura_resume_awards_txt.setVisibility(View.GONE);
        noel_resume_awards_txt.setVisibility(View.GONE);
        samuel_resume_awards_txt.setVisibility(View.GONE);
        thomas_resume_awards_txt.setVisibility(View.GONE);
        usman_resume_awards_txt.setVisibility(View.GONE);

        karen_resume_award_view.setVisibility(View.GONE);
    }

    private void setupResumeProjects() {


        if (profileId == null || profileId.isEmpty()) {
            Toast.makeText(this, "Profile ID is missing.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Fetch the "skills" sub-collection from Firestore
        db.collection("All Resume Profiles")
                .document(profileId)
                .collection("projects_and_activities")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<ProjectItem> projectList = new ArrayList<>();

                    // Iterate through the documents in the sub-collection
                    for (com.google.firebase.firestore.DocumentSnapshot document : queryDocumentSnapshots) {
                        // Convert each document to a SkillsItem object
                        ProjectItem projectItem = document.toObject(ProjectItem.class);
                        if (projectItem != null) {
                            projectList.add(projectItem);
                        }
                    }

                    // Set up the RecyclerView with the fetched data
                    if (!projectList.isEmpty()) {
                        projectsAdapter = new ProjectsAdapter(this, projectList);
                        resumeProjectRecyclerView.setAdapter(projectsAdapter);
                        lizzieResumeProjectRecyclerView.setAdapter(projectsAdapter);
                        jackResumeProjectRecyclerView.setAdapter(projectsAdapter);
                        davidResumeProjectRecyclerView.setAdapter(projectsAdapter);
                        dyrectionResumeProjectRecyclerView.setAdapter(projectsAdapter);
                        jenniferResumeProjectRecyclerView.setAdapter(projectsAdapter);
                        karenResumeProjectRecyclerView.setAdapter(projectsAdapter);
                        kellyResumeProjectRecyclerView.setAdapter(projectsAdapter);
                        khalilResumeProjectRecyclerView.setAdapter(projectsAdapter);
                        lauraResumeProjectRecyclerView.setAdapter(projectsAdapter);
                        noelResumeProjectRecyclerView.setAdapter(projectsAdapter);
                        samuelResumeProjectRecyclerView.setAdapter(projectsAdapter);
                        thomasResumeProjectRecyclerView.setAdapter(projectsAdapter);
                        usmanResumeProjectRecyclerView.setAdapter(projectsAdapter);
                    } else {

                        projectTxtAndProjectRecyclearviewHide();

                        Toast.makeText(this, "No skills data found.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {

                    projectTxtAndProjectRecyclearviewHide();

                    Toast.makeText(this, "Error fetching skills data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });




    }

    private void projectTxtAndProjectRecyclearviewHide() {


        resumeProjectRecyclerView.setVisibility(View.GONE);
        lizzieResumeProjectRecyclerView.setVisibility(View.GONE);
        jackResumeProjectRecyclerView.setVisibility(View.GONE);

        davidResumeProjectRecyclerView.setVisibility(View.GONE);
        david_resume_project_line_view.setVisibility(View.GONE);

        dyrectionResumeProjectRecyclerView.setVisibility(View.GONE);
        jenniferResumeProjectRecyclerView.setVisibility(View.GONE);
        karenResumeProjectRecyclerView.setVisibility(View.GONE);
        kellyResumeProjectRecyclerView.setVisibility(View.GONE);
        khalilResumeProjectRecyclerView.setVisibility(View.GONE);
        lauraResumeProjectRecyclerView.setVisibility(View.GONE);
        noelResumeProjectRecyclerView.setVisibility(View.GONE);
        samuelResumeProjectRecyclerView.setVisibility(View.GONE);
        thomasResumeProjectRecyclerView.setVisibility(View.GONE);
        usmanResumeProjectRecyclerView.setVisibility(View.GONE);

        resume_project_txt.setVisibility(View.GONE);
        lizzie_resume_project_txt.setVisibility(View.GONE);
        jack_resume_project_txt.setVisibility(View.GONE);
        david_resume_project_txt.setVisibility(View.GONE);
        dyrection_resume_project_txt.setVisibility(View.GONE);
        jennifer_resume_project_txt.setVisibility(View.GONE);
        karen_resume_project_txt.setVisibility(View.GONE);
        kelly_resume_project_txt.setVisibility(View.GONE);
        khalil_resume_project_txt.setVisibility(View.GONE);
        laura_resume_project_txt.setVisibility(View.GONE);
        noel_resume_project_txt.setVisibility(View.GONE);
        samuel_resume_project_txt.setVisibility(View.GONE);
        thomas_resume_project_txt.setVisibility(View.GONE);
        usman_resume_project_txt.setVisibility(View.GONE);

        karen_resume_project_view.setVisibility(View.GONE);


    }

    private void setupResumeWorkExperience() {

        if (profileId == null || profileId.isEmpty()) {
            Toast.makeText(this, "Profile ID is missing.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Fetch the "skills" sub-collection from Firestore
        db.collection("All Resume Profiles")
                .document(profileId)
                .collection("work_experience")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<WorkExperienceItem> workExperienceList = new ArrayList<>();

                    // Iterate through the documents in the sub-collection
                    for (com.google.firebase.firestore.DocumentSnapshot document : queryDocumentSnapshots) {
                        // Convert each document to a SkillsItem object
                        WorkExperienceItem workExperienceItem = document.toObject(WorkExperienceItem.class);
                        if (workExperienceItem != null) {
                            workExperienceList.add(workExperienceItem);
                        }
                    }

                    // Set up the RecyclerView with the fetched data
                    if (!workExperienceList.isEmpty()) {
                        workExperienceAdapter = new WorkExperienceAdapter(this, workExperienceList);
                        resumeWorkExperienceRecyclerView.setAdapter(workExperienceAdapter);
                        lizzieResumeWorkExperienceRecyclerView.setAdapter(workExperienceAdapter);
                        jackResumeWorkExperienceRecyclerView.setAdapter(workExperienceAdapter);
                        davidResumeWorkExperienceRecyclerView.setAdapter(workExperienceAdapter);
                        dyrectionResumeWorkExperienceRecyclerView.setAdapter(workExperienceAdapter);
                        jenniferResumeWorkExperienceRecyclerView.setAdapter(workExperienceAdapter);
                        karenResumeWorkExperienceRecyclerView.setAdapter(workExperienceAdapter);
                        kellyResumeWorkExperienceRecyclerView.setAdapter(workExperienceAdapter);
                        khalilResumeWorkExperienceRecyclerView.setAdapter(workExperienceAdapter);
                        lauraResumeWorkExperienceRecyclerView.setAdapter(workExperienceAdapter);
                        noelResumeWorkExperienceRecyclerView.setAdapter(workExperienceAdapter);
                        samuelResumeWorkExperienceRecyclerView.setAdapter(workExperienceAdapter);
                        thomasResumeWorkExperienceRecyclerView.setAdapter(workExperienceAdapter);
                        usmanResumeWorkExperienceRecyclerView.setAdapter(workExperienceAdapter);
                    } else {

                        workExpreanceTxtAndWorkExpreanceRecyclerViewHide();

                        Toast.makeText(this, "No skills data found.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {

                    workExpreanceTxtAndWorkExpreanceRecyclerViewHide();

                    Toast.makeText(this, "Error fetching skills data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });



    }

    private void workExpreanceTxtAndWorkExpreanceRecyclerViewHide() {

        resumeWorkExperienceRecyclerView.setVisibility(View.GONE);
        lizzieResumeWorkExperienceRecyclerView.setVisibility(View.GONE);
        jackResumeWorkExperienceRecyclerView.setVisibility(View.GONE);

        davidResumeWorkExperienceRecyclerView.setVisibility(View.GONE);
        david_resume_workExperience_line_view.setVisibility(View.GONE);

        dyrectionResumeWorkExperienceRecyclerView.setVisibility(View.GONE);
        jenniferResumeWorkExperienceRecyclerView.setVisibility(View.GONE);
        karenResumeWorkExperienceRecyclerView.setVisibility(View.GONE);
        kellyResumeWorkExperienceRecyclerView.setVisibility(View.GONE);
        khalilResumeWorkExperienceRecyclerView.setVisibility(View.GONE);
        lauraResumeWorkExperienceRecyclerView.setVisibility(View.GONE);
        noelResumeWorkExperienceRecyclerView.setVisibility(View.GONE);
        samuelResumeWorkExperienceRecyclerView.setVisibility(View.GONE);
        thomasResumeWorkExperienceRecyclerView.setVisibility(View.GONE);
        usmanResumeWorkExperienceRecyclerView.setVisibility(View.GONE);

        resume_workExperience_txt.setVisibility(View.GONE);
        lizzie_resume_workExperience_txt.setVisibility(View.GONE);
        jack_resume_workExperience_txt.setVisibility(View.GONE);
        david_resume_work_experience_txt.setVisibility(View.GONE);
        dyrection_resume_work_experience_txt.setVisibility(View.GONE);
        jennifer_resume_work_experience_txt.setVisibility(View.GONE);
        karen_resume_work_experience_txt.setVisibility(View.GONE);
        kelly_resume_work_experience_txt.setVisibility(View.GONE);
        khalil_resume_work_experience_txt.setVisibility(View.GONE);
        laura_resume_work_experience_txt.setVisibility(View.GONE);
        noel_resume_work_experience_txt.setVisibility(View.GONE);
        samuel_resume_work_experience_txt.setVisibility(View.GONE);
        thomas_resume_work_experience_txt.setVisibility(View.GONE);
        usman_resume_work_experience_txt.setVisibility(View.GONE);

        karen_resume_work_experience_view.setVisibility(View.GONE);
    }

    private void setupResumeLanguage() {

        if (profileId == null || profileId.isEmpty()) {
            Toast.makeText(this, "Profile ID is missing.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Fetch the "skills" sub-collection from Firestore
        db.collection("All Resume Profiles")
                .document(profileId)
                .collection("languages_known")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<LanguageItem> languageList = new ArrayList<>();

                    // Iterate through the documents in the sub-collection
                    for (com.google.firebase.firestore.DocumentSnapshot document : queryDocumentSnapshots) {
                        // Convert each document to a SkillsItem object
                        LanguageItem languageItem = document.toObject(LanguageItem.class);
                        if (languageItem != null) {
                            languageList.add(languageItem);
                        }
                    }

                    // Set up the RecyclerView with the fetched data
                    if (!languageList.isEmpty()) {
                        languagesAdapter = new LanguagesAdapter(this, languageList);
                        resumeLanguageRecyclerView.setAdapter(languagesAdapter);
                        lizzieResumeLanguageRecyclerView.setAdapter(languagesAdapter);
                        jackResumeLanguageRecyclerView.setAdapter(languagesAdapter);
                        davidResumeLanguageRecyclerView.setAdapter(languagesAdapter);
                        dyrectionResumeLanguageRecyclerView.setAdapter(languagesAdapter);
                        jenniferResumeLanguageRecyclerView.setAdapter(languagesAdapter);
                        karenResumeLanguageRecyclerView.setAdapter(languagesAdapter);
                        kellyResumeLanguageRecyclerView.setAdapter(languagesAdapter);
                        khalilResumeLanguageRecyclerView.setAdapter(languagesAdapter);
                        lauraResumeLanguageRecyclerView.setAdapter(languagesAdapter);
                        noelResumeLanguageRecyclerView.setAdapter(languagesAdapter);
                        samuelResumeLanguageRecyclerView.setAdapter(languagesAdapter);
                        thomasResumeLanguageRecyclerView.setAdapter(languagesAdapter);
                        usmanResumeLanguageRecyclerView.setAdapter(languagesAdapter);
                    } else {

                        languageTxtAndLanguageRecyclerViewHide();

                        Toast.makeText(this, "No skills data found.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {

                    languageTxtAndLanguageRecyclerViewHide();

                    Toast.makeText(this, "Error fetching skills data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });


    }

    private void languageTxtAndLanguageRecyclerViewHide() {


        resumeLanguageRecyclerView.setVisibility(View.GONE);
        lizzieResumeLanguageRecyclerView.setVisibility(View.GONE);
        jackResumeLanguageRecyclerView.setVisibility(View.GONE);

        davidResumeLanguageRecyclerView.setVisibility(View.GONE);
        david_resume_language_line_view.setVisibility(View.GONE);

        dyrectionResumeLanguageRecyclerView.setVisibility(View.GONE);
        jenniferResumeLanguageRecyclerView.setVisibility(View.GONE);
        karenResumeLanguageRecyclerView.setVisibility(View.GONE);
        kellyResumeLanguageRecyclerView.setVisibility(View.GONE);
        khalilResumeLanguageRecyclerView.setVisibility(View.GONE);
        lauraResumeLanguageRecyclerView.setVisibility(View.GONE);
        noelResumeLanguageRecyclerView.setVisibility(View.GONE);
        samuelResumeLanguageRecyclerView.setVisibility(View.GONE);
        thomasResumeLanguageRecyclerView.setVisibility(View.GONE);
        usmanResumeLanguageRecyclerView.setVisibility(View.GONE);

        resume_language_txt.setVisibility(View.GONE);
        lizzie_resume_language_txt.setVisibility(View.GONE);
        jack_resume_language_txt.setVisibility(View.GONE);
        david_resume_language_txt.setVisibility(View.GONE);
        dyrection_resume_language_txt.setVisibility(View.GONE);
        jennifer_resume_language_txt.setVisibility(View.GONE);
        karen_resume_language_txt.setVisibility(View.GONE);
        kelly_resume_language_txt.setVisibility(View.GONE);
        khalil_resume_language_txt.setVisibility(View.GONE);
        laura_resume_language_txt.setVisibility(View.GONE);
        noel_resume_language_txt.setVisibility(View.GONE);
        samuel_resume_language_txt.setVisibility(View.GONE);
        thomas_resume_language_txt.setVisibility(View.GONE);
        usman_resume_language_txt.setVisibility(View.GONE);

        karen_resume_language_view.setVisibility(View.GONE);
    }

    private void setupResumeExpertise() {
        if (profileId == null || profileId.isEmpty()) {
            Toast.makeText(this, "Profile ID is missing.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Fetch the "skills" sub-collection from Firestore
        db.collection("All Resume Profiles")
                .document(profileId)
                .collection("skills")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<SkillsItem> skillsList = new ArrayList<>();

                    // Iterate through the documents in the sub-collection
                    for (com.google.firebase.firestore.DocumentSnapshot document : queryDocumentSnapshots) {
                        // Convert each document to a SkillsItem object
                        SkillsItem skillsItem = document.toObject(SkillsItem.class);
                        if (skillsItem != null) {
                            skillsList.add(skillsItem);
                        }
                    }

                    // Set up the RecyclerView with the fetched data
                    if (!skillsList.isEmpty()) {
                        skillsAdapter = new SkillsAdapter(this, skillsList);
                        resumeExpertiseRecyclerView.setAdapter(skillsAdapter);
                        lizzieResumeExpertiseRecyclerView.setAdapter(skillsAdapter);
                        jackResumeExpertiseRecyclerView.setAdapter(skillsAdapter);
                        davidResumeExpertiseRecyclerView.setAdapter(skillsAdapter);
                        dyrectionResumeExpertiseRecyclerView.setAdapter(skillsAdapter);
                        jenniferResumeExpertiseRecyclerView.setAdapter(skillsAdapter);
                        karenResumeExpertiseRecyclerView.setAdapter(skillsAdapter);
                        kellyResumeExpertiseRecyclerView.setAdapter(skillsAdapter);
                        khalilResumeExpertiseRecyclerView.setAdapter(skillsAdapter);
                        lauraResumeExpertiseRecyclerView.setAdapter(skillsAdapter);
                        noelResumeExpertiseRecyclerView.setAdapter(skillsAdapter);
                        samuelResumeExpertiseRecyclerView.setAdapter(skillsAdapter);
                        thomasResumeExpertiseRecyclerView.setAdapter(skillsAdapter);
                        usmanResumeExpertiseRecyclerView.setAdapter(skillsAdapter);
                    } else {

                        expertiseTxtAndExpertiseRecyclerViewHide();

                        Toast.makeText(this, "No skills data found.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {

                    expertiseTxtAndExpertiseRecyclerViewHide();

                    Toast.makeText(this, "Error fetching skills data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void expertiseTxtAndExpertiseRecyclerViewHide() {



        resumeExpertiseRecyclerView.setVisibility(View.GONE);
        lizzieResumeExpertiseRecyclerView.setVisibility(View.GONE);
        jackResumeExpertiseRecyclerView.setVisibility(View.GONE);

        davidResumeExpertiseRecyclerView.setVisibility(View.GONE);
        david_resume_expertise_line_view.setVisibility(View.GONE);

        dyrectionResumeExpertiseRecyclerView.setVisibility(View.GONE);
        jenniferResumeExpertiseRecyclerView.setVisibility(View.GONE);
        karenResumeExpertiseRecyclerView.setVisibility(View.GONE);
        kellyResumeExpertiseRecyclerView.setVisibility(View.GONE);
        khalilResumeExpertiseRecyclerView.setVisibility(View.GONE);
        lauraResumeExpertiseRecyclerView.setVisibility(View.GONE);
        noelResumeExpertiseRecyclerView.setVisibility(View.GONE);
        samuelResumeExpertiseRecyclerView.setVisibility(View.GONE);
        thomasResumeExpertiseRecyclerView.setVisibility(View.GONE);
        usmanResumeExpertiseRecyclerView.setVisibility(View.GONE);
        resume_expertise_txt.setVisibility(View.GONE);
        lizzie_resume_expertise_txt.setVisibility(View.GONE);
        jack_resume_expertise_txt.setVisibility(View.GONE);
        david_resume_expertise_txt.setVisibility(View.GONE);
        dyrection_resume_expertise_txt.setVisibility(View.GONE);
        jennifer_resume_expertise_txt.setVisibility(View.GONE);
        karen_resume_expertise_txt.setVisibility(View.GONE);
        kelly_resume_expertise_txt.setVisibility(View.GONE);
        khalil_resume_expertise_txt.setVisibility(View.GONE);
        laura_resume_expertise_txt.setVisibility(View.GONE);
        noel_resume_expertise_txt.setVisibility(View.GONE);
        samuel_resume_expertise_txt.setVisibility(View.GONE);
        thomas_resume_expertise_txt.setVisibility(View.GONE);
        usman_resume_expertise_txt.setVisibility(View.GONE);

        karen_resume_expertise_view.setVisibility(View.GONE);

    }


    private void setupResumeCertifications() {
        if (profileId == null || profileId.isEmpty()) {
            Toast.makeText(this, "Profile ID is missing.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Fetch the "certifications_and_training" sub-collection from Firestore
        db.collection("All Resume Profiles")
                .document(profileId)
                .collection("certifications_and_training")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<CertificationItem> certificationList = new ArrayList<>();

                    // Iterate through the documents in the sub-collection
                    for (com.google.firebase.firestore.DocumentSnapshot document : queryDocumentSnapshots) {
                        // Convert each document to a CertificationItem object
                        CertificationItem certificationItem = document.toObject(CertificationItem.class);
                        if (certificationItem != null) {
                            certificationList.add(certificationItem);
                        }
                    }

                    // Set up the RecyclerView with the fetched data
                    if (!certificationList.isEmpty()) {
                        certificationsAdapter = new CertificationsAdapter(this, certificationList);
                        resumeCertificationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                        resumeCertificationsRecyclerView.setAdapter(certificationsAdapter);
                        lizzieResumeCertificationsRecyclerView.setAdapter(certificationsAdapter);
                        jackResumeCertificationsRecyclerView.setAdapter(certificationsAdapter);
                        davidResumeCertificationsRecyclerView.setAdapter(certificationsAdapter);
                        dyrectionResumeCertificationsRecyclerView.setAdapter(certificationsAdapter);
                        jenniferResumeCertificationsRecyclerView.setAdapter(certificationsAdapter);
                        karenResumeCertificationsRecyclerView.setAdapter(certificationsAdapter);
                        kellyResumeCertificationsRecyclerView.setAdapter(certificationsAdapter);
                        khalilResumeCertificationsRecyclerView.setAdapter(certificationsAdapter);
                        lauraResumeCertificationsRecyclerView.setAdapter(certificationsAdapter);
                        noelResumeCertificationsRecyclerView.setAdapter(certificationsAdapter);
                        samuelResumeCertificationsRecyclerView.setAdapter(certificationsAdapter);
                        thomasResumeCertificationsRecyclerView.setAdapter(certificationsAdapter);
                        usmanResumeCertificationsRecyclerView.setAdapter(certificationsAdapter);
                    } else {

                        certificationTxtAndCertificationRecyclerViewHide();

                        Toast.makeText(this, "No certifications data found.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {

                    certificationTxtAndCertificationRecyclerViewHide();

                    Toast.makeText(this, "Error fetching certifications data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void certificationTxtAndCertificationRecyclerViewHide() {


        resumeCertificationsRecyclerView.setVisibility(View.GONE);
        lizzieResumeCertificationsRecyclerView.setVisibility(View.GONE);
        jackResumeCertificationsRecyclerView.setVisibility(View.GONE);

        davidResumeCertificationsRecyclerView.setVisibility(View.GONE);
        david_resume_certifications_line_view.setVisibility(View.GONE);

        dyrectionResumeCertificationsRecyclerView.setVisibility(View.GONE);
        jenniferResumeCertificationsRecyclerView.setVisibility(View.GONE);
        karenResumeCertificationsRecyclerView.setVisibility(View.GONE);
        kellyResumeCertificationsRecyclerView.setVisibility(View.GONE);
        khalilResumeCertificationsRecyclerView.setVisibility(View.GONE);
        lauraResumeCertificationsRecyclerView.setVisibility(View.GONE);
        noelResumeCertificationsRecyclerView.setVisibility(View.GONE);
        samuelResumeCertificationsRecyclerView.setVisibility(View.GONE);
        thomasResumeCertificationsRecyclerView.setVisibility(View.GONE);
        usmanResumeCertificationsRecyclerView.setVisibility(View.GONE);
        resume_certification_txt.setVisibility(View.GONE);
        lizzie_resume_certifications_txt.setVisibility(View.GONE);
        jack_resume_certifications_txt.setVisibility(View.GONE);
        david_resume_certifications_txt.setVisibility(View.GONE);
        dyrection_resume_certifications_txt.setVisibility(View.GONE);
        jennifer_resume_certifications_txt.setVisibility(View.GONE);
        karen_resume_certifications_txt.setVisibility(View.GONE);
        kelly_resume_certifications_txt.setVisibility(View.GONE);
        khalil_resume_certifications_txt.setVisibility(View.GONE);
        laura_resume_certifications_txt.setVisibility(View.GONE);
        noel_resume_certifications_txt.setVisibility(View.GONE);
        samuel_resume_certifications_txt.setVisibility(View.GONE);
        thomas_resume_certifications_txt.setVisibility(View.GONE);
        usman_resume_certifications_txt.setVisibility(View.GONE);

        karen_resume_certifications_view.setVisibility(View.GONE);

    }

    private void setupResumeEducation() {
        if (profileId == null || profileId.isEmpty()) {
            Toast.makeText(this, "Profile ID is missing.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Fetch the "educational_qualifications" sub-collection from Firestore
        db.collection("All Resume Profiles")
                .document(profileId)
                .collection("educational_qualifications")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<EducationItem> educationList = new ArrayList<>();

                    // Iterate through the documents in the sub-collection
                    for (com.google.firebase.firestore.DocumentSnapshot document : queryDocumentSnapshots) {
                        // Convert each document to an EducationItem object
                        EducationItem educationItem = document.toObject(EducationItem.class);
                        if (educationItem != null) {
                            educationList.add(educationItem);
                        }
                    }

                    // Set up the RecyclerView with the fetched data
                    if (!educationList.isEmpty()) {
                        educationAdapter = new EducationAdapter(this, educationList);
                        resumeEducationRecyclerView.setAdapter(educationAdapter);
                        lizzieResumeEducationRecyclerView.setAdapter(educationAdapter);
                        jackResumeEducationRecyclerView.setAdapter(educationAdapter);
                        davidResumeEducationRecyclerView.setAdapter(educationAdapter);
                        dyrectionResumeEducationRecyclerView.setAdapter(educationAdapter);
                        jenniferResumeEducationRecyclerView.setAdapter(educationAdapter);
                        karenResumeEducationRecyclerView.setAdapter(educationAdapter);
                        kellyResumeEducationRecyclerView.setAdapter(educationAdapter);
                        khalilResumeEducationRecyclerView.setAdapter(educationAdapter);
                        lauraResumeEducationRecyclerView.setAdapter(educationAdapter);
                        noelResumeEducationRecyclerView.setAdapter(educationAdapter);
                        samuelResumeEducationRecyclerView.setAdapter(educationAdapter);
                        thomasResumeEducationRecyclerView.setAdapter(educationAdapter);
                        usmanResumeEducationRecyclerView.setAdapter(educationAdapter);
                    } else {

                        educationTxtAndEducationRecyclearViewHide();


                        Toast.makeText(this, "No education data found.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    educationTxtAndEducationRecyclearViewHide();
                    Toast.makeText(this, "Error fetching education data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void educationTxtAndEducationRecyclearViewHide() {




        resumeEducationRecyclerView.setVisibility(View.GONE);
        lizzieResumeEducationRecyclerView.setVisibility(View.GONE);
        jackResumeEducationRecyclerView.setVisibility(View.GONE);

        davidResumeEducationRecyclerView.setVisibility(View.GONE);
        david_resume_education_line_view.setVisibility(View.GONE);

        dyrectionResumeEducationRecyclerView.setVisibility(View.GONE);
        jenniferResumeEducationRecyclerView.setVisibility(View.GONE);
        karenResumeEducationRecyclerView.setVisibility(View.GONE);
        kellyResumeEducationRecyclerView.setVisibility(View.GONE);
        khalilResumeEducationRecyclerView.setVisibility(View.GONE);
        lauraResumeEducationRecyclerView.setVisibility(View.GONE);
        noelResumeEducationRecyclerView.setVisibility(View.GONE);
        samuelResumeEducationRecyclerView.setVisibility(View.GONE);
        thomasResumeEducationRecyclerView.setVisibility(View.GONE);
        usmanResumeEducationRecyclerView.setVisibility(View.GONE);
        resume_education_txt.setVisibility(View.GONE);
        lizzie_resume_education_txt.setVisibility(View.GONE);
        jack_resume_education_txt.setVisibility(View.GONE);
        david_resume_education_txt.setVisibility(View.GONE);
        dyrection_resume_education_txt.setVisibility(View.GONE);
        jennifer_resume_education_txt.setVisibility(View.GONE);
        karen_resume_education_txt.setVisibility(View.GONE);
        kelly_resume_education_txt.setVisibility(View.GONE);
        khalil_resume_education_txt.setVisibility(View.GONE);
        laura_resume_education_txt.setVisibility(View.GONE);
        noel_resume_education_txt.setVisibility(View.GONE);
        samuel_resume_education_txt.setVisibility(View.GONE);
        thomas_resume_education_txt.setVisibility(View.GONE);
        usman_resume_education_txt.setVisibility(View.GONE);

        karen_resume_education_view.setVisibility(View.GONE);

    }

    private void setupResumeProfileData() {
        if (profileId == null || profileId.isEmpty()) {
            Toast.makeText(this, "Profile ID is missing.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Fetch the document from Firestore
        db.collection("All Resume Profiles")
                .document(profileId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Retrieve data from the document
                        String headshotUrl = documentSnapshot.getString("headshotUrl");
                        String fullName = documentSnapshot.getString("fullName");
                        String position = documentSnapshot.getString("position");
                        String email = documentSnapshot.getString("email");
                        String phone = documentSnapshot.getString("phone");
                        String address = documentSnapshot.getString("address");
                        String linkedin = documentSnapshot.getString("linkedin");
                        String portfolio = documentSnapshot.getString("portfolio");
                        String socialLink = documentSnapshot.getString("socialLink");
                        String resumeId = documentSnapshot.getString("resumeId");



                        // Update the UI with the retrieved data
                        updateUIWithProfileData(headshotUrl, fullName, position, email, phone, address, linkedin, portfolio, socialLink);
                    } else {
                        Toast.makeText(this, "Profile not found.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error fetching profile: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void updateUIWithProfileData(String headshotUrl, String fullName, String position, String email, String phone, String address, String linkedin, String portfolio, String socialLink) {
        // Update the UI components with the retrieved data
        if (headshotUrl != null && !headshotUrl.isEmpty()) {
            // Load the profile image using a library like Glide or Picasso
            // Example using Glide:
            // Glide.with(this).load(headshotUrl).into(resumeProfileImg);

            if (headshotUrl != null){

                Glide.with(this).load(headshotUrl).into(resumeProfileImg);
                Glide.with(this).load(headshotUrl).into(samuelProfileImage);
                Glide.with(this).load(headshotUrl).into(jenniferResumeProfileImg);
                Glide.with(this).load(headshotUrl).into(dyrectionResumeProfileImg);
                Glide.with(this).load(headshotUrl).into(karenResumeProfileImg);
                Glide.with(this).load(headshotUrl).into(thomasProfileImage);
                Glide.with(this).load(headshotUrl).into(lizzieResumeProfileImg);
                Glide.with(this).load(headshotUrl).into(kellyCircleImageView);
                Glide.with(this).load(headshotUrl).into(davidResumeProfileImg);
                Glide.with(this).load(headshotUrl).into(usmanResumeProfileImg);
                Glide.with(this).load(headshotUrl).into(lauraResumeProfileImg);
                Glide.with(this).load(headshotUrl).into(noelResumeProfileImg);
                Glide.with(this).load(headshotUrl).into(khalilCircleImageView);


            }



        }

        if (fullName != null) {
            resumeName=fullName;

            resumeFullName.setText(fullName);
            lizzieResumeFullName.setText(fullName);
            jackResumeFullName.setText(fullName);
            davidResumeFullName.setText(fullName);
            dyrectionResumeFullName.setText(fullName);
            jenniferResumeFullName.setText(fullName);
            karenResumeFullName.setText(fullName);
            kellyResumeFullName.setText(fullName);
            khalilResumeFullName.setText(fullName);
            lauraResumeFullName.setText(fullName);
            noelResumeFullName.setText(fullName);
            samuelResumeFullName.setText(fullName);
            thomasResumeFullName.setText(fullName);
            usmanResumeFullName.setText(fullName);

        }

        if (position != null) {
            resumePosition.setText(position);
            lizzieResumePosition.setText(position);
            jackResumePosition.setText(position);
            davidResumePosition.setText(position);
            dyrectionResumePosition.setText(position);
            jenniferResumePosition.setText(position);
            karenResumePosition.setText(position);
            kellyResumePosition.setText(position);
            khalilResumePosition.setText(position);
            lauraResumePosition.setText(position);
            noelResumePosition.setText(position);
            samuelResumePosition.setText(position);
            thomasResumePosition.setText(position);
            usmanResumePosition.setText(position);
        }

        if (email != null) {
            resumeEmail.setText(email);
            lizzieResumeEmail.setText(email);
            jackResumeEmail.setText(email);
            davidResumeEmail.setText(email);
            dyrectionResumeEmail.setText(email);
            jenniferResumeEmail.setText(email);
            karenResumeEmail.setText(email);
            kellyResumeEmail.setText(email);
            khalilResumeEmail.setText(email);
            lauraResumeEmail.setText(email);
            noelResumeEmail.setText(email);
            samuelResumeEmail.setText(email);
            thomasResumeEmail.setText(email);
            usmanResumeEmail.setText(email);
        }

        if (phone != null) {
            resumePhoneNumber.setText(phone);
            lizzieResumePhoneNumber.setText(phone);
            jackResumePhoneNumber.setText(phone);
            davidResumePhoneNumber.setText(phone);
            dyrectionResumePhoneNumber.setText(phone);
            jenniferResumePhoneNumber.setText(phone);
            karenResumePhoneNumber.setText(phone);
            kellyResumePhoneNumber.setText(phone);
            khalilResumePhoneNumber.setText(phone);
            lauraResumePhoneNumber.setText(phone);
            noelResumePhoneNumber.setText(phone);
            samuelResumePhoneNumber.setText(phone);
            thomasResumePhoneNumber.setText(phone);
            usmanResumePhoneNumber.setText(phone);
        }

        if (address != null) {
            resumeAddress.setText(address);
            lizzieResumeAddress.setText(address);
            jackResumeAddress.setText(address);
            davidResumeAddress.setText(address);
            dyrectionResumeAddress.setText(address);
            jenniferResumeAddress.setText(address);
            karenResumeAddress.setText(address);
            kellyResumeAddress.setText(address);
            khalilResumeAddress.setText(address);
            lauraResumeAddress.setText(address);
            noelResumeAddress.setText(address);
            samuelResumeAddress.setText(address);
            thomasResumeAddress.setText(address);
            usmanResumeAddress.setText(address);
        }

        if (linkedin != null) {
            // If you have a TextView for LinkedIn, update it here
        }

        if (portfolio != null) {
            resumeWebsite.setText(portfolio);
            lizzieResumeWebsite.setText(portfolio);
            jackResumeWebsite.setText(portfolio);
            davidResumeWebsite.setText(portfolio);
            dyrectionResumeWebsite.setText(portfolio);
            jenniferResumeWebsite.setText(portfolio);
            karenResumeWebsite.setText(portfolio);
            kellyResumeWebsite.setText(portfolio);
            khalilResumeWebsite.setText(portfolio);
            lauraResumeWebsite.setText(portfolio);
            noelResumeWebsite.setText(portfolio);
            samuelResumeWebsite.setText(portfolio);
            usmanResumeWebsite.setText(portfolio);
        }

        if (socialLink != null) {
            // If you have a TextView for social links, update it here
        }
    }



    private void initializeViews() {

        template_usman_ali = findViewById(R.id.template_usman_ali);
        template_thomas_smith = findViewById(R.id.template_thomas_smith);
        template_samuel_white = findViewById(R.id.template_samuel_white);
        template_noel_taylor = findViewById(R.id.template_noel_taylor);
        template_laura_parker = findViewById(R.id.template_laura_parker);
        template_khalil_richardson = findViewById(R.id.template_khalil_richardson);
        template_kelly_white = findViewById(R.id.template_kelly_white);
        template_karen_richards = findViewById(R.id.template_karen_richards);
        template_jennifer_anderson = findViewById(R.id.template_jennifer_anderson);
        template_dyrection_pesh = findViewById(R.id.template_dyrection_pesh);
        template_david_anderson = findViewById(R.id.template_david_anderson);
        template_jack_william = findViewById(R.id.template_jack_william);
        template_olivia_wilson = findViewById(R.id.template_olivia_wilson);
        template_lizzie = findViewById(R.id.template_lizzie);
        main_template_layout = findViewById(R.id.main_template_layout);
        backButton = findViewById(R.id.back_btn);
        shareButton = findViewById(R.id.share_btn);

        shareButton.setOnClickListener(v -> showBottomSheet());
    }

    private void showBottomSheet() {
        // Inflate the BottomSheet layout
        View bottomSheetView = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_share_export, null);

        // Create the BottomSheet dialog
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();

        // Handle Share option
        TextView tvShare = bottomSheetView.findViewById(R.id.tv_share);
        tvShare.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            shareResume();
        });

        // Handle Export option
        TextView tvExport = bottomSheetView.findViewById(R.id.tv_export);
        tvExport.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            checkStoragePermissionAndProceed();
        });
    }

    private void exportResumeToPdf() {
        // Measure the full content size
        View contentView = main_template_layout;
        contentView.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        );

        int contentWidth = contentView.getMeasuredWidth();
        int contentHeight = contentView.getMeasuredHeight();

        if (contentWidth == 0 || contentHeight == 0) {
            Toast.makeText(this, "Error: View is not properly measured!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Define margins
        int margin = 72;
        if (templateName.equals("lizzie_johan") || templateName.equals("david_anderson") ||
                templateName.equals("dyrection_pesh") || templateName.equals("jennifer_anderson") ||
                templateName.equals("karen_richards") || templateName.equals("kelly_white") ||
                templateName.equals("khalil_richardson") || templateName.equals("noel_taylor") ||
                templateName.equals("samuel_white")) {
            margin = 0;
        }

        // Scale factor for HD quality (2x or 3x for better quality)
        float scaleFactor = 3.0f;

        int scaledContentWidth = (int) (contentWidth * scaleFactor);
        int scaledContentHeight = (int) (contentHeight * scaleFactor);
        int scaledMargin = (int) (margin * scaleFactor);

        int pageWidth = contentWidth + (2 * margin);
        int pageHeight = contentHeight + (2 * margin);

        // Create PDF document
        android.graphics.pdf.PdfDocument document = new android.graphics.pdf.PdfDocument();

        try {
            // Create high-resolution bitmap
            Bitmap bitmap = Bitmap.createBitmap(scaledContentWidth, scaledContentHeight, Bitmap.Config.ARGB_8888);
            Canvas bitmapCanvas = new Canvas(bitmap);

            // Scale the canvas for HD rendering
            bitmapCanvas.scale(scaleFactor, scaleFactor);

            // Set anti-aliasing and other quality flags
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setFilterBitmap(true);
            paint.setDither(true);
            bitmapCanvas.drawPaint(paint);

            // Layout and draw the content to the scaled bitmap
            contentView.layout(0, 0, contentWidth, contentHeight);
            contentView.draw(bitmapCanvas);

            // Create PDF page
            android.graphics.pdf.PdfDocument.PageInfo pageInfo =
                    new android.graphics.pdf.PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();
            android.graphics.pdf.PdfDocument.Page page = document.startPage(pageInfo);
            Canvas pageCanvas = page.getCanvas();

            // Enable anti-aliasing on PDF canvas
            Paint pdfPaint = new Paint();
            pdfPaint.setAntiAlias(true);
            pdfPaint.setFilterBitmap(true);
            pdfPaint.setDither(true);

            // Create a destination rect for proper scaling
            Rect srcRect = new Rect(0, 0, scaledContentWidth, scaledContentHeight);
            Rect dstRect = new Rect(margin, margin, contentWidth + margin, contentHeight + margin);

            // Draw the high-resolution bitmap scaled down to fit properly
            pageCanvas.drawBitmap(bitmap, srcRect, dstRect, pdfPaint);

            // Finish page
            document.finishPage(page);

            // Create QuickResume directory
            File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            File quickResumeDir = new File(storageDir, "QuickResume");

            if (!quickResumeDir.exists()) {
                boolean dirCreated = quickResumeDir.mkdirs();
                if (!dirCreated) {
                    Toast.makeText(this, "Failed to create QuickResume directory", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            // Save PDF
            String fileName = resumeName + "_Resume_HD_" + System.currentTimeMillis() + ".pdf";
            File pdfFile = new File(quickResumeDir, fileName);

            FileOutputStream fos = new FileOutputStream(pdfFile);
            document.writeTo(fos);
            document.close();
            fos.close();

            // Clean up bitmap
            bitmap.recycle();

            Toast.makeText(this, "HD Resume saved to QuickResume folder", Toast.LENGTH_LONG).show();
            updateTemplateScore(templateName);
            openPdf(pdfFile);
            finish();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error saving PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            Toast.makeText(this, "Out of memory. Try reducing content size.", Toast.LENGTH_SHORT).show();
        } finally {
            if (document != null) {
                document.close();
            }
        }
    }

    // Alternative method using vector-based rendering (even better quality)
    private void exportResumeToVectorPdf() {
        View contentView = main_template_layout;
        contentView.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        );

        int contentWidth = contentView.getMeasuredWidth();
        int contentHeight = contentView.getMeasuredHeight();

        if (contentWidth == 0 || contentHeight == 0) {
            Toast.makeText(this, "Error: View is not properly measured!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Define margins
        int margin = 72;
        if (templateName.equals("lizzie_johan") || templateName.equals("david_anderson") ||
                templateName.equals("dyrection_pesh") || templateName.equals("jennifer_anderson") ||
                templateName.equals("karen_richards") || templateName.equals("kelly_white") ||
                templateName.equals("khalil_richardson") || templateName.equals("noel_taylor") ||
                templateName.equals("samuel_white")) {
            margin = 0;
        }

        int pageWidth = contentWidth + (2 * margin);
        int pageHeight = contentHeight + (2 * margin);

        // Create PDF document
        android.graphics.pdf.PdfDocument document = new android.graphics.pdf.PdfDocument();

        try {
            // Create PDF page
            android.graphics.pdf.PdfDocument.PageInfo pageInfo =
                    new android.graphics.pdf.PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();
            android.graphics.pdf.PdfDocument.Page page = document.startPage(pageInfo);
            Canvas pageCanvas = page.getCanvas();

            // Enable high-quality rendering
            pageCanvas.setDensity(DisplayMetrics.DENSITY_XXHIGH);

            // Set paint for anti-aliasing
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setSubpixelText(true);
            paint.setLinearText(true);
            paint.setFilterBitmap(true);

            // Translate canvas for margin
            pageCanvas.translate(margin, margin);

            // Layout and draw directly to PDF canvas (vector-based)
            contentView.layout(0, 0, contentWidth, contentHeight);
            contentView.draw(pageCanvas);

            // Finish page
            document.finishPage(page);

            // Create QuickResume directory
            File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            File quickResumeDir = new File(storageDir, "QuickResume");

            if (!quickResumeDir.exists()) {
                boolean dirCreated = quickResumeDir.mkdirs();
                if (!dirCreated) {
                    Toast.makeText(this, "Failed to create QuickResume directory", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            // Save PDF
            String fileName = resumeName + "_Resume_Vector_" + System.currentTimeMillis() + ".pdf";
            File pdfFile = new File(quickResumeDir, fileName);

            FileOutputStream fos = new FileOutputStream(pdfFile);
            document.writeTo(fos);
            document.close();
            fos.close();

            Toast.makeText(this, "Vector Resume saved to QuickResume folder", Toast.LENGTH_LONG).show();
            updateTemplateScore(templateName);
            openPdf(pdfFile);
            finish();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error saving PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            if (document != null) {
                document.close();
            }
        }
    }

    // টেমপ্লেট স্কোর আপডেট করার ফাংশন
    // টেমপ্লেট স্কোর আপডেট করার ফাংশন
    private void updateTemplateScore(String templateName) {
        // টেমপ্লেট নামের উপর ভিত্তি করে কুয়েরি করা
        CollectionReference templatesRef = db.collection("Templates");
        Query query = templatesRef.whereEqualTo("templateName", templateName);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                    // টেমপ্লেট আগে থেকেই আছে, আপডেট করা
                    DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                    DocumentReference templateRef = documentSnapshot.getReference();

                    // ট্রানজ্যাকশন ব্যবহার করে স্কোর আপডেট করা
                    db.runTransaction(transaction -> {
                        DocumentSnapshot snapshot = transaction.get(templateRef);
                        long currentScore = 0;

                        if (snapshot.contains("template_score")) {
                            currentScore = snapshot.getLong("template_score");
                        }

                        // স্কোর বাড়ানো
                        currentScore += 1;

                        // ডকুমেন্ট আপডেট করা
                        transaction.update(templateRef, "template_score", currentScore);
                        return null;
                    }).addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Template score updated successfully", Toast.LENGTH_SHORT).show();
                    }).addOnFailureListener(e -> {
                        Toast.makeText(this, "Error updating template score: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                } else {
                    // নতুন টেমপ্লেট তৈরি করা
                    Map<String, Object> templateData = new HashMap<>();
                    templateData.put("templateName", templateName);
                    templateData.put("template_score", 1);

                    DocumentReference newTemplateRef = templatesRef.document();
                    newTemplateRef.set(templateData)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(this, "New template created with score: 1", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Error creating new template: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                }
            } else {
                Toast.makeText(this, "Error querying template: " + task.getException(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    // Method to open PDF
    private void openPdf(File file) {
        Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No PDF viewer found!", Toast.LENGTH_SHORT).show();
        }
    }





    private void shareResume() {
        String fileName = "Resume_" + System.currentTimeMillis() + ".pdf";
        File pdfFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);

        if (!pdfFile.exists()) {
            // First create the PDF if it doesn't exist
            exportResumeToPdf();
        }

        if (pdfFile.exists()) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("application/pdf");

            // Use FileProvider for Android 7.0+
            Uri fileUri = FileProvider.getUriForFile(
                    this,
                    getPackageName() + ".fileprovider",
                    pdfFile
            );

            shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(shareIntent, "Share Resume"));
        } else {
            Toast.makeText(this, "Resume PDF not found.", Toast.LENGTH_SHORT).show();
        }
    }



    private void initializeTemplateViews() {

        // Initialize the views using findViewById
        resumeProfileImg = findViewById(R.id.resume_profile_img);
        resumeFullName = findViewById(R.id.resume_full_name);
        resumePosition = findViewById(R.id.resume_position);
        resumePhoneNumber = findViewById(R.id.resume_phone_number);
        resumeEmail = findViewById(R.id.resume_email);
        resumeWebsite = findViewById(R.id.resume_website);
        resumeAddress = findViewById(R.id.resume_address);
        resumeEducationRecyclerView = findViewById(R.id.resume_education_recyclearview);
        resumeCertificationsRecyclerView = findViewById(R.id.resume_certifications_recyclearview);
        resumeExpertiseRecyclerView = findViewById(R.id.resume_expertise_recyclearView);
        resumeLanguageRecyclerView = findViewById(R.id.resume_language_recyclearView);
        resumeProfileTxt = findViewById(R.id.resume_profile_txt);
        resumeWorkExperienceRecyclerView = findViewById(R.id.resume_workExperience_recyclearView);
        resumeProjectTxt = findViewById(R.id.resume_project_txt);
        resumeProjectRecyclerView = findViewById(R.id.resume_project_recyclearView);
        resumeReferencesRecyclerView = findViewById(R.id.resume_references_recyclearView);
        resumeAwardsRecyclerView = findViewById(R.id.resume_awards_recyclearView);
        resumeExtraCurricularRecyclerView = findViewById(R.id.resume_extra_curricular_recyclearView);

        resume_certification_txt = findViewById(R.id.resume_certificate_txt);
        resume_education_txt = findViewById(R.id.resume_education_txt);
        resume_expertise_txt = findViewById(R.id.resume_expertise_txt);
        resume_language_txt = findViewById(R.id.resume_language_txt);
        resume_workExperience_txt = findViewById(R.id.resume_workExperience_txt);
        resume_project_txt = findViewById(R.id.resume_project_txt);
        resume_references_txt = findViewById(R.id.resume_references_txt);
        resume_award_txt = findViewById(R.id.resume_award_txt);
        resume_extra_curricular_txt = findViewById(R.id.resume_extra_curricular_txt);


        setupRecyclerView(resumeEducationRecyclerView);
        setupRecyclerView(resumeCertificationsRecyclerView);
        setupRecyclerView(resumeExpertiseRecyclerView);
        setupRecyclerView(resumeLanguageRecyclerView);
        setupRecyclerView(resumeWorkExperienceRecyclerView);
        setupRecyclerView(resumeProjectRecyclerView);
        setupRecyclerView(resumeReferencesRecyclerView);
        setupRecyclerView(resumeAwardsRecyclerView);
        setupRecyclerView(resumeExtraCurricularRecyclerView);

    }


    private void setupListeners() {
        backButton.setOnClickListener(v -> onBackPressed());
    }






    private ActivityResultLauncher<String[]> requestMultiplePermissionsLauncher;

    private void setupPermissionLauncher() {
        // Single permission launcher for Android 6-12
        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        exportResumeToPdf();
                    } else {
                        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            showSettingsDialog();
                        } else {
                            showPermissionDeniedDialog();
                        }
                    }
                });

        // Multiple permissions launcher for Android 13+
        requestMultiplePermissionsLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                permissions -> {
                    boolean allGranted = true;
                    for (Boolean granted : permissions.values()) {
                        if (!granted) {
                            allGranted = false;
                            break;
                        }
                    }

                    if (allGranted) {
                        exportResumeToPdf();
                    } else {
                        showSettingsDialog();
                    }
                });
    }

    private void checkStoragePermissionAndProceed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_AUDIO) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_VIDEO) == PackageManager.PERMISSION_GRANTED) {
                exportResumeToPdf();
            } else {
                showExplanationDialog();
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) { // Android 11-12
            if (Environment.isExternalStorageManager() ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                exportResumeToPdf();
            } else {
                showExplanationDialog();
            }
        } else { // Android 6-10
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                exportResumeToPdf();
            } else {
                showExplanationDialog();
            }
        }
    }

    // Modified Java method with custom layout
    private void showExplanationDialog() {
        String title, message;

        // Determine title and message based on Android version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
            title = "Media Access Permission Needed";
            message = "This app needs access to your device's media files to save your resume as a PDF. This is required for Android 13+ devices.";
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) { // Android 11-12
            title = "Storage Permission Needed";
            message = "This app needs storage access to save your resume as a PDF. For Android 11+, you may need to grant 'All files access' permission.";
        } else { // Android 6-10
            title = "Storage Permission Needed";
            message = "This permission is needed to save your resume as a PDF file in your device's storage.";
        }

        // Inflate custom layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_permission_explanation, null);

        // Get references to views
        TextView dialogTitle = dialogView.findViewById(R.id.dialog_title);
        TextView dialogMessage = dialogView.findViewById(R.id.dialog_message);
        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);
        Button btnGrantPermission = dialogView.findViewById(R.id.btn_grant_permission);

        // Set the dynamic content
        dialogTitle.setText(title);
        dialogMessage.setText(message);

        // Create the AlertDialog with custom view
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(false)
                .create();

        // Set click listeners for buttons
        btnGrantPermission.setOnClickListener(v -> {
            dialog.dismiss(); // Dismiss dialog first

            // Same permission logic as before
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                String[] permissions = {
                        Manifest.permission.READ_MEDIA_IMAGES,
                        Manifest.permission.READ_MEDIA_AUDIO,
                        Manifest.permission.READ_MEDIA_VIDEO
                };
                requestMultiplePermissionsLauncher.launch(permissions);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                try {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    intent.setData(Uri.fromParts("package", getPackageName(), null));
                    startActivity(intent);
                } catch (Exception e) {
                    requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
            } else {
                requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        // Show the dialog
        dialog.show();
    }

    private void showPermissionDeniedDialog() {
        String message;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            message = "Without media access permission, we cannot save your resume. This is required for Android 13+ devices. Would you like to try again?";
        } else {
            message = "Without storage permission, we cannot save your resume. Would you like to try again?";
        }

        new AlertDialog.Builder(this)
                .setTitle("Permission Denied")
                .setMessage(message)
                .setPositiveButton("Try Again", (dialog, which) -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        String[] permissions = {
                                Manifest.permission.READ_MEDIA_IMAGES,
                                Manifest.permission.READ_MEDIA_AUDIO,
                                Manifest.permission.READ_MEDIA_VIDEO
                        };
                        requestMultiplePermissionsLauncher.launch(permissions);
                    } else {
                        requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                })
                .setCancelable(false)
                .create()
                .show();
    }

    private void showSettingsDialog() {
        String message;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            message = "Media access permission is required to save resumes. Please enable 'Photos and videos', 'Music and audio' permissions in app settings.";
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            message = "Storage permission is required to save resumes. Please enable 'Files and media' or 'All files access' in app settings.";
        } else {
            message = "Storage permission is required to save resumes. Please enable it in app settings.";
        }

        new AlertDialog.Builder(this)
                .setTitle("Permission Required")
                .setMessage(message)
                .setPositiveButton("Open Settings", (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                })
                .setCancelable(false)
                .create()
                .show();
    }

    private void showProgressDialog(String message) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }


}