package com.suitexen.quickresume.Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.suitexen.quickresume.Activitys.AddResumeDataActivity;
import com.suitexen.quickresume.Activitys.ResumeProfilesActivity;
import com.suitexen.quickresume.Adapters.RecentResumeAdapter;
import com.suitexen.quickresume.Adapters.TemplatesAdapter;
import com.suitexen.quickresume.ModelClass.RecentResumeModel;
import com.suitexen.quickresume.ModelClass.ResumeProfileModel;
import com.suitexen.quickresume.ModelClass.TemplatesModel;
import com.suitexen.quickresume.R;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Fragment that displays the home screen with popular templates and recent resumes.
 * Includes shimmer loading animation for better user experience.
 */
public class HomeFragment extends Fragment {

    // Constants
    private static final String TAG = "HomeFragment";
    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final int SHIMMER_DURATION = 1500;
    private static final int MAX_TEMPLATES = 10;
    private static final int MAX_RECENT_RESUMES = 10;

    // UI Components
    private CardView quickBuildCV;
    private CardView allProfilesCV;
    private RecyclerView popularTemplatesRV;
    private RecyclerView recentCreateResumeRV;
    private View mainContentView;
    private ShimmerFrameLayout shimmerLayout;

    // Firebase
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String userId;

    // Adapters and Data
    private List<TemplatesModel> templatesList;
    private TemplatesAdapter templatesAdapter;
    private List<RecentResumeModel> resumeList;
    private RecentResumeAdapter recentResumeAdapter;

    // Image Upload
    private Uri selectedImageUri = null;
    private BottomSheetDialog currentBottomSheetDialog = null;
    private ImageView currentHeadshotIv = null;
    private boolean isCloudinaryInitialized = false;

    // Loading States
    private boolean templatesLoaded = false;
    private boolean resumesLoaded = false;

    // Activity Result Launchers
    private ActivityResultLauncher<Intent> pickImageLauncher;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private ActivityResultLauncher<String[]> requestMultiplePermissionsLauncher;

    CircleImageView recently_created_empty_iv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActivityResultLaunchers();
        initCloudinaryConfig();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initFirebase();
        initViews(view);
        initShimmerLayout(view);

        // Show shimmer and load data
        showShimmerAndLoadData();

        return view;
    }

    // ============= INITIALIZATION METHODS =============

    /**
     * Initialize Firebase instances and get current user ID
     */
    private void initFirebase() {
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            userId = mAuth.getCurrentUser().getUid();
        }
    }

    /**
     * Initialize all UI views and set click listeners
     */
    private void initViews(View view) {
        quickBuildCV = view.findViewById(R.id.quick_build_CV);
        allProfilesCV = view.findViewById(R.id.all_profiles_CV);
        popularTemplatesRV = view.findViewById(R.id.popular_templatesRV);
        recentCreateResumeRV = view.findViewById(R.id.recent_create_resumeRV);
        mainContentView = view.findViewById(R.id.mainLayForShimmer);
        recently_created_empty_iv = view.findViewById(R.id.recently_created_empty_iv);

        setupClickListeners();
    }

    /**
     * Initialize shimmer layout from the included layout
     */
    private void initShimmerLayout(View view) {
        View shimmerInclude = view.findViewById(R.id.shimmer_layout);

        // The included layout should contain ShimmerFrameLayout as root
        if (shimmerInclude instanceof ShimmerFrameLayout) {
            shimmerLayout = (ShimmerFrameLayout) shimmerInclude;
        } else {
            // If the included layout has ShimmerFrameLayout as child, find it
            shimmerLayout = shimmerInclude.findViewById(R.id.shimmer_container);

            // If still not found, look for any ShimmerFrameLayout in the view hierarchy
            if (shimmerLayout == null) {
                shimmerLayout = findShimmerFrameLayout(shimmerInclude);
            }
        }

        if (shimmerLayout == null) {
            Log.e(TAG, "ShimmerFrameLayout not found in layout");
        }
    }

    /**
     * Recursively find ShimmerFrameLayout in view hierarchy
     */
    private ShimmerFrameLayout findShimmerFrameLayout(View view) {
        if (view instanceof ShimmerFrameLayout) {
            return (ShimmerFrameLayout) view;
        }

        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                ShimmerFrameLayout found = findShimmerFrameLayout(group.getChildAt(i));
                if (found != null) {
                    return found;
                }
            }
        }

        return null;
    }

    /**
     * Setup click listeners for UI components
     */
    private void setupClickListeners() {
        quickBuildCV.setOnClickListener(v -> openBottomSheet());
        allProfilesCV.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), ResumeProfilesActivity.class)));
    }

    /**
     * Initialize activity result launchers for permissions and image picking
     */
    private void initActivityResultLaunchers() {
        // Image picker launcher
        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        if (selectedImageUri != null && currentHeadshotIv != null) {
                            loadImageIntoView(selectedImageUri, currentHeadshotIv);
                        }
                    }
                }
        );

        // Single permission launcher
        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        openImagePicker();
                    } else {
                        showToast("Permission denied. Cannot select image.");
                    }
                }
        );

        // Multiple permissions launcher (Android 13+)
        requestMultiplePermissionsLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                permissions -> {
                    boolean allGranted = permissions.values().stream().allMatch(granted -> granted);
                    if (allGranted) {
                        openImagePicker();
                    } else {
                        showToast("Permission denied. Cannot select image.");
                    }
                }
        );
    }

    // ============= SHIMMER METHODS =============

    /**
     * Show shimmer animation and start loading data
     */
    private void showShimmerAndLoadData() {
        showShimmer();
        resetLoadingStates();
        loadAllData();
    }

    /**
     * Show shimmer layout and hide main content
     */
    private void showShimmer() {
        if (shimmerLayout != null && mainContentView != null) {
            mainContentView.setVisibility(View.GONE);
            shimmerLayout.setVisibility(View.VISIBLE);
            shimmerLayout.startShimmer();

            Log.d(TAG, "Shimmer started");
        } else {
            Log.e(TAG, "Cannot show shimmer - shimmerLayout or mainContentView is null");
        }
    }

    /**
     * Hide shimmer layout and show main content
     */
    private void hideShimmer() {
        if (shimmerLayout != null && mainContentView != null) {
            shimmerLayout.stopShimmer();
            shimmerLayout.setVisibility(View.GONE);
            mainContentView.setVisibility(View.VISIBLE);

            Log.d(TAG, "Shimmer stopped, content shown");
        } else {
            Log.e(TAG, "Cannot hide shimmer - shimmerLayout or mainContentView is null");
        }
    }

    /**
     * Reset loading states for both templates and resumes
     */
    private void resetLoadingStates() {
        templatesLoaded = false;
        resumesLoaded = false;
    }

    /**
     * Check if both data sets are loaded and hide shimmer if so
     */
    private void checkAndHideShimmer() {
        if (templatesLoaded && resumesLoaded) {
            // Add small delay for better UX
            new Handler(Looper.getMainLooper()).postDelayed(this::hideShimmer, 300);
        }
    }

    // ============= DATA LOADING METHODS =============

    /**
     * Load both templates and resumes simultaneously
     */
    private void loadAllData() {
        loadPopularTemplates();
        loadRecentCreatedResumes();
    }

    /**
     * Load popular templates from Firebase Firestore
     */
    /**
     * Load popular templates from Firebase Firestore
     */
    private void loadPopularTemplates() {
        // Initialize list safely
        if (templatesList == null) {
            templatesList = new ArrayList<>();
        } else {
            templatesList.clear();
        }

        // Setup RecyclerView
        setupTemplatesRecyclerView();

        // Remove the showShimmer() call from here - it's already shown in showShimmerAndLoadData()

        // Add delay for shimmer visibility
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // Check if fragment is still attached
            if (!isAdded() || getContext() == null) {
                templatesLoaded = true; // Mark as loaded to prevent infinite waiting
                checkAndHideShimmer();
                return;
            }

            db.collection("Templates")
                    .orderBy("template_score", Query.Direction.DESCENDING)
                    .limit(MAX_TEMPLATES)
                    .get()
                    .addOnCompleteListener(task -> {
                        // Check if fragment is still attached
                        if (!isAdded() || getContext() == null) {
                            templatesLoaded = true;
                            checkAndHideShimmer();
                            return;
                        }

                        if (task.isSuccessful() && task.getResult() != null) {
                            // Clear list safely
                            if (templatesList != null) {
                                templatesList.clear();

                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    try {
                                        TemplatesModel template = document.toObject(TemplatesModel.class);
                                        if (template != null && templatesList != null) {
                                            templatesList.add(template);
                                        }
                                    } catch (Exception e) {
                                        Log.e(TAG, "Error parsing template: " + e.getMessage());
                                    }
                                }

                                // Notify adapter safely
                                if (templatesAdapter != null) {
                                    templatesAdapter.notifyDataSetChanged();
                                }

                                if (templatesList.isEmpty()) {
                                    Log.d(TAG, "No templates found");
                                } else {
                                    Log.d(TAG, "Templates loaded: " + templatesList.size());
                                }
                            }
                        } else {
                            Log.e(TAG, "Error loading templates", task.getException());
                        }

                        // Mark templates as loaded
                        templatesLoaded = true;
                        checkAndHideShimmer();
                    })
                    .addOnFailureListener(e -> {
                        // Check if fragment is still attached
                        if (!isAdded() || getContext() == null) {
                            templatesLoaded = true;
                            checkAndHideShimmer();
                            return;
                        }

                        Log.e(TAG, "Failed to load templates: " + e.getMessage());

                        // Mark as loaded even on failure to hide shimmer
                        templatesLoaded = true;
                        checkAndHideShimmer();
                    });
        }, SHIMMER_DURATION);
    }

    /**
     * Setup RecyclerView for templates
     */
    private void setupTemplatesRecyclerView() {
        if (popularTemplatesRV != null && getContext() != null && isAdded()) {
            if (templatesAdapter == null && templatesList != null) {
                templatesAdapter = new TemplatesAdapter(templatesList, getActivity(), "popularHome", "");
                popularTemplatesRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                popularTemplatesRV.setAdapter(templatesAdapter);
            }
        }
    }

    /**
     * Load recent resumes from local storage
     */
    /**
     * Load recent resumes from local storage
     */
    private void loadRecentCreatedResumes() {
        // Add delay for shimmer visibility
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // Check if fragment is still attached
            if (!isAdded() || getContext() == null) {
                resumesLoaded = true;
                checkAndHideShimmer();
                return;
            }

            if (resumeList == null) {
                resumeList = new ArrayList<>();
            } else {
                resumeList.clear();
            }

            loadPDFFilesFromStorage();
            setupResumesRecyclerView();

            if (resumeList.isEmpty()) {
                Log.d(TAG, "No recent resumes found");
                recently_created_empty_iv.setVisibility(View.VISIBLE);

            } else {
                Log.d(TAG, "Resumes loaded: " + resumeList.size());
                recently_created_empty_iv.setVisibility(View.GONE);
            }

            // Mark resumes as loaded
            resumesLoaded = true;
            checkAndHideShimmer();
        }, SHIMMER_DURATION + 500); // Slightly different timing for staggered loading
    }

    /**
     * Load PDF files from QuickResume directory
     */
    private void loadPDFFilesFromStorage() {
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File quickResumeDir = new File(storageDir, "QuickResume");

        if (!quickResumeDir.exists() || !quickResumeDir.isDirectory()) {
            return;
        }

        File[] files = quickResumeDir.listFiles((dir, name) ->
                name.toLowerCase().endsWith(".pdf"));

        if (files == null || files.length == 0) {
            return;
        }

        // Sort by last modified date (newest first)
        Arrays.sort(files, (file1, file2) ->
                Long.compare(file2.lastModified(), file1.lastModified()));

        // Load maximum recent resumes
        int limit = Math.min(files.length, MAX_RECENT_RESUMES);
        for (int i = 0; i < limit; i++) {
            File file = files[i];
            Bitmap thumbnail = generatePDFThumbnail(file);
            String formattedDate = formatDate(file.lastModified());

            resumeList.add(new RecentResumeModel(
                    file.getName(),
                    file.getAbsolutePath(),
                    thumbnail,
                    formattedDate));
        }
    }

    /**
     * Setup RecyclerView for recent resumes
     */
    private void setupResumesRecyclerView() {
        recentCreateResumeRV.setLayoutManager(new LinearLayoutManager(getContext()));
        recentResumeAdapter = new RecentResumeAdapter(resumeList, getActivity());
        recentCreateResumeRV.setAdapter(recentResumeAdapter);
    }

    // ============= UTILITY METHODS =============

    /**
     * Generate PDF thumbnail from first page
     */
    private Bitmap generatePDFThumbnail(File pdfFile) {
        try {
            ParcelFileDescriptor fileDescriptor = ParcelFileDescriptor.open(
                    pdfFile, ParcelFileDescriptor.MODE_READ_ONLY);
            PdfRenderer pdfRenderer = new PdfRenderer(fileDescriptor);
            PdfRenderer.Page page = pdfRenderer.openPage(0);

            Bitmap bitmap = Bitmap.createBitmap(
                    page.getWidth(),
                    page.getHeight(),
                    Bitmap.Config.ARGB_8888);
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

            page.close();
            pdfRenderer.close();
            fileDescriptor.close();

            return bitmap;
        } catch (IOException e) {
            Log.e(TAG, "Error generating PDF thumbnail", e);
            return null;
        }
    }

    /**
     * Format last modified date for display
     */
    private String formatDate(long lastModified) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault());
        return sdf.format(new Date(lastModified));
    }

    /**
     * Show toast message
     */
    private void showToast(String message) {
        if (getActivity() != null) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Load image into ImageView using Glide
     */
    private void loadImageIntoView(Uri imageUri, ImageView imageView) {
        Glide.with(getActivity())
                .load(imageUri)
                .centerCrop()
                .into(imageView);
    }

    // ============= CLOUDINARY & IMAGE UPLOAD =============

    /**
     * Initialize Cloudinary configuration
     */
    private void initCloudinaryConfig() {
        if (!isCloudinaryInitialized) {
            try {
                Map<String, String> config = new HashMap<>();
                config.put("cloud_name", "dciejz4v7");
                config.put("api_key", "993312848646693");
                config.put("api_secret", "WXWvFJdu7XF3wr4GobjyyGZjNw4");
                config.put("secure", "true");
                MediaManager.init(getActivity(), config);
                isCloudinaryInitialized = true;
                Log.d(TAG, "Cloudinary initialized successfully");
            } catch (Exception e) {
                Log.e(TAG, "Error initializing Cloudinary", e);
            }
        }
    }

    /**
     * Check permissions and open image picker
     */
    private void checkPermissionAndPickImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+ uses READ_MEDIA_IMAGES
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                requestMultiplePermissionsLauncher.launch(
                        new String[]{Manifest.permission.READ_MEDIA_IMAGES});
            } else {
                openImagePicker();
            }
        } else {
            // Below Android 13 uses READ_EXTERNAL_STORAGE
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            } else {
                openImagePicker();
            }
        }
    }

    /**
     * Open image picker for selecting profile image
     */
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageLauncher.launch(intent);
    }

    // ============= BOTTOM SHEET METHODS =============

    /**
     * Open bottom sheet for creating new profile
     */
    private void openBottomSheet() {
        openBottomSheet(null);
    }

    /**
     * Open bottom sheet for creating or editing profile
     */
    public void openBottomSheet(ResumeProfileModel existingProfile) {
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_resume_profile, null);
        currentBottomSheetDialog = new BottomSheetDialog(getActivity());
        currentBottomSheetDialog.setContentView(bottomSheetView);

        initBottomSheetViews(bottomSheetView, existingProfile);
        currentBottomSheetDialog.show();
    }

    /**
     * Initialize bottom sheet views and set up listeners
     */
    private void initBottomSheetViews(View bottomSheetView, ResumeProfileModel existingProfile) {
        // Find views
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

        // Store reference for image callback
        currentHeadshotIv = headshotIv;
        selectedImageUri = null;

        // Populate existing data if editing
        if (existingProfile != null) {
            populateExistingProfileData(existingProfile, headshotIv, fullNameEt, positionEt,
                    emailEt, phoneEt, addressEt, linkedinEt, portfolioEt, socialLinkEt, saveProfileBtn);
        }

        // Set up image selection listeners
        cameraIv.setOnClickListener(v -> checkPermissionAndPickImage());
        headshotIv.setOnClickListener(v -> checkPermissionAndPickImage());

        // Set up save button listener
        saveProfileBtn.setOnClickListener(v -> handleSaveProfile(
                existingProfile, fullNameEt, positionEt, emailEt, phoneEt,
                addressEt, linkedinEt, portfolioEt, socialLinkEt, saveProfileBtn));
    }

    /**
     * Populate existing profile data in bottom sheet
     */
    private void populateExistingProfileData(ResumeProfileModel existingProfile,
                                             CircleImageView headshotIv, TextInputEditText fullNameEt, TextInputEditText positionEt,
                                             TextInputEditText emailEt, TextInputEditText phoneEt, TextInputEditText addressEt,
                                             TextInputEditText linkedinEt, TextInputEditText portfolioEt,
                                             TextInputEditText socialLinkEt, MaterialButton saveProfileBtn) {

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
            Glide.with(getActivity())
                    .load(existingProfile.getHeadshotUrl())
                    .centerCrop()
                    .into(headshotIv);
        }

        saveProfileBtn.setText("Update Profile");
    }

    /**
     * Handle save profile button click
     */
    private void handleSaveProfile(ResumeProfileModel existingProfile,
                                   TextInputEditText fullNameEt, TextInputEditText positionEt, TextInputEditText emailEt,
                                   TextInputEditText phoneEt, TextInputEditText addressEt, TextInputEditText linkedinEt,
                                   TextInputEditText portfolioEt, TextInputEditText socialLinkEt, MaterialButton saveProfileBtn) {

        // Get input values
        String fullName = fullNameEt.getText().toString().trim();
        String position = positionEt.getText().toString().trim();
        String email = emailEt.getText().toString().trim();
        String phone = phoneEt.getText().toString().trim();
        String address = addressEt.getText().toString().trim();
        String linkedin = linkedinEt.getText().toString().trim();
        String portfolio = portfolioEt.getText().toString().trim();
        String socialLink = socialLinkEt.getText().toString().trim();

        // Validate required fields
        if (!validateRequiredFields(fullName, position, email, phone, address)) {
            return;
        }

        // Show saving state
        saveProfileBtn.setEnabled(false);
        saveProfileBtn.setText("Saving...");

        // Handle image upload and profile saving
        if (selectedImageUri != null) {
            uploadImageAndSaveProfile(existingProfile, selectedImageUri, fullName, position,
                    email, phone, address, linkedin, portfolio, socialLink, saveProfileBtn);
        } else {
            String headShotUrl = existingProfile != null ? existingProfile.getHeadshotUrl() : "";
            saveProfile(existingProfile, headShotUrl, fullName, position, email, phone,
                    address, linkedin, portfolio, socialLink, saveProfileBtn);
        }
    }

    /**
     * Validate required fields
     */
    private boolean validateRequiredFields(String fullName, String position, String email,
                                           String phone, String address) {
        if (TextUtils.isEmpty(fullName) || TextUtils.isEmpty(position) ||
                TextUtils.isEmpty(email) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(address)) {
            showToast("Please fill all required fields");
            return false;
        }
        return true;
    }

    /**
     * Upload image to Cloudinary and then save profile
     */
    private void uploadImageAndSaveProfile(ResumeProfileModel existingProfile, Uri imageUri,
                                           String fullName, String position, String email, String phone, String address,
                                           String linkedin, String portfolio, String socialLink, MaterialButton saveProfileBtn) {

        uploadImageToCloudinary(imageUri, headShotUrl -> {
            saveProfile(existingProfile, headShotUrl, fullName, position, email, phone,
                    address, linkedin, portfolio, socialLink, saveProfileBtn);
        });
    }

    /**
     * Upload image to Cloudinary
     */
    private void uploadImageToCloudinary(Uri imageUri, OnImageUploadListener listener) {
        String filename = "profile_" + UUID.randomUUID().toString();

        MediaManager.get().upload(imageUri)
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
                        Log.d(TAG, "Upload progress: " + progress + "%");
                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        Log.d(TAG, "Upload successful");
                        String secureUrl = (String) resultData.get("secure_url");
                        listener.onImageUploaded(secureUrl);
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        Log.e(TAG, "Upload error: " + error.getDescription());
                        showToast("Error uploading image: " + error.getDescription());
                        listener.onImageUploaded(""); // Pass empty string on error
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {
                        Log.w(TAG, "Upload rescheduled: " + error.getDescription());
                    }
                })
                .dispatch();
    }

    /**
     * Save or update profile in Firestore
     */
    private void saveProfile(ResumeProfileModel existingProfile, String headShotUrl,
                             String fullName, String position, String email, String phone, String address,
                             String linkedin, String portfolio, String socialLink, MaterialButton saveProfileBtn) {

        if (existingProfile != null) {
            updateResumeProfile(existingProfile.getResumeId(), headShotUrl, fullName, position,
                    email, phone, address, linkedin, portfolio, socialLink, saveProfileBtn);
        } else {
            createResumeProfile(headShotUrl, fullName, position, email, phone, address,
                    linkedin, portfolio, socialLink, saveProfileBtn);
        }
    }

    /**
     * Create new resume profile
     */
    private void createResumeProfile(String headShotUrl, String fullName, String position,
                                     String email, String phone, String address, String linkedin, String portfolio,
                                     String socialLink, MaterialButton saveProfileBtn) {

        // Create document reference to get ID
        DocumentReference docRef = db.collection("All Resume Profiles").document();
        String resumeId = docRef.getId();

        // Create profile model
        ResumeProfileModel profileModel = new ResumeProfileModel(headShotUrl, fullName, position,
                email, phone, address, linkedin, portfolio, socialLink, resumeId, userId);

        // Save to Firestore
        docRef.set(profileModel)
                .addOnSuccessListener(aVoid -> {
                    showToast("Profile created successfully");
                    dismissBottomSheetAndNavigate(resumeId, fullName);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error creating profile", e);
                    showToast("Error creating profile: " + e.getMessage());
                    resetSaveButton(saveProfileBtn);
                });
    }

    /**
     * Update existing resume profile
     */
    private void updateResumeProfile(String resumeId, String headShotUrl, String fullName,
                                     String position, String email, String phone, String address, String linkedin,
                                     String portfolio, String socialLink, MaterialButton saveProfileBtn) {

        // Create updated profile model
        ResumeProfileModel profileModel = new ResumeProfileModel(headShotUrl, fullName, position,
                email, phone, address, linkedin, portfolio, socialLink, resumeId, userId);

        // Update in Firestore
        db.collection("All Resume Profiles").document(resumeId)
                .set(profileModel)
                .addOnSuccessListener(aVoid -> {
                    showToast("Profile updated successfully");
                    dismissBottomSheetAndNavigate(resumeId, fullName);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error updating profile", e);
                    showToast("Error updating profile: " + e.getMessage());
                    resetSaveButton(saveProfileBtn);
                });
    }

    /**
     * Dismiss bottom sheet and navigate to AddResumeDataActivity
     */
    private void dismissBottomSheetAndNavigate(String resumeId, String fullName) {
        if (currentBottomSheetDialog != null) {
            currentBottomSheetDialog.dismiss();
        }

        Intent intent = new Intent(getActivity(), AddResumeDataActivity.class);
        intent.putExtra("resumeId", resumeId);
        intent.putExtra("fullName", fullName);
        startActivity(intent);
    }

    /**
     * Reset save button to original state
     */
    private void resetSaveButton(MaterialButton saveProfileBtn) {
        saveProfileBtn.setEnabled(true);
        saveProfileBtn.setText("Save Profile");
    }

    // ============= INTERFACE FOR IMAGE UPLOAD =============

    /**
     * Interface to handle Cloudinary upload callback
     */
    private interface OnImageUploadListener {
        void onImageUploaded(String imageUrl);
    }

    // ============= LIFECYCLE METHODS =============

    @Override
    public void onResume() {
        super.onResume();
        // Restart shimmer if it's visible
        if (shimmerLayout != null && shimmerLayout.getVisibility() == View.VISIBLE) {
            shimmerLayout.startShimmer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // Stop shimmer to save battery
        if (shimmerLayout != null) {
            shimmerLayout.stopShimmer();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Stop shimmer animations
        if (shimmerLayout != null) {
            shimmerLayout.stopShimmer();
        }

        // Clean up references to prevent memory leaks
        shimmerLayout = null;
        mainContentView = null;
        currentBottomSheetDialog = null;
        currentHeadshotIv = null;
        templatesList = null;
        templatesAdapter = null;
        resumeList = null;
        recentResumeAdapter = null;
    }

    // ============= PUBLIC METHODS FOR EXTERNAL ACCESS =============

    /**
     * Refresh data and show shimmer animation
     * Can be called from parent activity if needed
     */
    public void refreshData() {
        if (shimmerLayout != null && mainContentView != null) {
            showShimmerAndLoadData();
        }
    }

    /**
     * Method for testing shimmer animation
     */
    public void showShimmerForTesting() {
        showShimmer();
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            templatesLoaded = true;
            resumesLoaded = true;
            checkAndHideShimmer();
        }, 3000);
    }

    // ============= LEGACY PERMISSION HANDLING =============

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImagePicker();
            } else {
                showToast("Permission denied");
            }
        }
    }
}