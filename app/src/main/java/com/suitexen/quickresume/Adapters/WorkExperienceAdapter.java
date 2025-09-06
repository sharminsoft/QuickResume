package com.suitexen.quickresume.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.suitexen.quickresume.ModelClass.WorkExperienceItem;
import com.suitexen.quickresume.R;
import java.util.List;

public class WorkExperienceAdapter extends RecyclerView.Adapter<WorkExperienceAdapter.WorkExperienceViewHolder> {

    public static String template_tag = "";
    private Context context;
    private List<WorkExperienceItem> workExperienceList;

    public WorkExperienceAdapter(Context context, List<WorkExperienceItem> workExperienceList) {
        this.context = context;
        this.workExperienceList = workExperienceList;
    }

    @NonNull
    @Override
    public WorkExperienceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_work_experience, parent, false);
        return new WorkExperienceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkExperienceViewHolder holder, int position) {


        if (template_tag.equals("lizzie_johan")) {
            holder.tvJobTitle.setTextColor(context.getResources().getColor(R.color.black_light));
            holder.tvJobDate.setTextColor(context.getResources().getColor(R.color.black_light));
            holder.tvCompany.setTextColor(context.getResources().getColor(R.color.black_light));
            holder.tvJobDescription.setTextColor(context.getResources().getColor(R.color.black_light));

            holder.tvJobTitle.setTextSize(8);
            holder.tvJobDate.setTextSize(6);
            holder.tvCompany.setTextSize(6);
            holder.tvJobDescription.setTextSize(6);
        }else if (template_tag.equals("david_anderson")) {
            holder.tvJobTitle.setTextColor(context.getResources().getColor(R.color.black_light));
            holder.tvJobDate.setTextColor(context.getResources().getColor(R.color.black_light));
            holder.tvCompany.setTextColor(context.getResources().getColor(R.color.black_light));
            holder.tvJobDescription.setTextColor(context.getResources().getColor(R.color.black_light));

            holder.tvJobTitle.setTextSize(8);
            holder.tvJobDate.setTextSize(6);
            holder.tvCompany.setTextSize(6);
            holder.tvJobDescription.setTextSize(6);
        }else if (template_tag.equals("dyrection_pesh")) {
            holder.tvJobTitle.setTextColor(context.getResources().getColor(R.color.black_light));
            holder.tvJobDate.setTextColor(context.getResources().getColor(R.color.black_light));
            holder.tvCompany.setTextColor(context.getResources().getColor(R.color.black_light));
            holder.tvJobDescription.setTextColor(context.getResources().getColor(R.color.black_light));

            holder.tvJobTitle.setTextSize(8);
            holder.tvJobDate.setTextSize(6);
            holder.tvCompany.setTextSize(6);
            holder.tvJobDescription.setTextSize(6);
        }else if (template_tag.equals("olivia_wilson")) {
            holder.tvJobTitle.setTextColor(context.getResources().getColor(R.color.black_light));
            holder.tvJobDate.setTextColor(context.getResources().getColor(R.color.black_light));
            holder.tvCompany.setTextColor(context.getResources().getColor(R.color.black_light));
            holder.tvJobDescription.setTextColor(context.getResources().getColor(R.color.black_light));

            holder.tvJobTitle.setTextSize(8);
            holder.tvJobDate.setTextSize(6);
            holder.tvCompany.setTextSize(6);
            holder.tvJobDescription.setTextSize(6);
        }else if (template_tag.equals("jack_william")) {
            holder.tvJobTitle.setTextColor(context.getResources().getColor(R.color.black_light));
            holder.tvJobDate.setTextColor(context.getResources().getColor(R.color.black_light));
            holder.tvCompany.setTextColor(context.getResources().getColor(R.color.black_light));
            holder.tvJobDescription.setTextColor(context.getResources().getColor(R.color.black_light));

            holder.tvJobTitle.setTextSize(8);
            holder.tvJobDate.setTextSize(6);
            holder.tvCompany.setTextSize(6);
            holder.tvJobDescription.setTextSize(6);
        }


        WorkExperienceItem workExperience = workExperienceList.get(position);
        holder.tvJobTitle.setText(workExperience.getJobTitle());
        holder.tvJobDate.setText(workExperience.getStartDate() + " - " + workExperience.getEndDate());
        holder.tvCompany.setText(workExperience.getCompanyName());
        holder.tvJobDescription.setText(workExperience.getResponsibilities());
    }

    @Override
    public int getItemCount() {
        return workExperienceList.size();
    }

    public static class WorkExperienceViewHolder extends RecyclerView.ViewHolder {
        TextView tvJobTitle, tvJobDate, tvCompany, tvJobDescription;

        public WorkExperienceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJobTitle = itemView.findViewById(R.id.tv_job_title);
            tvJobDate = itemView.findViewById(R.id.tv_job_date);
            tvCompany = itemView.findViewById(R.id.tv_company);
            tvJobDescription = itemView.findViewById(R.id.tv_job_description);
        }
    }
}