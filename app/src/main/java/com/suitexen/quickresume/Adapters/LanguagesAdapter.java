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
import com.suitexen.quickresume.ModelClass.LanguageItem;
import com.suitexen.quickresume.R;
import java.util.List;

public class LanguagesAdapter extends RecyclerView.Adapter<LanguagesAdapter.LanguageViewHolder> {

    public static String template_tag = "";
    private Context context;
    private List<LanguageItem> languageList;

    public LanguagesAdapter(Context context, List<LanguageItem> languageList) {
        this.context = context;
        this.languageList = languageList;
    }

    @NonNull
    @Override
    public LanguageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_language, parent, false);
        return new LanguageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LanguageViewHolder holder, int position) {
        LanguageItem language = languageList.get(position);
        holder.tv_language.setText(language.getNativeLanguage());
        if (template_tag.equals("white")) {
            holder.dot_language.setImageResource(R.drawable.circle_dot);
            holder.tv_language.setTextColor(context.getResources().getColor(R.color.white));
        } else if (template_tag.equals("thomas_smith")) {
            holder.dot_language.setVisibility(View.GONE);
            holder.end_dot_language.setVisibility(View.VISIBLE);
            holder.language_layout.setGravity(Gravity.END);
        }


    }

    @Override
    public int getItemCount() {
        return languageList.size();
    }

    public static class LanguageViewHolder extends RecyclerView.ViewHolder {
        TextView tv_language;
        ImageView dot_language, end_dot_language;
        LinearLayout language_layout;

        public LanguageViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_language = itemView.findViewById(R.id.tv_language);
            dot_language = itemView.findViewById(R.id.dot_language);
            end_dot_language = itemView.findViewById(R.id.end_dot_language);
            language_layout = itemView.findViewById(R.id.language_layout);

        }
    }
}