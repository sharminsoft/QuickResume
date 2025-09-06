package com.suitexen.quickresume.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.suitexen.quickresume.Adapters.TemplatesAdapter;
import com.suitexen.quickresume.ModelClass.TemplatesModel;
import com.suitexen.quickresume.R;

import java.util.ArrayList;
import java.util.List;

public class TemplateFragment extends Fragment {

    RecyclerView templateRecycler;
    ProgressBar loadingPB;
    View shimmerLayout;
    private FirebaseFirestore db;
    private List<TemplatesModel> templatesList;
    private TemplatesAdapter templatesAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_template, container, false);

        templateRecycler = view.findViewById(R.id.templateRV);
        loadingPB = view.findViewById(R.id.loadingPB);
        shimmerLayout = view.findViewById(R.id.shimmerLayout);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();
        templatesList = new ArrayList<>();

        // Setup RecyclerView
        templateRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
        templatesAdapter = new TemplatesAdapter(templatesList, getActivity(),"", "");
        templateRecycler.setAdapter(templatesAdapter);

        // Load templates from Firestore
        loadTemplates();

        return view;
    }

    private void loadTemplates() {
        // Show shimmer, hide recycler view
        shimmerLayout.setVisibility(View.VISIBLE);
        templateRecycler.setVisibility(View.GONE);

        db.collection("Templates")
                .get()
                .addOnCompleteListener(task -> {
                    // Hide shimmer, show recycler view
                    shimmerLayout.setVisibility(View.GONE);
                    templateRecycler.setVisibility(View.VISIBLE);

                    if (task.isSuccessful()) {
                        templatesList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            TemplatesModel template = document.toObject(TemplatesModel.class);
                            templatesList.add(template);
                        }
                        templatesAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), "Error loading templates", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}