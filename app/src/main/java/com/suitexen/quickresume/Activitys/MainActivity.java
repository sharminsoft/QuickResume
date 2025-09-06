package com.suitexen.quickresume.Activitys;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.suitexen.quickresume.Fragments.HomeFragment;
import com.suitexen.quickresume.Fragments.TemplateFragment;
import com.suitexen.quickresume.R;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private FragmentManager fragmentManager;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private BottomNavigationView bottomNavigationView;

    // Track current fragment to avoid unnecessary reloading
    private String currentFragmentTag = "";
    private static final String HOME_FRAGMENT_TAG = "HOME_FRAGMENT";
    private static final String TEMPLATE_FRAGMENT_TAG = "TEMPLATE_FRAGMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Fix: Correct API level check (UPSIDE_DOWN_CAKE is API 34)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            EdgeToEdge.enable(this);
        }

        setContentView(R.layout.activity_main);

        // Apply window insets for edge-to-edge if supported
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // Initialize views
        initializeViews();

        // Load default fragment only if savedInstanceState is null
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment(), HOME_FRAGMENT_TAG);
            bottomNavigationView.setSelectedItemId(R.id.nav_home);
        } else {
            // Restore current fragment tag
            currentFragmentTag = savedInstanceState.getString("current_fragment_tag", HOME_FRAGMENT_TAG);
        }
    }

    private void initializeViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        fragmentManager = getSupportFragmentManager();

        // Setup navigation listeners
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    private void loadFragment(Fragment fragment, String tag) {
        // Avoid reloading the same fragment
        if (currentFragmentTag.equals(tag)) {
            return;
        }

        // Check if activity is in valid state for fragment transactions
        if (isFinishing() || isDestroyed()) {
            return;
        }

        try {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            // Add smooth animation
            transaction.setCustomAnimations(
                    android.R.anim.fade_in,
                    android.R.anim.fade_out
            );

            transaction.replace(R.id.fragment_container, fragment, tag);
            transaction.commitAllowingStateLoss(); // Use commitAllowingStateLoss to prevent crashes

            currentFragmentTag = tag;
        } catch (Exception e) {
            // Log error and fallback
            e.printStackTrace();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        // Handle bottom navigation item clicks
        if (itemId == R.id.nav_home) {
            loadFragment(new HomeFragment(), HOME_FRAGMENT_TAG);
            return true;
        } else if (itemId == R.id.nav_templates) {
            loadFragment(new TemplateFragment(), TEMPLATE_FRAGMENT_TAG);
            return true;
        } else if (itemId == R.id.nav_settings) {
            // Start settings activity
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);

            // Return false to keep current tab selected
            return false;
        }

        return false;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        // Reset to home when returning from any other activity
        if (!isFinishing() && !isDestroyed()) {
            // Post to handler to avoid fragment transaction issues
            bottomNavigationView.post(() -> {
                bottomNavigationView.setSelectedItemId(R.id.nav_home);
                loadFragment(new HomeFragment(), HOME_FRAGMENT_TAG);
            });
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save current fragment tag to restore on configuration change
        outState.putString("current_fragment_tag", currentFragmentTag);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clean up references to avoid memory leaks
        mAuth = null;
        firestore = null;
        bottomNavigationView = null;
        fragmentManager = null;
    }
}