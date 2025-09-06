package com.suitexen.quickresume.Adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.suitexen.quickresume.ModelClass.SkillsItem;
import com.suitexen.quickresume.R;
import java.util.List;

public class SkillsAdapter extends RecyclerView.Adapter<SkillsAdapter.SkillViewHolder> {

    public static String template_tag = "";
    private Context context;
    private List<SkillsItem> skillsList;

    public SkillsAdapter(Context context, List<SkillsItem> skillsList) {
        this.context = context;
        this.skillsList = skillsList;
    }

    @NonNull
    @Override
    public SkillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_skill, parent, false);
        return new SkillViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SkillViewHolder holder, int position) {
        SkillsItem skill = skillsList.get(position);

        holder.tvSkill.setText(skill.getSkill());
        if (template_tag.equals("white")) {
            holder.skill_dot.setImageResource(R.drawable.circle_dot);
            holder.tvSkill.setTextColor(context.getResources().getColor(R.color.white));
        }else if (template_tag.equals("thomas_smith")) {
            holder.skill_dot.setVisibility(View.GONE);
            holder.end_skill_dot.setVisibility(View.VISIBLE);
            holder.skill_layout.setGravity(Gravity.END);
        }


    }

    @Override
    public int getItemCount() {
        return skillsList.size();
    }

    public static class SkillViewHolder extends RecyclerView.ViewHolder {
        TextView tvSkill;
        ImageView skill_dot, end_skill_dot;
        LinearLayout skill_layout;

        public SkillViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSkill = itemView.findViewById(R.id.tv_skill);
            skill_dot = itemView.findViewById(R.id.skill_dot);
            end_skill_dot = itemView.findViewById(R.id.end_skill_dot);
            skill_layout = itemView.findViewById(R.id.skill_layout);
        }
    }
}