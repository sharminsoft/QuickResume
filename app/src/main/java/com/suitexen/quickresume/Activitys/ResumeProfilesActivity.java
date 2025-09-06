package com.suitexen.quickresume.Activitys;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.suitexen.quickresume.Adapters.ResumeProfilesAdapter;
import com.suitexen.quickresume.ModelClass.ResumeProfileModel;
import com.suitexen.quickresume.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class ResumeProfilesActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    private static final String TAG = "ResumeProfilesActivity";
    private static final int PERMISSION_REQUEST_CODE = 100;

    ImageView back_btn, add_btn;
    RecyclerView resume_profilesRV;
    LinearLayout empty_state_layout;
    MaterialButton create_profile_btn;

    private FirebaseFirestore db;
    FirebaseAuth mAuth;
    String userId;

    private ArrayList<ResumeProfileModel> profileList;
    private ResumeProfilesAdapter adapter;

    private Uri selectedImageUri = null;
    private BottomSheetDialog currentBottomSheetDialog = null;
    private ImageView currentHeadshotIv = null;
    private boolean isCloudinaryInitialized = false;

    // Define ActivityResultLauncher for image picking
    private final ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    if (selectedImageUri != null && currentHeadshotIv != null) {
                        // Load the selected image into the ImageView
                        Glide.with(ResumeProfilesActivity.this)
                                .load(selectedImageUri)
                                .centerCrop()
                                .into(currentHeadshotIv);
                    }
                }
            }
    );

    // Define ActivityResultLauncher for requesting permissions
    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted) {
                    openImagePicker();
                } else {
                    Toast.makeText(this, "Permission denied. Cannot select image.", Toast.LENGTH_SHORT).show();
                }
            }
    );

    // For Android 13+ multiple permissions
    private final ActivityResultLauncher<String[]> requestMultiplePermissionsLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(),
            permissions -> {
                boolean allGranted = true;
                for (Boolean isGranted : permissions.values()) {
                    if (!isGranted) {
                        allGranted = false;
                        break;
                    }
                }
                if (allGranted) {
                    openImagePicker();
                } else {
                    Toast.makeText(this, "Permission denied. Cannot select image.", Toast.LENGTH_SHORT).show();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_resume_profiles);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        showProgressDialog("Loading Resume Profiles...");

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Null check for current user
        if (mAuth.getCurrentUser() != null) {
            userId = mAuth.getCurrentUser().getUid();
        } else {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initCloudinaryConfig();
        initViews();
        setupRecyclerView();
        setupClickListeners();
        loadResumeProfiles();
    }

    private void initViews() {
        back_btn = findViewById(R.id.back_btn);
        add_btn = findViewById(R.id.add_btn);
        resume_profilesRV = findViewById(R.id.resume_profilesRV);
        empty_state_layout = findViewById(R.id.empty_state_layout);
        create_profile_btn = findViewById(R.id.create_profile_btn);
    }

    private void setupRecyclerView() {
        profileList = new ArrayList<>();
        adapter = new ResumeProfilesAdapter(profileList);
        resume_profilesRV.setLayoutManager(new LinearLayoutManager(this));
        resume_profilesRV.setAdapter(adapter);

        // Add debug log to check if RecyclerView is properly set up
        Log.d(TAG, "RecyclerView setup complete. LayoutManager: " + resume_profilesRV.getLayoutManager());
        Log.d(TAG, "Adapter: " + resume_profilesRV.getAdapter());
    }

    private void setupClickListeners() {
        back_btn.setOnClickListener(v -> finish());

        add_btn.setOnClickListener(v -> openBottomSheet());

        create_profile_btn.setOnClickListener(v -> openBottomSheet());
    }

    private void updateEmptyState() {
        Log.d(TAG, "updateEmptyState called. Profile list size: " + profileList.size());

        if (profileList.isEmpty()) {
            resume_profilesRV.setVisibility(View.GONE);
            empty_state_layout.setVisibility(View.VISIBLE);
            Log.d(TAG, "Showing empty state");
        } else {
            resume_profilesRV.setVisibility(View.VISIBLE);
            empty_state_layout.setVisibility(View.GONE);
            Log.d(TAG, "Showing RecyclerView with " + profileList.size() + " items");
        }
    }

    private void initCloudinaryConfig() {
        if (!isCloudinaryInitialized) {
            try {
                // Check if MediaManager is already initialized
                if (MediaManager.get() == null) {
                    Map<String, String> config = new HashMap<>();
                    config.put("cloud_name", "dciejz4v7");
                    config.put("api_key", "993312848646693");
                    config.put("api_secret", "WXWvFJdu7XF3wr4GobjyyGZjNw4");
                    config.put("secure", "true");
                    MediaManager.init(this, config);
                }
                isCloudinaryInitialized = true;
                Log.d(TAG, "Cloudinary initialized successfully");
            } catch (Exception e) {
                Log.e(TAG, "Error initializing Cloudinary: " + e.getMessage(), e);
            }
        }
    }

    private void openBottomSheet() {
        openBottomSheet(null);
    }

    public void openBottomSheet(ResumeProfileModel existingProfile) {
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_resume_profile, null);
        currentBottomSheetDialog = new BottomSheetDialog(this);
        currentBottomSheetDialog.setContentView(bottomSheetView);

        CardView headshotCV = bottomSheetView.findViewById(R.id.headshotCV);
        CircleImageView headshotIv = bottomSheetView.findViewById(R.id.headshotIv);
        ImageView cameraIv = bottomSheetView.findViewById(R.id.cameraIv);
        TextInputEditText fullNameEt = bottomSheetView.findViewById(R.id.fullNameEt);
        TextInputEditText positionEt = bottomSheetView.findViewById(R.id.positionEt);
        TextInputEditText emailEt = bottomSheetView.findViewById(R.id.emailEt);
        TextInputEditText phoneEt = bottomSheetView.findViewById(R.id.phoneEt);
        TextInputEditText addressEt = bottomSheetView.findViewById(R.id.addressEt);
        TextInputEditText linkedinEt = bottomSheetView.findViewById(R.id.linkedinEt);
        TextInputEditText portfolioEt = bottomSheetView.findViewById(R.id.portfolioEt);
        TextInputEditText socialLinkEt = bottomSheetView.findViewById(R.id.socialLinkEt);
        MaterialButton saveProfileBtn = bottomSheetView.findViewById(R.id.saveProfileBtn);

        // Store the current headshotIv for use in callback
        currentHeadshotIv = headshotIv;
        selectedImageUri = null;

        if (existingProfile != null) {
            fullNameEt.setText(existingProfile.getFullName());
            positionEt.setText(existingProfile.getPosition());
            emailEt.setText(existingProfile.getEmail());
            phoneEt.setText(existingProfile.getPhone());
            addressEt.setText(existingProfile.getAddress());
            linkedinEt.setText(existingProfile.getLinkedin());
            portfolioEt.setText(existingProfile.getPortfolio());
            socialLinkEt.setText(existingProfile.getSocialLink());

            // Load existing headshot if available
            if (!TextUtils.isEmpty(existingProfile.getHeadshotUrl())) {
                Glide.with(this)
                        .load(existingProfile.getHeadshotUrl())
                        .centerCrop()
                        .into(headshotIv);
            }

            saveProfileBtn.setText("Update Profile");
        }

        // Set up camera icon click listener for image selection
        cameraIv.setOnClickListener(v -> checkPermissionAndPickImage());

        // Alternatively, allow clicking on the image itself to select a new one
        headshotIv.setOnClickListener(v -> checkPermissionAndPickImage());

        saveProfileBtn.setOnClickListener(v -> {
            String fullName = fullNameEt.getText().toString().trim();
            String position = positionEt.getText().toString().trim();
            String email = emailEt.getText().toString().trim();
            String phone = phoneEt.getText().toString().trim();
            String address = addressEt.getText().toString().trim();
            String linkedin = linkedinEt.getText().toString().trim();
            String portfolio = portfolioEt.getText().toString().trim();
            String socialLink = socialLinkEt.getText().toString().trim();

            if (TextUtils.isEmpty(fullName) || TextUtils.isEmpty(position) || TextUtils.isEmpty(email) ||
                    TextUtils.isEmpty(phone) || TextUtils.isEmpty(address)) {
                Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Show saving progress
            saveProfileBtn.setEnabled(false);
            saveProfileBtn.setText("Saving...");

            if (selectedImageUri != null) {
                // Check if Cloudinary is properly initialized before uploading
                if (!isCloudinaryInitialized) {
                    initCloudinaryConfig();
                }

                if (isCloudinaryInitialized) {
                    // Upload image first, then save profile
                    uploadImageToCloudinary(selectedImageUri, headShotUrl -> {
                        runOnUiThread(() -> {
                            if (TextUtils.isEmpty(headShotUrl)) {
                                saveProfileBtn.setEnabled(true);
                                saveProfileBtn.setText(existingProfile != null ? "Update Profile" : "Save Profile");
                                return;
                            }

                            if (existingProfile != null) {
                                updateResumeProfile(existingProfile.getResumeId(), headShotUrl, fullName, position,
                                        email, phone, address, linkedin, portfolio, socialLink, currentBottomSheetDialog);
                            } else {
                                createResumeProfile(headShotUrl, fullName, position, email, phone, address,
                                        linkedin, portfolio, socialLink, currentBottomSheetDialog);
                            }
                        });
                    });
                } else {
                    Toast.makeText(this, "Error initializing cloud storage. Saving without image.", Toast.LENGTH_SHORT).show();
                    // Save without image
                    String headShotUrl = existingProfile != null ? existingProfile.getHeadshotUrl() : "";
                    if (existingProfile != null) {
                        updateResumeProfile(existingProfile.getResumeId(), headShotUrl, fullName, position,
                                email, phone, address, linkedin, portfolio, socialLink, currentBottomSheetDialog);
                    } else {
                        createResumeProfile(headShotUrl, fullName, position, email, phone, address,
                                linkedin, portfolio, socialLink, currentBottomSheetDialog);
                    }
                }
            } else {
                // No new image selected, use existing URL or empty string
                String headShotUrl = existingProfile != null ? existingProfile.getHeadshotUrl() : "";
                if (existingProfile != null) {
                    updateResumeProfile(existingProfile.getResumeId(), headShotUrl, fullName, position,
                            email, phone, address, linkedin, portfolio, socialLink, currentBottomSheetDialog);
                } else {
                    createResumeProfile(headShotUrl, fullName, position, email, phone, address,
                            linkedin, portfolio, socialLink, currentBottomSheetDialog);
                }
            }
        });

        currentBottomSheetDialog.show();
    }

    private void checkPermissionAndPickImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13 (API level 33) and above
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                    != PackageManager.PERMISSION_GRANTED) {
                requestMultiplePermissionsLauncher.launch(new String[]{Manifest.permission.READ_MEDIA_IMAGES});
            } else {
                openImagePicker();
            }
        } else { // Below Android 13
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            } else {
                openImagePicker();
            }
        }
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageLauncher.launch(intent);
    }

    private void uploadImageToCloudinary(Uri imageUri, OnImageUploadListener listener) {
        try {
            String filename = "profile_" + UUID.randomUUID().toString();

            String requestId = MediaManager.get().upload(imageUri)
                    .option("folder", "userProfileImages")
                    .option("public_id", filename)
                    .option("resource_type", "image")
                    .callback(new UploadCallback() {
                        @Override
                        public void onStart(String requestId) {
                            Log.d(TAG, "Cloudinary upload started");
                        }

                        @Override
                        public void onProgress(String requestId, long bytes, long totalBytes) {
                            int progress = (int) ((bytes * 100) / totalBytes);
                            Log.d(TAG, "Cloudinary upload progress: " + progress + "%");
                        }

                        @Override
                        public void onSuccess(String requestId, Map resultData) {
                            Log.d(TAG, "Cloudinary upload successful: " + resultData.get("url"));
                            String secureUrl = (String) resultData.get("secure_url");
                            listener.onImageUploaded(secureUrl);
                        }

                        @Override
                        public void onError(String requestId, ErrorInfo error) {
                            Log.e(TAG, "Cloudinary upload error: " + error.getDescription());
                            runOnUiThread(() -> {
                                Toast.makeText(ResumeProfilesActivity.this,
                                        "Error uploading image: " + error.getDescription(),
                                        Toast.LENGTH_SHORT).show();
                            });
                            listener.onImageUploaded("");
                        }

                        @Override
                        public void onReschedule(String requestId, ErrorInfo error) {
                            Log.e(TAG, "Cloudinary upload rescheduled: " + error.getDescription());
                        }
                    })
                    .dispatch();
        } catch (Exception e) {
            Log.e(TAG, "Error starting Cloudinary upload: " + e.getMessage(), e);
            Toast.makeText(this, "Error uploading image", Toast.LENGTH_SHORT).show();
            listener.onImageUploaded("");
        }
    }

    // Interface to handle Cloudinary upload callback
    private interface OnImageUploadListener {
        void onImageUploaded(String imageUrl);
    }

    private void loadResumeProfiles() {
        Log.d(TAG, "loadResumeProfiles called for userId: " + userId);

        if (userId == null) {
            hideProgressDialog();
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("All Resume Profiles")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    Log.d(TAG, "Firestore query successful. Document count: " + queryDocumentSnapshots.size());

                    profileList.clear();
                    for (com.google.firebase.firestore.DocumentSnapshot document : queryDocumentSnapshots) {
                        try {
                            ResumeProfileModel profile = document.toObject(ResumeProfileModel.class);
                            if (profile != null) {
                                // Set the document ID if it's not already set
                                if (TextUtils.isEmpty(profile.getResumeId())) {
                                    profile.setResumeId(document.getId());
                                }
                                profileList.add(profile);
                                Log.d(TAG, "Added profile: " + profile.getFullName() + " - " + profile.getPosition());
                            } else {
                                Log.w(TAG, "Failed to convert document to ResumeProfileModel: " + document.getId());
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Error converting document to ResumeProfileModel: " + e.getMessage(), e);
                        }
                    }

                    Log.d(TAG, "Total profiles loaded: " + profileList.size());

                    // Notify adapter about data change
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                        Log.d(TAG, "Adapter notified of data change");
                    } else {
                        Log.e(TAG, "Adapter is null!");
                    }

                    // Update UI on main thread
                    runOnUiThread(() -> {
                        updateEmptyState();
                        hideProgressDialog();
                    });
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error loading profiles: " + e.getMessage(), e);
                    hideProgressDialog();
                    Toast.makeText(this, "Error loading profiles: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                    updateEmptyState(); // Show empty state on error too
                });
    }

    private void createResumeProfile(String headShotUrl, String fullName, String position, String email, String phone, String address,
                                     String linkedin, String portfolio, String socialLink,
                                     BottomSheetDialog dialog) {

        DocumentReference docRef = db.collection("All Resume Profiles").document();
        String resumeId = docRef.getId();

        ResumeProfileModel profileModel = new ResumeProfileModel(headShotUrl, fullName, position, email, phone, address, linkedin, portfolio, socialLink, resumeId, userId);

        docRef.set(profileModel)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Profile created successfully with ID: " + resumeId);
                    Toast.makeText(this, "Profile created successfully", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    loadResumeProfiles(); // Reload data
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error creating profile: " + e.getMessage(), e);
                    Toast.makeText(this, "Error creating profile: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                    MaterialButton saveBtn = dialog.findViewById(R.id.saveProfileBtn);
                    if (saveBtn != null) {
                        saveBtn.setEnabled(true);
                        saveBtn.setText("Save Profile");
                    }
                });
    }

    private void updateResumeProfile(String resumeId, String headShotUrl, String fullName, String position, String email, String phone, String address,
                                     String linkedin, String portfolio, String socialLink,
                                     BottomSheetDialog dialog) {
        ResumeProfileModel profileModel = new ResumeProfileModel(headShotUrl, fullName, position, email, phone, address, linkedin, portfolio, socialLink, resumeId, userId);

        db.collection("All Resume Profiles").document(resumeId)
                .set(profileModel)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Profile updated successfully with ID: " + resumeId);
                    Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    loadResumeProfiles(); // Reload data
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error updating profile: " + e.getMessage(), e);
                    Toast.makeText(this, "Error updating profile: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                    MaterialButton saveBtn = dialog.findViewById(R.id.saveProfileBtn);
                    if (saveBtn != null) {
                        saveBtn.setEnabled(true);
                        saveBtn.setText("Update Profile");
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImagePicker();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showProgressDialog(String message) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}