package com.suitexen.quickresume.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.suitexen.quickresume.ModelClass.ProjectItem;
import com.suitexen.quickresume.R;
import java.util.List;

public class ProjectsAdapter extends RecyclerView.Adapter<ProjectsAdapter.ProjectViewHolder> {

    private Context context;
    private List<ProjectItem> projectList;

    public static String template_tag = "";

    public ProjectsAdapter(Context context, List<ProjectItem> projectList) {
        this.context = context;
        this.projectList = projectList;
    }



    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_project, parent, false);
        return new ProjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder holder, int position) {

        if (template_tag.equals("lizzie_johan")) {
            holder.tvProjectTitle.setTextColor(context.getResources().getColor(R.color.black_light));
            holder.tvProjectDescription.setTextColor(context.getResources().getColor(R.color.black_light));
            holder.tvTechnologies.setTextColor(context.getResources().getColor(R.color.black_light));

            holder.tvProjectTitle.setTextSize(8);
            holder.tvProjectDescription.setTextSize(6);
            holder.tvTechnologies.setTextSize(6);
        }else if (template_tag.equals("david_anderson")) {
            holder.tvProjectTitle.setTextColor(context.getResources().getColor(R.color.black_light));
            holder.tvProjectDescription.setTextColor(context.getResources().getColor(R.color.black_light));
            holder.tvTechnologies.setTextColor(context.getResources().getColor(R.color.black_light));

            holder.tvProjectTitle.setTextSize(8);
            holder.tvProjectDescription.setTextSize(6);
            holder.tvTechnologies.setTextSize(6);
        }else if (template_tag.equals("dyrection_pesh")) {
            holder.tvProjectTitle.setTextColor(context.getResources().getColor(R.color.black_light));
            holder.tvProjectDescription.setTextColor(context.getResources().getColor(R.color.black_light));
            holder.tvTechnologies.setTextColor(context.getResources().getColor(R.color.black_light));

            holder.tvProjectTitle.setTextSize(8);
            holder.tvProjectDescription.setTextSize(6);
            holder.tvTechnologies.setTextSize(6);
        }else if (template_tag.equals("olivia_wilson")) {
            holder.tvProjectTitle.setTextColor(context.getResources().getColor(R.color.black_light));
            holder.tvProjectDescription.setTextColor(context.getResources().getColor(R.color.black_light));
            holder.tvTechnologies.setTextColor(context.getResources().getColor(R.color.black_light));

            holder.tvProjectTitle.setTextSize(8);
            holder.tvProjectDescription.setTextSize(6);
            holder.tvTechnologies.setTextSize(6);
        }else if (template_tag.equals("jack_william")) {
            holder.tvProjectTitle.setTextColor(context.getResources().getColor(R.color.black_light));
            holder.tvProjectDescription.setTextColor(context.getResources().getColor(R.color.black_light));
            holder.tvTechnologies.setTextColor(context.getResources().getColor(R.color.black_light));

            holder.tvProjectTitle.setTextSize(8);
            holder.tvProjectDescription.setTextSize(6);
            holder.tvTechnologies.setTextSize(6);
        }

        ProjectItem project = projectList.get(position);
        holder.tvProjectTitle.setText(project.getProjectName());
        holder.tvProjectDescription.setText(project.getProjectDescription());
        holder.tvTechnologies.setText("Technologies: " + project.getTechnologiesUsed());
    }

    @Override
    public int getItemCount() {
        return projectList.size();
    }

    public static class ProjectViewHolder extends RecyclerView.ViewHolder {
        TextView tvProjectTitle, tvProjectDescription, tvTechnologies;

        public ProjectViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProjectTitle = itemView.findViewById(R.id.tv_project_title);
            tvProjectDescription = itemView.findViewById(R.id.tv_project_description);
            tvTechnologies = itemView.findViewById(R.id.tv_technologies);
        }
    }
}