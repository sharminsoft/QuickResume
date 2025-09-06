package com.suitexen.quickresume.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.suitexen.quickresume.ModelClass.CertificationItem;
import com.suitexen.quickresume.R;
import java.util.List;

public class CertificationsAdapter extends RecyclerView.Adapter<CertificationsAdapter.CertificationViewHolder> {

    public static String template_tag = "";

    private Context context;
    private List<CertificationItem> certificationList;

    public CertificationsAdapter(Context context, List<CertificationItem> certificationList) {
        this.context = context;
        this.certificationList = certificationList;
    }

    @NonNull
    @Override
    public CertificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_certification, parent, false);
        return new CertificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CertificationViewHolder holder, int position) {
        CertificationItem certification = certificationList.get(position);
        holder.tvCertificationName.setText(certification.getCourseName());
        holder.tvCertificationIssuer.setText(certification.getInstituteName() + " (" + certification.getCompletionYear() + ")");


        if (template_tag.equals("white")) {
            holder.tvCertificationName.setTextColor(context.getResources().getColor(R.color.white));
            holder.tvCertificationIssuer.setTextColor(context.getResources().getColor(R.color.white));

        } else if (template_tag.equals("thomas_smith")) {

            holder.tvCertificationName.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
            holder.tvCertificationIssuer.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);


        } else if (template_tag.equals("dyrection_pesh")) {

            holder.tvCertificationName.setTextColor(context.getResources().getColor(R.color.black_shadow));
            holder.tvCertificationIssuer.setTextColor(context.getResources().getColor(R.color.black_shadow));


        } else if (template_tag.equals("jack_william")) {

            holder.tvCertificationName.setTextColor(context.getResources().getColor(R.color.black_shadow));
            holder.tvCertificationIssuer.setTextColor(context.getResources().getColor(R.color.black_shadow));


        }
    }

    @Override
    public int getItemCount() {
        return certificationList.size();
    }

    public static class CertificationViewHolder extends RecyclerView.ViewHolder {
        TextView tvCertificationName, tvCertificationIssuer;

        public CertificationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCertificationName = itemView.findViewById(R.id.tv_certification_name);
            tvCertificationIssuer = itemView.findViewById(R.id.tv_certification_issuer);
        }
    }
}