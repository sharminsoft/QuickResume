package com.suitexen.quickresume.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.suitexen.quickresume.ModelClass.VolunteerWorkItem;
import com.suitexen.quickresume.R;
import java.util.List;

public class VolunteerWorkAdapter extends RecyclerView.Adapter<VolunteerWorkAdapter.VolunteerWorkViewHolder> {

    private Context context;
    private List<VolunteerWorkItem> volunteerWorkList;

    public static String template_tag = "";

    public VolunteerWorkAdapter(Context context, List<VolunteerWorkItem> volunteerWorkList) {
        this.context = context;
        this.volunteerWorkList = volunteerWorkList;
    }

    @NonNull
    @Override
    public VolunteerWorkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_extracurricular, parent, false);
        return new VolunteerWorkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VolunteerWorkViewHolder holder, int position) {

        if (template_tag.equals("template_olivia_wilson")) {
            holder.tvActivityTitle.setTextColor(context.getResources().getColor(R.color.black_light));
            holder.tvDuration.setTextColor(context.getResources().getColor(R.color.black_light));
            holder.tvActivityDescription.setTextColor(context.getResources().getColor(R.color.black_light));
            holder.tvActivityTitle.setTypeface(null, android.graphics.Typeface.BOLD);

            holder.tvActivityTitle.setTextSize(8);
            holder.tvDuration.setTextSize(6);
            holder.tvActivityDescription.setTextSize(6);
        }else if (template_tag.equals("david_anderson")) {
            holder.tvActivityTitle.setTextColor(context.getResources().getColor(R.color.black_light));
            holder.tvDuration.setTextColor(context.getResources().getColor(R.color.black_light));
            holder.tvActivityDescription.setTextColor(context.getResources().getColor(R.color.black_light));
            holder.tvActivityTitle.setTypeface(null, android.graphics.Typeface.BOLD);

            holder.tvActivityTitle.setTextSize(8);
            holder.tvDuration.setTextSize(6);
            holder.tvActivityDescription.setTextSize(6);
        }else if (template_tag.equals("dyrection_pesh")) {
            holder.tvActivityTitle.setTextColor(context.getResources().getColor(R.color.black_light));
            holder.tvDuration.setTextColor(context.getResources().getColor(R.color.black_light));
            holder.tvActivityDescription.setTextColor(context.getResources().getColor(R.color.black_light));
            holder.tvActivityTitle.setTypeface(null, android.graphics.Typeface.BOLD);

            holder.tvActivityTitle.setTextSize(8);
            holder.tvDuration.setTextSize(6);
            holder.tvActivityDescription.setTextSize(6);
        }else if (template_tag.equals("olivia_wilson")) {
            holder.tvActivityTitle.setTextColor(context.getResources().getColor(R.color.black_light));
            holder.tvDuration.setTextColor(context.getResources().getColor(R.color.black_light));
            holder.tvActivityDescription.setTextColor(context.getResources().getColor(R.color.black_light));
            holder.tvActivityTitle.setTypeface(null, android.graphics.Typeface.BOLD);

            holder.tvActivityTitle.setTextSize(8);
            holder.tvDuration.setTextSize(6);
            holder.tvActivityDescription.setTextSize(6);
        }else if (template_tag.equals("jack_william")) {
            holder.tvActivityTitle.setTextColor(context.getResources().getColor(R.color.black_light));
            holder.tvDuration.setTextColor(context.getResources().getColor(R.color.black_light));
            holder.tvActivityDescription.setTextColor(context.getResources().getColor(R.color.black_light));
            holder.tvActivityTitle.setTypeface(null, android.graphics.Typeface.BOLD);

            holder.tvActivityTitle.setTextSize(8);
            holder.tvDuration.setTextSize(6);
            holder.tvActivityDescription.setTextSize(6);
        }


        VolunteerWorkItem volunteerWork = volunteerWorkList.get(position);
        holder.tvActivityTitle.setText(volunteerWork.getOrganizationName());
        holder.tvDuration.setText(volunteerWork.getDuration());
        holder.tvActivityDescription.setText(volunteerWork.getDescription());
    }

    @Override
    public int getItemCount() {
        return volunteerWorkList.size();
    }

    public static class VolunteerWorkViewHolder extends RecyclerView.ViewHolder {
        TextView tvActivityTitle, tvDuration, tvActivityDescription;

        public VolunteerWorkViewHolder(@NonNull View itemView) {
            super(itemView);
            tvActivityTitle = itemView.findViewById(R.id.tv_activity_title);
            tvActivityDescription = itemView.findViewById(R.id.tv_activity_description);
            tvDuration = itemView.findViewById(R.id.tv_duration);
        }
    }
}