package com.suitexen.quickresume.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.suitexen.quickresume.ModelClass.EducationItem;
import com.suitexen.quickresume.R;
import java.util.List;

public class EducationAdapter extends RecyclerView.Adapter<EducationAdapter.EducationViewHolder> {


    public static String template_tag = "";

    private Context context;
    private List<EducationItem> educationList;

    public EducationAdapter(Context context, List<EducationItem> educationList) {
        this.context = context;
        this.educationList = educationList;
    }

    @NonNull
    @Override
    public EducationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_education, parent, false);
        return new EducationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EducationViewHolder holder, int position) {

        if (template_tag.equals("lizzie_johan")) {
            holder.tvDegree.setTextColor(context.getResources().getColor(R.color.black_light));
            holder.tvEducationDate.setTextColor(context.getResources().getColor(R.color.black_light));
            holder.tvInstitution.setTextColor(context.getResources().getColor(R.color.black_light));

            holder.tvDegree.setTextSize(8);
            holder.tvEducationDate.setTextSize(6);
            holder.tvInstitution.setTextSize(6);
        } else if (template_tag.equals("david_anderson")) {
            holder.tvDegree.setTextColor(context.getResources().getColor(R.color.black_light));
            holder.tvEducationDate.setTextColor(context.getResources().getColor(R.color.black_light));
            holder.tvInstitution.setTextColor(context.getResources().getColor(R.color.black_light));

            holder.tvDegree.setTextSize(8);
            holder.tvEducationDate.setTextSize(6);
            holder.tvInstitution.setTextSize(6);
        } else if (template_tag.equals("dyrection_pesh")) {
            holder.tvDegree.setTextColor(context.getResources().getColor(R.color.black_light));
            holder.tvEducationDate.setTextColor(context.getResources().getColor(R.color.black_light));
            holder.tvInstitution.setTextColor(context.getResources().getColor(R.color.black_light));

            holder.tvDegree.setTextSize(8);
            holder.tvEducationDate.setTextSize(6);
            holder.tvInstitution.setTextSize(6);
        }else if (template_tag.equals("olivia_wilson")) {
            holder.tvDegree.setTextColor(context.getResources().getColor(R.color.black_light));
            holder.tvEducationDate.setTextColor(context.getResources().getColor(R.color.black_light));
            holder.tvInstitution.setTextColor(context.getResources().getColor(R.color.black_light));

            holder.tvDegree.setTextSize(8);
            holder.tvEducationDate.setTextSize(6);
            holder.tvInstitution.setTextSize(6);
        }else if (template_tag.equals("jack_william")) {
            holder.tvDegree.setTextColor(context.getResources().getColor(R.color.black_light));
            holder.tvEducationDate.setTextColor(context.getResources().getColor(R.color.black_light));
            holder.tvInstitution.setTextColor(context.getResources().getColor(R.color.black_light));

            holder.tvDegree.setTextSize(8);
            holder.tvEducationDate.setTextSize(6);
            holder.tvInstitution.setTextSize(6);
        }


        EducationItem education = educationList.get(position);
        holder.tvDegree.setText(education.getDegreeName());
        String dateRange = education.getStartDate() + " - " + education.getEndDate();
        holder.tvEducationDate.setText(dateRange);
        holder.tvInstitution.setText(education.getUniversityName());

        if (education.getCgpa() == null) {
            holder.tv_cgpa.setVisibility(View.GONE);
        } else {
            holder.tv_cgpa.setVisibility(View.VISIBLE);
            holder.tv_cgpa.setText("CGPA: " + education.getCgpa());
        }


    }

    @Override
    public int getItemCount() {
        return educationList.size();
    }

    public static class EducationViewHolder extends RecyclerView.ViewHolder {
        TextView tvDegree, tvEducationDate, tvInstitution, tv_cgpa;

        public EducationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDegree = itemView.findViewById(R.id.tv_degree);
            tvEducationDate = itemView.findViewById(R.id.tv_education_date);
            tvInstitution = itemView.findViewById(R.id.tv_institution);
            tv_cgpa = itemView.findViewById(R.id.tv_cgpa);

        }
    }
}