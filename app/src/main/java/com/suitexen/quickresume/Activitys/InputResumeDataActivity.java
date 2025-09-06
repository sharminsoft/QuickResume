package com.suitexen.quickresume.Activitys;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.suitexen.quickresume.Adapters.ResumeItemAdapter;
import com.suitexen.quickresume.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InputResumeDataActivity extends AppCompatActivity {

    ImageView back_btn, done_btn;
    TextView item_title;
    FloatingActionButton fab;
    RecyclerView itemRecyclerView;
    FirebaseFirestore db;
    String resumeId, section;
    ResumeItemAdapter adapter;
    ArrayList<Map<String, Object>> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_input_resume_data);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
        setupRecyclerView();
        loadSectionData();
    }

    private void initializeViews() {
        back_btn = findViewById(R.id.back_btn);
        done_btn = findViewById(R.id.done_btn);
        item_title = findViewById(R.id.item_title);
        fab = findViewById(R.id.fab);
        itemRecyclerView = findViewById(R.id.itemRecyclerView);

        resumeId = getIntent().getStringExtra("resumeId");
        section = getIntent().getStringExtra("section");
        db = FirebaseFirestore.getInstance();

        if (resumeId == null || resumeId.isEmpty() || section == null || section.isEmpty()) {
            showToast("Invalid resume or section data");
            finish();
            return;
        }

        updateTitle();
        setupClickListeners();
    }

    private void updateTitle() {
        switch (section) {
            case "educational_qualifications":
                item_title.setText("Educational Qualifications");
                break;
            case "work_experience":
                item_title.setText("Work Experience");
                break;
            case "certifications_and_training":
                item_title.setText("Certifications and Training");
                break;
            case "career_objective":
                item_title.setText("Career Objective");
                break;
            case "skills":
                item_title.setText("Skills");
                break;
            case "projects_and_activities":
                item_title.setText("Projects and Activities");
                break;
            case "references":
                item_title.setText("References");
                break;
            case "awards_and_achievements":
                item_title.setText("Awards and Achievements");
                break;
            case "volunteer_work":
                item_title.setText("Volunteer Work");
                break;
            case "languages_known":
                item_title.setText("Languages");
                break;
            default:
                item_title.setText("Resume Section");
                break;
        }
    }

    private void setupClickListeners() {
        back_btn.setOnClickListener(v -> finish());
        done_btn.setOnClickListener(v -> finish());
        fab.setOnClickListener(v -> showBottomSheetDialog());
    }

    private void setupRecyclerView() {
        items = new ArrayList<>();
        adapter = new ResumeItemAdapter(items, section,
                (item, position) -> showBottomSheetDialog(item), // Edit listener
                (item, position) -> deleteItem(item)); // Delete listener
        itemRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemRecyclerView.setAdapter(adapter);
    }

    private void deleteItem(Map<String, Object> item) {
        if (resumeId == null || resumeId.isEmpty()) {
            showToast("Unable to delete item: Invalid resume ID");
            return;
        }

        String itemId = (String) item.get("id");
        if (itemId == null) {
            showToast("Unable to delete item: Invalid item ID");
            return;
        }

        db.collection("All Resume Profiles")
                .document(resumeId)
                .collection(section)
                .document(itemId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    showToast("Item deleted successfully");
                    // The RecyclerView will update automatically due to the SnapshotListener
                })
                .addOnFailureListener(e -> {
                    showToast("Failed to delete item: " + e.getMessage());
                });
    }

    private void showBottomSheetDialog(Map<String, Object> itemToEdit) {
        View bottomSheetView = null;
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);

        switch (section) {
            case "career_objective":
                bottomSheetView = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_career_objective, null);
                setupCareerObjectiveSheet(bottomSheetView, bottomSheetDialog, itemToEdit);
                break;
            case "educational_qualifications":
                bottomSheetView = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_educational_qualifications, null);
                setupEducationalQualificationsSheet(bottomSheetView, bottomSheetDialog, itemToEdit);
                break;
            case "work_experience":
                bottomSheetView = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_work_experience, null);
                setupWorkExperienceSheet(bottomSheetView, bottomSheetDialog, itemToEdit);
                break;
            case "skills":
                bottomSheetView = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_skills, null);
                setupSkillsSheet(bottomSheetView, bottomSheetDialog, itemToEdit);
                break;
            case "projects_and_activities":
                bottomSheetView = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_projects, null);
                setupProjectsSheet(bottomSheetView, bottomSheetDialog, itemToEdit);
                break;
            case "certifications_and_training":
                bottomSheetView = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_certifications, null);
                setupCertificationsSheet(bottomSheetView, bottomSheetDialog, itemToEdit);
                break;
            case "awards_and_achievements":
                bottomSheetView = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_awards, null);
                setupAwardsSheet(bottomSheetView, bottomSheetDialog, itemToEdit);
                break;
            case "languages_known":
                bottomSheetView = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_languages, null);
                setupLanguagesSheet(bottomSheetView, bottomSheetDialog, itemToEdit);
                break;
            case "references":
                bottomSheetView = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_references, null);
                setupReferencesSheet(bottomSheetView, bottomSheetDialog, itemToEdit);
                break;
            case "volunteer_work":
                bottomSheetView = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_volunteer_work, null);
                setupVolunteerWorkSheet(bottomSheetView, bottomSheetDialog, itemToEdit);
                break;
        }

        if (bottomSheetView != null) {
            bottomSheetDialog.setContentView(bottomSheetView);
            bottomSheetDialog.show();
        }
    }

    private void loadSectionData() {
        if (resumeId == null || resumeId.isEmpty()) return;

        db.collection("All Resume Profiles")
                .document(resumeId)
                .collection(section)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        showToast("Error loading data: " + error.getMessage());
                        return;
                    }
                    if (value == null) return;

                    items.clear();
                    for (DocumentSnapshot doc : value.getDocuments()) {
                        Map<String, Object> data = doc.getData();
                        if (data != null) {
                            // Add document ID to the data for edit/delete operations
                            data.put("id", doc.getId());
                            items.add(data);
                        }
                    }
                    adapter.updateItems(items);
                });
    }

    private void showBottomSheetDialog() {
        showBottomSheetDialog(null);
    }

    private void setupCareerObjectiveSheet(View view, BottomSheetDialog dialog, Map<String, Object> itemToEdit) {
        TextInputEditText careerObjectiveEt = view.findViewById(R.id.careerObjectiveEt);
        MaterialButton saveBtn = view.findViewById(R.id.saveBtn);

        if (itemToEdit != null && itemToEdit.containsKey("careerObjective")) {
            careerObjectiveEt.setText((String) itemToEdit.get("careerObjective"));
        }

        saveBtn.setOnClickListener(v -> {
            String careerObjective = careerObjectiveEt.getText().toString().trim();
            if (!careerObjective.isEmpty()) {
                Map<String, Object> data = new HashMap<>();
                data.put("careerObjective", careerObjective);
                if (itemToEdit != null && itemToEdit.containsKey("id")) {
                    data.put("id", itemToEdit.get("id"));
                }
                saveToFirestore(section, data, dialog);
            } else {
                showToast("Please enter career objective");
            }
        });
    }

    private void setupEducationalQualificationsSheet(View view, BottomSheetDialog dialog, Map<String, Object> itemToEdit) {
        TextInputEditText degreeNameEt = view.findViewById(R.id.degreeNameEt);
        TextInputEditText universityNameEt = view.findViewById(R.id.universityNameEt);
        TextInputEditText startDateEt = view.findViewById(R.id.startDateEt);
        TextInputEditText endDateEt = view.findViewById(R.id.endDateEt);
        TextInputEditText cgpaEt = view.findViewById(R.id.cgpaEt);
        MaterialButton saveBtn = view.findViewById(R.id.saveBtn);

        if (itemToEdit != null) {
            degreeNameEt.setText((String) itemToEdit.get("degreeName"));
            universityNameEt.setText((String) itemToEdit.get("universityName"));
            startDateEt.setText((String) itemToEdit.get("startDate"));
            endDateEt.setText((String) itemToEdit.get("endDate"));
            if (itemToEdit.containsKey("cgpa")) {
                cgpaEt.setText((String) itemToEdit.get("cgpa"));
            }
        }

        saveBtn.setOnClickListener(v -> {
            String degreeName = degreeNameEt.getText().toString().trim();
            String universityName = universityNameEt.getText().toString().trim();
            String startDate = startDateEt.getText().toString().trim();
            String endDate = endDateEt.getText().toString().trim();
            String cgpa = cgpaEt.getText().toString().trim();

            if (!degreeName.isEmpty() && !universityName.isEmpty() && !startDate.isEmpty()) {
                Map<String, Object> data = new HashMap<>();
                data.put("degreeName", degreeName);
                data.put("universityName", universityName);
                data.put("startDate", startDate);
                data.put("endDate", endDate);
                if (!cgpa.isEmpty()) data.put("cgpa", cgpa);

                if (itemToEdit != null && itemToEdit.containsKey("id")) {
                    data.put("id", itemToEdit.get("id"));
                }
                saveToFirestore(section, data, dialog);
            } else {
                showToast("Please fill all required fields");
            }
        });
    }

    private void setupWorkExperienceSheet(View view, BottomSheetDialog dialog, Map<String, Object> itemToEdit) {
        TextInputEditText companyNameEt = view.findViewById(R.id.companyNameEt);
        TextInputEditText jobTitleEt = view.findViewById(R.id.jobTitleEt);
        TextInputEditText startDateEt = view.findViewById(R.id.startDateEt);
        TextInputEditText endDateEt = view.findViewById(R.id.endDateEt);
        TextInputEditText responsibilitiesEt = view.findViewById(R.id.responsibilitiesEt);
        MaterialButton saveBtn = view.findViewById(R.id.saveBtn);

        if (itemToEdit != null) {
            companyNameEt.setText((String) itemToEdit.get("companyName"));
            jobTitleEt.setText((String) itemToEdit.get("jobTitle"));
            startDateEt.setText((String) itemToEdit.get("startDate"));
            endDateEt.setText((String) itemToEdit.get("endDate"));
            responsibilitiesEt.setText((String) itemToEdit.get("responsibilities"));

        }

        saveBtn.setOnClickListener(v -> {
            String companyName = companyNameEt.getText().toString().trim();
            String jobTitle = jobTitleEt.getText().toString().trim();
            String startDate = startDateEt.getText().toString().trim();
            String endDate = endDateEt.getText().toString().trim();
            String responsibilities = responsibilitiesEt.getText().toString().trim();

            if (!companyName.isEmpty() && !jobTitle.isEmpty() && !startDate.isEmpty()
                    && !endDate.isEmpty() && !responsibilities.isEmpty()) {
                Map<String, Object> data = new HashMap<>();
                data.put("companyName", companyName);
                data.put("jobTitle", jobTitle);
                data.put("startDate", startDate);
                data.put("endDate", endDate);
                data.put("responsibilities", responsibilities);


                if (itemToEdit != null && itemToEdit.containsKey("id")) {
                    data.put("id", itemToEdit.get("id"));
                }
                saveToFirestore(section, data, dialog);
            } else {
                showToast("Please fill all required fields");
            }
        });
    }

    private void setupSkillsSheet(View view, BottomSheetDialog dialog, Map<String, Object> itemToEdit) {
        TextInputEditText technicalSkillsEt = view.findViewById(R.id.technicalSkillsEt);
        MaterialButton saveBtn = view.findViewById(R.id.saveBtn);

        if (itemToEdit != null) {
            technicalSkillsEt.setText((String) itemToEdit.get("technicalSkills"));

        }

        saveBtn.setOnClickListener(v -> {
            String technicalSkills = technicalSkillsEt.getText().toString().trim();


            if (!technicalSkills.isEmpty()) {
                Map<String, Object> data = new HashMap<>();
                data.put("skill", technicalSkills);
                if (itemToEdit != null && itemToEdit.containsKey("id")) {
                    data.put("id", itemToEdit.get("id"));
                }
                saveToFirestore(section, data, dialog);
            } else {
                showToast("Please fill all required fields");
            }
        });
    }

    private void setupProjectsSheet(View view, BottomSheetDialog dialog, Map<String, Object> itemToEdit) {
        TextInputEditText projectNameEt = view.findViewById(R.id.projectNameEt);
        TextInputEditText projectDescriptionEt = view.findViewById(R.id.projectDescriptionEt);
        TextInputEditText technologiesUsedEt = view.findViewById(R.id.technologiesUsedEt);
        TextInputEditText projectLinkEt = view.findViewById(R.id.projectLinkEt);
        MaterialButton saveBtn = view.findViewById(R.id.saveBtn);

        if (itemToEdit != null) {
            projectNameEt.setText((String) itemToEdit.get("projectName"));
            projectDescriptionEt.setText((String) itemToEdit.get("projectDescription"));
            technologiesUsedEt.setText((String) itemToEdit.get("technologiesUsed"));
            if (itemToEdit.containsKey("projectLink")) {
                projectLinkEt.setText((String) itemToEdit.get("projectLink"));
            }
        }

        saveBtn.setOnClickListener(v -> {
            String projectName = projectNameEt.getText().toString().trim();
            String projectDescription = projectDescriptionEt.getText().toString().trim();
            String technologiesUsed = technologiesUsedEt.getText().toString().trim();
            String projectLink = projectLinkEt.getText().toString().trim();

            if (!projectName.isEmpty() && !projectDescription.isEmpty() && !technologiesUsed.isEmpty()) {
                Map<String, Object> data = new HashMap<>();
                data.put("projectName", projectName);
                data.put("projectDescription", projectDescription);
                data.put("technologiesUsed", technologiesUsed);
                if (!projectLink.isEmpty()) data.put("projectLink", projectLink);

                if (itemToEdit != null && itemToEdit.containsKey("id")) {
                    data.put("id", itemToEdit.get("id"));
                }
                saveToFirestore(section, data, dialog);
            } else {
                showToast("Please fill all required fields");
            }
        });
    }

    private void setupCertificationsSheet(View view, BottomSheetDialog dialog, Map<String, Object> itemToEdit) {
        TextInputEditText courseNameEt = view.findViewById(R.id.courseNameEt);
        TextInputEditText instituteNameEt = view.findViewById(R.id.instituteNameEt);
        TextInputEditText completionYearEt = view.findViewById(R.id.completionYearEt);
        MaterialButton saveBtn = view.findViewById(R.id.saveBtn);

        if (itemToEdit != null) {
            courseNameEt.setText((String) itemToEdit.get("courseName"));
            instituteNameEt.setText((String) itemToEdit.get("instituteName"));
            completionYearEt.setText((String) itemToEdit.get("completionYear"));
        }

        saveBtn.setOnClickListener(v -> {
            String courseName = courseNameEt.getText().toString().trim();
            String instituteName = instituteNameEt.getText().toString().trim();
            String completionYear = completionYearEt.getText().toString().trim();

            if (!courseName.isEmpty() && !instituteName.isEmpty() && !completionYear.isEmpty()) {
                Map<String, Object> data = new HashMap<>();
                data.put("courseName", courseName);
                data.put("instituteName", instituteName);
                data.put("completionYear", completionYear);

                if (itemToEdit != null && itemToEdit.containsKey("id")) {
                    data.put("id", itemToEdit.get("id"));
                }
                saveToFirestore(section, data, dialog);
            } else {
                showToast("Please fill all required fields");
            }
        });
    }

    private void setupAwardsSheet(View view, BottomSheetDialog dialog, Map<String, Object> itemToEdit) {
        TextInputEditText awardNameEt = view.findViewById(R.id.awardNameEt);
        TextInputEditText awardDescriptionEt = view.findViewById(R.id.awardDescriptionEt);
        TextInputEditText yearReceivedEt = view.findViewById(R.id.yearReceivedEt);
        MaterialButton saveBtn = view.findViewById(R.id.saveBtn);

        if (itemToEdit != null) {
            awardNameEt.setText((String) itemToEdit.get("awardName"));
            awardDescriptionEt.setText((String) itemToEdit.get("awardDescription"));
            yearReceivedEt.setText((String) itemToEdit.get("yearReceived"));
        }

        saveBtn.setOnClickListener(v -> {
            String awardName = awardNameEt.getText().toString().trim();
            String awardDescription = awardDescriptionEt.getText().toString().trim();
            String yearReceived = yearReceivedEt.getText().toString().trim();

            if (!awardName.isEmpty() && !awardDescription.isEmpty() && !yearReceived.isEmpty()) {
                Map<String, Object> data = new HashMap<>();
                data.put("awardName", awardName);
                data.put("awardDescription", awardDescription);
                data.put("yearReceived", yearReceived);

                if (itemToEdit != null && itemToEdit.containsKey("id")) {
                    data.put("id", itemToEdit.get("id"));
                }
                saveToFirestore(section, data, dialog);
            } else {
                showToast("Please fill all required fields");
            }
        });
    }

    private void setupLanguagesSheet(View view, BottomSheetDialog dialog, Map<String, Object> itemToEdit) {
        TextInputEditText nativeLanguageEt = view.findViewById(R.id.nativeLanguageEt);
        MaterialButton saveBtn = view.findViewById(R.id.saveBtn);

        if (itemToEdit != null) {
            nativeLanguageEt.setText((String) itemToEdit.get("nativeLanguage"));
        }

        saveBtn.setOnClickListener(v -> {
            String nativeLanguage = nativeLanguageEt.getText().toString().trim();

            if (!nativeLanguage.isEmpty()) {
                Map<String, Object> data = new HashMap<>();
                data.put("nativeLanguage", nativeLanguage);

                if (itemToEdit != null && itemToEdit.containsKey("id")) {
                    data.put("id", itemToEdit.get("id"));
                }
                saveToFirestore(section, data, dialog);
            } else {
                showToast("Please fill all required fields");
            }
        });
    }

    private void setupReferencesSheet(View view, BottomSheetDialog dialog, Map<String, Object> itemToEdit) {
        TextInputEditText referenceNameEt = view.findViewById(R.id.referenceNameEt);
        TextInputEditText referencePositionEt = view.findViewById(R.id.referencePositionEt);
        TextInputEditText referenceCompanyEt = view.findViewById(R.id.referenceCompanyEt);
        TextInputEditText referenceContactEt = view.findViewById(R.id.referenceContactEt);
        TextInputEditText referenceEmailEt = view.findViewById(R.id.referenceEmailEt);
        MaterialButton saveBtn = view.findViewById(R.id.saveReferenceButton);



        if (itemToEdit != null) {
            referenceNameEt.setText(getString(itemToEdit, "referenceName"));
            referencePositionEt.setText(getString(itemToEdit, "referencePosition"));
            referenceCompanyEt.setText(getString(itemToEdit, "referenceCompany"));
            referenceContactEt.setText(getString(itemToEdit, "referenceContact"));
            referenceEmailEt.setText(getString(itemToEdit, "referenceEmail"));
        }

        saveBtn.setOnClickListener(v -> {
            String referenceName = referenceNameEt.getText().toString().trim();
            String referencePosition = referencePositionEt.getText().toString().trim();
            String referenceCompany = referenceCompanyEt.getText().toString().trim();
            String referenceContact = referenceContactEt.getText().toString().trim();
            String referenceEmail = referenceEmailEt.getText().toString().trim();

            if (!referenceName.isEmpty() && !referencePosition.isEmpty() && !referenceCompany.isEmpty()
                    && !referenceContact.isEmpty() && !referenceEmail.isEmpty()) {
                Map<String, Object> data = new HashMap<>();
                data.put("referenceName", referenceName);
                data.put("referencePosition", referencePosition);
                data.put("referenceCompany", referenceCompany);
                data.put("referenceContact", referenceContact);
                data.put("referenceEmail", referenceEmail);

                if (itemToEdit != null && itemToEdit.containsKey("id")) {
                    data.put("id", itemToEdit.get("id"));
                }
                saveToFirestore(section, data, dialog);
            } else {
                showToast("Please fill all required fields");
            }
        });
    }

    // Helper method to safely get string values from the map
    private String getString(Map<String, Object> map, String key) {
        return map.containsKey(key) ? (String) map.get(key) : "";
    }

    private void setupVolunteerWorkSheet(View view, BottomSheetDialog dialog, Map<String, Object> itemToEdit) {
        TextInputEditText organizationNameEt = view.findViewById(R.id.organizationNameInput);
        TextInputEditText roleEt = view.findViewById(R.id.volunteerRoleInput);
        TextInputEditText durationEt = view.findViewById(R.id.volunteerDurationInput);
        TextInputEditText descriptionEt = view.findViewById(R.id.volunteerDescriptionInput);
        MaterialButton saveBtn = view.findViewById(R.id.saveVolunteerWorkButton);

        if (itemToEdit != null) {
            organizationNameEt.setText((String) itemToEdit.get("organizationName"));
            roleEt.setText((String) itemToEdit.get("role"));
            durationEt.setText((String) itemToEdit.get("duration"));
            descriptionEt.setText((String) itemToEdit.get("description"));
        }

        saveBtn.setOnClickListener(v -> {
            String organizationName = organizationNameEt.getText().toString().trim();
            String role = roleEt.getText().toString().trim();
            String duration = durationEt.getText().toString().trim();
            String description = descriptionEt.getText().toString().trim();

            if (!organizationName.isEmpty() && !role.isEmpty() && !duration.isEmpty()
                    && !description.isEmpty()) {
                Map<String, Object> data = new HashMap<>();
                data.put("organizationName", organizationName);
                data.put("role", role);
                data.put("duration", duration);
                data.put("description", description);

                if (itemToEdit != null && itemToEdit.containsKey("id")) {
                    data.put("id", itemToEdit.get("id"));
                }
                saveToFirestore(section, data, dialog);
            } else {
                showToast("Please fill all required fields");
            }
        });
    }

    private void saveToFirestore(String collectionName, Map<String, Object> data, BottomSheetDialog dialog) {
        if (resumeId == null || resumeId.isEmpty()) {
            showToast("Unable to save: Invalid resume ID");
            return;
        }

        // Check if we're editing an existing document
        String documentId = null;
        if (data.containsKey("id")) {
            documentId = (String) data.get("id");
        }

        DocumentReference docRef;

        if (documentId != null && !documentId.isEmpty()) {
            // Update existing document
            docRef = db.collection("All Resume Profiles")
                    .document(resumeId)
                    .collection(collectionName)
                    .document(documentId);

            // Remove the ID field before saving to Firestore
            data.remove("id");
        } else {
            // Create new document reference
            docRef = db.collection("All Resume Profiles")
                    .document(resumeId)
                    .collection(collectionName)
                    .document();
        }

        docRef.set(data)
                .addOnSuccessListener(aVoid -> {
                    showToast("Saved successfully");
                    dialog.dismiss();
                })
                .addOnFailureListener(e -> {
                    showToast("Failed to save: " + e.getMessage());
                });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}