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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;
import com.suitexen.quickresume.Activitys.MainActivity;
import com.suitexen.quickresume.R;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    private TextView text_login, emailFilterTxt, phoneFilterTxt;
    private TextInputEditText name_edt, email_edt, password_edt, confirm_password_edt, phone_edt;
    private Button button_signup;
    private LinearLayout phone_layout;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private ProgressDialog progressDialog;
    // private String verificationId;
    // private PhoneAuthProvider.ForceResendingToken resendToken;

    private String emailOrPhone = "";
    // private CountryCodePicker ccp;

    // Store user data temporarily
    private String tempName, tempEmail, tempPhone, tempPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
        setupFirebase();
        setupClickListeners();
        setupInitialUIState();
    }

    private void initializeViews() {
        text_login = findViewById(R.id.text_login);
        name_edt = findViewById(R.id.name_edt);
        email_edt = findViewById(R.id.email_edt);
        password_edt = findViewById(R.id.password_edt);
        confirm_password_edt = findViewById(R.id.confirm_password_edt);
        button_signup = findViewById(R.id.button_signup);
        emailFilterTxt = findViewById(R.id.emailFilterTxt);
        phoneFilterTxt = findViewById(R.id.phoneFilterTxt);
        phone_edt = findViewById(R.id.phone_edt);
        // ccp = findViewById(R.id.ccp);
        phone_layout = findViewById(R.id.phone_layout);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
    }

    private void setupFirebase() {
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    private void setupClickListeners() {
        text_login.setOnClickListener(v -> finish());

        button_signup.setOnClickListener(v -> {
            if (validate()) {
                // Store user data temporarily
                storeTemporaryData();

                // if (emailOrPhone.equals("email")) {
                sendEmailVerification();
                // } else {
                //     startPhoneVerification();
                // }
            }
        });

        // emailFilterTxt.setOnClickListener(v -> switchToEmailAuth());
        // phoneFilterTxt.setOnClickListener(v -> switchToPhoneAuth());
    }

    private void storeTemporaryData() {
        tempName = name_edt.getText().toString().trim();
        tempPassword = password_edt.getText().toString().trim();

        // if (emailOrPhone.equals("email")) {
        tempEmail = email_edt.getText().toString().trim();
        tempPhone = "";
        // } else {
        //     tempPhone = ccp.getSelectedCountryCodeWithPlus() + phone_edt.getText().toString().trim();
        //     tempEmail = "";
        // }

        Log.d(TAG, "Temporary data stored - Name: " + tempName + ", Email: " + tempEmail + ", Phone: " + tempPhone);
    }

    private void setupInitialUIState() {
        emailFilterTxt.setBackgroundResource(R.drawable.emai_phone_filter_selected);
        emailFilterTxt.setTextColor(getResources().getColor(R.color.white));
        phoneFilterTxt.setTextColor(getResources().getColor(R.color.primary_dark));
        phoneFilterTxt.setBackgroundResource(R.drawable.email_phone_filter_unselected);
        emailOrPhone = "email";
        phone_layout.setVisibility(View.GONE);
        email_edt.setVisibility(View.VISIBLE);
    }

    // private void switchToEmailAuth() {
    //     emailFilterTxt.setBackgroundResource(R.drawable.emai_phone_filter_selected);
    //     emailFilterTxt.setTextColor(getResources().getColor(R.color.white));
    //     phoneFilterTxt.setTextColor(getResources().getColor(R.color.primary_dark));
    //     phoneFilterTxt.setBackgroundResource(R.drawable.email_phone_filter_unselected);
    //     emailOrPhone = "email";
    //     phone_layout.setVisibility(View.GONE);
    //     email_edt.setVisibility(View.VISIBLE);
    // }

    // private void switchToPhoneAuth() {
    //     emailFilterTxt.setBackgroundResource(R.drawable.email_phone_filter_unselected);
    //     phoneFilterTxt.setTextColor(getResources().getColor(R.color.white));
    //     emailFilterTxt.setTextColor(getResources().getColor(R.color.primary_dark));
    //     phoneFilterTxt.setBackgroundResource(R.drawable.emai_phone_filter_selected);
    //     emailOrPhone = "phone";
    //     email_edt.setVisibility(View.GONE);
    //     phone_layout.setVisibility(View.VISIBLE);
    // }

    private void sendEmailVerification() {
        progressDialog.setMessage("Creating account...");
        progressDialog.show();

        String email = tempEmail;
        String password = tempPassword;

        Log.d(TAG, "Creating account with email: " + email);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            Log.d(TAG, "Account created successfully. User ID: " + user.getUid());
                            progressDialog.setMessage("Saving user data...");
                            saveUserDataToFirestore(user.getUid());
                            /*
                            user.sendEmailVerification()
                                    .addOnCompleteListener(verificationTask -> {
                                        if (verificationTask.isSuccessful()) {
                                            Log.d(TAG, "Verification email sent successfully");
                                            progressDialog.setMessage("Please check your email and click the verification link...");
                                            checkEmailVerification(user);
                                        } else {
                                            progressDialog.dismiss();
                                            Log.e(TAG, "Failed to send verification email", verificationTask.getException());
                                            Toast.makeText(SignupActivity.this,
                                                    "Failed to send verification email: " +
                                                            (verificationTask.getException() != null ?
                                                                    verificationTask.getException().getMessage() : "Unknown error"),
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    });
                            */
                        }
                    } else {
                        progressDialog.dismiss();
                        Log.e(TAG, "Account creation failed", task.getException());
                        String errorMessage = task.getException() != null ?
                                task.getException().getMessage() : "Registration failed";
                        Toast.makeText(SignupActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
    }

    /*
    private void checkEmailVerification(FirebaseUser user) {
        Handler handler = new Handler();
        final int[] checkCount = {0}; // To prevent infinite checking

        Runnable checkVerification = new Runnable() {
            @Override
            public void run() {
                checkCount[0]++;
                Log.d(TAG, "Checking email verification - Attempt: " + checkCount[0]);

                user.reload().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (user.isEmailVerified()) {
                            Log.d(TAG, "Email verified successfully");
                            progressDialog.setMessage("Email verified! Saving user data...");
                            saveUserDataToFirestore(user.getUid());
                        } else if (checkCount[0] < 30) { // Check for maximum 1 minute (30 * 2 seconds)
                            Log.d(TAG, "Email not yet verified, checking again in 2 seconds...");
                            handler.postDelayed(this, 2000);
                        } else {
                            progressDialog.dismiss();
                            Log.w(TAG, "Email verification timeout");
                            Toast.makeText(SignupActivity.this,
                                    "Email verification timeout. Please check your email and try logging in.",
                                    Toast.LENGTH_LONG).show();
                        }
                    } else {
                        progressDialog.dismiss();
                        Log.e(TAG, "Failed to reload user", task.getException());
                        Toast.makeText(SignupActivity.this,
                                "Failed to check email verification: " +
                                        (task.getException() != null ? task.getException().getMessage() : "Unknown error"),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        };
        handler.post(checkVerification);
    }
    */

    /*
    private void startPhoneVerification() {
        progressDialog.setMessage("Sending verification code...");
        progressDialog.show();

        String phoneNumber = tempPhone;
        Log.d(TAG, "Starting phone verification for: " + phoneNumber);

        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                     public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                        Log.d(TAG, "Phone verification completed automatically");
                        progressDialog.dismiss();
                        signInWithPhoneAuthCredential(credential);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        progressDialog.dismiss();
                        Log.e(TAG, "Phone verification failed", e);
                        Toast.makeText(SignupActivity.this,
                                "Verification failed: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String vId,
                                           @NonNull PhoneAuthProvider.ForceResendingToken token) {
                        progressDialog.dismiss();
                        verificationId = vId;
                        resendToken = token;

                        Log.d(TAG, "Verification code sent successfully");

                        // Navigate to OTPActivity with all necessary data
                        Intent intent = new Intent(SignupActivity.this, OTPActivity.class);
                        intent.putExtra("verificationId", verificationId);
                        intent.putExtra("phoneNumber", phoneNumber);
                        intent.putExtra("name", tempName);
                        intent.putExtra("email", tempEmail);
                        intent.putExtra("phone", tempPhone);
                        intent.putExtra("password", tempPassword);
                        intent.putExtra("isSignup", true);
                        startActivity(intent);
                        finish(); // Finish this activity
                    }
                })
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    */

    /*
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = task.getResult().getUser();
                        if (user != null) {
                            Log.d(TAG, "Phone authentication successful. User ID: " + user.getUid());
                            saveUserDataToFirestore(user.getUid());
                        }
                    } else {
                        progressDialog.dismiss();
                        Log.e(TAG, "Phone authentication failed", task.getException());
                        Toast.makeText(SignupActivity.this,
                                "Authentication failed: " +
                                        (task.getException() != null ? task.getException().getMessage() : "Unknown error"),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
    */

    private void saveUserDataToFirestore(String userId) {
        Log.d(TAG, "Saving user data to Firestore. User ID: " + userId);

        // Create user data map - more reliable than custom model
        Map<String, Object> userData = new HashMap<>();
        userData.put("userId", userId);
        userData.put("name", tempName);
        userData.put("email", tempEmail);
        userData.put("password", tempPassword); // Note: In production, don't store plain passwords
        userData.put("phone", tempPhone);
        userData.put("createdAt", System.currentTimeMillis());
        userData.put("isActive", true);

        Log.d(TAG, "User data prepared: " + userData.toString());

        firestore.collection("Users").document(userId)
                .set(userData)
                .addOnSuccessListener(aVoid -> {
                    progressDialog.dismiss();
                    Log.d(TAG, "User data saved successfully to Firestore");
                    Toast.makeText(SignupActivity.this,
                            "Registration successful! Welcome " + tempName,
                            Toast.LENGTH_SHORT).show();

                    // Navigate to MainActivity
                    Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Log.e(TAG, "Failed to save user data to Firestore", e);
                    Toast.makeText(SignupActivity.this,
                            "Failed to save user data: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();

                    // Optionally, you can retry or ask user to try again
                    // For now, we'll still navigate to MainActivity as the user is authenticated
                    Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                });
    }

    private boolean validate() {
        String name = name_edt.getText().toString().trim();
        String password = password_edt.getText().toString().trim();
        String confirmPassword = confirm_password_edt.getText().toString().trim();

        if (name.isEmpty()) {
            name_edt.setError("Enter Your Name");
            name_edt.requestFocus();
            return false;
        }

        // if (emailOrPhone.equals("email")) {
        String email = email_edt.getText().toString().trim();
        if (email.isEmpty()) {
            email_edt.setError("Enter Your Email");
            email_edt.requestFocus();
            return false;
        }
        if (!isValidEmail(email)) {
            email_edt.setError("Enter a valid Email address");
            email_edt.requestFocus();
            return false;
        }
        /*
        } else {
            String phone = phone_edt.getText().toString().trim();
            if (phone.isEmpty()) {
                phone_edt.setError("Enter Your Phone Number");
                phone_edt.requestFocus();
                return false;
            }
            // More flexible phone validation
            if (phone.length() < 10 || phone.length() > 15) {
                phone_edt.setError("Enter a valid phone number (10-15 digits)");
                phone_edt.requestFocus();
                return false;
            }
        }
        */

        if (password.isEmpty()) {
            password_edt.setError("Enter Password");
            password_edt.requestFocus();
            return false;
        }

        if (password.length() < 6) {
            password_edt.setError("Password must be at least 6 characters");
            password_edt.requestFocus();
            return false;
        }

        if (confirmPassword.isEmpty()) {
            confirm_password_edt.setError("Confirm Your Password");
            confirm_password_edt.requestFocus();
            return false;
        }

        if (!confirmPassword.equals(password)) {
            confirm_password_edt.setError("Passwords do not match");
            confirm_password_edt.requestFocus();
            return false;
        }

        return true;
    }

    private boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
        return pattern.matcher(email).matches();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}