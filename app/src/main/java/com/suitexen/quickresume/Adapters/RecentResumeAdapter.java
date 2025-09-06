package com.suitexen.quickresume.Adapters;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

import com.suitexen.quickresume.ModelClass.RecentResumeModel;
import com.suitexen.quickresume.R;

public class RecentResumeAdapter extends RecyclerView.Adapter<RecentResumeAdapter.ViewHolder> {

    private List<RecentResumeModel> resumeList;
    private Context context;

    public RecentResumeAdapter(List<RecentResumeModel> resumeList, Context context) {
        this.resumeList = resumeList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recent_resume_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecentResumeModel resume = resumeList.get(position);
        holder.fileName.setText(resume.getFileName());
        holder.export_date_time.setText(resume.getExportDateTime());

        Bitmap thumbnail = resume.getThumbnail();
        if (thumbnail != null) {
            holder.thumbnailImage.setImageBitmap(thumbnail);
        } else {
            holder.thumbnailImage.setImageResource(R.drawable.resume_profiles); // ডিফল্ট ইমেজ
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File pdfFile = new File(resume.getFilePath());
                Uri pdfUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", pdfFile);

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(pdfUri, "application/pdf");
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                try {
                    context.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(context, "No PDF viewer installed!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return resumeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView fileName, export_date_time;
        ImageView thumbnailImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fileName = itemView.findViewById(R.id.file_name);
            thumbnailImage = itemView.findViewById(R.id.thumbnail_image);
            export_date_time = itemView.findViewById(R.id.export_date_time);
        }
    }
}
