package com.suitexen.quickresume.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.suitexen.quickresume.ModelClass.ReferenceItem;
import com.suitexen.quickresume.R;
import java.util.List;

public class ReferencesAdapter extends RecyclerView.Adapter<ReferencesAdapter.ReferenceViewHolder> {


    private Context context;
    private List<ReferenceItem> referenceList;
    public static String template_tag="";

    public ReferencesAdapter(Context context, List<ReferenceItem> referenceList) {
        this.context = context;
        this.referenceList = referenceList;
    }

    @NonNull
    @Override
    public ReferenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_reference, parent, false);
        return new ReferenceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReferenceViewHolder holder, int position) {

        if(template_tag.equals("lizzie_johan")){
            holder.tvReferenceName.setTextColor(context.getResources().getColor(R.color.black_light));
            holder.tvReferenceTitle.setTextColor(context.getResources().getColor(R.color.black_light));
            holder.tvReferenceContact.setTextColor(context.getResources().getColor(R.color.black_light));

            holder.tvReferenceName.setTextSize(8);
            holder.tvReferenceTitle.setTextSize(6);
            holder.tvReferenceContact.setTextSize(6);

        }else if(template_tag.equals("david_anderson")){
            holder.tvReferenceName.setTextColor(context.getResources().getColor(R.color.black_light));
            holder.tvReferenceTitle.setTextColor(context.getResources().getColor(R.color.black_light));
            holder.tvReferenceContact.setTextColor(context.getResources().getColor(R.color.black_light));

            holder.tvReferenceName.setTextSize(8);
            holder.tvReferenceTitle.setTextSize(6);
            holder.tvReferenceContact.setTextSize(6);

        }else if(template_tag.equals("olivia_wilson")){
            holder.tvReferenceName.setTextColor(context.getResources().getColor(R.color.black_light));
            holder.tvReferenceTitle.setTextColor(context.getResources().getColor(R.color.black_light));
            holder.tvReferenceContact.setTextColor(context.getResources().getColor(R.color.black_light));

            holder.tvReferenceName.setTextSize(8);
            holder.tvReferenceTitle.setTextSize(6);
            holder.tvReferenceContact.setTextSize(6);

        }else if(template_tag.equals("jack_william")){
            holder.tvReferenceName.setTextColor(context.getResources().getColor(R.color.black_light));
            holder.tvReferenceTitle.setTextColor(context.getResources().getColor(R.color.black_light));
            holder.tvReferenceContact.setTextColor(context.getResources().getColor(R.color.black_light));

            holder.tvReferenceName.setTextSize(8);
            holder.tvReferenceTitle.setTextSize(6);
            holder.tvReferenceContact.setTextSize(6);

        }
        ReferenceItem reference = referenceList.get(position);
        holder.tvReferenceName.setText(reference.getReferenceName());
        holder.tvReferenceTitle.setText(reference.getReferencePosition() + " | " + reference. getReferenceCompany());
        holder.tvReferenceContact.setText("Email: " + reference.getReferenceEmail() + "\nPhone: " + reference.getReferenceContact());


    }

    @Override
    public int getItemCount() {
        return referenceList.size();
    }

    public static class ReferenceViewHolder extends RecyclerView.ViewHolder {
        TextView tvReferenceName, tvReferenceTitle, tvReferenceContact;

        public ReferenceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvReferenceName = itemView.findViewById(R.id.tv_reference_name);
            tvReferenceTitle = itemView.findViewById(R.id.tv_reference_title);
            tvReferenceContact = itemView.findViewById(R.id.tv_reference_contact);
        }
    }
}