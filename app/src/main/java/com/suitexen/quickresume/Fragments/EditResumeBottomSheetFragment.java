package com.suitexen.quickresume.Fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.FirebaseFirestore;
import com.suitexen.quickresume.R;

import java.util.HashMap;
import java.util.Map;

public class EditResumeBottomSheetFragment extends BottomSheetDialogFragment {
    private String section;
    private Map<String, Object> itemData;
    private String documentId;
    private FirebaseFirestore db;
    private LinearLayout formContainer;
    private OnItemUpdatedListener itemUpdatedListener;

    public interface OnItemUpdatedListener {
        void onItemUpdated(Map<String, Object> updatedData);
    }

    public EditResumeBottomSheetFragment() {}

    public static EditResumeBottomSheetFragment newInstance(String section, Map<String, Object> itemData, String documentId) {
        EditResumeBottomSheetFragment fragment = new EditResumeBottomSheetFragment();
        Bundle args = new Bundle();
        args.putString("section", section);
        args.putString("documentId", documentId);
        fragment.setArguments(args);
        fragment.setItemData(itemData);
        return fragment;
    }

    public void setItemData(Map<String, Object> itemData) {
        this.itemData = itemData;
    }

    public void setOnItemUpdatedListener(OnItemUpdatedListener listener) {
        this.itemUpdatedListener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            section = getArguments().getString("section");
            documentId = getArguments().getString("documentId");
        }
        db = FirebaseFirestore.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_resume_bottom_sheet, container, false);
        formContainer = view.findViewById(R.id.formContainer);
        Button saveButton = view.findViewById(R.id.saveButton);

        setupFormFields();

        saveButton.setOnClickListener(v -> saveChanges());

        return view;
    }

    private void setupFormFields() {
        LayoutInflater inflater = LayoutInflater.from(getContext());

        switch (section) {
            case "career_objective":
                addField(inflater, "Career Objective", "careerObjective", true);
                break;

            case "educational_qualifications":
                addField(inflater, "Degree Name", "degreeName", false);
                addField(inflater, "University Name", "universityName", false);
                addField(inflater, "Year of Study", "yearOfStudy", false);
                addField(inflater, "CGPA", "cgpa", false);
                break;

            case "work_experience":
                addField(inflater, "Job Title", "jobTitle", false);
                addField(inflater, "Company Name", "companyName", false);
                addField(inflater, "Start Date", "startDate", false);
                addField(inflater, "End Date", "endDate", false);
                addField(inflater, "Responsibilities", "responsibilities", true);
                addField(inflater, "Achievements", "achievements", true);
                break;

            case "skills":
                addField(inflater, "Technical Skills", "technicalSkills", true);
                addField(inflater, "Soft Skills", "softSkills", true);
                break;

            case "projects_and_activities":
                addField(inflater, "Project Name", "projectName", false);
                addField(inflater, "Technologies Used", "technologiesUsed", false);
                addField(inflater, "Project Description", "projectDescription", true);
                addField(inflater, "Project Link", "projectLink", false);
                break;

            case "certifications_and_training":
                addField(inflater, "Course Name", "courseName", false);
                addField(inflater, "Institute Name", "instituteName", false);
                addField(inflater, "Completion Year", "completionYear", false);
                break;

            case "awards_and_achievements":
                addField(inflater, "Award Name", "awardName", false);
                addField(inflater, "Year Received", "yearReceived", false);
                addField(inflater, "Award Description", "awardDescription", true);
                break;

            case "languages_known":
                addField(inflater, "Native Language", "nativeLanguage", false);
                addField(inflater, "Other Languages", "otherLanguages", true);
                break;
        }
    }

    private void addField(LayoutInflater inflater, String label, String key, boolean multiLine) {
        View fieldView = inflater.inflate(R.layout.edit_field_layout, formContainer, false);
        EditText editText = fieldView.findViewById(R.id.editText);
        editText.setHint(label);
        editText.setSingleLine(!multiLine);
        if (multiLine) {
            editText.setMinLines(3);
        }
        
        // Set existing value if available
        if (itemData != null && itemData.containsKey(key)) {
            editText.setText((String) itemData.get(key));
        }

        editText.setTag(key); // Tag to identify field when saving
        formContainer.addView(fieldView);
    }

    private void saveChanges() {
        Map<String, Object> updatedData = new HashMap<>();
        boolean isValid = true;

        // Collect data from all fields
        for (int i = 0; i < formContainer.getChildCount(); i++) {
            View fieldView = formContainer.getChildAt(i);
            EditText editText = fieldView.findViewById(R.id.editText);
            String key = (String) editText.getTag();
            String value = editText.getText().toString().trim();

            if (TextUtils.isEmpty(value)) {
                editText.setError("This field is required");
                isValid = false;
            } else {
                updatedData.put(key, value);
            }
        }

        if (!isValid) {
            return;
        }

        // Update Firestore
        db.collection(section)
                .document(documentId)
                .update(updatedData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Updated successfully", Toast.LENGTH_SHORT).show();
                    if (itemUpdatedListener != null) {
                        itemUpdatedListener.onItemUpdated(updatedData);
                    }
                    dismiss();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Update failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}