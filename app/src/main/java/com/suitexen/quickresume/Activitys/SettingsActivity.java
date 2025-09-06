package com.suitexen.quickresume.Activitys;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.suitexen.quickresume.Account.LoginActivity;
import com.suitexen.quickresume.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String currentUserId;

    private TextView userName, userEmail;
    private LinearLayout changePasswordLayout, privacyPolicyLayout, contactSupportLayout,
            sendFeedbackLayout, logoutAccountLayout;
    private ImageView backBtn;
    CircleImageView profile_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeFirebase();
        initializeViews();
        loadUserData();
        loadProfileImage(); // নতুন method call
        setupClickListeners();
    }

    private void initializeFirebase() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUserId = currentUser.getUid();
        } else {
            // User not logged in, redirect to login
            redirectToLogin();
        }
    }

    private void initializeViews() {
        userName = findViewById(R.id.userName);
        userEmail = findViewById(R.id.userEmail);
        changePasswordLayout = findViewById(R.id.changePasswordLayout);
        privacyPolicyLayout = findViewById(R.id.privacyPolicyLayout);
        contactSupportLayout = findViewById(R.id.contactSupportLayout);
        sendFeedbackLayout = findViewById(R.id.sendFeedbackLayout);
        logoutAccountLayout = findViewById(R.id.logoutAccountLayout);
        profile_image = findViewById(R.id.profile_image);
        backBtn = findViewById(R.id.backBtn);
    }

    private void loadProfileImage() {
        // Method 1: Google Sign-In account থেকে profile photo load করা
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null && account.getPhotoUrl() != null) {
            loadImageWithGlide(account.getPhotoUrl());
            return;
        }

        // Method 2: Firebase User থেকে profile photo load করা
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null && user.getPhotoUrl() != null) {
            loadImageWithGlide(user.getPhotoUrl());
            return;
        }

        // Method 3: Gmail profile photo construct করা (user এর email থেকে)
        if (user != null && user.getEmail() != null) {
            loadGmailProfileImage(user.getEmail());
            return;
        }

        // Method 4: Default image set করা
        setDefaultProfileImage();
    }

    private void loadImageWithGlide(Uri photoUrl) {
        Glide.with(this)
                .load(photoUrl)
                .apply(RequestOptions.circleCropTransform())
                .placeholder(R.drawable.profile_place_holder) // আপনার default image
                .error(R.drawable.profile_place_holder)
                .into(profile_image);
    }

    private void loadGmailProfileImage(String email) {
        // Gmail profile image URL construct করা
        // এটি সবসময় কাজ নাও করতে পারে privacy settings এর কারণে
        String photoUrl = "https://www.google.com/s2/photos/profile/" + email;

        Glide.with(this)
                .load(photoUrl)
                .apply(RequestOptions.circleCropTransform())
                .placeholder(R.drawable.profile_place_holder)
                .error(R.drawable.profile_place_holder)
                .into(profile_image);
    }

    private void setDefaultProfileImage() {
        // Default profile image set করা
        profile_image.setImageResource(R.drawable.profile_place_holder);

        // অথবা user এর name এর first letter দিয়ে profile image generate করা
        generateProfileImageFromName();
    }

    private void generateProfileImageFromName() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null && user.getDisplayName() != null && !user.getDisplayName().isEmpty()) {
            String firstLetter = user.getDisplayName().substring(0, 1).toUpperCase();

            // আপনি এখানে programmatically একটি image generate করতে পারেন
            // অথবা predefined letter images use করতে পারেন

            // Example: letter based default image
            int resourceId = getLetterImageResource(firstLetter);
            if (resourceId != 0) {
                profile_image.setImageResource(resourceId);
            } else {
                profile_image.setImageResource(R.drawable.profile_place_holder);
            }
        }
    }

    private int getLetterImageResource(String letter) {
        // আপনি drawable folder এ A.png, B.png ইত্যাদি রাখতে পারেন
        String resourceName = "letter_" + letter.toLowerCase();
        int resourceId = getResources().getIdentifier(resourceName, "drawable", getPackageName());
        return resourceId;
    }

    // আপনার বাকি methods এখানে থাকবে...
    private void loadUserData() {
        if (currentUserId == null) return;

        db.collection("Users").document(currentUserId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String name = documentSnapshot.getString("name");
                        String email = documentSnapshot.getString("email");

                        userName.setText(name != null ? name : "User Name");
                        userEmail.setText(email != null ? email : "email@gmail.com");

                        // যদি Firestore এ profile image URL save করা থাকে
                        String profileImageUrl = documentSnapshot.getString("profileImageUrl");
                        if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                            loadImageWithGlide(Uri.parse(profileImageUrl));
                        }
                    } else {
                        Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load user data: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void setupClickListeners() {
        backBtn.setOnClickListener(v -> onBackPressed());
        changePasswordLayout.setOnClickListener(v -> showChangePasswordDialog());
        privacyPolicyLayout.setOnClickListener(v -> openPrivacyPolicy());
        contactSupportLayout.setOnClickListener(v -> openContactSupport());
        sendFeedbackLayout.setOnClickListener(v -> openSendFeedback());
        logoutAccountLayout.setOnClickListener(v -> showLogoutConfirmation());
    }

    // বাকি সব methods same থাকবে...
    private void showChangePasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change Password");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final EditText currentPasswordInput = new EditText(this);
        currentPasswordInput.setHint("Current Password");
        currentPasswordInput.setInputType(android.text.InputType.TYPE_CLASS_TEXT |
                android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
        layout.addView(currentPasswordInput);

        final EditText newPasswordInput = new EditText(this);
        newPasswordInput.setHint("New Password");
        newPasswordInput.setInputType(android.text.InputType.TYPE_CLASS_TEXT |
                android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
        layout.addView(newPasswordInput);

        final EditText confirmPasswordInput = new EditText(this);
        confirmPasswordInput.setHint("Confirm New Password");
        confirmPasswordInput.setInputType(android.text.InputType.TYPE_CLASS_TEXT |
                android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
        layout.addView(confirmPasswordInput);

        builder.setView(layout);

        builder.setPositiveButton("Change Password", (dialog, which) -> {
            String currentPassword = currentPasswordInput.getText().toString().trim();
            String newPassword = newPasswordInput.getText().toString().trim();
            String confirmPassword = confirmPasswordInput.getText().toString().trim();

            if (validatePasswordInputs(currentPassword, newPassword, confirmPassword)) {
                changePassword(currentPassword, newPassword);
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private boolean validatePasswordInputs(String currentPassword, String newPassword, String confirmPassword) {
        if (TextUtils.isEmpty(currentPassword)) {
            Toast.makeText(this, "Please enter your current password", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(newPassword)) {
            Toast.makeText(this, "Please enter a new password", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (newPassword.length() < 6) {
            Toast.makeText(this, "New password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this, "New passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void changePassword(String currentPassword, String newPassword) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) return;

        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);

        user.reauthenticate(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        user.updatePassword(newPassword)
                                .addOnCompleteListener(updateTask -> {
                                    if (updateTask.isSuccessful()) {
                                        updatePasswordInFirestore(newPassword);
                                        Toast.makeText(this, "Password changed successfully",
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(this, "Failed to change password: " +
                                                        updateTask.getException().getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(this, "Current password is incorrect",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updatePasswordInFirestore(String newPassword) {
        if (currentUserId == null) return;

        db.collection("Users").document(currentUserId)
                .update("password", newPassword)
                .addOnSuccessListener(aVoid -> {
                    // Password updated in Firestore successfully
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to update password in database",
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void openPrivacyPolicy() {
        String privacyPolicyUrl = "https://sites.google.com/view/quickresume/home";
        
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(privacyPolicyUrl));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Unable to open privacy policy. Please check your internet connection.", Toast.LENGTH_SHORT).show();
        }
    }

    private void openContactSupport() {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"ahmedmahin232134@gmail.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Support Request - Quick Resume App");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Hello Support Team,\n\nI need help with:\n\n");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send Email"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "No email client installed", Toast.LENGTH_SHORT).show();
        }
    }

    private void openSendFeedback() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Send Feedback");

        final EditText feedbackInput = new EditText(this);
        feedbackInput.setHint("Enter your feedback here...");
        feedbackInput.setMinLines(3);
        feedbackInput.setMaxLines(6);

        LinearLayout layout = new LinearLayout(this);
        layout.setPadding(50, 40, 50, 10);
        layout.addView(feedbackInput);
        builder.setView(layout);

        builder.setPositiveButton("Send", (dialog, which) -> {
            String feedback = feedbackInput.getText().toString().trim();
            if (!TextUtils.isEmpty(feedback)) {
                sendFeedbackToFirestore(feedback);
            } else {
                Toast.makeText(this, "Please enter your feedback", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void sendFeedbackToFirestore(String feedback) {
        if (currentUserId == null) return;

        java.util.Map<String, Object> feedbackData = new java.util.HashMap<>();
        feedbackData.put("userId", currentUserId);
        feedbackData.put("feedback", feedback);
        feedbackData.put("timestamp", com.google.firebase.firestore.FieldValue.serverTimestamp());
        feedbackData.put("userEmail", userEmail.getText().toString());

        db.collection("Feedback")
                .add(feedbackData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Feedback sent successfully. Thank you!",
                            Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to send feedback: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void showLogoutConfirmation() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_logout, null);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();

        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        // Button initialization
        Button btnLogout = dialogView.findViewById(R.id.btnLogout);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);

        btnLogout.setOnClickListener(v -> {
            performLogout();  // Your logout logic
            dialog.dismiss();
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }




    private void performLogout() {
        mAuth.signOut();
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        redirectToLogin();
    }

    private void redirectToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}