package com.suitexen.quickresume.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.suitexen.quickresume.R;

import java.util.ArrayList;
import java.util.Map;

public class ResumeItemAdapter extends RecyclerView.Adapter<ResumeItemAdapter.ViewHolder> {

    private ArrayList<Map<String, Object>> items;
    private String section;
    private OnItemEditListener editListener;
    private OnItemDeleteListener deleteListener;

    public interface OnItemEditListener {
        void onItemEdit(Map<String, Object> item, int position);
    }

    public interface OnItemDeleteListener {
        void onItemDelete(Map<String, Object> item, int position);
    }

    public ResumeItemAdapter(ArrayList<Map<String, Object>> items, String section,
                             OnItemEditListener editListener, OnItemDeleteListener deleteListener) {
        this.items = items != null ? items : new ArrayList<>();
        this.section = section;
        this.editListener = editListener;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.resume_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Map<String, Object> item = items.get(position);
        if (item == null) return;

        switch (section) {
            case "career_objective":
                bindCareerObjective(holder, item);
                break;

            case "educational_qualifications":
                bindEducationalQualifications(holder, item);
                break;

            case "work_experience":
                bindWorkExperience(holder, item);
                break;

            case "skills":
                bindSkills(holder, item);
                break;

            case "projects_and_activities":
                bindProjectsAndActivities(holder, item);
                break;

            case "certifications_and_training":
                bindCertificationsAndTraining(holder, item);
                break;

            case "awards_and_achievements":
                bindAwardsAndAchievements(holder, item);
                break;

            case "languages_known":
                bindLanguagesKnown(holder, item);
                break;

            case "references":
                bindReferences(holder, item);
                break;

            case "volunteer_work":
                bindVolunteerWork(holder, item);
                break;
        }

        holder.editBtn.setOnClickListener(v -> {
            if (editListener != null) {
                editListener.onItemEdit(item, holder.getAdapterPosition());
            }
        });

        holder.deleteBtn.setOnClickListener(v -> {
            showDeleteConfirmationDialog(v.getContext(), item, holder.getAdapterPosition());
        });
    }

    private void bindCareerObjective(ViewHolder holder, Map<String, Object> item) {
        holder.titleTv.setText("Career Objective");
        holder.descriptionTv.setText(getString(item, "careerObjective"));
        holder.subtitleTv.setVisibility(View.GONE);
        holder.dateTv.setVisibility(View.GONE);
    }

    private void bindEducationalQualifications(ViewHolder holder, Map<String, Object> item) {
        holder.titleTv.setText(getString(item, "degreeName"));
        holder.subtitleTv.setText(getString(item, "universityName"));
        String startYear = getString(item, "startDate");
        String endYear = getString(item, "endDate");
        holder.dateTv.setText(startYear + " - " + endYear);
        String description = item.containsKey("cgpa") ? "CGPA: " + getString(item, "cgpa") : "";
        holder.descriptionTv.setText(description);
        holder.descriptionTv.setVisibility(description.isEmpty() ? View.GONE : View.VISIBLE);
    }

    private void bindWorkExperience(ViewHolder holder, Map<String, Object> item) {
        holder.titleTv.setText(getString(item, "jobTitle"));
        holder.subtitleTv.setText("at " + getString(item, "companyName"));
        holder.dateTv.setText(getString(item, "startDate") + " - " + getString(item, "endDate"));
        StringBuilder workDesc = new StringBuilder();
        workDesc.append("Responsibilities:\n").append(getString(item, "responsibilities"));
        if (item.containsKey("achievements")) {
            workDesc.append("\n\nAchievements:\n").append(getString(item, "achievements"));
        }
        holder.descriptionTv.setText(workDesc.toString());
    }

    private void bindSkills(ViewHolder holder, Map<String, Object> item) {
        holder.titleTv.setText("Skills");
        holder.subtitleTv.setVisibility(View.GONE);
        holder.descriptionTv.setText(getString(item, "skill"));
        holder.dateTv.setVisibility(View.GONE);
    }

    private void bindProjectsAndActivities(ViewHolder holder, Map<String, Object> item) {
        holder.titleTv.setText(getString(item, "projectName"));
        holder.subtitleTv.setText("Technologies: " + getString(item, "technologiesUsed"));
        StringBuilder projectDesc = new StringBuilder();
        projectDesc.append(getString(item, "projectDescription"));
        if (item.containsKey("projectLink")) {
            projectDesc.append("\nProject Link: ").append(getString(item, "projectLink"));
        }
        holder.descriptionTv.setText(projectDesc.toString());
        holder.dateTv.setVisibility(View.GONE);
    }

    private void bindCertificationsAndTraining(ViewHolder holder, Map<String, Object> item) {
        holder.titleTv.setText(getString(item, "courseName"));
        holder.subtitleTv.setText(getString(item, "instituteName"));
        holder.dateTv.setText("Completion Year: " + getString(item, "completionYear"));
        holder.descriptionTv.setVisibility(View.GONE);
    }

    private void bindAwardsAndAchievements(ViewHolder holder, Map<String, Object> item) {
        holder.titleTv.setText(getString(item, "awardName"));
        holder.subtitleTv.setText("Year Received: " + getString(item, "yearReceived"));
        holder.descriptionTv.setText(getString(item, "awardDescription"));
        holder.dateTv.setVisibility(View.GONE);
    }

    private void bindLanguagesKnown(ViewHolder holder, Map<String, Object> item) {
        holder.titleTv.setText("Languages");
        holder.subtitleTv.setVisibility(View.GONE);
        holder.descriptionTv.setText(getString(item, "nativeLanguage"));
        holder.dateTv.setVisibility(View.GONE);
    }

    private void bindReferences(ViewHolder holder, Map<String, Object> item) {
        holder.titleTv.setText(getString(item, "referenceName"));
        holder.subtitleTv.setVisibility(View.GONE);
        holder.descriptionTv.setText("Contact: " + getString(item, "referenceContact") + "\nEmail: " + getString(item, "referenceEmail"));
        holder.dateTv.setVisibility(View.GONE);
    }

    private void bindVolunteerWork(ViewHolder holder, Map<String, Object> item) {
        holder.titleTv.setText(getString(item, "organizationName"));
        holder.subtitleTv.setText(getString(item, "role"));
        holder.dateTv.setText(getString(item, "startDate") + " - " + getString(item, "endDate"));
        holder.descriptionTv.setText(getString(item, "description"));
    }

    private String getString(Map<String, Object> item, String key) {
        return item.containsKey(key) ? (String) item.get(key) : "";
    }

    private void showDeleteConfirmationDialog(Context context, Map<String, Object> item, int position) {
        new AlertDialog.Builder(context)
                .setTitle("Delete Item")
                .setMessage("Are you sure you want to delete this item?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    if (deleteListener != null) {
                        deleteListener.onItemDelete(item, position);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateItems(ArrayList<Map<String, Object>> newItems) {
        this.items = newItems != null ? newItems : new ArrayList<>();
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTv, subtitleTv, descriptionTv, dateTv;
        ImageButton editBtn, deleteBtn;

        ViewHolder(View itemView) {
            super(itemView);
            titleTv = itemView.findViewById(R.id.titleTv);
            subtitleTv = itemView.findViewById(R.id.subtitleTv);
            descriptionTv = itemView.findViewById(R.id.descriptionTv);
            dateTv = itemView.findViewById(R.id.dateTv);
            editBtn = itemView.findViewById(R.id.editBtn);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }
    }
}