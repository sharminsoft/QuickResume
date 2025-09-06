package com.suitexen.quickresume.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.suitexen.quickresume.Activitys.AllResumeProfileActivity;
import com.suitexen.quickresume.Activitys.ResumeBuildingActivity;
import com.suitexen.quickresume.ModelClass.TemplatesModel;
import com.suitexen.quickresume.R;

import java.util.List;

public class TemplatesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String POPULAR_HOME = "popularHome";
    private static final String ADD_RESUME_DATA_ACTIVITY = "AddResumeDataActivity";
    private static final String TEMPLATE_NAME = "templateName";

    private List<TemplatesModel> templatesModelList;
    private Context context;
    private String fromActivity;
    private String profileId;

    public TemplatesAdapter(List<TemplatesModel> templatesModelList, Context context, String fromActivity, String profileId) {
        this.templatesModelList = templatesModelList;
        this.context = context;
        this.fromActivity = fromActivity;
        this.profileId = profileId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        if (POPULAR_HOME.equals(fromActivity)) {
            // Use PopularTemplatesViewHolder for "popularHome"
            view = inflater.inflate(R.layout.item_template_popular, parent, false);
            return new PopularTemplatesViewHolder(view);
        } else {
            // Use TemplatesViewHolder for other cases
            view = inflater.inflate(R.layout.item_template, parent, false);
            return new TemplatesViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TemplatesModel templatesModel = templatesModelList.get(position);

        if (holder instanceof TemplatesViewHolder) {
            // Bind data for TemplatesViewHolder
            TemplatesViewHolder regularHolder = (TemplatesViewHolder) holder;
            Glide.with(context)
                    .load(templatesModel.getImageUrl())
                    .into(regularHolder.templateIV);
        } else if (holder instanceof PopularTemplatesViewHolder) {
            // Bind data for PopularTemplatesViewHolder
            PopularTemplatesViewHolder popularHolder = (PopularTemplatesViewHolder) holder;
            Glide.with(context)
                    .load(templatesModel.getImageUrl())
                    .into(popularHolder.templateIV);
        }

        // Set click listener for both view holders
        holder.itemView.setOnClickListener(v -> {
            if (ADD_RESUME_DATA_ACTIVITY.equals(fromActivity)) {
                Intent intent = new Intent(context, ResumeBuildingActivity.class);
                intent.putExtra(TEMPLATE_NAME, templatesModel.getTemplateName());
                intent.putExtra("profileId", profileId);
                context.startActivity(intent);
            } else {
                Intent intent = new Intent(context, AllResumeProfileActivity.class);
                intent.putExtra(TEMPLATE_NAME, templatesModel.getTemplateName());
                AllResumeProfileAdapter.templateName = templatesModel.getTemplateName();
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return templatesModelList.size();
    }

    // Regular Template ViewHolder
    public static class TemplatesViewHolder extends RecyclerView.ViewHolder {
        ImageView templateIV;

        public TemplatesViewHolder(@NonNull View itemView) {
            super(itemView);
            templateIV = itemView.findViewById(R.id.templateIV);
        }
    }

    // Popular Template ViewHolder
    public static class PopularTemplatesViewHolder extends RecyclerView.ViewHolder {
        ImageView templateIV;

        public PopularTemplatesViewHolder(@NonNull View itemView) {
            super(itemView);
            templateIV = itemView.findViewById(R.id.templateIV);
        }
    }
}