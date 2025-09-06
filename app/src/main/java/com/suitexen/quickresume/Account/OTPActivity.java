package com.suitexen.quickresume.Account;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.suitexen.quickresume.Activitys.MainActivity;
import com.suitexen.quickresume.R;

import in.aabhasjindal.otptextview.OtpTextView;

public class OTPActivity extends AppCompatActivity {

    private OtpTextView otpView;
    private Button verifyBtn;
    private TextView sendToNumberTxt;
    private ProgressDialog progressDialog;
    private String verificationId;
    private String phoneNumber;
    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_otpactivity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
        setupFirebase();
        setupClickListeners();
    }


    private void initializeViews() {
        otpView = findViewById(R.id.otp_view);
        verifyBtn = findViewById(R.id.verify_btn);
        sendToNumberTxt = findViewById(R.id.sendToNumberTxt);
        backBtn = findViewById(R.id.back_btn);

        backBtn.setOnClickListener(v -> finish());

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Verifying OTP...");
        progressDialog.setCancelable(false);

        // Get verificationId and phoneNumber from intent
        verificationId = getIntent().getStringExtra("verificationId");
        phoneNumber = getIntent().getStringExtra("phoneNumber");

        // Update the TextView with the phone number
        sendToNumberTxt.setText("Please enter the 6-digit code sent to " + phoneNumber);
    }

    private void setupFirebase() {
        // Firebase is already initialized in SignupActivity
    }

    private void setupClickListeners() {
        verifyBtn.setOnClickListener(v -> {
            String code = otpView.getOTP(); // Get OTP from OtpTextView
            if (code.isEmpty() || code.length() < 6) {
                Toast.makeText(this, "Please enter a valid OTP", Toast.LENGTH_SHORT).show();
                return;
            }

            progressDialog.show();
            verifyOTP(code);
        });
    }

    private void verifyOTP(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "OTP verified successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(OTPActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "OTP verification failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}