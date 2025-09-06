package com.suitexen.quickresume.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.suitexen.quickresume.Activitys.ResumeBuildingActivity;
import com.suitexen.quickresume.ModelClass.ResumeProfileModel;
import com.suitexen.quickresume.R;

import java.util.List;

public class AllResumeProfileAdapter extends RecyclerView.Adapter<AllResumeProfileAdapter.AllResumeProfileHolder> {

    List<ResumeProfileModel> allresumeProfileModelList;
    Context context;

    public static String templateName;

    public AllResumeProfileAdapter(List<ResumeProfileModel> allresumeProfileModelList, Context context) {
        this.allresumeProfileModelList = allresumeProfileModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public AllResumeProfileAdapter.AllResumeProfileHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.all_resume_profile_item, parent, false);
        return new AllResumeProfileHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllResumeProfileAdapter.AllResumeProfileHolder holder, int position) {

        ResumeProfileModel profile = allresumeProfileModelList.get(position);

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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ResumeBuildingActivity.class);
                intent.putExtra("profileId", profile.getResumeId());
                intent.putExtra("templateName", templateName);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return allresumeProfileModelList.size();
    }

    public static class AllResumeProfileHolder extends RecyclerView.ViewHolder {
        TextView fullNameTv, emailTv, phoneTv, addressTv;
        ImageView headshotIv;

        public AllResumeProfileHolder(@NonNull View itemView) {
            super(itemView);

            fullNameTv = itemView.findViewById(R.id.fullNameTv);
            emailTv = itemView.findViewById(R.id.emailTv);
            phoneTv = itemView.findViewById(R.id.phoneTv);
            addressTv = itemView.findViewById(R.id.addressTv);
            headshotIv = itemView.findViewById(R.id.headshot_imageView);
        }
    }
}
