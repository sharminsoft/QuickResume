package com.suitexen.quickresume.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.suitexen.quickresume.Activitys.AddResumeDataActivity;
import com.suitexen.quickresume.Activitys.ResumeProfilesActivity;
import com.suitexen.quickresume.ModelClass.ResumeProfileModel;
import com.suitexen.quickresume.R;

import java.util.ArrayList;

public class ResumeProfilesAdapter extends RecyclerView.Adapter<ResumeProfilesAdapter.ViewHolder> {

    private ArrayList<ResumeProfileModel> profileList;
    private FirebaseFirestore db;

    public ResumeProfilesAdapter(ArrayList<ResumeProfileModel> profileList) {
        this.profileList = profileList;
        this.db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_resume_profile, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ResumeProfileModel profile = profileList.get(position);

        holder.fullNameTv.setText(profile.getFullName());
        holder.emailTv.setText(profile.getEmail());
        holder.phoneTv.setText(profile.getPhone());
        holder.addressTv.setText(profile.getAddress());

        // Load headshot image with fallback
        if (profile.getHeadshotUrl() != null && !profile.getHeadshotUrl().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(profile.getHeadshotUrl())
                    .centerCrop()
                    .placeholder(R.drawable.round_person_24)
                    .into(holder.headshotIv);
        } else {
            Glide.with(holder.itemView.getContext())
                    .load(R.drawable.round_person_24)
                    .into(holder.headshotIv);
        }

        // Regular click - navigate to AddResumeDataActivity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddResumeDataActivity.class);
                intent.putExtra("resumeId", profile.getResumeId());
                intent.putExtra("fullName", profile.getFullName());
                v.getContext().startActivity(intent);
            }
        });

        // Long click - show edit/delete dialog
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showOptionsDialog(v.getContext(), profile, position);
                return true;
            }
        });
    }

    private void showOptionsDialog(Context context, ResumeProfileModel profile, int position) {
        String[] options = {"Edit Profile", "Delete Profile"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Profile Options");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // Edit
                        if (context instanceof ResumeProfilesActivity) {
                            ResumeProfilesActivity activity = (ResumeProfilesActivity) context;
                            activity.openBottomSheet(profile);
                        }
                        break;
                    case 1: // Delete
                        showDeleteConfirmationDialog(context, profile, position);
                        break;
                }
            }
        });
        builder.show();
    }

    private void showDeleteConfirmationDialog(Context context, ResumeProfileModel profile, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete Profile");
        builder.setMessage("Are you sure you want to delete this profile?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteProfile(context, profile, position);
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void deleteProfile(Context context, ResumeProfileModel profile, int position) {
        db.collection("All Resume Profiles").document(profile.getResumeId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    // Remove from local list and notify adapter
                    profileList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, profileList.size());
                    Toast.makeText(context, "Profile deleted successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Error deleting profile: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public int getItemCount() {
        return profileList != null ? profileList.size() : 0;
    }

    public void updateList(ArrayList<ResumeProfileModel> newList) {
        this.profileList.clear();
        this.profileList.addAll(newList);
        notifyDataSetChanged();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView fullNameTv, emailTv, phoneTv, addressTv;
        ImageView headshotIv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fullNameTv = itemView.findViewById(R.id.fullNameTv);
            emailTv = itemView.findViewById(R.id.emailTv);
            phoneTv = itemView.findViewById(R.id.phoneTv);
            addressTv = itemView.findViewById(R.id.addressTv);
            headshotIv = itemView.findViewById(R.id.headshotIv);
        }
    }
}