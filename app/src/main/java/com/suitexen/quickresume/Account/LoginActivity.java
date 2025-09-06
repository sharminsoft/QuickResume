package com.suitexen.quickresume.Account;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.suitexen.quickresume.Activitys.MainActivity;
import com.suitexen.quickresume.R;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private TextInputEditText login_email_edit, login_pass_edit;
    private TextView text_signup;
    private Button button_login;
    private LinearLayout button_login_with_google;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private ProgressDialog progressDialog;

    private GoogleSignInClient mGoogleSignInClient;
    private ActivityResultLauncher<Intent> googleSignInLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
        initializeFirebase();
        setupGoogleSignIn();
        setupClickListeners();
    }

    private void initializeViews() {
        login_email_edit = findViewById(R.id.login_email_edit);
        login_pass_edit = findViewById(R.id.login_pass_edit);
        button_login = findViewById(R.id.button_login);
        text_signup = findViewById(R.id.text_signup);
        button_login_with_google = findViewById(R.id.button_login_with_google);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
    }

    private void initializeFirebase() {
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    private void setupGoogleSignIn() {
        // Check if Google Play Services is available first
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {
            Log.e(TAG, "Google Play Services not available");
            button_login_with_google.setEnabled(false);
            return;
        }

        try {
            // Configure Google Sign-In with proper error handling
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .requestProfile()
                    .build();

            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

            // Setup activity result launcher
            googleSignInLauncher = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> handleGoogleSignInResult(result)
            );

            Log.d(TAG, "Google Sign-In configured successfully");

        } catch (Exception e) {
            Log.e(TAG, "Error setting up Google Sign-In", e);
            button_login_with_google.setEnabled(false);
            Toast.makeText(this, "Google Sign-In setup failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupClickListeners() {
        text_signup.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });

        button_login.setOnClickListener(v -> {
            if (validate()) {
                loginWithEmail();
            }
        });

        button_login_with_google.setOnClickListener(v -> startGoogleSignIn());
    }

    private void startGoogleSignIn() {
        Log.d(TAG, "Starting Google Sign-In");

        // Show progress immediately
        showProgressDialog("Preparing Google Sign-In...");

        try {
            // Clear any previous sign-in to ensure fresh account selection
            mGoogleSignInClient.signOut().addOnCompleteListener(this, task -> {
                Log.d(TAG, "Previous sign-out completed");

                // Update progress
                updateProgressDialog("Opening Google account selector...");

                // Launch sign-in intent
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                googleSignInLauncher.launch(signInIntent);
            }).addOnFailureListener(e -> {
                Log.e(TAG, "Error during sign-out", e);
                hideProgressDialog();
                Toast.makeText(this, "Error preparing Google Sign-In", Toast.LENGTH_SHORT).show();
            });

        } catch (Exception e) {
            Log.e(TAG, "Exception in startGoogleSignIn", e);
            hideProgressDialog();
            Toast.makeText(this, "Failed to start Google Sign-In", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleGoogleSignInResult(ActivityResult result) {
        Log.d(TAG, "Google Sign-In result received. Result code: " + result.getResultCode());

        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
            updateProgressDialog("Processing Google account...");

            try {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                GoogleSignInAccount account = task.getResult(ApiException.class);

                if (account != null) {
                    Log.d(TAG, "Google Sign-In successful");
                    Log.d(TAG, "Account: " + account.getEmail());
                    Log.d(TAG, "Display Name: " + account.getDisplayName());
                    Log.d(TAG, "ID Token: " + (account.getIdToken() != null ? "Present" : "Missing"));

                    if (account.getIdToken() != null) {
                        updateProgressDialog("Authenticating with Firebase...");
                        firebaseAuthWithGoogle(account);
                    } else {
                        Log.e(TAG, "ID Token is null");
                        hideProgressDialog();
                        Toast.makeText(this, "Authentication token missing. Please try again.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.e(TAG, "Google account is null");
                    hideProgressDialog();
                    Toast.makeText(this, "Failed to get account information", Toast.LENGTH_SHORT).show();
                }

            } catch (ApiException e) {
                Log.e(TAG, "Google Sign-In failed", e);
                hideProgressDialog();
                handleGoogleSignInError(e);
            }

        } else {
            Log.d(TAG, "Google Sign-In cancelled or failed");
            hideProgressDialog();

            if (result.getResultCode() != RESULT_CANCELED) {
                Toast.makeText(this, "Google Sign-In failed. Please try again.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void handleGoogleSignInError(ApiException e) {
        String errorMessage;
        int statusCode = e.getStatusCode();

        Log.e(TAG, "Google Sign-In API Exception: " + statusCode);

        switch (statusCode) {
            case 12501: // SIGN_IN_CANCELLED
                return; // User cancelled, don't show error
            case 12500: // SIGN_IN_CURRENTLY_IN_PROGRESS
                errorMessage = "Sign-in already in progress";
                break;
            case 10: // DEVELOPER_ERROR
                errorMessage = "Configuration error. Please contact support.";
                Log.e(TAG, "DEVELOPER_ERROR - Check SHA1 fingerprint and web client ID");
                break;
            case 7: // NETWORK_ERROR
                errorMessage = "Network error. Check your internet connection.";
                break;
            case 8: // INTERNAL_ERROR
                errorMessage = "Internal error. Please try again.";
                break;
            default:
                errorMessage = "Sign-in failed. Please try again. (Error: " + statusCode + ")";
                break;
        }

        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogle: Starting Firebase authentication");
        updateProgressDialog("Signing in to your account...");

        String idToken = account.getIdToken();
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Firebase authentication successful");
                        FirebaseUser user = mAuth.getCurrentUser();

                        if (user != null) {
                            updateProgressDialog("Setting up your profile...");
                            handleSuccessfulLogin(user, account);
                        } else {
                            Log.e(TAG, "Firebase user is null after successful auth");
                            hideProgressDialog();
                            Toast.makeText(this, "Authentication succeeded but user data is missing", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Log.e(TAG, "Firebase authentication failed", task.getException());
                        hideProgressDialog();

                        String errorMessage = "Authentication failed";
                        if (task.getException() != null) {
                            errorMessage += ": " + task.getException().getMessage();
                        }
                        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void handleSuccessfulLogin(FirebaseUser user, GoogleSignInAccount googleAccount) {
        String userId = user.getUid();
        String userName = googleAccount.getDisplayName() != null ? googleAccount.getDisplayName() : "Unknown User";
        String userEmail = googleAccount.getEmail() != null ? googleAccount.getEmail() : "";
        String photoUrl = googleAccount.getPhotoUrl() != null ? googleAccount.getPhotoUrl().toString() : "";

        Log.d(TAG, "Handling successful login for user: " + userEmail);

        // Check if user exists in Firestore
        firestore.collection("Users").document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();

                        if (!document.exists()) {
                            Log.d(TAG, "Creating new user document");
                            updateProgressDialog("Creating your profile...");
                            createNewUserDocument(userId, userName, userEmail, photoUrl);
                        } else {
                            Log.d(TAG, "User document already exists");
                            updateProgressDialog("Completing sign-in...");
                            // Add small delay for smooth UX
                            new android.os.Handler().postDelayed(() -> {
                                hideProgressDialog();
                                navigateToMainActivity(userName);
                            }, 500);
                        }
                    } else {
                        Log.e(TAG, "Error checking user document", task.getException());
                        hideProgressDialog();
                        // Still navigate to main activity
                        navigateToMainActivity(userName);
                    }
                });
    }

    private void createNewUserDocument(String userId, String userName, String userEmail, String photoUrl) {
        // Create user data using Map for better reliability
        Map<String, Object> userData = new HashMap<>();
        userData.put("userId", userId);
        userData.put("name", userName);
        userData.put("email", userEmail);
        userData.put("password", ""); // Empty for Google sign-in users
        userData.put("phone", "");
        userData.put("photoUrl", photoUrl);
        userData.put("provider", "google");
        userData.put("createdAt", System.currentTimeMillis());
        userData.put("isActive", true);

        Log.d(TAG, "Creating user document with data: " + userData.toString());

        firestore.collection("Users").document(userId)
                .set(userData)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "User document created successfully");
                    hideProgressDialog();
                    Toast.makeText(this, "Welcome " + userName + "!", Toast.LENGTH_SHORT).show();
                    navigateToMainActivity(userName);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to create user document", e);
                    hideProgressDialog();
                    Toast.makeText(this, "Profile creation failed, but login successful", Toast.LENGTH_SHORT).show();
                    navigateToMainActivity(userName);
                });
    }

    private void loginWithEmail() {
        showProgressDialog("Signing in...");

        String email = login_email_edit.getText().toString().trim();
        String password = login_pass_edit.getText().toString().trim();

        Log.d(TAG, "Attempting email login for: " + email);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    hideProgressDialog();
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Email login successful");
                        FirebaseUser user = mAuth.getCurrentUser();
                        String displayName = user != null && user.getDisplayName() != null ?
                                user.getDisplayName() : "User";
                        navigateToMainActivity(displayName);
                    } else {
                        Log.e(TAG, "Email login failed", task.getException());
                        String errorMessage = "Login failed";
                        if (task.getException() != null) {
                            errorMessage += ": " + task.getException().getMessage();
                        }
                        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void navigateToMainActivity(String userName) {
        Log.d(TAG, "Navigating to MainActivity");
        Toast.makeText(this, "Welcome back!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("userName", userName);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private boolean validate() {
        boolean isValid = true;

        String email = login_email_edit.getText().toString().trim();
        String password = login_pass_edit.getText().toString().trim();

        if (email.isEmpty()) {
            login_email_edit.setError("Enter your email");
            login_email_edit.requestFocus();
            isValid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            login_email_edit.setError("Enter a valid email address");
            login_email_edit.requestFocus();
            isValid = false;
        }

        if (password.isEmpty()) {
            login_pass_edit.setError("Enter your password");
            login_pass_edit.requestFocus();
            isValid = false;
        } else if (password.length() < 6) {
            login_pass_edit.setError("Password must be at least 6 characters");
            login_pass_edit.requestFocus();
            isValid = false;
        }

        return isValid;
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is already signed in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Log.d(TAG, "User already signed in: " + currentUser.getEmail());
            String displayName = currentUser.getDisplayName() != null ?
                    currentUser.getDisplayName() : "User";
            navigateToMainActivity(displayName);
        }
    }

    // Progress Dialog Helper Methods
    private void showProgressDialog(String message) {
        if (progressDialog != null) {
            progressDialog.setMessage(message);
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
    }

    private void updateProgressDialog(String message) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.setMessage(message);
        }
    }

    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideProgressDialog();
    }
}