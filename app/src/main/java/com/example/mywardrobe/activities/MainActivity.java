package com.example.mywardrobe.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.mywardrobe.R;
import com.example.mywardrobe.fragments.CalendarFragment;
import com.example.mywardrobe.fragments.CategoriesFragment;
import com.example.mywardrobe.fragments.OutfitsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    public final FragmentManager fragmentManager = getSupportFragmentManager();
    private BottomNavigationView bottomNavigationView;

    DrawerLayout dlDrawerLayout;
    NavigationView nvNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment;
                switch (menuItem.getItemId()) {
                    case R.id.action_clothes:
                        fragment = new CategoriesFragment();
                        break;
                    case R.id.action_outfits:
                        fragment = new OutfitsFragment();
                        break;
                    case R.id.action_calendar:
                    default:
                        fragment = new CalendarFragment();
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });
        // Set default menu item selection so that there is fragment showing, never empty
        bottomNavigationView.setSelectedItemId(R.id.action_clothes);

        dlDrawerLayout = findViewById(R.id.dlDrawerLayout);
        nvNavigationView = findViewById(R.id.nvNavigationView);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, dlDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        dlDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }
}