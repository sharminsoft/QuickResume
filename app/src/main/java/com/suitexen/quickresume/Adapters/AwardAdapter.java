package com.suitexen.quickresume.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.suitexen.quickresume.ModelClass.AwardItem;
import com.suitexen.quickresume.R;

import java.util.List;

public class AwardAdapter extends RecyclerView.Adapter<AwardAdapter.AwardViewHolder> {

    public static String template_tag = "";

    Context context;
    List<AwardItem> awardItemList;



    public AwardAdapter(Context context, List<AwardItem> awardItemList) {
        this.context = context;
        this.awardItemList = awardItemList;
    }

    @NonNull
    @Override
    public AwardAdapter.AwardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_award, parent, false);

        return new AwardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AwardAdapter.AwardViewHolder holder, int position) {

        AwardItem awardItem = awardItemList.get(position);
        holder.tvAwardName.setText(awardItem.getAwardName());
        holder.tvAwardDescription.setText(awardItem.getAwardDescription());
        holder.tvAwardReceived.setText(awardItem.getYearReceived());

        if (template_tag.equals("lizzie_johan")) {
            holder.tvAwardName.setTextColor(context.getResources().getColor(R.color.black_light));
            holder.tvAwardDescription.setTextColor(context.getResources().getColor(R.color.black_light));
            holder.tvAwardReceived.setTextColor(context.getResources().getColor(R.color.black_light));

            holder.tvAwardName.setTextSize(8);
            holder.tvAwardDescription.setTextSize(6);
            holder.tvAwardReceived.setTextSize(6);
        } else if (template_tag.equals("david_anderson")) {

            holder.tvAwardName.setTextColor(context.getResources().getColor(R.color.white));
            holder.tvAwardDescription.setTextColor(context.getResources().getColor(R.color.white));
            holder.tvAwardReceived.setTextColor(context.getResources().getColor(R.color.white));
            holder.tvAwardName.setTextSize(8);
            holder.tvAwardDescription.setTextSize(6);
            holder.tvAwardReceived.setTextSize(6);

        } else if (template_tag.equals("olivia_wilson")) {

            holder.tvAwardName.setTextColor(context.getResources().getColor(R.color.black_shadow));
            holder.tvAwardDescription.setTextColor(context.getResources().getColor(R.color.black_shadow));
            holder.tvAwardReceived.setTextColor(context.getResources().getColor(R.color.black_shadow));
            holder.tvAwardName.setTextSize(8);
            holder.tvAwardDescription.setTextSize(6);
            holder.tvAwardReceived.setTextSize(6);

        } else if (template_tag.equals("dyrection_pesh")) {

            holder.tvAwardName.setTextColor(context.getResources().getColor(R.color.black_shadow));
            holder.tvAwardDescription.setTextColor(context.getResources().getColor(R.color.black_shadow));
            holder.tvAwardReceived.setTextColor(context.getResources().getColor(R.color.black_shadow));
            holder.tvAwardName.setTextSize(8);
            holder.tvAwardDescription.setTextSize(6);
            holder.tvAwardReceived.setTextSize(6);

        } else if (template_tag.equals("jack_william")) {

            holder.tvAwardName.setTextColor(context.getResources().getColor(R.color.black_shadow));
            holder.tvAwardDescription.setTextColor(context.getResources().getColor(R.color.black_shadow));
            holder.tvAwardReceived.setTextColor(context.getResources().getColor(R.color.black_shadow));
            holder.tvAwardName.setTextSize(8);
            holder.tvAwardDescription.setTextSize(6);
            holder.tvAwardReceived.setTextSize(6);

        } else if (template_tag.equals("white")) {

            holder.tvAwardName.setTextColor(context.getResources().getColor(R.color.white));
            holder.tvAwardDescription.setTextColor(context.getResources().getColor(R.color.white));
            holder.tvAwardReceived.setTextColor(context.getResources().getColor(R.color.white));
            holder.tvAwardName.setTextSize(8);
            holder.tvAwardDescription.setTextSize(6);
            holder.tvAwardReceived.setTextSize(6);

        }

    }

    @Override
    public int getItemCount() {
        return awardItemList.size();
    }

    public class AwardViewHolder extends RecyclerView.ViewHolder {

        TextView tvAwardName, tvAwardDescription, tvAwardReceived;

        public AwardViewHolder(@NonNull View itemView) {
            super(itemView);

            tvAwardName = itemView.findViewById(R.id.tv_award_name);
            tvAwardDescription = itemView.findViewById(R.id.tv_award_description);
            tvAwardReceived = itemView.findViewById(R.id.tv_award_received);
        }
    }
}
